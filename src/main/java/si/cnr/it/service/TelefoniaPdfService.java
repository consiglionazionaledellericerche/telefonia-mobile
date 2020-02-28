package si.cnr.it.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.PDFBox;
//import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDPageLabelRange;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import si.cnr.it.repository.StoricoTelefonoRepository;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rst.pdfbox.layout.elements.ControlElement;
import rst.pdfbox.layout.elements.Document;
import rst.pdfbox.layout.elements.ImageElement;
import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Position;
import si.cnr.it.domain.StoricoTelefono;
import si.cnr.it.web.rest.StoricoTelefonoResource;


import java.awt.print.PrinterException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


@Service
public class TelefoniaPdfService {

    private final Logger log = LoggerFactory.getLogger(StoricoTelefonoResource.class);

    private final StoricoTelefonoRepository storicoTelefonoRepository;

    public TelefoniaPdfService(StoricoTelefonoRepository storicoTelefonoRepository
    ) {
        this.storicoTelefonoRepository = storicoTelefonoRepository;

    }

    float fontSizeTitolo = 13;
    float fontSizeIstituti = 10;
    float fontSizeCapiColonna = 10;
    float fontSizeTesto = 8;

    public File faiPdf() throws IOException {
        log.debug("Entra in faiPdf()");

        List<StoricoTelefono> listStoricoTelefonoVista = storicoTelefonoRepository.findByVersioneOrderByIntestatarioContratto("FIRMATO DIRETTORE");

        //String[] array = listStoricoTelefonoVista.stream().toArray(String[]::new);
        //String message2 = Arrays.toString(array);
        int i = listStoricoTelefonoVista.size();
        int num = 0;
        String filename = "vista.pdf";

        String message = "vediamo se si vede il testo";

        PDDocument doc = new PDDocument();

        PDPage page = new PDPage();
        PDPage page2 = new PDPage();

        PDImageXObject pdImage = PDImageXObject.createFromFile("C:/Users/Diego/Documents/lavoro/git/telefonia-mobile/src/main/webapp/content/images/CNR_logo_pdf_p.png",doc);
        //for(int p=0;p<i;p++) {
            doc.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;
            String istitutoNome = null;
            try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
                contents.drawImage(pdImage, 20, 730);
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTitolo);
                contents.newLineAtOffset(250, 700);
                contents.showText("Telefonia Mobile - Anno 2020");
                contents.endText();//Fine riga per testo da inserire

                Iterator v = listStoricoTelefonoVista.iterator();
                float ty = 650;
                while(v.hasNext()) {
                    float tx = 20;
                    StoricoTelefono sTelefono = (StoricoTelefono) v.next();
                    String istituto = sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto();
                    if(ty < 100){
                        ty = 700;
                        contents.close();
                        doc.addPage(page=new PDPage());
                        PDPageContentStream contents2 = new PDPageContentStream(doc, page);
                        contents2.beginText();//Inizio riga per testo da inserire
                        contents2.setFont(font, fontSizeTitolo);
                        contents2.newLineAtOffset(250, 700);
                        contents2.showText("Telefonia Mobile - Anno 2020");
                        contents2.endText();//Fine riga per testo da inserire
                        contents2.close();
                    }
                    if(istitutoNome != istituto){
                        log.debug("Entri in CapiColonna e Titolo");
                        if(istitutoNome != null){
                            contents.beginText();//Inizio riga per testo da inserire
                            contents.setFont(font, fontSizeTesto);
                            contents.newLineAtOffset(tx, ty);
                            contents.showText("_____________________________________________________________________________________________________");
                            contents.endText();//Fine riga per testo da inserire
                            ty = ty-20;
                        }
                        contents.beginText();//Inizio riga per testo da inserire
                        contents.setFont(font, fontSizeCapiColonna);
                        contents.newLineAtOffset(tx, ty);
                        contents.showText(sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto());
                        contents.endText();//Fine riga per testo da inserire
                        ty = ty-20;
                        creaTitoloColonna(contents,tx,ty);
                        ty = ty-20;
                    }
                    else{//da capire come gestire
                     /**   contents.beginText();//Inizio riga per testo da inserire
                        contents.setFont(font, fontSizeTesto);
                        contents.newLineAtOffset(tx, ty);
                        contents.showText("____________________________________________________________________");
                        contents.endText();//Fine riga per testo da inserire
                        ty = ty-20;*/
                    }
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getStoricotelefonoTelefono().getDataAttivazione().toString());
                    contents.endText();//Fine riga per testo da inserire
                    tx = tx+60;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    if(sTelefono.getStoricotelefonoTelefono().getDataCessazione() == null) {
                        contents.showText("");
                    }
                    else{
                        contents.showText(sTelefono.getStoricotelefonoTelefono().getDataCessazione().toString());
                    }
                    contents.endText();//Fine riga per testo da inserire
                    tx = tx+60;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getStoricotelefonoTelefono().getNumero());
                    contents.endText();//Fine riga per testo da inserire
                    tx = tx+60;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getStoricotelefonoTelefono().getUtilizzatoreUtenza());
                    contents.endText();//Fine riga per testo da inserire
                    tx = tx+90;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getOperatore());
                    contents.endText();//Fine riga per testo da inserire
                    tx = tx+90;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getDataModifica().toString());
                    contents.endText();//Fine riga per testo da inserire
                    ty = ty-20;
                    tx = 20;
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeTesto);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText("Servizi: "+sTelefono.getServizi());
                    contents.endText();//Fine riga per testo da inserire
                    ty = ty-20;
                    istitutoNome = sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto();
                }
            }
       // }
        File tmp = File.createTempFile("stm", null);
        doc.save(tmp);
        return tmp;
    }




