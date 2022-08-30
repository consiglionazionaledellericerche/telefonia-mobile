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
import it.cnr.si.domain.Operatore;
import it.cnr.si.domain.Telefono;
import it.cnr.si.repository.OperatoreRepository;
import it.cnr.si.repository.TelefonoRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.OperatoreService;
import it.cnr.si.service.TelefonoService;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import it.cnr.si.web.rest.util.HeaderUtil;
import it.cnr.si.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

/**
 * REST controller for managing Operatore.
 */
@RestController
@RequestMapping("/api")
public class OperatoreResource {

    private static final String ENTITY_NAME = "operatore";
    private final Logger log = LoggerFactory.getLogger(OperatoreResource.class);
    @Autowired
    private TelefonoRepository telefonoRepository;
    @Autowired
    private TelefonoService telefonoService;
    @Autowired
    private OperatoreRepository operatoreRepository;
    @Autowired
    private OperatoreService operatoreService;

    /**
     * POST  /operatores : Create a new operatore.
     *
     * @param operatore the operatore to create
     * @return the ResponseEntity with status 201 (Created) and with body the new operatore, or with status 400 (Bad Request) if the operatore has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/operatores")
    @Timed
    public ResponseEntity<Operatore> createOperatore(@Valid @RequestBody Operatore operatore) throws URISyntaxException {
        log.debug("REST request to save Operatore : {}", operatore);
        if (operatore.getId() != null) {
            throw new BadRequestAlertException("A new operatore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        operatoreService.controlloDate(operatore);
        //  operatoreService.controlloEsisteOperatore(operatore); //Non serve perchè va bene 1 a N
        /**  List<Operatore> op = operatoreRepository.findByTelefonoOperatore(operatore.getTelefonoOperatore());
         Iterator i = op.iterator();
         while (i.hasNext()) {
         Object o = i.next();
         if(operatore.getId().equals(((Operatore) o).getId())){
         throw new BadRequestAlertException("A new operatore cannot already have an ID", ENTITY_NAME, "idexists");
         }
         }*/
        Operatore result = operatoreRepository.save(operatore);
        telefonoService.salvabackground(operatore.getTelefonoOperatore(), "MODIFICATO OPERATORE ");
        return ResponseEntity.created(new URI("/api/operatores/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /operatores : Updates an existing operatore.
     *
     * @param operatore the operatore to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated operatore,
     * or with status 400 (Bad Request) if the operatore is not valid,
     * or with status 500 (Internal Server Error) if the operatore couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/operatores")
    @Timed
    public ResponseEntity<Operatore> updateOperatore(@Valid @RequestBody Operatore operatore) throws URISyntaxException {
        log.debug("REST request to update Operatore : {}", operatore);
        if (operatore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(operatore.getTelefonoOperatore().getIntestatarioContratto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        operatoreService.controlloDate(operatore);
        Operatore result = operatoreRepository.save(operatore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operatore.getId().toString()))
            .body(result);
    }

    /**
     * GET  /operatores : get all the operatores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of operatores in body
     */
    @GetMapping("/operatores")
    @Timed
    public ResponseEntity<List<Operatore>> getAllOperatores(Pageable pageable) {
        log.debug("REST request to get a page of Operatores");
        List<String> cdSUO = SecurityUtils.getCdSUO();

        Page<Operatore> page;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            page = operatoreRepository.findAllActive(false, pageable);
        else
            page = operatoreRepository.findByIntestatarioContrattoIn(cdSUO, false, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operatores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /operatores/:id : get the "id" operatore.
     *
     * @param id the id of the operatore to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the operatore, or with status 404 (Not Found)
     */
    @GetMapping("/operatores/{id}")
    @Timed
    public ResponseEntity<Operatore> getOperatore(@PathVariable Long id) {
        log.debug("REST request to get Operatore : {}", id);
        Optional<Operatore> operatore = operatoreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(operatore);
    }

    /**
     * DELETE  /operatores/:id : delete the "id" operatore.
     *
     * @param id the id of the operatore to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/operatores/{id}")
    @Timed
    public ResponseEntity<Void> deleteOperatore(@PathVariable Long id) {
        log.debug("REST request to delete Operatore : {}", id);

        operatoreRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //Per richiamare Telefoni
    @GetMapping("/operatores/findTelefono")
    @Timed
    public ResponseEntity<List<Telefono>> findTelefono() {
        List<Telefono> telefoni;
        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN))
            telefoni = telefonoRepository.findByDeletedFalse();
        else
            telefoni = telefonoRepository.findByIntestatarioContrattoInAndDeleted(cdSUO, false);

        return ResponseEntity.ok(telefoni);
    }

    //Per fare visualizzazione Annua telefoni attivi
    @GetMapping("/operatores/telefoniAnno/{anno}")
    @Timed
    public ResponseEntity<List<Operatore>> getTelefonoAnno(Pageable pageable, @PathVariable int anno) {
        log.debug("REST request to get a page of Operatores");

        Page<Operatore> pageOperatore = operatoreRepository.findAllActive(false, pageable);
        List<Operatore> listOperatore = new ArrayList<>();
        Integer annoInizio = null;
        Integer annoFine = null;
        for (Operatore operatore : pageOperatore.getContent()) {
            annoInizio = Optional.ofNullable(operatore.getData())
                .map(instant -> ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()))
                .map(ZonedDateTime::getYear)
                .orElse(Integer.MIN_VALUE);
            annoFine = Optional.ofNullable(operatore.getDataFine())
                .map(instant -> ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()))
                .map(ZonedDateTime::getYear)
                .orElse(Integer.MAX_VALUE);

            log.debug("annoFine: {}", annoFine);
            if (anno >= annoInizio && anno <= annoFine) {
                listOperatore.add(operatore);
            }
        }
        final Page<Operatore> page = new PageImpl<>(listOperatore, pageable, listOperatore.size());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/operatores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
