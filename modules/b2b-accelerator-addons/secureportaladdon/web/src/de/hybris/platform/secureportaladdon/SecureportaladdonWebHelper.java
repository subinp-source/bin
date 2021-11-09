/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.secureportaladdon;

import org.apache.log4j.Logger;


/**
 * Simple test class to demonstrate how to include utility classes to your webmodule.
 */
public class SecureportaladdonWebHelper // NOSONAR
{
	/** Edit the local|project.properties to change logging behavior (properties log4j.*). */
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SecureportaladdonWebHelper.class.getName());

	public static final String getTestOutput()
	{
		return "testoutput";
	}
}
