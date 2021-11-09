/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundsync.config.impl;

import de.hybris.platform.outboundsync.config.IdentifierGenerator;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;

public class CronJobIdentifierGenerator implements IdentifierGenerator<OutboundChannelConfigurationModel>
{
	@Override
	public String generate(final OutboundChannelConfigurationModel channel)
	{
		return channel.getCode() + "CronJob";
	}
}
