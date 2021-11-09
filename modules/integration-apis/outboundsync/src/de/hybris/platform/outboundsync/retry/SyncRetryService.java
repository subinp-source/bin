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

/**
 * Service responsible for updating the persisted retries based on the results of the synchronization process
 */
public interface SyncRetryService
{
	/**
	 * Performs the necessary operations in the retry table to handle a synchronization error {@link OutboundItemDTOGroup} and determines
	 * if it's the last attempt based on the configuration
	 *
	 * @param itemGroup with the information required for the search
	 * @return true if it's the last attempt, false if it is not.
	 * @deprecated use {@link #handleSyncFailure(OutboundItemDTOGroup)}
	 */
	@Deprecated(since = "1905.08-CEP", forRemoval = true)
	boolean determineLastAttemptAndUpdateRetry(OutboundItemDTOGroup itemGroup);

	/**
	 * Handles an outbound synchronization failure for the specified DTO group.
	 * @param dtoGroup a group of related items, for which a single synchronization should have been done but failed.
	 * @return {@code true}, if it was last attempt and no more future synchrinizations should be attempted for the specified item group;
	 * {@code false}, if more synchronizations should be attempted.
	 */
	boolean handleSyncFailure(OutboundItemDTOGroup dtoGroup);

	/**
	 * Performs the necessary operations in the retry table to handle a successful synchronization
	 *
	 * @param itemGroup of type {@link OutboundItemDTOGroup} with information about the successful synchronization
	 */
	void handleSyncSuccess(OutboundItemDTOGroup itemGroup);
}
