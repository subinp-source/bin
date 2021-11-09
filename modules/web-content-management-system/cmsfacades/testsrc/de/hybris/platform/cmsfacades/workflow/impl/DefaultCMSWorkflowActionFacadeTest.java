/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_COMMENT_DOMAIN;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cms2.model.CMSWorkflowCommentModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.common.service.impl.DefaultSearchResultConverter;
import de.hybris.platform.cmsfacades.data.CMSCommentData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowTaskData;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowActionFacadeTest
{
	private static final String VALID_WORKFLOW_ID = "ValidWorkflow";
	private static final String INVALID_WORKFLOW_ID = "InvalidWorkflow";
	private static final String VALID_ACTION_ID1 = "ValidAction1";
	private static final String VALID_ACTION_ID2 = "ValidAction2";
	private static final String INVALID_ACTION_ID = "InvalidAction";

	@Mock
	private WorkflowModel workflowModel;

	@Mock
	private WorkflowActionModel workflowActionModel1;

	@Mock
	private WorkflowActionModel workflowActionModel2;

	@Mock
	private PageableData pageableData;

	@Mock
	private SearchResult<WorkflowActionModel> workflowSearchResult;

	@Mock
	private PermissionCRUDService permissionCRUDService;

	@Mock
	private CMSWorkflowService cmsWorkflowService;

	@Mock
	private CMSWorkflowActionService workflowActionService;

	@Mock
	private UserService userService;

	@Mock
	private CommentService commentService;

	@Mock
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;

	@Mock
	private Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowWithActionsDataConverter;

	@Mock
	private Converter<CommentModel, CMSCommentData> cmsCommentDataConverter;

	@Mock
	private Converter<WorkflowActionModel, List<CMSWorkflowTaskData>> cmsWorkflowTaskDataConverter;

	@Mock
	private CommentModel comment1;

	@Mock
	private CommentModel comment2;

	@Mock
	private CMSCommentData commentData1;

	@Mock
	private CMSCommentData commentData2;

	@Mock
	private CMSWorkflowCommentModel cmsWorkflowComment;

	@Mock
	private DomainModel domain;

	@Mock
	private UserModel userModel;

	@Mock
	private CMSWorkflowTaskData workflowTaskData1;
	@Mock
	private CMSWorkflowTaskData workflowTaskData2;

	@Spy
	@InjectMocks
	private DefaultCMSWorkflowActionFacade cmsWorkflowActionFacade;

	@Before
	public void setUp() throws CMSItemNotFoundException
	{
		cmsWorkflowActionFacade.setSearchResultConverter(new DefaultSearchResultConverter());

		when(cmsWorkflowService.getWorkflowForCode(VALID_WORKFLOW_ID)).thenReturn(workflowModel);
		when(permissionCRUDService.canReadType(CommentModel._TYPECODE)).thenReturn(true);

		doThrow(new TypePermissionException("invalid")).when(cmsWorkflowActionFacade).throwTypePermissionException(anyString(),
				anyString());

		// User
		when(userService.getCurrentUser()).thenReturn(userModel);

		// Workflow Task Data
		doReturn(Collections.singletonList(workflowTaskData1)).when(cmsWorkflowTaskDataConverter).convert(workflowActionModel1);
		doReturn(Collections.singletonList(workflowTaskData2)).when(cmsWorkflowTaskDataConverter).convert(workflowActionModel2);

		// Workflow actions
		when(workflowModel.getActions()).thenReturn(Arrays.asList(workflowActionModel1, workflowActionModel2));
		when(workflowActionModel1.getCode()).thenReturn(VALID_ACTION_ID1);
		when(workflowActionModel2.getCode()).thenReturn(VALID_ACTION_ID2);

		// Action comments
		when(workflowActionModel1.getComments()).thenReturn(Arrays.asList(comment1, comment2));
		when(workflowActionModel2.getComments()).thenReturn(Collections.EMPTY_LIST);
		when(commentService.getDomainForCode(CMS_WORKFLOW_COMMENT_DOMAIN)).thenReturn(domain);
		when(cmsCommentDataConverter.convert(comment1)).thenReturn(commentData1);
		when(cmsCommentDataConverter.convert(comment2)).thenReturn(commentData2);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldFailFindAllActionsForInvalidWorkflowCode()
	{
		// GIVEN
		when(cmsWorkflowService.getWorkflowForCode(INVALID_WORKFLOW_ID)).thenThrow(new UnknownIdentifierException("invalid id"));

		// WHEN
		cmsWorkflowActionFacade.getActionsForWorkflowCode(INVALID_WORKFLOW_ID);
	}

	@Test
	public void shouldFindAllActionsForWorkflowCode()
	{
		// WHEN
		cmsWorkflowActionFacade.getActionsForWorkflowCode(VALID_WORKFLOW_ID);

		// THEN
		verify(cmsWorkflowService).getWorkflowForCode(VALID_WORKFLOW_ID);
		verify(cmsWorkflowWithActionsDataConverter).convert(any());
	}

	@Test(expected = TypePermissionException.class)
	public void shouldThrowExceptionIfUserDoesNotHaveReadPermissionForCommentModel()
	{
		// GIVEN
		when(permissionCRUDService.canReadType(CommentModel._TYPECODE)).thenReturn(false);

		// WHEN
		cmsWorkflowActionFacade.getActionComments(VALID_WORKFLOW_ID, VALID_ACTION_ID1, pageableData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfWorkflowIdIsInvalid()
	{
		// GIVEN
		when(cmsWorkflowService.getWorkflowForCode(INVALID_WORKFLOW_ID)).thenThrow(new UnknownIdentifierException("invalid id"));

		// WHEN
		cmsWorkflowActionFacade.getActionComments(INVALID_WORKFLOW_ID, VALID_ACTION_ID1, pageableData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfActionIdIsInvalid()
	{
		// GIVEN
		when(workflowActionService.getWorkflowActionForCode(workflowModel, INVALID_ACTION_ID))
				.thenThrow(new UnknownIdentifierException("invalid id"));

		// WHEN
		cmsWorkflowActionFacade.getActionComments(VALID_WORKFLOW_ID, INVALID_ACTION_ID, pageableData);
	}

	@Test
	public void shouldReturnListOfActionComments()
	{
		// GIVEN
		when(pageableData.getPageSize()).thenReturn(10);
		when(pageableData.getCurrentPage()).thenReturn(0);
		when(commentService.getItemComments(workflowActionModel1, null, domain, 0, 10))
				.thenReturn(Arrays.asList(comment1, comment2));
		when(workflowActionService.getWorkflowActionForCode(workflowModel, VALID_ACTION_ID1)).thenReturn(workflowActionModel1);

		// WHEN
		final SearchResult<CMSCommentData> actionComments = cmsWorkflowActionFacade.getActionComments(VALID_WORKFLOW_ID,
				VALID_ACTION_ID1, pageableData);

		// THEN
		assertThat(actionComments.getResult(), hasSize(2));
	}

	@Test(expected = TypePermissionException.class)
	public void shouldFailFindAllWorkflowTasksNoReadTypePermission()
	{
		// GIVEN
		when(permissionCRUDService.canReadType(WorkflowModel._TYPECODE)).thenReturn(false);

		// WHEN
		cmsWorkflowActionFacade.findAllWorkflowTasks(pageableData);
	}

	@Test
	public void shouldFindAllWorkflowTasks()
	{
		// GIVEN
		when(permissionCRUDService.canReadType(WorkflowModel._TYPECODE)).thenReturn(true);
		final Set<PrincipalModel> currentPrincipals = Collections.singleton(userModel);

		when(workflowSearchResult.getResult()).thenReturn(Arrays.asList(workflowActionModel1, workflowActionModel2));
		when(cmsWorkflowParticipantService.getRelatedPrincipals(userModel)).thenReturn(currentPrincipals);
		when(workflowActionService.findAllActiveWorkflowActionsByStatusAndPrincipals(CMS_WORKFLOW_ACTIVE_STATUSES,
				currentPrincipals, pageableData)).thenReturn(workflowSearchResult);

		// WHEN
		cmsWorkflowActionFacade.findAllWorkflowTasks(pageableData);

		// THEN
		verify(workflowActionService).findAllActiveWorkflowActionsByStatusAndPrincipals(CMS_WORKFLOW_ACTIVE_STATUSES,
				currentPrincipals, pageableData);
	}

}
