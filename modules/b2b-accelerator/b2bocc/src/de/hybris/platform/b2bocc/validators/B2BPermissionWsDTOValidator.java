/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2b.enums.B2BPermissionTypeEnum;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionTypeWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class B2BPermissionWsDTOValidator implements Validator
{

	private static final String FIELD_REQUIRED_ERROR_CODE = "field.required";
	private static final String FIELD_INVALID_ERROR_CODE = "field.invalid";
	private static final String THRESHOLD_VALUE_ERROR_CODE = "company.managePermissions.threshold.value.invalid";
	private static final Integer PERMISSION_CODE_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return B2BPermissionData.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final B2BPermissionWsDTO permission = (B2BPermissionWsDTO) target;
		final B2BPermissionTypeWsDTO permissionType = permission.getOrderApprovalPermissionType();

		validateCode(permission.getCode(), errors);

		if (permission.getOrgUnit() == null)
		{
			errors.rejectValue("orgUnit", FIELD_REQUIRED_ERROR_CODE);
		}

		if (permissionType == null)
		{
			errors.rejectValue("orderApprovalPermissionType", FIELD_REQUIRED_ERROR_CODE);
		}
		else
		{
			if (!B2BPermissionTypeEnum.B2BBUDGETEXCEEDEDPERMISSION.equals(B2BPermissionTypeEnum.valueOf(permissionType.getCode())))
			{
				if (permission.getCurrency() == null)
				{
					errors.rejectValue("currency", FIELD_REQUIRED_ERROR_CODE);
				}

				final Double threshold = permission.getThreshold();
				if (threshold == null)
				{
					errors.rejectValue("threshold", FIELD_REQUIRED_ERROR_CODE);
				}
				else if (threshold < 0D)
				{
					errors.rejectValue("threshold", THRESHOLD_VALUE_ERROR_CODE);
				}
			}

			if (B2BPermissionTypeEnum.B2BORDERTHRESHOLDTIMESPANPERMISSION
					.equals(B2BPermissionTypeEnum.valueOf(permissionType.getCode())) && permission.getPeriodRange() == null)
			{
				errors.rejectValue("periodRange", FIELD_REQUIRED_ERROR_CODE);
			}
		}
	}

	private void validateCode(final String permissionCode, final Errors errors)
	{
		if (permissionCode == null || StringUtils.isBlank(permissionCode))
		{
			errors.rejectValue("code", FIELD_REQUIRED_ERROR_CODE);
		}
		else if(permissionCode.length() > PERMISSION_CODE_MAX_LENGTH)
		{
			errors.rejectValue("code", FIELD_INVALID_ERROR_CODE, new Object[]{permissionCode}, FIELD_INVALID_ERROR_CODE);
		}
	}
}
