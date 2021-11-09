/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.notificationaddon.constants.NotificationaddonConstants;
import org.apache.log4j.Logger;

public class NotificationaddonManager extends GeneratedNotificationaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NotificationaddonManager.class.getName() );
	
	public static final NotificationaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NotificationaddonManager) em.getExtension(NotificationaddonConstants.EXTENSIONNAME);
	}
	
}
