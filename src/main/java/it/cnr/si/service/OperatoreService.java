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

}
