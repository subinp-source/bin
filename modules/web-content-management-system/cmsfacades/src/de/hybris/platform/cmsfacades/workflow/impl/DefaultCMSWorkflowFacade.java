/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_COMMENT_COMPONENT;
import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_COMMENT_DOMAIN;
import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_DECISION_COMMENT_TYPE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.CmsApprovalStatus;
import de.hybris.platform.cms2.exceptions.AttributePermissionException;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cms2.model.CMSWorkflowCommentModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminItemService;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.common.service.SearchResultConverter;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowEditableItemData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.enums.CMSWorkflowOperation;
import de.hybris.platform.cmsfacades.exception.WorkflowCreationException;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.cmsfacades.workflow.CMSWorkflowFacade;
import de.hybris.platform.cmsfacades.workflow.postaction.PostWorkflowAction;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.Validator;


/**
 * Default implementation of the {@link CMSWorkflowFacade}.
 */
public class DefaultCMSWorkflowFacade implements CMSWorkflowFacade
{
	private Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate;
	private CMSAdminItemService cmsAdminItemService;
	private CMSVersionService cmsVersionService;
	private Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowDataConverter;
	private CMSWorkflowService workflowService;
	private PersistentKeyGenerator commentsCodeGenerator;
	private CommentService commentService;
	private Validator createWorkflowValidator;
	private Validator editWorkflowValidator;
	private FacadeValidationService facadeValidationService;
	private Validator findWorkflowValidator;
	private ModelService modelService;
	private PermissionCRUDService permissionCRUDService;
	private List<PostWorkflowAction> postWorkflowActions;
	private SearchResultConverter searchResultConverter;
	private PlatformTransactionManager transactionManager;
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	private UserService userService;
	private CMSWorkflowActionService workflowActionService;
	private WorkflowProcessingService workflowProcessingService;
	private WorkflowTemplateService workflowTemplateService;
	private Predicate<ItemModel> pageTypePredicate;

	@Override
	public CMSWorkflowData createAndStartWorkflow(final CMSWorkflowData requestData)
	{
		return new TransactionTemplate(getTransactionManager()).execute(status -> {
			validateTypePermission(PermissionsConstants.CREATE, WorkflowModel._TYPECODE,
					getPermissionCRUDService()::canCreateTypeInstance);

			getFacadeValidationService().validate(getCreateWorkflowValidator(), requestData);

			final WorkflowModel workflow = createWorkflow(requestData);
			if (getWorkflowProcessingService().startWorkflow(workflow))
			{
				workflow.setStatus(CronJobStatus.RUNNING);
				getModelService().saveAll();

				final CMSWorkflowData responseData = getCmsWorkflowDataConverter().convert(workflow);

				// Doing it here to avoid having to re-process in the converter information already available.
				responseData.setAttachments(requestData.getAttachments());
				responseData.setTemplateCode(requestData.getTemplateCode());

				return responseData;
			}
			else
			{
				throw new WorkflowCreationException("Workflow could not be started.");
			}
		});
	}

	@Override
	public CMSWorkflowData editWorkflow(final String workflowCode, final CMSWorkflowData workflowData)
	{
		validateTypePermission(PermissionsConstants.CHANGE, WorkflowModel._TYPECODE, getPermissionCRUDService()::canChangeType);

		WorkflowModel workflow = getWorkflowService().getWorkflowForCode(workflowCode);

		workflowData.setOriginalWorkflowCode(workflowCode);
		getFacadeValidationService().validate(getEditWorkflowValidator(), workflowData);

		workflow = editWorkflow(workflowData, workflow);

		final CMSWorkflowData responseData = getCmsWorkflowDataConverter().convert(workflow);
		responseData.setAttachments(workflowData.getAttachments());
		responseData.setTemplateCode(workflow.getJob().getCode());
		return responseData;
	}

	@Override
	public CMSWorkflowData getWorkflowForCode(final String workflowCode)
	{
		return getCmsWorkflowDataConverter().convert(getWorkflowService().getWorkflowForCode(workflowCode));
	}

