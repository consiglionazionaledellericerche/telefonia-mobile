package si.cnr.it.web.rest;

import ch.qos.logback.core.net.server.Client;
import com.codahale.metrics.annotation.Timed;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.Paragraph;
import si.cnr.it.domain.StoricoTelefono;
import si.cnr.it.repository.StoricoTelefonoRepository;
import si.cnr.it.security.AuthoritiesConstants;
import si.cnr.it.service.TelefoniaPdfService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.ws.Response;
import java.awt.*;
import java.awt.print.PrinterException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;

/**
 * REST controller for managing StoricoTelefono.
 */
@RestController
@RequestMapping("/api")
public class StoricoTelefonoResource {

    @Autowired
    private TelefoniaPdfService telefoniaPdfService;

    private final Logger log = LoggerFactory.getLogger(StoricoTelefonoResource.class);

    private static final String ENTITY_NAME = "storicoTelefono";

    private final StoricoTelefonoRepository storicoTelefonoRepository;
   // private final TelefoniaPdfService telefoniaPdfService;

    public StoricoTelefonoResource(StoricoTelefonoRepository storicoTelefonoRepository
                                   ) {
        this.storicoTelefonoRepository = storicoTelefonoRepository;

    }

    /**
     * POST  /storico-telefonos : Create a new storicoTelefono.
     *
     * @param storicoTelefono the storicoTelefono to create
     * @return the ResponseEntity with status 201 (Created) and with body the new storicoTelefono, or with status 400 (Bad Request) if the storicoTelefono has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/storico-telefonos")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<StoricoTelefono> createStoricoTelefono(@Valid @RequestBody StoricoTelefono storicoTelefono) throws URISyntaxException {
        log.debug("REST request to save StoricoTelefono : {}", storicoTelefono);
        if (storicoTelefono.getId() != null) {
            throw new BadRequestAlertException("A new storicoTelefono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StoricoTelefono result = storicoTelefonoRepository.save(storicoTelefono);
        return ResponseEntity.created(new URI("/api/storico-telefonos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /storico-telefonos : Updates an existing storicoTelefono.
     *
     * @param storicoTelefono the storicoTelefono to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated storicoTelefono,
     * or with status 400 (Bad Request) if the storicoTelefono is not valid,
     * or with status 500 (Internal Server Error) if the storicoTelefono couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/storico-telefonos")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<StoricoTelefono> updateStoricoTelefono(@Valid @RequestBody StoricoTelefono storicoTelefono) throws URISyntaxException {
        log.debug("REST request to update StoricoTelefono : {}", storicoTelefono);
        if (storicoTelefono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StoricoTelefono result = storicoTelefonoRepository.save(storicoTelefono);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, storicoTelefono.getId().toString()))
            .body(result);
    }

    /**
     * GET  /storico-telefonos : get all the storicoTelefonos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of storicoTelefonos in body
     */
    @GetMapping("/storico-telefonos")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<List<StoricoTelefono>> getAllStoricoTelefonos(Pageable pageable) {
        log.debug("REST request to get a page of StoricoTelefonos");
        Page<StoricoTelefono> page = storicoTelefonoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/storico-telefonos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /storico-telefonos/:id : get the "id" storicoTelefono.
     *
     * @param id the id of the storicoTelefono to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the storicoTelefono, or with status 404 (Not Found)
     */
    @GetMapping("/storico-telefonos/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<StoricoTelefono> getStoricoTelefono(@PathVariable Long id) {
        log.debug("REST request to get StoricoTelefono : {}", id);
        Optional<StoricoTelefono> storicoTelefono = storicoTelefonoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(storicoTelefono);
    }

    /**
     * DELETE  /storico-telefonos/:id : delete the "id" storicoTelefono.
     *
     * @param id the id of the storicoTelefono to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/storico-telefonos/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<Void> deleteStoricoTelefono(@PathVariable Long id) {
        log.debug("REST request to delete StoricoTelefono : {}", id);

        storicoTelefonoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


    ///Visualizza query per anno

    @GetMapping("/storico-telefonos/vista")
    @Secured(AuthoritiesConstants.ADMIN)
    @Timed
    public ResponseEntity<List<StoricoTelefono>> getVista(Pageable pageable) throws IOException, COSVisitorException, PrinterException, URISyntaxException {
        log.debug("REST request to get a page of Storico Telefono getVista");
        String versione = "FIRMATO DIRETTORE";
        Page<StoricoTelefono> page = storicoTelefonoRepository.findByVersione(versione, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telefonos/vista");
       // PDPageContentStream pdf = telefoniaPdfService.creaPdf();
        //makePdf(pdf);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

   @RequestMapping(value = "/storico-telefonos/pdf", headers = "Accept=application/pdf", method = RequestMethod.GET, produces = "application/pdf")
   @ResponseBody
   @Timed
   @Secured(AuthoritiesConstants.USER)
   /**@GetMapping("/storico-telefono/pdf")
   @Secured(AuthoritiesConstants.ADMIN)
   @Timed*/
   public ResponseEntity<byte[]> getPdf(HttpServletRequest req) throws IOException, COSVisitorException, PrinterException, URISyntaxException {
       log.debug("Entri in getPdf e salvi pdf");

       final float FONT_SIZE = 10;
       final float TITLE_SIZE = 18;

       final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
       //telefoniaPdfService.faiPdf(outputStream);

       Document pdf = new Document(40, 60, 40, 60);
       Paragraph paragraphField = new Paragraph();
       Paragraph paragraphDiagram = new Paragraph();
       Paragraph paragraphDocs = new Paragraph();
       Paragraph paragraphHistory = new Paragraph();

       String titolo = "Titolo pdf \n";
       paragraphField.addText(titolo, TITLE_SIZE, HELVETICA_BOLD);
       pdf.add(paragraphField);
       pdf.add(ControlElement.NEWPAGE);
       pdf.add(paragraphDiagram);
       //  pdf.add(image);
       pdf.add(ControlElement.NEWPAGE);
       pdf.add(paragraphDocs);
       pdf.add(ControlElement.NEWPAGE);
       pdf.add(paragraphHistory);
       //  pdf.save(new File("prova2.pdf"));
       pdf.save(outputStream);

       String fileName = "pdf.pdf";
       HttpHeaders headers = new HttpHeaders();
       headers.set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
       headers.setContentType(MediaType.parseMediaType("application/pdf"));
       headers.setContentLength(outputStream.toByteArray().length);
       return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);

       //makePdf(pdf);

       //return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
   }
/**
    @RequestMapping(value = "/storico-telefonos/makepdf", headers = "Accept=application/pdf", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody
    @Secured(AuthoritiesConstants.USER)
    @Timed
    public static ResponseEntity<PDDocument> makePdf(PDDocument pdf) throws COSVisitorException, PrinterException, IOException {
        System.out.println("entri");
       // PDPageContentStream pdf = telefoniaPdfService.faiPdf();

        String fileName = "pdf.pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        headers.setContentType(MediaType.parseMediaType("application/pdf"));

        return new ResponseEntity<>(pdf,headers,HttpStatus.OK);
        //return new ResponseEntity<>(filePair.getSecond(), headers, HttpStatus.OK);
    }*/

}
