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

import it.cnr.si.repository.ListaOperatoriRepository;
import it.cnr.si.TelefoniaApp;

import it.cnr.si.domain.ListaOperatori;
import it.cnr.si.repository.ListaOperatoriRepository;
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
import java.util.List;


import static it.cnr.si.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ListaOperatoriResource REST controller.
 *
 * @see ListaOperatoriResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniaApp.class)
public class ListaOperatoriResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private ListaOperatoriRepository listaOperatoriRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restListaOperatoriMockMvc;

    private ListaOperatori listaOperatori;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ListaOperatoriResource listaOperatoriResource = new ListaOperatoriResource(listaOperatoriRepository);
        this.restListaOperatoriMockMvc = MockMvcBuilders.standaloneSetup(listaOperatoriResource)
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
    public static ListaOperatori createEntity(EntityManager em) {
        ListaOperatori listaOperatori = new ListaOperatori()
            .nome(DEFAULT_NOME);
        return listaOperatori;
    }

    @Before
    public void initTest() {
        listaOperatori = createEntity(em);
    }

    @Test
    @Transactional
    public void createListaOperatori() throws Exception {
        int databaseSizeBeforeCreate = listaOperatoriRepository.findAll().size();

        // Create the ListaOperatori
        restListaOperatoriMockMvc.perform(post("/api/lista-operatoris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listaOperatori)))
            .andExpect(status().isCreated());

        // Validate the ListaOperatori in the database
        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeCreate + 1);
        ListaOperatori testListaOperatori = listaOperatoriList.get(listaOperatoriList.size() - 1);
        assertThat(testListaOperatori.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createListaOperatoriWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = listaOperatoriRepository.findAll().size();

        // Create the ListaOperatori with an existing ID
        listaOperatori.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restListaOperatoriMockMvc.perform(post("/api/lista-operatoris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listaOperatori)))
            .andExpect(status().isBadRequest());

        // Validate the ListaOperatori in the database
        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = listaOperatoriRepository.findAll().size();
        // set the field null
        listaOperatori.setNome(null);

        // Create the ListaOperatori, which fails.

        restListaOperatoriMockMvc.perform(post("/api/lista-operatoris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listaOperatori)))
            .andExpect(status().isBadRequest());

        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllListaOperatoris() throws Exception {
        // Initialize the database
        listaOperatoriRepository.saveAndFlush(listaOperatori);

        // Get all the listaOperatoriList
        restListaOperatoriMockMvc.perform(get("/api/lista-operatoris?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listaOperatori.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getListaOperatori() throws Exception {
        // Initialize the database
        listaOperatoriRepository.saveAndFlush(listaOperatori);

        // Get the listaOperatori
        restListaOperatoriMockMvc.perform(get("/api/lista-operatoris/{id}", listaOperatori.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(listaOperatori.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingListaOperatori() throws Exception {
        // Get the listaOperatori
        restListaOperatoriMockMvc.perform(get("/api/lista-operatoris/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListaOperatori() throws Exception {
        // Initialize the database
        listaOperatoriRepository.saveAndFlush(listaOperatori);

        int databaseSizeBeforeUpdate = listaOperatoriRepository.findAll().size();

        // Update the listaOperatori
        ListaOperatori updatedListaOperatori = listaOperatoriRepository.findById(listaOperatori.getId()).get();
        // Disconnect from session so that the updates on updatedListaOperatori are not directly saved in db
        em.detach(updatedListaOperatori);
        updatedListaOperatori
            .nome(UPDATED_NOME);

        restListaOperatoriMockMvc.perform(put("/api/lista-operatoris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedListaOperatori)))
            .andExpect(status().isOk());

        // Validate the ListaOperatori in the database
        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeUpdate);
        ListaOperatori testListaOperatori = listaOperatoriList.get(listaOperatoriList.size() - 1);
        assertThat(testListaOperatori.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingListaOperatori() throws Exception {
        int databaseSizeBeforeUpdate = listaOperatoriRepository.findAll().size();

        // Create the ListaOperatori

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListaOperatoriMockMvc.perform(put("/api/lista-operatoris")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listaOperatori)))
            .andExpect(status().isBadRequest());

        // Validate the ListaOperatori in the database
        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteListaOperatori() throws Exception {
        // Initialize the database
        listaOperatoriRepository.saveAndFlush(listaOperatori);

        int databaseSizeBeforeDelete = listaOperatoriRepository.findAll().size();

        // Get the listaOperatori
        restListaOperatoriMockMvc.perform(delete("/api/lista-operatoris/{id}", listaOperatori.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ListaOperatori> listaOperatoriList = listaOperatoriRepository.findAll();
        assertThat(listaOperatoriList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListaOperatori.class);
        ListaOperatori listaOperatori1 = new ListaOperatori();
        listaOperatori1.setId(1L);
        ListaOperatori listaOperatori2 = new ListaOperatori();
        listaOperatori2.setId(listaOperatori1.getId());
        assertThat(listaOperatori1).isEqualTo(listaOperatori2);
        listaOperatori2.setId(2L);
        assertThat(listaOperatori1).isNotEqualTo(listaOperatori2);
        listaOperatori1.setId(null);
        assertThat(listaOperatori1).isNotEqualTo(listaOperatori2);
    }
}
