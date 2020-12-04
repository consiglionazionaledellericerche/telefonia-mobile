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
import it.cnr.si.repository.TelefonoRepository;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.AceService;
import it.cnr.si.service.CacheService;
import it.cnr.si.service.TelefonoService;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDtoForGerarchia;
import it.cnr.si.service.dto.anagrafica.scritture.UtenteDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleUtenteWebDto;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ch.qos.logback.core.encoder.ByteArrayUtil.hexStringToByteArray;

/**
 * REST controller for managing Telefono.
 */
@RestController
@RequestMapping("/api")
public class TelefonoResource {

    private static final String ENTITY_NAME = "telefono";
    private final Logger log = LoggerFactory.getLogger(TelefonoResource.class);
    private final TelefonoRepository telefonoRepository;

    private final TelefonoService telefonoService;
    private final AceService ace;
    private final CacheService cacheService;
    private List<EntitaOrganizzativaWebDtoForGerarchia> ist;

    public TelefonoResource(TelefonoRepository telefonoRepository,
                            TelefonoService telefonoService,
                            AceService ace,
                            CacheService cacheService) {
        this.telefonoRepository = telefonoRepository;
        this.telefonoService = telefonoService;
        this.ace = ace;
        this.cacheService = cacheService;
    }

    /**
     * POST  /telefonos : Create a new telefono.
     *
     * @param telefono the telefono to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telefono, or with status 400 (Bad Request) if the telefono has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telefonos")
    @Timed
    public ResponseEntity<Telefono> createTelefono(@Valid @RequestBody Telefono telefono) throws URISyntaxException {
        log.debug("REST request to save Telefono : {}", telefono);
        if (telefono.getId() != null) {
            throw new BadRequestAlertException("A new telefono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        telefonoService.controlloDate(telefono);
        telefono.setNumeroContratto("");
        byte[] b = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        telefono.setContratto(b);
        telefono.setContrattoContentType("");
        Telefono result = telefonoRepository.save(telefono);
        String stato = "INSERITO";
        telefonoService.salvabackground(telefono, stato);
        return ResponseEntity.created(new URI("/api/telefonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefonos : Updates an existing telefono.
     *
     * @param telefono the telefono to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telefono,
     * or with status 400 (Bad Request) if the telefono is not valid,
     * or with status 500 (Internal Server Error) if the telefono couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telefonos")
    @Timed
    public ResponseEntity<Telefono> updateTelefono(@Valid @RequestBody Telefono telefono) throws URISyntaxException {
        log.debug("REST request to update Telefono : {}", telefono);

        if (telefono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) &&
            !cdSUO.contains(telefono.getIntestatarioContratto())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        telefonoService.controlloDate(telefono);
        telefono.setNumeroContratto("");
        byte[] b = hexStringToByteArray("e04fd020ea3a6910a2d808002b30309d");
        telefono.setContratto(b);
        telefono.setContrattoContentType("");
        Telefono result = telefonoRepository.save(telefono);
        String stato = "MODIFICATO";
        telefonoService.salvabackground(telefono, stato);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefono.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefonos : get all the telefonos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of telefonos in body
     */
    @GetMapping("/telefonos")
    @Timed
    public ResponseEntity<List<Telefono>> getAllTelefonos(Pageable pageable) {
        log.debug("REST request to get a page of Telefonos");
        List<String> cdSUO = SecurityUtils.getCdSUO();
        Page<Telefono> telefoni;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            telefoni = telefonoRepository.findByDeletedFalse(pageable);
        } else {
            telefoni = telefonoRepository.findByIntestatarioContrattoInAndDeleted(cdSUO, false, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(telefoni, "/api/telefonos");
        return new ResponseEntity<>(telefoni.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /telefonos/:id : get the "id" telefono.
     *
     * @param id the id of the telefono to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telefono, or with status 404 (Not Found)
     */
    @GetMapping("/telefonos/{id}")
    @Timed
    public ResponseEntity<Telefono> getTelefono(@PathVariable Long id) {
        log.debug("REST request to get Telefono : {}", id);
        Optional<Telefono> telefono = telefonoRepository.findById(id);
        if (!telefono.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(telefono.get().getIntestatarioContratto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseUtil.wrapOrNotFound(telefono);
    }

    /**
     * DELETE  /telefonos/:id : delete the "id" telefono.
     *
     * @param id the id of the telefono to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telefonos/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelefono(@PathVariable Long id) {
        log.debug("REST request to delete Telefono : {}", id);

        Optional<Telefono> telefono = telefonoRepository.findById(id);
        if (!telefono.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        List<String> cdSUO = SecurityUtils.getCdSUO();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN) ||
            cdSUO.contains(telefono.get().getIntestatarioContratto()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Telefono tel = telefono.get();
        tel.setDeleted(true);
        tel.setDeletedNote("USER = " + SecurityUtils.getCurrentUserLogin() + " DATA = " + LocalDateTime.now());
        telefonoRepository.save(tel);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    //Per richiamare utenze ACE
    @GetMapping("/telefonos/findUtenza/{term}")
    @Timed
    public ResponseEntity<List<String>> findPersona(@PathVariable String term) {
        return ResponseEntity.ok(
            ace.searchUtenti(
                Stream.of(
                    new AbstractMap.SimpleEntry<>("page", "0"),
                    new AbstractMap.SimpleEntry<>("offset", "20"),
                    new AbstractMap.SimpleEntry<>("username", term)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            )
                .stream()
                .filter(utenteDto -> Optional.ofNullable(utenteDto.getUsername()).isPresent())
                .map(SimpleUtenteWebDto::getUsername)
                .collect(Collectors.toList()));
    }

    //Per richiamare istituti ACE
    @GetMapping("/telefonos/getIstituti")
    @Timed
    public ResponseEntity<List<SimpleEntitaOrganizzativaWebDto>> findIstituto() {
        List<String> cdSUO = SecurityUtils.getCdSUO();
        return ResponseEntity.ok(cacheService.getSediDiLavoro()
            .stream()
            .filter(entitaOrganizzativa -> Optional.ofNullable(entitaOrganizzativa.getCdsuo()).isPresent())
            .filter(entitaOrganizzativaWebDto -> {
                if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
                    return true;
                } else {
                    return cdSUO.contains(entitaOrganizzativaWebDto.getCdsuo());
                }
            })
            .sorted((i1, i2) -> i1.getCdsuo().compareTo(i2.getCdsuo()))
            .map(i -> {
                i.setDenominazione(i.getDenominazione().toUpperCase());
                return i;
            })
            .collect(Collectors.toList()));
    }

}
