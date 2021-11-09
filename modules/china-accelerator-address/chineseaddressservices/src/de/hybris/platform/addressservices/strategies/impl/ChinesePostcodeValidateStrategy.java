/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies.impl;

import de.hybris.platform.addressservices.strategies.PostcodeValidateStrategy;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * A implementation for validating Chinese post code
 */
public class ChinesePostcodeValidateStrategy implements PostcodeValidateStrategy
{

	private String regex;

	@Override
	public boolean validate(final String postcode)
	{

		if (StringUtils.isNotBlank(regex))
		{
			return Pattern.compile(regex).matcher(postcode).matches();
		}

		return true;
	}

	protected String getRegex()
	{
		return regex;
	}

	@Required
	public void setRegex(final String regex)
	{
		this.regex = regex;
	}
}
