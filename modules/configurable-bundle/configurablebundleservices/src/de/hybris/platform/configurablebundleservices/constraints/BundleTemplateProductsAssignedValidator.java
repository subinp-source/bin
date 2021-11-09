/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.constraints;

import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Validates that the given {@link BundleTemplateModel} has any child templates OR any products.
 */
public class BundleTemplateProductsAssignedValidator
		extends BasicBundleTemplateValidator<BundleTemplateProductsAssigned>
{
	@Override
	public boolean isValid(final BundleTemplateModel value, final ConstraintValidatorContext context)
	{
		validateParameterNotNull(value, "Validating object is null");
		return value.getPk() == null
				|| CollectionUtils.isNotEmpty(value.getChildTemplates())
				|| CollectionUtils.isNotEmpty(value.getProducts());
	}
}
