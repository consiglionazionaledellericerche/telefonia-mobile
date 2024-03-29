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
import it.cnr.si.repository.TelefonoServiziRepository;
import org.springframework.security.test.context.support.WithMockUser;
import it.cnr.si.TelefoniaApp;

import it.cnr.si.domain.TelefonoServizi;
import it.cnr.si.domain.Servizi;
import it.cnr.si.domain.Telefono;
import it.cnr.si.repository.TelefonoServiziRepository;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static it.cnr.si.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TelefonoServiziResource REST controller.
 *
 * @see TelefonoServiziResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniaApp.class)
public class TelefonoServiziResourceIntTest {

    private static final String DEFAULT_ALTRO = "AAAAAAAAAA";
    private static final String UPDATED_ALTRO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_INIZIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INIZIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private TelefonoServiziRepository telefonoServiziRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private TelefonoServiziResource telefonoServiziResource;

    @Autowired
    private EntityManager em;

    private MockMvc restTelefonoServiziMockMvc;

    private TelefonoServizi telefonoServizi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.restTelefonoServiziMockMvc = MockMvcBuilders.standaloneSetup(telefonoServiziResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(TestUtil.createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TelefonoServizi createEntity(EntityManager em) {
        TelefonoServizi telefonoServizi = new TelefonoServizi()
            .altro(DEFAULT_ALTRO)
            .dataInizio(DEFAULT_DATA_INIZIO)
            .dataFine(DEFAULT_DATA_FINE);
        // Add required entity
        Servizi servizi = ServiziResourceIntTest.createEntity(em);
        em.persist(servizi);
        em.flush();
        telefonoServizi.setServizi(servizi);
        // Add required entity
        Telefono telefono = TelefonoResourceIntTest.createEntity(em);
        em.persist(telefono);
        em.flush();
        telefonoServizi.setTelefono(telefono);
        return telefonoServizi;
    }

    @Before
    public void initTest() {
        telefonoServizi = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void createTelefonoServizi() throws Exception {
        int databaseSizeBeforeCreate = telefonoServiziRepository.findAll().size();

        // Create the TelefonoServizi
        restTelefonoServiziMockMvc.perform(post("/api/telefono-servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoServizi)))
            .andExpect(status().isCreated());

        // Validate the TelefonoServizi in the database
        List<TelefonoServizi> telefonoServiziList = telefonoServiziRepository.findAll();
        assertThat(telefonoServiziList).hasSize(databaseSizeBeforeCreate + 1);
        TelefonoServizi testTelefonoServizi = telefonoServiziList.get(telefonoServiziList.size() - 1);
        assertThat(testTelefonoServizi.getAltro()).isEqualTo(DEFAULT_ALTRO);
    }

    @Test
    @Transactional
    public void createTelefonoServiziWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telefonoServiziRepository.findAll().size();

        // Create the TelefonoServizi with an existing ID
        telefonoServizi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelefonoServiziMockMvc.perform(post("/api/telefono-servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoServizi)))
            .andExpect(status().isBadRequest());

        // Validate the TelefonoServizi in the database
        List<TelefonoServizi> telefonoServiziList = telefonoServiziRepository.findAll();
        assertThat(telefonoServiziList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void getAllTelefonoServizis() throws Exception {
        // Initialize the database
        telefonoServiziRepository.saveAndFlush(telefonoServizi);

        // Get all the telefonoServiziList
        restTelefonoServiziMockMvc.perform(get("/api/telefono-servizis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefonoServizi.getId().intValue())))
            .andExpect(jsonPath("$.[*].altro").value(hasItem(DEFAULT_ALTRO.toString())))
            .andExpect(jsonPath("$.[*].dataInizio").value(hasItem(DEFAULT_DATA_INIZIO.toString())))
            .andExpect(jsonPath("$.[*].dataFine").value(hasItem(DEFAULT_DATA_FINE.toString())));
    }

    @Test
    @Transactional
    public void getTelefonoServizi() throws Exception {
        // Initialize the database
        telefonoServiziRepository.saveAndFlush(telefonoServizi);

        // Get the telefonoServizi
        restTelefonoServiziMockMvc.perform(get("/api/telefono-servizis/{id}", telefonoServizi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(telefonoServizi.getId().intValue()))
            .andExpect(jsonPath("$.altro").value(DEFAULT_ALTRO.toString()))
            .andExpect(jsonPath("$.dataInizio").value(DEFAULT_DATA_INIZIO.toString()))
            .andExpect(jsonPath("$.dataFine").value(DEFAULT_DATA_FINE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTelefonoServizi() throws Exception {
        // Get the telefonoServizi
        restTelefonoServiziMockMvc.perform(get("/api/telefono-servizis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void updateTelefonoServizi() throws Exception {
        // Initialize the database
        telefonoServiziRepository.saveAndFlush(telefonoServizi);

        int databaseSizeBeforeUpdate = telefonoServiziRepository.findAll().size();

        // Update the telefonoServizi
        TelefonoServizi updatedTelefonoServizi = telefonoServiziRepository.findById(telefonoServizi.getId()).get();
        // Disconnect from session so that the updates on updatedTelefonoServizi are not directly saved in db
        em.detach(updatedTelefonoServizi);
        updatedTelefonoServizi
            .altro(UPDATED_ALTRO);

        restTelefonoServiziMockMvc.perform(put("/api/telefono-servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTelefonoServizi)))
            .andExpect(status().isOk());

        // Validate the TelefonoServizi in the database
        List<TelefonoServizi> telefonoServiziList = telefonoServiziRepository.findAll();
        assertThat(telefonoServiziList).hasSize(databaseSizeBeforeUpdate);
        TelefonoServizi testTelefonoServizi = telefonoServiziList.get(telefonoServiziList.size() - 1);
        assertThat(testTelefonoServizi.getAltro()).isEqualTo(UPDATED_ALTRO);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void updateNonExistingTelefonoServizi() throws Exception {
        int databaseSizeBeforeUpdate = telefonoServiziRepository.findAll().size();

        // Create the TelefonoServizi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelefonoServiziMockMvc.perform(put("/api/telefono-servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoServizi)))
            .andExpect(status().isBadRequest());

        // Validate the TelefonoServizi in the database
        List<TelefonoServizi> telefonoServiziList = telefonoServiziRepository.findAll();
        assertThat(telefonoServiziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void deleteTelefonoServizi() throws Exception {
        // Initialize the database
        telefonoServiziRepository.saveAndFlush(telefonoServizi);

        int databaseSizeBeforeDelete = telefonoServiziRepository.findAll().size();

        // Get the telefonoServizi
        restTelefonoServiziMockMvc.perform(delete("/api/telefono-servizis/{id}", telefonoServizi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TelefonoServizi> telefonoServiziList = telefonoServiziRepository.findAll();
        assertThat(telefonoServiziList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TelefonoServizi.class);
        TelefonoServizi telefonoServizi1 = new TelefonoServizi();
        telefonoServizi1.setId(1L);
        TelefonoServizi telefonoServizi2 = new TelefonoServizi();
        telefonoServizi2.setId(telefonoServizi1.getId());
        assertThat(telefonoServizi1).isEqualTo(telefonoServizi2);
        telefonoServizi2.setId(2L);
        assertThat(telefonoServizi1).isNotEqualTo(telefonoServizi2);
        telefonoServizi1.setId(null);
        assertThat(telefonoServizi1).isNotEqualTo(telefonoServizi2);
    }
}
