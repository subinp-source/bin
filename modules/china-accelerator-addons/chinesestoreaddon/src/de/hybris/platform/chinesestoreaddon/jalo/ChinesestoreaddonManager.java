/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesestoreaddon.jalo;

import de.hybris.platform.chinesestoreaddon.constants.ChinesestoreaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesestoreaddonManager extends GeneratedChinesestoreaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesestoreaddonManager.class.getName() );
	
	public static final ChinesestoreaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesestoreaddonManager) em.getExtension(ChinesestoreaddonConstants.EXTENSIONNAME);
	}
	
}
