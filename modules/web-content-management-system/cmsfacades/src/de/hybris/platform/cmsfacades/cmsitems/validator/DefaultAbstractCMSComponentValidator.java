/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the validator for {@link AbstractCMSComponentModel}
 */
public class DefaultAbstractCMSComponentValidator implements Validator<AbstractCMSComponentModel>
{
	private Validator<CMSItemModel> workflowItemValidator;
	private ModelService modelService;

	private ValidationErrorsProvider validationErrorsProvider;
	
	@Override
	public void validate(final AbstractCMSComponentModel componentModel)
	{
		if (!getModelService().isNew(componentModel))
		{
			getWorkflowItemValidator().validate(componentModel);
		}
	}

	protected ValidationErrorsProvider getValidationErrorsProvider()
	{
		return validationErrorsProvider;
	}

	@Required
	public void setValidationErrorsProvider(final ValidationErrorsProvider validationErrorsProvider)
	{
		this.validationErrorsProvider = validationErrorsProvider;
	}

	protected Validator<CMSItemModel> getWorkflowItemValidator()
	{
		return workflowItemValidator;
	}

	@Required
	public void setWorkflowItemValidator(
			final Validator<CMSItemModel> workflowItemValidator)
	{
		this.workflowItemValidator = workflowItemValidator;
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
}
