/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import static com.hybris.backoffice.workflow.designer.handler.connection.validator.NodesAlreadyConnectedPartialValidator.EVENT_TYPE_ALREADY_CONNECTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
public class NodesAlreadyConnectedPartialValidatorTest
{

	@Mock
	ConnectionFinder mockedConnectionFinder;
	@Mock
	NodeTypeService mockedNodeTypeService;

	@Mock
	NetworkChartContext networkChartContext;
	@Mock
	Edge edge;

	@InjectMocks
	NodesAlreadyConnectedPartialValidator validator;

	@Test
	public void shouldNotComplainWhenNodesAreNotYetConnected()
	{
		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldComplainWhenNodesAreAlreadyConnectedFromSourceToTarget()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);
		given(mockedNodeTypeService.getNodeName(sourceNode)).willReturn("sourceNode");

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");
		given(mockedConnectionFinder.areNodesConnected(networkChartContext, sourceNode, targetNode)).willReturn(true);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations())
				.containsOnly(Violation.create(EVENT_TYPE_ALREADY_CONNECTED, "sourceNode", "targetNode"));
	}

	@Test
	public void shouldComplainWhenNodesAreAlreadyConnectedFromTargetToSource()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);
		given(mockedNodeTypeService.getNodeName(sourceNode)).willReturn("sourceNode");

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");
		given(mockedConnectionFinder.areNodesConnected(networkChartContext, targetNode, sourceNode)).willReturn(true);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations())
				.containsOnly(Violation.create(EVENT_TYPE_ALREADY_CONNECTED, "sourceNode", "targetNode"));
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
