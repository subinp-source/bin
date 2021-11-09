/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync

import com.google.common.base.Preconditions
import de.hybris.platform.integrationservices.util.IntegrationTestUtil
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel
import de.hybris.platform.outboundsync.model.OutboundSyncStreamConfigurationModel

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx

class OutboundSyncStreamConfigurationBuilder {
	private String streamId
	private static final String OUTBOUND_SYNC_CONTAINER = "outboundSyncDataStreams"
	private String itemType
	private OutboundChannelConfigurationModel outboundChannelConfiguration
	private String whereClause = ''

	static OutboundSyncStreamConfigurationBuilder outboundSyncStreamConfigurationBuilder()
	{
		return new OutboundSyncStreamConfigurationBuilder()
	}

	OutboundSyncStreamConfigurationBuilder withId(final String streamId)
	{
		this.streamId = streamId
		this
	}

	OutboundSyncStreamConfigurationBuilder withItemType(final String itemType)
	{
		this.itemType = itemType
		this
	}

	OutboundSyncStreamConfigurationBuilder withOutboundChannelConfiguration(final OutboundChannelConfigurationModel outboundChannelConfiguration)
	{
		this.outboundChannelConfiguration = outboundChannelConfiguration
		this
	}

	OutboundSyncStreamConfigurationBuilder withWhereClause(final String whereClause)
	{
		this.whereClause = whereClause
		this
	}

	OutboundSyncStreamConfigurationModel build()
	{
		Preconditions.checkArgument(streamId != null, "streamId cannot be null")
		Preconditions.checkArgument(itemType != null, "itemType cannot be null")
		Preconditions.checkArgument(outboundChannelConfiguration != null, "outboundChannelConfiguration cannot be null")
		streamConfiguration(streamId, itemType, outboundChannelConfiguration, whereClause)
	}

	static OutboundSyncStreamConfigurationModel streamConfiguration(final String streamId, final String itemType, final OutboundChannelConfigurationModel outboundChannelConfiguration, final String whereClause)
	{
		importImpEx(
				"INSERT_UPDATE OutboundSyncStreamConfiguration  ; streamId[unique = true] ; container(id)            ; itemTypeForStream(code) ; outboundChannelConfiguration(code)        ; whereClause",
				"												; $streamId               ; $OUTBOUND_SYNC_CONTAINER ; $itemType               ; ${outboundChannelConfiguration.getCode()} ; $whereClause"
		)
		getOutboundSyncStreamConfigurationById(streamId)
	}

	static OutboundSyncStreamConfigurationModel getOutboundSyncStreamConfigurationById(final String streamId)
	{
		IntegrationTestUtil.findAny(OutboundSyncStreamConfigurationModel, { it.streamId == streamId }).orElse(null)
	}
}
