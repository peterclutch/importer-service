package com.peter.importerservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class AdministrativeAreaDTO implements Comparable<AdministrativeAreaDTO> {

    private CountryDTO country;

    private String isoCode;

    private String label;

    public AdministrativeAreaDTO isoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    public AdministrativeAreaDTO label(String label) {
        this.label = label;
        return this;
    }

    public AdministrativeAreaDTO country(CountryDTO country) {
        this.country = country;
        return this;
    }

    @Override
    public int compareTo(final AdministrativeAreaDTO area) {
        return Objects.compare(label, area.getLabel(), String::compareTo);
    }
}
