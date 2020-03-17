package it.cnr.si.repository;

import it.cnr.si.domain.Operatore;
import it.cnr.si.domain.Telefono;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import it.cnr.si.domain.Operatore;
import it.cnr.si.domain.Telefono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import it.cnr.si.domain.TelefonoServizi;

import java.util.List;


/**
 * Spring Data  repository for the Operatore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatoreRepository extends JpaRepository<Operatore, Long> {

    @Query("SELECT op FROM Operatore op where op.telefonoOperatore.intestatarioContratto like :intestatarioContratto AND op.telefonoOperatore.deleted =:deleted ")
    public Page<Operatore> findByIntestatarioContrattoStartsWith(@Param("intestatarioContratto") String intestatarioContratto,@Param("deleted") boolean deleted, Pageable pageable);

    @Query("SELECT op FROM Operatore op where op.telefonoOperatore.deleted =:deleted ")
    public Page<Operatore> findAllActive(@Param("deleted") boolean deleted,  Pageable pageable);

    public List<Operatore> findByTelefonoOperatore(Telefono telefonoOperatore);
}
