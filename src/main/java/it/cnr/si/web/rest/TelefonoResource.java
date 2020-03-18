package it.cnr.si.web.rest;


import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import it.cnr.si.domain.*;
import it.cnr.si.repository.*;
import it.cnr.si.security.AuthoritiesConstants;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.service.AceService;
import it.cnr.si.service.CacheService;
import it.cnr.si.service.TelefonoService;
import it.cnr.si.service.dto.anagrafica.base.PageDto;
import it.cnr.si.service.dto.anagrafica.base.Select2Dto;
import it.cnr.si.service.dto.anagrafica.base.Select2Item;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDtoForGerarchia;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        String sede = SecurityUtils.getCdS();
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN) &&
            !telefono.getIntestatarioContratto().startsWith(sede)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
        String sede = SecurityUtils.getCdS();
        Page<Telefono> telefoni;
        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN)) {
            telefoni = telefonoRepository.findByDeletedFalse(pageable);
        } else {
            telefoni = telefonoRepository.findByIntestatarioContrattoStartsWithAndDeleted(sede, false, pageable);
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

        String sede = SecurityUtils.getCdS();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN) ||
            telefono.get().getIntestatarioContratto().startsWith(sede))) {
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

        String sede = SecurityUtils.getCdS();
        if (!(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN) || telefono.get().getIntestatarioContratto().startsWith(sede))) {
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
            ace.getPersone(
                Stream.of(
                    new AbstractMap.SimpleEntry<>("page", "0"),
                    new AbstractMap.SimpleEntry<>("offset", "20"),
                    new AbstractMap.SimpleEntry<>("term", term)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            )
            .getItems()
            .stream()
            .filter(personaWebDto -> Optional.ofNullable(personaWebDto.getUsername()).isPresent())
            .map(PersonaWebDto::getUsername)
            .collect(Collectors.toList()));
    }

    //Per richiamare istituti ACE
    @GetMapping("/telefonos/getIstituti")
    @Timed
    public ResponseEntity<List<EntitaOrganizzativaWebDto>> findIstituto() {
        String sede = SecurityUtils.getCdS();
        return ResponseEntity.ok(cacheService.getSediDiLavoro()
            .stream()
            .filter(entitaOrganizzativa -> Optional.ofNullable(entitaOrganizzativa.getCdsuo()).isPresent())
            .filter(entitaOrganizzativaWebDto -> {
                if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.SUPERUSER, AuthoritiesConstants.ADMIN)) {
                    return true;
                } else {
                    return entitaOrganizzativaWebDto.getCdsuo().startsWith(sede);
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
