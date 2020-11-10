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

import it.cnr.si.domain.*;
import it.cnr.si.repository.*;
import it.cnr.si.security.SecurityUtils;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;

@Service
public class TelefonoService {
    private static final String ENTITY_NAME = "telefono";
    private final Logger log = LoggerFactory.getLogger(TelefonoService.class);
    private final TelefonoRepository telefonoRepository;
    private final TelefonoServiziRepository telefonoServiziRepository;
    private final OperatoreRepository operatoreRepository;
    private final StoricoTelefonoRepository storicoTelefonoRepository;
    private final ValidazioneRepository validazioneRepository;

    public TelefonoService(TelefonoServiziRepository telefonoServiziRepository, OperatoreRepository operatoreRepository,
                           StoricoTelefonoRepository storicoTelefonoRepository, ValidazioneRepository validazioneRepository,
                           TelefonoRepository telefonoRepository) {
        this.telefonoServiziRepository = telefonoServiziRepository;
        this.operatoreRepository = operatoreRepository;
        this.storicoTelefonoRepository = storicoTelefonoRepository;
        this.validazioneRepository = validazioneRepository;
        this.telefonoRepository = telefonoRepository;
    }

    public void controlloDate(Telefono telefono) {
        log.debug("Entrato in controllo Date {}", telefono);
        Instant dataAttivazione = telefono.getDataAttivazione();
        Instant dataCessazione = telefono.getDataCessazione();
        String risposta = "no";
        if (dataCessazione != null) {
            if (dataAttivazione.isAfter(dataCessazione)) {//Fare controllo che data attivazione minore di data Cessazione
                throw new BadRequestAlertException("Data Inizio è maggiore di data Fine", ENTITY_NAME, "dataInizioMaggioreDataFine");
            }
        }

        List<Telefono> telefoniNumeroUguale = telefonoRepository.findByNumero(telefono.getNumero());
        if (telefoniNumeroUguale == null || telefoniNumeroUguale.isEmpty()) {
            //vuol dire che non esiste nessun Telefono
        } else {
            /**
             * Creare iterator per telefoniNumeroUguale
             */
            Iterator i = telefoniNumeroUguale.iterator();
            while (i.hasNext()) {
                Object t = i.next();
                //pensare se si è in modifica e sta provando a modificare un qualcosa di già inserito
                log.debug("idTelefono {}", telefono.getId());
                log.debug("idTelefono Iterator {}", ((Telefono) t).getId());
                Long idTelefono = telefono.getId();
                if (idTelefono == null) {
                    idTelefono = 0L;
                }
                if (idTelefono.equals(((Telefono) t).getId())) {
                } else {
                    if (((Telefono) t).getDataCessazione() == null) {
                        throw new BadRequestAlertException("Inserire data Cessazione Vecchio Contratto", ENTITY_NAME, "dataCessazioneVecchioContratto");
                    }
                    if (dataCessazione != null) {
                        if (dataAttivazione.isBefore(((Telefono) t).getDataAttivazione()) && dataCessazione.isBefore(((Telefono) t).getDataAttivazione())) {
                            risposta = "si";
                        }
                        if (dataAttivazione.isAfter(((Telefono) t).getDataCessazione()) && dataCessazione.isAfter(((Telefono) t).getDataCessazione())) {
                            risposta = "si";
                        }
                        if (risposta.equals("no")) {
                            throw new BadRequestAlertException("Controllare date sui contratti", ENTITY_NAME, "dataErrata");
                        }
                        /**
                         * Fare controllo che data Attivazione è prima di data attivazione altro contratto e data cessazzione è prima di altro contratto
                         *
                         * o data attivazione è dopo data cessazione altro contratto e data cessazione è dopo data cessazione altro contratto
                         */
                    } else {
                        if (dataAttivazione.isBefore(((Telefono) t).getDataAttivazione())) {
                            risposta = "si";
                        }
                        if (dataAttivazione.isAfter(((Telefono) t).getDataCessazione())) {
                            risposta = "si";
                        }
                        if (risposta.equals("no")) {
                            throw new BadRequestAlertException("Controllare date sui contratti", ENTITY_NAME, "dataErrata");
                        }
                    }
                }
            }
        }
        //Controllo se dataAttivazione è maggiore della
        // data di cessazione degli altri contratti se ci sono
    }

