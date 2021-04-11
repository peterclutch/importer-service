package com.peter.importerservice.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "purchase_order")
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "purchase_order_sequenceGenerator")
    @SequenceGenerator(
            name = "purchase_order_sequenceGenerator",
            sequenceName = "purchase_order_sequence",
            allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "reference", nullable = false)
    private String reference;

    @Column(name = "shipment_date")
    private LocalDate shipmentDate;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @ManyToOne(optional = false)
    @NotNull
    private Factory factory;

    @ManyToOne(optional = false)
    @NotNull
    private Brand brand;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotEmpty
    @Valid
    private Set<PurchaseOrderProduct> purchaseOrderProducts = new HashSet<>();

    public PurchaseOrder id(final Long id) {
        this.id = id;
        return this;
    }

    public PurchaseOrder reference(String reference) {
        this.reference = reference;
        return this;
    }

    public PurchaseOrder shipmentDate(LocalDate shipmentDate) {
        this.shipmentDate = shipmentDate;
        return this;
    }

    public PurchaseOrder orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public PurchaseOrder factory(Factory factory) {
        this.factory = factory;
        return this;
    }

    public PurchaseOrder purchaseOrderProducts(Set<PurchaseOrderProduct> purchaseOrderProducts) {
        this.purchaseOrderProducts = purchaseOrderProducts;
        return this;
    }

    public PurchaseOrder brand(Brand brand) {
        this.brand = brand;
        return this;
    }
}
