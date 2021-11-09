/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Triggers when {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel#getChildTemplates()}
 * is not empty AND {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel#getBundleSelectionCriteria()}
 * is not empty.
 */
public class BundleTemplateSelectionCriteriaValidator
        extends BasicBundleTemplateValidator<BundleTemplateSelectionCriteria>
{
    @Override
    public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        return !(CollectionUtils.isNotEmpty(value.getChildTemplates()) && value.getBundleSelectionCriteria() != null);
    }
}
