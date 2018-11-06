package si.cnr.it.repository;

import si.cnr.it.domain.Servizi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Servizi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiziRepository extends JpaRepository<Servizi, Long> {

}
