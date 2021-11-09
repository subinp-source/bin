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
package de.hybris.platform.outboundsync.retry;

import de.hybris.platform.outboundsync.dto.OutboundItemDTOGroup;
import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel;

/**
 * Service responsible for searching for persisted retries based on the {@link OutboundItemDTOGroup}
 */
public interface RetrySearchService
{
	/**
	 * Finds {@link OutboundSyncRetryModel} with the parameters included in {@link OutboundItemDTOGroup}
	 * or throws {@throws SyncRetryNotFoundException} if no corresponding
	 * {@link OutboundSyncRetryModel} is found.
	 *
	 * @param outboundItemDTOGroup - {@link OutboundItemDTOGroup} that contains the changes made to the root Item
	 * @return - {@link OutboundSyncRetryModel} matching the root Item pk and outbound channel configuration included in the {@link OutboundItemDTOGroup}
	 */
	OutboundSyncRetryModel findRetry(OutboundItemDTOGroup outboundItemDTOGroup);
}
