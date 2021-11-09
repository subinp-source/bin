/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
 package de.hybris.platform.notificationfacades.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceDataList;

import java.util.List;

import org.springframework.util.Assert;


/**
 * populator to populate notification preference data
 */
public class NotificationPreferencesPopulator
		implements Populator<List<NotificationPreferenceData>, NotificationPreferenceDataList>
{

	@Override
	public void populate(final List<NotificationPreferenceData> source, final NotificationPreferenceDataList target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setPreferences(source);

	}


}
