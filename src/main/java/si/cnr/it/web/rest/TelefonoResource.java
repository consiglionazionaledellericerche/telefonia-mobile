package si.cnr.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import it.cnr.si.service.AceService;
import it.cnr.si.service.dto.anagrafica.base.NodeDto;
import it.cnr.si.service.dto.anagrafica.base.PageDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.letture.IndirizzoWebDto;
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

    private List<EntitaOrganizzativaWebDto> ist;

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
        if(telefono.getUtilizzatoreUtenza().contains(".")){

        }
        else{
            return (ResponseEntity<Telefono>) ResponseEntity.unprocessableEntity();
        }
        /**
         * Per mettere il cdsuo
         */
        String telefonocdsuo = telefono.getIntestatarioContratto();
        telefonocdsuo = telefonocdsuo.substring(0,6);
        telefono.setCdsuo(telefonocdsuo);
        telefono.setIntestatarioContratto(telefono.getIntestatarioContratto().substring(9));
        /**
         * Fine mettere cdsuo
         */
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

        String sede_user = getSedeUser();
        String cds = getCdsUser();

// Per mettere CDSUO
        String telefonocdsuo = telefono.getIntestatarioContratto();
        telefonocdsuo = telefonocdsuo.substring(0,6);
        telefono.setCdsuo(telefonocdsuo);
        telefono.setIntestatarioContratto(telefono.getIntestatarioContratto().substring(9));
//Fine
        // Codice che permette di salvare solo se sei la persona corretta
        boolean hasPermission = false;

        if (cds.equals("000"))
            hasPermission = true;
        else {
//            Telefono t = telefonoRepository.getOne(telefono.getId());
            String t = telefono.getIntestatarioContratto();
            hasPermission = sede_user.equals(t);
        }
