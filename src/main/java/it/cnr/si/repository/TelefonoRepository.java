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

import it.cnr.si.domain.Telefono;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Telefono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {


    Page<Telefono> findByIntestatarioContrattoInAndDeleted(List<String> intestatarioContratto, Boolean deleted, Pageable pageable);

    List<Telefono> findByIntestatarioContrattoInAndDeleted(List<String> intestatarioContratto, Boolean deleted);

    Page<Telefono> findByDeletedFalse(Pageable pageable);

    List<Telefono> findByDeletedFalse();

    List<Telefono> findByNumero(String numero);

    List<Telefono> findByNumeroAndDeletedFalse(String numero);
}
