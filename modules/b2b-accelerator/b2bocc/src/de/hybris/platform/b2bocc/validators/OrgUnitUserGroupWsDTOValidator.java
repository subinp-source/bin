/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupWsDTO;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class OrgUnitUserGroupWsDTOValidator implements Validator
{
	private static final String ORG_UNIT_USERGROUP_UID_INVALID_ERROR_CODE = "usergroup.uid.invalid";
	private static final String ORG_UNIT_USERGROUP_NAME_INVALID_ERROR_CODE = "usergroup.name.invalid";
	private static final String ORG_UNIT_USERGROUP_PARENTUNIT_INVALID_ERROR_CODE = "usergroup.unit.invalid";

	private static final int ORG_UNIT_USERGROUP_UID_MIN_LENGTH = 1;
	private static final int ORG_UNIT_USERGROUP_UID_MAX_LENGTH = 255;

	private static final int ORG_UNIT_USERGROUP_NAME_MIN_LENGTH = 1;
	private static final int ORG_UNIT_USERGROUP_NAME_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return OrgUnitUserGroupWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final OrgUnitUserGroupWsDTO orgUnitUserGroup = (OrgUnitUserGroupWsDTO) target;

		if (orgUnitUserGroup.getUid() == null)
		{
			errors.rejectValue("uid", ORG_UNIT_USERGROUP_UID_INVALID_ERROR_CODE);
		}
		else
		{
			if (orgUnitUserGroup.getUid().length() < ORG_UNIT_USERGROUP_UID_MIN_LENGTH
					|| orgUnitUserGroup.getUid().length() > ORG_UNIT_USERGROUP_UID_MAX_LENGTH)
			{
				errors.rejectValue("uid", ORG_UNIT_USERGROUP_UID_INVALID_ERROR_CODE);
			}
		}

		if (orgUnitUserGroup.getName() == null)
		{
			errors.rejectValue("name", ORG_UNIT_USERGROUP_NAME_INVALID_ERROR_CODE);
		}
		else
		{
			if (orgUnitUserGroup.getName().length() < ORG_UNIT_USERGROUP_NAME_MIN_LENGTH
					|| orgUnitUserGroup.getName().length() > ORG_UNIT_USERGROUP_NAME_MAX_LENGTH)
			{
				errors.rejectValue("name", ORG_UNIT_USERGROUP_NAME_INVALID_ERROR_CODE);
			}
		}

		if(orgUnitUserGroup.getOrgUnit() == null) {
			errors.rejectValue("orgUnit", ORG_UNIT_USERGROUP_PARENTUNIT_INVALID_ERROR_CODE);
		}
	}
}