//        System.out.print("Che valore hai true o false? "+hasPermission);
        if (hasPermission) {
            if(telefono.getUtilizzatoreUtenza().contains(".")){

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

        String sede_user = getSedeUser(); //sede di username
        String cds = getCdsUser();

        Page<Telefono> telefoni;
        if(cds.equals("000")) {
            telefoni = telefonoRepository.findAll(pageable);
        } else {
            telefoni = telefonoRepository.findByIntestatarioContratto(sede_user, pageable);
        }
        System.out.println("TELEFONI = "+telefoni+"CDS="+cds+"="+sede_user+"=ISTITUTO PER LE RISORSE BIOLOGICHE E BIOTECNOLOGIE MARINE=");

        findIstituto();
        Iterator v = telefoni.iterator();
        while(v.hasNext()) {
            Telefono tel = (Telefono) v.next();
            Iterator<EntitaOrganizzativaWebDto> i = ist.iterator();
            while (i.hasNext()) {
                EntitaOrganizzativaWebDto is = (EntitaOrganizzativaWebDto) i.next();
                if(tel.getCdsuo().equals(is.getCdsuo())){
//                if(tel.getIntestatarioContratto().equals(is.getDenominazione())){
//                if(tel.getIntestatarioContratto().equals(is.getSigla())){
                    tel.setIntestatarioContratto(tel.getIntestatarioContratto()+" ("+is.getIndirizzoPrincipale().getComune()+")");
                }
            }
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

        String sede_user = getSedeUser();
        String cds = getCdsUser();
        String denominazione = "";


        findIstituto();
        Iterator<EntitaOrganizzativaWebDto> i = ist.iterator();
        while (i.hasNext()) {
            EntitaOrganizzativaWebDto is = (EntitaOrganizzativaWebDto) i.next();
            if(telefono.get().getCdsuo().equals(is.getCdsuo())){
//            if(telefono.get().getIntestatarioContratto().equals(is.getSigla())){
                denominazione = telefono.get().getIntestatarioContratto();
                telefono.get().setIntestatarioContratto(telefono.get().getIntestatarioContratto()+" ("+is.getIndirizzoPrincipale().getComune()+")");
            }
        }

        System.out.print("Vediamo un pò"+sede_user+" == "+denominazione+"Fine");
        if (cds.equals("000")){
            telefono.get().setIntestatarioContratto(telefono.get().getCdsuo() + " - " + telefono.get().getIntestatarioContratto());
            return ResponseUtil.wrapOrNotFound(telefono);
        }
        else{
            if(sede_user.equals(denominazione)) {
                telefono.get().setIntestatarioContratto(telefono.get().getCdsuo() + " - " + telefono.get().getIntestatarioContratto());
                return ResponseUtil.wrapOrNotFound(telefono);
            }
            else {
                System.out.print("Sei quiiiiiiii!!!");
                java.lang.Long ids = Long.valueOf(0);
                Optional<Telefono> telefonos = telefonoRepository.findById(ids);
                return ResponseUtil.wrapOrNotFound(telefonos);
            }
        }
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


        String cds = getCdsUser();

        //Istituti Richiamo
        List<NodeDto> gerarchiaIstituti = ace.getGerarchiaIstituti();
        //Uffici Richiamo
        List<NodeDto> gerarchiaUffici = ace.getGerarchiaUffici();

        //Array completo
        List<EntitaOrganizzativaWebDto> istitutiESedi = new ArrayList<>();

        //Inserisco Sede Centrale
        EntitaOrganizzativaWebDto ist = new EntitaOrganizzativaWebDto();
        IndirizzoWebDto indirizzo = new IndirizzoWebDto();
        indirizzo.setComune("Roma");
        ist.setCdsuo("000000");
        ist.setDenominazione("SEDE CENTRALE");
        ist.setIndirizzoPrincipale(indirizzo);
        //Fine inserimento Sede Centrale


        String cdsuo = "";
        String cdsuos = "";
        String Prova = "";
        for (NodeDto ufficio: gerarchiaUffici) {
            for (NodeDto figlio: ufficio.children) {
                if(figlio.entitaOrganizzativa.getCdsuo().substring(0,3).equals(cds) || cds.equals("000")){
                    figlio.entitaOrganizzativa.setIndirizzoPrincipale(indirizzo); //Lo forzo sennò non funziona
                    istitutiESedi.add(figlio.entitaOrganizzativa);
                }


            }
        }





        //Istituti
        //List<EntitaOrganizzativaWebDto> istitutiESedi = new ArrayList<>();

     //   String cdsuo = "";
     //   String cdsuos = "";
        cdsuo = "";
        cdsuos = "";
String nome = "";
        int a = 0;

        //  System.out.print(cds);
        for (NodeDto istituto: gerarchiaIstituti) {
            if(a == 0 && cds.equals("000")) {
                //Prova inserimento a buffo sede centrale
                istitutiESedi.add(ist);
                //Fine Prova inserimento a buffo sede centrale
                a = a+1;
            }
            if(istituto.entitaOrganizzativa.getCdsuo().substring(0,3).equals(cds) || cds.equals("000")) {

                /** Tolgo il padre che sono i figli quelli che mi interessano
                 * istitutiESedi.add(istituto.entitaOrganizzativa);
                 cdsuo = istituto.entitaOrganizzativa.getCdsuo();
                 cdsuos = cdsuos+" - "+istituto.entitaOrganizzativa.getCdsuo();*/
                // System.out.print("quanto è cdsuo: "+cdsuo);
            }
            nome = istituto.entitaOrganizzativa.getDenominazione();
            for (NodeDto figlio: istituto.children) {
                // System.out.print("Contiene cdsuo = "+istitutiESedi.contains(figlio.entitaOrganizzativa.getCdsuo())+" - questo valore: "+figlio.entitaOrganizzativa.getCdsuo()+" ||");
                if(figlio.entitaOrganizzativa.getCdsuo().equals(cdsuo)){

                }
                else{
                    if(cdsuos.contains(figlio.entitaOrganizzativa.getCdsuo())){

                    }
                    else {
                        if(figlio.entitaOrganizzativa.getCdsuo().substring(0,3).equals(cds) || cds.equals("000"))
                            figlio.entitaOrganizzativa.setDenominazione(nome);
                            istitutiESedi.add(figlio.entitaOrganizzativa);
                    }
                }
                cdsuos = cdsuos+" - "+figlio.entitaOrganizzativa.getCdsuo();

            }
        }

        istitutiESedi = istitutiESedi.stream()
            .sorted((i1, i2) -> i1.getDenominazione().compareTo(i2.getDenominazione()))
            .map(i -> {
                //i.setDenominazione(i.getCdsuo()+" - "+i.getDenominazione().toUpperCase());
//                i.setDenominazione(i.getDenominazione().toUpperCase()+" - "+i.getIndirizzoPrincipale().getProvincia());
                i.setDenominazione(i.getDenominazione().toUpperCase());
                return i;
            })
            .collect(Collectors.toList());



        Iterator i = istitutiESedi.iterator();
        while(i.hasNext()){
            EntitaOrganizzativaWebDto EO = (EntitaOrganizzativaWebDto) i.next();
//            System.out.println("Inizio"+EO.getDenominazione()+" - "+EO.getCdsuo()+""+EO.getIndirizzoPrincipale().getComune()+"Fine");
        }




//        List<EntitaOrganizzativaWebDto> istituti = ace.listaIstitutiAttivi();
//
//        istituti = istituti.stream()
//            .sorted((i1, i2) -> i1.getDenominazione().compareTo(i2.getDenominazione()))
//            .map(i -> {
//                i.setDenominazione(i.getDenominazione().toUpperCase());
//                return i;
//            })
//            .collect(Collectors.toList());




//        List<String> result = new ArrayList<>();
//
//        Map<String, String> query = new HashMap<>();
//        query.put("term", term);
//
//        List<EntitaOrganizzativaWebDto> istituti = ace.listaIstitutiAttivi();
//
//
//        for (EntitaOrganizzativaWebDto istituto : istituti ) {
//            if ( istituto.getDenominazione() != null)
//                result.add(  istituto.getDenominazione()  );
//        }
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

        setIst(istitutiESedi);


        return ResponseEntity.ok(istitutiESedi);
    }

    public String getSedeUser(){
//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getDenominazione(); //sede di username
        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getDenominazione(); //sede di username
//        String sede_user = ace.getPersonaByUsername("gaetana.irrera").getSede().getSigla(); //sigla di username
//        String sede_user = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getSigla(); //sigla di username
        return sede_user;
    }

    public String getCdsUser(){
//        String sede_cdsuoUser = ace.getPersonaByUsername("gaetana.irrera").getSede().getCdsuo(); //sede_cds di username
        String sede_cdsuoUser = ace.getPersonaByUsername(securityUtils.getCurrentUserLogin().get()).getSede().getCdsuo(); //sede_cds di username
        String cds = sede_cdsuoUser.substring(0,3); //passo solo i primi tre caratteri quindi cds
        return cds;
    }

    public void setIst(List<EntitaOrganizzativaWebDto> istituti){
        ist = istituti;
    }

    public List<EntitaOrganizzativaWebDto> getIst(){
        return ist;
    }



}
