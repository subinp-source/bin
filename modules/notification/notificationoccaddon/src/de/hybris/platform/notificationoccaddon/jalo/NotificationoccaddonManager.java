/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationoccaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.notificationoccaddon.constants.NotificationoccaddonConstants;
import org.apache.log4j.Logger;

public class NotificationoccaddonManager extends GeneratedNotificationoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NotificationoccaddonManager.class.getName() );
	
	public static final NotificationoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NotificationoccaddonManager) em.getExtension(NotificationoccaddonConstants.EXTENSIONNAME);
	}
	
}
