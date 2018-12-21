package si.cnr.it.repository;

import si.cnr.it.domain.TelefonoServizi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TelefonoServizi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoServiziRepository extends JpaRepository<TelefonoServizi, Long> {

}
