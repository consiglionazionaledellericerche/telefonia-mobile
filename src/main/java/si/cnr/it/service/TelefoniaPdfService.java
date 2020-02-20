package si.cnr.it.service;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.PDFBox;
//import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDPageLabelRange;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;
import org.apache.pdfbox.pdmodel.common.PDStream;
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
import si.cnr.it.web.rest.StoricoTelefonoResource;


import java.awt.print.PrinterException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;


@Service
public class TelefoniaPdfService {

    private final Logger log = LoggerFactory.getLogger(StoricoTelefonoResource.class);


    public File faiPdf() throws IOException {
        log.debug("Entra in faiPdf()");


        String filename = "vista.pdf";
        String message = "vediamo se si vede il testo";

        PDDocument doc = new PDDocument();

        PDPage page = new PDPage();
        doc.addPage(page);

        PDFont font = PDType1Font.HELVETICA_BOLD;

        try (PDPageContentStream contents = new PDPageContentStream(doc, page)) {
            contents.beginText();
            contents.setFont(font, 12);
            contents.newLineAtOffset(100, 700);
            contents.showText(message);
            contents.endText();
        }
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


}
