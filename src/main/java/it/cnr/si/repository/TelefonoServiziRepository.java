package it.cnr.si.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import it.cnr.si.domain.Telefono;
import it.cnr.si.domain.TelefonoServizi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TelefonoServizi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoServiziRepository extends JpaRepository<TelefonoServizi, Long> {

    public Boolean falso = Boolean.FALSE;

    // @Query("select form from Form form where form.processDefinitionKey =:processDefinitionKey and form.version = :version and form.taskId =:taskId")
    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.intestatarioContratto like :intestatarioContratto and ts.telefono.deleted =:deleted")
    public Page<TelefonoServizi> findByIntestatarioContrattoStartsWith(@Param("intestatarioContratto") String intestatarioContratto,@Param("deleted") boolean deleted, Pageable pageable);

    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.deleted =:deleted ")
    public Page<TelefonoServizi> findAllActive(@Param("deleted") boolean deleted,  Pageable pageable);

    public List<TelefonoServizi> findByTelefono(Telefono telefono);
}
