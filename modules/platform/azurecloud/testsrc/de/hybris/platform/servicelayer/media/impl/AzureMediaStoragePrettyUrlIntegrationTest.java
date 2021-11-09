/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.servicelayer.media.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.azure.constants.AzurecloudConstants;
import de.hybris.platform.core.model.media.MediaModel;

@IntegrationTest
public class AzureMediaStoragePrettyUrlIntegrationTest extends AbstractMediaStoragePrettyUrlIntegrationTest
{
	@Override
	protected String getTestFolderQualifier()
	{
		return "azurestorageprettyurltesting";
	}

	@Override
	protected boolean isPrettyUrlPossible(final MediaModel mediaModel)
	{
		final String fileName = mediaModel.getRealFileName();
		return fileName != null && fileName.contains(".");
	}

	@Override
	protected boolean isTestEnabled()
	{
		return isStorageStrategyUnderTest(AzurecloudConstants.EXTENSIONNAME);
	}
}
