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

package it.cnr.si.web.rest;

import it.cnr.si.TelefoniaApp;
import it.cnr.si.repository.StoricoTelefonoRepository;
import it.cnr.si.TelefoniaApp;

import it.cnr.si.domain.StoricoTelefono;
import it.cnr.si.domain.Telefono;
import it.cnr.si.repository.StoricoTelefonoRepository;
import it.cnr.si.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static it.cnr.si.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StoricoTelefonoResource REST controller.
 *
 * @see StoricoTelefonoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniaApp.class)
public class StoricoTelefonoResourceIntTest {

    private static final Instant DEFAULT_DATA_MODIFICA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_MODIFICA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_ATTIVAZIONE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_ATTIVAZIONE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_CESSAZIONE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_CESSAZIONE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INTESTATARIO_CONTRATTO = "AAAAAAAAAA";
    private static final String UPDATED_INTESTATARIO_CONTRATTO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_CONTRATTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CONTRATTO = "BBBBBBBBBB";

    private static final String DEFAULT_UTILIZZATORE_UTENZA = "AAAAAAAAAA";
    private static final String UPDATED_UTILIZZATORE_UTENZA = "BBBBBBBBBB";

    private static final String DEFAULT_CDSUO = "AAAAAAAAAA";
    private static final String UPDATED_CDSUO = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATORE = "AAAAAAAAAA";
    private static final String UPDATED_OPERATORE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVIZI = "AAAAAAAAAA";
    private static final String UPDATED_SERVIZI = "BBBBBBBBBB";

    private static final String DEFAULT_USER_MODIFICA = "AAAAAAAAAA";
    private static final String UPDATED_USER_MODIFICA = "BBBBBBBBBB";

    private static final String DEFAULT_VERSIONE = "AAAAAAAAAA";
    private static final String UPDATED_VERSIONE = "BBBBBBBBBB";

    @Autowired
    private StoricoTelefonoRepository storicoTelefonoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStoricoTelefonoMockMvc;

