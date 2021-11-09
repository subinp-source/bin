/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class B2BUnitWsDTOValidator implements Validator
{
	private static final String UNIT_UID_INVALID_ERROR_CODE = "unit.uid.invalid";
	private static final String UNIT_NAME_INVALID_ERROR_CODE = "unit.name.invalid";

	private static final int UNIT_UID_MIN_LENGTH = 1;
	private static final int UNIT_UID_MAX_LENGTH = 255;

	private static final int UNIT_NAME_MIN_LENGTH = 1;
	private static final int UNIT_NAME_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return B2BUnitWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final B2BUnitWsDTO orgUnit = (B2BUnitWsDTO) target;

		if (orgUnit.getUid() == null)
		{
			errors.rejectValue("uid", UNIT_UID_INVALID_ERROR_CODE);
		}
		else
		{
			if (orgUnit.getUid().length() < UNIT_UID_MIN_LENGTH || orgUnit.getUid().length() > UNIT_UID_MAX_LENGTH)
			{
				errors.rejectValue("uid", UNIT_UID_INVALID_ERROR_CODE);
			}
		}

		if (orgUnit.getName() == null)
		{
			errors.rejectValue("name", UNIT_NAME_INVALID_ERROR_CODE);
		}
		else
		{
			if (orgUnit.getName().length() < UNIT_NAME_MIN_LENGTH || orgUnit.getName().length() > UNIT_NAME_MAX_LENGTH)
			{
				errors.rejectValue("name", UNIT_NAME_INVALID_ERROR_CODE);
			}
		}
	}
}