    public void salvabackground(Telefono telefono, String stato) {
        //Inserisce storicoTelefono
        Instant instant = Instant.now();

        StoricoTelefono storicoTelefono = new StoricoTelefono();
        storicoTelefono.setDataAttivazione(telefono.getDataAttivazione());
        storicoTelefono.setDataCessazione(telefono.getDataCessazione());
        storicoTelefono.setDataModifica(instant);
        storicoTelefono.setUserModifica(SecurityUtils.getCurrentUserLogin().get());
        storicoTelefono.setIntestatarioContratto(telefono.getIntestatarioContratto());
        storicoTelefono.setNumeroContratto(telefono.getNumeroContratto());
        storicoTelefono.setUtilizzatoreUtenza(telefono.getUtilizzatoreUtenza());
        storicoTelefono.setCdsuo(telefono.getCdsuo());
        storicoTelefono.setVersione(stato);
        storicoTelefono.setStoricotelefonoTelefono(telefono);

        ///fare iterator per valori di servizi
        List<TelefonoServizi> listTelefonoServizi = telefonoServiziRepository.findByTelefono(telefono);
        Iterator<TelefonoServizi> iTS = listTelefonoServizi.iterator();
        String servizi = "";
        while (iTS.hasNext()) {
            TelefonoServizi tS = iTS.next();
            if (servizi.equals("")) {
                servizi = "Dal:" + tS.getDataInizio() + ";Al:" + tS.getDataFine() + " (" + tS.getServizi().getNome() + ")";
            } else {
                servizi = servizi + ";--- Dal:" + tS.getDataInizio() + ";Al:" + tS.getDataFine() + " (" + tS.getServizi().getNome() + ")";
            }
        }
        storicoTelefono.setServizi(servizi);

        //Fare Iterator per operatore
        List<Operatore> listOperatore = operatoreRepository.findByTelefonoOperatore(telefono);
        Iterator<Operatore> iO = listOperatore.iterator();
        String operatore = "";
        while (iO.hasNext()) {
            Operatore o = iO.next();
            if (operatore.equals("")) {
                operatore = "Dal:" + o.getData() + ";Al:" + o.getDataFine() + " (Operatore:" + o.getListaOperatori().getNome() + ", NumeroContratto:" + o.getNumeroContratto() + ")";
            } else {
                operatore = operatore + "; --- Dal:" + o.getData() + ";Al:" + o.getDataFine() + " (Operatore:" + o.getListaOperatori().getNome() + ", NumeroContratto:" + o.getNumeroContratto() + ");";
            }
        }
        storicoTelefono.setOperatore(operatore);

        StoricoTelefono valStoricoTelefono = storicoTelefonoRepository.save(storicoTelefono);

        log.debug("Servizi è  : {}", servizi);
        log.debug("operatore è : {}", operatore);


        //Inserisce validazioneTelefono
        if (!stato.equals("FIRMATO DIRETTORE")) {
            if (!operatore.equals("") & !servizi.equals("")) {
                Validazione validazione = new Validazione();
                validazione.setDescrizione(stato + " TELEFONO nome utente:" + telefono.getUtilizzatoreUtenza() + "; IntestatarioContratto:" + telefono.getIntestatarioContratto() + "; Cellulare:" + telefono.getNumero());
                validazione.setValidazioneTelefono(telefono);
                validazione.setDataModifica(LocalDate.now());
                validazione.setStampa(valStoricoTelefono);
                validazione.setDataValidazione(ZonedDateTime.now());
                Validazione resultValidazione = validazioneRepository.save(validazione);
            }
        }
    }

}
