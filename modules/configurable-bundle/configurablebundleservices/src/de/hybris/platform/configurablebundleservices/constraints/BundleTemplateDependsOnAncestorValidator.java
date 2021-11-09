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
 * is an ancestor of the model.
 */
public class BundleTemplateDependsOnAncestorValidator
        extends BasicBundleTemplateValidator<BundleTemplateDependsOnAncestor>
{
    @Override
    public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        BundleTemplateModel ancestor = value.getParentTemplate();
        while (ancestor != null)
        {
            if (value.getDependentBundleTemplates() != null && value.getDependentBundleTemplates().contains(ancestor))
            {
                buildErrorMessage(BundleTemplateModel.DEPENDENTBUNDLETEMPLATES, context, ancestor.getId());
                return false;
            }
            ancestor = ancestor.getParentTemplate();
        }
        return true;
    }
}
