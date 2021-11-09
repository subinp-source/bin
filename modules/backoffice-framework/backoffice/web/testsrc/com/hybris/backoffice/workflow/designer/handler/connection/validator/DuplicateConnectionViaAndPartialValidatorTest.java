/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;


import static com.hybris.backoffice.workflow.designer.handler.connection.validator.DuplicateConnectionViaAndPartialValidator.EVENT_TYPE_MULTIPLE_AND_NODES_POINTING_TO_SAME_TARGET;
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
public class DuplicateConnectionViaAndPartialValidatorTest
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
	DuplicateConnectionViaAndPartialValidator validator;

	@Test
	public void shouldComplainAboutExistingAndConnectionBetweenNodes()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);
		given(mockedNodeTypeService.getNodeName(targetNode)).willReturn("targetNode");

		final Node existingAndNode = mock(Node.class);
		given(mockedNodeTypeService.isAnd(existingAndNode)).willReturn(true);

		final Edge existingAndConnection = mock(Edge.class);
		given(existingAndConnection.getFromNode()).willReturn(existingAndNode);

		given(mockedNodeTypeService.isAnd(sourceNode)).willReturn(true);
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, targetNode)).willReturn(Set.of(existingAndConnection));

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations())
				.containsOnly(Violation.create(EVENT_TYPE_MULTIPLE_AND_NODES_POINTING_TO_SAME_TARGET, "targetNode"));
	}

	@Test
	public void shouldNotComplainWhenEdgeIsComingFromNonAndNode()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);

		given(mockedNodeTypeService.isAnd(sourceNode)).willReturn(false);

		// when
		final WorkflowConnectionValidationResult result = validator
				.validate(ValidationContext.ofContextAndEdge(networkChartContext, edge));

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldNotComplainWhenEdgeIsNotYetCreatedToTargetNode()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);

		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);

		given(mockedNodeTypeService.isAnd(sourceNode)).willReturn(true);
		given(mockedConnectionFinder.findEdgesToNode(networkChartContext, targetNode)).willReturn(Collections.emptySet());

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
