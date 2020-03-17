package it.cnr.si.repository;

import it.cnr.si.service.dto.anagrafica.base.PageDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import it.cnr.si.domain.Telefono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Telefono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {


    public Page<Telefono> findByIntestatarioContrattoStartsWithAndDeleted(String intestatarioContratto,Boolean deleted, Pageable pageable);

    public List<Telefono> findByIntestatarioContrattoStartsWithAndDeleted(String intestatarioContratto,Boolean deleted);

    public Page<Telefono> findByDeletedFalse(Pageable pageable);

    public List<Telefono> findByDeletedFalse();
}
