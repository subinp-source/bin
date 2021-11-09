/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineoccaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionengineoccaddon.constants.TimedaccesspromotionengineoccaddonConstants;
import org.apache.log4j.Logger;

public class TimedaccesspromotionengineoccaddonManager extends GeneratedTimedaccesspromotionengineoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionengineoccaddonManager.class.getName() );
	
	public static final TimedaccesspromotionengineoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionengineoccaddonManager) em.getExtension(TimedaccesspromotionengineoccaddonConstants.EXTENSIONNAME);
	}
	
}
