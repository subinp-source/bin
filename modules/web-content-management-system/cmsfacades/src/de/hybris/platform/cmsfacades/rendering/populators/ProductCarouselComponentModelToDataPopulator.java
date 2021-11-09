/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel.PRODUCTCODES;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator to provide product codes if they were not automatically retrieved directly from the item model.
 * It can happen if the item was just created (never saved) and populated from a version.
 */
public class ProductCarouselComponentModelToDataPopulator implements Populator<CMSItemModel, Map<String, Object>>
{
	private Predicate<CMSItemModel> productCarouselComponentCarouselPredicate;

	@Override
	public void populate(final CMSItemModel cmsItemModel, final Map<String, Object> stringObjectMap) throws ConversionException
	{
		if (getProductCarouselComponentCarouselPredicate().test(cmsItemModel) && Objects.isNull(stringObjectMap.get(PRODUCTCODES)))
		{
			final ProductCarouselComponentModel productCarouselComponent = (ProductCarouselComponentModel) cmsItemModel;
			if (Objects.nonNull(productCarouselComponent.getProductCodes()))
			{
				stringObjectMap.put(PRODUCTCODES, productCarouselComponent.getProductCodes());
			}
			else if (Objects.nonNull(productCarouselComponent.getProducts()))
			{
				final List<String> productCodes = productCarouselComponent.getProducts() //
						.stream() //
						.map(ProductModel::getCode) //
						.collect(Collectors.toList());
				stringObjectMap.put(PRODUCTCODES, productCodes);
			}
		}
	}

	protected Predicate<CMSItemModel> getProductCarouselComponentCarouselPredicate()
	{
		return productCarouselComponentCarouselPredicate;
	}

	@Required
	public void setProductCarouselComponentCarouselPredicate(
			final Predicate<CMSItemModel> productCarouselComponentCarouselPredicate)
	{
		this.productCarouselComponentCarouselPredicate = productCarouselComponentCarouselPredicate;
	}
}
