/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.attributeconverters;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Objects;

/**
 * Rendering Attribute Converter for {@link de.hybris.platform.core.model.product.ProductModel}.
 * Converts the category into its product code (string).
 */
public class ProductToDataContentConverter implements Converter<ProductModel, String>
{
	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------
	@Override
	public String convert(ProductModel source)
	{
		if(Objects.isNull(source))
		{
			return null;
		}
		return source.getCode();
	}
}
