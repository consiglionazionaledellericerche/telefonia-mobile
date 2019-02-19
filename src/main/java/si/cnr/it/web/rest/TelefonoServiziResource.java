package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.service.AceService;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import si.cnr.it.domain.Telefono;
import si.cnr.it.domain.TelefonoServizi;
import si.cnr.it.repository.TelefonoRepository;
import si.cnr.it.repository.TelefonoServiziRepository;
import si.cnr.it.security.AuthoritiesConstants;
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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing TelefonoServizi.
 */
@RestController
@RequestMapping("/api")
public class TelefonoServiziResource {

    @Autowired
    private AceService ace;

    @Autowired
    private TelefonoRepository telefonoRepository;

    private TelefonoResource telefonoResource;

    private SecurityUtils securityUtils;

    private final Logger log = LoggerFactory.getLogger(TelefonoServiziResource.class);

    private static final String ENTITY_NAME = "telefonoServizi";

    private final TelefonoServiziRepository telefonoServiziRepository;

    public TelefonoServiziResource(TelefonoServiziRepository telefonoServiziRepository) {
        this.telefonoServiziRepository = telefonoServiziRepository;
    }

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
        TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
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
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<TelefonoServizi> updateTelefonoServizi(@Valid @RequestBody TelefonoServizi telefonoServizi) throws URISyntaxException {
        log.debug("REST request to update TelefonoServizi : {}", telefonoServizi);
        if (telefonoServizi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getDenominazione(); //sede di username
//        String sede_cdsuoUser = ace.getPersonaByUsername("gaetana.irrera").getSede().getCdsuo(); //sede_cds di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getSigla(); //sigla di username
//        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
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
            String t = telefonoServizi.getTelefono().getIntestatarioContratto();
            hasPermission = sede_user.equals(t);
        }
     //   System.out.print("Che valore hai true o false? "+hasPermission);
        if (hasPermission) {
            TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefonoServizi.getId().toString()))
                .body(result);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        TelefonoServizi result = telefonoServiziRepository.save(telefonoServizi);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefonoServizi.getId().toString()))
//            .body(result);
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



//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getDenominazione(); //sede di username
//        String sede_cdsuoUser = ace.getPersonaByUsername("gaetana.irrera").getSede().getCdsuo(); //sede_cds di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getSigla(); //sigla di username
//        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
        String sede_user = telefonoResource.getSedeUser(); //sigla di username
        String sede_cdsuoUser = telefonoResource.getCdsUser(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds

        Page<TelefonoServizi> page;
        if (cds.equals("000"))
            page = telefonoServiziRepository.findAll(pageable);
        else
            page = telefonoServiziRepository.findByIntestatarioContratto(sede_user, pageable);

        /**Prova Valerio
        // System.out.println("TI TROVO = "+securityUtils.getCurrentUserLogin().get()); username
        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
        // System.out.println(sede_cdsuoUser+" - "+sede_user);
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds
        // System.out.println(cds);
        String vedetutto = "0";
        Iterator i = page.iterator();


         //cds = "002"; //forzo cds
         //sede_user = "ISTITUTO AMBIENTE MARINO E COSTIERO"; //forzo denominazione

        while(i.hasNext()){
            Telefono telefono = (Telefono) i.next();

            if(cds.equals("000")){
                vedetutto = "1";
            }
            else if(!telefono.getIstitutoTelefono().equals(sede_user) && vedetutto.equals("0")){
                i.remove();
            }

        }
        Fine Prova Valerio */


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
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Void> deleteTelefonoServizi(@PathVariable Long id) {
        log.debug("REST request to delete TelefonoServizi : {}", id);

        telefonoServiziRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //Per richiamare Telefoni
    @GetMapping("/telefono-servizis/findTelefono")
    @Timed
    public ResponseEntity<List<Telefono>> findTelefono() {

        List<Telefono> telefoni;

//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getDenominazione(); //sede di username
//        String sede_cdsuoUser = ace.getPersonaByUsername("gaetana.irrera").getSede().getCdsuo(); //sede_cds di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getSigla(); //sigla di username
//        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
        String sede_user = telefonoResource.getSedeUser(); //sigla di username
        String sede_cdsuoUser = telefonoResource.getCdsUser(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds

        if (cds.equals("000"))
            telefoni = telefonoRepository.findAll();
        else
            telefoni = telefonoRepository.findByIntestatarioContratto(sede_user);


        return ResponseEntity.ok(telefoni);
    }

}
