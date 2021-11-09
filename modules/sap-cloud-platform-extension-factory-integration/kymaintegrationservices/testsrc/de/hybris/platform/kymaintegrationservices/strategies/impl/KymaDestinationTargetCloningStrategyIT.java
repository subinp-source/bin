/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.kymaintegrationservices.strategies.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.dao.EventConfigurationDao;
import de.hybris.platform.apiregistryservices.enums.RegistrationStatus;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.events.EventConfigurationModel;
import de.hybris.platform.apiregistryservices.services.DestinationService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import javax.annotation.Resource;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;


@IntegrationTest
public class KymaDestinationTargetCloningStrategyIT extends ServicelayerTransactionalTest
{

	private static final String TEMPLATE_DESTINATION_TARGET_ID = "template_kyma";
	private static final String TEST_DESTINATION_TARGET_ID = "testDestinationTarget";
	private static final String TEST_DESTINATION_ID = "template_first_dest";
	private static final String TEST_EVENT_CONFIG_ID = "de.hybris.platform.commerceservices.event.RegisterEvent";

	@Resource
	KymaDestinationTargetCloningStrategy kymaDestinationTargetCloningStrategy;

	@Resource
	FlexibleSearchService flexibleSearchService;

	@Resource
	DestinationService<AbstractDestinationModel> destinationService;

	@Resource
	EventConfigurationDao eventConfigurationDao;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setUp() throws Exception
	{
		importCsv("/test/apiConfigurations-lifecycleTest.impex", "UTF-8");
	}

	@Test
	public void testCreateDestinationTarget()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		Assert.assertNotNull(testDestinationTarget);
		Assert.assertEquals(testDestinationTarget.getRegistrationStatus(), RegistrationStatus.STARTED);
		Assert.assertNotNull(flexibleSearchService.getModelByExample(exampleDestinationTarget));
	}

	@Test
	public void testCreateDestinationsWithNullDestinationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy.createDestinations(templateDestinationTarget, testDestinationTarget, null);

		final List<AbstractDestinationModel> clonedDestinations = destinationService
				.getDestinationsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, clonedDestinations.size());
		Assert.assertTrue(clonedDestinations.stream().anyMatch(destination -> destination.getId().equals(TEST_DESTINATION_ID)));
	}

	@Test
	public void testCreateDestinationsWithEmptyDestinationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy
				.createDestinations(templateDestinationTarget, testDestinationTarget, Collections.emptyList());

		final List<AbstractDestinationModel> clonedDestinations = destinationService
				.getDestinationsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, clonedDestinations.size());
		Assert.assertTrue(clonedDestinations.stream().anyMatch(destination -> destination.getId().equals(TEST_DESTINATION_ID)));
	}

	@Test
	public void testCreateDestinationsWithNotEmptyDestinationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		final List<AbstractDestinationModel> destinations = destinationService
				.getDestinationsByDestinationTargetId(TEMPLATE_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy.createDestinations(null, testDestinationTarget, destinations);

		final List<AbstractDestinationModel> clonedDestinations = destinationService
				.getDestinationsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, clonedDestinations.size());
		Assert.assertTrue(clonedDestinations.stream().anyMatch(destination -> destination.getId().equals(TEST_DESTINATION_ID)));
	}

	@Test
	public void testCreateEventConfigurationsWithNullEventConfigurationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy.createEventConfigurations(templateDestinationTarget, testDestinationTarget, null);

		final List<EventConfigurationModel> eventConfigurations = eventConfigurationDao
				.findEventConfigsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, eventConfigurations.size());
		Assert.assertTrue(
				eventConfigurations.stream().anyMatch(eventConfig -> eventConfig.getEventClass().equals(TEST_EVENT_CONFIG_ID)));
	}

	@Test
	public void testCreateEventConfigurationsWithEmptyEventConfigurationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy
				.createEventConfigurations(templateDestinationTarget, testDestinationTarget, Collections.emptyList());

		final List<EventConfigurationModel> eventConfigurations = eventConfigurationDao
				.findEventConfigsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, eventConfigurations.size());
		Assert.assertTrue(
				eventConfigurations.stream().anyMatch(eventConfig -> eventConfig.getEventClass().equals(TEST_EVENT_CONFIG_ID)));
	}

	@Test
	public void testCreateEventConfigurationsWithNotEmptyEventConfigurationList()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		final DestinationTargetModel testDestinationTarget = kymaDestinationTargetCloningStrategy
				.createDestinationTarget(templateDestinationTarget, TEST_DESTINATION_TARGET_ID);

		final List<EventConfigurationModel> eventConfigurations = eventConfigurationDao
				.findEventConfigsByDestinationTargetId(TEMPLATE_DESTINATION_TARGET_ID);

		kymaDestinationTargetCloningStrategy.createEventConfigurations(null, testDestinationTarget, eventConfigurations);

		final List<EventConfigurationModel> clonedEventConfigurations = eventConfigurationDao
				.findEventConfigsByDestinationTargetId(testDestinationTarget.getId());

		Assert.assertEquals(3, clonedEventConfigurations.size());
		Assert.assertTrue(
				clonedEventConfigurations.stream().anyMatch(eventConfig -> eventConfig.getEventClass().equals(TEST_EVENT_CONFIG_ID)));
	}

	@Test
	public void testDeleteDestinationTarget()
	{
		final DestinationTargetModel exampleDestinationTarget = new DestinationTargetModel();
		exampleDestinationTarget.setId(TEMPLATE_DESTINATION_TARGET_ID);

		final KymaDestinationTargetCloningStrategy kymaDestinationTargetCloningStrategySpy = spy(kymaDestinationTargetCloningStrategy);
		doNothing().when(kymaDestinationTargetCloningStrategySpy).removeGetInfoCronJob(any());

		final DestinationTargetModel templateDestinationTarget = flexibleSearchService.getModelByExample(exampleDestinationTarget);

		kymaDestinationTargetCloningStrategySpy.deleteDestinationTarget(templateDestinationTarget);

		Assert.assertTrue(destinationService.getDestinationsByDestinationTargetId(TEMPLATE_DESTINATION_TARGET_ID).isEmpty());
		Assert.assertTrue(eventConfigurationDao.findEventConfigsByDestinationTargetId(TEMPLATE_DESTINATION_TARGET_ID).isEmpty());

		expectedException.expect(ModelNotFoundException.class);

		flexibleSearchService.getModelByExample(exampleDestinationTarget);
	}
}
