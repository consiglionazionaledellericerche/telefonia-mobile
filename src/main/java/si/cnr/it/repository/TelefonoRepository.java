package si.cnr.it.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import si.cnr.it.domain.Telefono;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Telefono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {

    public Page<Telefono> findByIstitutoTelefono(String istitutoTelefono, Pageable pageable);

    public List<Telefono> findByIstitutoTelefono(String istitutoTelefono);
}
