/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundlefacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.subscriptionbundlefacades.constants.SubscriptionbundlefacadesConstants;
import org.apache.log4j.Logger;

public class SubscriptionbundlefacadesManager extends GeneratedSubscriptionbundlefacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SubscriptionbundlefacadesManager.class.getName() );
	
	public static final SubscriptionbundlefacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SubscriptionbundlefacadesManager) em.getExtension(SubscriptionbundlefacadesConstants.EXTENSIONNAME);
	}
	
}
