/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import it.cnr.si.domain.Servizi;
import it.cnr.si.repository.ServiziRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Servizi.
 */
@RestController
@RequestMapping("/api")
public class ServiziResource {

    private static final String ENTITY_NAME = "servizi";
    private final Logger log = LoggerFactory.getLogger(ServiziResource.class);
    private final ServiziRepository serviziRepository;

    public ServiziResource(ServiziRepository serviziRepository) {
        this.serviziRepository = serviziRepository;
    }

    /**
     * POST  /servizis : Create a new servizi.
     *
     * @param servizi the servizi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new servizi, or with status 400 (Bad Request) if the servizi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/servizis")
    @Secured({AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN})
    @Timed
    public ResponseEntity<Servizi> createServizi(@Valid @RequestBody Servizi servizi) throws URISyntaxException {
        log.debug("REST request to save Servizi : {}", servizi);
        if (servizi.getId() != null) {
            throw new BadRequestAlertException("A new servizi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Servizi result = serviziRepository.save(servizi);
        return ResponseEntity.created(new URI("/api/servizis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servizis : Updates an existing servizi.
     *
     * @param servizi the servizi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated servizi,
     * or with status 400 (Bad Request) if the servizi is not valid,
     * or with status 500 (Internal Server Error) if the servizi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/servizis")
    @Secured({AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN})
    @Timed
    public ResponseEntity<Servizi> updateServizi(@Valid @RequestBody Servizi servizi) throws URISyntaxException {
        log.debug("REST request to update Servizi : {}", servizi);
        if (servizi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Servizi result = serviziRepository.save(servizi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, servizi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servizis : get all the servizis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of servizis in body
     */
    @GetMapping("/servizis")
    @Timed
    public ResponseEntity<List<Servizi>> getAllServizis(Pageable pageable) {
        log.debug("REST request to get a page of Servizis");
        Page<Servizi> page = serviziRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/servizis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /servizis/:id : get the "id" servizi.
     *
     * @param id the id of the servizi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the servizi, or with status 404 (Not Found)
     */
    @GetMapping("/servizis/{id}")
    @Timed
    public ResponseEntity<Servizi> getServizi(@PathVariable Long id) {
        log.debug("REST request to get Servizi : {}", id);
        Optional<Servizi> servizi = serviziRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(servizi);
    }

    /**
     * DELETE  /servizis/:id : delete the "id" servizi.
     *
     * @param id the id of the servizi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/servizis/{id}")
    @Secured({AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN})
    @Timed
    public ResponseEntity<Void> deleteServizi(@PathVariable Long id) {
        log.debug("REST request to delete Servizi : {}", id);

        serviziRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
