/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.facade.impl;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory;
import de.hybris.platform.outboundservices.config.OutboundServicesConfiguration;
import de.hybris.platform.outboundservices.decorator.DecoratorContext;
import de.hybris.platform.outboundservices.decorator.DecoratorContextFactory;
import de.hybris.platform.outboundservices.decorator.DecoratorExecution;
import de.hybris.platform.outboundservices.decorator.DefaultDecoratorExecution;
import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator;
import de.hybris.platform.outboundservices.facade.ConsumedDestinationNotFoundModel;
import de.hybris.platform.outboundservices.facade.OutboundServiceFacade;
import de.hybris.platform.outboundservices.facade.SyncParameters;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import rx.Observable;

/**
 * Default implementation of OutboundServiceFacade.
 */
public class DefaultOutboundServiceFacade implements OutboundServiceFacade
{
	private static final Logger LOG = Log.getLogger(DefaultOutboundServiceFacade.class);

	private IntegrationRestTemplateFactory integrationRestTemplateFactory;
	private IntegrationObjectService integrationObjectService;
	private List<OutboundRequestDecorator> decorators = Collections.emptyList();
	private OutboundServicesConfiguration outboundServicesConfiguration;
	private OutboundRequestDecorator monitoringDecorator;
	private FlexibleSearchService flexibleSearchService;
	private DecoratorContextFactory contextFactory;

	@Override
	public Observable<ResponseEntity<Map>> send(final ItemModel itemModel, final String ioCode, final String destinationId)
	{
		Preconditions.checkArgument(StringUtils.isNotBlank(destinationId), "destination cannot be null or empty");
		final ConsumedDestinationModel destination = getConsumedDestinationOrLogIfNotFound(
				itemModel, ioCode, destinationId);
		final SyncParameters params = SyncParameters.syncParametersBuilder()
		                                            .withItem(itemModel)
		                                            .withIntegrationObjectCode(ioCode)
		                                            .withDestination(destination)
		                                            .build();
		return send(params);
	}

	@Override
	public Observable<ResponseEntity<Map>> send(final SyncParameters params)
	{
		final DecoratorContext context = contextFactory.createContext(params);
		return createObservable(context);
	}

	protected Observable<ResponseEntity<Map>> createObservable(final DecoratorContext context)
	{
		final HttpHeaders headersToDecorate = new HttpHeaders();
		final Map<String, Object> payloadToDecorate = new HashMap<>();

		return Observable.just(context)
		                 .map((DecoratorContext ctx) -> {
			                 //createHttpEntity must happen first because it creates the OutboundRequestModel.
			                 // If an error occurs after the DefaultOutboundRequestResponseInterceptor will update the
			                 // OutboundRequestModel accordingly for monitoring when monitoring is enabled.
			                 final var entity = createHttpEntity(headersToDecorate, payloadToDecorate, ctx);
			                 final RestOperations rest = getIntegrationRestTemplateFactory().create(
					                 ctx.getDestinationModel());
			                 return rest.postForEntity(ctx.getDestinationModel().getUrl(), entity, Map.class);
		                 });
	}

	protected HttpEntity<Map<String, Object>> createHttpEntity(final HttpHeaders headers,
	                                                           final Map<String, Object> payload,
	                                                           final DecoratorContext context)
	{
		final List<OutboundRequestDecorator> requestDecorators = addMonitoringDecorator(getOutboundRequestDecorators());
		final DecoratorExecution execution = new DefaultDecoratorExecution(requestDecorators);
		return execution.createHttpEntity(headers, payload, context);
	}

	protected List<OutboundRequestDecorator> addMonitoringDecorator(final List<OutboundRequestDecorator> requestDecorators)
	{
		return getOutboundServicesConfiguration().isMonitoringEnabled() && monitoringDecorator != null ?
				Stream.concat(Stream.of(getMonitoringDecorator()), requestDecorators.stream())
				      .collect(Collectors.toList()) :
				requestDecorators;
	}

