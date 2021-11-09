/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.converters.Populator;

import org.springframework.util.Assert;


/**
 * Converter implementation for {@link de.hybris.platform.catalog.model.classification.ClassificationClassModel} as
 * source and {@link de.hybris.platform.commercefacades.product.data.ClassificationData} as target type.
 */
public class ClassificationPopulator implements Populator<ClassificationClassModel, ClassificationData>
{

	@Override
	public void populate(final ClassificationClassModel source, final ClassificationData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setName(source.getName());
	}
}
