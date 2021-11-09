/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;

import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Triggers when one of the
 * {@link BundleTemplateModel#getDependentBundleTemplates()}
 * is is the model itself.
 */
public class BundleTemplateDependsOnItselfValidator extends BasicBundleTemplateValidator<BundleTemplateDependsOnItself>
{
    @Override
    public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        if (value.getDependentBundleTemplates() != null && value.getDependentBundleTemplates().contains(value))
        {
            buildErrorMessage(BundleTemplateModel.DEPENDENTBUNDLETEMPLATES, context, value.getId());
            return false;
        }
        return true;
    }
}

