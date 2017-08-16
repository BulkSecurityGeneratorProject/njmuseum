package com.alienlab.njmuseum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.njmuseum.domain.UnitContent;

import com.alienlab.njmuseum.repository.UnitContentRepository;
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
 * REST controller for managing UnitContent.
 */
@RestController
@RequestMapping("/api")
public class UnitContentResource {

    private final Logger log = LoggerFactory.getLogger(UnitContentResource.class);

    private static final String ENTITY_NAME = "unitContent";
        
    private final UnitContentRepository unitContentRepository;

    public UnitContentResource(UnitContentRepository unitContentRepository) {
        this.unitContentRepository = unitContentRepository;
    }

    /**
     * POST  /unit-contents : Create a new unitContent.
     *
     * @param unitContent the unitContent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new unitContent, or with status 400 (Bad Request) if the unitContent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/unit-contents")
    @Timed
    public ResponseEntity<UnitContent> createUnitContent(@RequestBody UnitContent unitContent) throws URISyntaxException {
        log.debug("REST request to save UnitContent : {}", unitContent);
        if (unitContent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new unitContent cannot already have an ID")).body(null);
        }
        UnitContent result = unitContentRepository.save(unitContent);
        return ResponseEntity.created(new URI("/api/unit-contents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /unit-contents : Updates an existing unitContent.
     *
     * @param unitContent the unitContent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated unitContent,
     * or with status 400 (Bad Request) if the unitContent is not valid,
     * or with status 500 (Internal Server Error) if the unitContent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/unit-contents")
    @Timed
    public ResponseEntity<UnitContent> updateUnitContent(@RequestBody UnitContent unitContent) throws URISyntaxException {
        log.debug("REST request to update UnitContent : {}", unitContent);
        if (unitContent.getId() == null) {
            return createUnitContent(unitContent);
        }
        UnitContent result = unitContentRepository.save(unitContent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, unitContent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /unit-contents : get all the unitContents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of unitContents in body
     */
    @GetMapping("/unit-contents")
    @Timed
    public List<UnitContent> getAllUnitContents() {
        log.debug("REST request to get all UnitContents");
        List<UnitContent> unitContents = unitContentRepository.findAll();
        return unitContents;
    }

    /**
     * GET  /unit-contents/:id : get the "id" unitContent.
     *
     * @param id the id of the unitContent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the unitContent, or with status 404 (Not Found)
     */
    @GetMapping("/unit-contents/{id}")
    @Timed
    public ResponseEntity<UnitContent> getUnitContent(@PathVariable Long id) {
        log.debug("REST request to get UnitContent : {}", id);
        UnitContent unitContent = unitContentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(unitContent));
    }

    /**
     * DELETE  /unit-contents/:id : delete the "id" unitContent.
     *
     * @param id the id of the unitContent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/unit-contents/{id}")
    @Timed
    public ResponseEntity<Void> deleteUnitContent(@PathVariable Long id) {
        log.debug("REST request to delete UnitContent : {}", id);
        unitContentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
