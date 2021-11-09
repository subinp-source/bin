/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populate {@code AbstractPageData#setOtherProperties(Map)} for product pages
 */
public class ProductPageModelToDataRenderingPopulator implements Populator<AbstractPageModel, Map<String, Object>>
{
	private Predicate<ItemModel> productPageTypePredicate;

	@Override
	public void populate(final AbstractPageModel source, final Map<String, Object> target) throws ConversionException
	{
		if (getProductPageTypePredicate().test(source))
		{
			final List<String> codes = source.getRestrictions().stream()
					.filter(restriction -> CMSProductRestrictionModel.class.isAssignableFrom(restriction.getClass()))
					.map(restriction -> (CMSProductRestrictionModel) restriction)
					.filter(restriction -> !restriction.getProducts().isEmpty()) //
					.flatMap(restriction -> restriction.getProducts().stream()) //
					.map(ProductModel::getCode) //
					.collect(Collectors.toList());
			target.put("productCodes", codes);
		}
	}

	protected Predicate<ItemModel> getProductPageTypePredicate()
	{
		return productPageTypePredicate;
	}

	@Required
	public void setProductPageTypePredicate(final Predicate<ItemModel> productPageTypePredicate)
	{
		this.productPageTypePredicate = productPageTypePredicate;
	}

}
