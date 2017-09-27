package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.Page;
import com.alienlab.njmuseum.domain.PageUnit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Set;


/**
 * Spring Data JPA repository for the PageUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageUnitRepository extends JpaRepository<PageUnit,Long> {
    Set<PageUnit> findByPage(Page page);

}
