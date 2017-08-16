package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.ContentInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContentInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentInfoRepository extends JpaRepository<ContentInfo,Long> {

}
