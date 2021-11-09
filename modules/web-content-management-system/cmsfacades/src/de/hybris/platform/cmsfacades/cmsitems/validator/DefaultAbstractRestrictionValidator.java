/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.validator;

import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmsfacades.common.function.Validator;
import de.hybris.platform.cmsfacades.common.validator.ValidationErrorsProvider;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of the validator for {@link AbstractRestrictionModel}
 */
public class DefaultAbstractRestrictionValidator implements Validator<AbstractRestrictionModel>
{
	
	private ValidationErrorsProvider validationErrorsProvider;
	
	@Override
	public void validate(final AbstractRestrictionModel validatee)
	{
		// intentionally left empty
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
}
