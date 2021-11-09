/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * Triggers when child templates AND products
 * of a {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel} are empty.
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = BundleTemplateProductsAssignedValidator.class)
@Documented
public @interface BundleTemplateProductsAssigned
{
	String message() default "{de.hybris.platform.configurablebundleservices.constraints.BundleTemplateProductsAssigned.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
