/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.OrgCustomerCreationWsDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class OrgCustomerCreationWsDTOValidator implements Validator
{
	private static final String EMAIL_INVALID_ERROR_CODE = "profile.email.invalid";
	private static final String TITLE_INVALID_ERROR_CODE = "profile.title.invalid";
	private static final String FIRST_NAME_INVALID_ERROR_CODE = "profile.firstName.invalid";
	private static final String LAST_NAME_INVALID_ERROR_CODE = "profile.lastName.invalid";
	private static final String ORG_UNIT_INVALID_ERROR_CODE = "profile.orgUnit.invalid";

	private static final int EMAIL_MAX_LENGTH = 255;
	private static final int TITLE_MAX_LENGTH = 255;
	private static final int FIRST_NAME_MAX_LENGTH = 255;
	private static final int LAST_NAME_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return OrgCustomerCreationWsDTO.class.equals(clazz);
	}

	@SuppressWarnings("squid:S1541")
	@Override
	public void validate(final Object target, final Errors errors)
	{
		final OrgCustomerCreationWsDTO orgCustomerCreation = (OrgCustomerCreationWsDTO) target;

		if (StringUtils.isBlank(orgCustomerCreation.getEmail())
				|| StringUtils.length(orgCustomerCreation.getEmail()) > EMAIL_MAX_LENGTH || !EmailValidator.getInstance()
				.isValid(orgCustomerCreation.getEmail()))
		{
			errors.rejectValue("email", EMAIL_INVALID_ERROR_CODE);
		}

		if (StringUtils.length(orgCustomerCreation.getTitleCode()) > TITLE_MAX_LENGTH)
		{
			errors.rejectValue("titleCode", TITLE_INVALID_ERROR_CODE);
		}

		if (StringUtils.isBlank(orgCustomerCreation.getFirstName())
				|| StringUtils.length(orgCustomerCreation.getFirstName()) > FIRST_NAME_MAX_LENGTH)
		{
			errors.rejectValue("firstName", FIRST_NAME_INVALID_ERROR_CODE);
		}

		if (StringUtils.isBlank(orgCustomerCreation.getLastName())
				|| StringUtils.length(orgCustomerCreation.getLastName()) > LAST_NAME_MAX_LENGTH)
		{
			errors.rejectValue("lastName", LAST_NAME_INVALID_ERROR_CODE);
		}

		if (orgCustomerCreation.getOrgUnit() == null || StringUtils.isBlank(orgCustomerCreation.getOrgUnit().getUid()))
		{
			errors.rejectValue("orgUnit", ORG_UNIT_INVALID_ERROR_CODE);
		}
	}
}
