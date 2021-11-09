/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import static com.hybris.backoffice.workflow.designer.renderer.AndRenderer.AND_CONNECTION_TEMPLATE_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowActionTemplate;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowDecisionTemplate;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@IntegrationTest
public class DefaultWorkflowDesignerPersistenceServiceIntegrationTest extends AbstractWorkflowDesignerIntegrationTest
{

	WorkflowDesignerPersistenceService workflowDesignerPersistenceService;

	@Before
	@Override
	public void prepareApplicationContextAndSession() throws Exception
	{
		super.prepareApplicationContextAndSession();
		CockpitTestUtil.mockZkEnvironment();
		this.workflowDesignerPersistenceService = getApplicationContext().getBean("workflowTemplatePersistenceService",
				WorkflowDesignerPersistenceService.class);
	}

	@Test
	public void shouldSaveOneAction()
	{
		// given
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);
		workflowTemplateModel.setActions(Collections.emptyList());
		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		createNewActionNode(context, "firstAction");

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).hasSize(1).extracting(ACTION_CODE).containsOnly("firstAction");
	}

	@Test
	public void shouldSaveActionWithDecisions()
	{
		// given
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);
		workflowTemplateModel.setActions(Collections.emptyList());

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: incomingDecision -> action -> outgoingDecision
		final Node action = createNewActionNode(context, "action");

		final Node outgoingDecision = createNewDecisionNode(context, "outgoingDecision");
		createEdge(context, action, outgoingDecision);

		final Node incomingDecision = createNewDecisionNode(context, "incomingDecision");
		createEdge(context, incomingDecision, action);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("outgoingDecision");
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("incomingDecision");
	}

	@Test
	public void shouldChangeOutgoingDecisionToIncomingDecision()
	{
		// given
		// workflow in database: action -> decision
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowDecisionTemplateModel decisionModel = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel.setCode("decision");

		final WorkflowActionTemplateModel actionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel.setCode("action");
		actionModel.setDecisionTemplates(Lists.newArrayList(decisionModel));
		actionModel.setIncomingTemplateDecisions(new ArrayList<>());
		actionModel.setWorkflow(workflowTemplateModel);
		actionModel.setPrincipalAssigned(userService.getAdminUser());

		workflowTemplateModel.setActions(Lists.newArrayList(actionModel));

		modelService.saveAll(decisionModel, actionModel, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on ui: decision -> action
		final Node action = createActionNode(context, new WorkflowActionTemplate(actionModel));

		final Node decision = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel));
		createEdge(context, decision, action);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_DECISIONS).isEmpty();
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision");
	}

	@Test
	public void shouldChangeIncomingDecisionToOutgoingDecision()
	{
		// given
		// workflow in database: decision -> action
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowDecisionTemplateModel decisionModel = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel.setCode("decision");

		final WorkflowActionTemplateModel actionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel.setCode("action");
		actionModel.setDecisionTemplates(new ArrayList<>());
		actionModel.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel));
		actionModel.setWorkflow(workflowTemplateModel);
		actionModel.setPrincipalAssigned(userService.getAdminUser());

		workflowTemplateModel.setActions(Lists.newArrayList(actionModel));

		modelService.saveAll(decisionModel, actionModel, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on ui: action -> decision
		final Node action = createActionNode(context, new WorkflowActionTemplate(actionModel));

		final Node decision = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel));
		createEdge(context, action, decision);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).isEmpty();
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision");
	}

	@Test
	public void shouldRemoveOutgoingActionWithoutDecisions()
	{
		// given
		// workflow in database fromAction -> decision -> toAction
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowDecisionTemplateModel decisionModel = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel.setCode("decision");

		final WorkflowActionTemplateModel fromActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		fromActionModel.setCode("fromAction");
		fromActionModel.setDecisionTemplates(Lists.newArrayList(decisionModel));
		fromActionModel.setIncomingTemplateDecisions(new ArrayList<>());
		fromActionModel.setWorkflow(workflowTemplateModel);
		fromActionModel.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowActionTemplateModel toActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		toActionModel.setCode("toAction");
		toActionModel.setDecisionTemplates(new ArrayList<>());
		toActionModel.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel));
		toActionModel.setWorkflow(workflowTemplateModel);
		toActionModel.setPrincipalAssigned(userService.getAdminUser());

		workflowTemplateModel.setActions(Lists.newArrayList(fromActionModel, toActionModel));

		modelService.saveAll(decisionModel, fromActionModel, toActionModel, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: fromAction -> decision
		final Node fromAction = createActionNode(context, new WorkflowActionTemplate(fromActionModel));
		final Node decision = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel));
		createEdge(context, fromAction, decision);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).as("toAction should be removed").containsOnly(fromActionModel);
		final WorkflowActionTemplateModel savedAction = workflowTemplateModel.getActions().get(0);
		final WorkflowDecisionTemplateModel savedDecision = Lists.newArrayList(savedAction.getDecisionTemplates()).get(0);

		assertThat(savedAction.getDecisionTemplates()).as("action is pointing towards decision").containsOnly(decisionModel);
		assertThat(savedAction.getIncomingTemplateDecisions()).as("no decision is pointing towards fromAction").isEmpty();
		assertThat(savedDecision.getToTemplateActions()).as("decision is not pointing towards any action").isEmpty();
		assertThat(savedDecision.getActionTemplate()).as("decision is coming from fromAction").isEqualTo(savedAction);
	}

	@Test
	public void shouldRemoveIncomingActionWithoutDecisions()
	{
		// given
		// workflow in database fromAction -> decision -> toAction
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowDecisionTemplateModel decisionModel = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel.setCode("decision");

		final WorkflowActionTemplateModel fromActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		fromActionModel.setCode("fromAction");
		fromActionModel.setDecisionTemplates(Lists.newArrayList(decisionModel));
		fromActionModel.setIncomingTemplateDecisions(new ArrayList<>());
		fromActionModel.setWorkflow(workflowTemplateModel);
		fromActionModel.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowActionTemplateModel toActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		toActionModel.setCode("toAction");
		toActionModel.setDecisionTemplates(new ArrayList<>());
		toActionModel.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel));
		toActionModel.setWorkflow(workflowTemplateModel);
		toActionModel.setPrincipalAssigned(userService.getAdminUser());

		workflowTemplateModel.setActions(Lists.newArrayList(fromActionModel, toActionModel));

		modelService.saveAll(decisionModel, fromActionModel, toActionModel, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: decision -> toAction
		final Node toAction = createActionNode(context, new WorkflowActionTemplate(toActionModel));
		final Node decision = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel));
		createEdge(context, decision, toAction);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).as("fromAction should be removed").containsOnly(toActionModel);
		final WorkflowActionTemplateModel savedAction = workflowTemplateModel.getActions().get(0);
		final WorkflowDecisionTemplateModel savedDecision = Lists.newArrayList(savedAction.getIncomingTemplateDecisions()).get(0);

		assertThat(savedAction.getDecisionTemplates()).as("toAction is not pointing towards any decision").isEmpty();
		assertThat(savedAction.getIncomingTemplateDecisions()).as("toAction should be coming from decision")
				.containsOnly(savedDecision);
		assertThat(savedDecision.getToTemplateActions()).as("decision is pointing towards toAction").containsOnly(savedAction);
		assertThat(savedDecision.getActionTemplate()).as("no action is pointing towards decision").isNull();
	}

	@Test
	public void shouldRemoveDecisionWithoutRemovingActions()
	{
		// given
		// workflow in database fromAction -> decision -> toAction
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowDecisionTemplateModel decisionModel = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel.setCode("decision");

		final WorkflowActionTemplateModel fromActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		fromActionModel.setCode("fromAction");
		fromActionModel.setDecisionTemplates(Lists.newArrayList(decisionModel));
		fromActionModel.setIncomingTemplateDecisions(new ArrayList<>());
		fromActionModel.setWorkflow(workflowTemplateModel);
		fromActionModel.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowActionTemplateModel toActionModel = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		toActionModel.setCode("toAction");
		toActionModel.setDecisionTemplates(new ArrayList<>());
		toActionModel.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel));
		toActionModel.setWorkflow(workflowTemplateModel);
		toActionModel.setPrincipalAssigned(userService.getAdminUser());

		workflowTemplateModel.setActions(Lists.newArrayList(fromActionModel, toActionModel));

		modelService.saveAll(decisionModel, fromActionModel, toActionModel, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: fromAction action
		createActionNode(context, new WorkflowActionTemplate(fromActionModel));
		createActionNode(context, new WorkflowActionTemplate(toActionModel));

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		assertThat(workflowTemplateModel.getActions()).as("no action should be removed").containsOnly(fromActionModel,
				toActionModel);
		assertThat(workflowTemplateModel.getActions()).as("no action is pointing towards decision").flatExtracting(ACTION_DECISIONS)
				.isEmpty();
		assertThat(workflowTemplateModel.getActions()).as("no decision is pointing towards any action")
				.flatExtracting(ACTION_INCOMING_DECISIONS).isEmpty();
	}

	@Test
	public void shouldChangeDirectionOfTheGraphCycle()
	{
		// given
		// workflow with cycle in database (...) -> action1 -> decision1 -> action2 -> decision2 -> action1 -> (...)
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowActionTemplateModel actionModel1 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel1.setCode("action1");
		actionModel1.setWorkflow(workflowTemplateModel);
		actionModel1.setPrincipalAssigned(userService.getAdminUser());
		final WorkflowActionTemplateModel actionModel2 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel2.setCode("action2");
		actionModel2.setWorkflow(workflowTemplateModel);
		actionModel2.setPrincipalAssigned(userService.getAdminUser());
		final WorkflowDecisionTemplateModel decisionModel1 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel1.setCode("decision1");
		final WorkflowDecisionTemplateModel decisionModel2 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel2.setCode("decision2");

		actionModel1.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel2));
		actionModel1.setDecisionTemplates(Lists.newArrayList(decisionModel1));

		decisionModel1.setActionTemplate(actionModel1);
		decisionModel1.setToTemplateActions(Lists.newArrayList(actionModel2));

		actionModel2.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel1));
		actionModel2.setDecisionTemplates(Lists.newArrayList(decisionModel2));

		decisionModel2.setActionTemplate(actionModel2);
		decisionModel2.setToTemplateActions(Lists.newArrayList(actionModel1));

		modelService.saveAll(decisionModel1, decisionModel2, actionModel1, actionModel2, workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: (...) <- action1 <- decision1 <- action2 <- decision2 <- action1 <- (...)
		final Node action1 = createActionNode(context, new WorkflowActionTemplate(actionModel1));
		final Node action2 = createActionNode(context, new WorkflowActionTemplate(actionModel2));
		final Node decision1 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel1));
		final Node decision2 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel2));
		createEdge(context, decision1, action1);
		createEdge(context, action2, decision1);
		createEdge(context, decision2, action2);
		createEdge(context, action1, decision2);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		final List<WorkflowActionTemplateModel> actions = workflowTemplateModel.getActions();
		final Set<WorkflowDecisionTemplateModel> allDecisions = extractDecisions(workflowTemplateModel);

		assertThat(actions).filteredOn(actionOfCode("action1")).flatExtracting(ACTION_INCOMING_DECISIONS)
				.containsOnly(decisionModel1);
		assertThat(actions).filteredOn(actionOfCode("action1")).flatExtracting(ACTION_DECISIONS).containsOnly(decisionModel2);

		assertThat(actions).filteredOn(actionOfCode("action2")).flatExtracting(ACTION_INCOMING_DECISIONS)
				.containsOnly(decisionModel2);
		assertThat(actions).filteredOn(actionOfCode("action2")).flatExtracting(ACTION_DECISIONS).containsOnly(decisionModel1);

		assertThat(allDecisions).filteredOn(decisionOfCode("decision1"))
				.extracting(WorkflowDecisionTemplateModel::getActionTemplate).containsOnly(actionModel2);
		assertThat(allDecisions).filteredOn(decisionOfCode("decision1"))
				.flatExtracting(WorkflowDecisionTemplateModel::getToTemplateActions).containsOnly(actionModel1);

		assertThat(allDecisions).filteredOn(decisionOfCode("decision2"))
				.extracting(WorkflowDecisionTemplateModel::getActionTemplate).containsOnly(actionModel1);
		assertThat(allDecisions).filteredOn(decisionOfCode("decision2"))
				.flatExtracting(WorkflowDecisionTemplateModel::getToTemplateActions).containsOnly(actionModel2);
	}

	@Test
	public void shouldChangeParentOfDecisionToNewAction()
	{
		// given
		// workflow in database action1 -> decision1
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowActionTemplateModel actionModel1 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel1.setCode("action1");
		actionModel1.setWorkflow(workflowTemplateModel);
		actionModel1.setPrincipalAssigned(userService.getAdminUser());
		final WorkflowDecisionTemplateModel decisionModel1 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel1.setCode("decision1");

		actionModel1.setDecisionTemplates(Lists.newArrayList(decisionModel1));

		modelService.saveAll(decisionModel1, actionModel1, workflowTemplateModel);
		modelService.refresh(decisionModel1);
		modelService.refresh(actionModel1);
		modelService.refresh(workflowTemplateModel);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI: action1  decision1 <- action2
		createActionNode(context, new WorkflowActionTemplate(actionModel1));
		final Node action2 = createNewActionNode(context, "action2");
		final Node decision1 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel1));
		createEdge(context, action2, decision1);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);
		final List<WorkflowActionTemplateModel> actions = workflowTemplateModel.getActions();
		final Set<WorkflowDecisionTemplateModel> decisions = extractDecisions(workflowTemplateModel);

		assertThat(actions).filteredOn(actionOfCode("action1")).flatExtracting(ACTION_INCOMING_DECISIONS).isEmpty();
		assertThat(actions).filteredOn(actionOfCode("action1")).flatExtracting(ACTION_DECISIONS).isEmpty();

		assertThat(actions).filteredOn(actionOfCode("action2")).flatExtracting(ACTION_INCOMING_DECISIONS).isEmpty();
		assertThat(actions).filteredOn(actionOfCode("action2")).flatExtracting(ACTION_DECISIONS).containsOnly(decisionModel1);

		assertThat(decisions).filteredOn(decisionOfCode("decision1")).extracting(WorkflowDecisionTemplateModel::getActionTemplate)
				.first().satisfies(action -> assertThat(action.getCode()).isEqualTo("action2"));
		assertThat(decisions).filteredOn(decisionOfCode("decision1"))
				.flatExtracting(WorkflowDecisionTemplateModel::getToTemplateActions).isEmpty();
	}

	@Test
	public void shouldConnectTwoDecisionsWithAndToAction()
	{
		// given
		// empty workflow in database
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);
		workflowTemplateModel.setActions(Collections.emptyList());
		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI:
		// decision1 -> and -> action1
		// decision2 -> and -> action1
		final Node newAction = createNewActionNode(context, "action1");
		final Node decision1 = createNewDecisionNode(context, "decision1");
		final Node decision2 = createNewDecisionNode(context, "decision2");
		final Node andNode = createAndNode(context);

		createEdge(context, decision1, andNode);
		createEdge(context, decision2, andNode);
		createEdge(context, andNode, newAction);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);

		// action is connected with both decisions
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision1", "decision2");
		// links have 'and' property
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_LINKS)
				.extracting(this::getAndConnectionProperty).allMatch(andConnectionProperty -> andConnectionProperty.equals(true));
	}

	@Test
	public void shouldReAddTargetActionOfAndNode()
	{
		// given
		// workflow in database:
		// decision1 -> and -> action1
		// decision2 -> and -> action1
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowActionTemplateModel actionModel1 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel1.setCode("action1");
		actionModel1.setWorkflow(workflowTemplateModel);
		actionModel1.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowDecisionTemplateModel decisionModel1 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel1.setCode("decision1");

		final WorkflowDecisionTemplateModel decisionModel2 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel2.setCode("decision2");

		actionModel1.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel1, decisionModel2));
		decisionModel1.setToTemplateActions(Lists.newArrayList(actionModel1));
		decisionModel2.setToTemplateActions(Lists.newArrayList(actionModel1));
		modelService.saveAll(decisionModel1, decisionModel2, actionModel1);
		workflowTemplateService.setAndConnectionBetweenActionAndDecision(decisionModel1, actionModel1);
		workflowTemplateService.setAndConnectionBetweenActionAndDecision(decisionModel2, actionModel1);
		modelService.refresh(decisionModel1);
		modelService.refresh(decisionModel2);
		modelService.refresh(actionModel1);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI:
		// decision1 -> and -> newAction
		// decision2 -> and -> newAction
		final Node newAction = createNewActionNode(context, "newAction");
		final Node decision1 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel1));
		final Node decision2 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel2));
		final Node andNode = createAndNode(context);

		createEdge(context, decision1, andNode);
		createEdge(context, decision2, andNode);
		createEdge(context, andNode, newAction);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);

		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision1", "decision2");
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_LINKS)
				.extracting(this::getAndConnectionProperty).allMatch(andConnectionProperty -> andConnectionProperty.equals(true));
	}

	@Test
	public void shouldChangeExistingConnectionsBetweenDecisionsAndActionsToGoThroughAnd()
	{
		// given
		// workflow in database:
		// decision1 -> action1
		// decision2 -> action1
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowActionTemplateModel actionModel1 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel1.setCode("action1");
		actionModel1.setWorkflow(workflowTemplateModel);
		actionModel1.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowDecisionTemplateModel decisionModel1 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel1.setCode("decision1");

		final WorkflowDecisionTemplateModel decisionModel2 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel2.setCode("decision2");

		actionModel1.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel1, decisionModel2));
		decisionModel1.setToTemplateActions(Lists.newArrayList(actionModel1));
		decisionModel2.setToTemplateActions(Lists.newArrayList(actionModel1));
		modelService.saveAll(decisionModel1, decisionModel2, actionModel1);
		modelService.refresh(decisionModel1);
		modelService.refresh(decisionModel2);
		modelService.refresh(actionModel1);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI:
		// decision1 -> and -> action1
		// decision2 -> and -> action1
		final Node newAction = createActionNode(context, new WorkflowActionTemplate(actionModel1));
		final Node decision1 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel1));
		final Node decision2 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel2));
		final Node andNode = createAndNode(context);

		createEdge(context, decision1, andNode);
		createEdge(context, decision2, andNode);
		createEdge(context, andNode, newAction);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);

		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision1", "decision2");
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_LINKS)
				.extracting(this::getAndConnectionProperty).allMatch(andConnectionProperty -> andConnectionProperty.equals(true));
	}

	@Test
	public void shouldChangeExistingConnectionsBetweenDecisionsAndActionsToGoDirectly()
	{
		// given
		// workflow in database:
		// decision1 -> and -> action1
		// decision2 -> and -> action1
		final WorkflowTemplateModel workflowTemplateModel = modelService.create(WorkflowTemplateModel._TYPECODE);

		final WorkflowActionTemplateModel actionModel1 = modelService.create(WorkflowActionTemplateModel._TYPECODE);
		actionModel1.setCode("action1");
		actionModel1.setWorkflow(workflowTemplateModel);
		actionModel1.setPrincipalAssigned(userService.getAdminUser());

		final WorkflowDecisionTemplateModel decisionModel1 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel1.setCode("decision1");

		final WorkflowDecisionTemplateModel decisionModel2 = modelService.create(WorkflowDecisionTemplateModel._TYPECODE);
		decisionModel2.setCode("decision2");

		actionModel1.setIncomingTemplateDecisions(Lists.newArrayList(decisionModel1, decisionModel2));
		decisionModel1.setToTemplateActions(Lists.newArrayList(actionModel1));
		decisionModel2.setToTemplateActions(Lists.newArrayList(actionModel1));
		modelService.saveAll(decisionModel1, decisionModel2, actionModel1);
		workflowTemplateService.setAndConnectionBetweenActionAndDecision(decisionModel1, actionModel1);
		workflowTemplateService.setAndConnectionBetweenActionAndDecision(decisionModel2, actionModel1);
		modelService.refresh(decisionModel1);
		modelService.refresh(decisionModel2);
		modelService.refresh(actionModel1);

		final NetworkChartContext context = prepareContext(workflowTemplateModel);

		// workflow on UI:
		// decision1 -> action1
		// decision2 -> action1
		final Node action1 = createActionNode(context, new WorkflowActionTemplate(actionModel1));
		final Node decision1 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel1));
		final Node decision2 = createDecisionNode(context, new WorkflowDecisionTemplate(decisionModel2));

		createEdge(context, decision1, action1);
		createEdge(context, decision2, action1);

		// when
		workflowDesignerPersistenceService.persist(context);

		// then
		modelService.refresh(workflowTemplateModel);

		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_DECISIONS).extracting(DECISION_CODE)
				.containsOnly("decision1", "decision2");
		assertThat(workflowTemplateModel.getActions()).flatExtracting(ACTION_INCOMING_LINKS)
				.extracting(this::getAndConnectionProperty)
				.allMatch(andConnectionProperty -> BooleanUtils.isFalse((Boolean) andConnectionProperty));
	}

	private Object getAndConnectionProperty(final LinkModel linkModel)
	{
		final Link link = modelService.getSource(linkModel);
		try
		{
			return link.getAttribute(AND_CONNECTION_TEMPLATE_PROPERTY);
		}
		catch (final JaloSecurityException e)
		{
			return null;
		}
	}

	private Predicate<WorkflowActionTemplateModel> actionOfCode(final String actionCode)
	{
		return action -> actionCode.equals(action.getCode());
	}

	private Predicate<WorkflowDecisionTemplateModel> decisionOfCode(final String decisionCode)
	{
		return decision -> decisionCode.equals(decision.getCode());
	}

	Set<WorkflowDecisionTemplateModel> extractDecisions(final WorkflowTemplateModel workflowTemplateModel)
	{
		return workflowTemplateModel.getActions().stream()
				.flatMap(
						action -> Stream.concat(action.getDecisionTemplates().stream(), action.getIncomingTemplateDecisions().stream()))
				.collect(Collectors.toSet());
	}
}
