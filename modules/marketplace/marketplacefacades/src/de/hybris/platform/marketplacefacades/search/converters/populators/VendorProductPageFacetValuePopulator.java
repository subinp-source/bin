/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplacefacades.search.converters.populators;

import de.hybris.platform.commercefacades.search.converters.populator.FacetValuePopulator;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;

import org.springframework.util.Assert;


/**
 * A populator for setting facet values.
 */
public class VendorProductPageFacetValuePopulator<QUERY, STATE> extends FacetValuePopulator<QUERY, STATE>
{

	@Override
	public void populate(final FacetValueData<QUERY> source, final FacetValueData<STATE> target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		super.populate(source, target);
	}
}