	/**
	 * Updates the {@link WorkflowModel} with the data from {@link CMSWorkflowData}.
	 *
	 * @param requestData
	 *           - the object that contains the data about the new workflow to create.
	 * @param workflow
	 *           - the workflow which needs to be updated.
	 * @return the updated {@link WorkflowModel}.
	 */
	protected WorkflowModel editWorkflow(final CMSWorkflowData requestData, final WorkflowModel workflow)
	{
		final boolean canModifyDescription = getPermissionCRUDService().canChangeAttribute(WorkflowModel._TYPECODE,
				WorkflowModel.DESCRIPTION);
		final boolean canModifyAttachments = getPermissionCRUDService().canChangeAttribute(WorkflowModel._TYPECODE,
				WorkflowModel.ATTACHMENTS);
		if (requestData.getDescription() != null && canModifyDescription)
		{
			workflow.setDescription(requestData.getDescription());
		}

		if (CollectionUtils.isNotEmpty(requestData.getAttachments()) && canModifyAttachments)
		{
			final List<CMSItemModel> itemsToAttach = requestData.getAttachments().stream() //
					.map(this::getCmsItemByUUID) //
					.collect(toList());

			final List<WorkflowItemAttachmentModel> attachments = itemsToAttach.stream()
					.map(cmsItem -> createAttachment(workflow, cmsItem)).collect(toList());
			workflow.setAttachments(attachments);
		}

		modelService.save(workflow);

		return workflow;
	}

	@Override
	public CMSWorkflowData performOperation(final String workflowCode, final CMSWorkflowOperationData data)
	{
		if (data.getOperation() == null)
		{
			throw new IllegalArgumentException(
					"Payload must contain 'operation' field with value one of " + Arrays.toString(CMSWorkflowOperation.values()));
		}

		final WorkflowModel workflow = getWorkflowService().getWorkflowForCode(workflowCode);

		final CMSWorkflowData workflowData = new TransactionTemplate(getTransactionManager()).execute(status -> {

			if (data.getOperation() == CMSWorkflowOperation.CANCEL_WORKFLOW)
			{
				return cancelWorkflow(workflow);
			}
			else if (data.getOperation() == CMSWorkflowOperation.MAKE_DECISION)
			{
				if (data.getCreateVersion() != null && data.getCreateVersion())
				{
					final List<CMSItemModel> attachments = getWorkflowAttachments(workflow);
					createVersionsForItems(attachments, data.getVersionLabel());
				}
				return makeDecision(workflow, data.getActionCode(), data.getDecisionCode(), data.getComment());
			}
			return null;
		});

		if (Objects.nonNull(workflowData))
		{
			processPostWorkflowActions(workflow, data);
		}

		return workflowData;
	}

	/**
	 * Performs additional logic after the operation is successfully executed.
	 *
	 * @param workflow
	 *           the workflow for which extra operation will be executed
	 * @param data
	 *           the DTO containing the information about the operation that was just performed successfully
	 */
	protected void processPostWorkflowActions(final WorkflowModel workflow, final CMSWorkflowOperationData data)
	{
		getPostWorkflowActions().stream() //
				.filter(action -> action.isApplicable().test(workflow, data)) //
				.forEach(action -> action.execute(workflow));
	}

	/**
	 * Cancels the workflow for the provided {@link WorkflowModel}.
	 *
	 * @param workflow
	 *           - the {@link WorkflowModel}
	 * @return The data of the cancelled workflow
	 */
	protected CMSWorkflowData cancelWorkflow(final WorkflowModel workflow)
	{
		validateTypePermission(PermissionsConstants.CHANGE, WorkflowModel._TYPECODE, getPermissionCRUDService()::canChangeType);
		validateAttributePermission(PermissionsConstants.CHANGE, WorkflowModel._TYPECODE, WorkflowModel.STATUS,
				getPermissionCRUDService()::canChangeAttribute);
		getWorkflowProcessingService().terminateWorkflow(workflow);
		return getCmsWorkflowDataConverter().convert(workflow);
	}

