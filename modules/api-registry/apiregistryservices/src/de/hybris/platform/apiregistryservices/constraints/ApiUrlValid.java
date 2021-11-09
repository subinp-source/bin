/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation to check if the given string value is a valid URL.
 * The protocol of the url should match one of the determined protocols.
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ApiUrlValidator.class)
@Documented
public @interface ApiUrlValid
{
	String message() default "de.hybris.platform.apiregistryservices.constraints.ApiUrlValid.message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
