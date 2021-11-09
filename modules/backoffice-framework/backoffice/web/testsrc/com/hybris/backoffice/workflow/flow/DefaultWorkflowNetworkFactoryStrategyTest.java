/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowNetworkFactoryStrategyTest
{

	private final DefaultWorkflowNetworkFactoryStrategy strategy = new DefaultWorkflowNetworkFactoryStrategy();

	@Test
	public void shouldFindFactoryIfAtLeastOneCanHandleInput()
	{
		// given
		final WorkflowFlowNetworkFactory factory1 = mock(WorkflowFlowNetworkFactory.class);
		final WorkflowFlowNetworkFactory factory2 = mock(WorkflowFlowNetworkFactory.class);
		given(factory1.canHandle(any(), any())).willReturn(false);
		given(factory2.canHandle(any(), any())).willReturn(true);
		strategy.setFactories(Arrays.asList(factory1, factory2));

		// when
		final Optional<WorkflowFlowNetworkFactory> factory = strategy.find(List.of(), mock(NetworkChartContext.class));

		// then
		assertThat(factory).isPresent();
		assertThat(factory).hasValue(factory2);
	}

	@Test
	public void shouldNotFindFactoryIfNoneOfFactoriesCanHandleInput()
	{
		// given
		final WorkflowFlowNetworkFactory factory1 = mock(WorkflowFlowNetworkFactory.class);
		final WorkflowFlowNetworkFactory factory2 = mock(WorkflowFlowNetworkFactory.class);
		given(factory1.canHandle(any(), any())).willReturn(false);
		given(factory2.canHandle(any(), any())).willReturn(false);
		strategy.setFactories(Arrays.asList(factory1, factory2));

		// when
		final Optional<WorkflowFlowNetworkFactory> factory = strategy.find(List.of(), mock(NetworkChartContext.class));

		// then
		assertThat(factory).isEmpty();
	}

}
