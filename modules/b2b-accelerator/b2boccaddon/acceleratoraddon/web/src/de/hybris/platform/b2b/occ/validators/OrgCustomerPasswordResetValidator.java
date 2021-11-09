/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.OrgCustomerModificationWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class OrgCustomerPasswordResetValidator implements Validator
{
	private static final String PASSWORD_INVALID_ERROR_CODE = "updatePwd.pwd.invalid";

	private static final int PASSWORD_MIN_LENGTH = 6;
	private static final int PASSWORD_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return OrgCustomerModificationWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final OrgCustomerModificationWsDTO orgCustomerModification = (OrgCustomerModificationWsDTO) target;

		if (StringUtils.length(orgCustomerModification.getPassword()) < PASSWORD_MIN_LENGTH
				|| StringUtils.length(orgCustomerModification.getPassword()) > PASSWORD_MAX_LENGTH)
		{
			errors.rejectValue("password", PASSWORD_INVALID_ERROR_CODE);
		}
	}
}
