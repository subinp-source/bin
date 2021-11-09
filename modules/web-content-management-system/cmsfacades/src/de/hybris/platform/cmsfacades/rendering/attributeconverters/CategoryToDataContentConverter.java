/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.attributeconverters;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.common.functions.Converter;

import java.util.Objects;

/**
 * Rendering Attribute Converter for {@link de.hybris.platform.category.model.CategoryModel}.
 * Converts the category into its category code (string).
 */
public class CategoryToDataContentConverter implements Converter<CategoryModel, String>
{
	// --------------------------------------------------------------------------
	// Public API
	// --------------------------------------------------------------------------
	@Override
	public String convert(CategoryModel source)
	{
		if(Objects.isNull(source))
		{
			return null;
		}

		return source.getCode();
	}
}
