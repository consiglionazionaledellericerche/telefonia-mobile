package si.cnr.it.web.rest;

import si.cnr.it.TelefoniApp;

import si.cnr.it.domain.Telefonoservizi;
import si.cnr.it.domain.Telefono;
import si.cnr.it.domain.Servizi;
import si.cnr.it.repository.TelefonoserviziRepository;
import si.cnr.it.web.rest.errors.ExceptionTranslator;

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


import static si.cnr.it.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TelefonoserviziResource REST controller.
 *
 * @see TelefonoserviziResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniApp.class)
public class TelefonoserviziResourceIntTest {

    private static final String DEFAULT_ALTRO = "AAAAAAAAAA";
    private static final String UPDATED_ALTRO = "BBBBBBBBBB";

    @Autowired
    private TelefonoserviziRepository telefonoserviziRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelefonoserviziMockMvc;

    private Telefonoservizi telefonoservizi;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelefonoserviziResource telefonoserviziResource = new TelefonoserviziResource(telefonoserviziRepository);
        this.restTelefonoserviziMockMvc = MockMvcBuilders.standaloneSetup(telefonoserviziResource)
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
    public static Telefonoservizi createEntity(EntityManager em) {
        Telefonoservizi telefonoservizi = new Telefonoservizi()
            .altro(DEFAULT_ALTRO);
        // Add required entity
        Telefono telefono = TelefonoResourceIntTest.createEntity(em);
        em.persist(telefono);
        em.flush();
        telefonoservizi.setTelefono_telser(telefono);
        // Add required entity
        Servizi servizi = ServiziResourceIntTest.createEntity(em);
        em.persist(servizi);
        em.flush();
        telefonoservizi.setServizi_telser(servizi);
        return telefonoservizi;
    }

    @Before
    public void initTest() {
        telefonoservizi = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelefonoservizi() throws Exception {
        int databaseSizeBeforeCreate = telefonoserviziRepository.findAll().size();

        // Create the Telefonoservizi
        restTelefonoserviziMockMvc.perform(post("/api/telefonoservizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoservizi)))
            .andExpect(status().isCreated());

        // Validate the Telefonoservizi in the database
        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeCreate + 1);
        Telefonoservizi testTelefonoservizi = telefonoserviziList.get(telefonoserviziList.size() - 1);
        assertThat(testTelefonoservizi.getAltro()).isEqualTo(DEFAULT_ALTRO);
    }

    @Test
    @Transactional
    public void createTelefonoserviziWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telefonoserviziRepository.findAll().size();

        // Create the Telefonoservizi with an existing ID
        telefonoservizi.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelefonoserviziMockMvc.perform(post("/api/telefonoservizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoservizi)))
            .andExpect(status().isBadRequest());

        // Validate the Telefonoservizi in the database
        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAltroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonoserviziRepository.findAll().size();
        // set the field null
        telefonoservizi.setAltro(null);

        // Create the Telefonoservizi, which fails.

        restTelefonoserviziMockMvc.perform(post("/api/telefonoservizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoservizi)))
            .andExpect(status().isBadRequest());

        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefonoservizis() throws Exception {
        // Initialize the database
        telefonoserviziRepository.saveAndFlush(telefonoservizi);

        // Get all the telefonoserviziList
        restTelefonoserviziMockMvc.perform(get("/api/telefonoservizis?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefonoservizi.getId().intValue())))
            .andExpect(jsonPath("$.[*].altro").value(hasItem(DEFAULT_ALTRO.toString())));
    }
    
    @Test
    @Transactional
    public void getTelefonoservizi() throws Exception {
        // Initialize the database
        telefonoserviziRepository.saveAndFlush(telefonoservizi);

        // Get the telefonoservizi
        restTelefonoserviziMockMvc.perform(get("/api/telefonoservizis/{id}", telefonoservizi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(telefonoservizi.getId().intValue()))
            .andExpect(jsonPath("$.altro").value(DEFAULT_ALTRO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTelefonoservizi() throws Exception {
        // Get the telefonoservizi
        restTelefonoserviziMockMvc.perform(get("/api/telefonoservizis/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefonoservizi() throws Exception {
        // Initialize the database
        telefonoserviziRepository.saveAndFlush(telefonoservizi);

        int databaseSizeBeforeUpdate = telefonoserviziRepository.findAll().size();

        // Update the telefonoservizi
        Telefonoservizi updatedTelefonoservizi = telefonoserviziRepository.findById(telefonoservizi.getId()).get();
        // Disconnect from session so that the updates on updatedTelefonoservizi are not directly saved in db
        em.detach(updatedTelefonoservizi);
        updatedTelefonoservizi
            .altro(UPDATED_ALTRO);

        restTelefonoserviziMockMvc.perform(put("/api/telefonoservizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTelefonoservizi)))
            .andExpect(status().isOk());

        // Validate the Telefonoservizi in the database
        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeUpdate);
        Telefonoservizi testTelefonoservizi = telefonoserviziList.get(telefonoserviziList.size() - 1);
        assertThat(testTelefonoservizi.getAltro()).isEqualTo(UPDATED_ALTRO);
    }

    @Test
    @Transactional
    public void updateNonExistingTelefonoservizi() throws Exception {
        int databaseSizeBeforeUpdate = telefonoserviziRepository.findAll().size();

        // Create the Telefonoservizi

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelefonoserviziMockMvc.perform(put("/api/telefonoservizis")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefonoservizi)))
            .andExpect(status().isBadRequest());

        // Validate the Telefonoservizi in the database
        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelefonoservizi() throws Exception {
        // Initialize the database
        telefonoserviziRepository.saveAndFlush(telefonoservizi);

        int databaseSizeBeforeDelete = telefonoserviziRepository.findAll().size();

        // Get the telefonoservizi
        restTelefonoserviziMockMvc.perform(delete("/api/telefonoservizis/{id}", telefonoservizi.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Telefonoservizi> telefonoserviziList = telefonoserviziRepository.findAll();
        assertThat(telefonoserviziList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Telefonoservizi.class);
        Telefonoservizi telefonoservizi1 = new Telefonoservizi();
        telefonoservizi1.setId(1L);
        Telefonoservizi telefonoservizi2 = new Telefonoservizi();
        telefonoservizi2.setId(telefonoservizi1.getId());
        assertThat(telefonoservizi1).isEqualTo(telefonoservizi2);
        telefonoservizi2.setId(2L);
        assertThat(telefonoservizi1).isNotEqualTo(telefonoservizi2);
        telefonoservizi1.setId(null);
        assertThat(telefonoservizi1).isNotEqualTo(telefonoservizi2);
    }
}
