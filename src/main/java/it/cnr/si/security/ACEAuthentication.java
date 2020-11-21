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

package it.cnr.si.security;

import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleUtenteWebDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ACEAuthentication extends UsernamePasswordAuthenticationToken implements Authentication {

    private final SimpleEntitaOrganizzativaWebDto sede;
    private SimpleUtenteWebDto utente;

    public ACEAuthentication(Object principal, Object credentials, SimpleEntitaOrganizzativaWebDto sede) {
        super(principal, credentials);
        this.sede = sede;
    }
    public ACEAuthentication(Object principal, SimpleUtenteWebDto simpleUtenteWebDto, Object credentials, Collection<? extends GrantedAuthority> authorities,
                             SimpleEntitaOrganizzativaWebDto sede) {
        super(principal, credentials, authorities);
        this.sede = sede;
        this.utente = simpleUtenteWebDto;
    }

    public SimpleEntitaOrganizzativaWebDto getSede() {
        return sede;
    }

    public SimpleUtenteWebDto getUtente() {
        return utente;
    }
}
