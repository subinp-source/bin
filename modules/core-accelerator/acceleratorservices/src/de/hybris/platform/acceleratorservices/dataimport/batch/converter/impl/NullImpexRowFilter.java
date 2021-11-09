/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter.impl;

import de.hybris.platform.acceleratorservices.dataimport.batch.converter.ImpexRowFilter;

import java.util.Map;


/**
 * Filter implementation that does not filter any rows.
 */
public class NullImpexRowFilter implements ImpexRowFilter
{

	@Override
	public boolean filter(final Map<Integer, String> row)
	{
		return true;
	}

}