	/**
	 * Makes decision for provided {@link WorkflowModel}
	 *
	 * @param workflow
	 *           - the {@link WorkflowModel}
	 * @param actionCode
	 *           - the action code
	 * @param decisionCode
	 *           - the decision code
	 * @return The data of the changed workflow
	 */
	protected CMSWorkflowData makeDecision(final WorkflowModel workflow, final String actionCode, final String decisionCode,
			final String comment)
	{
		validateTypePermission(PermissionsConstants.CHANGE, WorkflowModel._TYPECODE, getPermissionCRUDService()::canChangeType);
		final WorkflowActionModel workflowAction = getWorkflowActionService().getWorkflowActionForCode(workflow, actionCode);
		final WorkflowDecisionModel workflowDecision = getWorkflowActionService().getActionDecisionForCode(workflowAction,
				decisionCode);

		getWorkflowProcessingService().decideAction(workflowAction, workflowDecision);
		createDecisionComments(workflowDecision, workflowDecision.getName(), comment);
		return getCmsWorkflowDataConverter().convert(workflow);
	}

	/**
	 * Creates a comment and adds it to incoming and non-automated outgoing actions. The comment itself has a reference
	 * to the outgoing action.
	 *
	 * @param decision
	 *           - the {@link WorkflowDecisionModel}
	 * @param subject
	 *           - the subject of the comment
	 * @param text
	 *           - the text of the comment
	 */
	protected void createDecisionComments(final WorkflowDecisionModel decision, final String subject, final String text)
	{
		final DomainModel domain = getCommentService().getDomainForCode(CMS_WORKFLOW_COMMENT_DOMAIN);
		final ComponentModel component = getCommentService().getComponentForCode(domain, CMS_WORKFLOW_COMMENT_COMPONENT);
		final CommentTypeModel decisionCommentType = getCommentService().getCommentTypeForCode(component,
				CMS_WORKFLOW_DECISION_COMMENT_TYPE);

		final CMSWorkflowCommentModel comment = createComment(decision, decisionCommentType, component, subject, text);

		// add comment to the incoming action
		addCommentToAction(decision.getAction(), comment);

		// add comment to all outgoing actions
		decision.getToActions().stream() //
				.map(this::findNextAction) //
				.forEach(action -> addCommentToAction(action, comment));

		getModelService().saveAll();
	}

	/**
	 * Finds the next non-automated outgoing workflow action where a comment should be added to.
	 *
	 * @param action
	 *           the action just being completed
	 * @return the next action to be activated
	 */
	protected WorkflowActionModel findNextAction(final WorkflowActionModel action)
	{
		if (getAutomatedWorkflowActionTypePredicate().test(action))
		{
			// find the next action
			WorkflowDecisionModel selectedDecision = action.getSelectedDecision();
			if (selectedDecision == null)
			{
				selectedDecision = action.getDecisions().iterator().next();
			}
			return findNextAction(selectedDecision.getToActions().stream().findFirst().orElseThrow());
		}
		return action;
	}

	/**
	 * Adds a comment to an action.
	 *
	 * @param action
	 *           - the {@link WorkflowActionModel}
	 * @param comment
	 *           - the {@link CMSWorkflowCommentModel}
	 */
	protected void addCommentToAction(final WorkflowActionModel action, final CMSWorkflowCommentModel comment)
	{
		validateTypePermission(PermissionsConstants.CHANGE, WorkflowActionModel._TYPECODE,
				getPermissionCRUDService()::canChangeType);
		final List<CommentModel> comments = new ArrayList<>(action.getComments());
		comments.add(comment);
		action.setComments(comments);
	}

	/**
	 * Creates a comment and assigns a decision to it.
	 *
	 * @param decision
	 *           - the {@link WorkflowDecisionModel} to assign
	 * @param commentType
	 *           - the {@link CommentTypeModel}
	 * @param component
	 *           - the {@link ComponentModel}
	 * @param subject
	 *           - the subject of the comment
	 * @param text
	 *           - the text of the comment
	 * @return the {@link CMSWorkflowCommentModel}
	 */
	protected CMSWorkflowCommentModel createComment(final WorkflowDecisionModel decision, final CommentTypeModel commentType,
			final ComponentModel component, final String subject, final String text)
	{
		validateTypePermission(PermissionsConstants.CREATE, CMSWorkflowCommentModel._TYPECODE,
				getPermissionCRUDService()::canCreateTypeInstance);
		final CMSWorkflowCommentModel comment = getModelService().create(CMSWorkflowCommentModel.class);
		comment.setCode(getCommentsCodeGenerator().generate().toString());
		comment.setAuthor(getUserService().getCurrentUser());
		comment.setCommentType(commentType);
		comment.setComponent(component);
		comment.setText(text == null ? "" : text);
		comment.setSubject(subject);
		comment.setDecision(decision);
		return comment;
	}

