package com.peter.importerservice.service.importer.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PurchaseOrderLineImportValidation.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PurchaseOrderLineImport {

  String message() default "Purchase Order line is not correct.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
