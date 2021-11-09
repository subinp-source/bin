/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.service;

import de.hybris.platform.inboundservices.model.InboundChannelConfigurationModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;

import java.util.Optional;

/**
 * Provides methods to interact with Inbound Channel Configuration.
 */
public interface InboundChannelConfigurationService
{
	/**
	 * Searches inbound channel configuration by an integration object
	 *
	 * @param integrationObject
	 * 			an integration object, for which the inbound channel configuration associated with it should be searched
	 *                          {@link de.hybris.platform.integrationservices.model.IntegrationObjectModel}
	 * @return an Optional containing of inbound channel configuration {@code Optional<InboundChannelConfigurationModel>}
	 * containing the inbound channel configuration corresponding to the provided integration object
	 * or an {@code Optional.empty()}, if the integration object is not associated with any inbound channel configuration.
	 */
	Optional<InboundChannelConfigurationModel> findInboundChannelConfigurationByIntegrationObject(
			IntegrationObjectModel integrationObject);
}