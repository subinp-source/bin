/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * (“Confidential Information”). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.retry;

import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.outboundsync.jalo.OutboundChannelConfiguration;
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel;

/**
 *  Throws a {@throws RuntimeException} indicating that no {@link OutboundSyncRetryModel}
 *  that corresponds with the itemPk, and channelConfigurationCode was found.
 */
public class SyncRetryNotFoundException extends RuntimeException
{
	private static final String MSG = "The OutboundSyncRetryModel for itemPk [%s] and channel with code [%s] does not exist";

	private final Long itemPk;
	private final String channelConfigurationCode;

	/**
	 *
	 * @param itemPk - Long representation of the pk identifying the {@link IntegrationObjectItemModel} under concern
	 * @param channelConfigurationCode - {@link OutboundChannelConfiguration} code for identifying the channel under concern
	 */
	public SyncRetryNotFoundException(final Long itemPk, final String channelConfigurationCode)
	{
		super(String.format(MSG, itemPk, channelConfigurationCode));
		this.channelConfigurationCode = channelConfigurationCode;
		this.itemPk = itemPk;
	}

	public Long getItemPk()
	{
		return itemPk;
	}

	public String getChannelConfigurationCode()
	{
		return channelConfigurationCode;
	}
}