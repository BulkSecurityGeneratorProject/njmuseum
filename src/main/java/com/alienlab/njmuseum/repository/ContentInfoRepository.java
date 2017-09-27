package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.ContentInfo;
import com.alienlab.njmuseum.domain.UnitContent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the ContentInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentInfoRepository extends JpaRepository<ContentInfo,Long> {
    Set<ContentInfo> findByUnitContent(UnitContent content);
}
