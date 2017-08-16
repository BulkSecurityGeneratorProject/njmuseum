package com.alienlab.njmuseum.repository;

import com.alienlab.njmuseum.domain.UnitContent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the UnitContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitContentRepository extends JpaRepository<UnitContent,Long> {

}
