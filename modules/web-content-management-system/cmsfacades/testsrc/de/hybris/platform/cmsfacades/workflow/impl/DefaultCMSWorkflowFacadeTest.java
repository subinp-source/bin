/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_COMMENT_DOMAIN;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.exceptions.AttributePermissionException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.ItemNotFoundException;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cms2.model.CMSWorkflowCommentModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminItemService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.common.service.impl.DefaultSearchResultConverter;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowEditableItemData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.exception.ValidationError;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.exception.WorkflowCreationException;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.Validator;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCMSWorkflowFacadeTest
{
	private static final String TEMPLATE_CODE = "some template code";
	private static final String WORKFLOW_DESCRIPTION = "some description";
	private static final String PAGE_UUID_1 = "page-uuid-1";
	private static final String PAGE_UUID_2 = "page-uuid-2";
	private static final String VERSION_LABEL = "some version label";
	private static final String GENERATED_VERSION_LABEL1 = VERSION_LABEL + " - 1";
	private static final String GENERATED_VERSION_LABEL2 = VERSION_LABEL + " - 2";
	private static final String VALID_WORKFLOW_ID = "ValidWorkflow";
	private static final String INVALID_WORKFLOW_ID = "InvalidWorkflow";
	private static final String VALID_ACTION_ID = "ValidAction";
	private static final String VALID_ACTION_DECISION_ID1 = "ValidActionDecision1";
	private static final String VALID_ACTION_DECISION_ID2 = "ValidActionDecision2";
	private static final String INVALID_ACTION_DECISION_ID = "InValidActionDecision";

	private static final String INVALID_ITEM_UID = "InvalidItemUid";
	private static final String VALID_ITEM_UID_1 = "ValidItemUid-1";
	private static final String VALID_ITEM_UID_2 = "ValidItemUid-2";
	private static final String VALID_ITEM_UUID_1 = "ValidItemUuid-1";
	private static final String VALID_ITEM_UUID_2 = "ValidItemUuid-2";

	private static final String COMMENT_CODE_KEY = "SomeCommentCode";

	@Mock
	private CMSItemModel cmsItemModel_1;

	@Mock
	private CMSItemModel cmsItemModel_2;

	@Mock
	private CMSWorkflowData cmsWorkflowData1;

	@Mock
	private CMSWorkflowData cmsWorkflowData2;

	@Mock
	private WorkflowModel workflowModel1;

	@Mock
	private WorkflowModel workflowModel2;

	@Mock
	private AbstractPageModel pageModel1;

	@Mock
	private AbstractPageModel pageModel2;

	@Mock
	private WorkflowItemAttachmentModel attachment1;

	@Mock
	private WorkflowItemAttachmentModel attachment2;

	@Captor
	private ArgumentCaptor<List<WorkflowItemAttachmentModel>> argumentCaptor;

	@Mock
	private UserModel userModel;

	@Mock
	private WorkflowModel workflowModel;

	@Mock
	private WorkflowActionModel workflowActionModel;

	@Mock
	private WorkflowActionModel decisionTargetActionModel1;

	@Mock
	private WorkflowActionModel decisionTargetActionModel2;

	@Mock
	private WorkflowTemplateModel workflowTemplateModel;

	@Mock
	private CMSWorkflowData workflowData;

	@Mock
	private CMSWorkflowData editWorkflowData;

	@Mock
	private CMSWorkflowData responseData;

	@Mock
	private PageableData pageableData;

	@Mock
	private SearchResult<WorkflowModel> workflowSearchResult;

	@Mock
	private Validator createWorkflowValidator;

	@Mock
	private Validator findWorkflowValidator;

	@Mock
	private FacadeValidationService facadeValidationService;

	@Mock
	private PlatformTransactionManager transactionManager;

	@Mock
	private UserService userService;

	@Mock
	private ModelService modelService;

	@Mock
	private PermissionCRUDService permissionCRUDService;

	@Mock
	private CMSVersionService cmsVersionService;

	@Mock
	private WorkflowTemplateService workflowTemplateService;

	@Mock
	private WorkflowProcessingService workflowProcessingService;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowDataConverter;

	@Mock
	private Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate;

	@Mock
	private CMSWorkflowService workflowService;

	@Mock
	private CMSWorkflowActionService workflowActionService;

	@Mock
	private Validator editWorkflowValidator;

	@Mock
	private CommentService commentService;

	@Mock
	private CommentModel comment;

	@Mock
	private DomainModel domain;

	@Mock
	private SearchResult<CommentModel> actionCommentSearchResult;

	@Mock
	private WorkflowDecisionModel workflowDecision1;

	@Mock
	private WorkflowDecisionModel workflowDecision2;

	@Mock
	private CMSWorkflowCommentModel cmsWorkflowComment;

	@Mock
	private ComposedTypeModel pageType1;

	@Mock
	private CMSAdminItemService cmsAdminItemService;

	@Mock
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	@Mock
	private PersistentKeyGenerator commentsCodeGenerator;

	@Mock
	private WorkflowActionModel automatedWorkflowAction;

	@Mock
	private WorkflowActionModel automatedWorkflowAction2;

	@Mock
	private Predicate<ItemModel> pageTypePredicate;

	@Spy
	@InjectMocks
	private DefaultCMSWorkflowFacade cmsWorkflowFacade;

	@Before
	public void setUp() throws CMSItemNotFoundException
	{
		cmsWorkflowFacade.setSearchResultConverter(new DefaultSearchResultConverter());
		cmsWorkflowFacade.setPostWorkflowActions(Collections.emptyList());

		when(workflowService.getWorkflowForCode(VALID_WORKFLOW_ID)).thenReturn(workflowModel);
		when(workflowService.getWorkflowForCode(INVALID_WORKFLOW_ID)).thenThrow(new UnknownIdentifierException("invalid id"));

		// Permissions
		doReturn(true).when(permissionCRUDService).canCreateTypeInstance(WorkflowModel._TYPECODE);
		doReturn(true).when(permissionCRUDService).canChangeType(WorkflowModel._TYPECODE);
		doReturn(true).when(permissionCRUDService).canReadType(WorkflowModel._TYPECODE);
		doReturn(true).when(permissionCRUDService).canCreateTypeInstance(CMSWorkflowCommentModel._TYPECODE);
		doReturn(true).when(permissionCRUDService).canChangeType(WorkflowActionModel._TYPECODE);
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.DESCRIPTION)).thenReturn(true);
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.ATTACHMENTS)).thenReturn(true);
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.STATUS)).thenReturn(true);

		doThrow(new TypePermissionException("invalid")).when(cmsWorkflowFacade).throwTypePermissionException(anyString(),
				anyString());
		doThrow(new AttributePermissionException("invalid")).when(cmsWorkflowFacade).throwAttributePermissionException(anyString(),
				anyString(), anyString());

		// Workflow
		doReturn(workflowTemplateModel).when(workflowTemplateService).getWorkflowTemplateForCode(TEMPLATE_CODE);
		doReturn(workflowModel).when(workflowService).createWorkflow(workflowTemplateModel, userModel);
		doReturn(true).when(workflowProcessingService).startWorkflow(workflowModel);

		// Attachments
		doReturn(Optional.of(pageModel1)).when(uniqueItemIdentifierService).getItemModel(PAGE_UUID_1, CMSItemModel.class);
		doReturn(Optional.of(pageModel2)).when(uniqueItemIdentifierService).getItemModel(PAGE_UUID_2, CMSItemModel.class);

		when(modelService.create(WorkflowItemAttachmentModel.class)).thenReturn(attachment1, attachment2);

		// Request Data
		doReturn(TEMPLATE_CODE).when(workflowData).getTemplateCode();
		doReturn(WORKFLOW_DESCRIPTION).when(workflowData).getDescription();
		doReturn(Arrays.asList(PAGE_UUID_1, PAGE_UUID_2)).when(workflowData).getAttachments();
		doReturn(false).when(workflowData).getCreateVersion();
		doReturn(VERSION_LABEL).when(workflowData).getVersionLabel();

		// Response Data
		doReturn(responseData).when(cmsWorkflowDataConverter).convert(workflowModel);

		// Workflow actions
		when(workflowModel.getActions()).thenReturn(Arrays.asList(workflowActionModel));
		when(workflowActionModel.getCode()).thenReturn(VALID_ACTION_ID);

		// Action comments
		when(workflowActionModel.getComments()).thenReturn(Arrays.asList(comment));
		when(commentService.getDomainForCode(CMS_WORKFLOW_COMMENT_DOMAIN)).thenReturn(domain);
		when(modelService.create(CMSWorkflowCommentModel.class)).thenReturn(cmsWorkflowComment);
		when(commentsCodeGenerator.generate()).thenReturn(COMMENT_CODE_KEY);

		// Action decisions
		when(workflowActionModel.getDecisions()).thenReturn(Arrays.asList(workflowDecision1, workflowDecision2));
		when(workflowDecision1.getCode()).thenReturn(VALID_ACTION_DECISION_ID1);
		when(workflowDecision2.getCode()).thenReturn(VALID_ACTION_DECISION_ID2);
		when(workflowDecision1.getAction()).thenReturn(workflowActionModel);

		// Workflow attachments
		when(workflowModel.getAttachments()).thenReturn(Arrays.asList(attachment1, attachment2));
		when(attachment1.getItem()).thenReturn(pageModel1);
		when(attachment2.getItem()).thenReturn(pageModel2);

		// User
		when(userService.getCurrentUser()).thenReturn(userModel);

		// Valid cms item models
		when(cmsAdminItemService.findByUid(VALID_ITEM_UID_1)).thenReturn(cmsItemModel_1);
		when(cmsAdminItemService.findByUid(VALID_ITEM_UID_2)).thenReturn(cmsItemModel_2);
		when(cmsItemModel_1.getUid()).thenReturn(VALID_ITEM_UID_1);
		when(cmsItemModel_2.getUid()).thenReturn(VALID_ITEM_UID_2);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(cmsItemModel_1)).thenReturn(VALID_ITEM_UUID_1);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(cmsItemModel_2)).thenReturn(VALID_ITEM_UUID_2);

		// Item related workflows
		when(workflowService.getRelatedWorkflowsForItem(cmsItemModel_1, CMS_WORKFLOW_ACTIVE_STATUSES))
				.thenReturn(Arrays.asList(workflowModel1, workflowModel2));
		when(workflowService.getRelatedWorkflowsForItem(cmsItemModel_2, CMS_WORKFLOW_ACTIVE_STATUSES)).thenReturn(Arrays.asList());
		when(cmsWorkflowDataConverter.convert(workflowModel1)).thenReturn(cmsWorkflowData1);
		when(cmsWorkflowDataConverter.convert(workflowModel2)).thenReturn(cmsWorkflowData2);

		// Page type predicates
		when(pageTypePredicate.test(pageModel1)).thenReturn(true);
		when(pageTypePredicate.test(pageModel2)).thenReturn(true);
	}

	@Test(expected = TypePermissionException.class)
	public void givenUserHasNoCreatePermissionOnWorkflow_WhenCreateAndStartWorkflowCalled_ThenItThrowsAnException()
			throws CMSItemNotFoundException
	{
		// GIVEN
		doReturn(false).when(permissionCRUDService).canCreateTypeInstance(WorkflowModel._TYPECODE);

		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);
	}

	@Test(expected = ValidationException.class)
	public void givenInvalidPayload_WhenCreateAndStartWorkflowCalled_ThenItMustThrowValidationError()
			throws CMSItemNotFoundException
	{
		// GIVEN
		doThrow(new ValidationException(new ValidationError(""))).when(facadeValidationService).validate(createWorkflowValidator,
				workflowData);

		cmsWorkflowFacade.createAndStartWorkflow(workflowData);
	}

	@Test
	public void givenValidPayload_WhenCreateAndStartWorkflowCalled_ThenItMustCreateAndStartWorkflow()
			throws CMSItemNotFoundException
	{
		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);

		// THEN
		verify(workflowService).createWorkflow(workflowTemplateModel, userModel);
		verify(workflowModel).setStatus(CronJobStatus.RUNNING);
		verify(modelService).saveAll();
	}

	@Test
	public void givenValidPayload_WhenCreateAndStartWorkflowCalled_ThenItMustConvertAndReturnTheRightResponsePayload()
			throws CMSItemNotFoundException
	{
		// WHEN
		final CMSWorkflowData result = cmsWorkflowFacade.createAndStartWorkflow(workflowData);

		// THEN
		// - Assert Original Data is correct
		verify(workflowModel).setDescription(workflowData.getDescription());
		verify(workflowModel).setAttachments(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue(), containsInAnyOrder(attachment1, attachment2));

		verify(attachment1).setWorkflow(workflowModel);
		verify(attachment1).setItem(pageModel1);
		verify(attachment2).setWorkflow(workflowModel);
		verify(attachment2).setItem(pageModel2);

		// - Assert original data is converted
		verify(cmsWorkflowDataConverter).convert(workflowModel);

		// - Assert converted data is correct
		verify(responseData).setAttachments(workflowData.getAttachments());
		verify(responseData).setTemplateCode(TEMPLATE_CODE);
		assertThat(result, is(responseData));

	}

	@Test
	public void givenValidPayload_WhenCreateAndStartWorkflowCalled_ThenItMustSetApprovalStatusOfPageAttachemntsToCheck()
	{
		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);

		// THEN
		verify(pageModel1).setApprovalStatus(CmsApprovalStatus.CHECK);
		verify(pageModel2).setApprovalStatus(CmsApprovalStatus.CHECK);
	}

	@Test
	public void givenCreateVersionFlagIsFalse_WhenCreateAndStartWorkflowCalled_ThenNoVersionIsCreated()
			throws CMSItemNotFoundException
	{
		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);

		// THEN
		verifyZeroInteractions(cmsVersionService);
	}

	@Test
	public void givenCreateVersionFlagIsTrue_WhenCreateAndStartWorkflowCalled_ThenItMustGenerateANewVersionForEachAttachment()
			throws CMSItemNotFoundException
	{
		// GIVEN
		doReturn(true).when(workflowData).getCreateVersion();

		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);

		// THEN
		verify(cmsVersionService).createVersionForItem(pageModel1, GENERATED_VERSION_LABEL1, "");
		verify(cmsVersionService).createVersionForItem(pageModel2, GENERATED_VERSION_LABEL2, "");
	}

	@Test(expected = WorkflowCreationException.class)
	public void givenWorkflowCannotBeStarted_WhenCreateAndStartWorkflowIsCalled_ThenItMustThrowAnException()
			throws CMSItemNotFoundException
	{
		// GIVEN
		doReturn(false).when(workflowProcessingService).startWorkflow(workflowModel);

		// WHEN
		cmsWorkflowFacade.createAndStartWorkflow(workflowData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void givenEmptyWorkFlowOperationObjectWhenPerformOperationIsCalledThenWillThrowException() throws ItemNotFoundException
	{

		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void givenInvalidWorkflowCode_WhenPerformOperationIsCalled_ThenWillThrowException() throws ItemNotFoundException
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.CANCEL_WORKFLOW);

		// WHEN
		cmsWorkflowFacade.performOperation(INVALID_WORKFLOW_ID, data);
	}

	@Test(expected = TypePermissionException.class)
	public void givenUserHasNoPermissionOnWorkflow_WhenPerformCancelOperationIsCalledThen_WillThrowException()
			throws ItemNotFoundException
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.CANCEL_WORKFLOW);
		doReturn(false).when(permissionCRUDService).canChangeType(WorkflowModel._TYPECODE);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);
	}

	@Test
	public void givenAWorkflowForCode_WhenPerformCancelWorkflowIsCalled_ThenItMustCancelTheWorkflow() throws ItemNotFoundException
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.CANCEL_WORKFLOW);

		doReturn(true).when(permissionCRUDService).canChangeType(WorkflowModel._TYPECODE);

		workflowModel.setCode(VALID_WORKFLOW_ID);
		when(responseData.getWorkflowCode()).thenReturn(VALID_WORKFLOW_ID);
		doReturn(true).when(workflowProcessingService).terminateWorkflow(workflowModel);
		when(workflowActionService.getWorkflowActionForCode(workflowModel, VALID_ACTION_ID)).thenReturn(workflowActionModel);
		when(workflowActionService.getActionDecisionForCode(workflowActionModel, VALID_ACTION_DECISION_ID1))
				.thenReturn(workflowDecision1);

		// WHEN
		final CMSWorkflowData result = cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);

		// THEN
		verify(workflowService).getWorkflowForCode(VALID_WORKFLOW_ID);
		verify(workflowProcessingService).terminateWorkflow(workflowModel);
		assertThat(result.getWorkflowCode(), is(VALID_WORKFLOW_ID));
	}

	@Test(expected = AttributePermissionException.class)
	public void givenAWorkflowForCodeAndUserHasNoPermissionOnWorkflowStatusAttribute_WhenPerformCancelWorkflowIsCalled_ThenItMustThrowException()
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.CANCEL_WORKFLOW);
		when(permissionCRUDService.canChangeType(WorkflowModel._TYPECODE)).thenReturn(true);
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.STATUS)).thenReturn(false);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);
	}

	@Test(expected = TypePermissionException.class)
	public void givenUserHasNoChangePermissionOnWorkflow_WhenPerformMakeDecision_ThenWillThrowException()
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.MAKE_DECISION);
		when(permissionCRUDService.canChangeType(WorkflowModel._TYPECODE)).thenReturn(false);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);
	}

	@Test
	public void givenWorkflowForCode_WhenPerformMakeDecision_ThenItMustMakeDecision()
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.MAKE_DECISION);
		data.setCreateVersion(false);
		data.setActionCode(VALID_ACTION_ID);
		data.setDecisionCode(VALID_ACTION_DECISION_ID1);

		when(workflowActionService.getWorkflowActionForCode(workflowModel, VALID_ACTION_ID)).thenReturn(workflowActionModel);
		when(workflowActionService.getActionDecisionForCode(workflowActionModel, VALID_ACTION_DECISION_ID1))
				.thenReturn(workflowDecision1);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);

		// THEN
		verify(workflowProcessingService).decideAction(workflowActionModel, workflowDecision1);
	}

	@Test
	public void givenRequestToMakeDecisionWithVersion_WhenPerformMakeDecision_ThenVersionIsCreated()
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		data.setOperation(CMSWorkflowOperation.MAKE_DECISION);
		data.setVersionLabel(VERSION_LABEL);
		data.setCreateVersion(true);
		data.setActionCode(VALID_ACTION_ID);
		data.setDecisionCode(VALID_ACTION_DECISION_ID1);

		when(workflowActionService.getWorkflowActionForCode(workflowModel, VALID_ACTION_ID)).thenReturn(workflowActionModel);
		when(workflowActionService.getActionDecisionForCode(workflowActionModel, VALID_ACTION_DECISION_ID1))
				.thenReturn(workflowDecision1);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);

		// THEN
		verify(cmsVersionService).createVersionForItem(pageModel1, GENERATED_VERSION_LABEL1, "");
		verify(cmsVersionService).createVersionForItem(pageModel2, GENERATED_VERSION_LABEL2, "");
	}

	@Test
	public void givenDecisionWithTwoTargetActions_WhenPerformMakeDecisionIsCalled_ThenACommentIsAttachedToTheIncomingAndTargetActions()
	{
		// GIVEN
		final CMSWorkflowOperationData data = new CMSWorkflowOperationData();
		final String commentText = "Some text";
		data.setOperation(CMSWorkflowOperation.MAKE_DECISION);
		data.setCreateVersion(false);
		data.setActionCode(VALID_ACTION_ID);
		data.setDecisionCode(VALID_ACTION_DECISION_ID1);
		data.setComment(commentText);

		when(workflowActionService.getWorkflowActionForCode(workflowModel, VALID_ACTION_ID)).thenReturn(workflowActionModel);
		when(workflowActionService.getActionDecisionForCode(workflowActionModel, VALID_ACTION_DECISION_ID1))
				.thenReturn(workflowDecision1);
		when(workflowDecision1.getToActions()).thenReturn(Arrays.asList(decisionTargetActionModel1, decisionTargetActionModel2));
		when(automatedWorkflowActionTypePredicate.test(any())).thenReturn(false);

		// WHEN
		cmsWorkflowFacade.performOperation(VALID_WORKFLOW_ID, data);

		// THEN
		verify(cmsWorkflowComment).setCode(COMMENT_CODE_KEY);
		verify(cmsWorkflowComment).setText(commentText);
		verify(commentsCodeGenerator).generate();
		assertActionHasComment(workflowActionModel, cmsWorkflowComment);
		assertActionHasComment(decisionTargetActionModel1, cmsWorkflowComment);
		assertActionHasComment(decisionTargetActionModel2, cmsWorkflowComment);
	}

	@Test
	public void shouldFindNextActionForNonAutomatedAction()
	{
		when(automatedWorkflowActionTypePredicate.test(workflowActionModel)).thenReturn(false);

		final WorkflowActionModel result = cmsWorkflowFacade.findNextAction(workflowActionModel);

		assertThat(result, equalTo(workflowActionModel));
	}

	@Test
	public void shouldFindNextActionForAutomatedAction()
	{
		when(automatedWorkflowActionTypePredicate.test(automatedWorkflowAction)).thenReturn(true);
		when(automatedWorkflowAction.getSelectedDecision()).thenReturn(workflowDecision1);
		when(workflowDecision1.getToActions()).thenReturn(Collections.singleton(workflowActionModel));
		when(automatedWorkflowActionTypePredicate.test(workflowActionModel)).thenReturn(false);

		final WorkflowActionModel result = cmsWorkflowFacade.findNextAction(automatedWorkflowAction);

		assertThat(result, equalTo(workflowActionModel));
	}

	@Test
	public void shouldFindNextActionForConsecutiveAutomatedActions()
	{
		when(automatedWorkflowActionTypePredicate.test(automatedWorkflowAction)).thenReturn(true);
		when(automatedWorkflowAction.getSelectedDecision()).thenReturn(workflowDecision1);
		when(workflowDecision1.getToActions()).thenReturn(Collections.singleton(automatedWorkflowAction2));
		when(automatedWorkflowActionTypePredicate.test(automatedWorkflowAction2)).thenReturn(true);
		when(automatedWorkflowAction2.getSelectedDecision()).thenReturn(null);
		when(automatedWorkflowAction2.getDecisions()).thenReturn(Collections.singleton(workflowDecision2));
		when(workflowDecision2.getToActions()).thenReturn(Collections.singleton(workflowActionModel));
		when(automatedWorkflowActionTypePredicate.test(workflowActionModel)).thenReturn(false);

		final WorkflowActionModel result = cmsWorkflowFacade.findNextAction(automatedWorkflowAction);

		assertThat(result, equalTo(workflowActionModel));
	}

	@Test(expected = TypePermissionException.class)
	public void shouldFailFindAllWorkflowsNoReadTypePermission()
	{
		// GIVEN
		doReturn(false).when(permissionCRUDService).canReadType(WorkflowModel._TYPECODE);

		// WHEN
		cmsWorkflowFacade.findAllWorkflows(workflowData, pageableData);
	}

	@Test(expected = ValidationException.class)
	public void shouldFailFindAllWorkflowsValidationError()
	{
		// GIVEN
		doThrow(new ValidationException(new ValidationError("invalid inputs"))).when(facadeValidationService)
				.validate(findWorkflowValidator, workflowData);

		// WHEN
		cmsWorkflowFacade.findAllWorkflows(workflowData, pageableData);
	}

	@Test
	public void shouldPassFindAllWorkflows()
	{
		//GIVEN
		when(workflowData.getAttachments()).thenReturn(Collections.singletonList(PAGE_UUID_1));
		when(workflowData.getStatuses()).thenReturn(Arrays.asList(CronJobStatus.RUNNING.getCode()));
		final List<CMSItemModel> items = Collections.singletonList(pageModel1);
		final Set<CronJobStatus> statuses = Collections.singleton(CronJobStatus.RUNNING);
		when(workflowService.findWorkflowsForAttachedItemsAndStatuses(items, statuses, pageableData))
				.thenReturn(workflowSearchResult);

		// WHEN
		cmsWorkflowFacade.findAllWorkflows(workflowData, pageableData);

		// THEN
		verify(facadeValidationService).validate(findWorkflowValidator, workflowData);
		verify(workflowService).findWorkflowsForAttachedItemsAndStatuses(items, statuses, pageableData);
	}

	@Test(expected = TypePermissionException.class)
	public void givenUserHasNoChangePermissionOnWorkflow_WhenEditWorkflowCalled_ThenItThrowsAnException()
			throws ItemNotFoundException
	{
		// GIVEN
		doReturn(false).when(permissionCRUDService).canChangeType(WorkflowModel._TYPECODE);

		// WHEN
		cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);
	}

	@Test(expected = ValidationException.class)
	public void givenWorkflowDataHasValidationError_WhenEditWorkflowCalled_ThenItThrowsAnException() throws ItemNotFoundException
	{
		// GIVEN
		doThrow(new ValidationException(new ValidationError(""))).when(facadeValidationService).validate(editWorkflowValidator,
				editWorkflowData);

		// WHEN
		cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);
	}

	@Test
	public void givenWorkflowDataHasOnlyDescription_WhenEditWorkflowCalled_ThenDescriptionIsUpdated() throws ItemNotFoundException
	{
		// GIVEN
		when(editWorkflowData.getDescription()).thenReturn(WORKFLOW_DESCRIPTION);
		when(workflowModel.getJob()).thenReturn(workflowTemplateModel);
		when(workflowTemplateModel.getCode()).thenReturn(TEMPLATE_CODE);

		// WHEN
		final CMSWorkflowData result = cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);

		// THEN
		verify(workflowModel).setDescription(editWorkflowData.getDescription());
		verify(workflowModel, never()).setAttachments(any());
		verify(cmsWorkflowDataConverter).convert(workflowModel);

		assertThat(result, is(responseData));
	}

	@Test
	public void givenWorkflowDataHasDescriptionAndUserDoesNotHaveAttributePermission_WhenEditWorkflowCalled_ThenDescriptionIsNotUpdated()
	{
		// GIVEN
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.DESCRIPTION)).thenReturn(false);
		when(editWorkflowData.getDescription()).thenReturn(WORKFLOW_DESCRIPTION);
		when(workflowModel.getJob()).thenReturn(workflowTemplateModel);
		when(workflowTemplateModel.getCode()).thenReturn(TEMPLATE_CODE);

		// WHEN
		cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);

		// THEN
		verify(workflowModel, never()).setDescription(any());
	}

	@Test
	public void givenWorkflowDataHasAttachmentsAndUserDoesNotHaveAttributePermission_WhenEditWorkflowCalled_ThenAttachmentsAttributeIsNotUpdated()
	{
		// GIVEN
		when(permissionCRUDService.canChangeAttribute(WorkflowModel._TYPECODE, WorkflowModel.ATTACHMENTS)).thenReturn(false);
		when(editWorkflowData.getDescription()).thenReturn(WORKFLOW_DESCRIPTION);
		when(workflowModel.getJob()).thenReturn(workflowTemplateModel);
		when(workflowTemplateModel.getCode()).thenReturn(TEMPLATE_CODE);

		// WHEN
		cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);

		// THEN
		verify(workflowModel, never()).setAttachments(any());
	}

	@Test
	public void givenWorkflowDataHasAttachments_WhenEditWorkflowCalled_ThenAttachmentsAreUpdated() throws ItemNotFoundException
	{
		// GIVEN
		when(editWorkflowData.getAttachments()).thenReturn(Arrays.asList(PAGE_UUID_1, PAGE_UUID_2));
		when(workflowModel.getJob()).thenReturn(workflowTemplateModel);
		when(workflowTemplateModel.getCode()).thenReturn(TEMPLATE_CODE);

		// WHEN
		final CMSWorkflowData result = cmsWorkflowFacade.editWorkflow(VALID_WORKFLOW_ID, editWorkflowData);

		// THEN
		verify(workflowModel).setAttachments(argumentCaptor.capture());
		assertThat(argumentCaptor.getValue(), containsInAnyOrder(attachment1, attachment2));

		verify(attachment1).setWorkflow(workflowModel);
		verify(attachment1).setItem(pageModel1);
		verify(attachment2).setWorkflow(workflowModel);
		verify(attachment2).setItem(pageModel2);


		verify(workflowModel, never()).setDescription(any());
		verify(cmsWorkflowDataConverter).convert(workflowModel);

		verify(responseData).setAttachments(workflowData.getAttachments());
		assertThat(result, is(responseData));

	}

	@Test
	public void givenWorkflowForCode_WhenGetWorkflowForCodeIsCalled_ThenWorkflowIsReturned() throws ItemNotFoundException
	{
		// WHEN
		final CMSWorkflowData workflowData = cmsWorkflowFacade.getWorkflowForCode(VALID_WORKFLOW_ID);

		// THEN
		verify(cmsWorkflowDataConverter).convert(workflowModel);
		assertNotNull(workflowData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void shouldThrowExceptionIfUidDoesNotExistWhileVerifyingWhetherItemsEditableOrNot() throws CMSItemNotFoundException
	{
		// GIVEN
		when(cmsAdminItemService.findByUid(INVALID_ITEM_UID)).thenThrow(new CMSItemNotFoundException("Not found"));

		// WHEN
		cmsWorkflowFacade.getSessionUserEditableItems(Arrays.asList(INVALID_ITEM_UID));
	}

	@Test
	public void shouldReturnFirstItemIsEditableAndFalseForSecondWhenGetItemsEditableByCurrentUserIsCalled()
	{
		//GIVEN
		when(workflowService.isItemEditableBySessionUser(cmsItemModel_1)).thenReturn(true);
		when(workflowService.isItemEditableBySessionUser(cmsItemModel_2)).thenReturn(false);
		when(workflowService.getWorkflowWhereItemEditable(cmsItemModel_1)).thenReturn(Optional.of(workflowModel1));
		when(workflowService.getWorkflowWhereItemEditable(cmsItemModel_2)).thenReturn(Optional.empty());
		// WHEN
		final List<CMSWorkflowEditableItemData> editableItems = cmsWorkflowFacade
				.getSessionUserEditableItems(Arrays.asList(VALID_ITEM_UID_1, VALID_ITEM_UID_2));

		// THEN
		final CMSWorkflowEditableItemData itemData1 = getEditableItemByUid(editableItems, VALID_ITEM_UID_1);
		final CMSWorkflowEditableItemData itemData2 = getEditableItemByUid(editableItems, VALID_ITEM_UID_2);
		assertTrue(itemData1.isEditableByUser());
		assertFalse(itemData2.isEditableByUser());
	}

	protected CMSWorkflowEditableItemData getEditableItemByUid(final List<CMSWorkflowEditableItemData> editableItems,
			final String itemUid)
	{
		return editableItems.stream().filter(item -> item.getUid().equalsIgnoreCase(itemUid)).findFirst().get();
	}

	@SuppressWarnings("unchecked")
	protected void assertActionHasComment(final WorkflowActionModel actionModel, final CommentModel expectedComment)
	{
		final ArgumentCaptor<List<CommentModel>> argumentCaptor = ArgumentCaptor.forClass((Class) List.class);
		verify(actionModel).setComments(argumentCaptor.capture());
		assertTrue(argumentCaptor.getValue().contains(expectedComment));
	}
}
