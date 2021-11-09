/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.NetworkPopulator;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class NetworkPopulationDetectingHandlerTest
{
	@Mock
	NetworkPopulator mockedDelegate;
	@Mock
	WorkflowDesignerDataManipulationListener mockedListener;

	@InjectMocks
	NetworkPopulationDetectingHandler handler;

	@Test
	public void shouldHandleDelegationToPopulateAndNotifyListener()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final Network expectedNetwork = mock(Network.class);
		given(mockedDelegate.populate(networkChartContext)).willReturn(expectedNetwork);

		// when
		final Network network = handler.populate(networkChartContext);

		// then
		assertThat(network).isEqualTo(expectedNetwork);
		then(mockedDelegate).should().populate(networkChartContext);
		then(mockedListener).should().onNew(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldHandleDelegationToPopulateWithoutNotifyingListener()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final Network emptyNetwork = Network.EMPTY;
		given(mockedDelegate.populate(networkChartContext)).willReturn(emptyNetwork);

		// when
		final Network network = handler.populate(networkChartContext);

		// then
		assertThat(network).isEqualTo(emptyNetwork);
		then(mockedDelegate).should().populate(networkChartContext);
		then(mockedListener).should(never()).onNew(any());
		then(mockedListener).should(never()).onChange(any());
	}

	@Test
	public void shouldHandleDelegationToUpdateAndNotifyListener()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates expectedNetworkUpdates = mock(NetworkUpdates.class);
		final Object updatedObject = mock(Object.class);
		given(mockedDelegate.update(updatedObject, networkChartContext)).willReturn(expectedNetworkUpdates);

		// when
		final NetworkUpdates network = handler.update(updatedObject, networkChartContext);

		// then
		assertThat(network).isEqualTo(expectedNetworkUpdates);
		then(mockedDelegate).should().update(updatedObject, networkChartContext);
		then(mockedListener).should().onChange(networkChartContext.getWim().getModel());
	}

	@Test
	public void shouldHandleDelegationToUpdateWithoutNotifyingListener()
	{
		// given
		final NetworkChartContext networkChartContext = prepareNetworkChartContext();
		final NetworkUpdates emptyNetworkUpdates = NetworkUpdates.EMPTY;
		final Object updatedObject = mock(Object.class);
		given(mockedDelegate.update(updatedObject, networkChartContext)).willReturn(emptyNetworkUpdates);

		// when
		final NetworkUpdates network = handler.update(updatedObject, networkChartContext);

		// then
		assertThat(network).isEqualTo(emptyNetworkUpdates);
		then(mockedDelegate).should().update(updatedObject, networkChartContext);
		then(mockedListener).should(never()).onNew(any());
		then(mockedListener).should(never()).onChange(any());
	}

	private NetworkChartContext prepareNetworkChartContext()
	{
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		given(widgetInstanceManager.getModel()).willReturn(mock(WidgetModel.class));
		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);
		given(networkChartContext.getWim()).willReturn(widgetInstanceManager);
		return networkChartContext;
	}
}
