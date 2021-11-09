/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.events;

import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetupContext;

import java.util.List;


public class SampleDataImportedEvent extends AbstractDataImportEvent
{
	public SampleDataImportedEvent(final SystemSetupContext context, final List<ImportData> importData)
	{
		super(context, importData);
	}
}
