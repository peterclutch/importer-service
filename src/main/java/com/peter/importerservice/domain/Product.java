package com.peter.importerservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequenceGenerator")
    @SequenceGenerator(
            name = "product_sequenceGenerator",
            sequenceName = "product_sequence",
            allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "identifier_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SpecificationType identifierType;

    @NotNull
    @NotBlank
    @Column(name = "identifier_value", nullable = false)
    private String identifierValue;

    @NotNull
    @NotBlank
    @Column(name = "specification")
    private String specification;

    @JsonIgnore
    @ManyToOne
    @ToString.Exclude private Brand brand;

    public Product id(final Long id) {
        this.id = id;
        return this;
    }

    public Product identifierType(SpecificationType identifierType) {
        this.identifierType = identifierType;
        return this;
    }

    public Product identifierValue(String identifierValue) {
        this.identifierValue = identifierValue;
        return this;
    }

    public Product specification(String specification) {
        this.specification = specification;
        return this;
    }

    public Product brand(Brand brand) {
        this.brand = brand;
        return this;
    }
}
