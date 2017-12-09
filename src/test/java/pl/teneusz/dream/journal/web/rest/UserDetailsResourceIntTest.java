package pl.teneusz.dream.journal.web.rest;

import pl.teneusz.dream.journal.DreamJournalApp;

import pl.teneusz.dream.journal.domain.UserDetails;
import pl.teneusz.dream.journal.repository.UserDetailsRepository;
import pl.teneusz.dream.journal.repository.search.UserDetailsSearchRepository;
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

import pl.teneusz.dream.journal.domain.enumeration.GenderEnum;
/**
 * Test class for the UserDetailsResource REST controller.
 *
 * @see UserDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DreamJournalApp.class)
public class UserDetailsResourceIntTest {

    private static final GenderEnum DEFAULT_GENDER = GenderEnum.MALE;
    private static final GenderEnum UPDATED_GENDER = GenderEnum.FEMALE;

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserDetailsSearchRepository userDetailsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserDetailsMockMvc;

    private UserDetails userDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserDetailsResource userDetailsResource = new UserDetailsResource(userDetailsRepository, userDetailsSearchRepository);
        this.restUserDetailsMockMvc = MockMvcBuilders.standaloneSetup(userDetailsResource)
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
    public static UserDetails createEntity(EntityManager em) {
        UserDetails userDetails = new UserDetails()
            .gender(DEFAULT_GENDER)
            .birthDate(DEFAULT_BIRTH_DATE);
        return userDetails;
    }

    @Before
    public void initTest() {
        userDetailsSearchRepository.deleteAll();
        userDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserDetails() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // Create the UserDetails
        restUserDetailsMockMvc.perform(post("/api/user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testUserDetails.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);

        // Validate the UserDetails in Elasticsearch
        UserDetails userDetailsEs = userDetailsSearchRepository.findOne(testUserDetails.getId());
        assertThat(userDetailsEs).isEqualToComparingFieldByField(testUserDetails);
    }

    @Test
    @Transactional
    public void createUserDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userDetailsRepository.findAll().size();

        // Create the UserDetails with an existing ID
        userDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailsMockMvc.perform(post("/api/user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get all the userDetailsList
        restUserDetailsMockMvc.perform(get("/api/user-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))));
    }

    @Test
    @Transactional
    public void getUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);

        // Get the userDetails
        restUserDetailsMockMvc.perform(get("/api/user-details/{id}", userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userDetails.getId().intValue()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.birthDate").value(sameInstant(DEFAULT_BIRTH_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingUserDetails() throws Exception {
        // Get the userDetails
        restUserDetailsMockMvc.perform(get("/api/user-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);
        userDetailsSearchRepository.save(userDetails);
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Update the userDetails
        UserDetails updatedUserDetails = userDetailsRepository.findOne(userDetails.getId());
        updatedUserDetails
            .gender(UPDATED_GENDER)
            .birthDate(UPDATED_BIRTH_DATE);

        restUserDetailsMockMvc.perform(put("/api/user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserDetails)))
            .andExpect(status().isOk());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate);
        UserDetails testUserDetails = userDetailsList.get(userDetailsList.size() - 1);
        assertThat(testUserDetails.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testUserDetails.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);

        // Validate the UserDetails in Elasticsearch
        UserDetails userDetailsEs = userDetailsSearchRepository.findOne(testUserDetails.getId());
        assertThat(userDetailsEs).isEqualToComparingFieldByField(testUserDetails);
    }

    @Test
    @Transactional
    public void updateNonExistingUserDetails() throws Exception {
        int databaseSizeBeforeUpdate = userDetailsRepository.findAll().size();

        // Create the UserDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUserDetailsMockMvc.perform(put("/api/user-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userDetails)))
            .andExpect(status().isCreated());

        // Validate the UserDetails in the database
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);
        userDetailsSearchRepository.save(userDetails);
        int databaseSizeBeforeDelete = userDetailsRepository.findAll().size();

        // Get the userDetails
        restUserDetailsMockMvc.perform(delete("/api/user-details/{id}", userDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean userDetailsExistsInEs = userDetailsSearchRepository.exists(userDetails.getId());
        assertThat(userDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<UserDetails> userDetailsList = userDetailsRepository.findAll();
        assertThat(userDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUserDetails() throws Exception {
        // Initialize the database
        userDetailsRepository.saveAndFlush(userDetails);
        userDetailsSearchRepository.save(userDetails);

        // Search the userDetails
        restUserDetailsMockMvc.perform(get("/api/_search/user-details?query=id:" + userDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDetails.class);
        UserDetails userDetails1 = new UserDetails();
        userDetails1.setId(1L);
        UserDetails userDetails2 = new UserDetails();
        userDetails2.setId(userDetails1.getId());
        assertThat(userDetails1).isEqualTo(userDetails2);
        userDetails2.setId(2L);
        assertThat(userDetails1).isNotEqualTo(userDetails2);
        userDetails1.setId(null);
        assertThat(userDetails1).isNotEqualTo(userDetails2);
    }
}
