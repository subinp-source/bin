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

import de.hybris.platform.outboundsync.model.OutboundSyncRetryModel;

/**
 * Indicates a problem updating or deleting a {@link OutboundSyncRetryModel}
 */
public class RetryUpdateException extends RuntimeException
{
	private static final String MSG = "The OutboundSyncRetryModel for itemPk [%s] and channel with code [%s] could not be updated";

	private final OutboundSyncRetryModel retry;

	/**
	 * Constructor for the exception
	 *
	 * @param retry - {@link OutboundSyncRetryModel} that could not be updated
	 */
	public RetryUpdateException(final OutboundSyncRetryModel retry)
	{
		super(String.format(MSG, retry.getItemPk(), retry.getChannel().getCode()));
		this.retry = retry;
	}

	public OutboundSyncRetryModel getRetry()
	{
		return retry;
	}
}