	/**
	 * Validates that the current principal has the permission to execute an operation on the given type.
	 *
	 * @param permissionName
	 *           - The name of the permission to check.
	 * @param typeCode
	 *           - The code of the type for which to check the principal permission.
	 * @param permissionCheck
	 *           - Predicate to execute the permission check.
	 * @throws TypePermissionException
	 *            if the user does not have the expected permission on the given type.
	 */
	protected void validateTypePermission(final String permissionName, final String typeCode,
			final Predicate<String> permissionCheck)
	{
		if (!permissionCheck.test(typeCode))
		{
			throwTypePermissionException(permissionName, typeCode);
		}
	}

	/**
	 * Validates that the current principal has the permission to execute an operation on the given attribute.
	 *
	 * @param permissionName
	 *           - The name of the permission to check.
	 * @param typeCode
	 *           - The code of the type for which to check the principal permission.
	 * @param qualifier
	 *           - The qualifier of the attribute.
	 * @param permissionCheck
	 *           - Predicate to execute the permission check.
	 * @throws TypePermissionException
	 *            if the user does not have the expected permission on the given attribute.
	 */
	protected void validateAttributePermission(final String permissionName, final String typeCode, final String qualifier,
			final BiPredicate<String, String> permissionCheck)
	{
		if (!permissionCheck.test(typeCode, qualifier))
		{
			throwAttributePermissionException(permissionName, typeCode, qualifier);
		}
	}

	/**
	 * This method creates and throws a new {@link TypePermissionException}.
	 *
	 * @param permissionName
	 *           - the name of the permission that triggered the exception.
	 * @param typeCode
	 *           - the type for which the principal does not have permission and that triggered the exception.
	 */
	protected void throwTypePermissionException(final String permissionName, final String typeCode)
	{
		throw PermissionExceptionUtils.createTypePermissionException(permissionName, typeCode);
	}

	/**
	 * This method creates and throws a new {@link AttributePermissionException}.
	 *
	 * @param permissionName
	 *           - the name of the permission that triggered the exception.
	 * @param typeCode
	 *           - the type for which the principal does not have permission and that triggered the exception.
	 */
	protected void throwAttributePermissionException(final String permissionName, final String typeCode, final String qualifier)
	{
		throw PermissionExceptionUtils.createAttributePermissionException(permissionName, typeCode, qualifier);
	}

	/**
	 * This method creates and saves a new workflow. If the given request data has the createVersion flag enabled, this
	 * method will also create a new version for each of the attached items.
	 *
	 * @param requestData
	 *           - the object that contains the data about the new workflow to create.
	 * @return the newly created {@link WorkflowModel}.
	 */
	protected WorkflowModel createWorkflow(final CMSWorkflowData requestData)
	{
		final UserModel userModel = getUserService().getCurrentUser();
		final WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService()
				.getWorkflowTemplateForCode(requestData.getTemplateCode());

		final WorkflowModel workflow = getWorkflowService().createWorkflow(workflowTemplate, userModel);
		workflow.setDescription(requestData.getDescription());

		final List<CMSItemModel> itemsToAttach = requestData.getAttachments().stream() //
				.map(this::getCmsItemByUUID) //
				.collect(toList());

		// Update the page attachments approval status to CHECK so that the pages go into DRAFT state so that once
		// workflow is started all the pages are in IN PROGRESS.
		updatePageApprovalStatus(itemsToAttach);

		if (requestData.getCreateVersion() != null && requestData.getCreateVersion())
		{
			createVersionsForItems(itemsToAttach, requestData.getVersionLabel());
		}

		final List<WorkflowItemAttachmentModel> attachments = itemsToAttach.stream() //
				.map(cmsItem -> createAttachment(workflow, cmsItem)) //
				.collect(toList());
		workflow.setAttachments(attachments);

		// Needs to be saved before it can be started.
		modelService.save(workflow);

		return workflow;
	}

