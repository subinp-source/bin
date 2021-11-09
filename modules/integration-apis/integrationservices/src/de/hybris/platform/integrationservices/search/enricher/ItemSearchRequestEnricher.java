/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.enricher;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;

import javax.validation.constraints.NotNull;

/**
 * An extension point where the {@link ItemSearchRequest} can be enriched before performing the search
 */
public interface ItemSearchRequestEnricher
{
	/**
	 * Enriches the {@link ItemSearchRequest}
	 *
	 * @param request request to enrich
	 * @return The enriched request
	 */
	ItemSearchRequest enrich(@NotNull ItemSearchRequest request);
}