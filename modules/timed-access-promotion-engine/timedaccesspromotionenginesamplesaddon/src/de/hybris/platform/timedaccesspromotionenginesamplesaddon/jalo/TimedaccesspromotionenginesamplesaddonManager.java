/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionenginesamplesaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.timedaccesspromotionenginesamplesaddon.constants.TimedaccesspromotionenginesamplesaddonConstants;
import org.apache.log4j.Logger;

public class TimedaccesspromotionenginesamplesaddonManager extends GeneratedTimedaccesspromotionenginesamplesaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( TimedaccesspromotionenginesamplesaddonManager.class.getName() );
	
	public static final TimedaccesspromotionenginesamplesaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (TimedaccesspromotionenginesamplesaddonManager) em.getExtension(TimedaccesspromotionenginesamplesaddonConstants.EXTENSIONNAME);
	}
	
}