	/**
	 * Creates versions for a list of items.
	 *
	 * @param items
	 *           - the list of {@link CMSItemModel}
	 * @param versionLabel
	 *           - the version label
	 */
	protected void createVersionsForItems(final List<CMSItemModel> items, final String versionLabel)
	{
		final boolean moreThanOneVersion = items.size() > 1;

		IntStream.range(0, items.size()).forEach(index -> {
			final String generatedVersionLabel = moreThanOneVersion ? versionLabel + " - " + (index + 1) : versionLabel;
			createVersion(items.get(index), generatedVersionLabel);
		});
	}

	/**
	 * Updates the approval status of the page attachments to CHECK.
	 *
	 * @param items
	 *           - the list of {@link CMSItemModel}
	 */
	protected void updatePageApprovalStatus(final List<CMSItemModel> items)
	{
		items.stream() //
				.filter(getPageTypePredicate()::test) //
				.map(AbstractPageModel.class::cast) //
				.forEach(page -> {
					page.setApprovalStatus(CmsApprovalStatus.CHECK);
				});
	}

	/**
	 * This method retrieves the cmsItem identified by the given uuid.
	 *
	 * @param cmsItemUuid
	 *           - The uuid uniquely identifying the cmsItem to retrieve.
	 * @return the {@link CMSItemModel} found.
	 */
	protected CMSItemModel getCmsItemByUUID(final String cmsItemUuid)
	{
		// Note: This has already been validated. At this point, we are sure the item does exist.
		return getUniqueItemIdentifierService().getItemModel(cmsItemUuid, CMSItemModel.class)
				.orElseThrow(() -> new WorkflowCreationException("Cannot find item selected as a workflow attachment."));
	}

	/**
	 * This method creates a new version of the given cmsItem.
	 *
	 * @param cmsItem
	 *           - The cmsItem for which to create a new version.
	 * @param versionLabel
	 *           - The label used to identify the new version to create.
	 */
	protected void createVersion(final CMSItemModel cmsItem, final String versionLabel)
	{
		getCmsVersionService().createVersionForItem(cmsItem, versionLabel, "");
	}

	/**
	 * Creates a new attachment from a given cmsItem and adds it to the given workflow.
	 *
	 * @param workflow
	 *           - the workflow to which the new attachment will be added.
	 * @param cmsItem
	 *           - the CmsItem for which to create the new attachment.
	 * @return the newly created {@link WorkflowItemAttachmentModel}.
	 */
	protected WorkflowItemAttachmentModel createAttachment(final WorkflowModel workflow, final CMSItemModel cmsItem)
	{
		final WorkflowItemAttachmentModel attachment = getModelService().create(WorkflowItemAttachmentModel.class);
		attachment.setWorkflow(workflow);
		attachment.setItem(cmsItem);

		return attachment;
	}

	@Override
	public SearchResult<CMSWorkflowData> findAllWorkflows(final CMSWorkflowData workflowData, final PageableData pageableData)
	{
		validateTypePermission(PermissionsConstants.READ, WorkflowModel._TYPECODE, getPermissionCRUDService()::canReadType);
		getFacadeValidationService().validate(getFindWorkflowValidator(), workflowData);

		final List<CMSItemModel> items = Objects.nonNull(workflowData.getAttachments())
				? workflowData.getAttachments().stream().map(this::getCmsItemByUUID).collect(toList())
				: Collections.emptyList();

		final Set<CronJobStatus> statuses = workflowData.getStatuses().stream().map(String::toUpperCase).map(CronJobStatus::valueOf)
				.collect(toSet());

		final SearchResult<WorkflowModel> workflowsResult = getWorkflowService().findWorkflowsForAttachedItemsAndStatuses(items,
				statuses, pageableData);

		return getSearchResultConverter().convert(workflowsResult,
				workflowModel -> getCmsWorkflowDataConverter().convert(workflowModel));
	}

