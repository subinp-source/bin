/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades.search.converters.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.ProductCategorySearchPagePopulator;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;

import java.util.stream.Collectors;


/**
 * A populator for populating search result.
 */
public class VendorProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ITEM extends ProductData, SCAT, CATEGORY> //NOSONAR
		extends
		ProductCategorySearchPagePopulator<QUERY, STATE, RESULT, ProductData, SCAT, CATEGORY>
{

	@Override
	public void populate(final ProductCategorySearchPageData<QUERY, RESULT, SCAT> source,
			final ProductCategorySearchPageData<STATE, ProductData, CATEGORY> target)
	{
		source.setFacets(source.getFacets().stream().filter(facet -> "category".equalsIgnoreCase(facet.getCode()))
				.collect(Collectors.toList()));
		super.populate(source, target);
	}
}
