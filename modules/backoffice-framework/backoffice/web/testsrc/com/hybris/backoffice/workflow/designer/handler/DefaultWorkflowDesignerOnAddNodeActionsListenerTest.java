/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_INIT_DATA;
import static com.hybris.backoffice.widgets.networkchart.NetworkChartController.MODEL_NETWORK_NODES;
import static com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerOnAddNodeActionsListener.SOCKET_OUT_CREATE_ACTION;
import static com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerOnAddNodeActionsListener.SOCKET_OUT_CREATE_AND;
import static com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerOnAddNodeActionsListener.SOCKET_OUT_CREATE_DECISION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.zkoss.zk.ui.event.Event;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


public class DefaultWorkflowDesignerOnAddNodeActionsListenerTest
{
	private static final Event ANY_EVENT = null;
	final DefaultWorkflowDesignerOnAddNodeActionsListener onAddNodeActionsListener = new DefaultWorkflowDesignerOnAddNodeActionsListener();

	@Test
	public void shouldHandleAddAction()
	{
		// given
		final WorkflowTemplateModel workflowTemplateModel = mock(WorkflowTemplateModel.class);
		final NetworkChartContext networkChartContext = mockNetworkChartContext(workflowTemplateModel);
		final Map output = Map.of(WorkflowDesignerModelKey.KEY_PARENT_OBJECT, workflowTemplateModel,
				WorkflowDesignerModelKey.KEY_NODES, Set.of());

		// when
		onAddNodeActionsListener.onAddActionNodeButtonClick(ANY_EVENT, networkChartContext);

		// then
		then(networkChartContext.getWim()).should().sendOutput(SOCKET_OUT_CREATE_ACTION, output);
	}

	@Test
	public void shouldHandleAddDecision()
	{
		// given
		final WorkflowTemplateModel workflowTemplateModel = mock(WorkflowTemplateModel.class);
		final NetworkChartContext networkChartContext = mockNetworkChartContext(workflowTemplateModel);
		final Map output = Map.of(WorkflowDesignerModelKey.KEY_PARENT_OBJECT, workflowTemplateModel,
				WorkflowDesignerModelKey.KEY_NODES, Set.of());

		// when
		onAddNodeActionsListener.onAddDecisionNodeButtonClick(ANY_EVENT, networkChartContext);

		// then
		then(networkChartContext.getWim()).should().sendOutput(SOCKET_OUT_CREATE_DECISION, output);
	}

	@Test
	public void shouldHandleAndConnection()
	{
		// given
		final WorkflowTemplateModel workflowTemplateModel = mock(WorkflowTemplateModel.class);
		final NetworkChartContext networkChartContext = mockNetworkChartContext(workflowTemplateModel);

		// when
		onAddNodeActionsListener.onAddAndNodeButtonClick(ANY_EVENT, networkChartContext);

		// then
		then(networkChartContext.getWim()).should().sendOutput(SOCKET_OUT_CREATE_AND, workflowTemplateModel);
	}

	private NetworkChartContext mockNetworkChartContext(final WorkflowTemplateModel workflowTemplateModel)
	{
		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(networkChartContext.getWim()).willReturn(widgetInstanceManager);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(MODEL_INIT_DATA, WorkflowTemplateModel.class)).willReturn(workflowTemplateModel);
		given(widgetModel.getValue(MODEL_NETWORK_NODES, Set.class)).willReturn(Set.of());
		return networkChartContext;
	}

	@Test
	public void shouldHandleAddActionWithNullContext()
	{
		// given
		final NetworkChartContext nullContext = null;
		Exception caughtException = null;

		// when
		try
		{
			onAddNodeActionsListener.onAddActionNodeButtonClick(ANY_EVENT, nullContext);
		}
		catch (final Exception e)
		{
			caughtException = e;
		}

		// then
		assertThat(caughtException).isInstanceOf(NullPointerException.class)
				.withFailMessage("Network Chart Context cannot be null");
	}

	@Test
	public void shouldHandleAddDecisionWithNullContext()
	{
		// given
		final NetworkChartContext nullContext = null;
		Exception caughtException = null;

		// when
		try
		{
			onAddNodeActionsListener.onAddDecisionNodeButtonClick(ANY_EVENT, nullContext);
		}
		catch (final Exception e)
		{
			caughtException = e;
		}

		// then
		assertThat(caughtException).isInstanceOf(NullPointerException.class)
				.withFailMessage("Network Chart Context cannot be null");
	}

	@Test
	public void shouldHandleAndConnectionWithNullContext()
	{
		// given
		final NetworkChartContext nullContext = null;
		Exception caughtException = null;

		// when
		try
		{
			onAddNodeActionsListener.onAddAndNodeButtonClick(ANY_EVENT, nullContext);
		}
		catch (final Exception e)
		{
			caughtException = e;
		}

		// then
		assertThat(caughtException).isInstanceOf(NullPointerException.class)
				.withFailMessage("Network Chart Context cannot be null");
	}
}
