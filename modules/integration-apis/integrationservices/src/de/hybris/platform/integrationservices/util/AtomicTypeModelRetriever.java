/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * A helper class to retrieve AtomicTypeModels given a type code.
 */
public class AtomicTypeModelRetriever
{
	private static final Log LOG = Log.getLogger(AtomicTypeModelRetriever.class);

	private final FlexibleSearchService flexibleSearchService;

	/**
	 * Creates new instance of the retriever.
	 * @param flexibleSearchService to search for the AtomicTypeModel.
	 */
	public AtomicTypeModelRetriever(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	/**
	 * Retrieves the AtomicTypeModel present in the type system.
	 * @param type value for the AtomicTypeModel requested.
	 * @return an AtomicTypeModel for the given type or null if the type isn't found.
	 */
	public AtomicTypeModel get(final String type)
	{
		final var sample = new AtomicTypeModel();
		sample.setCode(type);
		try
		{
			return flexibleSearchService.getModelByExample(sample);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.debug("Can't find atomic type '{}'", type);
		}
		return null;
	}
}