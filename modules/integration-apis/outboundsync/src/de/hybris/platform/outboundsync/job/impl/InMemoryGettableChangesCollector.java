/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.outboundsync.job.impl;

import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.outboundsync.job.GettableChangesCollector;

import java.util.Collections;
import java.util.List;

/**
 * A {@link GettableChangesCollector} that stores the changes in memory.
 * @deprecated not used for outbound sync anymore. Replaced with {@link StreamingChangesCollector}
 * for better memory management an no risk of {@link OutOfMemoryError}
 */
@Deprecated(since = "1905.2003-CEP", forRemoval = true)
public class InMemoryGettableChangesCollector implements GettableChangesCollector
{
	private final InMemoryChangesCollector collector = new InMemoryChangesCollector();

	@Override
	public List<ItemChangeDTO> getChanges()
	{
		final List<ItemChangeDTO> changes = collector.getChanges();
		return changes != null ? changes : Collections.emptyList();
	}

	@Override
	public boolean collect(final ItemChangeDTO change)
	{
		return change != null && collector.collect(change);
	}

	@Override
	public void finish()
	{
		collector.finish();
	}
}
