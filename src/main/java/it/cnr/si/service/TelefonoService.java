package it.cnr.si.service;

import it.cnr.si.domain.*;
import it.cnr.si.repository.OperatoreRepository;
import it.cnr.si.repository.StoricoTelefonoRepository;
import it.cnr.si.repository.TelefonoServiziRepository;
import it.cnr.si.repository.ValidazioneRepository;
import it.cnr.si.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Service
public class TelefonoService {
    private final Logger log = LoggerFactory.getLogger(TelefonoService.class);

    private final TelefonoServiziRepository telefonoServiziRepository;
    private final OperatoreRepository operatoreRepository;
    private final StoricoTelefonoRepository storicoTelefonoRepository;
    private final ValidazioneRepository validazioneRepository;

    public TelefonoService(TelefonoServiziRepository telefonoServiziRepository, OperatoreRepository operatoreRepository, StoricoTelefonoRepository storicoTelefonoRepository, ValidazioneRepository validazioneRepository) {
        this.telefonoServiziRepository = telefonoServiziRepository;
        this.operatoreRepository = operatoreRepository;
        this.storicoTelefonoRepository = storicoTelefonoRepository;
        this.validazioneRepository = validazioneRepository;
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
                servizi = tS.getServizi().getNome();
            } else {
                servizi = servizi + ";" + tS.getServizi().getNome();
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
                operatore = o.getData() + " (" + o.getListaOperatori().getNome() + ")";
            } else {
                operatore = operatore + ";" + o.getData() + " (" + o.getListaOperatori().getNome();
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
                Validazione resultValidazione = validazioneRepository.save(validazione);
            }
        }
    }

}
