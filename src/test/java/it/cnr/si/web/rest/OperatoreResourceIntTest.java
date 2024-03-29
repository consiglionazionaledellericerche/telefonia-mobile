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
import org.springframework.security.test.context.support.WithMockUser;
import it.cnr.si.TelefoniaApp;

import it.cnr.si.domain.Operatore;
import it.cnr.si.domain.Telefono;
import it.cnr.si.domain.ListaOperatori;
import it.cnr.si.repository.OperatoreRepository;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the OperatoreResource REST controller.
 *
 * @see OperatoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniaApp.class)
public class OperatoreResourceIntTest {

    private static final Instant DEFAULT_DATA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA = Instant.ofEpochMilli(0L);

    private static final Instant DEFAULT_DATA_FINE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FINE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NUMERO_CONTRATTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CONTRATTO = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTRATTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTRATTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTRATTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTRATTO_CONTENT_TYPE = "image/png";

    @Autowired
    private OperatoreRepository operatoreRepository;
    @Autowired
    private OperatoreResource operatoreResource;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOperatoreMockMvc;

    private Operatore operatore;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
     //   final OperatoreResource operatoreResource = new OperatoreResource(operatoreRepository);
        this.restOperatoreMockMvc = MockMvcBuilders.standaloneSetup(operatoreResource)
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
    public static Operatore createEntity(EntityManager em) {
        Operatore operatore = new Operatore()
            .data(DEFAULT_DATA)
            .dataFine(DEFAULT_DATA_FINE)
            .numeroContratto(DEFAULT_NUMERO_CONTRATTO)
            .contratto(DEFAULT_CONTRATTO)
            .contrattoContentType(DEFAULT_CONTRATTO_CONTENT_TYPE);
        // Add required entity
        Telefono telefono = TelefonoResourceIntTest.createEntity(em);
        em.persist(telefono);
        em.flush();
        operatore.setTelefonoOperatore(telefono);
        // Add required entity
        ListaOperatori listaOperatori = ListaOperatoriResourceIntTest.createEntity(em);
        em.persist(listaOperatori);
        em.flush();
        operatore.setListaOperatori(listaOperatori);
        return operatore;
    }

    @Before
    public void initTest() {
        operatore = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void createOperatore() throws Exception {
        int databaseSizeBeforeCreate = operatoreRepository.findAll().size();

        // Create the Operatore
        restOperatoreMockMvc.perform(post("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isCreated());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeCreate + 1);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createOperatoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = operatoreRepository.findAll().size();

        // Create the Operatore with an existing ID
        operatore.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperatoreMockMvc.perform(post("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void getAllOperatores() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        // Get all the operatoreList
        restOperatoreMockMvc.perform(get("/api/operatores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operatore.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].dataFine").value(hasItem(DEFAULT_DATA_FINE.toString())))
            .andExpect(jsonPath("$.[*].numeroContratto").value(hasItem(DEFAULT_NUMERO_CONTRATTO.toString())))
            .andExpect(jsonPath("$.[*].contrattoContentType").value(hasItem(DEFAULT_CONTRATTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].contratto").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTRATTO))));
    }

    @Test
    @Transactional
    public void getOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        // Get the operatore
        restOperatoreMockMvc.perform(get("/api/operatores/{id}", operatore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(operatore.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.dataFine").value(DEFAULT_DATA_FINE.toString()))
            .andExpect(jsonPath("$.numeroContratto").value(DEFAULT_NUMERO_CONTRATTO.toString()))
            .andExpect(jsonPath("$.contrattoContentType").value(DEFAULT_CONTRATTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.contratto").value(Base64Utils.encodeToString(DEFAULT_CONTRATTO)));
    }

    @Test
    @Transactional
    public void getNonExistingOperatore() throws Exception {
        // Get the operatore
        restOperatoreMockMvc.perform(get("/api/operatores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username= DomainUserDetailsServiceIntTest.ACE_USER_ADMIN,roles={"USER","ADMIN"})
    public void updateOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Update the operatore
        Operatore updatedOperatore = operatoreRepository.findById(operatore.getId()).get();
        // Disconnect from session so that the updates on updatedOperatore are not directly saved in db
        em.detach(updatedOperatore);
        updatedOperatore
            .data(UPDATED_DATA);

        restOperatoreMockMvc.perform(put("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOperatore)))
            .andExpect(status().isOk());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingOperatore() throws Exception {
        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Create the Operatore

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperatoreMockMvc.perform(put("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeDelete = operatoreRepository.findAll().size();

        // Get the operatore
        restOperatoreMockMvc.perform(delete("/api/operatores/{id}", operatore.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Operatore.class);
        Operatore operatore1 = new Operatore();
        operatore1.setId(1L);
        Operatore operatore2 = new Operatore();
        operatore2.setId(operatore1.getId());
        assertThat(operatore1).isEqualTo(operatore2);
        operatore2.setId(2L);
        assertThat(operatore1).isNotEqualTo(operatore2);
        operatore1.setId(null);
        assertThat(operatore1).isNotEqualTo(operatore2);
    }
}
