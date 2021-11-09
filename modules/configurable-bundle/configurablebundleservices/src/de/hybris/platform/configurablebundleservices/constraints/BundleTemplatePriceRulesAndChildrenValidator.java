/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import org.springframework.util.CollectionUtils;
import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Triggers when child templates AND changeProductPriceBundleRules
 * of {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel} are not empty.
 */
public class BundleTemplatePriceRulesAndChildrenValidator
        extends BasicBundleTemplateValidator<BundleTemplatePriceRulesAndChildren>
{
    @Override
    public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        if (CollectionUtils.isEmpty(value.getChildTemplates())
                || CollectionUtils.isEmpty(value.getChangeProductPriceBundleRules()))
        {
            return true;
        }
        else
        {
            buildErrorMessage(BundleTemplateModel.CHANGEPRODUCTPRICEBUNDLERULES, context);
            return false;
        }
    }
}
