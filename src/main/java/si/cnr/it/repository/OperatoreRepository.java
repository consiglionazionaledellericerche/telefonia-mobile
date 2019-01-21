package si.cnr.it.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import si.cnr.it.domain.Operatore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import si.cnr.it.domain.TelefonoServizi;


/**
 * Spring Data  repository for the Operatore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatoreRepository extends JpaRepository<Operatore, Long> {

    @Query("SELECT op FROM Operatore op where op.telefonoOperatore.istitutoTelefono =:istitutoTelefono ")
    public Page<Operatore> findByIstitutoTelefono(@Param("istitutoTelefono") String istitutoTelefono, Pageable pageable);
}
