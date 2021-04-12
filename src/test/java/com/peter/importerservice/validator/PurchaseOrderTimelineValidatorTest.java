package com.peter.importerservice.validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;
import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseOrderTimelineValidatorTest {

  @Mock ConstraintValidatorContext context;

  @Mock ConstraintViolationBuilder builder;

  @Mock NodeBuilderCustomizableContext nodeBuilder;

  private PurchaseOrderTimelineValidator underTest;

  @BeforeEach
  public void setUp() {
    underTest = new PurchaseOrderTimelineValidator();
  }

  @AfterEach
  public void tearDown() {
    underTest = null;
  }

  @Test
  @DisplayName("Should pass with a null object")
  void testWithNullValue() {
    assertTrue(underTest.isValid(null, context));
  }

  @Test
  @DisplayName("Should pass with a shipment date null")
  void testWithNoShipmentDate() {
    assertTrue(underTest.isValid(newTimeConstraint(LocalDate.now(), null), context));
  }

  @Test
  @DisplayName("Should pass with a shipment date greater or equal to order date")
  void testWithShipmentDateGeOrderDate() {
    assertTrue(
        underTest.isValid(
            newTimeConstraint(LocalDate.now(), LocalDate.now().plusDays(5L)), context));
  }

  @Test
  @DisplayName("Should pass with a shipment date equal to order date")
  void testWithShipmentDateEqualOrderDate() {
    assertTrue(underTest.isValid(newTimeConstraint(LocalDate.now(), LocalDate.now()), context));
  }

  @Test
  @DisplayName("Should not pass with a shipment date lesser to order date")
  void testWithShipmentDateLesserOrderDate() {
    setContextBehaviour();
    assertFalse(
        underTest.isValid(
            newTimeConstraint(LocalDate.now(), LocalDate.now().plusDays(-8L)), context));

    assertContext();
  }

  @Test
  @DisplayName("Should pass with an order date null")
  void testWithoutOrderDate() {
    assertTrue(underTest.isValid(newTimeConstraint(null, LocalDate.now().plusDays(-8L)), context));
  }

  @Test
  @DisplayName("Should pass with any filled dates")
  void testWithoutDates() {
    assertTrue(underTest.isValid(newTimeConstraint(null, null), context));
  }

  private void setContextBehaviour() {
    when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    when(builder.addPropertyNode(anyString())).thenReturn(nodeBuilder);
  }

  private void assertContext() {
    verify(context, times(1)).buildConstraintViolationWithTemplate(anyString());
    verify(builder, times(1)).addPropertyNode(anyString());
    verify(nodeBuilder, times(1)).addConstraintViolation();
  }

  private ITimelineConstraint newTimeConstraint(
      final LocalDate orderDate, final LocalDate shipmentDate) {
    return new ITimelineConstraint() {

      @Override
      public LocalDate getShipmentDate() {
        return shipmentDate;
      }

      @Override
      public LocalDate getOrderDate() {
        return orderDate;
      }
    };
  }
}
