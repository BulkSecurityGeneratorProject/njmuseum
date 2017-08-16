package com.alienlab.njmuseum.web.rest;

import com.alienlab.njmuseum.NjmuseumApp;

import com.alienlab.njmuseum.domain.Page;
import com.alienlab.njmuseum.repository.PageRepository;
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
 * Test class for the PageResource REST controller.
 *
 * @see PageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NjmuseumApp.class)
public class PageResourceIntTest {

    private static final String DEFAULT_PAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_MEMO = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_SORT = 1;
    private static final Integer UPDATED_PAGE_SORT = 2;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPageMockMvc;

    private Page page;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PageResource pageResource = new PageResource(pageRepository);
        this.restPageMockMvc = MockMvcBuilders.standaloneSetup(pageResource)
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
    public static Page createEntity(EntityManager em) {
        Page page = new Page()
            .pageName(DEFAULT_PAGE_NAME)
            .pageMemo(DEFAULT_PAGE_MEMO)
            .pageImage(DEFAULT_PAGE_IMAGE)
            .pageSort(DEFAULT_PAGE_SORT);
        return page;
    }

    @Before
    public void initTest() {
        page = createEntity(em);
    }

    @Test
    @Transactional
    public void createPage() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // Create the Page
        restPageMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isCreated());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeCreate + 1);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getPageName()).isEqualTo(DEFAULT_PAGE_NAME);
        assertThat(testPage.getPageMemo()).isEqualTo(DEFAULT_PAGE_MEMO);
        assertThat(testPage.getPageImage()).isEqualTo(DEFAULT_PAGE_IMAGE);
        assertThat(testPage.getPageSort()).isEqualTo(DEFAULT_PAGE_SORT);
    }

    @Test
    @Transactional
    public void createPageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // Create the Page with an existing ID
        page.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPageMockMvc.perform(post("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPages() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);

        // Get all the pageList
        restPageMockMvc.perform(get("/api/pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(page.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageName").value(hasItem(DEFAULT_PAGE_NAME.toString())))
            .andExpect(jsonPath("$.[*].pageMemo").value(hasItem(DEFAULT_PAGE_MEMO.toString())))
            .andExpect(jsonPath("$.[*].pageImage").value(hasItem(DEFAULT_PAGE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].pageSort").value(hasItem(DEFAULT_PAGE_SORT)));
    }

    @Test
    @Transactional
    public void getPage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);

        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", page.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(page.getId().intValue()))
            .andExpect(jsonPath("$.pageName").value(DEFAULT_PAGE_NAME.toString()))
            .andExpect(jsonPath("$.pageMemo").value(DEFAULT_PAGE_MEMO.toString()))
            .andExpect(jsonPath("$.pageImage").value(DEFAULT_PAGE_IMAGE.toString()))
            .andExpect(jsonPath("$.pageSort").value(DEFAULT_PAGE_SORT));
    }

    @Test
    @Transactional
    public void getNonExistingPage() throws Exception {
        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page
        Page updatedPage = pageRepository.findOne(page.getId());
        updatedPage
            .pageName(UPDATED_PAGE_NAME)
            .pageMemo(UPDATED_PAGE_MEMO)
            .pageImage(UPDATED_PAGE_IMAGE)
            .pageSort(UPDATED_PAGE_SORT);

        restPageMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPage)))
            .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pageList.get(pageList.size() - 1);
        assertThat(testPage.getPageName()).isEqualTo(UPDATED_PAGE_NAME);
        assertThat(testPage.getPageMemo()).isEqualTo(UPDATED_PAGE_MEMO);
        assertThat(testPage.getPageImage()).isEqualTo(UPDATED_PAGE_IMAGE);
        assertThat(testPage.getPageSort()).isEqualTo(UPDATED_PAGE_SORT);
    }

    @Test
    @Transactional
    public void updateNonExistingPage() throws Exception {
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Create the Page

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPageMockMvc.perform(put("/api/pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(page)))
            .andExpect(status().isCreated());

        // Validate the Page in the database
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);
        int databaseSizeBeforeDelete = pageRepository.findAll().size();

        // Get the page
        restPageMockMvc.perform(delete("/api/pages/{id}", page.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Page> pageList = pageRepository.findAll();
        assertThat(pageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Page.class);
        Page page1 = new Page();
        page1.setId(1L);
        Page page2 = new Page();
        page2.setId(page1.getId());
        assertThat(page1).isEqualTo(page2);
        page2.setId(2L);
        assertThat(page1).isNotEqualTo(page2);
        page1.setId(null);
        assertThat(page1).isNotEqualTo(page2);
    }
}
