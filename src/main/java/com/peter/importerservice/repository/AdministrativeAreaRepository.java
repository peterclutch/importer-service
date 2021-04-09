package com.peter.importerservice.repository;

import com.peter.importerservice.domain.AdministrativeArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrativeAreaRepository extends JpaRepository<AdministrativeArea, String> {

    @Query("select count(*) > 0 from Country ct left join AdministrativeArea ad on ad.country = ct.isoCode where ct.isoCode = :isoCode and ad.isoCode is not null")
    boolean hasAreaCodes(@Param("isoCode") String isoCode);

    @Query("select ad from Country ct left join AdministrativeArea ad on ad.country = ct.isoCode where ct.isoCode = :isoCode and lower(ad.label) = lower(:label)")
    Optional<AdministrativeArea> findByCountryCodeAndAdministrativeAreaLabel(
            @Param("isoCode") String isoCode,
            @Param("label") String label
    );

    boolean existsAdministrativeAreaByCountryIsoCodeAndIsoCode(String isoCountry, String areaCode);
}
