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
 * Triggers when one of the
 * {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel#getRequiredBundleTemplates()}
 * is an ancestor of the model.
 */
@Target(
        { FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = BundleTemplateRequiresAncestorValidator.class)
@Documented
public @interface BundleTemplateRequiresAncestor
{
    String message() default "{de.hybris.platform.configurablebundleservices.constraints.BundleTemplateRequiresAncestor.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
