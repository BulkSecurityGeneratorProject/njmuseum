package com.alienlab.njmuseum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.alienlab.njmuseum.domain.ContentInfo;

import com.alienlab.njmuseum.repository.ContentInfoRepository;
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
 * REST controller for managing ContentInfo.
 */
@RestController
@RequestMapping("/api")
public class ContentInfoResource {

    private final Logger log = LoggerFactory.getLogger(ContentInfoResource.class);

    private static final String ENTITY_NAME = "contentInfo";
        
    private final ContentInfoRepository contentInfoRepository;

    public ContentInfoResource(ContentInfoRepository contentInfoRepository) {
        this.contentInfoRepository = contentInfoRepository;
    }

    /**
     * POST  /content-infos : Create a new contentInfo.
     *
     * @param contentInfo the contentInfo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contentInfo, or with status 400 (Bad Request) if the contentInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/content-infos")
    @Timed
    public ResponseEntity<ContentInfo> createContentInfo(@RequestBody ContentInfo contentInfo) throws URISyntaxException {
        log.debug("REST request to save ContentInfo : {}", contentInfo);
        if (contentInfo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new contentInfo cannot already have an ID")).body(null);
        }
        ContentInfo result = contentInfoRepository.save(contentInfo);
        return ResponseEntity.created(new URI("/api/content-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /content-infos : Updates an existing contentInfo.
     *
     * @param contentInfo the contentInfo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contentInfo,
     * or with status 400 (Bad Request) if the contentInfo is not valid,
     * or with status 500 (Internal Server Error) if the contentInfo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/content-infos")
    @Timed
    public ResponseEntity<ContentInfo> updateContentInfo(@RequestBody ContentInfo contentInfo) throws URISyntaxException {
        log.debug("REST request to update ContentInfo : {}", contentInfo);
        if (contentInfo.getId() == null) {
            return createContentInfo(contentInfo);
        }
        ContentInfo result = contentInfoRepository.save(contentInfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contentInfo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /content-infos : get all the contentInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contentInfos in body
     */
    @GetMapping("/content-infos")
    @Timed
    public List<ContentInfo> getAllContentInfos() {
        log.debug("REST request to get all ContentInfos");
        List<ContentInfo> contentInfos = contentInfoRepository.findAll();
        return contentInfos;
    }

    /**
     * GET  /content-infos/:id : get the "id" contentInfo.
     *
     * @param id the id of the contentInfo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contentInfo, or with status 404 (Not Found)
     */
    @GetMapping("/content-infos/{id}")
    @Timed
    public ResponseEntity<ContentInfo> getContentInfo(@PathVariable Long id) {
        log.debug("REST request to get ContentInfo : {}", id);
        ContentInfo contentInfo = contentInfoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contentInfo));
    }

    /**
     * DELETE  /content-infos/:id : delete the "id" contentInfo.
     *
     * @param id the id of the contentInfo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/content-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteContentInfo(@PathVariable Long id) {
        log.debug("REST request to delete ContentInfo : {}", id);
        contentInfoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
