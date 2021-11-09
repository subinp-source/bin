/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsservices.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.messagecentercsservices.constants.MessagecentercsservicesConstants;
import org.apache.log4j.Logger;

public class MessagecentercsservicesManager extends GeneratedMessagecentercsservicesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MessagecentercsservicesManager.class.getName() );
	
	public static final MessagecentercsservicesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MessagecentercsservicesManager) em.getExtension(MessagecentercsservicesConstants.EXTENSIONNAME);
	}
	
}
