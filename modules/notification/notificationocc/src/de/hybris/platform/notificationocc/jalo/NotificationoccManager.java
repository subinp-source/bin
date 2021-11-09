/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationocc.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.notificationocc.constants.NotificationoccConstants;
import org.apache.log4j.Logger;

public class NotificationoccManager extends GeneratedNotificationoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NotificationoccManager.class.getName() );
	
	public static final NotificationoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NotificationoccManager) em.getExtension(NotificationoccConstants.EXTENSIONNAME);
	}
	
}
