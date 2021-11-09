/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspwechatpay.jalo;

import de.hybris.platform.chinesepspwechatpay.constants.ChinesepspwechatpaymentaddonConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepspwechatpaymentaddonManager extends GeneratedChinesepspwechatpaymentaddonManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepspwechatpaymentaddonManager.class.getName() );
	
	public static final ChinesepspwechatpaymentaddonManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepspwechatpaymentaddonManager) em.getExtension(ChinesepspwechatpaymentaddonConstants.EXTENSIONNAME);
	}
	
}