    private StoricoTelefono storicoTelefono;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StoricoTelefonoResource storicoTelefonoResource = new StoricoTelefonoResource(storicoTelefonoRepository);
        this.restStoricoTelefonoMockMvc = MockMvcBuilders.standaloneSetup(storicoTelefonoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoricoTelefono createEntity(EntityManager em) {
        StoricoTelefono storicoTelefono = new StoricoTelefono()
            .dataModifica(DEFAULT_DATA_MODIFICA)
            .dataAttivazione(DEFAULT_DATA_ATTIVAZIONE)
            .dataCessazione(DEFAULT_DATA_CESSAZIONE)
            .intestatarioContratto(DEFAULT_INTESTATARIO_CONTRATTO)
            .numeroContratto(DEFAULT_NUMERO_CONTRATTO)
            .utilizzatoreUtenza(DEFAULT_UTILIZZATORE_UTENZA)
            .cdsuo(DEFAULT_CDSUO)
            .operatore(DEFAULT_OPERATORE)
            .servizi(DEFAULT_SERVIZI)
            .userModifica(DEFAULT_USER_MODIFICA)
            .versione(DEFAULT_VERSIONE);
        // Add required entity
        Telefono telefono = TelefonoResourceIntTest.createEntity(em);
        em.persist(telefono);
        em.flush();
        storicoTelefono.setStoricotelefonoTelefono(telefono);
        return storicoTelefono;
    }

    @Before
    public void initTest() {
        storicoTelefono = createEntity(em);
    }

    @Test
    @Transactional
    public void createStoricoTelefono() throws Exception {
        int databaseSizeBeforeCreate = storicoTelefonoRepository.findAll().size();

        // Create the StoricoTelefono
        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isCreated());

        // Validate the StoricoTelefono in the database
        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeCreate + 1);
        StoricoTelefono testStoricoTelefono = storicoTelefonoList.get(storicoTelefonoList.size() - 1);
        assertThat(testStoricoTelefono.getDataModifica()).isEqualTo(DEFAULT_DATA_MODIFICA);
        assertThat(testStoricoTelefono.getDataAttivazione()).isEqualTo(DEFAULT_DATA_ATTIVAZIONE);
        assertThat(testStoricoTelefono.getDataCessazione()).isEqualTo(DEFAULT_DATA_CESSAZIONE);
        assertThat(testStoricoTelefono.getIntestatarioContratto()).isEqualTo(DEFAULT_INTESTATARIO_CONTRATTO);
        assertThat(testStoricoTelefono.getNumeroContratto()).isEqualTo(DEFAULT_NUMERO_CONTRATTO);
        assertThat(testStoricoTelefono.getUtilizzatoreUtenza()).isEqualTo(DEFAULT_UTILIZZATORE_UTENZA);
        assertThat(testStoricoTelefono.getCdsuo()).isEqualTo(DEFAULT_CDSUO);
        assertThat(testStoricoTelefono.getOperatore()).isEqualTo(DEFAULT_OPERATORE);
        assertThat(testStoricoTelefono.getServizi()).isEqualTo(DEFAULT_SERVIZI);
        assertThat(testStoricoTelefono.getUserModifica()).isEqualTo(DEFAULT_USER_MODIFICA);
        assertThat(testStoricoTelefono.getVersione()).isEqualTo(DEFAULT_VERSIONE);
    }

    @Test
    @Transactional
    public void createStoricoTelefonoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = storicoTelefonoRepository.findAll().size();

        // Create the StoricoTelefono with an existing ID
        storicoTelefono.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        // Validate the StoricoTelefono in the database
        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDataModificaIsRequired() throws Exception {
        int databaseSizeBeforeTest = storicoTelefonoRepository.findAll().size();
        // set the field null
        storicoTelefono.setDataModifica(null);

        // Create the StoricoTelefono, which fails.

        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataAttivazioneIsRequired() throws Exception {
        int databaseSizeBeforeTest = storicoTelefonoRepository.findAll().size();
        // set the field null
        storicoTelefono.setDataAttivazione(null);

        // Create the StoricoTelefono, which fails.

        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIntestatarioContrattoIsRequired() throws Exception {
        int databaseSizeBeforeTest = storicoTelefonoRepository.findAll().size();
        // set the field null
        storicoTelefono.setIntestatarioContratto(null);

        // Create the StoricoTelefono, which fails.

        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserModificaIsRequired() throws Exception {
        int databaseSizeBeforeTest = storicoTelefonoRepository.findAll().size();
        // set the field null
        storicoTelefono.setUserModifica(null);

        // Create the StoricoTelefono, which fails.

        restStoricoTelefonoMockMvc.perform(post("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStoricoTelefonos() throws Exception {
        // Initialize the database
        storicoTelefonoRepository.saveAndFlush(storicoTelefono);

        // Get all the storicoTelefonoList
        restStoricoTelefonoMockMvc.perform(get("/api/storico-telefonos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storicoTelefono.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataModifica").value(hasItem(DEFAULT_DATA_MODIFICA.toString())))
            .andExpect(jsonPath("$.[*].dataAttivazione").value(hasItem(DEFAULT_DATA_ATTIVAZIONE.toString())))
            .andExpect(jsonPath("$.[*].dataCessazione").value(hasItem(DEFAULT_DATA_CESSAZIONE.toString())))
            .andExpect(jsonPath("$.[*].intestatarioContratto").value(hasItem(DEFAULT_INTESTATARIO_CONTRATTO.toString())))
            .andExpect(jsonPath("$.[*].numeroContratto").value(hasItem(DEFAULT_NUMERO_CONTRATTO.toString())))
            .andExpect(jsonPath("$.[*].utilizzatoreUtenza").value(hasItem(DEFAULT_UTILIZZATORE_UTENZA.toString())))
            .andExpect(jsonPath("$.[*].cdsuo").value(hasItem(DEFAULT_CDSUO.toString())))
            .andExpect(jsonPath("$.[*].operatore").value(hasItem(DEFAULT_OPERATORE.toString())))
            .andExpect(jsonPath("$.[*].servizi").value(hasItem(DEFAULT_SERVIZI.toString())))
            .andExpect(jsonPath("$.[*].userModifica").value(hasItem(DEFAULT_USER_MODIFICA.toString())))
            .andExpect(jsonPath("$.[*].versione").value(hasItem(DEFAULT_VERSIONE.toString())));
    }

    @Test
    @Transactional
    public void getStoricoTelefono() throws Exception {
        // Initialize the database
        storicoTelefonoRepository.saveAndFlush(storicoTelefono);

        // Get the storicoTelefono
        restStoricoTelefonoMockMvc.perform(get("/api/storico-telefonos/{id}", storicoTelefono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(storicoTelefono.getId().intValue()))
            .andExpect(jsonPath("$.dataModifica").value(DEFAULT_DATA_MODIFICA.toString()))
            .andExpect(jsonPath("$.dataAttivazione").value(DEFAULT_DATA_ATTIVAZIONE.toString()))
            .andExpect(jsonPath("$.dataCessazione").value(DEFAULT_DATA_CESSAZIONE.toString()))
            .andExpect(jsonPath("$.intestatarioContratto").value(DEFAULT_INTESTATARIO_CONTRATTO.toString()))
            .andExpect(jsonPath("$.numeroContratto").value(DEFAULT_NUMERO_CONTRATTO.toString()))
            .andExpect(jsonPath("$.utilizzatoreUtenza").value(DEFAULT_UTILIZZATORE_UTENZA.toString()))
            .andExpect(jsonPath("$.cdsuo").value(DEFAULT_CDSUO.toString()))
            .andExpect(jsonPath("$.operatore").value(DEFAULT_OPERATORE.toString()))
            .andExpect(jsonPath("$.servizi").value(DEFAULT_SERVIZI.toString()))
            .andExpect(jsonPath("$.userModifica").value(DEFAULT_USER_MODIFICA.toString()))
            .andExpect(jsonPath("$.versione").value(DEFAULT_VERSIONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStoricoTelefono() throws Exception {
        // Get the storicoTelefono
        restStoricoTelefonoMockMvc.perform(get("/api/storico-telefonos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStoricoTelefono() throws Exception {
        // Initialize the database
        storicoTelefonoRepository.saveAndFlush(storicoTelefono);

        int databaseSizeBeforeUpdate = storicoTelefonoRepository.findAll().size();

        // Update the storicoTelefono
        StoricoTelefono updatedStoricoTelefono = storicoTelefonoRepository.findById(storicoTelefono.getId()).get();
        // Disconnect from session so that the updates on updatedStoricoTelefono are not directly saved in db
        em.detach(updatedStoricoTelefono);
        updatedStoricoTelefono
            .dataModifica(UPDATED_DATA_MODIFICA)
            .dataAttivazione(UPDATED_DATA_ATTIVAZIONE)
            .dataCessazione(UPDATED_DATA_CESSAZIONE)
            .intestatarioContratto(UPDATED_INTESTATARIO_CONTRATTO)
            .numeroContratto(UPDATED_NUMERO_CONTRATTO)
            .utilizzatoreUtenza(UPDATED_UTILIZZATORE_UTENZA)
            .cdsuo(UPDATED_CDSUO)
            .operatore(UPDATED_OPERATORE)
            .servizi(UPDATED_SERVIZI)
            .userModifica(UPDATED_USER_MODIFICA)
            .versione(UPDATED_VERSIONE);

        restStoricoTelefonoMockMvc.perform(put("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStoricoTelefono)))
            .andExpect(status().isOk());

        // Validate the StoricoTelefono in the database
        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeUpdate);
        StoricoTelefono testStoricoTelefono = storicoTelefonoList.get(storicoTelefonoList.size() - 1);
        assertThat(testStoricoTelefono.getDataModifica()).isEqualTo(UPDATED_DATA_MODIFICA);
        assertThat(testStoricoTelefono.getDataAttivazione()).isEqualTo(UPDATED_DATA_ATTIVAZIONE);
        assertThat(testStoricoTelefono.getDataCessazione()).isEqualTo(UPDATED_DATA_CESSAZIONE);
        assertThat(testStoricoTelefono.getIntestatarioContratto()).isEqualTo(UPDATED_INTESTATARIO_CONTRATTO);
        assertThat(testStoricoTelefono.getNumeroContratto()).isEqualTo(UPDATED_NUMERO_CONTRATTO);
        assertThat(testStoricoTelefono.getUtilizzatoreUtenza()).isEqualTo(UPDATED_UTILIZZATORE_UTENZA);
        assertThat(testStoricoTelefono.getCdsuo()).isEqualTo(UPDATED_CDSUO);
        assertThat(testStoricoTelefono.getOperatore()).isEqualTo(UPDATED_OPERATORE);
        assertThat(testStoricoTelefono.getServizi()).isEqualTo(UPDATED_SERVIZI);
        assertThat(testStoricoTelefono.getUserModifica()).isEqualTo(UPDATED_USER_MODIFICA);
        assertThat(testStoricoTelefono.getVersione()).isEqualTo(UPDATED_VERSIONE);
    }

    @Test
    @Transactional
    public void updateNonExistingStoricoTelefono() throws Exception {
        int databaseSizeBeforeUpdate = storicoTelefonoRepository.findAll().size();

        // Create the StoricoTelefono

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoricoTelefonoMockMvc.perform(put("/api/storico-telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(storicoTelefono)))
            .andExpect(status().isBadRequest());

        // Validate the StoricoTelefono in the database
        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStoricoTelefono() throws Exception {
        // Initialize the database
        storicoTelefonoRepository.saveAndFlush(storicoTelefono);

        int databaseSizeBeforeDelete = storicoTelefonoRepository.findAll().size();

        // Get the storicoTelefono
        restStoricoTelefonoMockMvc.perform(delete("/api/storico-telefonos/{id}", storicoTelefono.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StoricoTelefono> storicoTelefonoList = storicoTelefonoRepository.findAll();
        assertThat(storicoTelefonoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StoricoTelefono.class);
        StoricoTelefono storicoTelefono1 = new StoricoTelefono();
        storicoTelefono1.setId(1L);
        StoricoTelefono storicoTelefono2 = new StoricoTelefono();
        storicoTelefono2.setId(storicoTelefono1.getId());
        assertThat(storicoTelefono1).isEqualTo(storicoTelefono2);
        storicoTelefono2.setId(2L);
        assertThat(storicoTelefono1).isNotEqualTo(storicoTelefono2);
        storicoTelefono1.setId(null);
        assertThat(storicoTelefono1).isNotEqualTo(storicoTelefono2);
    }
}
