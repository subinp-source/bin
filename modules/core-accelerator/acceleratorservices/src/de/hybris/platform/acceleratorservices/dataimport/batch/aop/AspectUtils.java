/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.aop;

import de.hybris.platform.acceleratorservices.dataimport.batch.BatchHeader;

import org.apache.commons.lang.ArrayUtils;


/**
 * Utilities for batch aspect implementations.
 */
public final class AspectUtils
{
	private AspectUtils()
	{
		//empty
	}

	/**
	 * Retrieves a header from the given arguments.
	 * 
	 * @param args
	 * @return header
	 */
	public static BatchHeader getHeader(final Object[] args)
	{
		BatchHeader header = null;
		if (!ArrayUtils.isEmpty(args))
		{
			final Object arg = args[0];
			if (arg instanceof BatchHeader)
			{
				header = (BatchHeader) arg;
			}
		}
		return header;
	}
}
