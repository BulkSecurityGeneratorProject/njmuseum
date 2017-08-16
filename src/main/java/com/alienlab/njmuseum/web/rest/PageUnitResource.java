package com.alienlab.njmuseum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.njmuseum.domain.PageUnit;

import com.alienlab.njmuseum.repository.PageUnitRepository;
import com.alienlab.njmuseum.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing PageUnit.
 */
@RestController
@RequestMapping("/api")
public class PageUnitResource {

    private final Logger log = LoggerFactory.getLogger(PageUnitResource.class);

    private static final String ENTITY_NAME = "pageUnit";
        
    private final PageUnitRepository pageUnitRepository;

    public PageUnitResource(PageUnitRepository pageUnitRepository) {
        this.pageUnitRepository = pageUnitRepository;
    }

    /**
     * POST  /page-units : Create a new pageUnit.
     *
     * @param pageUnit the pageUnit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pageUnit, or with status 400 (Bad Request) if the pageUnit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/page-units")
    @Timed
    public ResponseEntity<PageUnit> createPageUnit(@RequestBody PageUnit pageUnit) throws URISyntaxException {
        log.debug("REST request to save PageUnit : {}", pageUnit);
        if (pageUnit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pageUnit cannot already have an ID")).body(null);
        }
        PageUnit result = pageUnitRepository.save(pageUnit);
        return ResponseEntity.created(new URI("/api/page-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /page-units : Updates an existing pageUnit.
     *
     * @param pageUnit the pageUnit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pageUnit,
     * or with status 400 (Bad Request) if the pageUnit is not valid,
     * or with status 500 (Internal Server Error) if the pageUnit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/page-units")
    @Timed
    public ResponseEntity<PageUnit> updatePageUnit(@RequestBody PageUnit pageUnit) throws URISyntaxException {
        log.debug("REST request to update PageUnit : {}", pageUnit);
        if (pageUnit.getId() == null) {
            return createPageUnit(pageUnit);
        }
        PageUnit result = pageUnitRepository.save(pageUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pageUnit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /page-units : get all the pageUnits.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pageUnits in body
     */
    @GetMapping("/page-units")
    @Timed
    public List<PageUnit> getAllPageUnits() {
        log.debug("REST request to get all PageUnits");
        List<PageUnit> pageUnits = pageUnitRepository.findAll();
        return pageUnits;
    }

    /**
     * GET  /page-units/:id : get the "id" pageUnit.
     *
     * @param id the id of the pageUnit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pageUnit, or with status 404 (Not Found)
     */
    @GetMapping("/page-units/{id}")
    @Timed
    public ResponseEntity<PageUnit> getPageUnit(@PathVariable Long id) {
        log.debug("REST request to get PageUnit : {}", id);
        PageUnit pageUnit = pageUnitRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pageUnit));
    }

    /**
     * DELETE  /page-units/:id : delete the "id" pageUnit.
     *
     * @param id the id of the pageUnit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/page-units/{id}")
    @Timed
    public ResponseEntity<Void> deletePageUnit(@PathVariable Long id) {
        log.debug("REST request to delete PageUnit : {}", id);
        pageUnitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
