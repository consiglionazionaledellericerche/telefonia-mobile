package it.cnr.si.web.rest;

import it.cnr.si.TelefoniaApp;
import it.cnr.si.TelefoniaApp;

import it.cnr.si.domain.Servizi;
import it.cnr.si.repository.ServiziRepository;
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
 * Test class for the ServiziResource REST controller.
 *
 * @see ServiziResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniaApp.class)
public class ServiziResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private ServiziRepository serviziRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiziMockMvc;

    private Servizi servizi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiziResource serviziResource = new ServiziResource(serviziRepository);
        this.restServiziMockMvc = MockMvcBuilders.standaloneSetup(serviziResource)
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
    public static Servizi createEntity(EntityManager em) {
        Servizi servizi = new Servizi()
            .nome(DEFAULT_NOME);
        return servizi;
    }

    @Before
    public void initTest() {
        servizi = createEntity(em);
    }

    @Test
    @Transactional
    public void createServizi() throws Exception {
        int databaseSizeBeforeCreate = serviziRepository.findAll().size();

        // Create the Servizi
        restServiziMockMvc.perform(post("/api/servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servizi)))
            .andExpect(status().isCreated());

        // Validate the Servizi in the database
        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeCreate + 1);
        Servizi testServizi = serviziList.get(serviziList.size() - 1);
        assertThat(testServizi.getNome()).isEqualTo(DEFAULT_NOME);
    }

    @Test
    @Transactional
    public void createServiziWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviziRepository.findAll().size();

        // Create the Servizi with an existing ID
        servizi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiziMockMvc.perform(post("/api/servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servizi)))
            .andExpect(status().isBadRequest());

        // Validate the Servizi in the database
        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviziRepository.findAll().size();
        // set the field null
        servizi.setNome(null);

        // Create the Servizi, which fails.

        restServiziMockMvc.perform(post("/api/servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servizi)))
            .andExpect(status().isBadRequest());

        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServizis() throws Exception {
        // Initialize the database
        serviziRepository.saveAndFlush(servizi);

        // Get all the serviziList
        restServiziMockMvc.perform(get("/api/servizis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(servizi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getServizi() throws Exception {
        // Initialize the database
        serviziRepository.saveAndFlush(servizi);

        // Get the servizi
        restServiziMockMvc.perform(get("/api/servizis/{id}", servizi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(servizi.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingServizi() throws Exception {
        // Get the servizi
        restServiziMockMvc.perform(get("/api/servizis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServizi() throws Exception {
        // Initialize the database
        serviziRepository.saveAndFlush(servizi);

        int databaseSizeBeforeUpdate = serviziRepository.findAll().size();

        // Update the servizi
        Servizi updatedServizi = serviziRepository.findById(servizi.getId()).get();
        // Disconnect from session so that the updates on updatedServizi are not directly saved in db
        em.detach(updatedServizi);
        updatedServizi
            .nome(UPDATED_NOME);

        restServiziMockMvc.perform(put("/api/servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServizi)))
            .andExpect(status().isOk());

        // Validate the Servizi in the database
        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeUpdate);
        Servizi testServizi = serviziList.get(serviziList.size() - 1);
        assertThat(testServizi.getNome()).isEqualTo(UPDATED_NOME);
    }

    @Test
    @Transactional
    public void updateNonExistingServizi() throws Exception {
        int databaseSizeBeforeUpdate = serviziRepository.findAll().size();

        // Create the Servizi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiziMockMvc.perform(put("/api/servizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(servizi)))
            .andExpect(status().isBadRequest());

        // Validate the Servizi in the database
        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteServizi() throws Exception {
        // Initialize the database
        serviziRepository.saveAndFlush(servizi);

        int databaseSizeBeforeDelete = serviziRepository.findAll().size();

        // Get the servizi
        restServiziMockMvc.perform(delete("/api/servizis/{id}", servizi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Servizi> serviziList = serviziRepository.findAll();
        assertThat(serviziList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Servizi.class);
        Servizi servizi1 = new Servizi();
        servizi1.setId(1L);
        Servizi servizi2 = new Servizi();
        servizi2.setId(servizi1.getId());
        assertThat(servizi1).isEqualTo(servizi2);
        servizi2.setId(2L);
        assertThat(servizi1).isNotEqualTo(servizi2);
        servizi1.setId(null);
        assertThat(servizi1).isNotEqualTo(servizi2);
    }
}
