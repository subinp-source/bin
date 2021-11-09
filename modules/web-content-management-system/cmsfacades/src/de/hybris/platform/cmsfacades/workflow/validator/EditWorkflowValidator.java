/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_WORKFLOW_CODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_WORKFLOW_STATUS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_CANNOT_UPDATE_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_CANNOT_UPDATE_STATUS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_CODE_MISMATCH;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Objects;
import java.util.function.BiPredicate;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link CMSWorkflowData} for a edit workflow operation.
 */
public class EditWorkflowValidator implements Validator
{

	private CMSWorkflowService cmsWorkflowService;

	private WorkflowService workflowService;

	private CMSAdminSiteService cmsAdminSiteService;

	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSWorkflowData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object objToValidate, final Errors errors)
	{
		final CMSWorkflowData cmsWorkflowData = (CMSWorkflowData) objToValidate;

		if (Objects.nonNull(cmsWorkflowData.getWorkflowCode())
				&& !cmsWorkflowData.getWorkflowCode().equals(cmsWorkflowData.getOriginalWorkflowCode()))
		{
			errors.rejectValue(FIELD_WORKFLOW_CODE, WORKFLOW_CODE_MISMATCH);
		}

		final WorkflowModel workflow = getCmsWorkflowService().getWorkflowForCode(cmsWorkflowData.getOriginalWorkflowCode());

		if (cmsWorkflowData.getStatus() != null && !workflow.getStatus().getCode().equals(cmsWorkflowData.getStatus()))
		{
			errors.rejectValue(FIELD_WORKFLOW_STATUS, WORKFLOW_CANNOT_UPDATE_STATUS);
		}

		if (CollectionUtils.isNotEmpty(cmsWorkflowData.getAttachments()))
		{

			// If the workflow status is planned then validate if all the attachments belong to the catalog version in context.
			// else the attachments cannot be updated.
			if (getWorkflowService().isPlanned(workflow))
			{
				final CatalogVersionModel catalogVersion = getCmsAdminSiteService().getActiveCatalogVersion();
				cmsWorkflowData.getAttachments().stream().forEach(itemUuid -> validateAttachment(itemUuid, catalogVersion, errors));
			}
			else
			{
				errors.rejectValue(FIELD_ATTACHMENTS, WORKFLOW_CANNOT_UPDATE_ATTACHMENTS, new Object[]
				{ workflow.getStatus().getCode() }, null);
			}

		}

	}

	/**
	 * This method validates each CmsItem that will be added as a workflow attachment.
	 *
	 * @param itemUuid
	 *           - The unique identifier of the CmsItem
	 * @param catalogVersion
	 *           - The catalog version where the workflow instance will be updated.
	 * @param errors
	 *           - Object that keeps track of the validation errors.
	 */
	protected void validateAttachment(final String itemUuid, final CatalogVersionModel catalogVersion, final Errors errors)
	{
		if (getCmsItemExistsInCatalogVersionPredicate().negate().test(itemUuid, catalogVersion))
		{
			errors.rejectValue(FIELD_ATTACHMENTS, CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENTS);
		}
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

	public WorkflowService getWorkflowService()
	{
		return workflowService;
	}

	@Required
	public void setWorkflowService(final WorkflowService workflowService)
	{
		this.workflowService = workflowService;
	}

	public CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	public BiPredicate<String, CatalogVersionModel> getCmsItemExistsInCatalogVersionPredicate()
	{
		return cmsItemExistsInCatalogVersionPredicate;
	}

	@Required
	public void setCmsItemExistsInCatalogVersionPredicate(
			final BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate)
	{
		this.cmsItemExistsInCatalogVersionPredicate = cmsItemExistsInCatalogVersionPredicate;
	}
}
