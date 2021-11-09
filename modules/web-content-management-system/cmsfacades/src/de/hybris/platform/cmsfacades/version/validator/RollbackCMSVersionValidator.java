/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.version.validator;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_UID;

import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;
import de.hybris.platform.cmsfacades.data.CMSVersionData;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * Validates fields of {@link CMSVersionData} for a rollback operation
 */
public class RollbackCMSVersionValidator implements Validator
{

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return CMSVersionData.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object obj, final Errors errors)
	{
		ValidationUtils.rejectIfEmpty(errors, FIELD_UID, CmsfacadesConstants.FIELD_REQUIRED);
	}

}
