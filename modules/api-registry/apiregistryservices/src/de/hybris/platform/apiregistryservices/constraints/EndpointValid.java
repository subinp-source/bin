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
 * Annotation for {@link EndpointValidator}
 */
@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = EndpointValidator.class)
@Documented
public @interface EndpointValid
{
    String message() default "de.hybris.platform.apiregistryservices.constraints.Endpoint.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
