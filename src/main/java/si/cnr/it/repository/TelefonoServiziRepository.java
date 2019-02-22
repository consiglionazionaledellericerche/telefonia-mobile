package si.cnr.it.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import si.cnr.it.domain.Telefono;
import si.cnr.it.domain.TelefonoServizi;
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
    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.intestatarioContratto =:intestatarioContratto ")
    public Page<TelefonoServizi> findByIntestatarioContratto(@Param("intestatarioContratto") String intestatarioContratto, Pageable pageable);

    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.deleted =:deleted ")
    public Page<TelefonoServizi> findAllActive(@Param("deleted") boolean deleted,  Pageable pageable);
}
