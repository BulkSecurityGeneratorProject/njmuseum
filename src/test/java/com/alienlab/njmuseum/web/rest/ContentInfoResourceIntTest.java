package com.alienlab.njmuseum.web.rest;

import com.alienlab.njmuseum.NjmuseumApp;

import com.alienlab.njmuseum.domain.ContentInfo;
import com.alienlab.njmuseum.repository.ContentInfoRepository;
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
 * Test class for the ContentInfoResource REST controller.
 *
 * @see ContentInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NjmuseumApp.class)
public class ContentInfoResourceIntTest {

    private static final String DEFAULT_INFO_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_INFO_COVER = "AAAAAAAAAA";
    private static final String UPDATED_INFO_COVER = "BBBBBBBBBB";

    private static final String DEFAULT_INFO_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_INFO_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_INFO_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_IMAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_INFO_SORT = 1;
    private static final Integer UPDATED_INFO_SORT = 2;

    @Autowired
    private ContentInfoRepository contentInfoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContentInfoMockMvc;

    private ContentInfo contentInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContentInfoResource contentInfoResource = new ContentInfoResource(contentInfoRepository);
        this.restContentInfoMockMvc = MockMvcBuilders.standaloneSetup(contentInfoResource)
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
    public static ContentInfo createEntity(EntityManager em) {
        ContentInfo contentInfo = new ContentInfo()
            .infoTitle(DEFAULT_INFO_TITLE)
            .infoCover(DEFAULT_INFO_COVER)
            .infoText(DEFAULT_INFO_TEXT)
            .infoImage(DEFAULT_INFO_IMAGE)
            .infoSort(DEFAULT_INFO_SORT);
        return contentInfo;
    }

