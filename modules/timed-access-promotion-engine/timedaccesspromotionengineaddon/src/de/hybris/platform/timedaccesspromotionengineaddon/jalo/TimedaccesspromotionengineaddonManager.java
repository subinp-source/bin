/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionengineaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionengineaddon.constants.TimedaccesspromotionengineaddonConstants;
import org.apache.log4j.Logger;

public class TimedaccesspromotionengineaddonManager extends GeneratedTimedaccesspromotionengineaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionengineaddonManager.class.getName() );
	
	public static final TimedaccesspromotionengineaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionengineaddonManager) em.getExtension(TimedaccesspromotionengineaddonConstants.EXTENSIONNAME);
	}
	
}
