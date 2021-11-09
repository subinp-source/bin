/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.inboundservices.service.InboundChannelConfigurationService;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link InboundChannelConfigurationService}
 */
public class DefaultInboundChannelConfigurationService implements InboundChannelConfigurationService
{
	private static final Logger LOG = Log.getLogger(DefaultInboundChannelConfigurationService.class);

	private static final String INTEGRATION_OBJECT = "integrationObject";

	private FlexibleSearchService flexibleSearchService;

	protected static final String GET_ICC_BY_INTEGRATION_OBJECT = "select {" + InboundChannelConfigurationModel.PK + "} " +
			"from {" + InboundChannelConfigurationModel._TYPECODE + " as icc} WHERE {icc.integrationObject} = ?"+INTEGRATION_OBJECT;

	/**
	 * Constructor
	 * @param flexibleSearchService to search for the inbound channel configurations {@link InboundChannelConfigurationModel}.
	 */
	public DefaultInboundChannelConfigurationService(@NotNull final FlexibleSearchService flexibleSearchService)
	{
		Preconditions.checkArgument(flexibleSearchService != null, "Non-null flexibleSearchService must be provided");
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public Optional<InboundChannelConfigurationModel> findInboundChannelConfigurationByIntegrationObject(
			final IntegrationObjectModel integrationObject)
	{
		LOG.debug("Finding InboundChannelConfiguration by integration object {}", integrationObject);
		List<InboundChannelConfigurationModel> results = Collections.emptyList();

		if (integrationObject != null)
		{
			try
			{
				final Map<String, PK> paramsMap = new HashMap<>();
				paramsMap.put(INTEGRATION_OBJECT, integrationObject.getPk());

				final SearchResult<InboundChannelConfigurationModel> query = flexibleSearchService.search(
						GET_ICC_BY_INTEGRATION_OBJECT, paramsMap);
				results = query.getResult();
			}
			catch (final RuntimeException e)
			{
				LOG.error("Failed finding InboundChannelConfiguration by integration object '{}'", integrationObject.getCode(),
						  e);
			}
		}
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}
}
