package com.peter.importerservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "country_administrative_area")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@NoArgsConstructor
public class AdministrativeArea implements Comparable<AdministrativeArea> {

    @Id
    @NotBlank
    @Column(name = "iso_code", nullable = false)
    private String isoCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    @NotNull
    @Column(nullable = false)
    private String label;

    public AdministrativeArea label(String label) {
        this.label = label;
        return this;
    }

    public AdministrativeArea country(Country country) {
        this.country = country;
        return this;
    }

    public AdministrativeArea isoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    @Override
    public int compareTo(final AdministrativeArea area) {
        return Objects.compare(isoCode, area.getIsoCode(), String::compareTo);
    }
}
