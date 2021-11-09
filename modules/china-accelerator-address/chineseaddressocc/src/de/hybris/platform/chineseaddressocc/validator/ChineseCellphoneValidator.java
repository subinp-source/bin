/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.chineseaddressocc.validator;

import de.hybris.platform.addressfacades.address.impl.ChineseAddressFacade;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validates the cellphone in Chinese address.
 */
public class ChineseCellphoneValidator implements Validator
{
	private final ChineseAddressFacade chineseAddressFacade;

	private static final String CELLPHONE_INVALID = "cellphone.invalid";
	private static final String CELLPHONE = "cellphone";

	public ChineseCellphoneValidator(final ChineseAddressFacade chineseAddressFacade)
	{
		this.chineseAddressFacade = chineseAddressFacade;
	}

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return true;
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null");

		final String fieldValue = (String) errors.getFieldValue(CELLPHONE);
		if (getChineseAddressFacade().isInvalidCellphone(fieldValue))
		{
			errors.rejectValue(CELLPHONE, CELLPHONE_INVALID, new String[]
			{ CELLPHONE }, null);
		}
	}

	protected ChineseAddressFacade getChineseAddressFacade()
	{
		return chineseAddressFacade;
	}

}
