package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.PageUnit;
import com.alienlab.njmuseum.domain.UnitContent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the UnitContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitContentRepository extends JpaRepository<UnitContent,Long> {
    Set<UnitContent> findByPageUnit(PageUnit pageUnit);
}
