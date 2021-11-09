/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static com.hybris.backoffice.workflow.designer.validation.DecisionValidator.DECISION_NEEDS_AT_LEAST_ONE_CONNECTION;
import static com.hybris.backoffice.workflow.designer.validation.Violation.error;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class DecisionValidatorTest extends AbstractValidatorTest
{

	@InjectMocks
	DecisionValidator decisionValidator;


	@Test
	public void shouldCheckForOrphanedDecisions()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);

		final Node connectedNode = mock(Node.class);
		given(mockedConnectionFinder.findEdgesToNode(context, connectedNode)).willReturn(Set.of(mock(Edge.class)));
		given(mockedConnectionFinder.findEdgesFromNode(context, connectedNode)).willReturn(Set.of(mock(Edge.class)));

		final Node orphanedNode = mock(Node.class);
		given(mockedConnectionFinder.findEdgesToNode(context, orphanedNode)).willReturn(Collections.emptySet());
		given(mockedConnectionFinder.findEdgesFromNode(context, orphanedNode)).willReturn(Collections.emptySet());
		given(mockedNodeTypeService.getNodeName(orphanedNode)).willReturn("orphaned node");

		given(mockedNetworkEntityFinder.findDecisionNodes(context)).willReturn(List.of(connectedNode, orphanedNode));

		// when
		final WorkflowDesignerValidationResult result = decisionValidator.validate(context);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations()).usingFieldByFieldElementComparator()
				.containsOnly(error(DECISION_NEEDS_AT_LEAST_ONE_CONNECTION, "orphaned node"));
	}
}
