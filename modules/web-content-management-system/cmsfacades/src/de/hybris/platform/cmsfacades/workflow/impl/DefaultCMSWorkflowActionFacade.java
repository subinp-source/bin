/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_ACTIVE_STATUSES;
import static de.hybris.platform.cms2.constants.Cms2Constants.CMS_WORKFLOW_COMMENT_DOMAIN;

import de.hybris.platform.cms2.common.exceptions.PermissionExceptionUtils;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.TypePermissionException;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowActionService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.common.service.SearchResultConverter;
import de.hybris.platform.cmsfacades.data.CMSCommentData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowTaskData;
import de.hybris.platform.cmsfacades.workflow.CMSWorkflowActionFacade;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CMSWorkflowActionFacade}.
 */
public class DefaultCMSWorkflowActionFacade implements CMSWorkflowActionFacade
{
	private CommentService commentService;
	private CMSWorkflowService cmsWorkflowService;
	private PermissionCRUDService permissionCRUDService;
	private SearchResultConverter searchResultConverter;
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;
	private UserService userService;
	private CMSWorkflowActionService workflowActionService;

	private Converter<CommentModel, CMSCommentData> cmsCommentDataConverter;
	private Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowWithActionsDataConverter;
	private Converter<WorkflowActionModel, List<CMSWorkflowTaskData>> cmsWorkflowTaskDataConverter;

	@Override
	public CMSWorkflowData getActionsForWorkflowCode(final String workflowCode)
	{
		return getCmsWorkflowWithActionsDataConverter().convert(getCmsWorkflowService().getWorkflowForCode(workflowCode));
	}

	@Override
	public SearchResult<CMSCommentData> getActionComments(final String workflowCode, final String actionCode,
			final PageableData pageableData)
	{
		validateTypePermission(PermissionsConstants.READ, CommentModel._TYPECODE, getPermissionCRUDService()::canReadType);

		final WorkflowModel workflow = getCmsWorkflowService().getWorkflowForCode(workflowCode);
		final DomainModel domain = getCommentService().getDomainForCode(CMS_WORKFLOW_COMMENT_DOMAIN);
		final WorkflowActionModel workflowAction = getWorkflowActionService().getWorkflowActionForCode(workflow, actionCode);

		final List<CommentModel> itemComments = getCommentService().getItemComments(workflowAction, null, domain,
				pageableData.getCurrentPage() * pageableData.getPageSize(), pageableData.getPageSize());

		final SearchResultImpl<CommentModel> searchResult = new SearchResultImpl(itemComments, workflowAction.getComments().size(),
				pageableData.getPageSize(), pageableData.getCurrentPage());

		return getSearchResultConverter().convert(searchResult, commentModel -> getCmsCommentDataConverter().convert(commentModel));
	}

	@Override
	public SearchResult<CMSWorkflowTaskData> findAllWorkflowTasks(final PageableData pageableData)
	{
		validateTypePermission(PermissionsConstants.READ, WorkflowModel._TYPECODE, getPermissionCRUDService()::canReadType);

		final UserModel currentUser = getUserService().getCurrentUser();
		final Collection<PrincipalModel> currentPrincipals = getCmsWorkflowParticipantService().getRelatedPrincipals(currentUser);

		final SearchResult<WorkflowActionModel> workflowActionResults = getWorkflowActionService()
				.findAllActiveWorkflowActionsByStatusAndPrincipals(CMS_WORKFLOW_ACTIVE_STATUSES, currentPrincipals, pageableData);

		final List<CMSWorkflowTaskData> cmsWorkflowTaskDataList = workflowActionResults.getResult().stream()
				.map(workflowActionModel -> getCmsWorkflowTaskDataConverter().convert(workflowActionModel)) //
				.flatMap(Collection::stream) //
				.collect(Collectors.toList());

		return new SearchResultImpl<>(cmsWorkflowTaskDataList, workflowActionResults.getTotalCount(),
				workflowActionResults.getRequestedCount(), workflowActionResults.getRequestedStart());
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

	protected CommentService getCommentService()
	{
		return commentService;
	}

	@Required
	public void setCommentService(final CommentService commentService)
	{
		this.commentService = commentService;
	}

	protected Converter<CommentModel, CMSCommentData> getCmsCommentDataConverter()
	{
		return cmsCommentDataConverter;
	}

	@Required
	public void setCmsCommentDataConverter(final Converter<CommentModel, CMSCommentData> cmsCommentDataConverter)
	{
		this.cmsCommentDataConverter = cmsCommentDataConverter;
	}

	protected Converter<WorkflowModel, CMSWorkflowData> getCmsWorkflowWithActionsDataConverter()
	{
		return cmsWorkflowWithActionsDataConverter;
	}

	@Required
	public void setCmsWorkflowWithActionsDataConverter(
			final Converter<WorkflowModel, CMSWorkflowData> cmsWorkflowWithActionsDataConverter)
	{
		this.cmsWorkflowWithActionsDataConverter = cmsWorkflowWithActionsDataConverter;
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

	protected CMSWorkflowService getCmsWorkflowService()
	{
		return cmsWorkflowService;
	}

	@Required
	public void setCmsWorkflowService(final CMSWorkflowService cmsWorkflowService)
	{
		this.cmsWorkflowService = cmsWorkflowService;
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

	protected Converter<WorkflowActionModel, List<CMSWorkflowTaskData>> getCmsWorkflowTaskDataConverter()
	{
		return cmsWorkflowTaskDataConverter;
	}

	@Required
	public void setCmsWorkflowTaskDataConverter(
			final Converter<WorkflowActionModel, List<CMSWorkflowTaskData>> cmsWorkflowTaskDataConverter)
	{
		this.cmsWorkflowTaskDataConverter = cmsWorkflowTaskDataConverter;
	}

	protected CMSWorkflowParticipantService getCmsWorkflowParticipantService()
	{
		return cmsWorkflowParticipantService;
	}

	@Required
	public void setCmsWorkflowParticipantService(final CMSWorkflowParticipantService cmsWorkflowParticipantService)
	{
		this.cmsWorkflowParticipantService = cmsWorkflowParticipantService;
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

	protected CMSWorkflowActionService getWorkflowActionService()
	{
		return workflowActionService;
	}

	@Required
	public void setWorkflowActionService(final CMSWorkflowActionService workflowActionService)
	{
		this.workflowActionService = workflowActionService;
	}
}
