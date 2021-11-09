/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.core.Registry;

/**
 * This is the extension manager of the CommerceServices extension.
 */
public class CommerceServicesManager extends GeneratedCommerceServicesManager
{
	/**
	 * Get the valid instance of this manager.
	 *
	 * @return the current instance of this manager
	 */
	public static CommerceServicesManager getInstance()
	{
		return (CommerceServicesManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(CommerceServicesConstants.EXTENSIONNAME);
	}
}
