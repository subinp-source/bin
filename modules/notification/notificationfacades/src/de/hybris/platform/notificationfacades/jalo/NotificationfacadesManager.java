/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.notificationfacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.notificationfacades.constants.NotificationfacadesConstants;
import org.apache.log4j.Logger;

public class NotificationfacadesManager extends GeneratedNotificationfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( NotificationfacadesManager.class.getName() );
	
	public static final NotificationfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (NotificationfacadesManager) em.getExtension(NotificationfacadesConstants.EXTENSIONNAME);
	}
	
}
