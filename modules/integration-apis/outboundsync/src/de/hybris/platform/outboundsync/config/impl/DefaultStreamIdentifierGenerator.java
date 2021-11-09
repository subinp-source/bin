/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.outboundsync.config.StreamIdentifierGenerator;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

public class DefaultStreamIdentifierGenerator implements StreamIdentifierGenerator
{
	private static final String SEPARATOR = "_";
	@Override
	public String generate(final OutboundChannelConfigurationModel channel, final IntegrationObjectItemModel item)
	{
		return  channel.getCode() + SEPARATOR + item.getType().getCode().replace(SEPARATOR, "") + SEPARATOR + "Stream";
	}
}
