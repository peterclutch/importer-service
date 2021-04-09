package com.peter.importerservice.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
public class CountryDTO implements Comparable<CountryDTO> {

    @NotBlank
    private String isoCode;

    @NotBlank private String label;

    public CountryDTO isoCode(String isoCode) {
        this.setIsoCode(isoCode);
        return this;
    }

    public CountryDTO label(String label) {
        this.setLabel(label);
        return this;
    }

    @Override
    public int compareTo(CountryDTO country) {
        return Objects.compare(label, country.getLabel(), String::compareTo);
    }
}
