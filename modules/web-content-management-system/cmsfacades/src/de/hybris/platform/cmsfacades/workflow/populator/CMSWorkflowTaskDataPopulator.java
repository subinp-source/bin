/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowParticipantService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowActionData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowAttachmentData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowTaskData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a list of {@link CMSWorkflowTaskData} instance from the {@link WorkflowActionModel} source data model.
 */
public class CMSWorkflowTaskDataPopulator implements Populator<WorkflowActionModel, List<CMSWorkflowTaskData>>
{
	private Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter;
	private UserService userService;
	private CMSWorkflowParticipantService cmsWorkflowParticipantService;
	private TypeService typeService;

	@Override
	public void populate(final WorkflowActionModel workflowActionModel, final List<CMSWorkflowTaskData> cmsWorkflowTaskDataList)
			throws ConversionException
	{

		if (isPrincipalAssignedInCurrentPrincipals().test(workflowActionModel))
		{
			final CMSWorkflowTaskData taskData = new CMSWorkflowTaskData();

			taskData.setAction(getCmsWorkflowActionDataConverter().convert(workflowActionModel));

			final List<CMSWorkflowAttachmentData> pages = workflowActionModel.getWorkflow().getAttachments().stream() //
					.filter(isPageTypeAttachment()) //
					.map(attachement -> (CMSItemModel) attachement.getItem()) //
					.map(item -> populateWorkflowAttachmentData(item)) //
					.collect(Collectors.toList());
			taskData.setAttachments(pages);

			cmsWorkflowTaskDataList.add(taskData);
		}

	}

	/**
	 * Returns the attachment data for the provided {@link CMSItemModel}.
	 *
	 * @param attachmentItem
	 *           The attached page.
	 * @return The associated {@link CMSWorkflowAttachmentData}.
	 */
	protected CMSWorkflowAttachmentData populateWorkflowAttachmentData(final CMSItemModel attachmentItem)
	{
		final CMSWorkflowAttachmentData pageData = new CMSWorkflowAttachmentData();
		pageData.setPageUid(attachmentItem.getUid());
		pageData.setPageName(attachmentItem.getName());
		pageData.setCatalogVersion(attachmentItem.getCatalogVersion().getVersion());
		pageData.setCatalogId(attachmentItem.getCatalogVersion().getCatalog().getId());
		pageData.setCatalogName(attachmentItem.getCatalogVersion().getCatalog().getName());
		return pageData;
	}

	/**
	 * Verifies that the item type of given attachment is a page type.
	 *
	 * @return true if the attachment is of type page, false otherwise.
	 */
	protected Predicate<WorkflowItemAttachmentModel> isPageTypeAttachment()
	{
		final ComposedTypeModel abstractPageComposedTypeModel = this.getTypeService()
				.getComposedTypeForClass(AbstractPageModel.class);

		return attachment -> getTypeService().isAssignableFrom(abstractPageComposedTypeModel, attachment.getTypeOfItem());
	}

	/**
	 * Verifies that the PrincipalAssigned of given {@link WorkflowActionModel} is part of related principals of current
	 * user.
	 *
	 * @return true if the PrincipalAssigned is part of the related principals of current user, false otherwise.
	 */
	protected Predicate<WorkflowActionModel> isPrincipalAssignedInCurrentPrincipals()
	{
		final UserModel currentUser = getUserService().getCurrentUser();
		final Collection<PrincipalModel> currentPrincipals = getCmsWorkflowParticipantService().getRelatedPrincipals(currentUser);

		return actionModel -> currentPrincipals.contains(actionModel.getPrincipalAssigned());
	}

	protected Converter<WorkflowActionModel, CMSWorkflowActionData> getCmsWorkflowActionDataConverter()
	{
		return cmsWorkflowActionDataConverter;
	}

	@Required
	public void setCmsWorkflowActionDataConverter(
			final Converter<WorkflowActionModel, CMSWorkflowActionData> cmsWorkflowActionDataConverter)
	{
		this.cmsWorkflowActionDataConverter = cmsWorkflowActionDataConverter;
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

	protected CMSWorkflowParticipantService getCmsWorkflowParticipantService()
	{
		return cmsWorkflowParticipantService;
	}

	@Required
	public void setCmsWorkflowParticipantService(final CMSWorkflowParticipantService cmsWorkflowParticipantService)
	{
		this.cmsWorkflowParticipantService = cmsWorkflowParticipantService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}
}
