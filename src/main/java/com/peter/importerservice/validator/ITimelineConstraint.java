package com.peter.importerservice.validator;

import java.time.LocalDate;

public interface ITimelineConstraint {

  LocalDate getShipmentDate();

  LocalDate getOrderDate();
}
