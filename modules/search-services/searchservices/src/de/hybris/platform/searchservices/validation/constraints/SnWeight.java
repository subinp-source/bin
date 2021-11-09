/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target(
{ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy =
{ SnWeightValidator.class })
public @interface SnWeight {

	String message() default "{de.hybris.platform.searchservices.validation.constraints.SnWeight.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
