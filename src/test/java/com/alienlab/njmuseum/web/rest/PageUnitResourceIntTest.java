package com.alienlab.njmuseum.web.rest;

import com.alienlab.njmuseum.NjmuseumApp;

import com.alienlab.njmuseum.domain.PageUnit;
import com.alienlab.njmuseum.repository.PageUnitRepository;
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
 * Test class for the PageUnitResource REST controller.
 *
 * @see PageUnitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NjmuseumApp.class)
public class PageUnitResourceIntTest {

    private static final String DEFAULT_UNIT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_UNIT_SORT = 1;
    private static final Integer UPDATED_UNIT_SORT = 2;

    @Autowired
    private PageUnitRepository pageUnitRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPageUnitMockMvc;

    private PageUnit pageUnit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PageUnitResource pageUnitResource = new PageUnitResource(pageUnitRepository);
        this.restPageUnitMockMvc = MockMvcBuilders.standaloneSetup(pageUnitResource)
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
    public static PageUnit createEntity(EntityManager em) {
        PageUnit pageUnit = new PageUnit()
            .unitName(DEFAULT_UNIT_NAME)
            .unitMemo(DEFAULT_UNIT_MEMO)
            .unitTitle(DEFAULT_UNIT_TITLE)
            .unitSort(DEFAULT_UNIT_SORT);
        return pageUnit;
    }

    @Before
    public void initTest() {
        pageUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createPageUnit() throws Exception {
        int databaseSizeBeforeCreate = pageUnitRepository.findAll().size();

        // Create the PageUnit
        restPageUnitMockMvc.perform(post("/api/page-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pageUnit)))
            .andExpect(status().isCreated());

        // Validate the PageUnit in the database
        List<PageUnit> pageUnitList = pageUnitRepository.findAll();
        assertThat(pageUnitList).hasSize(databaseSizeBeforeCreate + 1);
        PageUnit testPageUnit = pageUnitList.get(pageUnitList.size() - 1);
        assertThat(testPageUnit.getUnitName()).isEqualTo(DEFAULT_UNIT_NAME);
        assertThat(testPageUnit.getUnitMemo()).isEqualTo(DEFAULT_UNIT_MEMO);
        assertThat(testPageUnit.getUnitTitle()).isEqualTo(DEFAULT_UNIT_TITLE);
        assertThat(testPageUnit.getUnitSort()).isEqualTo(DEFAULT_UNIT_SORT);
    }

    @Test
    @Transactional
    public void createPageUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pageUnitRepository.findAll().size();

        // Create the PageUnit with an existing ID
        pageUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageUnitMockMvc.perform(post("/api/page-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pageUnit)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PageUnit> pageUnitList = pageUnitRepository.findAll();
        assertThat(pageUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPageUnits() throws Exception {
        // Initialize the database
        pageUnitRepository.saveAndFlush(pageUnit);

        // Get all the pageUnitList
        restPageUnitMockMvc.perform(get("/api/page-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unitName").value(hasItem(DEFAULT_UNIT_NAME.toString())))
            .andExpect(jsonPath("$.[*].unitMemo").value(hasItem(DEFAULT_UNIT_MEMO.toString())))
            .andExpect(jsonPath("$.[*].unitTitle").value(hasItem(DEFAULT_UNIT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].unitSort").value(hasItem(DEFAULT_UNIT_SORT)));
    }

    @Test
    @Transactional
    public void getPageUnit() throws Exception {
        // Initialize the database
        pageUnitRepository.saveAndFlush(pageUnit);

        // Get the pageUnit
        restPageUnitMockMvc.perform(get("/api/page-units/{id}", pageUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pageUnit.getId().intValue()))
            .andExpect(jsonPath("$.unitName").value(DEFAULT_UNIT_NAME.toString()))
            .andExpect(jsonPath("$.unitMemo").value(DEFAULT_UNIT_MEMO.toString()))
            .andExpect(jsonPath("$.unitTitle").value(DEFAULT_UNIT_TITLE.toString()))
            .andExpect(jsonPath("$.unitSort").value(DEFAULT_UNIT_SORT));
    }

    @Test
    @Transactional
    public void getNonExistingPageUnit() throws Exception {
        // Get the pageUnit
        restPageUnitMockMvc.perform(get("/api/page-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePageUnit() throws Exception {
        // Initialize the database
        pageUnitRepository.saveAndFlush(pageUnit);
        int databaseSizeBeforeUpdate = pageUnitRepository.findAll().size();

        // Update the pageUnit
        PageUnit updatedPageUnit = pageUnitRepository.findOne(pageUnit.getId());
        updatedPageUnit
            .unitName(UPDATED_UNIT_NAME)
            .unitMemo(UPDATED_UNIT_MEMO)
            .unitTitle(UPDATED_UNIT_TITLE)
            .unitSort(UPDATED_UNIT_SORT);

        restPageUnitMockMvc.perform(put("/api/page-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPageUnit)))
            .andExpect(status().isOk());

        // Validate the PageUnit in the database
        List<PageUnit> pageUnitList = pageUnitRepository.findAll();
        assertThat(pageUnitList).hasSize(databaseSizeBeforeUpdate);
        PageUnit testPageUnit = pageUnitList.get(pageUnitList.size() - 1);
        assertThat(testPageUnit.getUnitName()).isEqualTo(UPDATED_UNIT_NAME);
        assertThat(testPageUnit.getUnitMemo()).isEqualTo(UPDATED_UNIT_MEMO);
        assertThat(testPageUnit.getUnitTitle()).isEqualTo(UPDATED_UNIT_TITLE);
        assertThat(testPageUnit.getUnitSort()).isEqualTo(UPDATED_UNIT_SORT);
    }

    @Test
    @Transactional
    public void updateNonExistingPageUnit() throws Exception {
        int databaseSizeBeforeUpdate = pageUnitRepository.findAll().size();

        // Create the PageUnit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPageUnitMockMvc.perform(put("/api/page-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pageUnit)))
            .andExpect(status().isCreated());

        // Validate the PageUnit in the database
        List<PageUnit> pageUnitList = pageUnitRepository.findAll();
        assertThat(pageUnitList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePageUnit() throws Exception {
        // Initialize the database
        pageUnitRepository.saveAndFlush(pageUnit);
        int databaseSizeBeforeDelete = pageUnitRepository.findAll().size();

        // Get the pageUnit
        restPageUnitMockMvc.perform(delete("/api/page-units/{id}", pageUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PageUnit> pageUnitList = pageUnitRepository.findAll();
        assertThat(pageUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PageUnit.class);
        PageUnit pageUnit1 = new PageUnit();
        pageUnit1.setId(1L);
        PageUnit pageUnit2 = new PageUnit();
        pageUnit2.setId(pageUnit1.getId());
        assertThat(pageUnit1).isEqualTo(pageUnit2);
        pageUnit2.setId(2L);
        assertThat(pageUnit1).isNotEqualTo(pageUnit2);
        pageUnit1.setId(null);
        assertThat(pageUnit1).isNotEqualTo(pageUnit2);
    }
}
