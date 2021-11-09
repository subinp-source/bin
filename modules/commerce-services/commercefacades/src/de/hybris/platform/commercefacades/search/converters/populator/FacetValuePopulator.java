/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.search.converters.populator;

import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;

/**
 */
public class FacetValuePopulator<QUERY, STATE> implements Populator<FacetValueData<QUERY>, FacetValueData<STATE>>
{
	private Converter<QUERY, STATE> searchStateConverter;

	protected Converter<QUERY, STATE> getSearchStateConverter()
	{
		return searchStateConverter;
	}

	@Required
	public void setSearchStateConverter(final Converter<QUERY, STATE> searchStateConverter)
	{
		this.searchStateConverter = searchStateConverter;
	}

	@Override
	public void populate(final FacetValueData<QUERY> source, final FacetValueData<STATE> target)
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
		target.setCount(source.getCount());
		target.setSelected(source.isSelected());

		if (source.getQuery() != null)
		{
			target.setQuery(getSearchStateConverter().convert(source.getQuery()));
		}
	}
}
