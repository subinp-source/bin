/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.setup.events;

import de.hybris.platform.commerceservices.setup.data.ImportData;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.servicelayer.event.SynchronousEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import java.util.List;


public class AbstractDataImportEvent extends AbstractEvent implements SynchronousEvent
{
	private final List<ImportData> importData;
	private final transient SystemSetupContext context;

	public AbstractDataImportEvent(final SystemSetupContext context, final List<ImportData> importData)
	{
		this.context = context;
		this.importData = importData;
	}

	public SystemSetupContext getContext()
	{
		return context;
	}

	public List<ImportData> getImportData()
	{
		return importData;
	}
}
