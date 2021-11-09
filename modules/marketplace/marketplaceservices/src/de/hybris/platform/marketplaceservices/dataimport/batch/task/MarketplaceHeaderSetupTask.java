/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceservices.dataimport.batch.task;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;
import de.hybris.platform.acceleratorservices.dataimport.batch.task.HeaderSetupTask;
import de.hybris.platform.marketplaceservices.dataimport.batch.util.DataIntegrationUtils;

import java.io.File;

import org.springframework.util.Assert;


/**
 * Override original task to add vendor folder after storeBaseDirectory
 */
public class MarketplaceHeaderSetupTask extends HeaderSetupTask
{
	@Override
	public BatchHeader execute(final File file)
	{
		Assert.notNull(file, "The file can not be null.");
		final BatchHeader result = super.execute(file);
		final String vendorCode = DataIntegrationUtils.resolveVendorCode(file);
		result.setStoreBaseDirectory(storeBaseDirectory + File.separator + vendorCode);
		return result;
	}
}
