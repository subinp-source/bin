/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.mycompany.BudgetWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;


public class BudgetWsDTOValidator implements Validator
{

	private static final String FIELD_REQUIRED_ERROR_CODE = "field.required";

	private static final int BUDGET_CODE_MIN_LENGTH = 1;
	private static final int BUDGET_CODE_MAX_LENGTH = 255;

	private static final int BUDGET_NAME_MIN_LENGTH = 1;
	private static final int BUDGET_NAME_MAX_LENGTH = 255;

	@Override
	public boolean supports(Class<?> clazz)
	{
		return BudgetWsDTOValidator.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors)
	{
		final BudgetWsDTO budget = (BudgetWsDTO) target;
		validateBudgetCode(budget.getCode(), errors);
		validateBudgetName(budget.getName(), errors);
		validateBudgetOrgUnit(budget.getOrgUnit(), errors);
		validateBudgetCurrency(budget.getCurrency(), errors);
		validateBudgetStartDate(budget.getStartDate(), errors);
		validateBudgetEndDate(budget.getEndDate(), errors);
	}

	private void validateBudgetCode(final String code, final Errors errors)
	{
		if (code == null || code.length() < BUDGET_CODE_MIN_LENGTH || code.length() > BUDGET_CODE_MAX_LENGTH)
		{
			errors.rejectValue("code", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateBudgetName(final String name, final Errors errors)
	{
		if (name == null || name.length() < BUDGET_NAME_MIN_LENGTH || name.length() > BUDGET_NAME_MAX_LENGTH)
		{
			errors.rejectValue("name", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateBudgetOrgUnit(final B2BUnitWsDTO orgUnit, final Errors errors)
	{
		if (orgUnit == null)
		{
			errors.rejectValue("orgUnit", FIELD_REQUIRED_ERROR_CODE);
		}
		else if (orgUnit.getUid() == null)
		{
			errors.rejectValue("orgUnit.uid", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateBudgetCurrency(final CurrencyWsDTO currency, final Errors errors)
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

	private void validateBudgetStartDate(final Date startDate, final Errors errors)
	{
		if (startDate == null)
		{
			errors.rejectValue("startDate", FIELD_REQUIRED_ERROR_CODE);
		}
	}

	private void validateBudgetEndDate(final Date endDate, final Errors errors)
	{
		if (endDate == null)
		{
			errors.rejectValue("endDate", FIELD_REQUIRED_ERROR_CODE);
		}
	}
}
