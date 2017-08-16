package com.alienlab.njmuseum.web.rest;

import com.alienlab.njmuseum.NjmuseumApp;

import com.alienlab.njmuseum.domain.UnitContent;
import com.alienlab.njmuseum.repository.UnitContentRepository;
import com.alienlab.njmuseum.web.rest.errors.ExceptionTranslator;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UnitContentResource REST controller.
 *
 * @see UnitContentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NjmuseumApp.class)
public class UnitContentResourceIntTest {

    private static final String DEFAULT_CONTENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_MEMO = "BBBBBBBBBB";

    private static final Integer DEFAULT_CONTENT_SORT = 1;
    private static final Integer UPDATED_CONTENT_SORT = 2;

    @Autowired
    private UnitContentRepository unitContentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUnitContentMockMvc;

    private UnitContent unitContent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnitContentResource unitContentResource = new UnitContentResource(unitContentRepository);
        this.restUnitContentMockMvc = MockMvcBuilders.standaloneSetup(unitContentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UnitContent createEntity(EntityManager em) {
        UnitContent unitContent = new UnitContent()
            .contentName(DEFAULT_CONTENT_NAME)
            .contentMemo(DEFAULT_CONTENT_MEMO)
            .contentSort(DEFAULT_CONTENT_SORT);
        return unitContent;
    }

    @Before
    public void initTest() {
        unitContent = createEntity(em);
    }

    @Test
    @Transactional
    public void createUnitContent() throws Exception {
        int databaseSizeBeforeCreate = unitContentRepository.findAll().size();

        // Create the UnitContent
        restUnitContentMockMvc.perform(post("/api/unit-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitContent)))
            .andExpect(status().isCreated());

        // Validate the UnitContent in the database
        List<UnitContent> unitContentList = unitContentRepository.findAll();
        assertThat(unitContentList).hasSize(databaseSizeBeforeCreate + 1);
        UnitContent testUnitContent = unitContentList.get(unitContentList.size() - 1);
        assertThat(testUnitContent.getContentName()).isEqualTo(DEFAULT_CONTENT_NAME);
        assertThat(testUnitContent.getContentMemo()).isEqualTo(DEFAULT_CONTENT_MEMO);
        assertThat(testUnitContent.getContentSort()).isEqualTo(DEFAULT_CONTENT_SORT);
    }

    @Test
    @Transactional
    public void createUnitContentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = unitContentRepository.findAll().size();

        // Create the UnitContent with an existing ID
        unitContent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnitContentMockMvc.perform(post("/api/unit-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitContent)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<UnitContent> unitContentList = unitContentRepository.findAll();
        assertThat(unitContentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUnitContents() throws Exception {
        // Initialize the database
        unitContentRepository.saveAndFlush(unitContent);

        // Get all the unitContentList
        restUnitContentMockMvc.perform(get("/api/unit-contents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unitContent.getId().intValue())))
            .andExpect(jsonPath("$.[*].contentName").value(hasItem(DEFAULT_CONTENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contentMemo").value(hasItem(DEFAULT_CONTENT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].contentSort").value(hasItem(DEFAULT_CONTENT_SORT)));
    }

    @Test
    @Transactional
    public void getUnitContent() throws Exception {
        // Initialize the database
        unitContentRepository.saveAndFlush(unitContent);

        // Get the unitContent
        restUnitContentMockMvc.perform(get("/api/unit-contents/{id}", unitContent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(unitContent.getId().intValue()))
            .andExpect(jsonPath("$.contentName").value(DEFAULT_CONTENT_NAME.toString()))
            .andExpect(jsonPath("$.contentMemo").value(DEFAULT_CONTENT_MEMO.toString()))
            .andExpect(jsonPath("$.contentSort").value(DEFAULT_CONTENT_SORT));
    }

    @Test
    @Transactional
    public void getNonExistingUnitContent() throws Exception {
        // Get the unitContent
        restUnitContentMockMvc.perform(get("/api/unit-contents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnitContent() throws Exception {
        // Initialize the database
        unitContentRepository.saveAndFlush(unitContent);
        int databaseSizeBeforeUpdate = unitContentRepository.findAll().size();

        // Update the unitContent
        UnitContent updatedUnitContent = unitContentRepository.findOne(unitContent.getId());
        updatedUnitContent
            .contentName(UPDATED_CONTENT_NAME)
            .contentMemo(UPDATED_CONTENT_MEMO)
            .contentSort(UPDATED_CONTENT_SORT);

        restUnitContentMockMvc.perform(put("/api/unit-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUnitContent)))
            .andExpect(status().isOk());

        // Validate the UnitContent in the database
        List<UnitContent> unitContentList = unitContentRepository.findAll();
        assertThat(unitContentList).hasSize(databaseSizeBeforeUpdate);
        UnitContent testUnitContent = unitContentList.get(unitContentList.size() - 1);
        assertThat(testUnitContent.getContentName()).isEqualTo(UPDATED_CONTENT_NAME);
        assertThat(testUnitContent.getContentMemo()).isEqualTo(UPDATED_CONTENT_MEMO);
        assertThat(testUnitContent.getContentSort()).isEqualTo(UPDATED_CONTENT_SORT);
    }

    @Test
    @Transactional
    public void updateNonExistingUnitContent() throws Exception {
        int databaseSizeBeforeUpdate = unitContentRepository.findAll().size();

        // Create the UnitContent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUnitContentMockMvc.perform(put("/api/unit-contents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(unitContent)))
            .andExpect(status().isCreated());

        // Validate the UnitContent in the database
        List<UnitContent> unitContentList = unitContentRepository.findAll();
        assertThat(unitContentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUnitContent() throws Exception {
        // Initialize the database
        unitContentRepository.saveAndFlush(unitContent);
        int databaseSizeBeforeDelete = unitContentRepository.findAll().size();

        // Get the unitContent
        restUnitContentMockMvc.perform(delete("/api/unit-contents/{id}", unitContent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UnitContent> unitContentList = unitContentRepository.findAll();
        assertThat(unitContentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitContent.class);
        UnitContent unitContent1 = new UnitContent();
        unitContent1.setId(1L);
        UnitContent unitContent2 = new UnitContent();
        unitContent2.setId(unitContent1.getId());
        assertThat(unitContent1).isEqualTo(unitContent2);
        unitContent2.setId(2L);
        assertThat(unitContent1).isNotEqualTo(unitContent2);
        unitContent1.setId(null);
        assertThat(unitContent1).isNotEqualTo(unitContent2);
    }
}
