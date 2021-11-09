/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validates that conditional product list of a price rule has at least 1 product.
 */
@Target(
        { FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PriceRuleConditionalProductsAssignedValidator.class)
@Documented
public @interface PriceRuleConditionalProductsAssigned
{
    String message() default
            "{de.hybris.platform.configurablebundleservices.constraints.PriceRuleConditionalProductsAssigned.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
