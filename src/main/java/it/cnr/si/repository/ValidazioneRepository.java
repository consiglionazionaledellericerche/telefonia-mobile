package it.cnr.si.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import it.cnr.si.domain.Validazione;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Validazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidazioneRepository extends JpaRepository<Validazione, Long> {
    public Page<Validazione> findByValidazioneTelefonoIntestatarioContrattoStartsWith(String intestatarioContratto,Pageable pageable);
}
