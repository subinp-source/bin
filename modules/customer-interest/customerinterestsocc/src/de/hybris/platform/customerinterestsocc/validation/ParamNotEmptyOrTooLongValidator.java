/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsocc.validation;

import de.hybris.platform.customerinterestsocc.constants.ErrorMessageConstants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/*
 * validate if the field is empty or too long
 */
public class ParamNotEmptyOrTooLongValidator implements Validator
{
	private final String fieldName;
	private final int maxLength;

	public ParamNotEmptyOrTooLongValidator(final String fieldName, final int maxLength)
	{
		this.fieldName = fieldName;
		this.maxLength = maxLength;
	}

	@Override
	public boolean supports(final Class<?> aClass)
	{
		return String.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		Assert.notNull(errors, "Errors object must not be null.");
		final String fieldValue = (String) object;
		if (StringUtils.isBlank(fieldValue) || StringUtils.length(fieldValue) > this.maxLength)
		{
			errors.reject(ErrorMessageConstants.FIELD_REQUIRED_TOOLONG, new String[]
			{ fieldName, String.valueOf(maxLength) }, ErrorMessageConstants.FIELD_REQUIRED_TOOLONG_MESSAGE);
		}
	}

	public void validate(final Object object, final Errors errors, boolean required)
	{
		Assert.notNull(errors, "Errors object must not be null.");
		final String fieldValue = (String) object;
		if (!required)
		{
			if (StringUtils.length(fieldValue) > this.maxLength)
			{
				errors.reject(ErrorMessageConstants.FIELD_TOOLONG, new String[]
				{ fieldName, String.valueOf(maxLength) }, ErrorMessageConstants.FIELD_TOOLONG_MESSAGE);
			}
		}
		else {
			validate(object, errors);
			 }
	}

	protected String getFieldName()
	{
		return fieldName;
	}


	protected int getMaxLength()
	{
		return maxLength;
	}
}