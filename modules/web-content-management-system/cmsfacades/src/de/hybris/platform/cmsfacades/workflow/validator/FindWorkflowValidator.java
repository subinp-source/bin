/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_ATTACHMENTS;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_STATUSES;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_ATTACHMENT;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.WORKFLOW_INVALID_STATUS;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cronjob.enums.CronJobStatus;

import java.util.Arrays;
import java.util.function.BiPredicate;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link CMSWorkflowData} for a find workflows operation
 */
public class FindWorkflowValidator implements Validator
{
	private CMSAdminSiteService cmsAdminSiteService;
	private BiPredicate<String, CatalogVersionModel> cmsItemExistsInCatalogVersionPredicate;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSWorkflowData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final CMSWorkflowData workflowData = (CMSWorkflowData) object;

		// Verify that all statuses are valid
		if (CollectionUtils.isNotEmpty(workflowData.getStatuses()))
		{
			workflowData.getStatuses().forEach(status -> {
				final boolean isValidStatus = Arrays.stream(CronJobStatus.values())
						.anyMatch(cronJobStatus -> cronJobStatus.getCode().equalsIgnoreCase(status));
				if (!isValidStatus)
				{
					errors.rejectValue(FIELD_STATUSES, WORKFLOW_INVALID_STATUS, new Object[]
					{ status }, null);
				}
			});
		}

		// Verify that all attachments are from the same catalog version
		if (CollectionUtils.isNotEmpty(workflowData.getAttachments()))
		{
			final CatalogVersionModel catalogVersion = getCmsAdminSiteService().getActiveCatalogVersion();
			final String itemUuid = workflowData.getAttachments().get(0);
			if (getCmsItemExistsInCatalogVersionPredicate().negate().test(itemUuid, catalogVersion))
			{
				errors.rejectValue(FIELD_ATTACHMENTS, WORKFLOW_INVALID_ATTACHMENT, new Object[]
				{ itemUuid }, null);
			}
		}
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
}
