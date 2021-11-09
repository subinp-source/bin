/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bapprovalprocessfacades.company;

import java.util.Map;


/**
 * Provides a facade for getting the processes for OrderApproval
 */
public interface B2BApprovalProcessFacade
{

	/**
	 * Get a collection of available business processes for OrderApproval
	 *
	 * @return A map where the key is process code and value is process name based on the current session locale
	 */
	Map<String, String> getProcesses();

}
