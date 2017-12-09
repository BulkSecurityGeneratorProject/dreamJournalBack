package pl.teneusz.dream.journal.web.rest;

import pl.teneusz.dream.journal.DreamJournalApp;

import pl.teneusz.dream.journal.domain.Dream;
import pl.teneusz.dream.journal.repository.DreamRepository;
import pl.teneusz.dream.journal.repository.UserRepository;
import pl.teneusz.dream.journal.repository.search.DreamSearchRepository;
import pl.teneusz.dream.journal.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static pl.teneusz.dream.journal.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DreamResource REST controller.
 *
 * @see DreamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DreamJournalApp.class)
public class DreamResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_LUCID = false;
    private static final Boolean UPDATED_IS_LUCID = true;

    private static final Integer DEFAULT_NIGHT_INDEX = 1;
    private static final Integer UPDATED_NIGHT_INDEX = 2;

    private static final Boolean DEFAULT_VISIBILITY = false;
    private static final Boolean UPDATED_VISIBILITY = true;

    private static final Integer DEFAULT_LENGHT = 5;
    private static final Integer UPDATED_LENGHT = 6;

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private DreamSearchRepository dreamSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private MockMvc restDreamMockMvc;

    private Dream dream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DreamResource dreamResource = new DreamResource(dreamRepository, dreamSearchRepository, userRepository);
        this.restDreamMockMvc = MockMvcBuilders.standaloneSetup(dreamResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dream createEntity(EntityManager em) {
        Dream dream = new Dream()
            .title(DEFAULT_TITLE)
            .createDate(DEFAULT_CREATE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .isLucid(DEFAULT_IS_LUCID)
            .nightIndex(DEFAULT_NIGHT_INDEX)
            .visibility(DEFAULT_VISIBILITY)
            .lenght(DEFAULT_LENGHT)
            .score(DEFAULT_SCORE);
        return dream;
    }

    @Before
    public void initTest() {
        dreamSearchRepository.deleteAll();
        dream = createEntity(em);
    }

    @Test
    @Transactional
    public void createDream() throws Exception {
        int databaseSizeBeforeCreate = dreamRepository.findAll().size();

        // Create the Dream
        restDreamMockMvc.perform(post("/api/dreams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dream)))
            .andExpect(status().isCreated());

        // Validate the Dream in the database
        List<Dream> dreamList = dreamRepository.findAll();
        assertThat(dreamList).hasSize(databaseSizeBeforeCreate + 1);
        Dream testDream = dreamList.get(dreamList.size() - 1);
        assertThat(testDream.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDream.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testDream.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDream.isIsLucid()).isEqualTo(DEFAULT_IS_LUCID);
        assertThat(testDream.getNightIndex()).isEqualTo(DEFAULT_NIGHT_INDEX);
        assertThat(testDream.isVisibility()).isEqualTo(DEFAULT_VISIBILITY);
        assertThat(testDream.getLenght()).isEqualTo(DEFAULT_LENGHT);
        assertThat(testDream.getScore()).isEqualTo(DEFAULT_SCORE);

        // Validate the Dream in Elasticsearch
        Dream dreamEs = dreamSearchRepository.findOne(testDream.getId());
        assertThat(dreamEs).isEqualToComparingFieldByField(testDream);
    }

    @Test
    @Transactional
    public void createDreamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dreamRepository.findAll().size();

        // Create the Dream with an existing ID
        dream.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDreamMockMvc.perform(post("/api/dreams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dream)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Dream> dreamList = dreamRepository.findAll();
        assertThat(dreamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDreams() throws Exception {
        // Initialize the database
        dreamRepository.saveAndFlush(dream);

        // Get all the dreamList
        restDreamMockMvc.perform(get("/api/dreams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dream.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isLucid").value(hasItem(DEFAULT_IS_LUCID.booleanValue())))
            .andExpect(jsonPath("$.[*].nightIndex").value(hasItem(DEFAULT_NIGHT_INDEX)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].lenght").value(hasItem(DEFAULT_LENGHT)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    public void getDream() throws Exception {
        // Initialize the database
        dreamRepository.saveAndFlush(dream);

        // Get the dream
        restDreamMockMvc.perform(get("/api/dreams/{id}", dream.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dream.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isLucid").value(DEFAULT_IS_LUCID.booleanValue()))
            .andExpect(jsonPath("$.nightIndex").value(DEFAULT_NIGHT_INDEX))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.booleanValue()))
            .andExpect(jsonPath("$.lenght").value(DEFAULT_LENGHT))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE));
    }

    @Test
    @Transactional
    public void getNonExistingDream() throws Exception {
        // Get the dream
        restDreamMockMvc.perform(get("/api/dreams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDream() throws Exception {
        // Initialize the database
        dreamRepository.saveAndFlush(dream);
        dreamSearchRepository.save(dream);
        int databaseSizeBeforeUpdate = dreamRepository.findAll().size();

        // Update the dream
        Dream updatedDream = dreamRepository.findOne(dream.getId());
        updatedDream
            .title(UPDATED_TITLE)
            .createDate(UPDATED_CREATE_DATE)
            .description(UPDATED_DESCRIPTION)
            .isLucid(UPDATED_IS_LUCID)
            .nightIndex(UPDATED_NIGHT_INDEX)
            .visibility(UPDATED_VISIBILITY)
            .lenght(UPDATED_LENGHT)
            .score(UPDATED_SCORE);

        restDreamMockMvc.perform(put("/api/dreams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDream)))
            .andExpect(status().isOk());

        // Validate the Dream in the database
        List<Dream> dreamList = dreamRepository.findAll();
        assertThat(dreamList).hasSize(databaseSizeBeforeUpdate);
        Dream testDream = dreamList.get(dreamList.size() - 1);
        assertThat(testDream.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDream.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testDream.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDream.isIsLucid()).isEqualTo(UPDATED_IS_LUCID);
        assertThat(testDream.getNightIndex()).isEqualTo(UPDATED_NIGHT_INDEX);
        assertThat(testDream.isVisibility()).isEqualTo(UPDATED_VISIBILITY);
        assertThat(testDream.getLenght()).isEqualTo(UPDATED_LENGHT);
        assertThat(testDream.getScore()).isEqualTo(UPDATED_SCORE);

        // Validate the Dream in Elasticsearch
        Dream dreamEs = dreamSearchRepository.findOne(testDream.getId());
        assertThat(dreamEs).isEqualToComparingFieldByField(testDream);
    }

    @Test
    @Transactional
    public void updateNonExistingDream() throws Exception {
        int databaseSizeBeforeUpdate = dreamRepository.findAll().size();

        // Create the Dream

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDreamMockMvc.perform(put("/api/dreams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dream)))
            .andExpect(status().isCreated());

        // Validate the Dream in the database
        List<Dream> dreamList = dreamRepository.findAll();
        assertThat(dreamList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDream() throws Exception {
        // Initialize the database
        dreamRepository.saveAndFlush(dream);
        dreamSearchRepository.save(dream);
        int databaseSizeBeforeDelete = dreamRepository.findAll().size();

        // Get the dream
        restDreamMockMvc.perform(delete("/api/dreams/{id}", dream.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dreamExistsInEs = dreamSearchRepository.exists(dream.getId());
        assertThat(dreamExistsInEs).isFalse();

        // Validate the database is empty
        List<Dream> dreamList = dreamRepository.findAll();
        assertThat(dreamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDream() throws Exception {
        // Initialize the database
        dreamRepository.saveAndFlush(dream);
        dreamSearchRepository.save(dream);

        // Search the dream
        restDreamMockMvc.perform(get("/api/_search/dreams?query=id:" + dream.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dream.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isLucid").value(hasItem(DEFAULT_IS_LUCID.booleanValue())))
            .andExpect(jsonPath("$.[*].nightIndex").value(hasItem(DEFAULT_NIGHT_INDEX)))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.booleanValue())))
            .andExpect(jsonPath("$.[*].lenght").value(hasItem(DEFAULT_LENGHT)))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dream.class);
        Dream dream1 = new Dream();
        dream1.setId(1L);
        Dream dream2 = new Dream();
        dream2.setId(dream1.getId());
        assertThat(dream1).isEqualTo(dream2);
        dream2.setId(2L);
        assertThat(dream1).isNotEqualTo(dream2);
        dream1.setId(null);
        assertThat(dream1).isNotEqualTo(dream2);
    }
}
