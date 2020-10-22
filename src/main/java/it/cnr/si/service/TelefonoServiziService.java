package it.cnr.si.service;

import it.cnr.si.domain.Operatore;
import it.cnr.si.domain.Telefono;
import it.cnr.si.domain.TelefonoServizi;
import it.cnr.si.repository.*;
import it.cnr.si.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;

@Service
public class TelefonoServiziService {
    private final Logger log = LoggerFactory.getLogger(TelefonoServiziService.class);

    private static final String ENTITY_NAME = "telefonoServizi";

    private final TelefonoRepository telefonoRepository;
    private final TelefonoServiziRepository telefonoServiziRepository;
    private final OperatoreRepository operatoreRepository;
    private final StoricoTelefonoRepository storicoTelefonoRepository;
    private final ValidazioneRepository validazioneRepository;

    public TelefonoServiziService(TelefonoServiziRepository telefonoServiziRepository, OperatoreRepository operatoreRepository,
                                  StoricoTelefonoRepository storicoTelefonoRepository, ValidazioneRepository validazioneRepository,
                                  TelefonoRepository telefonoRepository) {
        this.telefonoServiziRepository = telefonoServiziRepository;
        this.operatoreRepository = operatoreRepository;
        this.storicoTelefonoRepository = storicoTelefonoRepository;
        this.validazioneRepository = validazioneRepository;
        this.telefonoRepository = telefonoRepository;
    }


    public void controlloDate(TelefonoServizi telefonoServizi){
        log.debug("Entrato in controllo Date {}", telefonoServizi);
        Instant dataInizio = telefonoServizi.getDataInizio();
        Instant dataFine = telefonoServizi.getDataFine();
        String risposta = "no";
        if(dataFine != null){
            if(dataInizio.isAfter(dataFine)){//Fare controllo che data attivazione minore di data Cessazione
                throw new BadRequestAlertException("Data Inizio è maggiore di data Fine", ENTITY_NAME, "dataInizioMaggioreDataFine");
            }
        }

        List<TelefonoServizi> telefoniNumeroUguale = telefonoServiziRepository.findByTelefono(telefonoServizi.getTelefono());
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
                log.debug("idTelefonoServizi {}",telefonoServizi.getId());
                log.debug("idTelefonoServizi Iterator {}",((TelefonoServizi) t).getId());
                Long idTelefonoServizi = telefonoServizi.getId();
                if (idTelefonoServizi == null){
                    idTelefonoServizi = 0L;
                }
                if (idTelefonoServizi.equals(((TelefonoServizi) t).getId())) {
                }
                else{
                    log.debug("datainizio {}",dataInizio);
                    log.debug("datafine {}",dataFine);
                    log.debug("datainizioDB {}",((TelefonoServizi) t).getDataInizio());
                    log.debug("datafineDB {}",((TelefonoServizi) t).getDataFine());
                    if(((TelefonoServizi) t).getDataFine() == null){
                        throw new BadRequestAlertException("Inserire data Cessazione Vecchio Contratto", ENTITY_NAME, "dataCessazioneVecchioContratto");
                    }
                    if (dataFine != null) {
                        if (dataInizio.isBefore(((TelefonoServizi) t).getDataInizio()) && dataFine.isBefore(((TelefonoServizi) t).getDataInizio())) {
                            risposta = "si";
                        }
                        if (dataInizio.isAfter(((TelefonoServizi) t).getDataFine()) && dataFine.isAfter(((TelefonoServizi) t).getDataFine())) {
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
                        if (dataInizio.isBefore(((TelefonoServizi) t).getDataInizio())) {
                            risposta = "si";
                        }
                        if (dataInizio.isAfter(((TelefonoServizi) t).getDataFine())) {
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
