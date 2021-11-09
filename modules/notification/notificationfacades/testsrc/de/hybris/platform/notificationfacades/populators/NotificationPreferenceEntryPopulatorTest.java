/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.populators;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class NotificationPreferenceEntryPopulatorTest
{
	private NotificationPreferenceEntryPopulator populator;

	private NotificationPreferenceData target;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Configuration conf;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new NotificationPreferenceEntryPopulator();
		populator.setConfigurationService(configurationService);
	}

	@Test
	public void testPopulate_visible_true()
	{
		when(configurationService.getConfiguration()).thenReturn(conf);
		when(conf.getBoolean(Mockito.anyObject(), Mockito.anyBoolean())).thenReturn(true);

		target = new NotificationPreferenceData();
		populator.populate(NotificationChannel.EMAIL, target);
		Assert.assertEquals(NotificationChannel.EMAIL, target.getChannel());
		Assert.assertTrue(target.isVisible());
	}

	@Test
	public void testPopulate_visible_false()
	{
		when(configurationService.getConfiguration()).thenReturn(conf);
		when(conf.getBoolean(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(false);

		target = new NotificationPreferenceData();
		populator.populate(NotificationChannel.EMAIL, target);
		Assert.assertEquals(NotificationChannel.EMAIL, target.getChannel());
		Assert.assertFalse(target.isVisible());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateSourceNull()
	{
		populator.populate(null, target);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetNull()
	{
		populator.populate(NotificationChannel.EMAIL, null);

	}
}