	/**
	 * Returns a list of workflow attachments. Filters out non CMSItemModel items.
	 *
	 * @param workflow
	 *           the {@link WorkflowModel}
	 * @return the list of {@link CMSItemModel} attachments.
	 */
	protected List<CMSItemModel> getWorkflowAttachments(final WorkflowModel workflow)
	{
		return workflow.getAttachments() //
				.stream() //
				.filter(attachment -> attachment.getItem() instanceof CMSItemModel) //
				.map(attachment -> (CMSItemModel) attachment.getItem()) //
				.collect(toList());
	}

	@Override
	public List<CMSWorkflowEditableItemData> getSessionUserEditableItems(final List<String> itemUids)
	{
		return itemUids.stream().distinct().map(this::getCMSItemByUid) //
				.map(item -> getSessionUserEditableItemData(item, getWorkflowService().isItemEditableBySessionUser(item))) //
				.collect(toList());
	}

	/**
	 * Generates {@link CMSWorkflowEditableItemData} from {@link CMSItemModel} and editableByCurrentUser attribute.
	 *
	 * @param itemModel
	 *           the {@link CMSItemModel}
	 * @param editableByCurrentUser
	 *           the attribute tells whether the item is editable or not
	 * @return the {@link CMSWorkflowEditableItemData}
	 */
	protected CMSWorkflowEditableItemData getSessionUserEditableItemData(final CMSItemModel itemModel,
			final boolean editableByCurrentUser)
	{
		final CMSWorkflowEditableItemData data = new CMSWorkflowEditableItemData();
		data.setUid(itemModel.getUid());
		data.setUuid(getUniqueIdentifierAttributeToDataContentConverter().convert(itemModel));
		data.setEditableByUser(editableByCurrentUser);
		final Optional<WorkflowModel> workflowWhereItemEditable = getWorkflowService().getWorkflowWhereItemEditable(itemModel);
		workflowWhereItemEditable.ifPresent(workflow -> data.setEditableInWorkflow(workflow.getCode()));
		return data;
	}

	/**
	 * Returns an {@link CMSItemModel} for a given uid.
	 *
	 * @param uid
	 *           the uid of an {@link CMSItemModel}
	 * @return the {@link CMSItemModel}
	 * @throws UnknownIdentifierException
	 *            if an item model cannot be found for the provided uid.
	 */
	protected CMSItemModel getCMSItemByUid(final String uid)
	{
		try
		{
			return getCmsAdminItemService().findByUid(uid);
		}
		catch (final CMSItemNotFoundException e)
		{
			throw new UnknownIdentifierException("No cms item found for uid '" + uid + "'");
		}
	}

	protected Validator getCreateWorkflowValidator()
	{
		return createWorkflowValidator;
	}

	@Required
	public void setCreateWorkflowValidator(final Validator createWorkflowValidator)
	{
		this.createWorkflowValidator = createWorkflowValidator;
	}

	protected Validator getFindWorkflowValidator()
	{
		return findWorkflowValidator;
	}

	@Required
	public void setFindWorkflowValidator(final Validator findWorkflowValidator)
	{
		this.findWorkflowValidator = findWorkflowValidator;
	}

	protected PlatformTransactionManager getTransactionManager()
	{
		return transactionManager;
	}

	@Required
	public void setTransactionManager(final PlatformTransactionManager transactionManager)
	{
		this.transactionManager = transactionManager;
	}

	protected FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	protected WorkflowTemplateService getWorkflowTemplateService()
	{
		return workflowTemplateService;
	}

