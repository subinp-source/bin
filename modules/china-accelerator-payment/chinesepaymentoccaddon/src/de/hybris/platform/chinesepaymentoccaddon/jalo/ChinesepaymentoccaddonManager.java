/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentoccaddon.jalo;

import de.hybris.platform.chinesepaymentoccaddon.constants.ChinesepaymentoccaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepaymentoccaddonManager extends GeneratedChinesepaymentoccaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepaymentoccaddonManager.class.getName() );
	
	public static final ChinesepaymentoccaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepaymentoccaddonManager) em.getExtension(ChinesepaymentoccaddonConstants.EXTENSIONNAME);
	}
	
}
