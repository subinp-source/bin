/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.AbstractBundleRuleModel;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Validates that conditional product list of a disable rule has at least 1 product.
 */
public class DisableRuleConditionalProductsAssignedValidator
    extends BasicBundleRuleValidator<DisableRuleConditionalProductsAssigned>
{
    @Override
    public boolean isValid(final AbstractBundleRuleModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        if (CollectionUtils.isEmpty(value.getConditionalProducts()))
        {
            buildErrorMessage(AbstractBundleRuleModel.CONDITIONALPRODUCTS, context, value.getId());
            return false;
        }
        return true;
    }
}
