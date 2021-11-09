/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.services;

import static com.hybris.cockpitng.testing.util.CockpitTestUtil.mockWidgetInstanceManager;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerNetworkPopulator;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;


@RunWith(MockitoJUnitRunner.class)
public class WorkflowModelFinderTest
{
	@Mock
	private NodeTypeService nodeTypeService;

	@InjectMocks
	@Spy
	private WorkflowModelFinder workflowModelFinder;

	@Mock
	private NetworkChartContext context;
	private WidgetInstanceManager wim;
	private WidgetModel model;

	@Before
	public void setUp()
	{
		wim = mockWidgetInstanceManager();
		model = wim.getModel();

		given(context.getWim()).willReturn(wim);
	}

	@Test
	public void shouldFindWorkflowAction()
	{
		// given
		final Node a1 = mock(Node.class);
		final WorkflowActionTemplateModel workflowAction1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionTemplateModel workflowAction2 = mock(WorkflowActionTemplateModel.class);

		doReturn(Set.of(workflowAction1, workflowAction2)).when(workflowModelFinder).findAllWorkflowActions(context);
		given(nodeTypeService.isSameAction(workflowAction1, a1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findWorkflowAction(context, a1).get()).isEqualTo(workflowAction1);
	}

	@Test
	public void shouldFindWorkflowDecision()
	{
		// given
		final Node d1 = mock(Node.class);
		final WorkflowDecisionTemplateModel workflowDecision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision2 = mock(WorkflowDecisionTemplateModel.class);

		doReturn(Set.of(workflowDecision1, workflowDecision2)).when(workflowModelFinder).findAllWorkflowDecisions(context);
		given(nodeTypeService.isSameDecision(workflowDecision1, d1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findWorkflowDecision(context, d1).get()).isEqualTo(workflowDecision1);
	}

	@Test
	public void shouldFindNewWorkflowAction()
	{
		// given
		final Node actionNode1 = mock(Node.class);
		final WorkflowActionTemplateModel workflowAction1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionTemplateModel workflowAction2 = mock(WorkflowActionTemplateModel.class);

		doReturn(Set.of(workflowAction1, workflowAction2)).when(workflowModelFinder).findNewWorkflowActions(context);
		given(nodeTypeService.isSameAction(workflowAction1, actionNode1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findNewWorkflowAction(context, actionNode1).get()).isEqualTo(workflowAction1);
	}

	@Test
	public void shouldFindNewWorkflowDecision()
	{
		// given
		final Node decisionNode1 = mock(Node.class);
		final WorkflowDecisionTemplateModel workflowDecision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision2 = mock(WorkflowDecisionTemplateModel.class);

		doReturn(Set.of(workflowDecision1, workflowDecision2)).when(workflowModelFinder).findNewWorkflowDecisions(context);
		given(nodeTypeService.isSameDecision(workflowDecision1, decisionNode1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findNewWorkflowDecision(context, decisionNode1).get()).isEqualTo(workflowDecision1);
	}

	@Test
	public void shouldFindAllWorkflowActions()
	{
		// given
		final WorkflowActionTemplateModel a1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionTemplateModel a2 = mock(WorkflowActionTemplateModel.class);
		doReturn(Set.of(a1)).when(workflowModelFinder).findNewWorkflowActions(context);
		doReturn(Set.of(a2)).when(workflowModelFinder).findWorkflowActionsFromWorkflowTemplateModel(context);

		// when
		final Set<WorkflowActionTemplateModel> allWorkflowActions = workflowModelFinder.findAllWorkflowActions(context);

		// then
		assertThat(allWorkflowActions).containsOnly(a1, a2);
	}

	@Test
	public void shouldFindAllWorkflowDecisions()
	{
		// given
		final WorkflowDecisionTemplateModel d1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplateModel d2 = mock(WorkflowDecisionTemplateModel.class);
		doReturn(Set.of(d1)).when(workflowModelFinder).findNewWorkflowDecisions(context);
		doReturn(Set.of(d2)).when(workflowModelFinder).findWorkflowDecisionsFromWorkflowTemplateModel(context);

		// when
		final Set<WorkflowDecisionTemplateModel> allWorkflowActions = workflowModelFinder.findAllWorkflowDecisions(context);

		// then
		assertThat(allWorkflowActions).containsOnly(d1, d2);
	}

	@Test
	public void shouldFindWorkflowTemplate()
	{
		// given
		final WorkflowTemplateModel workflow = mock(WorkflowTemplateModel.class);
		model.setValue(NetworkChartController.MODEL_INIT_DATA, workflow);

		// when / then
		assertThat(workflowModelFinder.findWorkflowTemplate(context)).isEqualTo(workflow);
	}

	@Test
	public void shouldFindWorkflowActionInWorkflowTemplateModel()
	{
		// given
		final Node a1 = mock(Node.class);
		final WorkflowActionTemplateModel workflowAction1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionTemplateModel workflowAction2 = mock(WorkflowActionTemplateModel.class);

		doReturn(Set.of(workflowAction1, workflowAction2)).when(workflowModelFinder)
				.findWorkflowActionsFromWorkflowTemplateModel(context);
		given(nodeTypeService.isSameAction(workflowAction1, a1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findWorkflowActionInWorkflowTemplateModel(context, a1).get()).isEqualTo(workflowAction1);
	}

	@Test
	public void shouldFindWorkflowDecisionInWorkflowTemplateModel()
	{
		// given
		final Node d1 = mock(Node.class);
		final WorkflowDecisionTemplateModel workflowDecision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision2 = mock(WorkflowDecisionTemplateModel.class);

		doReturn(Set.of(workflowDecision1, workflowDecision2)).when(workflowModelFinder)
				.findWorkflowDecisionsFromWorkflowTemplateModel(context);
		given(nodeTypeService.isSameDecision(workflowDecision1, d1)).willReturn(true);

		// when / then
		assertThat(workflowModelFinder.findWorkflowDecisionInWorkflowTemplateModel(context, d1).get()).isEqualTo(workflowDecision1);
	}

	@Test
	public void shouldFindWorkflowActionsFromWorkflowTemplateModel()
	{
		// given
		final WorkflowActionTemplateModel workflowAction1 = mock(WorkflowActionTemplateModel.class);
		final WorkflowActionTemplateModel workflowAction2 = mock(WorkflowActionTemplateModel.class);
		final WorkflowTemplateModel workflow = mock(WorkflowTemplateModel.class);
		given(workflow.getActions()).willReturn(List.of(workflowAction1, workflowAction2));

		doReturn(workflow).when(workflowModelFinder).findWorkflowTemplate(context);

		// when / then
		assertThat(workflowModelFinder.findWorkflowActionsFromWorkflowTemplateModel(context)).containsOnly(workflowAction1,
				workflowAction2);
	}

	@Test
	public void shouldFindWorkflowDecisionsFromWorkflowTemplateModel()
	{
		// given
		final WorkflowActionTemplateModel workflowAction = mock(WorkflowActionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision = mock(WorkflowDecisionTemplateModel.class);

		doReturn(Set.of(workflowAction)).when(workflowModelFinder).findWorkflowActionsFromWorkflowTemplateModel(context);
		doReturn(Set.of(workflowDecision)).when(workflowModelFinder).findDecisionsOfAction(workflowAction);

		// when / then
		assertThat(workflowModelFinder.findWorkflowDecisionsFromWorkflowTemplateModel(context)).containsOnly(workflowDecision);
	}

	@Test
	public void shouldFindActionDecisions()
	{
		// given
		final WorkflowActionTemplateModel workflowAction = mock(WorkflowActionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision1 = mock(WorkflowDecisionTemplateModel.class);
		final WorkflowDecisionTemplateModel workflowDecision2 = mock(WorkflowDecisionTemplateModel.class);

		given(workflowAction.getDecisionTemplates()).willReturn(List.of(workflowDecision1));
		given(workflowAction.getIncomingTemplateDecisions()).willReturn(List.of(workflowDecision2));

		// when / then
		assertThat(workflowModelFinder.findDecisionsOfAction(workflowAction)).containsOnly(workflowDecision1, workflowDecision2);
	}

	@Test
	public void shouldFindNewModels()
	{
		// given
		final ItemModel firstItem = mock(ItemModel.class);
		final ItemModel secondItem = mock(ItemModel.class);
		given(context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.willReturn(List.of(firstItem, secondItem));

		// when
		final List<ItemModel> newModels = workflowModelFinder.findNewModels(context);

		// then
		assertThat(newModels).containsOnly(firstItem, secondItem);
	}

	@Test
	public void shouldFindNewModelsReturningEmptyCollection()
	{
		// given
		given(context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.willReturn(null);

		// when
		final List<ItemModel> newModels = workflowModelFinder.findNewModels(context);

		// then
		assertThat(newModels).isNotNull().isEmpty();
	}

	@Test
	public void shouldFindNewWorkflowActions()
	{
		// given
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		final ItemModel someItem = mock(ItemModel.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		given(context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.willReturn(List.of(action, someItem, decision));

		// when
		final Set<WorkflowActionTemplateModel> newActions = workflowModelFinder.findNewWorkflowActions(context);

		// then
		assertThat(newActions).containsOnly(action);
	}

	@Test
	public void shouldFindNewWorkflowDecisions()
	{
		// given
		final WorkflowActionTemplateModel action = mock(WorkflowActionTemplateModel.class);
		final ItemModel someItem = mock(ItemModel.class);
		final WorkflowDecisionTemplateModel decision = mock(WorkflowDecisionTemplateModel.class);
		given(context.getWim().getModel().getValue(WorkflowDesignerNetworkPopulator.MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class))
				.willReturn(List.of(action, someItem, decision));

		// when
		final Set<WorkflowDecisionTemplateModel> newDecisions = workflowModelFinder.findNewWorkflowDecisions(context);

		// then
		assertThat(newDecisions).containsOnly(decision);
	}
}
