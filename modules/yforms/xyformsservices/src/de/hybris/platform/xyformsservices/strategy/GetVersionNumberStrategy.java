/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.xyformsservices.strategy;



/**
 * Strategy for getting a new form number based on some parameters.
 */
public interface GetVersionNumberStrategy
{
	/**
	 * Returns the next version number available for a form definition indentified by applicationId and formId
	 *
	 * @param applicationId
	 * @param formId
	 */
	public int execute(final String applicationId, final String formId);
}
