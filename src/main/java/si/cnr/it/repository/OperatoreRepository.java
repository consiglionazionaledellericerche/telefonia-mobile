package si.cnr.it.repository;

import si.cnr.it.domain.Operatore;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Operatore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperatoreRepository extends JpaRepository<Operatore, Long> {

}
