/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.config.impl;

/**
 * Configuration parameters for the outbound sync.
 */
public interface OutboundSyncConfiguration
{
	int DEFAULT_OUTBOUNDSYNC_CRONJOBMODEL_SEARCH_SLEEP_MILLIS = 1000;

	/**
	 * Determines the max number of retry attempts allowed by the system after the initial synchronization attempt.
	 *
	 * @return number of times a failed item change synchronization will be reattempted.
	 */
	int getMaxOutboundSyncRetries();

	/**
	 * Determines maximum number of item changes accumulated during the grouping period before initiating the item change synchronization.
	 * Once this number of changes is achieved, the synchronization will start even, if the elapsed time for grouping is less than
	 * {@link #getItemGroupingTimeout()}.
	 *
	 * @return maximum number of changes grouped together for a single synchronization.
	 * @see #getItemGroupingTimeout()
	 */
	int getItemGroupSizeMax();

	/**
	 * Determines time through which changes to the same item will be treated as a single change. This helps with some optimization
	 * for sending changes to the destination. For example, if a single Order is created with 10 OrderLines, we will have 11 changes
	 * for the order and, if this value is set to 0, the Order will be sent 11 times to the destination. But, if this value is
	 * greater than zero, than outbound synchronization will expect more changes and won't send each change individually. Only after
	 * this time elapsed all the item changes will be grouped together and send as a single change.
	 *
	 * @return time in milliseconds to accumulate changes for an item before synchronizing them to the destination.
	 * @see #getItemGroupSizeMax()
	 */
	int getItemGroupingTimeout();

	/**
	 * Determines the sleep time in milliseconds to force OutboundSyncJobRegister to wait a small amount of time before searching
	 * for the OutboundSyncCronjobModel model before updating it to prevent a potential race condition.
	 *
	 * @return time in milliseconds to force OutboundSyncJobRegister to wait before searching for the OutboundSyncCronjobModel
	 */
	default int getOutboundSyncCronjobModelSearchSleep()
	{
		return DEFAULT_OUTBOUNDSYNC_CRONJOBMODEL_SEARCH_SLEEP_MILLIS;
	}
}
