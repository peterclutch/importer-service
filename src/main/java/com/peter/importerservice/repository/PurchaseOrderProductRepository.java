package com.peter.importerservice.repository;

import com.peter.importerservice.domain.PurchaseOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderProductRepository extends JpaRepository<PurchaseOrderProduct, Long> {
}
