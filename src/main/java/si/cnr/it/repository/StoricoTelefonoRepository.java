package si.cnr.it.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import si.cnr.it.domain.StoricoTelefono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StoricoTelefono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoricoTelefonoRepository extends JpaRepository<StoricoTelefono, Long> {

//    @Query("SELECT op FROM Operatore op where op.telefonoOperatore.deleted =:deleted ")
//    public Page<StoricoTelefono> findDataModifica(@Param("deleted") boolean deleted, Pageable pageable);

    public Page<StoricoTelefono> findByVersione(String versione, Pageable pageable);
}
