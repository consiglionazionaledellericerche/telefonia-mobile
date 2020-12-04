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
import it.cnr.si.domain.Telefono;
import it.cnr.si.domain.TelefonoServizi;
import it.cnr.si.repository.TelefonoRepository;
import it.cnr.si.repository.TelefonoServiziRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.TelefonoService;
import it.cnr.si.service.TelefonoServiziService;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing TelefonoServizi.
 */
@RestController
@RequestMapping("/api")
public class TelefonoServiziResource {
    private static final String ENTITY_NAME = "telefonoServizi";
    private final Logger log = LoggerFactory.getLogger(TelefonoServiziResource.class);
    @Autowired
    private TelefonoRepository telefonoRepository;
    @Autowired
    private TelefonoService telefonoService;
    @Autowired
    private TelefonoServiziRepository telefonoServiziRepository;
    @Autowired
    private TelefonoServiziService telefonoServiziService;

//    public TelefonoServiziResource(TelefonoServiziRepository telefonoServiziRepository) {
//        this.telefonoServiziRepository = telefonoServiziRepository;
//    }

    /**
     * POST  /telefono-servizis : Create a new telefonoServizi.
     *
     * @param telefonoServizi the telefonoServizi to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telefonoServizi, or with status 400 (Bad Request) if the telefonoServizi has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telefono-servizis")
    @Timed
    public ResponseEntity<TelefonoServizi> createTelefonoServizi(@Valid @RequestBody TelefonoServizi telefonoServizi) throws URISyntaxException {
        log.debug("REST request to save TelefonoServizi : {}", telefonoServizi);
        if (telefonoServizi.getId() != null) {
            throw new BadRequestAlertException("A new telefonoServizi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        telefonoServiziService.controlloDate(telefonoServizi);
        TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
        telefonoService.salvabackground(telefonoServizi.getTelefono(), "MODIFICATO SERVIZI ");
        return ResponseEntity.created(new URI("/api/telefono-servizis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefono-servizis : Updates an existing telefonoServizi.
     *
     * @param telefonoServizi the telefonoServizi to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telefonoServizi,
     * or with status 400 (Bad Request) if the telefonoServizi is not valid,
     * or with status 500 (Internal Server Error) if the telefonoServizi couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telefono-servizis")
    @Timed
    public ResponseEntity<TelefonoServizi> updateTelefonoServizi(@Valid @RequestBody TelefonoServizi telefonoServizi) throws URISyntaxException {
        log.debug("REST request to update TelefonoServizi : {}", telefonoServizi);
        if (telefonoServizi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(telefonoServizi.getTelefono().getIntestatarioContratto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        telefonoServiziService.controlloDate(telefonoServizi);
        TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefonoServizi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefono-servizis : get all the telefonoServizis.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of telefonoServizis in body
     */
    @GetMapping("/telefono-servizis")
    @Timed
    public ResponseEntity<List<TelefonoServizi>> getAllTelefonoServizis(Pageable pageable) {
        log.debug("REST request to get a page of TelefonoServizis");
        List<String> cdSUO = SecurityUtils.getCdSUO();

        Page<TelefonoServizi> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            page = telefonoServiziRepository.findAllActive(false, pageable);
        else
            page = telefonoServiziRepository.findByIntestatarioContrattoIn(cdSUO, false, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telefono-servizis");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /telefono-servizis/:id : get the "id" telefonoServizi.
     *
     * @param id the id of the telefonoServizi to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telefonoServizi, or with status 404 (Not Found)
     */
    @GetMapping("/telefono-servizis/{id}")
    @Timed
    public ResponseEntity<TelefonoServizi> getTelefonoServizi(@PathVariable Long id) {
        log.debug("REST request to get TelefonoServizi : {}", id);
        Optional<TelefonoServizi> telefonoServizi = telefonoServiziRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(telefonoServizi);
    }

    /**
     * DELETE  /telefono-servizis/:id : delete the "id" telefonoServizi.
     *
     * @param id the id of the telefonoServizi to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telefono-servizis/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelefonoServizi(@PathVariable Long id) {
        log.debug("REST request to delete TelefonoServizi : {}", id);
        final Optional<TelefonoServizi> telefonoServizi = telefonoServiziRepository.findById(id);
        if (!telefonoServizi.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<String> cdSUO = SecurityUtils.getCdSUO();

        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(telefonoServizi.get().getTelefono().getIntestatarioContratto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        telefonoServiziRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //Per richiamare Telefoni
    @GetMapping("/telefono-servizis/findTelefono")
    @Timed
    public ResponseEntity<List<Telefono>> findTelefono() {
        List<String> cdSUO = SecurityUtils.getCdSUO();
        List<Telefono> telefoni;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            telefoni = telefonoRepository.findByDeletedFalse();
        else
            telefoni = telefonoRepository.findByIntestatarioContrattoInAndDeleted(cdSUO, false);
        return ResponseEntity.ok(telefoni);
    }

}
