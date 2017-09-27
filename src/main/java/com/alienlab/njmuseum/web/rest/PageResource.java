package com.alienlab.njmuseum.web.rest;

import com.alienlab.njmuseum.domain.ContentInfo;
import com.alienlab.njmuseum.domain.PageUnit;
import com.alienlab.njmuseum.domain.UnitContent;
import com.alienlab.njmuseum.repository.ContentInfoRepository;
import com.alienlab.njmuseum.repository.PageUnitRepository;
import com.alienlab.njmuseum.repository.UnitContentRepository;
import com.codahale.metrics.annotation.Timed;
import com.alienlab.njmuseum.domain.Page;

import com.alienlab.njmuseum.repository.PageRepository;
import com.alienlab.njmuseum.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Page.
 */
@RestController
@RequestMapping("/api")
public class PageResource {

    private final Logger log = LoggerFactory.getLogger(PageResource.class);

    private static final String ENTITY_NAME = "page";

    private final PageRepository pageRepository;
    @Autowired
    PageUnitRepository pageUnitRepository;
    @Autowired
    UnitContentRepository unitContentRepository;
    @Autowired
    ContentInfoRepository contentInfoRepository;

    public PageResource(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    /**
     * POST  /pages : Create a new page.
     *
     * @param page the page to create
     * @return the ResponseEntity with status 201 (Created) and with body the new page, or with status 400 (Bad Request) if the page has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pages")
    @Timed
    public ResponseEntity<Page> createPage(@RequestBody Page page) throws URISyntaxException {
        log.debug("REST request to save Page : {}", page);
        if (page.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new page cannot already have an ID")).body(null);
        }
        Page result = pageRepository.save(page);
        return ResponseEntity.created(new URI("/api/pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pages : Updates an existing page.
     *
     * @param page the page to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated page,
     * or with status 400 (Bad Request) if the page is not valid,
     * or with status 500 (Internal Server Error) if the page couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pages")
    @Timed
    public ResponseEntity<Page> updatePage(@RequestBody Page page) throws URISyntaxException {
        log.debug("REST request to update Page : {}", page);
        if (page.getId() == null) {
            return createPage(page);
        }
        Page result = pageRepository.save(page);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, page.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pages : get all the pages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pages in body
     */
    @GetMapping("/pages")
    @Timed
    public List<Page> getAllPages() {
        log.debug("REST request to get all Pages");
        List<Page> pages = pageRepository.findAll();
        return pages;
    }

    /**
     * GET  /pages/:id : get the "id" page.
     *
     * @param id the id of the page to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the page, or with status 404 (Not Found)
     */
    @GetMapping("/pages/{id}")
    @Timed
    public ResponseEntity<Page> getPage(@PathVariable Long id) {
        log.debug("REST request to get Page : {}", id);
        Page page = pageRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(page));
    }

    /**
     * DELETE  /pages/:id : delete the "id" page.
     *
     * @param id the id of the page to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pages/{id}")
    @Timed
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        log.debug("REST request to delete Page : {}", id);
        pageRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/pages/json")
    public ResponseEntity getJson(){
        List<Page> pages=pageRepository.findAll();
        for (Page page : pages) {
            Set<PageUnit> units=pageUnitRepository.findByPage(page);
            for (PageUnit unit : units) {
                Set<UnitContent> contents=unitContentRepository.findByPageUnit(unit);
                for (UnitContent content : contents) {
                    Set<ContentInfo> infos=contentInfoRepository.findByUnitContent(content);
                    content.setInfos(infos);
                }
                unit.setContents(contents);
            }
            page.setUnits(units);
        }

        return ResponseEntity.ok(pages);
    }

}
