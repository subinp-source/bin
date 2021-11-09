/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.constraints;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import de.hybris.platform.core.model.product.ProductModel;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


/**
 * The value of the annotated element (a product's priceRowsValid attribute) must be
 * {@link Boolean#TRUE}. Supported type is {@link ProductModel} and its sub types.
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PriceRowsValidValidator.class)
@Documented
public @interface PriceRowsValid
{
	String message() default "{de.hybris.platform.subscriptionservices.constraints.PriceRowsValid.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String priceRowType();

}
