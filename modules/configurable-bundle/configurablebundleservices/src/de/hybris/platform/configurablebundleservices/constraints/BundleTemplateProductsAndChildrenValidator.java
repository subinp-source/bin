/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import org.apache.commons.collections.CollectionUtils;
import javax.validation.ConstraintValidatorContext;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * Triggers when child templates AND products
 * of {@link de.hybris.platform.configurablebundleservices.model.BundleTemplateModel} are not empty.
 */
public class BundleTemplateProductsAndChildrenValidator
        extends BasicBundleTemplateValidator<BundleTemplateProductsAndChildren>
{
    @Override
    public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
    {
        validateParameterNotNull(value, "Validating object is null");
        return CollectionUtils.isEmpty(value.getChildTemplates())
               || CollectionUtils.isEmpty(value.getProducts());
    }
}
