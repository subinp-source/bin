/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.search.solrfacetsearch.provider.impl;

import de.hybris.platform.acceleratorservices.promotions.dao.PromotionsDao;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.springframework.beans.factory.annotation.Required;


public class PromotionFacetDisplayNameProvider extends AbstractFacetValueDisplayNameProvider
{
	private PromotionsDao promotionsDao;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue)
	{
		final AbstractPromotionModel promotion = promotionsDao.getPromotionForCode(facetValue);
		if (promotion != null)
		{
			return promotion.getName();
		}
		return facetValue;
	}

	protected PromotionsDao getPromotionsDao()
	{
		return promotionsDao;
	}

	@Required
	public void setPromotionsDao(final PromotionsDao promotionsDao)
	{
		this.promotionsDao = promotionsDao;
	}
}
