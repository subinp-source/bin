/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import java.util.Map;


/**
 * Filter to filter out rows not matching a specified filter expression.
 */
public interface ImpexRowFilter
{
	/**
	 * Evaluate a single row and return a false, if the row should be filtered.
	 * 
	 * @param row
	 *           CSV row containing column indexes and values
	 * @return false, if the row should not be converted
	 */
	boolean filter(Map<Integer, String> row);
}
