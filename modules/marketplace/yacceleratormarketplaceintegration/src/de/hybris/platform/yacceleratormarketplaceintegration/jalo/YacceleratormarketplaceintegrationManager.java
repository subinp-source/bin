/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.yacceleratormarketplaceintegration.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.yacceleratormarketplaceintegration.constants.YacceleratormarketplaceintegrationConstants;
import org.apache.log4j.Logger;

public class YacceleratormarketplaceintegrationManager extends GeneratedYacceleratormarketplaceintegrationManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( YacceleratormarketplaceintegrationManager.class.getName() );
	
	public static final YacceleratormarketplaceintegrationManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (YacceleratormarketplaceintegrationManager) em.getExtension(YacceleratormarketplaceintegrationConstants.EXTENSIONNAME);
	}
	
}
