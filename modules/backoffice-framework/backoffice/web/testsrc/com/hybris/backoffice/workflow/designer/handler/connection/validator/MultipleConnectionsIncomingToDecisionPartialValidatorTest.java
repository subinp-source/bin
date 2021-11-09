/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import static com.hybris.backoffice.workflow.designer.handler.connection.validator.MultipleConnectionsIncomingToDecisionPartialValidator.EVENT_TYPE_DECISION_CAN_HAVE_ONLY_ONE_INCOMING_CONNECTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
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
public class MultipleConnectionsIncomingToDecisionPartialValidatorTest
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
	MultipleConnectionsIncomingToDecisionPartialValidator validator;

	@Test
	public void shouldComplainWhenTargetDecisionNodeHasAlreadyIncomingConnections()
	{
		// given
		final Node targetDecisionNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetDecisionNode);
		given(mockedNodeTypeService.isDecision(targetDecisionNode)).willReturn(true);
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, targetDecisionNode)).willReturn(Set.of(mock(Edge.class)));
		given(mockedNodeTypeService.getNodeName(targetDecisionNode)).willReturn("targetNode");

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult
				.ofViolations(Violation.create(EVENT_TYPE_DECISION_CAN_HAVE_ONLY_ONE_INCOMING_CONNECTION, "targetNode")));
	}

	@Test
	public void shouldNotComplainWhenTargetNodeIsNotDecision()
	{
		// given
		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);
		given(mockedNodeTypeService.isDecision(targetNode)).willReturn(false);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldNotComplainAboutTargetNodeWithoutIncomingConnections()
	{
		// given
		final Node targetDecisionNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetDecisionNode);
		given(mockedNodeTypeService.isDecision(targetDecisionNode)).willReturn(true);
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, targetDecisionNode)).willReturn(Collections.emptySet());

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
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
