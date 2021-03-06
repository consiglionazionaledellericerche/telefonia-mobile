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

package it.cnr.si.service;

import it.cnr.si.service.dto.anagrafica.base.NodeDto;
import it.cnr.si.service.dto.anagrafica.letture.EntitaOrganizzativaWebDto;
import it.cnr.si.service.dto.anagrafica.simpleweb.SimpleEntitaOrganizzativaWebDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CacheService {

    public static final String ACE_GERARCHIA_ISTITUTI = "ACE-GERARCHIA-ISTITUTI";
    public static final String ACE_GERARCHIA_UFFICI = "ACE-GERARCHIA-UFFICI";
    public static final String ACE_SEDE_LAVORO = "ACE-SEDE-LAVORO";
    @Autowired
    CacheManager cacheManager;
    @Autowired
    private AceService aceService;

    @Cacheable(ACE_GERARCHIA_ISTITUTI)
    public List<NodeDto> getGerarchiaIstituti() {
        return aceService.getGerarchiaIstituti();
    }

    @Cacheable(ACE_GERARCHIA_UFFICI)
    public List<NodeDto> getGerarchiaUffici() {
        return aceService.getGerarchiaUffici();
    }

    public List<SimpleEntitaOrganizzativaWebDto> getSediDiLavoro() {
        return aceService.entitaOrganizzativaFind(null, null, LocalDate.now(), null);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void evictAllcachesAtIntervals() {
        evictAllCaches();
    }

    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
            .filter(s -> s.equals(ACE_GERARCHIA_ISTITUTI) || s.equals(ACE_GERARCHIA_UFFICI) || s.equals(ACE_SEDE_LAVORO))
            .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}
