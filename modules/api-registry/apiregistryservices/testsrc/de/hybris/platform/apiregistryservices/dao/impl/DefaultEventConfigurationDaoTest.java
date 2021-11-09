/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dao.EventConfigurationDao;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultEventConfigurationDaoTest extends ServicelayerTransactionalTest
{
	private static final String KYMA_DEFAULT = "kymaDefault";

	@Resource
	private EventConfigurationDao eventConfigurationDao;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/eventConfigurations.impex", "UTF-8");
	}

	@Test
	public void findActiveEventConfigsByClassWith2Channels()
	{
		final List<EventConfigurationModel> eventConfigsByClass = eventConfigurationDao
				.findActiveEventConfigsByClass("de.hybris.RegisterEvent");

		assertEquals("total number wrong", eventConfigsByClass.size(), 2);

		assertTrue(eventConfigsByClass.stream().anyMatch(configurationModel -> configurationModel.getDestinationTarget()
				.getDestinationChannel().equals(DestinationChannel.DEFAULT) && configurationModel.getVersion() == 1));
		assertTrue(eventConfigsByClass.stream().anyMatch(configurationModel -> configurationModel.getDestinationTarget()
				.getDestinationChannel().equals(DestinationChannel.DEFAULT) && configurationModel.getVersion() == 2));
	}

	@Test
	public void findActiveEventConfigsByClassWithOneChannel()
	{
		final List<EventConfigurationModel> eventConfigsByClassWith3Versions = eventConfigurationDao
				.findActiveEventConfigsByClass("de.hybris.AfterCronJobFinishedEvent");

		assertEquals("total number wrong", eventConfigsByClassWith3Versions.size(), 3);
		assertTrue(eventConfigsByClassWith3Versions.stream()
				.allMatch(c -> c.getDestinationTarget().getDestinationChannel().equals(DestinationChannel.DEFAULT)));

		final List<EventConfigurationModel> eventConfigsByClassWith2Versions = eventConfigurationDao
				.findActiveEventConfigsByClass("de.hybris.SubmitOrderEvent");

		assertEquals("total number wrong", eventConfigsByClassWith2Versions.size(), 2);
		assertTrue(
				eventConfigsByClassWith2Versions.stream().anyMatch(configurationModel -> configurationModel.getDestinationTarget()
						.getDestinationChannel().equals(DestinationChannel.DEFAULT) && configurationModel.getVersion() == 1));
		assertTrue(
				eventConfigsByClassWith2Versions.stream().anyMatch(configurationModel -> configurationModel.getDestinationTarget()
						.getDestinationChannel().equals(DestinationChannel.DEFAULT) && configurationModel.getVersion() == 3));

		final List<EventConfigurationModel> eventConfigsByClassWith1Version = eventConfigurationDao
				.findActiveEventConfigsByClass("de.hybris.ForgottenPwdEvent");
		assertEquals("total number wrong", eventConfigsByClassWith1Version.size(), 1);
		assertTrue(eventConfigsByClassWith1Version.get(0).getDestinationTarget().getDestinationChannel()
				.equals(DestinationChannel.DEFAULT));
		assertTrue(eventConfigsByClassWith1Version.get(0).getVersion() == 1);
	}

	@Test
	public void findActiveEventConfigsByInactiveClass()
	{
		final List<EventConfigurationModel> eventConfigsByClass = eventConfigurationDao
				.findActiveEventConfigsByClass("de.hybris.Inactive");

		assertEquals("total number wrong", eventConfigsByClass.size(), 0);
	}

	@Test
	public void findNotExistedEventConfigs()
	{
		final List<EventConfigurationModel> eventConfigsByClass = eventConfigurationDao.findActiveEventConfigsByClass("UNDEFINED");

		assertEquals("total number wrong", eventConfigsByClass.size(), 0);
	}

	@Test
	public void findEventConfigsByDestinationTargetId()
	{
		final List<EventConfigurationModel> eventConfigurations = eventConfigurationDao
				.findEventConfigsByDestinationTargetId(KYMA_DEFAULT);

		assertEquals(11, eventConfigurations.size());
	}
}
