package si.cnr.it.web.rest;

import si.cnr.it.TelefoniApp;

import si.cnr.it.domain.Telefono;
import si.cnr.it.domain.Utenza;
import si.cnr.it.domain.Istituto;
import si.cnr.it.repository.TelefonoRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static si.cnr.it.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TelefonoResource REST controller.
 *
 * @see TelefonoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TelefoniApp.class)
public class TelefonoResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATAATTIVAZIONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATAATTIVAZIONE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATACESSAZIONE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATACESSAZIONE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INTESTATARIOCONTRATTO = "AAAAAAAAAA";
    private static final String UPDATED_INTESTATARIOCONTRATTO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMEROCONTRATTO = "AAAAAAAAAA";
    private static final String UPDATED_NUMEROCONTRATTO = "BBBBBBBBBB";

    @Autowired
    private TelefonoRepository telefonoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTelefonoMockMvc;

    private Telefono telefono;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TelefonoResource telefonoResource = new TelefonoResource(telefonoRepository);
        this.restTelefonoMockMvc = MockMvcBuilders.standaloneSetup(telefonoResource)
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
    public static Telefono createEntity(EntityManager em) {
        Telefono telefono = new Telefono()
            .numero(DEFAULT_NUMERO)
            .dataattivazione(DEFAULT_DATAATTIVAZIONE)
            .datacessazione(DEFAULT_DATACESSAZIONE)
            .intestatariocontratto(DEFAULT_INTESTATARIOCONTRATTO)
            .numerocontratto(DEFAULT_NUMEROCONTRATTO);
        // Add required entity
        Utenza utenza = UtenzaResourceIntTest.createEntity(em);
        em.persist(utenza);
        em.flush();
        telefono.setUtenza_telefono(utenza);
        // Add required entity
        Istituto istituto = IstitutoResourceIntTest.createEntity(em);
        em.persist(istituto);
        em.flush();
        telefono.setIstituto_telefono(istituto);
        return telefono;
    }

    @Before
    public void initTest() {
        telefono = createEntity(em);
    }

    @Test
    @Transactional
    public void createTelefono() throws Exception {
        int databaseSizeBeforeCreate = telefonoRepository.findAll().size();

        // Create the Telefono
        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isCreated());

        // Validate the Telefono in the database
        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeCreate + 1);
        Telefono testTelefono = telefonoList.get(telefonoList.size() - 1);
        assertThat(testTelefono.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testTelefono.getDataattivazione()).isEqualTo(DEFAULT_DATAATTIVAZIONE);
        assertThat(testTelefono.getDatacessazione()).isEqualTo(DEFAULT_DATACESSAZIONE);
        assertThat(testTelefono.getIntestatariocontratto()).isEqualTo(DEFAULT_INTESTATARIOCONTRATTO);
        assertThat(testTelefono.getNumerocontratto()).isEqualTo(DEFAULT_NUMEROCONTRATTO);
    }

    @Test
    @Transactional
    public void createTelefonoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = telefonoRepository.findAll().size();

        // Create the Telefono with an existing ID
        telefono.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        // Validate the Telefono in the database
        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonoRepository.findAll().size();
        // set the field null
        telefono.setNumero(null);

        // Create the Telefono, which fails.

        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataattivazioneIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonoRepository.findAll().size();
        // set the field null
        telefono.setDataattivazione(null);

        // Create the Telefono, which fails.

        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIntestatariocontrattoIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonoRepository.findAll().size();
        // set the field null
        telefono.setIntestatariocontratto(null);

        // Create the Telefono, which fails.

        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumerocontrattoIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefonoRepository.findAll().size();
        // set the field null
        telefono.setNumerocontratto(null);

        // Create the Telefono, which fails.

        restTelefonoMockMvc.perform(post("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefonos() throws Exception {
        // Initialize the database
        telefonoRepository.saveAndFlush(telefono);

        // Get all the telefonoList
        restTelefonoMockMvc.perform(get("/api/telefonos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefono.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].dataattivazione").value(hasItem(DEFAULT_DATAATTIVAZIONE.toString())))
            .andExpect(jsonPath("$.[*].datacessazione").value(hasItem(DEFAULT_DATACESSAZIONE.toString())))
            .andExpect(jsonPath("$.[*].intestatariocontratto").value(hasItem(DEFAULT_INTESTATARIOCONTRATTO.toString())))
            .andExpect(jsonPath("$.[*].numerocontratto").value(hasItem(DEFAULT_NUMEROCONTRATTO.toString())));
    }
    
    @Test
    @Transactional
    public void getTelefono() throws Exception {
        // Initialize the database
        telefonoRepository.saveAndFlush(telefono);

        // Get the telefono
        restTelefonoMockMvc.perform(get("/api/telefonos/{id}", telefono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(telefono.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.dataattivazione").value(DEFAULT_DATAATTIVAZIONE.toString()))
            .andExpect(jsonPath("$.datacessazione").value(DEFAULT_DATACESSAZIONE.toString()))
            .andExpect(jsonPath("$.intestatariocontratto").value(DEFAULT_INTESTATARIOCONTRATTO.toString()))
            .andExpect(jsonPath("$.numerocontratto").value(DEFAULT_NUMEROCONTRATTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTelefono() throws Exception {
        // Get the telefono
        restTelefonoMockMvc.perform(get("/api/telefonos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefono() throws Exception {
        // Initialize the database
        telefonoRepository.saveAndFlush(telefono);

        int databaseSizeBeforeUpdate = telefonoRepository.findAll().size();

        // Update the telefono
        Telefono updatedTelefono = telefonoRepository.findById(telefono.getId()).get();
        // Disconnect from session so that the updates on updatedTelefono are not directly saved in db
        em.detach(updatedTelefono);
        updatedTelefono
            .numero(UPDATED_NUMERO)
            .dataattivazione(UPDATED_DATAATTIVAZIONE)
            .datacessazione(UPDATED_DATACESSAZIONE)
            .intestatariocontratto(UPDATED_INTESTATARIOCONTRATTO)
            .numerocontratto(UPDATED_NUMEROCONTRATTO);

        restTelefonoMockMvc.perform(put("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTelefono)))
            .andExpect(status().isOk());

        // Validate the Telefono in the database
        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeUpdate);
        Telefono testTelefono = telefonoList.get(telefonoList.size() - 1);
        assertThat(testTelefono.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTelefono.getDataattivazione()).isEqualTo(UPDATED_DATAATTIVAZIONE);
        assertThat(testTelefono.getDatacessazione()).isEqualTo(UPDATED_DATACESSAZIONE);
        assertThat(testTelefono.getIntestatariocontratto()).isEqualTo(UPDATED_INTESTATARIOCONTRATTO);
        assertThat(testTelefono.getNumerocontratto()).isEqualTo(UPDATED_NUMEROCONTRATTO);
    }

    @Test
    @Transactional
    public void updateNonExistingTelefono() throws Exception {
        int databaseSizeBeforeUpdate = telefonoRepository.findAll().size();

        // Create the Telefono

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTelefonoMockMvc.perform(put("/api/telefonos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefono)))
            .andExpect(status().isBadRequest());

        // Validate the Telefono in the database
        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTelefono() throws Exception {
        // Initialize the database
        telefonoRepository.saveAndFlush(telefono);

        int databaseSizeBeforeDelete = telefonoRepository.findAll().size();

        // Get the telefono
        restTelefonoMockMvc.perform(delete("/api/telefonos/{id}", telefono.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Telefono> telefonoList = telefonoRepository.findAll();
        assertThat(telefonoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Telefono.class);
        Telefono telefono1 = new Telefono();
        telefono1.setId(1L);
        Telefono telefono2 = new Telefono();
        telefono2.setId(telefono1.getId());
        assertThat(telefono1).isEqualTo(telefono2);
        telefono2.setId(2L);
        assertThat(telefono1).isNotEqualTo(telefono2);
        telefono1.setId(null);
        assertThat(telefono1).isNotEqualTo(telefono2);
    }
}
