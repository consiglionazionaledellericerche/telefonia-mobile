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

package it.cnr.si.service;

import it.cnr.si.domain.StoricoTelefono;
import it.cnr.si.repository.StoricoTelefonoRepository;
import it.cnr.si.web.rest.StoricoTelefonoResource;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

//import org.apache.pdfbox.PDFBox;
//import org.apache.pdfbox.exceptions.COSVisitorException;


@Service
public class TelefoniaPdfService {

    private final Logger log = LoggerFactory.getLogger(StoricoTelefonoResource.class);

    private final StoricoTelefonoRepository storicoTelefonoRepository;
    float fontSizeTitolo = 13;
    float fontSizeIstituti = 10;
    float fontSizeCapiColonna = 10;
    float fontSizeTesto = 8;

    public TelefoniaPdfService(StoricoTelefonoRepository storicoTelefonoRepository
    ) {
        this.storicoTelefonoRepository = storicoTelefonoRepository;

    }

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

        PDImageXObject pdImage = PDImageXObject.createFromFile("src/main/webapp/content/images/CNR_logo_pdf_p.png", doc);
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
            while (v.hasNext()) {
                float tx = 20;
                StoricoTelefono sTelefono = (StoricoTelefono) v.next();
                String istituto = sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto();
                if (ty < 100) {
                    ty = 700;
                    contents.close();
                    doc.addPage(page = new PDPage());
                    PDPageContentStream contents2 = new PDPageContentStream(doc, page);
                    contents2.beginText();//Inizio riga per testo da inserire
                    contents2.setFont(font, fontSizeTitolo);
                    contents2.newLineAtOffset(250, 700);
                    contents2.showText("Telefonia Mobile - Anno 2020");
                    contents2.endText();//Fine riga per testo da inserire
                    contents2.close();
                }
                if (istitutoNome != istituto) {
                    log.debug("Entri in CapiColonna e Titolo");
                    if (istitutoNome != null) {
                        contents.beginText();//Inizio riga per testo da inserire
                        contents.setFont(font, fontSizeTesto);
                        contents.newLineAtOffset(tx, ty);
                        contents.showText("_____________________________________________________________________________________________________");
                        contents.endText();//Fine riga per testo da inserire
                        ty = ty - 20;
                    }
                    contents.beginText();//Inizio riga per testo da inserire
                    contents.setFont(font, fontSizeCapiColonna);
                    contents.newLineAtOffset(tx, ty);
                    contents.showText(sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto());
                    contents.endText();//Fine riga per testo da inserire
                    ty = ty - 20;
                    creaTitoloColonna(contents, tx, ty);
                    ty = ty - 20;
                } else {//da capire come gestire
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
                tx = tx + 60;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                if (sTelefono.getStoricotelefonoTelefono().getDataCessazione() == null) {
                    contents.showText("");
                } else {
                    contents.showText(sTelefono.getStoricotelefonoTelefono().getDataCessazione().toString());
                }
                contents.endText();//Fine riga per testo da inserire
                tx = tx + 60;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                contents.showText(sTelefono.getStoricotelefonoTelefono().getNumero());
                contents.endText();//Fine riga per testo da inserire
                tx = tx + 60;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                contents.showText(sTelefono.getStoricotelefonoTelefono().getUtilizzatoreUtenza());
                contents.endText();//Fine riga per testo da inserire
                tx = tx + 90;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                contents.showText(sTelefono.getOperatore());
                contents.endText();//Fine riga per testo da inserire
                tx = tx + 90;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                contents.showText(sTelefono.getDataModifica().toString());
                contents.endText();//Fine riga per testo da inserire
                ty = ty - 20;
                tx = 20;
                contents.beginText();//Inizio riga per testo da inserire
                contents.setFont(font, fontSizeTesto);
                contents.newLineAtOffset(tx, ty);
                contents.showText("Servizi: " + sTelefono.getServizi());
                contents.endText();//Fine riga per testo da inserire
                ty = ty - 20;
                istitutoNome = sTelefono.getStoricotelefonoTelefono().getIntestatarioContratto();
            }
        }
        // }
        File tmp = File.createTempFile("stm", null);
        doc.save(tmp);
        return tmp;
    }

    private void creaTitoloColonna(PDPageContentStream contents, float tx, float ty) throws IOException {
        PDFont font = PDType1Font.HELVETICA_BOLD;
        //float ty = 700;
        //float tx = 100;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Attivazione");
        contents.endText();//Fine riga per testo da inserire
        tx = tx + 60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Cessazione");
        contents.endText();//Fine riga per testo da inserire
        tx = tx + 60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Numero");
        contents.endText();//Fine riga per testo da inserire
        tx = tx + 60;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Nome Cognome");
        contents.endText();//Fine riga per testo da inserire
        tx = tx + 90;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Operatore");
        contents.endText();//Fine riga per testo da inserire
        tx = tx + 90;
        contents.beginText();//Inizio riga per testo da inserire
        contents.setFont(font, fontSizeCapiColonna);
        contents.newLineAtOffset(tx, ty);
        contents.showText("Data Modifica");
        contents.endText();//Fine riga per testo da inserire
        //tx = tx+100;
        ty = ty - 15;
    }
}
