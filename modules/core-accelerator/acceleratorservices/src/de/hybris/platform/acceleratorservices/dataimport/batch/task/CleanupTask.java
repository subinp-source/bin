/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.HeaderTask;


/**
 * Cleanup for the impex import process. Deletes the transformed file and moves the processed file to the archive
 * directory.
 */
public class CleanupTask implements HeaderTask
{
	private CleanupHelper cleanupHelper;

	@Override
	public BatchHeader execute(final BatchHeader header)
	{
		cleanupHelper.cleanup(header, false);
		return null;
	}

	/**
	 * @param cleanupHelper
	 *           the cleanupHelper to set
	 */
	public void setCleanupHelper(final CleanupHelper cleanupHelper)
	{
		this.cleanupHelper = cleanupHelper;
	}

	/**
	 * @return the cleanupHelper
	 */
	protected CleanupHelper getCleanupHelper()
	{
		return cleanupHelper;
	}
}