	/**
	 * @deprecated not used: destination ID is converted to {@link ConsumedDestinationModel} in {@link DecoratorContextFactory}.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected ConsumedDestinationModel getConsumedDestinationModelById(final String destinationId)
	{
		ConsumedDestinationModel destination = null;
		try
		{
			final ConsumedDestinationModel example = new ConsumedDestinationModel();
			example.setId(destinationId);
			destination = getFlexibleSearchService().getModelByExample(example);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Failed to find ConsumedDestination with id '{}'", destinationId, e);
		}
		return destination;
	}

	/**
	 * @deprecated not used: integration object code is converted to {@link de.hybris.platform.integrationservices.model.IntegrationObjectDescriptor}
	 * in {@link DecoratorContextFactory}.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected String findIntegrationObjectItemCode(final String integrationObjectCode, final ItemModel itemModel)
	{
		try
		{
			return getIntegrationObjectService().findIntegrationObjectItemByTypeCode(integrationObjectCode,
					itemModel.getItemtype()).getCode();
		}
		catch (final ModelNotFoundException | AmbiguousIdentifierException e)
		{
			LOG.trace("", e);
			return null;
		}
	}

	/**
	 * @deprecated use {@link #send(SyncParameters)} instead.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected Observable<ResponseEntity<Map>> orchestrate(final ItemModel itemModel, final String integrationObjectCode,
	                                                      final String destinationId)
	{
		// The payload is merged later on by DefaultPayloadBuildingRequestDecorator. This way, exceptions can be
		// handled by the Monitoring Decorator.
		final Map<String, Object> payload = Maps.newHashMap();

		final ConsumedDestinationModel destinationModel = getConsumedDestinationOrLogIfNotFound(itemModel,
				integrationObjectCode, destinationId);

		try
		{
			final RestOperations restOperations = getIntegrationRestTemplateFactory().create(destinationModel);
			return createObservable(restOperations, itemModel, integrationObjectCode, destinationModel, payload);
		}
		catch (final RuntimeException e)
		{
			logRequestIfMonitoringIsEnabled(itemModel, integrationObjectCode, destinationModel, payload);
			throw e;
		}
	}

	/**
	 * @deprecated use {@link #createObservable(DecoratorContext)} instead.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected Observable<ResponseEntity<Map>> createObservable(final RestOperations restOperations,
	                                                           final ItemModel itemModel,
	                                                           final String integrationObjectCode,
	                                                           final ConsumedDestinationModel destinationModel,
	                                                           final Map<String, Object> payload)
	{
		return Observable.just(restOperations).map(restTemplate -> {
			final HttpEntity<Map<String, Object>> entity =
					createHttpEntity(itemModel, integrationObjectCode, destinationModel, payload);
			return restTemplate.postForEntity(destinationModel.getUrl(), entity, Map.class);
		});
	}

	private void logRequestIfMonitoringIsEnabled(final ItemModel itemModel,
	                                             final String integrationObjectCode,
	                                             final ConsumedDestinationModel destinationModel,
	                                             final Map<String, Object> payload)
	{
		createHttpEntity(itemModel, integrationObjectCode, destinationModel, payload);
	}

	/**
	 * @deprecated use {@link #createHttpEntity(HttpHeaders, Map, DecoratorContext)} instead.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected HttpEntity<Map<String, Object>> createHttpEntity(final ItemModel itemModel,
	                                                           final String integrationObjectCode,
	                                                           final ConsumedDestinationModel destinationModel,
	                                                           final Map<String, Object> payload)
	{
		final HttpHeaders httpHeaders = new HttpHeaders();
		final SyncParameters params = SyncParameters.syncParametersBuilder()
		                                            .withItem(itemModel)
		                                            .withIntegrationObjectCode(integrationObjectCode)
		                                            .withDestination(destinationModel)
		                                            .build();
		final DecoratorContext context = contextFactory.createContext(params);

		final List<OutboundRequestDecorator> requestDecorators = addMonitoringDecorator(getOutboundRequestDecorators());
		final DecoratorExecution execution = new DefaultDecoratorExecution(requestDecorators.iterator());

		return execution.createHttpEntity(httpHeaders, payload, context);
	}

	/**
	 * Searches for a {@link ConsumedDestinationModel} with the provided destinationId.
	 *
	 * @param itemModel             the item model
	 * @param integrationObjectCode the integration object code
	 * @param destinationId         the destination id
	 * @return a matching {@link ConsumedDestinationModel}
	 * @throws ModelNotFoundException when a matching {@link ConsumedDestinationModel} is not found.
	 */
	private ConsumedDestinationModel getConsumedDestinationOrLogIfNotFound(final ItemModel itemModel,
	                                                                       final String integrationObjectCode,
	                                                                       final String destinationId)
	{
		try
		{
			final ConsumedDestinationModel example = new ConsumedDestinationModel();
			example.setId(destinationId);
			return getFlexibleSearchService().getModelByExample(example);
		}
		catch (final RuntimeException e)
		{
			LOG.warn("Failed to find ConsumedDestination with id '{}'", destinationId, e);
			final var nonExistingModel = new ConsumedDestinationNotFoundModel(destinationId);
			logRequestIfMonitoringIsEnabled(itemModel, integrationObjectCode, nonExistingModel, Maps.newHashMap());
			throw new ModelNotFoundException("Provided destination was not found.");
		}
	}

	protected IntegrationRestTemplateFactory getIntegrationRestTemplateFactory()
	{
		return integrationRestTemplateFactory;
	}

	@Required
	public void setIntegrationRestTemplateFactory(final IntegrationRestTemplateFactory integrationRestTemplateFactory)
	{
		this.integrationRestTemplateFactory = integrationRestTemplateFactory;
	}

	protected List<OutboundRequestDecorator> getOutboundRequestDecorators()
	{
		return decorators;
	}

	public void setOutboundRequestDecorators(final List<OutboundRequestDecorator> decorators)
	{
		this.decorators = decorators != null
				? decorators
				: Collections.emptyList();
	}

	protected OutboundRequestDecorator getMonitoringDecorator()
	{
		return monitoringDecorator;
	}

	public void setMonitoringDecorator(final OutboundRequestDecorator monitoringDecorator)
	{
		this.monitoringDecorator = monitoringDecorator;
	}

	/**
	 * @deprecated used only in the other deprecated {@code protected} methods. Will be removed with them.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	/**
	 * @deprecated used only in the other deprecated {@code protected} methods. Will be removed with them.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}

	protected OutboundServicesConfiguration getOutboundServicesConfiguration()
	{
		return outboundServicesConfiguration;
	}

	@Required
	public void setOutboundServicesConfiguration(final OutboundServicesConfiguration outboundServicesConfiguration)
	{
		this.outboundServicesConfiguration = outboundServicesConfiguration;
	}

	/**
	 * @deprecated used only in the other deprecated {@code protected} methods. Will be removed with them.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @deprecated used only in the other deprecated {@code protected} methods. Will be removed with them.
	 */
	@Deprecated(since = "2011.0", forRemoval = true)
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	@Required
	public void setContextFactory(final DecoratorContextFactory factory)
	{
		contextFactory = factory;
	}
}
