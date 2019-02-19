package si.cnr.it.repository;

import it.cnr.si.service.dto.anagrafica.base.PageDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
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

    public Page<Telefono> findByIntestatarioContratto(String intestatarioContratto, Pageable pageable);

    public List<Telefono> findByIntestatarioContratto(String intestatarioContratto);

    /**  @RequestLine("GET api/ace/v1/entitaorganizzativa?term={term}&tipo=1")
    PageDto<EntitaOrganizzativaWebDto> findIstitutiByTerm(@Param("term") String var1);*/
}
