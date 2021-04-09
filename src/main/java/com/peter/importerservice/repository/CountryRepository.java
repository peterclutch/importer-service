package com.peter.importerservice.repository;

import com.peter.importerservice.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    @Query("select count(c) > 0 from Country c where lower(c.label) = lower(:label) ")
    boolean existsByLabel(@Param("label") String label);

    @Query("select c from Country c where lower(c.label) = lower(:label) ")
    Country getByLabel(@Param("label") String label);
}
