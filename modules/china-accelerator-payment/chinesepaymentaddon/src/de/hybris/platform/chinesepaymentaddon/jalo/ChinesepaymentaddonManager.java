/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentaddon.jalo;

import de.hybris.platform.chinesepaymentaddon.constants.ChinesepaymentaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepaymentaddonManager extends GeneratedChinesepaymentaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepaymentaddonManager.class.getName() );
	
	public static final ChinesepaymentaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepaymentaddonManager) em.getExtension(ChinesepaymentaddonConstants.EXTENSIONNAME);
	}
	
}
