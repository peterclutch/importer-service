package com.peter.importerservice.service;

import com.peter.importerservice.cucumber.web.rest.QimaTestUtil;
import com.peter.importerservice.domain.Factory;
import com.peter.importerservice.domain.Product;
import com.peter.importerservice.domain.PurchaseOrder;
import com.peter.importerservice.domain.PurchaseOrderProduct;
import com.peter.importerservice.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderServiceTest {

    @Mock private PurchaseOrderRepository purchaseOrderRepository;
    @Mock private FactoryService factoryService;
    @Mock private ProductService productService;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @BeforeEach
    public void setUp() {
        QimaTestUtil.resetSecurityContext();
    }

    @AfterEach
    public void tearDown() {
        QimaTestUtil.resetSecurityContext();
    }

    @Test
    @DisplayName("given a PO with an already existing reference, when we create it, then throw error")
    public void cannotCreatePurchaseOrderIfReferenceAlreadyExist() {
        QimaTestUtil.setTestBrandUserSecurityContext();

        final String purchaseOrderReference = "THIS_REF_ALREADY_EXISTS";
        final PurchaseOrder purchaseOrder = new PurchaseOrder().reference(purchaseOrderReference);

        when(purchaseOrderRepository.existsByReferenceAndBrandId(
                purchaseOrderReference, QimaTestUtil.TEST_BRAND_ID))
                .thenReturn(true);

        Assertions.assertThrows(RuntimeException.class, () -> purchaseOrderService.create(purchaseOrder));
    }

    @Test
    @DisplayName("given a PO with a non existing product, when we create it, then throw error")
    public void cannotUpdatePurchaseOrderIfProductDoesNotExist() {
        QimaTestUtil.setTestBrandUserSecurityContext();

        final Long productId = 2L;
        final Product product = new Product().id(productId);
        final PurchaseOrder purchaseOrder =
                new PurchaseOrder()
                        .factory(new Factory())
                        .purchaseOrderProducts(
                                Set.of(new PurchaseOrderProduct().quantity(1d).product(product)));

        when(factoryService.getOne(any())).thenReturn(new Factory());
        when(productService.getOne(productId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> purchaseOrderService.create(purchaseOrder));
    }
}
