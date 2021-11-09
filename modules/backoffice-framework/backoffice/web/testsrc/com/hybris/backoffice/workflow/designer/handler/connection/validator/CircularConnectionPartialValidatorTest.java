/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import static com.hybris.backoffice.workflow.designer.handler.connection.validator.CircularConnectionPartialValidator.EVENT_TYPE_ALREADY_CONNECTED_VIA_AND;
import static com.hybris.backoffice.workflow.designer.handler.connection.validator.CircularConnectionPartialValidator.EVENT_TYPE_NODES_DIRECTLY_CONNECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.Ordered;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.ConnectionFinder;
import com.hybris.backoffice.workflow.designer.services.NodeTypeService;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class CircularConnectionPartialValidatorTest
{
	@Mock
	NodeTypeService mockedNodeTypeService;
	@Mock
	ConnectionFinder mockedConnectionFinder;

	@Mock
	NetworkChartContext networkChartContext;
	@Mock
	Edge edge;

	@InjectMocks
	CircularConnectionPartialValidator validator;

	@Test
	public void shouldValidateConnectionWithoutViolations()
	{
		// given
		final ValidationContext validationContext = ValidationContext.ofContextAndEdge(networkChartContext, edge);

		// when
		final WorkflowConnectionValidationResult result = validator.validate(validationContext);

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldDetectDecisionConnectedWithActionInCircularConnectionThroughAndAsSource()
	{
		// given
		// source (and) node and target (action) node
		final Node sourceNode = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(sourceNode)).willReturn("sourceNode");
		final Node targetNode = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");

		given(edge.getFromNode()).willReturn(sourceNode);
		given(edge.getToNode()).willReturn(targetNode);

		given(mockedNodeTypeService.isAnd(sourceNode)).willReturn(true);

		// the connection exists: node -> source(and) -> target(action)
		final Edge foundEdge = mock(Edge.class);
		final Node node = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(node)).willReturn("node");
		given(foundEdge.getFromNode()).willReturn(node);
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, sourceNode)).willReturn(Set.of(foundEdge));
		given(mockedConnectionFinder.areNodesConnected(networkChartContext, node, targetNode)).willReturn(true);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations()).containsOnly(
				Violation.create(EVENT_TYPE_NODES_DIRECTLY_CONNECTED, "sourceNode", "targetNode", "node", "targetNode"));
	}

	@Test
	public void shouldDetectDecisionConnectedWithActionInCircularConnectionThroughAndAsTarget()
	{
		// given
		// source (decision) node and target (and) node
		final Node sourceNode = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(sourceNode)).willReturn("sourceNode");
		final Node targetNode = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");

		given(edge.getFromNode()).willReturn(sourceNode);
		given(edge.getToNode()).willReturn(targetNode);

		given(mockedNodeTypeService.isAnd(targetNode)).willReturn(true);

		// the connection exists: source(decision) -> target(and) -> node
		final Edge foundEdge = mock(Edge.class);
		final Node node = mock(Node.class);
		given(mockedNodeTypeService.getNodeName(node)).willReturn("node");
		given(foundEdge.getToNode()).willReturn(node);
		given(mockedConnectionFinder.findEdgesFromNode(networkChartContext, targetNode)).willReturn(Set.of(foundEdge));
		given(mockedConnectionFinder.areNodesConnected(networkChartContext, sourceNode, node)).willReturn(true);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations()).containsOnly(
				Violation.create(EVENT_TYPE_NODES_DIRECTLY_CONNECTED, "sourceNode", "targetNode", "sourceNode", "node"));
	}

	@Test
	public void shouldDetectNodesConnectedViaAndConnector()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);
		given(mockedNodeTypeService.getNodeName(sourceNode)).willReturn("sourceNode");

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");

		final Node connectingNode = mock(Node.class);
		given(mockedNodeTypeService.isAnd(connectingNode)).willReturn(true);

		final Edge firstEdge = mock(Edge.class);
		final Edge secondEdge = mock(Edge.class);
		given(firstEdge.getToNode()).willReturn(connectingNode);
		given(secondEdge.getFromNode()).willReturn(connectingNode);

		given(mockedConnectionFinder.findEdgesFromNode(networkChartContext, sourceNode)).willReturn(Set.of(firstEdge));
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, targetNode)).willReturn(Set.of(secondEdge));

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations())
				.containsOnly(Violation.create(EVENT_TYPE_ALREADY_CONNECTED_VIA_AND, "sourceNode", "targetNode"));
	}

	@Test
	public void shouldHandleOrderChange()
	{
		// given
		final int customOrder = 123;

		// when
		validator.setOrder(customOrder);

		// then
		assertThat(validator).isInstanceOf(Ordered.class);
		assertThat(validator.getOrder()).isEqualTo(customOrder);
	}
}
