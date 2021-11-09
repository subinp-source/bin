/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundsync.job;

import de.hybris.deltadetection.ChangesCollector;

/**
 * A {@link ChangesCollector} that counts how many changes were collected/processed and exposes that count.
 */
public interface CountingChangesCollector extends ChangesCollector
{
	/**
	 * Reports how many changes were collected so far.
	 * @return number of changes collected or 0, if no changes collected.
	 */
	int getNumberOfChangesCollected();
}
