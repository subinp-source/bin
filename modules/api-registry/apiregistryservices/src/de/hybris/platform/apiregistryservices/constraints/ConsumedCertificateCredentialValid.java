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
 * Validation for {@link de.hybris.platform.apiregistryservices.model.ConsumedCertificateCredentialModel}. All instances
 * must have a valid certificate and a valid private key.
 */
@Target(
{ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = ConsumedCertificateCredentialValidator.class)
@Documented
public @interface ConsumedCertificateCredentialValid
{
	String message() default "de.hybris.platform.apiregistryservices.constraints.ConsumedCertificateCredentialValid.message";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
