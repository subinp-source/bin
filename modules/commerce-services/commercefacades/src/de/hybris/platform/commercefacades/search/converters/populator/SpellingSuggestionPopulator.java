/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.search.converters.populator;

import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.beans.factory.annotation.Required;


/**
 */
public class SpellingSuggestionPopulator<QUERY, STATE> implements Populator<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>>
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
	public void populate(final SpellingSuggestionData<QUERY> source, final SpellingSuggestionData<STATE> target)
	{
		target.setSuggestion(source.getSuggestion());
		if (source.getQuery() != null)
		{
			target.setQuery(getSearchStateConverter().convert(source.getQuery()));
		}
	}
}
