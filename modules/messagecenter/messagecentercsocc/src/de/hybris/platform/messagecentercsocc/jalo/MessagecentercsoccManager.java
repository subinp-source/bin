/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsocc.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.messagecentercsocc.constants.MessagecentercsoccConstants;
import org.apache.log4j.Logger;

public class MessagecentercsoccManager extends GeneratedMessagecentercsoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MessagecentercsoccManager.class.getName() );
	
	public static final MessagecentercsoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MessagecentercsoccManager) em.getExtension(MessagecentercsoccConstants.EXTENSIONNAME);
	}
	
}
