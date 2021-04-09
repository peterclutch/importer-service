package com.peter.importerservice.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_sequenceGenerator")
    @SequenceGenerator(
            name = "address_sequenceGenerator",
            sequenceName = "address_sequence",
            allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "country", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "area_code")
    private AdministrativeArea areaCode;

    @NotBlank
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank
    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "post_code")
    private String postCode;

    public Address id(Long id) {
        this.id = id;
        return this;
    }

    public Address country(Country country) {
        this.country = country;
        return this;
    }

    public Address areaCode(AdministrativeArea areaCode) {
        this.areaCode = areaCode;
        return this;
    }

    public Address street(String street) {
        this.street = street;
        return this;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public Address postCode(String postCode) {
        this.postCode = postCode;
        return this;
    }
}
