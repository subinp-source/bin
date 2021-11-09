/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search;


import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;

import java.util.List;

/**
 * Autocomplete interface. Its purpose is to retrieve valid search terms that start with the user's given input, to
 * enhance the search experience and avoid searches for nonexistent terms. This interface/service should be called
 * asynchronously, assisting the user while typing.
 *
 * @param <RESULT>
 *           The type of the result data structure containing the returned suggestions
 */
public interface ProductSearchAutocompleteService<RESULT extends AutocompleteSuggestion>
{
	/**
	 * Get the auto complete suggestions for the input provided.
	 *
	 * @param input
	 *           the user's input on which the autocomplete is based
	 * @return a list of suggested search terms
	 */
	List<RESULT> getAutocompleteSuggestions(String input);
}
