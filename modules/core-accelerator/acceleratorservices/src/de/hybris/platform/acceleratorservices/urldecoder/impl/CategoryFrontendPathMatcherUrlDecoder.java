/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.urldecoder.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class CategoryFrontendPathMatcherUrlDecoder extends BaseFrontendPathMatcherUrlDecoder<CategoryModel>
{

	private static final Logger LOG = Logger.getLogger(CategoryFrontendPathMatcherUrlDecoder.class);
	private CategoryService categoryService;

	@Override
	protected CategoryModel translateId(final String id)
	{
		try
		{
			return getCategoryService().getCategoryForCode(id);
		}
		catch (ModelNotFoundException | UnknownIdentifierException e)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(e);
			}
			return null;
		}
	}

	@Required
	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	public CategoryService getCategoryService()
	{
		return this.categoryService;
	}

}
