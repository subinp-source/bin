/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Additional populator implementation for {@link de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData} as source and
 * {@link de.hybris.platform.commercefacades.product.data.ProductData} as target type.
 */

public class B2BSearchResultProductPopulator implements Populator<SearchResultValueData, ProductData>
{
	private static final String THUMBNAIL_FORMAT = "img-96Wx96H";

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final Map<String, Object> valuesMap = source.getValues();

		final String firstVariantUrl = (String) valuesMap.get("firstVariantUrl");
		if (!StringUtils.isEmpty(firstVariantUrl))
		{
			target.setFirstVariantCode(firstVariantUrl.substring(firstVariantUrl.lastIndexOf('/') + 1));
		}

		target.setFirstVariantImage((String) valuesMap.get(THUMBNAIL_FORMAT));
	}
}
