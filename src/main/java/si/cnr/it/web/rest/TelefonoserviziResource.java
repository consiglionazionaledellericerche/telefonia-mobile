package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import si.cnr.it.domain.Telefonoservizi;
import si.cnr.it.repository.TelefonoserviziRepository;
import si.cnr.it.web.rest.errors.BadRequestAlertException;
import si.cnr.it.web.rest.util.HeaderUtil;
import si.cnr.it.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Telefonoservizi.
 */
@RestController
@RequestMapping("/api")
public class TelefonoserviziResource {

    private final Logger log = LoggerFactory.getLogger(TelefonoserviziResource.class);

    private static final String ENTITY_NAME = "telefonoservizi";

    private final TelefonoserviziRepository telefonoserviziRepository;

    public TelefonoserviziResource(TelefonoserviziRepository telefonoserviziRepository) {
        this.telefonoserviziRepository = telefonoserviziRepository;
    }

    /**
     * POST  /telefonoservizis : Create a new telefonoservizi.
     *
     * @param telefonoservizi the telefonoservizi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telefonoservizi, or with status 400 (Bad Request) if the telefonoservizi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telefonoservizis")
    @Timed
    public ResponseEntity<Telefonoservizi> createTelefonoservizi(@Valid @RequestBody Telefonoservizi telefonoservizi) throws URISyntaxException {
        log.debug("REST request to save Telefonoservizi : {}", telefonoservizi);
        if (telefonoservizi.getId() != null) {
            throw new BadRequestAlertException("A new telefonoservizi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Telefonoservizi result = telefonoserviziRepository.save(telefonoservizi);
        return ResponseEntity.created(new URI("/api/telefonoservizis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefonoservizis : Updates an existing telefonoservizi.
     *
     * @param telefonoservizi the telefonoservizi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telefonoservizi,
     * or with status 400 (Bad Request) if the telefonoservizi is not valid,
     * or with status 500 (Internal Server Error) if the telefonoservizi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telefonoservizis")
    @Timed
    public ResponseEntity<Telefonoservizi> updateTelefonoservizi(@Valid @RequestBody Telefonoservizi telefonoservizi) throws URISyntaxException {
        log.debug("REST request to update Telefonoservizi : {}", telefonoservizi);
        if (telefonoservizi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Telefonoservizi result = telefonoserviziRepository.save(telefonoservizi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefonoservizi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefonoservizis : get all the telefonoservizis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of telefonoservizis in body
     */
    @GetMapping("/telefonoservizis")
    @Timed
    public ResponseEntity<List<Telefonoservizi>> getAllTelefonoservizis(Pageable pageable) {
        log.debug("REST request to get a page of Telefonoservizis");
        Page<Telefonoservizi> page = telefonoserviziRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telefonoservizis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /telefonoservizis/:id : get the "id" telefonoservizi.
     *
     * @param id the id of the telefonoservizi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telefonoservizi, or with status 404 (Not Found)
     */
    @GetMapping("/telefonoservizis/{id}")
    @Timed
    public ResponseEntity<Telefonoservizi> getTelefonoservizi(@PathVariable Long id) {
        log.debug("REST request to get Telefonoservizi : {}", id);
        Optional<Telefonoservizi> telefonoservizi = telefonoserviziRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(telefonoservizi);
    }

    /**
     * DELETE  /telefonoservizis/:id : delete the "id" telefonoservizi.
     *
     * @param id the id of the telefonoservizi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telefonoservizis/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelefonoservizi(@PathVariable Long id) {
        log.debug("REST request to delete Telefonoservizi : {}", id);

        telefonoserviziRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
