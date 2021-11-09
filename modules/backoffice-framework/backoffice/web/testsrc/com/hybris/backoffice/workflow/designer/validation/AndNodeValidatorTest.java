/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.validation;

import static com.hybris.backoffice.workflow.designer.validation.AndNodeValidator.AND_NODE_NEEDS_AT_LEAST_TWO_INCOMING_AND_ONE_OUTGOING_CONNECTIONS;
import static com.hybris.backoffice.workflow.designer.validation.Violation.error;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.cockpitng.components.visjs.network.data.Node;


@RunWith(MockitoJUnitRunner.class)
public class AndNodeValidatorTest extends AbstractValidatorTest
{

	@InjectMocks
	AndNodeValidator andNodeValidator;

	@Test
	public void shouldCheckForRequiredIncomingAndOutgoingConnections()
	{
		// given
		final NetworkChartContext context = mock(NetworkChartContext.class);

		final Node orphanedAndNode = mock(Node.class, "orphaned and node");
		given(mockedConnectionFinder.findEdgesToNode(context, orphanedAndNode)).willReturn(Collections.emptySet());
		given(mockedConnectionFinder.findEdgesFromNode(context, orphanedAndNode)).willReturn(Collections.emptySet());

		given(mockedNetworkEntityFinder.findAndNodes(context)).willReturn(List.of(orphanedAndNode));

		// when
		final WorkflowDesignerValidationResult result = andNodeValidator.validate(context);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getViolations()).usingFieldByFieldElementComparator()
				.containsOnly(error(AND_NODE_NEEDS_AT_LEAST_TWO_INCOMING_AND_ONE_OUTGOING_CONNECTIONS));
	}
}
