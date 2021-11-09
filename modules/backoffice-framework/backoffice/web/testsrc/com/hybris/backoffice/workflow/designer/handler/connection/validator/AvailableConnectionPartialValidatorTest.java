/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection.validator;

import static com.hybris.backoffice.workflow.designer.handler.connection.validator.AvailableConnectionPartialValidator.EVENT_TYPE_WRONG_CONNECTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.Ordered;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowConnection;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;


public class AvailableConnectionPartialValidatorTest
{

	AvailableConnectionPartialValidator validator;

	@Before
	public void setUp()
	{
		validator = new AvailableConnectionPartialValidator();
	}

	@Test
	public void shouldNotComplainAboutConnectionsThatAreAllowed()
	{
		// given
		final Edge allowedConnection = mock(Edge.class);
		given(allowedConnection.getFromNode()).willReturn(mock(Node.class));
		given(allowedConnection.getToNode()).willReturn(mock(Node.class));

		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);

		final ValidationContext validationContext = ValidationContext.ofContextAndEdge(networkChartContext, allowedConnection);
		validator.setAvailableConnections(List.of(WorkflowConnection.of(allowedConnection)));

		// when
		final WorkflowConnectionValidationResult result = validator.validate(validationContext);

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult.EMPTY);
	}

	@Test
	public void shouldComplainAboutConnectionThatIsNotAllowed()
	{
		// given
		final Edge allowedConnection = mock(Edge.class);

		final Node sourceNode = mock(Node.class);
		final String actionGroup = WorkflowDesignerGroup.ACTION.getValue();
		given(sourceNode.getGroup()).willReturn(actionGroup);

		final Node targetNode = mock(Node.class);
		final String decision = WorkflowDesignerGroup.DECISION.getValue();
		given(targetNode.getGroup()).willReturn(decision);

		given(allowedConnection.getFromNode()).willReturn(sourceNode);
		given(allowedConnection.getToNode()).willReturn(targetNode);

		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);

		final ValidationContext validationContext = ValidationContext.ofContextAndEdge(networkChartContext, allowedConnection);
		validator.setAvailableConnections(Collections.emptyList());

		// when
		final WorkflowConnectionValidationResult result = validator.validate(validationContext);

		// then
		assertThat(result).isEqualTo(WorkflowConnectionValidationResult
				.ofViolations(Violation.create(EVENT_TYPE_WRONG_CONNECTION, actionGroup, decision)));
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
