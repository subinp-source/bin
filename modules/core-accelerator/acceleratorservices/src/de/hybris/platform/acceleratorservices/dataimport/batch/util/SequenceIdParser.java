/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.util;

import de.hybris.platform.acceleratorservices.util.RegexParser;

import java.io.File;

import org.springframework.util.Assert;


/**
 * Strategy to extract the sequenceId from a file name.
 */
public class SequenceIdParser
{
	private RegexParser parser;

	public Long getSequenceId(final File file)
	{
		Long result = null;
		Assert.notNull(file);
		final String fileName = file.getName();
		final String part = parser.parse(fileName, 1);
		if (part != null)
		{
			result = Long.valueOf(part);
		}
		else
		{
			throw new IllegalArgumentException("missing sequenceId in " + fileName);
		}
		return result;
	}

	/**
	 * @param parser
	 *           the parser to set
	 */
	public void setParser(final RegexParser parser)
	{
		this.parser = parser;
	}

	/**
	 * @return the parser
	 */
	protected RegexParser getParser()
	{
		return parser;
	}
}
