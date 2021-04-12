package com.peter.importerservice.service;

import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.domain.Product;
import com.peter.importerservice.domain.PurchaseOrder;
import com.peter.importerservice.domain.PurchaseOrderProduct;
import com.peter.importerservice.repository.PurchaseOrderRepository;
import com.peter.importerservice.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final FactoryService factoryService;
    private final ProductService productService;
    private final PurchaseOrderProductService purchaseOrderProductService;

    public PurchaseOrder create(final PurchaseOrder purchaseOrder) {
        return create(purchaseOrder, false);
    }

    public PurchaseOrder create(final PurchaseOrder purchaseOrder, Boolean dryRun) {
        if (!dryRun) {
//            BusinessEvent.builder()
//                    .elementType(BusinessEventElementType.PURCHASE_ORDER)
//                    .actionType(BusinessEventActionType.CREATE)
//                    .comment("Creation of a new purchase order")
//                    .publish();
        }
        final PurchaseOrder savedPO = savePO(purchaseOrder);

        if (!dryRun) {
//            BusinessEvent.builder()
//                    .elementType(BusinessEventElementType.PURCHASE_ORDER)
//                    .elementId(savedPO.getId())
//                    .actionType(BusinessEventActionType.CREATE)
//                    .comment("Purchase order created")
//                    .publish();
        }

        return savedPO;
    }

    private PurchaseOrder savePO(PurchaseOrder purchaseOrder) {
        final PurchaseOrder toSavePO;
        if (purchaseOrder.getId() != null) {
            toSavePO =
                    purchaseOrderRepository
                            .findById(purchaseOrder.getId())
                            .orElseThrow(() -> new RuntimeException(String.format(
                                    "No %s was found for this identifier: %s",
                                    PurchaseOrder.class,
                                    purchaseOrder.getId()))
                            );
        } else {
            toSavePO = purchaseOrder;
        }

        if (referenceExistsForCurrentUserBrand(
                purchaseOrder.getId(), purchaseOrder.getReference())) {
            throw new RuntimeException(purchaseOrder.getReference());
        }

        toSavePO.setShipmentDate(purchaseOrder.getShipmentDate());
        toSavePO.setOrderDate(purchaseOrder.getOrderDate());
        toSavePO.setReference(purchaseOrder.getReference());

        final Factory factory = factoryService.getOne(purchaseOrder.getFactory().getId());
        toSavePO.setFactory(factory);

        final Set<PurchaseOrderProduct> updatedPurchaseOrderProducts =
                purchaseOrder.getPurchaseOrderProducts().stream()
                        .map(
                                purchaseOrderProduct -> {
                                    Product product =
                                            productService
                                                    .getOne(purchaseOrderProduct.getProduct().getId())
                                                    .orElseThrow(
                                                            () ->
                                                                    new RuntimeException(String.format(
                                                                            "No %s was found for this identifier: %s",
                                                                            Product.class,
                                                                            purchaseOrderProduct.getProduct().getId())));
                                    if (null == purchaseOrderProduct.getId()) {
                                        return new PurchaseOrderProduct()
                                                .product(product)
                                                .purchaseOrder(toSavePO)
                                                .quantity(purchaseOrderProduct.getQuantity())
                                                .unit(purchaseOrderProduct.getUnit());
                                    } else {
                                        return purchaseOrderProductService
                                                .getOne(purchaseOrderProduct.getId())
                                                .orElseThrow(
                                                        () ->
                                                                new RuntimeException(String.format(
                                                                        "No %s was found for this identifier: %s",
                                                                        PurchaseOrderProduct.class,
                                                                        purchaseOrderProduct.getId())))
                                                .quantity(purchaseOrderProduct.getQuantity())
                                                .unit(purchaseOrderProduct.getUnit())
                                                .product(product);
                                    }
                                })
                        .collect(Collectors.toSet());
        toSavePO.getPurchaseOrderProducts().clear();
        toSavePO.getPurchaseOrderProducts().addAll(updatedPurchaseOrderProducts);
        final var savedPO = purchaseOrderRepository.saveAndFlush(toSavePO);
//        applicationEventPublisher.publishEvent(new PurchaseOrderSavedEvent(savedPO));

        return savedPO;
    }

    @Transactional(readOnly = true)
    public boolean referenceExistsForCurrentUserBrand(Long purchaseOrderId, String reference) {
        final Long brandId = SecurityUtils.getCurrentUserBrandWhenAuthorized();
        if (purchaseOrderId != null) {
            return purchaseOrderRepository.existsByIdNotAndReferenceAndBrandId(
                    purchaseOrderId, reference, brandId);
        } else {
            return purchaseOrderRepository.existsByReferenceAndBrandId(reference, brandId);
        }
    }
}
