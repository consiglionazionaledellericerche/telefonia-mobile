package si.cnr.it.repository;

import si.cnr.it.domain.Telefonoservizi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Telefonoservizi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoserviziRepository extends JpaRepository<Telefonoservizi, Long> {

}
