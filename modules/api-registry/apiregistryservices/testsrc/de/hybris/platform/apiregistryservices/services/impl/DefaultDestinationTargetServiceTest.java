/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.services.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.apiregistryservices.enums.DestinationChannel;
import de.hybris.platform.apiregistryservices.exceptions.ApiRegistrationException;
import de.hybris.platform.apiregistryservices.exceptions.DeleteDestinationTargetNotPossibleException;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetCloningStrategy;
import de.hybris.platform.apiregistryservices.strategies.DestinationTargetRegistrationStrategy;
import de.hybris.platform.impex.jalo.ImpExException;


@UnitTest
public class DefaultDestinationTargetServiceTest
{
	@InjectMocks
	private DefaultDestinationTargetService destinationTargetService = new DefaultDestinationTargetService();

	@Mock
	private Map<DestinationChannel, DestinationTargetCloningStrategy> destinationTargetCloningStrategyMap;
	@Mock
	private Map<DestinationChannel, DestinationTargetRegistrationStrategy> destinationTargetRegistrationStrategyMap;

	private DestinationChannel aDestinationChannel = DestinationChannel.DEFAULT;

	@Mock
	private DestinationTargetCloningStrategy destinationTargetCloningStrategy;

	@Mock
	private DestinationTargetRegistrationStrategy destinationTargetRegistrationStrategy;

	@Mock
	private DestinationTargetModel templateDestinationTarget;

	@Mock
	private DestinationTargetModel nonTemplateDestinationTarget;

	@Mock
	private DestinationTargetModel nonTemplateWoChannelDestinationTarget;

	@Mock
	private DestinationTargetModel templateWoChannelDestinationTarget;

	@Before
	public void setup() throws ImpExException, ApiRegistrationException
	{
		MockitoAnnotations.initMocks(this);

		doReturn(true).when(templateDestinationTarget).getTemplate();
		doReturn(false).when(nonTemplateDestinationTarget).getTemplate();
		doReturn(true).when(templateWoChannelDestinationTarget).getTemplate();
		doReturn(false).when(nonTemplateWoChannelDestinationTarget).getTemplate();

		doReturn(aDestinationChannel).when(templateDestinationTarget).getDestinationChannel();
		doReturn(aDestinationChannel).when(nonTemplateDestinationTarget).getDestinationChannel();
		doReturn(null).when(nonTemplateWoChannelDestinationTarget).getDestinationChannel();
		doReturn(null).when(templateWoChannelDestinationTarget).getDestinationChannel();

		doReturn(destinationTargetCloningStrategy).when(destinationTargetCloningStrategyMap).get(aDestinationChannel);
		doReturn(destinationTargetRegistrationStrategy).when(destinationTargetRegistrationStrategyMap).get(aDestinationChannel);
		doReturn(true).when(destinationTargetCloningStrategyMap).containsKey(aDestinationChannel);
		doReturn(true).when(destinationTargetRegistrationStrategyMap).containsKey(aDestinationChannel);
	}

	@Test
	public void testDeregisterAndDeleteTemplateDestinationTarget() throws ApiRegistrationException, DeleteDestinationTargetNotPossibleException
	{
		destinationTargetService.deregisterAndDeleteDestinationTarget(templateDestinationTarget);

		verify(destinationTargetRegistrationStrategy, times(0)).syncDestinationTargetWithRemoteSystem(templateDestinationTarget);
		verify(destinationTargetRegistrationStrategy, times(0)).deregisterDestinationTarget(templateDestinationTarget);
		verify(destinationTargetCloningStrategy, times(1)).deleteDestinationTarget(templateDestinationTarget);
	}

	@Test
	public void testDeregisterAndDeleteNonTemplateDestinationTarget() throws ApiRegistrationException,
			DeleteDestinationTargetNotPossibleException
	{
		destinationTargetService.deregisterAndDeleteDestinationTarget(nonTemplateDestinationTarget);
		verify(destinationTargetRegistrationStrategy, times(1)).deregisterDestinationTarget(nonTemplateDestinationTarget);
		verify(destinationTargetCloningStrategy, times(1)).deleteDestinationTarget(nonTemplateDestinationTarget);
	}

	@Test
	public void testDeregisterAndDeleteDestinationTargetWoChannel()
			throws ApiRegistrationException, DeleteDestinationTargetNotPossibleException
	{
		destinationTargetService.deregisterAndDeleteDestinationTarget(nonTemplateWoChannelDestinationTarget);
		verify(destinationTargetRegistrationStrategy, times(1)).deregisterDestinationTarget(nonTemplateWoChannelDestinationTarget);
		verify(destinationTargetCloningStrategy, times(1)).deleteDestinationTarget(nonTemplateWoChannelDestinationTarget);
	}

	@Test
	public void testDeregisterAndDeleteTemplateDestinationTargetWoChannel()
			throws ApiRegistrationException, DeleteDestinationTargetNotPossibleException
	{
		destinationTargetService.deregisterAndDeleteDestinationTarget(templateWoChannelDestinationTarget);
		verify(destinationTargetRegistrationStrategy, times(0)).deregisterDestinationTarget(templateWoChannelDestinationTarget);
		verify(destinationTargetCloningStrategy, times(1)).deleteDestinationTarget(templateWoChannelDestinationTarget);
	}
}
