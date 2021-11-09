/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundleservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.subscriptionbundleservices.constants.SubscriptionbundleservicesConstants;
import org.apache.log4j.Logger;

public class SubscriptionbundleservicesManager extends GeneratedSubscriptionbundleservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( SubscriptionbundleservicesManager.class.getName() );
	
	public static final SubscriptionbundleservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (SubscriptionbundleservicesManager) em.getExtension(SubscriptionbundleservicesConstants.EXTENSIONNAME);
	}
	
}
