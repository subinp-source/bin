/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import static com.hybris.cockpitng.testing.util.CockpitTestUtil.mockWidgetInstanceManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class NetworkEntityFinderTest
{
	@Mock
	private NodeTypeService nodeTypeService;

	@InjectMocks
	@Spy
	private NetworkEntityFinder networkEntityFinder;

	@Mock
	private NetworkChartContext context;
	private WidgetInstanceManager wim;
	private WidgetModel model;

	@Before
	public void setUp() throws Exception
	{
		wim = mockWidgetInstanceManager();
		model = wim.getModel();

		given(context.getWim()).willReturn(wim);
	}

	@Test
	public void shouldFindNodes()
	{
		// given
		final List<Node> nodes = List.of();
		model.setValue(NetworkChartController.MODEL_NETWORK_NODES, nodes);

		// when / then
		assertThat(networkEntityFinder.findNodes(context)).isSameAs(nodes);
	}

	@Test
	public void shouldFindEdges()
	{
		// given
		final List<Edge> edges = List.of();
		model.setValue(NetworkChartController.MODEL_NETWORK_EDGES, edges);

		// when / then
		assertThat(networkEntityFinder.findEdges(context)).isSameAs(edges);
	}

	@Test
	public void shouldFindActionNodes()
	{
		// given
		final Node action = mock(Node.class);
		final Node decision = mock(Node.class);
		doReturn(List.of(action, decision)).when(networkEntityFinder).findNodes(context);
		given(nodeTypeService.isAction(action)).willReturn(true);

		// when / then
		assertThat(networkEntityFinder.findActionNodes(context)).containsOnly(action);
	}

	@Test
	public void shouldFindDecisionNodes()
	{
		// given
		final Node action = mock(Node.class);
		final Node decision = mock(Node.class);
		doReturn(List.of(action, decision)).when(networkEntityFinder).findNodes(context);
		given(nodeTypeService.isDecision(decision)).willReturn(true);

		// when / then
		assertThat(networkEntityFinder.findDecisionNodes(context)).containsOnly(decision);
	}

	@Test
	public void shouldFindAndNodes()
	{
		// given
		final Node action = mock(Node.class);
		final Node and = mock(Node.class);
		doReturn(List.of(action, and)).when(networkEntityFinder).findNodes(context);
		given(nodeTypeService.isAnd(and)).willReturn(true);

		// when / then
		assertThat(networkEntityFinder.findAndNodes(context)).containsOnly(and);
	}

	@Test
	public void shouldFindActionNode()
	{
		// given
		final Node a1 = mock(Node.class);
		final Node a2 = mock(Node.class);
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		doReturn(List.of(a1, a2)).when(networkEntityFinder).findActionNodes(context);
		given(nodeTypeService.isSameAction(action, a1)).willReturn(true);

		// when / then
		assertThat(networkEntityFinder.findActionNode(context, action).get()).isEqualTo(a1);
	}

	@Test
	public void shouldFindDecisionNode()
	{
		// given
		final Node d1 = mock(Node.class);
		final Node d2 = mock(Node.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		doReturn(List.of(d1, d2)).when(networkEntityFinder).findDecisionNodes(context);
		given(nodeTypeService.isSameDecision(decision, d1)).willReturn(true);

		// when / then
		assertThat(networkEntityFinder.findDecisionNode(context, decision).get()).isEqualTo(d1);
	}
}
