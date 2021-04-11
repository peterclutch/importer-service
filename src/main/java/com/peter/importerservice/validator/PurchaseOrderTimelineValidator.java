package com.peter.importerservice.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PurchaseOrderTimelineValidator
    implements ConstraintValidator<PurchaseOrderTimeline, ITimelineConstraint> {

  @Override
  public boolean isValid(ITimelineConstraint value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return true;
    }

    var timelineCheck =
        null == value.getShipmentDate()
            || null == value.getOrderDate()
            || !value.getShipmentDate().isBefore(value.getOrderDate());

    if (!timelineCheck) {
      context
          .buildConstraintViolationWithTemplate(
              ValidationErrorCode.PurchaseOrder.TIMELINE_ERROR.name())
          .addPropertyNode("orderDate")
          .addConstraintViolation();
    }
    return timelineCheck;
  }
}
