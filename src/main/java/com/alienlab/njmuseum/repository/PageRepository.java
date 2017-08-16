package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Page entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PageRepository extends JpaRepository<Page,Long> {

}
