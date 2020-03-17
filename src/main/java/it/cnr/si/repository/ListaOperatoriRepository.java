package it.cnr.si.repository;

import it.cnr.si.domain.ListaOperatori;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ListaOperatori entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListaOperatoriRepository extends JpaRepository<ListaOperatori, Long> {

}
