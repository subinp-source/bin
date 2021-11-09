/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.setup.events;

import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.commerceservices.setup.events.SampleDataImportedEvent;
import de.hybris.platform.core.initialization.SystemSetupContext;

import java.util.List;


/**
 * Event indicating that sample data for an AddOn has been loaded.
 */
public class AddonSampleDataImportedEvent extends SampleDataImportedEvent
{
	public AddonSampleDataImportedEvent(final SystemSetupContext context, final List<ImportData> importData)
	{
		super(context, importData);
	}
}
