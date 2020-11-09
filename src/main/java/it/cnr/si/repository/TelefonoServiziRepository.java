/*
 * Copyright (C) 2020 Consiglio Nazionale delle Ricerche
 *
 *                 This program is free software: you can redistribute it and/or modify
 *                 it under the terms of the GNU Affero General Public License as
 *                 published by the Free Software Foundation, either version 3 of the
 *                 License, or (at your option) any later version.
 *
 *                 This program is distributed in the hope that it will be useful,
 *                 but WITHOUT ANY WARRANTY; without even the implied warranty of
 *                 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *                 GNU Affero General Public License for more details.
 *
 *                 You should have received a copy of the GNU Affero General Public License
 *                 along with this program. If not, see https://www.gnu.org/licenses/
 */

package it.cnr.si.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import it.cnr.si.domain.Telefono;
import it.cnr.si.domain.TelefonoServizi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TelefonoServizi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoServiziRepository extends JpaRepository<TelefonoServizi, Long> {

    public Boolean falso = Boolean.FALSE;

    // @Query("select form from Form form where form.processDefinitionKey =:processDefinitionKey and form.version = :version and form.taskId =:taskId")
    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.intestatarioContratto like :intestatarioContratto and ts.telefono.deleted =:deleted")
    public Page<TelefonoServizi> findByIntestatarioContrattoStartsWith(@Param("intestatarioContratto") String intestatarioContratto,@Param("deleted") boolean deleted, Pageable pageable);

    @Query("SELECT ts FROM TelefonoServizi ts where ts.telefono.deleted =:deleted ")
    public Page<TelefonoServizi> findAllActive(@Param("deleted") boolean deleted,  Pageable pageable);

    public List<TelefonoServizi> findByTelefono(Telefono telefono);
}
