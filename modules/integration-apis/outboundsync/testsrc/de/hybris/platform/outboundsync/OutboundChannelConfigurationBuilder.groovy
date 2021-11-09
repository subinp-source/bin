/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync

import com.google.common.base.Preconditions
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.impex.jalo.ImpExException
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundservices.ConsumedDestinationBuilder
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class OutboundChannelConfigurationBuilder {
	private String code
	private String integrationObjectCode
	private ConsumedDestinationModel destination

	static OutboundChannelConfigurationBuilder outboundChannelConfigurationBuilder()
	{
		new OutboundChannelConfigurationBuilder()
	}

	OutboundChannelConfigurationBuilder withCode(String code)
	{
		this.code = code
		this
	}

	OutboundChannelConfigurationBuilder withIntegrationObjectCode(String integrationObjectCode)
	{
		this.integrationObjectCode = integrationObjectCode
		this
	}

	OutboundChannelConfigurationBuilder withConsumedDestination(ConsumedDestinationBuilder builder) {
		withConsumedDestination builder.build()
	}

	OutboundChannelConfigurationBuilder withConsumedDestination(ConsumedDestinationModel destination)
	{
		this.destination = destination
		this
	}

	OutboundChannelConfigurationModel build() throws ImpExException
	{
		Preconditions.checkArgument(this.code != null, "code cannot be null")
		Preconditions.checkArgument(this.integrationObjectCode != null, "integrationObjectCode cannot be null")
		Preconditions.checkArgument(this.destination != null, "destination cannot be null")
		channelConfiguration(code, integrationObjectCode, destination)
	}

	static OutboundChannelConfigurationModel channelConfiguration(final String code, final String integrationObjectCode, final ConsumedDestinationModel destination)
	{
		importImpEx(
				"INSERT_UPDATE OutboundChannelConfiguration	; code[unique = true] ; integrationObject(code) ; destination(id)",
				"											; $code               ; $integrationObjectCode  ; ${destination.getId()}"
		)
		getOutboundChannelConfigurationByCode(code)
	}

	static OutboundChannelConfigurationModel getOutboundChannelConfigurationByCode(final String code)
	{
		IntegrationTestUtil.findAny(OutboundChannelConfigurationModel, { it.code == code }).orElse(null)
	}
}
