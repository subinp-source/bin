/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.flow.WorkflowFlowNetworkFactory;
import com.hybris.backoffice.workflow.flow.WorkflowNetworkFactoryStrategy;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowPopulatorTest
{
	@Mock
	WorkflowFlowNetworkFactory mockedWorkflowNetworkFactory;

	@Mock
	WorkflowNetworkFactoryStrategy mockedWorkflowNetworkFactoryStrategy;

	@Mock
	WorkflowItemExtractor mockedWorkflowItemExtractor;

	@InjectMocks
	WorkflowPopulator workflowPopulator;

	@Before
	public void setUp()
	{
		given(mockedWorkflowNetworkFactoryStrategy.find(any(), any())).willReturn(Optional.of(mockedWorkflowNetworkFactory));
	}

	@Test
	public void shouldPopulateNetwork()
	{
		// given
		final List<WorkflowItem> items = newArrayList(mock(WorkflowItem.class), mock(WorkflowItem.class));

		final NetworkChartContext mockedNetworkChartContext = mock(NetworkChartContext.class);
		given(mockedWorkflowItemExtractor.extract(mockedNetworkChartContext)).willReturn(items);

		final Network mockedNetwork = mock(Network.class);
		given(mockedWorkflowNetworkFactory.create(items, mockedNetworkChartContext)).willReturn(mockedNetwork);

		// when
		final Network result = workflowPopulator.populate(mockedNetworkChartContext);

		// then
		assertThat(result).isEqualTo(mockedNetwork);
	}

	@Test
	public void shouldNotHandleUpdates()
	{
		// given
		final Object anyUpdatedObject = mock(Object.class);
		final NetworkChartContext anyNetworkChartContext = mock(NetworkChartContext.class);

		// when
		final NetworkUpdates result = workflowPopulator.update(anyUpdatedObject, anyNetworkChartContext);

		// then
		assertThat(result).isSameAs(NetworkUpdates.EMPTY);
	}
}
