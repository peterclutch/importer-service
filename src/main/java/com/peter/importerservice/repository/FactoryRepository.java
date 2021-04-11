package com.peter.importerservice.repository;

import com.peter.importerservice.domain.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, Long> {

    @Query("select f from Factory f join f.brandFactories bf where lower(f.name) = lower(:name) and bf.id.brand.id = :brandId")
    Factory findOneByNameAndBrandId(@Param("name") String name, @Param("brandId") Long brandId);

    @Query("select count(f) > 0 from Factory f join f.brandFactories bf where lower(f.name) = lower(:name) and bf.id.brand.id = :brandId")
    boolean existsByNameAndBrandId(@Param("name") String name, @Param("brandId") Long brandId);

    @Query("select f.name from Factory f where f.id = :id")
    String getName(@Param("id") Long id);

    @Query("select count(f) > 0 from Factory f join f.brandFactories bf  where bf.id.brand.id = :brandId and bf.customId = :customId")
    boolean existsByCustomIdAndBrandId(@Param("customId") String customId, @Param("brandId") Long brandId);

    @Query("select bf.customId from BrandFactory bf where bf.id.brand.id = :brandId and bf.id.factory.id = :id")
    String getCustomId(@Param("id") Long id, @Param("brandId") Long brandId);

}
