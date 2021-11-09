/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowNetworkEntitiesFactory;
import com.hybris.backoffice.workflow.designer.pojo.Workflow;
import com.hybris.cockpitng.components.visjs.network.data.Network;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowWithCoordinatesNetworkFactoryTest
{
	@Mock
	private WorkflowNetworkEntitiesFactory entitiesFactory;
	@Mock
	private WorkflowVisualisationChecker visualisationChecker;
	@Spy
	@InjectMocks
	private WorkflowWithCoordinatesNetworkFactory networkFactory;

	@Before
	public void setUp()
	{
		doAnswer(answer -> answer.getArguments()[0]).when(networkFactory).decorate(any());
	}

	@Test
	public void shouldFactoryBeAbleToHandleInputWhenVisualisationIsSet()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(visualisationChecker.isVisualisationSet(context)).willReturn(true);

		// when
		final boolean output = networkFactory.canHandle(List.of(), context);

		// then
		assertThat(output).isTrue();
	}

	@Test
	public void shouldFactoryNotBeAbleToHandleInputWhenVisualisationIsNotSet()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(visualisationChecker.isVisualisationSet(context)).willReturn(false);

		// when
		final boolean output = networkFactory.canHandle(List.of(), context);

		// then
		assertThat(output).isFalse();
	}

	@Test
	public void shouldNetworkBeCreatedWhenWorkflowTemplateExistsAsInitData()
	{
		// given
		final WorkflowTemplateModel workflowTemplate = mock(WorkflowTemplateModel.class);
		final NetworkChartContext context = mock(NetworkChartContext.class);

		given(context.getInitData()).willReturn(Optional.of(workflowTemplate));

		final Network networkCreatedByEntitiesFactory = mock(Network.class);
		given(entitiesFactory.generateNetwork(any())).willAnswer(invocationOnMock -> {
			final Workflow workflow = ((Workflow) invocationOnMock.getArguments()[0]);
			return workflow.getModel() == workflowTemplate ? networkCreatedByEntitiesFactory : Network.EMPTY;
		});

		// when
		final Network output = networkFactory.create(List.of(), context);

		// then
		assertThat(output).isEqualTo(networkCreatedByEntitiesFactory);
	}

	@Test
	public void shouldNetworkBeEmptyWhenWorkflowTemplateDoesNotExistsAsInitData()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(context.getInitData()).willReturn(Optional.empty());

		// when
		final Network output = networkFactory.create(List.of(), context);

		// then
		assertThat(output).isEqualTo(Network.EMPTY);
	}

}
