/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.handler;

import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerDataManipulationViewEventHandler.SCSS_ACTION_BUTTON;
import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerDataManipulationViewEventHandler.SCSS_AND_BUTTON;
import static com.hybris.backoffice.workflow.designer.handler.WorkflowDesignerDataManipulationViewEventHandler.SCSS_DECISION_BUTTON;
import static com.hybris.cockpitng.testing.util.CockpitTestUtil.findAllChildren;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.zk.au.AuRequest;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.backoffice.workflow.designer.handler.connection.WorkflowDesignerGroup;
import com.hybris.backoffice.workflow.designer.handler.retriever.WorkflowModelRetriever;
import com.hybris.backoffice.workflow.designer.persistence.WorkflowDesignerPersistenceService;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.event.ClickOnAddNodeButtonEvent;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;



@RunWith(MockitoJUnitRunner.class)
public class WorkflowDesignerDataManipulationViewEventHandlerTest
{

	public static final String LEFT_COORDINATES = "10";
	public static final String BOTTOM_COORDINATES = "20";

	@Mock
	private WorkflowModelRetriever retriever;
	@Mock
	private WorkflowDesignerPersistenceService workflowTemplatePersistenceService;
	@Mock
	private NotificationService notificationService;

	@InjectMocks
	@Spy
	private WorkflowDesignerDataManipulationViewEventHandler handler;

	@Before
	public void setUp()
	{
		CockpitTestUtil.mockZkEnvironment();
	}

	@Test
	public void shouldAddMenuItemsAndRegisterEventListeners()
	{
		// given
		final ArgumentCaptor<Menupopup> menupopupArgumentCaptor = ArgumentCaptor.forClass(Menupopup.class);
		final NetworkChartContext networkChartContext = mock(NetworkChartContext.class);

		final Component component = mock(Component.class);
		given(component.appendChild(menupopupArgumentCaptor.capture())).willReturn(true);

		final AuRequest auRequest = mock(AuRequest.class);
		given(auRequest.getData()).willReturn(Map.of("left", LEFT_COORDINATES, "bottom", BOTTOM_COORDINATES));

		final ClickOnAddNodeButtonEvent event = new ClickOnAddNodeButtonEvent(component, auRequest);

		// when
		final NetworkUpdates networkUpdates = handler.onAddNodeButtonClick(event, networkChartContext);

		// then
		assertThat(networkUpdates).isEqualTo(NetworkUpdates.EMPTY);
		final Menupopup popup = menupopupArgumentCaptor.getValue();
		final List<Menuitem> menuItems = findAllChildren(popup, Menuitem.class).collect(toList());

		// menu items: action, decision, and
		assertThat(menuItems).hasSize(3).extracting("sclass").containsOnly(SCSS_ACTION_BUTTON, SCSS_DECISION_BUTTON,
				SCSS_AND_BUTTON);
		assertThat(menuItems).extracting(item -> item.getEventListeners(Events.ON_CLICK)).hasSize(3);
	}

	@Test
	public void shouldEditionNotBePossibleForAndNode()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.AND.getValue());

		// when
		handler.onEdit(node, null);
		handler.onDoubleClick(node, null);

		// then
		then(handler).should(never()).notifyAboutNodeEdition(any(), any());
		then(notificationService).should(times(2)).notifyUser(
				eq(WorkflowDesignerDataManipulationViewEventHandler.NOTIFICATION_AREA_SOURCE), eq("andNodeEdition"),
				eq(NotificationEvent.Level.WARNING));
	}

	@Test
	public void shouldEventAboutActionEditionBeSent()
	{
		// given
		final Node node = mock(Node.class);
		given(node.getGroup()).willReturn(WorkflowDesignerGroup.ACTION.getValue());

		final NetworkChartContext context = mock(NetworkChartContext.class);
		final WidgetInstanceManager wim = mock(WidgetInstanceManager.class);
		final WidgetModel widgetModel = mock(WidgetModel.class);
		given(context.getWim()).willReturn(wim);
		given(wim.getModel()).willReturn(widgetModel);
		given(widgetModel.getValue(any(), any())).willReturn(Set.of());

		final WorkflowActionTemplateModel model = mock(WorkflowActionTemplateModel.class);
		final ItemModelContext itemModelContext = mock(ItemModelContext.class);
		given(model.getItemModelContext()).willReturn(itemModelContext);
		given(retriever.retrieve(node, context)).willReturn(Optional.of(model));

		// when
		handler.onEdit(node, context);
		handler.onDoubleClick(node, context);

		// then
		then(wim).should(times(2)).sendOutput(eq(WorkflowDesignerDataManipulationViewEventHandler.SOCKET_OUT_UPDATE_ACTION),
				eq(Map.ofEntries(Pair.of(WorkflowDesignerModelKey.KEY_PARENT_OBJECT, model),
						Pair.of(WorkflowDesignerModelKey.KEY_NODE, node), Pair.of(WorkflowDesignerModelKey.KEY_NODES, Set.of()),
						Pair.of(WorkflowDesignerModelKey.KEY_UPDATE_ID, true))));
	}
}
