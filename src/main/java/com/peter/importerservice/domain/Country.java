package com.peter.importerservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
@NoArgsConstructor
public class Country {

    @Id
    @NotBlank
    @Column(name = "iso_code", nullable = false)
    private String isoCode;

    @NotBlank
    @Column(name = "label", nullable = false)
    private String label;

    public Country isoCode(String isoCode) {
        this.isoCode = isoCode;
        return this;
    }

    public Country label(String label) {
        this.label = label;
        return this;
    }
}
