/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_TEMPLATE_CODE;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_VERSION_LABEL;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link CMSWorkflowData} for a create operation
 */
public class CreateWorkflowValidator implements Validator
{
	private BiPredicate<String, Class<?>> itemModelExistsPredicate;
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;
	private Predicate<List<String>> isAnyItemAlreadyInWorkflowPredicate;
	private BiPredicate<String, String> labelExistsInCMSVersionsPredicate;
	private Predicate<String> cmsWorkflowTemplateExistsPredicate;
	private CMSAdminSiteService cmsAdminSiteService;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSWorkflowData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object objToValidate, final Errors errors)
	{
		final CMSWorkflowData cmsWorkflowData = (CMSWorkflowData) objToValidate;

		// Template
		ValidationUtils.rejectIfEmpty(errors, FIELD_TEMPLATE_CODE, CmsfacadesConstants.FIELD_REQUIRED);
		if (getCmsWorkflowTemplateExistsPredicate().negate().test(cmsWorkflowData.getTemplateCode()))
		{
			errors.rejectValue(FIELD_TEMPLATE_CODE, CmsfacadesConstants.FIELD_DOES_NOT_EXIST);
		}

		// Version
		if (cmsWorkflowData.getCreateVersion() != null && cmsWorkflowData.getCreateVersion())
		{
			ValidationUtils.rejectIfEmpty(errors, FIELD_VERSION_LABEL, CmsfacadesConstants.FIELD_REQUIRED);
		}

		// Validate attachments
		if (Objects.isNull(cmsWorkflowData.getAttachments()))
		{
			errors.rejectValue(FIELD_ATTACHMENTS, CmsfacadesConstants.FIELD_REQUIRED);
		}
		else if (cmsWorkflowData.getAttachments().isEmpty())
		{
			errors.rejectValue(FIELD_ATTACHMENTS, CmsfacadesConstants.WORKFLOW_MISSING_ATTACHMENTS);
		}
		else
		{
			final CatalogVersionModel catalogVersion = getCmsAdminSiteService().getActiveCatalogVersion();
			cmsWorkflowData.getAttachments().stream()
					.forEach(itemUuid -> validateAttachment(itemUuid, cmsWorkflowData, catalogVersion, errors));

			if (getIsAnyItemAlreadyInWorkflowPredicate().test(cmsWorkflowData.getAttachments()))
			{
				errors.rejectValue(FIELD_ATTACHMENTS, CmsfacadesConstants.WORKFLOW_ATTACHMENTS_ALREADY_IN_WORKFLOW);
			}
		}
	}

	/**
	 * This method validates each CmsItem that will be added as a workflow attachment.
	 *
	 * @param itemUuid
	 *           - The unique identifier of the CmsItem
	 * @param cmsWorkflowData
	 *           - The object that contains the data of the workflow to create
	 * @param catalogVersion
	 *           - The catalog version where the workflow instance will be created.
	 * @param errors
	 *           - Object that keeps track of the validation errors.
	 */
	protected void validateAttachment(final String itemUuid, final CMSWorkflowData cmsWorkflowData,
			final CatalogVersionModel catalogVersion, final Errors errors)
	{
		if (getCmsItemExistsInCatalogVersionPredicate().negate().test(itemUuid, catalogVersion))
		{
			errors.rejectValue(FIELD_ATTACHMENTS, CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENTS);
		}

		if (cmsWorkflowData.getCreateVersion() != null && cmsWorkflowData.getCreateVersion()
				&& getLabelExistsInCMSVersionsPredicate().test(itemUuid, cmsWorkflowData.getVersionLabel()))
		{
			errors.rejectValue(FIELD_VERSION_LABEL, CmsfacadesConstants.FIELD_ALREADY_EXIST);
		}
	}

	protected BiPredicate<String, Class<?>> getItemModelExistsPredicate()
	{
		return itemModelExistsPredicate;
	}

	@Required
	public void setItemModelExistsPredicate(final BiPredicate<String, Class<?>> itemModelExistsPredicate)
	{
		this.itemModelExistsPredicate = itemModelExistsPredicate;
	}

	protected BiPredicate<String, String> getLabelExistsInCMSVersionsPredicate()
	{
		return labelExistsInCMSVersionsPredicate;
	}

	@Required
	public void setLabelExistsInCMSVersionsPredicate(final BiPredicate<String, String> labelExistsInCMSVersionsPredicate)
	{
		this.labelExistsInCMSVersionsPredicate = labelExistsInCMSVersionsPredicate;
	}

	protected BiPredicate<String, CatalogVersionModel> getCmsItemExistsInCatalogVersionPredicate()
	{
		return cmsItemExistsInCatalogVersionPredicate;
	}

	@Required
	public void setCmsItemExistsInCatalogVersionPredicate(
			final BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate)
	{
		this.cmsItemExistsInCatalogVersionPredicate = cmsItemExistsInCatalogVersionPredicate;
	}

	protected CMSAdminSiteService getCmsAdminSiteService()
	{
		return cmsAdminSiteService;
	}

	@Required
	public void setCmsAdminSiteService(final CMSAdminSiteService cmsAdminSiteService)
	{
		this.cmsAdminSiteService = cmsAdminSiteService;
	}

	protected Predicate<List<String>> getIsAnyItemAlreadyInWorkflowPredicate()
	{
		return isAnyItemAlreadyInWorkflowPredicate;
	}

	@Required
	public void setIsAnyItemAlreadyInWorkflowPredicate(final Predicate<List<String>> isAnyItemAlreadyInWorkflowPredicate)
	{
		this.isAnyItemAlreadyInWorkflowPredicate = isAnyItemAlreadyInWorkflowPredicate;
	}

	protected Predicate<String> getCmsWorkflowTemplateExistsPredicate()
	{
		return cmsWorkflowTemplateExistsPredicate;
	}

	@Required
	public void setCmsWorkflowTemplateExistsPredicate(final Predicate<String> cmsWorkflowTemplateExistsPredicate)
	{
		this.cmsWorkflowTemplateExistsPredicate = cmsWorkflowTemplateExistsPredicate;
	}
}