	@Required
	public void setWorkflowTemplateService(final WorkflowTemplateService workflowTemplateService)
	{
		this.workflowTemplateService = workflowTemplateService;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

	@Required
	public void setUniqueItemIdentifierService(final UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected WorkflowProcessingService getWorkflowProcessingService()
	{
		return workflowProcessingService;
	}

	@Required
	public void setWorkflowProcessingService(final WorkflowProcessingService workflowProcessingService)
	{
		this.workflowProcessingService = workflowProcessingService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected CMSVersionService getCmsVersionService()
	{
		return cmsVersionService;
	}

	@Required
	public void setCmsVersionService(final CMSVersionService cmsVersionService)
	{
		this.cmsVersionService = cmsVersionService;
	}

	protected Converter<WorkflowModel, CMSWorkflowData> getCmsWorkflowDataConverter()
	{
		return cmsWorkflowDataConverter;
	}

	@Required
	public void setCmsWorkflowDataConverter(final Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowDataConverter)
	{
		this.cmsWorkflowDataConverter = cmsWorkflowDataConverter;
	}

	protected PermissionCRUDService getPermissionCRUDService()
	{
		return permissionCRUDService;
	}

	@Required
	public void setPermissionCRUDService(final PermissionCRUDService permissionCRUDService)
	{
		this.permissionCRUDService = permissionCRUDService;
	}

	protected CMSWorkflowService getWorkflowService()
	{
		return workflowService;
	}

	@Required
	public void setWorkflowService(final CMSWorkflowService workflowService)
	{
		this.workflowService = workflowService;
	}

	protected SearchResultConverter getSearchResultConverter()
	{
		return searchResultConverter;
	}

	@Required
	public void setSearchResultConverter(final SearchResultConverter searchResultConverter)
	{
		this.searchResultConverter = searchResultConverter;
	}

	protected Validator getEditWorkflowValidator()
	{
		return editWorkflowValidator;
	}

	@Required
	public void setEditWorkflowValidator(final Validator editWorkflowValidator)
	{
		this.editWorkflowValidator = editWorkflowValidator;
	}

	protected CommentService getCommentService()
	{
		return commentService;
	}

	@Required
	public void setCommentService(final CommentService commentService)
	{
		this.commentService = commentService;
	}

	protected UniqueIdentifierAttributeToDataContentConverter<ItemModel> getUniqueIdentifierAttributeToDataContentConverter()
	{
		return uniqueIdentifierAttributeToDataContentConverter;
	}

	@Required
	public void setUniqueIdentifierAttributeToDataContentConverter(
			final UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter)
	{
		this.uniqueIdentifierAttributeToDataContentConverter = uniqueIdentifierAttributeToDataContentConverter;
	}

	protected CMSAdminItemService getCmsAdminItemService()
	{
		return cmsAdminItemService;
	}

	@Required
	public void setCmsAdminItemService(final CMSAdminItemService cmsAdminItemService)
	{
		this.cmsAdminItemService = cmsAdminItemService;
	}

	protected PersistentKeyGenerator getCommentsCodeGenerator()
	{
		return commentsCodeGenerator;
	}

	@Required
	public void setCommentsCodeGenerator(final PersistentKeyGenerator commentsCodeGenerator)
	{
		this.commentsCodeGenerator = commentsCodeGenerator;
	}

	protected List<PostWorkflowAction> getPostWorkflowActions()
	{
		return postWorkflowActions;
	}

	@Required
	public void setPostWorkflowActions(final List<PostWorkflowAction> postWorkflowActions)
	{
		this.postWorkflowActions = postWorkflowActions;
	}

	protected CMSWorkflowActionService getWorkflowActionService()
	{
		return workflowActionService;
	}

	@Required
	public void setWorkflowActionService(final CMSWorkflowActionService workflowActionService)
	{
		this.workflowActionService = workflowActionService;
	}

	protected Predicate<WorkflowActionModel> getAutomatedWorkflowActionTypePredicate()
	{
		return automatedWorkflowActionTypePredicate;
	}

	@Required
	public void setAutomatedWorkflowActionTypePredicate(final Predicate<WorkflowActionModel> automatedWorkflowActionTypePredicate)
	{
		this.automatedWorkflowActionTypePredicate = automatedWorkflowActionTypePredicate;
	}

	protected Predicate<ItemModel> getPageTypePredicate()
	{
		return pageTypePredicate;
	}

	@Required
	public void setPageTypePredicate(final Predicate<ItemModel> pageTypePredicate)
	{
		this.pageTypePredicate = pageTypePredicate;
	}
}
