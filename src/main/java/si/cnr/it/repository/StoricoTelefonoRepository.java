package si.cnr.it.repository;

import si.cnr.it.domain.StoricoTelefono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StoricoTelefono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoricoTelefonoRepository extends JpaRepository<StoricoTelefono, Long> {

}
