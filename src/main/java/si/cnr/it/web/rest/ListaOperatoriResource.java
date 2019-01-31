package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import si.cnr.it.domain.ListaOperatori;
import si.cnr.it.repository.ListaOperatoriRepository;
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
 * REST controller for managing ListaOperatori.
 */
@RestController
@RequestMapping("/api")
public class ListaOperatoriResource {

    private final Logger log = LoggerFactory.getLogger(ListaOperatoriResource.class);

    private static final String ENTITY_NAME = "listaOperatori";

    private final ListaOperatoriRepository listaOperatoriRepository;

    public ListaOperatoriResource(ListaOperatoriRepository listaOperatoriRepository) {
        this.listaOperatoriRepository = listaOperatoriRepository;
    }

    /**
     * POST  /lista-operatoris : Create a new listaOperatori.
     *
     * @param listaOperatori the listaOperatori to create
     * @return the ResponseEntity with status 201 (Created) and with body the new listaOperatori, or with status 400 (Bad Request) if the listaOperatori has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lista-operatoris")
    @Timed
    public ResponseEntity<ListaOperatori> createListaOperatori(@Valid @RequestBody ListaOperatori listaOperatori) throws URISyntaxException {
        log.debug("REST request to save ListaOperatori : {}", listaOperatori);
        if (listaOperatori.getId() != null) {
            throw new BadRequestAlertException("A new listaOperatori cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListaOperatori result = listaOperatoriRepository.save(listaOperatori);
        return ResponseEntity.created(new URI("/api/lista-operatoris/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lista-operatoris : Updates an existing listaOperatori.
     *
     * @param listaOperatori the listaOperatori to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated listaOperatori,
     * or with status 400 (Bad Request) if the listaOperatori is not valid,
     * or with status 500 (Internal Server Error) if the listaOperatori couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lista-operatoris")
    @Timed
    public ResponseEntity<ListaOperatori> updateListaOperatori(@Valid @RequestBody ListaOperatori listaOperatori) throws URISyntaxException {
        log.debug("REST request to update ListaOperatori : {}", listaOperatori);
        if (listaOperatori.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ListaOperatori result = listaOperatoriRepository.save(listaOperatori);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, listaOperatori.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lista-operatoris : get all the listaOperatoris.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of listaOperatoris in body
     */
    @GetMapping("/lista-operatoris")
    @Timed
    public ResponseEntity<List<ListaOperatori>> getAllListaOperatoris(Pageable pageable) {
        log.debug("REST request to get a page of ListaOperatoris");
        Page<ListaOperatori> page = listaOperatoriRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lista-operatoris");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lista-operatoris/:id : get the "id" listaOperatori.
     *
     * @param id the id of the listaOperatori to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the listaOperatori, or with status 404 (Not Found)
     */
    @GetMapping("/lista-operatoris/{id}")
    @Timed
    public ResponseEntity<ListaOperatori> getListaOperatori(@PathVariable Long id) {
        log.debug("REST request to get ListaOperatori : {}", id);
        Optional<ListaOperatori> listaOperatori = listaOperatoriRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(listaOperatori);
    }

    /**
     * DELETE  /lista-operatoris/:id : delete the "id" listaOperatori.
     *
     * @param id the id of the listaOperatori to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lista-operatoris/{id}")
    @Timed
    public ResponseEntity<Void> deleteListaOperatori(@PathVariable Long id) {
        log.debug("REST request to delete ListaOperatori : {}", id);

        listaOperatoriRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
