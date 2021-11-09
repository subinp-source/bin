/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.search.converters.populator;

import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.converters.Populator;


/**
 */
public class AutocompleteSuggestionPopulator implements Populator<AutocompleteSuggestion, AutocompleteSuggestionData>
{

	@Override
	public void populate(final AutocompleteSuggestion source, final AutocompleteSuggestionData target)
	{
		target.setTerm(source.getTerm());
	}
}
