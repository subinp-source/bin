/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.services;



/**
 * Configuration service used to retrieve the punchout specific setting values.
 */
public interface PunchOutConfigurationService
{

	/**
	 * @return the punchout URL to be used by the punchout provider to authenticate and browse the catalog
	 */
	String getPunchOutLoginUrl();


	/**
	 * @return gets the default cost center.
	 */
	String getDefaultCostCenter();

}
