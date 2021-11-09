/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookbackoffice.services;

import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

/**
 * Handles the read and write requests from the extension's widgets
 */
public class WebhookConfigBackofficeService
{
	private static final Logger LOG = Log.getLogger(WebhookConfigBackofficeService.class);
	private static final String FIND_WEBHOOKCONFIG = " SELECT DISTINCT {" + ItemModel.PK + "}" +
			" FROM {" + WebhookConfigurationModel._TYPECODE + " as wbh}" +
			" WHERE {wbh:" + WebhookConfigurationModel.EVENTTYPE + "}=?eventClass AND " +
			"{wbh:" + WebhookConfigurationModel.INTEGRATIONOBJECT + "}=?IO AND " +
			"{wbh:" + WebhookConfigurationModel.DESTINATION + "}=?destination";
	private static final String FIND_CONSUMEDDESTINATION_BY_CHANNEL = " SELECT DISTINCT {" + ItemModel.PK + "}" +
			" FROM {" + ConsumedDestinationModel._TYPECODE + " as cd JOIN " + DestinationTargetModel._TYPECODE + " as dt" +
			" ON {cd:" + ConsumedDestinationModel.DESTINATIONTARGET + "}={dt:" + DestinationTargetModel.PK + "}" +
			" JOIN " + DestinationChannel._TYPECODE + " as dc ON {dt:" + DestinationTargetModel.DESTINATIONCHANNEL + "}={dc:pk}}" +
			" WHERE {dc:code}=?channel";
	private static final String FIND_ACTIVE_GROOVY_SCRIPTS = " SELECT DISTINCT {" + ItemModel.PK + "}" +
			" FROM {" + ScriptModel._TYPECODE + " as s JOIN " + ScriptType._TYPECODE + " as st ON {s:" + ScriptModel.SCRIPTTYPE + "}={st:pk}}" +
			" WHERE {st:code}='GROOVY' AND {s:active}=?scriptActive";

	private FlexibleSearchService flexibleSearchService;
	private ModelService modelService;

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * Retrieves all ConsumedDestinationModels filtered by DestinationChannel
	 *
	 * @param channel The DestinationChannel to be used as a filter
	 * @return A list of ConsumedDestinationModels
	 */
	public List<ConsumedDestinationModel> getConsumedDestinationByChannel(final DestinationChannel channel)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CONSUMEDDESTINATION_BY_CHANNEL);
		Map<String, Object> params = Map.of("channel", channel.getCode());
		query.addQueryParameters(params);
		final SearchResult<ConsumedDestinationModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	/**
	 * Retrieves a list of WebhookConfigurationModels searched with unique components
	 *
	 * @param integrationObject   An IO model
	 * @param consumedDestination A consumedDestination which is associated with a DestinationTarget that has {@link DestinationChannel}: 'WEBHOOKSERVICES'
	 * @param eventType           A class path
	 * @return A list of WebhookConfigurationModel
	 */
	public List<WebhookConfigurationModel> getWebhookConfiguration(final IntegrationObjectModel integrationObject,
	                                                               final ConsumedDestinationModel consumedDestination,
	                                                               final String eventType)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_WEBHOOKCONFIG);
		Map<String, Object> params = Map.of("eventClass", eventType, "IO", integrationObject.getPk(), "destination",
				consumedDestination.getPk());
		query.addQueryParameters(params);
		final SearchResult<WebhookConfigurationModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	/**
	 * Retrieves ScriptModels that are active and of type Groovy
	 *
	 * @return list of ScriptModels, or empty list
	 */
	public List<ScriptModel> getActiveGroovyScripts()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ACTIVE_GROOVY_SCRIPTS);
		query.addQueryParameter("scriptActive", Boolean.TRUE);
		final SearchResult<ScriptModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	/**
	 * Create, persist a WebhookConfigurationModels searched with unique components and return it.
	 *
	 * @param integrationObject   An IO model
	 * @param consumedDestination A consumedDestination which is associated with a DestinationTarget that has {@link DestinationChannel}: 'WEBHOOKSERVICES'
	 * @param eventType           A class path
	 * @param filterLocation      A location of filter
	 * @return WebhookConfigurationModel just created
	 */
	public WebhookConfigurationModel createAndPersistWebhookConfiguration(final IntegrationObjectModel integrationObject,
	                                                                      final ConsumedDestinationModel consumedDestination,
	                                                                      final String eventType, final String filterLocation)
	{
		final WebhookConfigurationModel webhookConfiguration = modelService.create(WebhookConfigurationModel.class);
		webhookConfiguration.setIntegrationObject(integrationObject);
		webhookConfiguration.setDestination(consumedDestination);
		webhookConfiguration.setEventType(eventType);
		webhookConfiguration.setFilterLocation(filterLocation);
		modelService.save(webhookConfiguration);
		LOG.info("New WebhookConfiguration has been created with IO: {}, ConsumedDestination: {} and EventType: {}.",
				integrationObject.getCode(), consumedDestination.getId(), eventType);
		return webhookConfiguration;
	}
}
