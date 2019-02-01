package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.service.AceService;
import it.cnr.si.service.dto.anagrafica.base.PageDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.PersonaWebDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import si.cnr.it.domain.Telefono;
import si.cnr.it.repository.TelefonoRepository;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Telefono.
 */
@RestController
@RequestMapping("/api")
public class TelefonoResource {

    @Autowired
    private AceService ace;

    private SecurityUtils securityUtils;

//    private EntitaLocale entitaLocale;

    private final Logger log = LoggerFactory.getLogger(TelefonoResource.class);

    private static final String ENTITY_NAME = "telefono";

    private final TelefonoRepository telefonoRepository;

    public TelefonoResource(TelefonoRepository telefonoRepository) {
        this.telefonoRepository = telefonoRepository;
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
        if(telefono.getUtenzaTelefono().contains(".")){

        }
        else{
            return (ResponseEntity<Telefono>) ResponseEntity.unprocessableEntity();
        }
        Telefono result = telefonoRepository.save(telefono);
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
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Telefono> updateTelefono(@Valid @RequestBody Telefono telefono) throws URISyntaxException {
        log.debug("REST request to update Telefono : {}", telefono);

        if (telefono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getDenominazione(); //sede di username
//        String sede_cdsuoUser = ace.getPersonaByUsername("gaetana.irrera").getSede().getCdsuo(); //sede_cds di username
        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds
/**
 * Codice che permette di salvare solo se sei
 * la persona corretta
 *
 */
        boolean hasPermission = false;

        if (cds.equals("000"))
            hasPermission = true;
        else {
//            Telefono t = telefonoRepository.getOne(telefono.getId());
            String t = telefono.getIstitutoTelefono();
            hasPermission = sede_user.equals(t);
        }
//        System.out.print("Che valore hai true o false? "+hasPermission);
        if (hasPermission) {
            if(telefono.getUtenzaTelefono().contains(".")){

            }
            else{
                return (ResponseEntity<Telefono>) ResponseEntity.unprocessableEntity();
            }
            Telefono result = telefonoRepository.save(telefono);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telefono.getId().toString()))
                .body(result);
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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
//        Page<Telefono> page = telefonoRepository.findAll(pageable);
        /**Prova Valerio */
       // System.out.println("TI TROVO = "+securityUtils.getCurrentUserLogin().get()); username
        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
//       // System.out.println(sede_cdsuoUser+" - "+sede_user);
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds
       // System.out.println(cds);
//        String vedetutto = "0";
//        Iterator i = page.iterator();

        Page<Telefono> telefoni;
        if(cds.equals("000")) {
            telefoni = telefonoRepository.findAll(pageable);
        } else {
            telefoni = telefonoRepository.findByIstitutoTelefono(sede_user, pageable);
        }

//       /** cds = "002"; //forzo cds
//        sede_user = "ISTITUTO AMBIENTE MARINO E COSTIERO"; //forzo denominazione */
//
//        while(i.hasNext()){
//            Telefono telefono = (Telefono) i.next();
//
//            if(cds.equals("000")){
//                vedetutto = "1";
//            }
//            else if(!telefono.getIstitutoTelefono().equals(sede_user) && vedetutto.equals("0")){
//                i.remove();
//            }
//
//        }
//        /**Fine Prova Valerio */
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
       /** Prova Valerio per cdsuo  */
//            String cdsuo = telefono.get().getIstitutoTelefono();
//            String denominazione;
//            List<EntitaOrganizzativaWebDto> istituti = ace.listaIstitutiAttivi();
//
//            for (EntitaOrganizzativaWebDto istituto : istituti ) {
//                if(istituto.getCdsuo().equals(cdsuo))
//                    telefono.get().setIst(istituto.getCdsuo()+ " - " +istituto.getDenominazione());
//            }

       /** Fine Prova Valerio */
        return ResponseUtil.wrapOrNotFound(telefono);
    }

    /**
     * DELETE  /telefonos/:id : delete the "id" telefono.
     *
     * @param id the id of the telefono to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telefonos/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Void> deleteTelefono(@PathVariable Long id) {
        log.debug("REST request to delete Telefono : {}", id);

        telefonoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    //Per richiamare utenze ACE
    @GetMapping("/telefonos/findUtenza/{term}")
    @Timed
    public ResponseEntity<List<String>> findPersona(@PathVariable String term) {

        List<String> result = new ArrayList<>();

        Map<String, String> query = new HashMap<>();
        query.put("term", term);
        PageDto<PersonaWebDto> persone = ace.getPersone(query);
        List<PersonaWebDto> listaPersone = persone.getItems();

        for (PersonaWebDto persona : listaPersone ) {
            if ( persona.getUsername() != null)
                result.add(  persona.getUsername()  );
        }
//
//        listaPersone.stream()
//            .forEach(persona -> result.add(  persona.getUsername()  )  );
//
//
//
//        result = listaPersone.stream()
//            .filter( persona -> persona.getUsername() != null )
//            .map(persona -> persona.getUsername())
//            .collect(Collectors.toList()    );



        return ResponseEntity.ok(result);
    }


    //Per richiamare istituti ACE
    @GetMapping("/telefonos/getIstituti")
    @Timed
    public ResponseEntity<List<EntitaOrganizzativaWebDto>> findIstituto() {

       List<EntitaOrganizzativaWebDto> istituti = ace.listaIstitutiAttivi();
//         List<EntitaOrganizzativaWebDto> istituti = ace.entitaOrganizzativaFindByTerm("122000");

        istituti = istituti.stream()
            .sorted((i1, i2) -> i1.getDenominazione().compareTo(i2.getDenominazione()))
            .map(i -> {
//                i.setDenominazione(i.getCdsuo()+" - "+i.getDenominazione().toUpperCase());
                i.setDenominazione(i.getDenominazione().toUpperCase());
                return i;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(istituti);
    }



}
