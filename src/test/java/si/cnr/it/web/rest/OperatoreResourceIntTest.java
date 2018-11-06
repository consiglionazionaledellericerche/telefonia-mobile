package si.cnr.it.web.rest;

import si.cnr.it.TelefoniApp;

import si.cnr.it.domain.Operatore;
import si.cnr.it.domain.Telefono;
import si.cnr.it.repository.OperatoreRepository;
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
 * Test class for the OperatoreResource REST controller.
 *
 * @see OperatoreResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniApp.class)
public class OperatoreResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private OperatoreRepository operatoreRepository;

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
        final OperatoreResource operatoreResource = new OperatoreResource(operatoreRepository);
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
            .nome(DEFAULT_NOME);
        // Add required entity
        Telefono telefono = TelefonoResourceIntTest.createEntity(em);
        em.persist(telefono);
        em.flush();
        operatore.setTelefono_operatore(telefono);
        return operatore;
    }

    @Before
    public void initTest() {
        operatore = createEntity(em);
    }

    @Test
    @Transactional
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
        assertThat(testOperatore.getNome()).isEqualTo(DEFAULT_NOME);
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
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = operatoreRepository.findAll().size();
        // set the field null
        operatore.setNome(null);

        // Create the Operatore, which fails.

        restOperatoreMockMvc.perform(post("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(operatore)))
            .andExpect(status().isBadRequest());

        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOperatores() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        // Get all the operatoreList
        restOperatoreMockMvc.perform(get("/api/operatores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operatore.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
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
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
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
    public void updateOperatore() throws Exception {
        // Initialize the database
        operatoreRepository.saveAndFlush(operatore);

        int databaseSizeBeforeUpdate = operatoreRepository.findAll().size();

        // Update the operatore
        Operatore updatedOperatore = operatoreRepository.findById(operatore.getId()).get();
        // Disconnect from session so that the updates on updatedOperatore are not directly saved in db
        em.detach(updatedOperatore);
        updatedOperatore
            .nome(UPDATED_NOME);

        restOperatoreMockMvc.perform(put("/api/operatores")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOperatore)))
            .andExpect(status().isOk());

        // Validate the Operatore in the database
        List<Operatore> operatoreList = operatoreRepository.findAll();
        assertThat(operatoreList).hasSize(databaseSizeBeforeUpdate);
        Operatore testOperatore = operatoreList.get(operatoreList.size() - 1);
        assertThat(testOperatore.getNome()).isEqualTo(UPDATED_NOME);
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
