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
import javax.validation.constraints.Pattern;


@Target(
{ java.lang.annotation.ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy =
{ SnCollectionPatternValidator.class })
public @interface SnCollectionPattern {
	String regexp() default "";

	Pattern.Flag[] flags();

	String message() default "{de.hybris.platform.searchservices.validation.constraints.SnCollectionPattern.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
