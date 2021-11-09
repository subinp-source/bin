/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingservices.validation.annotations;


import de.hybris.platform.consignmenttrackingservices.validation.validators.ConsignmentTrackingIdValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target(
{ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy =
{ ConsignmentTrackingIdValidator.class })
@Documented
public @interface ConsignmentTrackingIdValid
{
	String message() default "{de.hybris.platform.consignmenttrackingservices.validation.annotations.ConsignmentTrackingIdValid.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
