package com.peter.importerservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "brand_entity")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BrandFactory {

    @EqualsAndHashCode.Include
    @EmbeddedId
    private BrandEntityKey id;

    @NotBlank
    @Column
    private String customId;

    @NotNull
    @Fetch(FetchMode.JOIN)
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Address address;

    @NotNull
    @Fetch(FetchMode.JOIN)
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Contact contact;

    @Getter
    @Setter
    @Embeddable
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class BrandEntityKey implements Serializable {

        @ManyToOne(optional = false)
        @JoinColumn(updatable = false, name = "brand_id")
        @EqualsAndHashCode.Include
        private Brand brand;

        @ManyToOne(optional = false, fetch = FetchType.LAZY)
        @JoinColumn(updatable = false, name = "factory_id")
        @EqualsAndHashCode.Include
        private Factory factory;

        public BrandEntityKey factory(Factory factory) {
            this.factory = factory;
            return this;
        }

        public BrandEntityKey brand(Brand brand) {
            this.brand = brand;
            return this;
        }

        @Override
        public String toString() {
            var brandId = null != brand ? brand.getId() : null;
            var entityId = null != factory ? factory.getId() : null;

            return String.format("{brandId: %d, entityId: %d}", brandId, entityId);
        }
    }
}
