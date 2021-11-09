/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.jalo;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.core.Registry;


/**
 * This is the extension manager of the B2bcommerce extension.
 */
public class B2BCommerceManager extends GeneratedB2BCommerceManager
{
	/**
	 * Get the valid instance of this manager.
	 *
	 * @return the current instance of this manager
	 */
	public static B2BCommerceManager getInstance()
	{
		return (B2BCommerceManager) Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
				.getExtension(B2BConstants.EXTENSIONNAME);
	}
}
