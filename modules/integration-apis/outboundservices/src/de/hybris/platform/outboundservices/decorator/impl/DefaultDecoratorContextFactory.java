/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.impl.NullIntegrationObjectDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorContextFactory;
import de.hybris.platform.outboundservices.facade.ConsumedDestinationNotFoundModel;
import de.hybris.platform.outboundservices.facade.SyncParameters;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link DecoratorContextFactory}
 */
public class DefaultDecoratorContextFactory implements DecoratorContextFactory
{
	private static final Logger LOG = Log.getLogger(DefaultDecoratorContextFactory.class);
	private static final String DEST_NOT_FOUND_ERROR_MSG = "Provided destination '%s' was not found.";
	private static final String IO_NOT_FOUND_ERROR_MSG = "Provided integration object '%s' was not found.";

	private final IntegrationObjectService integrationObjectService;
	private final FlexibleSearchService flexibleSearchService;
	private final DescriptorFactory descriptorFactory;

	public DefaultDecoratorContextFactory(final IntegrationObjectService integrationObjectService,
	                                      final FlexibleSearchService flexibleSearchService,
	                                      final DescriptorFactory descriptorFactory)
	{
		Preconditions.checkArgument(integrationObjectService != null, "integrationObjectService must be provided");
		Preconditions.checkArgument(flexibleSearchService != null, "flexibleSearchService must be provided");
		Preconditions.checkArgument(descriptorFactory != null, "descriptorFactory must be provided");
		this.integrationObjectService = integrationObjectService;
		this.flexibleSearchService = flexibleSearchService;
		this.descriptorFactory = descriptorFactory;
	}

	@Override
	public @NotNull DecoratorContext createContext(@NotNull final SyncParameters params)
	{
		final Collection<String> errors = new ArrayList<>();
		return DecoratorContext.decoratorContextBuilder()
		                       .withDestinationModel(getConsumedDestinationModel(params, errors))
		                       .withIntegrationObject(getIntegrationObjectDescriptor(params, errors))
		                       .withItemModel(params.getItem())
		                       .withOutboundSource(params.getSource())
		                       .withErrors(errors)
		                       .build();
	}

	private ConsumedDestinationModel getConsumedDestinationModel(final SyncParameters params, final Collection<String> errors)
	{
		final ConsumedDestinationModel destination = params.getDestination() != null ?
				params.getDestination() :
				getConsumedDestinationModelById(params.getDestinationId());
		if (destination instanceof ConsumedDestinationNotFoundModel)
		{
			errors.add(String.format(DEST_NOT_FOUND_ERROR_MSG, destination.getId()));
		}
		return destination;
	}

	private ConsumedDestinationModel getConsumedDestinationModelById(final String destinationId)
	{
		if (StringUtils.isNotBlank(destinationId))
		{
			try
			{
				final ConsumedDestinationModel example = new ConsumedDestinationModel();
				example.setId(destinationId);
				return flexibleSearchService.getModelByExample(example);

			}
			catch (final RuntimeException e)
			{
				LOG.trace("Failed to find ConsumedDestination with id '{}'", destinationId, e);
			}
		}
		return new ConsumedDestinationNotFoundModel(destinationId);
	}

	private IntegrationObjectDescriptor getIntegrationObjectDescriptor(final SyncParameters params,
	                                                                   final Collection<String> errors)
	{
		final IntegrationObjectModel integrationObjectModel = params.getIntegrationObject();
		return integrationObjectModel != null
				? descriptorFactory.createIntegrationObjectDescriptor(integrationObjectModel)
				: findIntegrationObject(params.getIntegrationObjectCode(), errors);
	}

	private IntegrationObjectDescriptor findIntegrationObject(final String integrationObjectCode, final Collection<String> errors)
	{
		if (StringUtils.isNotBlank(integrationObjectCode))
		{
			try
			{
				return descriptorFactory.createIntegrationObjectDescriptor(
						integrationObjectService.findIntegrationObject(integrationObjectCode));
			}
			catch (final ModelNotFoundException | AmbiguousIdentifierException e)
			{
				LOG.trace("", e);
			}
		}
		errors.add(String.format(IO_NOT_FOUND_ERROR_MSG, integrationObjectCode));
		return new NullIntegrationObjectDescriptor(integrationObjectCode);
	}
}
