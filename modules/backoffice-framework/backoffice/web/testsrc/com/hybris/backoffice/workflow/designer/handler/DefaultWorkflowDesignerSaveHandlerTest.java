/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static com.hybris.backoffice.workflow.designer.handler.DefaultWorkflowDesignerSaveHandler.EXISTING_DECISIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willThrow;

import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.persistence.WorkflowDesignerPersistenceService;
import com.hybris.backoffice.workflow.designer.persistence.WorkflowDesignerSavingException;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowDesignerSaveHandlerTest
{

	@Mock
	private NotificationService notificationService;
	@Mock
	private WorkflowDesignerPersistenceService workflowTemplatePersistenceService;
	@Mock
	private WorkflowModelFinder nodeFinder;

	@InjectMocks
	private DefaultWorkflowDesignerSaveHandler handler;

	@Test
	public void shouldDelegateToWorkflowTemplatePersistenceService()
	{
		// given
		final NetworkChartContext context = prepareNetworkChartContext();

		// when 
		final NetworkUpdates networkUpdates = handler.save(context);

		// then
		verify(workflowTemplatePersistenceService).persist(context);
		assertThat(networkUpdates).isEqualTo(NetworkUpdates.EMPTY);
	}

	@Test
	public void shouldDisplayNotificationOnWorkflowDesignerSavingException()
	{
		// given
		final NetworkChartContext context = prepareNetworkChartContext();
		final WorkflowTemplateModel workflowTemplateModel = mock(WorkflowTemplateModel.class);
		given(nodeFinder.findWorkflowTemplate(context)).willReturn(workflowTemplateModel);
		given(workflowTemplateModel.getCode()).willReturn("myTemplate");
		doThrow(WorkflowDesignerSavingException.class).when(workflowTemplatePersistenceService).persist(context);

		// when
		final NetworkUpdates networkUpdates = handler.save(context);

		// then
		verify(notificationService).notifyUser(DefaultWorkflowDesignerSaveHandler.NOTIFICATION_AREA_SOURCE,
				DefaultWorkflowDesignerSaveHandler.EVENT_TYPE_CANNOT_SAVE, NotificationEvent.Level.FAILURE);
		assertThat(networkUpdates).isEqualTo(NetworkUpdates.EMPTY);
	}

	@Test
	public void shouldAddExistingDecisionsToModel()
	{
		// given
		final NetworkChartContext context = prepareNetworkChartContext();
		willThrow(new WorkflowDesignerSavingException("message", new Exception())).given(workflowTemplatePersistenceService)
				.persist(context);
		final Set<WorkflowDecisionTemplateModel> decisions = Set.of(mock(WorkflowDecisionTemplateModel.class));
		given(nodeFinder.findWorkflowDecisionsFromWorkflowTemplateModel(context)).willReturn(decisions);

		final WorkflowTemplateModel workflowTemplateModel = mock(WorkflowTemplateModel.class);
		given(nodeFinder.findWorkflowTemplate(context)).willReturn(workflowTemplateModel);
		given(workflowTemplateModel.getCode()).willReturn("myTemplate");

		// when
		final NetworkUpdates networkUpdates = handler.save(context);

		// then
		assertThat(networkUpdates).isEqualTo(NetworkUpdates.EMPTY);
		verify(context.getWim().getModel()).setValue(EXISTING_DECISIONS, decisions);
		verify(context.getWim().getModel()).remove(EXISTING_DECISIONS);
	}

	private NetworkChartContext prepareNetworkChartContext()
	{
		final WidgetInstanceManager widgetInstanceManager = mock(WidgetInstanceManager.class);
		given(widgetInstanceManager.getModel()).willReturn(mock(WidgetModel.class));

		final NetworkChartContext context = mock(NetworkChartContext.class);
		given(context.getWim()).willReturn(widgetInstanceManager);
		return context;
	}
}