/**
        ///prova salvataggio pdf
        Document pdf = new Document(40, 60, 40, 60);
        Paragraph paragraphField = new Paragraph();
        Paragraph paragraphDiagram = new Paragraph();
        Paragraph paragraphDocs = new Paragraph();
        Paragraph paragraphHistory = new Paragraph();
        pdf.add(paragraphField);
        pdf.add(ControlElement.NEWPAGE);
        pdf.add(paragraphDiagram);
    //  pdf.add(image);
        pdf.add(ControlElement.NEWPAGE);
        pdf.add(paragraphDocs);
        pdf.add(ControlElement.NEWPAGE);
        pdf.add(paragraphHistory);
        pdf.save(new File("src/main/webapp/content/documenti/prova2.pdf"));
      //  pdf.save(outputStream);
    }*/


    /**    public PDPageContentStream creaPdf() throws IOException {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();

            document.addPage(page);


            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.beginText();
            //contentStream.showText("Hello World");
            contentStream.drawString("CNR");
            contentStream.endText();
            contentStream.close();


            document.save("documenti/pdf.pdf");
            document.close();


        return contentStream;
    }*/

    /**ResponseBuilder response = response.ok((Object) document);
     response.header("Content-Disposition",
     "attachment; filename=pdf.pdf");*/


    /** Document pdf = new Document(40, 60, 40, 60);
     Paragraph paragraphField = new Paragraph();
     Paragraph paragraphDiagram = new Paragraph();
     Paragraph paragraphDocs = new Paragraph();
     Paragraph paragraphHistory = new Paragraph();

     pdf.add(paragraphField);
     pdf.add(ControlElement.NEWPAGE);
     pdf.add(paragraphDiagram);
     // pdf.add(image);
     pdf.add(ControlElement.NEWPAGE);
     pdf.add(paragraphDocs);
     pdf.add(ControlElement.NEWPAGE);
     pdf.add(paragraphHistory);

     pdf.;*/
