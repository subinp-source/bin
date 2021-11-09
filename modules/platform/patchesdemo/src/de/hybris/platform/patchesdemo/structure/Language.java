/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.structure;

import de.hybris.platform.patches.organisation.ImportLanguage;


/**
 * Example of languages enumeration used in import process.
 */
public enum Language implements ImportLanguage
{

	EN_US("en_US"), EN_CA("en_CA"), FR_CA("fr_CA"), FR_FR("fr_FR"), DE_DE("de_DE");

	private String code;

	Language(final String code)
	{
		this.code = code;
	}

	@Override
	public String getCode()
	{
		return this.code;
	}

}
