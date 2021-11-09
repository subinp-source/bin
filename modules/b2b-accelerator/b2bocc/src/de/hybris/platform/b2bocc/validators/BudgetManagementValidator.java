/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.hybris.platform.b2bcommercefacades.company.data.B2BBudgetData;


public class BudgetManagementValidator implements Validator
{
	@Override
	public boolean supports(final Class<?> clazz)
	{
		return B2BBudgetData.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		final B2BBudgetData b2BBudgetData = (B2BBudgetData) target;

		if (b2BBudgetData == null)
		{
			errors.reject("error.budgetcode.notfound");
		}
	}
}
