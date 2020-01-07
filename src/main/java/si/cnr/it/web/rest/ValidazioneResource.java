package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.service.AceService;
import org.springframework.beans.factory.annotation.Autowired;
import si.cnr.it.domain.Validazione;
import si.cnr.it.repository.ValidazioneRepository;
import si.cnr.it.security.SecurityUtils;
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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Validazione.
 */
@RestController
@RequestMapping("/api")
public class ValidazioneResource {

    @Autowired
    private TelefonoResource telefonoResource;

    @Autowired
    private AceService ace;

    private SecurityUtils securityUtils;

    private final Logger log = LoggerFactory.getLogger(ValidazioneResource.class);

    private static final String ENTITY_NAME = "validazione";

    private final ValidazioneRepository validazioneRepository;

    public ValidazioneResource(ValidazioneRepository validazioneRepository) {
        this.validazioneRepository = validazioneRepository;
    }

    /**
     * POST  /validaziones : Create a new validazione.
     *
     * @param validazione the validazione to create
     * @return the ResponseEntity with status 201 (Created) and with body the new validazione, or with status 400 (Bad Request) if the validazione has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/validaziones")
    @Timed
    public ResponseEntity<Validazione> createValidazione(@Valid @RequestBody Validazione validazione) throws URISyntaxException {
        log.debug("REST request to save Validazione : {}", validazione);
        if (validazione.getId() != null) {
            throw new BadRequestAlertException("A new validazione cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Validazione result = validazioneRepository.save(validazione);
        return ResponseEntity.created(new URI("/api/validaziones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /validaziones : Updates an existing validazione.
     *
     * @param validazione the validazione to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated validazione,
     * or with status 400 (Bad Request) if the validazione is not valid,
     * or with status 500 (Internal Server Error) if the validazione couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/validaziones")
    @Timed
    public ResponseEntity<Validazione> updateValidazione(@Valid @RequestBody Validazione validazione) throws URISyntaxException {
        log.debug("REST request to update Validazione : {}", validazione);
        if (validazione.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        telefonoResource.salvabackground(validazione.getValidazioneTelefono(), "FIRMATO DIRETTORE");
        validazione.setDataValidazione(ZonedDateTime.now(ZoneId.systemDefault()));
        validazione.setUserDirettore(ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getUsername());
        Validazione result = validazioneRepository.save(validazione);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, validazione.getId().toString()))
            .body(result);
    }

    /**
     * GET  /validaziones : get all the validaziones.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of validaziones in body
     */
    @GetMapping("/validaziones")
    @Timed
    public ResponseEntity<List<Validazione>> getAllValidaziones(Pageable pageable) {
        log.debug("REST request to get a page of Validaziones");
        String sede_user = telefonoResource.getSedeUser(); //sede di username
        String cds = telefonoResource.getCdsUser();
        Page<Validazione> page;
        if (cds.equals("000")) {
            page = validazioneRepository.findAll(pageable);
        } else {
            page = validazioneRepository.findByValidazioneTelefonoIntestatarioContratto(sede_user, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/validaziones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /validaziones/:id : get the "id" validazione.
     *
     * @param id the id of the validazione to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the validazione, or with status 404 (Not Found)
     */
    @GetMapping("/validaziones/{id}")
    @Timed
    public ResponseEntity<Validazione> getValidazione(@PathVariable Long id) {
        log.debug("REST request to get Validazione : {}", id);
        Optional<Validazione> val = validazioneRepository.findById(id);
        Optional<Validazione> validazione = validazioneRepository.findById(id);
        String s = null;
        if(val.isPresent()) {
            if (val.get().getUserDirettore() != null) {
                System.out.println("C'è firma direttore");
                validazione = validazioneRepository.findById((long) 0);
            }
        }

        /** ok ma non mi fa completare operazione
         * validazione.ifPresent( UserDirettore-> {
         System.out.println("User's name = " + UserDirettore.getUserDirettore());
         if (!UserDirettore.getUserDirettore().equals("")){
         System.out.println("C'è firma direttore");
         }
         else{
         System.out.println("Non c'è firma");
         }
         });*/
/**
 Boolean doc = validazione.get().getUserDirettore().isEmpty();
 log.debug("doc === ",doc);
 if(doc.equals("")){
 validazione = validazioneRepository.findById((long) 0);
 }*/
        return ResponseUtil.wrapOrNotFound(validazione);
    }

    /**
     * DELETE  /validaziones/:id : delete the "id" validazione.
     *
     * @param id the id of the validazione to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/validaziones/{id}")
    @Timed
    public ResponseEntity<Void> deleteValidazione(@PathVariable Long id) {
        log.debug("REST request to delete Validazione : {}", id);

        validazioneRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}
