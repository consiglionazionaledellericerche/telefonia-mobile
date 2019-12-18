package si.cnr.it.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import si.cnr.it.domain.Validazione;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Validazione entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValidazioneRepository extends JpaRepository<Validazione, Long> {
    //public Page<Validazione> findByIntestatarioContratto(String intestatarioContratto, Pageable pageable);
//TODO: fare query per visionare solo quelli del suo Ufficio/Istituto
  //  @Query("SELECT op FROM Operatore op where op.telefonoOperatore.deleted =:deleted ")
  //  public Page<Validazione> findAllActive(@Param("deleted") boolean deleted, Pageable pageable);
}
