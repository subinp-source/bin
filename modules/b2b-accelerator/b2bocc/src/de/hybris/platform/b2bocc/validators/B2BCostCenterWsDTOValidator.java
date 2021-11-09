/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class B2BCostCenterWsDTOValidator implements Validator
{

	private static final String FIELD_REQUIRED_ERROR_CODE = "field.required";

	private static final int COST_CENTER_CODE_MIN_LENGTH = 1;
	private static final int COST_CENTER_CODE_MAX_LENGTH = 255;

	private static final int COST_CENTER_NAME_MIN_LENGTH = 1;
	private static final int COST_CENTER_NAME_MAX_LENGTH = 255;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return B2BCostCenterWsDTO.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final B2BCostCenterWsDTO costCenter = (B2BCostCenterWsDTO) target;
		validateCostCenterCode(costCenter.getCode(), errors);
		validateCostCenterName(costCenter.getName(), errors);
		validateCostCenterUnit(costCenter.getUnit(), errors);
		validateCostCenterCurrency(costCenter.getCurrency(), errors);
	}

	private void validateCostCenterCode(final String code, final Errors errors)
	{
		if (code == null || code.length() < COST_CENTER_CODE_MIN_LENGTH || code.length() > COST_CENTER_CODE_MAX_LENGTH)
		{
			errors.rejectValue("code", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateCostCenterName(final String name, final Errors errors)
	{
		if (name == null || name.length() < COST_CENTER_NAME_MIN_LENGTH || name.length() > COST_CENTER_NAME_MAX_LENGTH)
		{
			errors.rejectValue("name", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateCostCenterUnit(final B2BUnitWsDTO unit, final Errors errors)
	{
		if (unit == null)
		{
			errors.rejectValue("unit", FIELD_REQUIRED_ERROR_CODE);
		}
		else if (unit.getUid() == null)
		{
			errors.rejectValue("unit.uid", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateCostCenterCurrency(final CurrencyWsDTO currency, final Errors errors)
	{
		if (currency == null)
		{
			errors.rejectValue("currency", FIELD_REQUIRED_ERROR_CODE);
		}
		else if (currency.getIsocode() == null)
		{
			errors.rejectValue("currency.isocode", FIELD_REQUIRED_ERROR_CODE);
		}
	}
}
