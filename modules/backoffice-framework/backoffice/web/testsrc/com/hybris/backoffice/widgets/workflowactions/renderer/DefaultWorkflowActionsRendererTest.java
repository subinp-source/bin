/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.workflowactions.renderer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.labels.HyperlinkFallbackLabelProvider;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;

import com.hybris.backoffice.renderer.utils.UIDateRendererProvider;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.ExtensibleWidget;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@ExtensibleWidget(level = ExtensibleWidget.ALL)
@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowActionsRendererTest extends AbstractCockpitngUnitTest<DefaultWorkflowActionsRenderer>
{

	public static final String COMPONENT_TITLE = "title";
	@InjectMocks
	private DefaultWorkflowActionsRenderer renderer;

	@Mock
	private TimeService timeService;
	@Mock
	private WorkflowActionModel workflowAction;
	@Mock
	private WorkflowModel workflow;
	@Mock
	private UIDateRendererProvider uiDateRendererProvider;
	@Mock
	private SessionService sessionService;
	@Mock
	private LabelService labelService;
	@Mock
	private PermissionFacade permissionFacade;
	@Mock
	private HyperlinkFallbackLabelProvider hyperlinkFallbackLabelProvider;

	private WidgetInstanceManager widgetInstanceManager;


	@Override
	protected Class<? extends DefaultWorkflowActionsRenderer> getWidgetType()
	{
		return DefaultWorkflowActionsRenderer.class;
	}

	@Before
	public void setUp()
	{
		widgetInstanceManager = CockpitTestUtil.mockWidgetInstanceManager();
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void shouldNotBreakOnNullActivatedDate()
	{
		when(workflowAction.getName()).thenReturn("workflowAction");
		when(workflowAction.getWorkflow()).thenReturn(workflow);
		when(workflow.getName()).thenReturn("workflow");
		when(workflow.getAttachments()).thenReturn(Collections.emptyList());

		renderer.render(new Listitem(), null, workflowAction, null, widgetInstanceManager);
	}

	@Test
	public void shouldCreateTitleButton()
	{
		//given
		given(permissionFacade.canReadInstance(workflowAction)).willReturn(Boolean.TRUE);
		given(hyperlinkFallbackLabelProvider.getFallback(COMPONENT_TITLE)).willReturn(COMPONENT_TITLE);

		//when
		final Component component = renderer.createTitleComponent(COMPONENT_TITLE, workflowAction, widgetInstanceManager);

		//then
		assertThat(component).isInstanceOf(Button.class);
		assertThat(((Button) component).getLabel()).isEqualTo(COMPONENT_TITLE);
	}

	@Test
	public void shouldCreateLabelIfUserHasNoPermissions()
	{
		//given
		given(permissionFacade.canReadInstance(workflowAction)).willReturn(Boolean.FALSE);

		//when
		final Component component = renderer.createTitleComponent(COMPONENT_TITLE, workflowAction, widgetInstanceManager);

		//then
		assertThat(component).isInstanceOf(Label.class);
		assertThat(((Label) component).getValue()).isEqualTo(COMPONENT_TITLE);
	}

	@Test
	public void shouldCreateNoReadAccessLabelInsteadOfNoOfAttachments()
	{
		//given
		final String label = "No read access";
		given(permissionFacade.canReadInstance(workflowAction)).willReturn(Boolean.FALSE);
		given(labelService.getAccessDeniedLabel(any())).willReturn(label);

		//when
		final Label result = renderer.createNoOfAttachmentsLabel(widgetInstanceManager, workflowAction);

		//then
		assertThat(result.getValue()).isEqualTo(label);
	}

	@Test
	public void shouldCreateNoReadAccessLabelInsteadOfDate()
	{
		//given
		final String label = "No read access";
		given(permissionFacade.canReadInstance(workflowAction)).willReturn(Boolean.FALSE);
		given(labelService.getAccessDeniedLabel(any())).willReturn(label);

		//when
		final Label result = renderer.createDateLabel(workflowAction);

		//then
		assertThat(result.getValue()).isEqualTo(label);
	}
}
