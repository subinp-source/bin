/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.notificationservices.constants.NotificationservicesConstants;
import org.apache.log4j.Logger;

public class NotificationservicesManager extends GeneratedNotificationservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NotificationservicesManager.class.getName() );
	
	public static final NotificationservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NotificationservicesManager) em.getExtension(NotificationservicesConstants.EXTENSIONNAME);
	}
	
}
