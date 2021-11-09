/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import static com.hybris.cockpitng.testing.util.CockpitTestUtil.mockWidgetInstanceManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.doReturn;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class ConnectionFinderTest
{

	@Mock
	private NodeTypeService nodeTypeService;
	@Mock
	private NetworkEntityFinder networkEntityFinder;

	@InjectMocks
	@Spy
	private ConnectionFinder connectionFinder;

	private NetworkChartContext context;
	private WidgetInstanceManager wim;
	private WidgetModel model;

	@Before
	public void setUp() throws Exception
	{
		context = mock(NetworkChartContext.class);
		wim = mockWidgetInstanceManager();
		model = wim.getModel();

		given(context.getWim()).willReturn(wim);
	}

	@Test
	public void shouldGetEdgesFromNode()
	{
		// given
		final Node node = mock(Node.class);
		final Edge edgeFromNode = mock(Edge.class);
		final Edge edgeNotFromNode = mock(Edge.class);

		given(edgeFromNode.getFromNode()).willReturn(node);
		given(edgeNotFromNode.getFromNode()).willReturn(mock(Node.class));
		given(networkEntityFinder.findEdges(context)).willReturn(List.of(edgeFromNode, edgeNotFromNode));

		// when / then
		assertThat(connectionFinder.findEdgesFromNode(context, node)).containsOnly(edgeFromNode);
	}

	@Test
	public void shouldGetEdgesToNode()
	{
		// given
		final Node node = mock(Node.class);
		final Edge edgeToNode = mock(Edge.class);
		final Edge edgeNotToNode = mock(Edge.class);

		given(edgeToNode.getToNode()).willReturn(node);
		given(edgeNotToNode.getToNode()).willReturn(mock(Node.class));
		given(networkEntityFinder.findEdges(context)).willReturn(List.of(edgeToNode, edgeNotToNode));

		// when / then
		assertThat(connectionFinder.findEdgesToNode(context, node)).containsOnly(edgeToNode);
	}

	@Test
	public void shouldGetEdgesFromOrToNode()
	{
		// given
		final Node node = mock(Node.class);
		final Edge edgeFromNode = mock(Edge.class);
		final Edge edgeToNode = mock(Edge.class);

		doReturn(Set.of(edgeFromNode)).when(connectionFinder).findEdgesFromNode(context, node);
		doReturn(Set.of(edgeToNode)).when(connectionFinder).findEdgesToNode(context, node);

		// when / then
		assertThat(connectionFinder.findEdgesOfNode(context, node)).containsOnly(edgeFromNode, edgeToNode);
	}

	@Test
	public void shouldFindActionToDecisionEdges()
	{
		// given
		final Edge actionToDecision = mockEdgeWithNodes();
		given(nodeTypeService.isAction(actionToDecision.getFromNode())).willReturn(true);
		given(nodeTypeService.isDecision(actionToDecision.getToNode())).willReturn(true);

		final Edge decisionToAction = mockEdgeWithNodes();
		given(nodeTypeService.isDecision(decisionToAction.getFromNode())).willReturn(true);
		given(nodeTypeService.isAction(decisionToAction.getToNode())).willReturn(true);

		given(networkEntityFinder.findEdges(context)).willReturn(List.of(actionToDecision, decisionToAction));

		// when / then
		assertThat(connectionFinder.findActionToDecisionEdges(context)).containsOnly(actionToDecision);
	}

	@Test
	public void shouldFindDecisionToActionEdges()
	{
		// given
		final Edge actionToDecision = mockEdgeWithNodes();
		given(nodeTypeService.isAction(actionToDecision.getFromNode())).willReturn(true);
		given(nodeTypeService.isDecision(actionToDecision.getToNode())).willReturn(true);

		final Edge decisionToAction = mockEdgeWithNodes();
		given(nodeTypeService.isDecision(decisionToAction.getFromNode())).willReturn(true);
		given(nodeTypeService.isAction(decisionToAction.getToNode())).willReturn(true);

		given(networkEntityFinder.findEdges(context)).willReturn(List.of(actionToDecision, decisionToAction));

		// when / then
		assertThat(connectionFinder.findDecisionToActionEdges(context)).containsOnly(decisionToAction);
	}

	@Test
	public void shouldFindActionToDecisionEdge()
	{
		// given
		final WorkflowActionTemplateModel action1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowDecisionTemplateModel decision1 = mock(WorkflowDecisionTemplateModel.class);

		final Edge action1ToDecision1 = mockEdgeWithNodes();
		final Edge action1ToDecision2 = mockEdgeWithNodes();
		final Edge action2ToDecision2 = mockEdgeWithNodes();

		given(nodeTypeService.isSameAction(action1, action1ToDecision1.getFromNode())).willReturn(true);
		given(nodeTypeService.isSameDecision(decision1, action1ToDecision1.getToNode())).willReturn(true);
		given(nodeTypeService.isSameAction(action1, action1ToDecision2.getFromNode())).willReturn(true);

		given(networkEntityFinder.findEdges(context))
				.willReturn(List.of(action1ToDecision1, action1ToDecision2, action2ToDecision2));

		// when / then
		assertThat(connectionFinder.findActionToDecisionEdge(context, action1, decision1).get()).isEqualTo(action1ToDecision1);
	}

	@Test
	public void shouldFindDecisionToActionEdge()
	{
		// given
		final WorkflowDecisionTemplateModel decision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowActionTemplateModel action1 = mock(WorkflowActionTemplateModel.class);

		final Edge decision1ToAction1 = mockEdgeWithNodes();
		final Edge decision1ToAction2 = mockEdgeWithNodes();
		final Edge decision2ToAction2 = mockEdgeWithNodes();

		given(nodeTypeService.isSameDecision(decision1, decision1ToAction1.getFromNode())).willReturn(true);
		given(nodeTypeService.isSameAction(action1, decision1ToAction1.getToNode())).willReturn(true);
		given(nodeTypeService.isSameDecision(decision1, decision1ToAction2.getFromNode())).willReturn(true);

		given(networkEntityFinder.findEdges(context))
				.willReturn(List.of(decision1ToAction1, decision1ToAction2, decision2ToAction2));

		// when / then
		assertThat(connectionFinder.findDecisionToActionEdge(context, decision1, action1).get()).isEqualTo(decision1ToAction1);
	}

	@Test
	public void shouldReturnTrueIfDecisionIsConnectedToActionThroughAnd()
	{
		// given: decision -> and -> action
		final WorkflowDecisionTemplateModel decision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowActionTemplateModel action1 = mock(WorkflowActionTemplateModel.class);

		final Edge decision1ToAnd1 = mockEdgeWithNodes();
		final Edge and1ToAction1 = mockEdgeWithNodes();

		given(nodeTypeService.isSameDecision(decision1, decision1ToAnd1.getFromNode())).willReturn(true);
		given(nodeTypeService.isAnd(decision1ToAnd1.getToNode())).willReturn(true);
		final Node and1 = decision1ToAnd1.getToNode();
		doReturn(Set.of(and1ToAction1)).when(connectionFinder).findEdgesFromNode(context, and1);
		given(nodeTypeService.isSameAction(action1, and1ToAction1.getToNode())).willReturn(true);

		given(networkEntityFinder.findEdges(context)).willReturn(List.of(decision1ToAnd1, and1ToAction1));

		// when / then
		assertThat(connectionFinder.isDecisionConnectedToActionThroughAnd(context, decision1, action1)).isTrue();
	}

	@Test
	public void shouldReturnFalseIfDecisionIsConnectedToActionDirectly()
	{
		// given: decision -> action
		final WorkflowDecisionTemplateModel decision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowActionTemplateModel action1 = mock(WorkflowActionTemplateModel.class);

		final Edge decision1ToAction1 = mockEdgeWithNodes();

		given(nodeTypeService.isSameDecision(decision1, decision1ToAction1.getFromNode())).willReturn(true);
		given(networkEntityFinder.findEdges(context)).willReturn(List.of(decision1ToAction1));

		// when / then
		assertThat(connectionFinder.isDecisionConnectedToActionThroughAnd(context, decision1, action1)).isFalse();
	}

	@Test
	public void shouldReturnFalseIfDecisionIsConnectedToAndButToDifferentAction()
	{
		// given: decision1 -> and1 -> action2
		final WorkflowDecisionTemplateModel decision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowActionTemplateModel action2 = mock(WorkflowActionTemplateModel.class);

		final Edge decision1ToAnd1 = mockEdgeWithNodes();
		final Edge and1ToAction2 = mockEdgeWithNodes();

		given(nodeTypeService.isSameDecision(decision1, decision1ToAnd1.getFromNode())).willReturn(true);
		given(nodeTypeService.isAnd(decision1ToAnd1.getToNode())).willReturn(true);
		final Node and1 = decision1ToAnd1.getToNode();
		doReturn(Set.of(and1ToAction2)).when(connectionFinder).findEdgesFromNode(context, and1);

		given(networkEntityFinder.findEdges(context)).willReturn(List.of(decision1ToAnd1, and1ToAction2));

		// when / then
		assertThat(connectionFinder.isDecisionConnectedToActionThroughAnd(context, decision1, action2)).isFalse();
	}

	private Edge mockEdgeWithNodes()
	{
		final Edge edge = mock(Edge.class);
		final Node from = mock(Node.class);
		final Node to = mock(Node.class);
		given(edge.getFromNode()).willReturn(from);
		given(edge.getToNode()).willReturn(to);
		return edge;
	}
}
