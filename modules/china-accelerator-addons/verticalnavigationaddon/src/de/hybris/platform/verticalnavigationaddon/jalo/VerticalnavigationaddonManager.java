/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.verticalnavigationaddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.verticalnavigationaddon.constants.VerticalnavigationaddonConstants;
import org.apache.log4j.Logger;

public class VerticalnavigationaddonManager extends GeneratedVerticalnavigationaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( VerticalnavigationaddonManager.class.getName() );
	
	public static final VerticalnavigationaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (VerticalnavigationaddonManager) em.getExtension(VerticalnavigationaddonConstants.EXTENSIONNAME);
	}
	
}
