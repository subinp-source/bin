/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Edges;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.data.Nodes;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowDesignerRemoveHandlerTest
{
	private static final NetworkChartContext ANY_CONTEXT = null;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@InjectMocks
	DefaultWorkflowDesignerRemoveHandler workflowDesignerRemoveHandler;

	@Test
	public void shouldConvertRemovedEdgesToNetworkUpdates()
	{
		// given
		final Edge edge1 = prepareEdge("edge1");
		final Edge edge2 = prepareEdge("edge2");

		// when
		final NetworkUpdates networkUpdates = workflowDesignerRemoveHandler.remove(new Edges(List.of(edge1, edge2)), ANY_CONTEXT);

		// then
		assertThat(networkUpdates).isNotNull();
		assertThat(networkUpdates.getUpdates()).containsOnly(new NetworkUpdate(Action.REMOVE, edge1),
				new NetworkUpdate(Action.REMOVE, edge2));
	}

	Edge prepareEdge(final String id)
	{
		return new Edge.Builder(new Node.Builder().build(), new Node.Builder().build()).withId(id).build();
	}

	@Test
	public void shouldConvertRemovedNodesToNetworkUpdates()
	{
		// given
		final Node node1 = prepareNode("node1");
		final Node node2 = prepareNode("node2");

		final NetworkChartContext context = prepareContext(Collections.emptyList());

		// when
		final NetworkUpdates networkUpdates = workflowDesignerRemoveHandler.remove(new Nodes(List.of(node1, node2)), context);

		// then
		assertThat(networkUpdates).isNotNull();
		assertThat(networkUpdates.getUpdates()).containsOnly(new NetworkUpdate(Action.REMOVE, node1),
				new NetworkUpdate(Action.REMOVE, node2));
	}

	@Test
	public void shouldRemoveNodesFromWidgetModel()
	{
		// given
		final Node node1 = prepareNode("node1");
		final Node node2 = prepareNode("node2");

		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		final NetworkChartContext context = prepareContext(Lists.newArrayList(action, decision));

		given(mockedNodeTypeService.isSameAction(action, node1)).willReturn(true);
		given(mockedNodeTypeService.isSameDecision(decision, node2)).willReturn(true);

		// when
		workflowDesignerRemoveHandler.remove(new Nodes(List.of(node1, node2)), context);

		// then
		assertThat(context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.isEmpty();
	}

	Node prepareNode(final String id)
	{
		return new Node.Builder().withId(id).build();
	}

	NetworkChartContext prepareContext(final List<Object> newWorkflowItems)
	{
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(widgetModel.getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.willReturn(newWorkflowItems);

		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		given(widgetInstanceManager.getModel()).willReturn(widgetModel);
		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(context.getWim()).willReturn(widgetInstanceManager);
		return context;
	}
}
