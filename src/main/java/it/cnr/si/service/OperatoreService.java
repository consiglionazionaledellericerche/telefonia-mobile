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
import java.util.Iterator;
import java.util.List;

@Service
public class OperatoreService {
    private final Logger log = LoggerFactory.getLogger(OperatoreService.class);

    private static final String ENTITY_NAME = "operatore";

    private final TelefonoRepository telefonoRepository;
    private final TelefonoServiziRepository telefonoServiziRepository;
    private final OperatoreRepository operatoreRepository;
    private final StoricoTelefonoRepository storicoTelefonoRepository;
    private final ValidazioneRepository validazioneRepository;

    public OperatoreService(TelefonoServiziRepository telefonoServiziRepository, OperatoreRepository operatoreRepository,
                            StoricoTelefonoRepository storicoTelefonoRepository, ValidazioneRepository validazioneRepository,
                            TelefonoRepository telefonoRepository) {
        this.telefonoServiziRepository = telefonoServiziRepository;
        this.operatoreRepository = operatoreRepository;
        this.storicoTelefonoRepository = storicoTelefonoRepository;
        this.validazioneRepository = validazioneRepository;
        this.telefonoRepository = telefonoRepository;
    }

    public void controlloEsisteOperatore(Operatore operatore){
        List<Operatore> op = operatoreRepository.findByTelefonoOperatore(operatore.getTelefonoOperatore());
        Iterator i = op.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if(operatore.getTelefonoOperatore().getId().equals(((Operatore) o).getTelefonoOperatore().getId())){
                throw new BadRequestAlertException("Telefono già inserito", ENTITY_NAME, "telefonoInserito");
            }
        }
    }


    public void controlloDate(Operatore operatore){
        log.debug("Entrato in controllo Date {}", operatore);
        Instant data = operatore.getData();
        Instant dataFine = operatore.getDataFine();
        String risposta = "no";
        if(dataFine != null){
            if(data.isAfter(dataFine)){//Fare controllo che data attivazione minore di data Cessazione
                throw new BadRequestAlertException("Data Inizio è maggiore di data Fine", ENTITY_NAME, "dataInizioMaggioreDataFine");
            }
        }

        List<Operatore> telefoniNumeroUguale = operatoreRepository.findByTelefonoOperatore(operatore.getTelefonoOperatore());
        if(telefoniNumeroUguale == null || telefoniNumeroUguale.isEmpty()) {
            //vuol dire che non esiste nessun Telefono
        }
        else{
            /**
             * Creare iterator per telefoniNumeroUguale
             */
            Iterator i = telefoniNumeroUguale.iterator();
            while (i.hasNext()) {
                Object t = i.next();
                //pensare se si è in modifica e sta provando a modificare un qualcosa di già inserito
                log.debug("idOperatore {}",operatore.getId());
                log.debug("idOperatore Iterator {}",((Operatore) t).getId());
                Long idOperatore = operatore.getId();
                if (idOperatore == null){
                    idOperatore = 0L;
                }
                if (idOperatore.equals(((Operatore) t).getId())) {
                }
                else{
                    if(((Operatore) t).getDataFine() == null){
                        throw new BadRequestAlertException("Inserire data Cessazione Vecchio Contratto", ENTITY_NAME, "dataCessazioneVecchioContratto");
                    }
                    if (dataFine != null) {
                        if (data.isBefore(((Operatore) t).getData()) && dataFine.isBefore(((Operatore) t).getData())) {
                            risposta = "si";
                        }
                        if (data.isAfter(((Operatore) t).getDataFine()) && dataFine.isAfter(((Operatore) t).getDataFine())) {
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
                    }
                    else {
                        if (data.isBefore(((Operatore) t).getData())) {
                            risposta = "si";
                        }
                        if (data.isAfter(((Operatore) t).getDataFine())) {
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

}
