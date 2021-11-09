/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSCategoryRestrictionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populate {@code AbstractPageData#setOtherProperties(Map)} for category pages
 */
public class CategoryPageModelToDataRenderingPopulator implements Populator<AbstractPageModel, Map<String, Object>>
{
	private Predicate<ItemModel> categoryPageTypePredicate;

	@Override
	public void populate(final AbstractPageModel source, final Map<String, Object> target) throws ConversionException
	{
		if (getCategoryPageTypePredicate().test(source))
		{
			final List<String> codes = source.getRestrictions().stream()
					.filter(restriction -> CMSCategoryRestrictionModel.class.isAssignableFrom(restriction.getClass()))
					.map(restriction -> (CMSCategoryRestrictionModel) restriction)
					.filter(restriction -> !restriction.getCategories().isEmpty()) //
					.flatMap(restriction -> restriction.getCategories().stream()) //
					.map(CategoryModel::getCode) //
					.collect(Collectors.toList());
			target.put("categoryCodes", codes);
		}
	}

	protected Predicate<ItemModel> getCategoryPageTypePredicate()
	{
		return categoryPageTypePredicate;
	}

	@Required
	public void setCategoryPageTypePredicate(final Predicate<ItemModel> categoryPageTypePredicate)
	{
		this.categoryPageTypePredicate = categoryPageTypePredicate;
	}

}
