/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.ITEM_IN_WORKFLOW_NOT_EDITABLE_BY_CURRENT_USER;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowService;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.exception.NonEditableItemInWorkflowException;
import de.hybris.platform.util.localization.Localization;

import org.springframework.beans.factory.annotation.Required;


/**
 * Validates whether the item model can be edited in current workflow context.
 */
public class DefaultWorkflowItemValidator implements Validator<CMSItemModel>
{
	private CMSWorkflowService cmsWorkflowService;

	/**
	 * @{@inheritDoc}
	 *
	 * It throws exception if the item can not be edited by current user.
	 */
	@Override
	public void validate(final CMSItemModel item)
	{
		final boolean itemIsEditable = getCmsWorkflowService().isItemEditableBySessionUser(item);
		if (!itemIsEditable) {
			throwNotEditableInCurrentWorkflowContextException();
		}
	}

	/**
	 * Throws a localized {@link NonEditableItemInWorkflowException} exception.
	 */
	protected void throwNotEditableInCurrentWorkflowContextException()
	{
		final String errorMsg = Localization.getLocalizedString(ITEM_IN_WORKFLOW_NOT_EDITABLE_BY_CURRENT_USER);
		throw new NonEditableItemInWorkflowException(errorMsg);
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
}
