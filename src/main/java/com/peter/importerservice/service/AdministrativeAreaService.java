package com.peter.importerservice.service;

import com.peter.importerservice.domain.AdministrativeArea;
import com.peter.importerservice.repository.AdministrativeAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdministrativeAreaService {

    private final AdministrativeAreaRepository administrativeAreaRepository;

    public Optional<AdministrativeArea> getAdministrativeAreaByLabel(
            String countryCode, String administrativeAreaLabel) {
        return this.administrativeAreaRepository.findByCountryCodeAndAdministrativeAreaLabel(
                countryCode, administrativeAreaLabel);
    }
}
