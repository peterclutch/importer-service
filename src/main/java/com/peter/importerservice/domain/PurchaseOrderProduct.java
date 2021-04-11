package com.peter.importerservice.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "purchase_order_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class PurchaseOrderProduct {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "purchase_order_product_sequenceGenerator")
    @SequenceGenerator(
            name = "purchase_order_product_sequenceGenerator",
            sequenceName = "purchase_order_product_sequence",
            allocationSize = 1)
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String unit;

    @EqualsAndHashCode.Include
    @NotNull
    private Double quantity;

    @ManyToOne(optional = false)
    @NotNull
    private PurchaseOrder purchaseOrder;

    @ManyToOne(optional = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Product product;

    public PurchaseOrderProduct id(Long id) {
        this.id = id;
        return this;
    }

    public PurchaseOrderProduct unit(String unit) {
        this.unit = unit;
        return this;
    }

    public PurchaseOrderProduct quantity(Double quantity) {
        this.quantity = quantity;
        return this;
    }

    public PurchaseOrderProduct product(Product product) {
        this.product = product;
        return this;
    }

    public PurchaseOrderProduct purchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        return this;
    }
}
