package com.peter.importerservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "factory")
@Getter
@Setter
@ToString(exclude = {"brandFactories"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Factory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factory_sequenceGenerator")
    @SequenceGenerator(
            name = "factory_sequenceGenerator",
            sequenceName = "factory_sequence",
            allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToMany(mappedBy = "id.factory", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<BrandFactory> brandFactories = new HashSet<>();

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "local_name")
    private String localName;

    public Factory id(Long id) {
        this.id = id;
        return this;
    }

    public Factory addBrandEntity(BrandFactory brandFactory) {
        brandFactories.add(brandFactory);
        return this;
    }
}
