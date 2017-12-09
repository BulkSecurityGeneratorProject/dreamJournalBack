package pl.teneusz.dream.journal.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.teneusz.dream.journal.domain.UserDetails;

import pl.teneusz.dream.journal.repository.UserDetailsRepository;
import pl.teneusz.dream.journal.repository.search.UserDetailsSearchRepository;
import pl.teneusz.dream.journal.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing UserDetails.
 */
@RestController
@RequestMapping("/api")
public class UserDetailsResource {

    private final Logger log = LoggerFactory.getLogger(UserDetailsResource.class);

    private static final String ENTITY_NAME = "userDetails";

    private final UserDetailsRepository userDetailsRepository;

    private final UserDetailsSearchRepository userDetailsSearchRepository;
    public UserDetailsResource(UserDetailsRepository userDetailsRepository, UserDetailsSearchRepository userDetailsSearchRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsSearchRepository = userDetailsSearchRepository;
    }

    /**
     * POST  /user-details : Create a new userDetails.
     *
     * @param userDetails the userDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userDetails, or with status 400 (Bad Request) if the userDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/user-details")
    @Timed
    public ResponseEntity<UserDetails> createUserDetails(@RequestBody UserDetails userDetails) throws URISyntaxException {
        log.debug("REST request to save UserDetails : {}", userDetails);
        if (userDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new userDetails cannot already have an ID")).body(null);
        }
        UserDetails result = userDetailsRepository.save(userDetails);
        userDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-details : Updates an existing userDetails.
     *
     * @param userDetails the userDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userDetails,
     * or with status 400 (Bad Request) if the userDetails is not valid,
     * or with status 500 (Internal Server Error) if the userDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/user-details")
    @Timed
    public ResponseEntity<UserDetails> updateUserDetails(@RequestBody UserDetails userDetails) throws URISyntaxException {
        log.debug("REST request to update UserDetails : {}", userDetails);
        if (userDetails.getId() == null) {
            return createUserDetails(userDetails);
        }
        UserDetails result = userDetailsRepository.save(userDetails);
        userDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, userDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-details : get all the userDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userDetails in body
     */
    @GetMapping("/user-details")
    @Timed
    public List<UserDetails> getAllUserDetails() {
        log.debug("REST request to get all UserDetails");
        return userDetailsRepository.findAll();
        }

    /**
     * GET  /user-details/:id : get the "id" userDetails.
     *
     * @param id the id of the userDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userDetails, or with status 404 (Not Found)
     */
    @GetMapping("/user-details/{id}")
    @Timed
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable Long id) {
        log.debug("REST request to get UserDetails : {}", id);
        UserDetails userDetails = userDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(userDetails));
    }

    /**
     * DELETE  /user-details/:id : delete the "id" userDetails.
     *
     * @param id the id of the userDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        log.debug("REST request to delete UserDetails : {}", id);
        userDetailsRepository.delete(id);
        userDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/user-details?query=:query : search for the userDetails corresponding
     * to the query.
     *
     * @param query the query of the userDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/user-details")
    @Timed
    public List<UserDetails> searchUserDetails(@RequestParam String query) {
        log.debug("REST request to search UserDetails for query {}", query);
        return StreamSupport
            .stream(userDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
