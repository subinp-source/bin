/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsoccaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.messagecentercsoccaddon.constants.MessagecentercsoccaddonConstants;
import org.apache.log4j.Logger;

public class MessagecentercsoccaddonManager extends GeneratedMessagecentercsoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MessagecentercsoccaddonManager.class.getName() );
	
	public static final MessagecentercsoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MessagecentercsoccaddonManager) em.getExtension(MessagecentercsoccaddonConstants.EXTENSIONNAME);
	}
	
}
