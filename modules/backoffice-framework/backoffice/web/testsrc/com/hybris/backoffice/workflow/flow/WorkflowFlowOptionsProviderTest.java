/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.cockpitng.components.visjs.network.data.Options;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowFlowOptionsProviderTest
{

	@Spy
	private final WorkflowFlowOptionsProvider provider = new WorkflowFlowOptionsProvider();

	private Options showFlowOptions;
	private Options workflowDesignerOptions;

	@Before
	public void setUp()
	{
		showFlowOptions = mock(Options.class);
		workflowDesignerOptions = mock(Options.class);
		doReturn(Optional.of(showFlowOptions)).when(provider).loadShowFlowOptions();
		doReturn(Optional.of(workflowDesignerOptions)).when(provider).loadWorkflowDesignerOptions();
	}

	@Test
	public void shouldShowFlowOptionsBeUsedWhenVisualisationIsNotSet()
	{
		// given
		final NetworkChartController controller = mock(NetworkChartController.class);
		final WorkflowTemplateModel workflowTemplate = mockInitModel(controller);
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		given(action.getVisualisationX()).willReturn(null);
		given(action.getVisualisationY()).willReturn(0);
		given(workflowTemplate.getActions()).willReturn(List.of(action));

		// when
		final Options options = provider.provide(controller);

		// then
		assertThat(options).isEqualTo(showFlowOptions);
	}

	@Test
	public void shouldWorkflowDesignerOptionsBeUsedWhenVisualisationIsSet()
	{
		// given
		final NetworkChartController controller = mock(NetworkChartController.class);
		final WorkflowTemplateModel workflowTemplate = mockInitModel(controller);
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		given(action.getVisualisationX()).willReturn(5443);
		given(action.getVisualisationY()).willReturn(343);
		given(workflowTemplate.getActions()).willReturn(List.of(action));

		// when
		final Options options = provider.provide(controller);

		// then
		assertThat(options).isEqualTo(workflowDesignerOptions);
	}

	private WorkflowTemplateModel mockInitModel(final NetworkChartController controller)
	{
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		final WorkflowTemplateModel workflowTemplate = mock(WorkflowTemplateModel.class);
		given(controller.getWidgetInstanceManager()).willReturn(wim);
		given(wim.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(NetworkChartController.MODEL_INIT_DATA, ItemModel.class)).willReturn(workflowTemplate);
		return workflowTemplate;
	}

}
