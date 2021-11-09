/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler.connection;

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level.WARNING;
import static com.hybris.backoffice.workflow.designer.handler.connection.DefaultWorkflowDesignerConnectionHandler.NOTIFICATION_AREA_SOURCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.ValidationContext;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.Violation;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.WorkflowConnectionValidationResult;
import com.hybris.backoffice.workflow.designer.handler.connection.validator.WorkflowConnectionValidator;
import com.hybris.cockpitng.components.visjs.network.data.Edge;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.util.notifications.NotificationService;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowDesignerConnectionHandlerTest
{
	@Mock
	WorkflowConnectionValidator mockedWorkflowConnectionValidator;
	@Mock
	NotificationService mockedNotificationService;

	@Mock
	NetworkChartContext context;
	@Mock
	Edge edge;

	@InjectMocks
	DefaultWorkflowDesignerConnectionHandler handler;

	@Test
	public void shouldHandleEdgeCreation()
	{
		// given
		final Node sourceNode = mock(Node.class);
		given(edge.getFromNode()).willReturn(sourceNode);
		final Node targetNode = mock(Node.class);
		given(edge.getToNode()).willReturn(targetNode);

		final NetworkUpdate expectedNetworkUpdate = new NetworkUpdate(Action.ADD,
				new Edge.Builder(edge, sourceNode, targetNode).build());

		given(mockedWorkflowConnectionValidator.validate(any())).willReturn(mock(WorkflowConnectionValidationResult.class));

		// when
		final NetworkUpdates networkUpdates = handler.addEdge(context, edge);

		// then
		assertThat(networkUpdates).isNotNull();
		assertThat(networkUpdates.getUpdates()).containsOnly(expectedNetworkUpdate);
		then(mockedWorkflowConnectionValidator).should().validate(ValidationContext.ofContextAndEdge(context, edge));
	}

	@Test
	public void shouldNotUpdateNetworkIfThereAreValidationErrors()
	{
		// given
		final WorkflowConnectionValidationResult validationResult = mock(WorkflowConnectionValidationResult.class);

		given(validationResult.isFailed()).willReturn(true);
		given(mockedWorkflowConnectionValidator.validate(any())).willReturn(validationResult);

		// when
		final NetworkUpdates networkUpdates = handler.addEdge(context, edge);

		// then
		assertThat(networkUpdates).isEqualTo(NetworkUpdates.EMPTY);
	}

	@Test
	public void shouldNotifyAboutViolationsDetected()
	{
		// given
		final WorkflowConnectionValidationResult validationResult = mock(WorkflowConnectionValidationResult.class);

		given(validationResult.isFailed()).willReturn(true);
		given(validationResult.getViolations())
				.willReturn(Set.of(Violation.create("violation1"), Violation.create("violation2", "param1", "param2")));

		given(mockedWorkflowConnectionValidator.validate(any())).willReturn(validationResult);

		// when
		handler.addEdge(context, edge);

		// then
		then(mockedNotificationService).should().notifyUser(NOTIFICATION_AREA_SOURCE, "violation1", WARNING);
		then(mockedNotificationService).should().notifyUser(NOTIFICATION_AREA_SOURCE, "violation2", WARNING, "param1", "param2");
		verifyNoMoreInteractions(mockedNotificationService);
	}
}
