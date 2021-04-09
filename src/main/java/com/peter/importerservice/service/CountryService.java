package com.peter.importerservice.service;

import com.peter.importerservice.domain.Country;
import com.peter.importerservice.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository countryRepository;

    public Country getCountryByLabel(final String countryLabel) {
        return this.countryRepository.getByLabel(countryLabel);
    }

}
