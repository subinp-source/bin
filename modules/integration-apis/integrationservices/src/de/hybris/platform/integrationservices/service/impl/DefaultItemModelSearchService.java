/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.service.ItemModelSearchService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link ItemModelSearchService}
 */
public class DefaultItemModelSearchService implements ItemModelSearchService
{
	private static final Logger LOG = Log.getLogger(DefaultItemModelSearchService.class);

	private final ModelService modelService;

	/**
	 * Instantiates the {@link ItemModelSearchService}
	 *
	 * @param modelService Service to look up the item
	 */
	public DefaultItemModelSearchService(@NotNull final ModelService modelService)
	{
		Preconditions.checkArgument(modelService != null, "ModelService cannot be null");
		this.modelService = modelService;
	}

	@Override
	public <T extends ItemModel> Optional<T> nonCachingFindByPk(final PK pk)
	{
		try
		{
			if (pk != null)
			{
				LOG.trace("Calling model service detachAll()");
				modelService.detachAll();
				return Optional.of(modelService.get(pk));
			}
			LOG.debug("Not finding the item because the PK provided is null");
		}
		catch (final ModelLoadingException e)
		{
			LOG.warn("The item with PK={} was not found.", pk, e);
		}
		return Optional.empty();
	}
}
