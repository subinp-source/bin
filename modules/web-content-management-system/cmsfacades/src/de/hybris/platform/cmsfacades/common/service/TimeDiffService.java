/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.service;

import java.util.Date;


/**
 * Service to calculate a time difference.
 */
public interface TimeDiffService
{
	/**
	 * Calculates a time difference in milliseconds between current date and provided date.
	 * @param date the {@link Date} to calculate the difference with.
	 * @return the difference in milliseconds.
	 */
	Long difference(Date date);
}
