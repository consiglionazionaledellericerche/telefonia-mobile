package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import si.cnr.it.domain.StoricoTelefono;
import si.cnr.it.repository.StoricoTelefonoRepository;
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
 * REST controller for managing StoricoTelefono.
 */
@RestController
@RequestMapping("/api")
public class StoricoTelefonoResource {

    private final Logger log = LoggerFactory.getLogger(StoricoTelefonoResource.class);

    private static final String ENTITY_NAME = "storicoTelefono";

    private final StoricoTelefonoRepository storicoTelefonoRepository;

    public StoricoTelefonoResource(StoricoTelefonoRepository storicoTelefonoRepository) {
        this.storicoTelefonoRepository = storicoTelefonoRepository;
    }

    /**
     * POST  /storico-telefonos : Create a new storicoTelefono.
     *
     * @param storicoTelefono the storicoTelefono to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storicoTelefono, or with status 400 (Bad Request) if the storicoTelefono has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/storico-telefonos")
    @Timed
    public ResponseEntity<StoricoTelefono> createStoricoTelefono(@Valid @RequestBody StoricoTelefono storicoTelefono) throws URISyntaxException {
        log.debug("REST request to save StoricoTelefono : {}", storicoTelefono);
        if (storicoTelefono.getId() != null) {
            throw new BadRequestAlertException("A new storicoTelefono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoricoTelefono result = storicoTelefonoRepository.save(storicoTelefono);
        return ResponseEntity.created(new URI("/api/storico-telefonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storico-telefonos : Updates an existing storicoTelefono.
     *
     * @param storicoTelefono the storicoTelefono to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storicoTelefono,
     * or with status 400 (Bad Request) if the storicoTelefono is not valid,
     * or with status 500 (Internal Server Error) if the storicoTelefono couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/storico-telefonos")
    @Timed
    public ResponseEntity<StoricoTelefono> updateStoricoTelefono(@Valid @RequestBody StoricoTelefono storicoTelefono) throws URISyntaxException {
        log.debug("REST request to update StoricoTelefono : {}", storicoTelefono);
        if (storicoTelefono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoricoTelefono result = storicoTelefonoRepository.save(storicoTelefono);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storicoTelefono.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storico-telefonos : get all the storicoTelefonos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storicoTelefonos in body
     */
    @GetMapping("/storico-telefonos")
    @Timed
    public ResponseEntity<List<StoricoTelefono>> getAllStoricoTelefonos(Pageable pageable) {
        log.debug("REST request to get a page of StoricoTelefonos");
        Page<StoricoTelefono> page = storicoTelefonoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storico-telefonos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /storico-telefonos/:id : get the "id" storicoTelefono.
     *
     * @param id the id of the storicoTelefono to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storicoTelefono, or with status 404 (Not Found)
     */
    @GetMapping("/storico-telefonos/{id}")
    @Timed
    public ResponseEntity<StoricoTelefono> getStoricoTelefono(@PathVariable Long id) {
        log.debug("REST request to get StoricoTelefono : {}", id);
        Optional<StoricoTelefono> storicoTelefono = storicoTelefonoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(storicoTelefono);
    }

    /**
     * DELETE  /storico-telefonos/:id : delete the "id" storicoTelefono.
     *
     * @param id the id of the storicoTelefono to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/storico-telefonos/{id}")
    @Timed
    public ResponseEntity<Void> deleteStoricoTelefono(@PathVariable Long id) {
        log.debug("REST request to delete StoricoTelefono : {}", id);

        storicoTelefonoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    ///Visualizza query per anno

    @GetMapping("/storico-telefonos/vista")
    @Timed
    public ResponseEntity<List<StoricoTelefono>> getVista(Pageable pageable) {
        log.debug("REST request to get a page of Storico Telefono getVista");
        String versione = "FIRMATO DIRETTORE";
        Page<StoricoTelefono> page = storicoTelefonoRepository.findByVersione(versione, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telefonos/vista");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