/**
    public byte[] makePdf(Enum.PdfType pdfType, JSONObject processvariables, String fileName, String utenteRichiedente, String processInstanceId) {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());

        String dir = new RelaxedPropertyResolver(env, "jasper-report.").getProperty("dir-cnr");
        if(activeProfiles.contains("oiv")) {
            dir = new RelaxedPropertyResolver(env, "jasper-report.").getProperty("dir-oiv");
        }
        else if(activeProfiles.contains("cnr")) {
            dir = new RelaxedPropertyResolver(env, "jasper-report.").getProperty("dir-cnr");
        }
        byte[] pdfByteArray = null;
        HashMap<String, Object> parameters = new HashMap();
        InputStream jasperFile = null;
        try {
            //carico le variabili della process instance
            LOGGER.debug("Json con i dati da inserire nel pdf: {}", processvariables.toString().replaceAll("\\\\\"","\""));
            JRDataSource datasource = new JsonDataSource(new ByteArrayInputStream(processvariables.toString().getBytes(Charset.forName("UTF-8"))));
            //JRDataSource datasource = new JsonDataSource(new ByteArrayInputStream(processvariables.toString().replaceAll("\\\\\"","\"").getBytes(Charset.forName("UTF-8"))));

            final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "net.sf.jasperreports.view.viewer", Locale.ITALIAN);

            //carico un'immagine nel pdf "dinamicamente" (sostituisco una variabile nel file jsper con lo stream dell'immagine)
            if(activeProfiles.contains("oiv")) {
                parameters.put("ANN_IMAGE", this.getClass().getResourceAsStream(dir.substring(dir.indexOf("/print")) + "logo_OIV.JPG"));
            }
            else if(activeProfiles.contains("cnr")) {
                parameters.put("ANN_IMAGE", this.getClass().getResourceAsStream(dir.substring(dir.indexOf("/print")) + "logo_CNR.JPG"));
            }
            parameters.put(JRParameter.REPORT_LOCALE, Locale.ITALIAN);
            parameters.put(JRParameter.REPORT_RESOURCE_BUNDLE, resourceBundle);
            parameters.put(JRParameter.REPORT_DATA_SOURCE, datasource);

            LocalJasperReportsContext ctx = new LocalJasperReportsContext(DefaultJasperReportsContext.getInstance());
            ctx.setClassLoader(ClassLoader.getSystemClassLoader());
            JasperFillManager fillmgr = JasperFillManager.getInstance(ctx);

            //il nome del file jasper da caricare(dipende dal tipo di pdf da creare)
            jasperFile = this.getClass().getResourceAsStream(dir.substring(dir.indexOf("/print")) + pdfType.name() + ".jasper");
            JasperPrint jasperPrint = fillmgr.fill(jasperFile, parameters);
            LOGGER.info("-- jasperFile: {}", pdfType.name() + ".jasper");

            pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new ReportException("Errore JASPER nella creazione del pdf: {}", e);
        }

        //"Allego" il file nel flusso
        Map<String, FlowsAttachment> attachments = flowsAttachmentService.getAttachementsForProcessInstance(processInstanceId);

        FlowsAttachment attachment = attachments.get(pdfType.name());
        if (attachment != null) {
            //aggiorno il pdf
            attachment.setFilename(fileName);
            attachment.setName(pdfType.name());
            attachment.setAzione(Aggiornamento);
            attachment.setUsername(utenteRichiedente);
        } else {
            //salvo il pdf nel flusso
            attachment = new FlowsAttachment();
            attachment.setAzione(Caricamento);
            attachment.setPath(runtimeService.getVariable(processInstanceId, "pathFascicoloDocumenti", String.class));;
            attachment.setTaskId(null);
            attachment.setTaskName(null);
            attachment.setTime(new Date());
            attachment.setName(pdfType.name());
            attachment.setFilename(fileName);
            attachment.setMimetype(com.google.common.net.MediaType.PDF.toString());
            attachment.setUsername(utenteRichiedente);
        }
        String taskId = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult().getId();
        flowsAttachmentService.saveAttachment(taskId, pdfType.name(), attachment, pdfByteArray);

        return pdfByteArray;
    }*/

    private void creaTitoloColonna(PDPageContentStream contents, float tx, float ty) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        //float ty = 700;
        //float tx = 100;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Attivazione");
        contents.endText();//Fine riga per testo da inserire
        tx = tx+60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Cessazione");
        contents.endText();//Fine riga per testo da inserire
        tx = tx+60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Numero");
        contents.endText();//Fine riga per testo da inserire
        tx = tx+60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Nome Cognome");
        contents.endText();//Fine riga per testo da inserire
        tx = tx+90;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Operatore");
        contents.endText();//Fine riga per testo da inserire
        tx = tx+90;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Data Modifica");
        contents.endText();//Fine riga per testo da inserire
        //tx = tx+100;
        ty = ty-15;
    }
}
