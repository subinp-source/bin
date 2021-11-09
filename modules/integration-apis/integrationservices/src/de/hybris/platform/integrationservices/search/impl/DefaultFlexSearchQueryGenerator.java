/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.integrationservices.search.FlexSearchQueryGenerator;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexSearchQueryGenerator implements FlexSearchQueryGenerator
{
	private FlexSearchQueryBuilderFactory queryBuilderFactory;

	@Override
	public FlexibleSearchQuery generate(final ItemSearchRequest searchRequest)
	{
		return queryBuilderFactory.createQueryBuilder(searchRequest).build();
	}

	/**
	 * Injects query builder factory implementation to be used by this generator.
	 *
	 * @param factory factory implementation to use
	 */
	@Required
	public void setQueryBuilderFactory(@NotNull final FlexSearchQueryBuilderFactory factory)
	{
		queryBuilderFactory = factory;
	}
}
