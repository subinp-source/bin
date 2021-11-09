/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.translators.ConvertPlaintextToEncodedUserPasswordTranslator;
import de.hybris.platform.jalo.Item;

import org.apache.commons.lang.RandomStringUtils;


/**
 * This translator adds some random text to the orginal password and passes this value to
 * ConvertPlaintestToEncodedUserPasswordTranslator.
 */
public class RandomPasswordTranslator extends ConvertPlaintextToEncodedUserPasswordTranslator
{
	private static final int RANDOM_STRING_LENGTH = 6;

	@Override
	public void performImport(final String cellValue, final Item processedItem) throws ImpExException
	{
		final String newCellValue = (cellValue == null ? "" : cellValue.trim()) +
				RandomStringUtils.randomAlphanumeric(RANDOM_STRING_LENGTH);
		super.performImport(newCellValue, processedItem);
	}
}