    @Before
    public void initTest() {
        contentInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createContentInfo() throws Exception {
        int databaseSizeBeforeCreate = contentInfoRepository.findAll().size();

        // Create the ContentInfo
        restContentInfoMockMvc.perform(post("/api/content-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentInfo)))
            .andExpect(status().isCreated());

        // Validate the ContentInfo in the database
        List<ContentInfo> contentInfoList = contentInfoRepository.findAll();
        assertThat(contentInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ContentInfo testContentInfo = contentInfoList.get(contentInfoList.size() - 1);
        assertThat(testContentInfo.getInfoTitle()).isEqualTo(DEFAULT_INFO_TITLE);
        assertThat(testContentInfo.getInfoCover()).isEqualTo(DEFAULT_INFO_COVER);
        assertThat(testContentInfo.getInfoText()).isEqualTo(DEFAULT_INFO_TEXT);
        assertThat(testContentInfo.getInfoImage()).isEqualTo(DEFAULT_INFO_IMAGE);
        assertThat(testContentInfo.getInfoSort()).isEqualTo(DEFAULT_INFO_SORT);
    }

    @Test
    @Transactional
    public void createContentInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contentInfoRepository.findAll().size();

        // Create the ContentInfo with an existing ID
        contentInfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContentInfoMockMvc.perform(post("/api/content-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentInfo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ContentInfo> contentInfoList = contentInfoRepository.findAll();
        assertThat(contentInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContentInfos() throws Exception {
        // Initialize the database
        contentInfoRepository.saveAndFlush(contentInfo);

        // Get all the contentInfoList
        restContentInfoMockMvc.perform(get("/api/content-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contentInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].infoTitle").value(hasItem(DEFAULT_INFO_TITLE.toString())))
            .andExpect(jsonPath("$.[*].infoCover").value(hasItem(DEFAULT_INFO_COVER.toString())))
            .andExpect(jsonPath("$.[*].infoText").value(hasItem(DEFAULT_INFO_TEXT.toString())))
            .andExpect(jsonPath("$.[*].infoImage").value(hasItem(DEFAULT_INFO_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].infoSort").value(hasItem(DEFAULT_INFO_SORT)));
    }

    @Test
    @Transactional
    public void getContentInfo() throws Exception {
        // Initialize the database
        contentInfoRepository.saveAndFlush(contentInfo);

        // Get the contentInfo
        restContentInfoMockMvc.perform(get("/api/content-infos/{id}", contentInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contentInfo.getId().intValue()))
            .andExpect(jsonPath("$.infoTitle").value(DEFAULT_INFO_TITLE.toString()))
            .andExpect(jsonPath("$.infoCover").value(DEFAULT_INFO_COVER.toString()))
            .andExpect(jsonPath("$.infoText").value(DEFAULT_INFO_TEXT.toString()))
            .andExpect(jsonPath("$.infoImage").value(DEFAULT_INFO_IMAGE.toString()))
            .andExpect(jsonPath("$.infoSort").value(DEFAULT_INFO_SORT));
    }

    @Test
    @Transactional
    public void getNonExistingContentInfo() throws Exception {
        // Get the contentInfo
        restContentInfoMockMvc.perform(get("/api/content-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContentInfo() throws Exception {
        // Initialize the database
        contentInfoRepository.saveAndFlush(contentInfo);
        int databaseSizeBeforeUpdate = contentInfoRepository.findAll().size();

        // Update the contentInfo
        ContentInfo updatedContentInfo = contentInfoRepository.findOne(contentInfo.getId());
        updatedContentInfo
            .infoTitle(UPDATED_INFO_TITLE)
            .infoCover(UPDATED_INFO_COVER)
            .infoText(UPDATED_INFO_TEXT)
            .infoImage(UPDATED_INFO_IMAGE)
            .infoSort(UPDATED_INFO_SORT);

        restContentInfoMockMvc.perform(put("/api/content-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedContentInfo)))
            .andExpect(status().isOk());

        // Validate the ContentInfo in the database
        List<ContentInfo> contentInfoList = contentInfoRepository.findAll();
        assertThat(contentInfoList).hasSize(databaseSizeBeforeUpdate);
        ContentInfo testContentInfo = contentInfoList.get(contentInfoList.size() - 1);
        assertThat(testContentInfo.getInfoTitle()).isEqualTo(UPDATED_INFO_TITLE);
        assertThat(testContentInfo.getInfoCover()).isEqualTo(UPDATED_INFO_COVER);
        assertThat(testContentInfo.getInfoText()).isEqualTo(UPDATED_INFO_TEXT);
        assertThat(testContentInfo.getInfoImage()).isEqualTo(UPDATED_INFO_IMAGE);
        assertThat(testContentInfo.getInfoSort()).isEqualTo(UPDATED_INFO_SORT);
    }

    @Test
    @Transactional
    public void updateNonExistingContentInfo() throws Exception {
        int databaseSizeBeforeUpdate = contentInfoRepository.findAll().size();

        // Create the ContentInfo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContentInfoMockMvc.perform(put("/api/content-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contentInfo)))
            .andExpect(status().isCreated());

        // Validate the ContentInfo in the database
        List<ContentInfo> contentInfoList = contentInfoRepository.findAll();
        assertThat(contentInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContentInfo() throws Exception {
        // Initialize the database
        contentInfoRepository.saveAndFlush(contentInfo);
        int databaseSizeBeforeDelete = contentInfoRepository.findAll().size();

        // Get the contentInfo
        restContentInfoMockMvc.perform(delete("/api/content-infos/{id}", contentInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ContentInfo> contentInfoList = contentInfoRepository.findAll();
        assertThat(contentInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContentInfo.class);
        ContentInfo contentInfo1 = new ContentInfo();
        contentInfo1.setId(1L);
        ContentInfo contentInfo2 = new ContentInfo();
        contentInfo2.setId(contentInfo1.getId());
        assertThat(contentInfo1).isEqualTo(contentInfo2);
        contentInfo2.setId(2L);
        assertThat(contentInfo1).isNotEqualTo(contentInfo2);
        contentInfo1.setId(null);
        assertThat(contentInfo1).isNotEqualTo(contentInfo2);
    }
}
