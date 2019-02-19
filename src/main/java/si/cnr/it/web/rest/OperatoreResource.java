package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.service.AceService;
import org.springframework.beans.factory.annotation.Autowired;
import si.cnr.it.domain.Operatore;
import si.cnr.it.domain.Telefono;
import si.cnr.it.repository.OperatoreRepository;
import si.cnr.it.repository.TelefonoRepository;
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

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Operatore.
 */
@RestController
@RequestMapping("/api")
public class OperatoreResource {

    @Autowired
    private AceService ace;

    @Autowired
    private TelefonoRepository telefonoRepository;

    private SecurityUtils securityUtils;

    private TelefonoResource telefonoResource;

    private final Logger log = LoggerFactory.getLogger(OperatoreResource.class);

    private static final String ENTITY_NAME = "operatore";

    private final OperatoreRepository operatoreRepository;

    public OperatoreResource(OperatoreRepository operatoreRepository) {
        this.operatoreRepository = operatoreRepository;
    }

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
        Operatore result = operatoreRepository.save(operatore);
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
        String sede_user = telefonoResource.getSedeUser(); //sigla di username
        String sede_cdsuoUser = telefonoResource.getCdsUser(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds

        String valoreCdsUoSede = sede_cdsuoUser+" - "+sede_user;

        /**
         * Codice che permette di salvare solo se sei
         * la persona corretta
         *
         */
        boolean hasPermission = false;

        if (cds.equals("000"))
            hasPermission = true;
        else {
            // TelefonoServizi t = telefonoServiziRepository.getOne(telefonoServizi.getId());
            String t = operatore.getTelefonoOperatore().getIntestatarioContratto();
            hasPermission = sede_user.equals(t);
        }
        //   System.out.print("Che valore hai true o false? "+hasPermission);
        if (hasPermission) {
            Operatore result = operatoreRepository.save(operatore);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operatore.getId().toString()))
                .body(result);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefonoServizi.getId().toString()))
//            .body(result);

//        Operatore result = operatoreRepository.save(operatore);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, operatore.getId().toString()))
//            .body(result);
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

        String sede_user = telefonoResource.getSedeUser(); //sigla di username
        String sede_cdsuoUser = telefonoResource.getCdsUser(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds

        Page<Operatore> page;
        if (cds.equals("000"))
            page = operatoreRepository.findAll(pageable);
        else
            page = operatoreRepository.findByIntestatarioContratto(sede_user, pageable);


//        Page<Operatore> page = operatoreRepository.findAll(pageable);
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

        String sede_user = telefonoResource.getSedeUser(); //sigla di username
        String sede_cdsuoUser = telefonoResource.getCdsUser(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds

        if (cds.equals("000"))
            telefoni = telefonoRepository.findAll();
        else
            telefoni = telefonoRepository.findByIntestatarioContratto(sede_user);

        System.out.println("TELEFONI ===== "+telefoni+" cds ==== "+cds+" sede_user ===== "+sede_user);

        return ResponseEntity.ok(telefoni);
    }
}
