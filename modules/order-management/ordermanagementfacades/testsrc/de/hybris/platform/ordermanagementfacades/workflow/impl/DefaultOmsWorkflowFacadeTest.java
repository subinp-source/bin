/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.ordermanagementfacades.workflow.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowActionData;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowCodesDataList;
import de.hybris.platform.ordermanagementfacades.workflow.data.WorkflowData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultOmsWorkflowFacadeTest
{
	protected static final String WORKFLOW_CODE_1 = "test1";
	protected static final String WORKFLOW_CODE_2 = "test2";
	protected static final String WORKFLOW_TEMPLATE_CODE = "templateCode";
	protected static final String WORKFLOW_DECISION_CODE = "decisionCode";
	protected static final List<String> WORKFLOW_CODES = asList(WORKFLOW_CODE_1, WORKFLOW_CODE_2);
	protected static final String USER_GROUP_UID = "userGroupUid";
	protected static final String ERROR_TYPE = "errorType";
	protected static final String ERROR_DESCRIPTION = "errorDescription";

	@InjectMocks
	private DefaultOmsWorkflowFacade omsWorkflowFacade;

	@Mock
	private ModelService modelService;
	@Mock
	private WorkflowService workflowService;
	@Mock
	private WorkflowTemplateService workflowTemplateService;
	@Mock
	private WorkflowProcessingService workflowProcessingService;
	@Mock
	private WorkflowActionService workflowActionService;
	@Mock
	private UserService userService;
	@Mock
	private WorkflowTemplateModel workflowTemplate;
	@Mock
	private WorkflowModel workflow1;
	@Mock
	private WorkflowModel workflow2;
	@Mock
	private WorkflowActionModel action1;
	@Mock
	private WorkflowActionModel action2;
	@Mock
	private UserGroupModel userGroup;
	@Mock
	private UserGroupModel adminUserGroup;
	@Mock
	private ItemModel item;
	@Mock
	private UserModel user;
	@Mock
	private WorkflowDecisionModel decision;
	@Mock
	private Converter<WorkflowModel, WorkflowData> workflowConverter;
	@Mock
	private Converter<WorkflowActionModel, WorkflowActionData> workflowActionConverter;
	@Mock
	private WorkflowActionData workflowActionData;

	@Before
	public void setUp()
	{
		when(userService.getUserGroupForUID(USER_GROUP_UID)).thenReturn(userGroup);
		when(userService.getAdminUserGroup()).thenReturn(adminUserGroup);
		when(workflowTemplateService.getWorkflowTemplateForCode(anyString())).thenReturn(workflowTemplate);
		when(workflowService
				.createWorkflow(anyString(), any(WorkflowTemplateModel.class), anyListOf(ItemModel.class), any(UserModel.class)))
				.thenReturn(workflow1);
		when(workflow1.getActions()).thenReturn(singletonList(action1));
		when(action1.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
	}

	@Test
	public void shouldFailWhenNoTemplateIsFound()
	{
		//given
		doThrow(UnknownIdentifierException.class).when(workflowTemplateService).getWorkflowTemplateForCode(WORKFLOW_TEMPLATE_CODE);

		//when
		omsWorkflowFacade.startWorkflow(item, WORKFLOW_CODE_1, WORKFLOW_TEMPLATE_CODE, USER_GROUP_UID);

		//then
		verify(workflowProcessingService, never()).startWorkflow(workflow1);
	}

	@Test
	public void shouldCreateWorkflow()
	{
		//when
		omsWorkflowFacade.startWorkflow(item, WORKFLOW_CODE_1, WORKFLOW_TEMPLATE_CODE, USER_GROUP_UID);

		//then
		verify(workflowProcessingService).startWorkflow(workflow1);
		verify(workflowConverter).convert(workflow1);
		verify(action1).setPrincipalAssigned(userGroup);
	}

	@Test
	public void shouldCreateWorkflowWithDefaultUserGroup()
	{
		//when
		omsWorkflowFacade.startWorkflow(item, WORKFLOW_CODE_1, WORKFLOW_TEMPLATE_CODE, null);

		//then
		verify(workflowProcessingService).startWorkflow(workflow1);
		verify(workflowConverter).convert(workflow1);
		verify(action1).setPrincipalAssigned(adminUserGroup);
	}

	@Test
	public void shouldCreateErrorWorkflow()
	{
		//when
		omsWorkflowFacade
				.startErrorRecoveryWorkflow(item, WORKFLOW_CODE_1, WORKFLOW_TEMPLATE_CODE, USER_GROUP_UID, ERROR_TYPE, ERROR_DESCRIPTION);

		//then
		verify(workflowProcessingService).startWorkflow(workflow1);
		verify(workflowConverter).convert(workflow1);
		verify(action1).setPrincipalAssigned(userGroup);
		verify(action1).setDescription(ERROR_TYPE);
		verify(action1).setComment(ERROR_DESCRIPTION);
	}

	@Test
	public void shouldNotCreateErrorWorkflowWhenTemplateDoesnotExist()
	{
		//given
		when(workflowTemplateService.getWorkflowTemplateForCode(anyString())).thenThrow(UnknownIdentifierException.class);
		//when
		omsWorkflowFacade
				.startErrorRecoveryWorkflow(item, WORKFLOW_CODE_1, WORKFLOW_TEMPLATE_CODE, USER_GROUP_UID, ERROR_TYPE, ERROR_DESCRIPTION);

		//then
		verify(workflowProcessingService, never()).startWorkflow(workflow1);
		verify(action1, never()).setPrincipalAssigned(userGroup);
		verify(workflow1, never()).setDescription(ERROR_TYPE);
	}

	@Test
	public void shouldDecideAction()
	{
		//given
		when(workflowService.getWorkflowForCode(anyString())).thenReturn(workflow1);
		when(workflow1.getActions()).thenReturn(singletonList(action1));
		when(action1.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(userService.getCurrentUser()).thenReturn(user);
		when(action1.getDecisions()).thenReturn(singletonList(decision));
		when(decision.getName()).thenReturn(WORKFLOW_DECISION_CODE);

		//when
		omsWorkflowFacade.decideAction(WORKFLOW_CODE_1, WORKFLOW_DECISION_CODE);

		//then
		verify(workflowProcessingService).decideAction(action1, decision);
		verify(action1).setPrincipalAssigned(userService.getAdminUserGroup());
	}

	@Test
	public void shouldDecideActions()
	{
		//given
		when(workflowService.getWorkflowForCode(WORKFLOW_CODE_1)).thenReturn(workflow1);
		when(workflowService.getWorkflowForCode(WORKFLOW_CODE_2)).thenReturn(workflow2);
		when(workflow1.getActions()).thenReturn(singletonList(action1));
		when(workflow2.getActions()).thenReturn(singletonList(action2));
		when(action1.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(action2.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(userService.getCurrentUser()).thenReturn(user);
		when(action1.getDecisions()).thenReturn(singletonList(decision));
		when(action2.getDecisions()).thenReturn(singletonList(decision));
		when(decision.getName()).thenReturn(WORKFLOW_DECISION_CODE);
		final WorkflowCodesDataList workflowCodesDataList = new WorkflowCodesDataList();
		workflowCodesDataList.setCodes(WORKFLOW_CODES);

		//when
		omsWorkflowFacade.decideActions(workflowCodesDataList, WORKFLOW_DECISION_CODE);

		//then
		verify(workflowProcessingService).decideAction(action1, decision);
		verify(workflowProcessingService).decideAction(action2, decision);
	}

	@Test
	public void shouldProceedDecideActionsWithInvalidCode()
	{
		//given
		when(workflowService.getWorkflowForCode(WORKFLOW_CODE_1)).thenThrow(UnknownIdentifierException.class);
		when(workflowService.getWorkflowForCode(WORKFLOW_CODE_2)).thenReturn(workflow2);
		when(workflow2.getActions()).thenReturn(singletonList(action2));
		when(action2.getStatus()).thenReturn(WorkflowActionStatus.IN_PROGRESS);
		when(userService.getCurrentUser()).thenReturn(user);
		when(action2.getDecisions()).thenReturn(singletonList(decision));
		when(decision.getName()).thenReturn(WORKFLOW_DECISION_CODE);
		final WorkflowCodesDataList workflowCodesDataList = new WorkflowCodesDataList();
		workflowCodesDataList.setCodes(WORKFLOW_CODES);

		//when
		omsWorkflowFacade.decideActions(workflowCodesDataList, WORKFLOW_DECISION_CODE);

		//then
		verify(workflowProcessingService).decideAction(action2, decision);
	}

	@Test
	public void testGetWorkflowActions()
	{
		//given
		when(workflowActionService
				.getAllUserWorkflowActionsWithAttachments(anyListOf(String.class), anyListOf(WorkflowActionStatus.class)))
				.thenReturn(singletonList(action1));
		when(workflowActionConverter.convert(action1)).thenReturn(workflowActionData);

		//when
		final List<WorkflowActionData> workflowActionDataResult = omsWorkflowFacade.getWorkflowActions();

		//then
		verify(workflowActionService)
				.getAllUserWorkflowActionsWithAttachments(emptyList(), singletonList(WorkflowActionStatus.IN_PROGRESS));
		verify(workflowActionConverter).convert(action1);
		assertEquals(singletonList(workflowActionData), workflowActionDataResult);
	}
}
