/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundlecockpits.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.subscriptionbundlecockpits.constants.SubscriptionbundlecockpitsConstants;
import org.apache.log4j.Logger;

public class SubscriptionbundlecockpitsManager extends GeneratedSubscriptionbundlecockpitsManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SubscriptionbundlecockpitsManager.class.getName() );
	
	public static final SubscriptionbundlecockpitsManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SubscriptionbundlecockpitsManager) em.getExtension(SubscriptionbundlecockpitsConstants.EXTENSIONNAME);
	}
	
}
