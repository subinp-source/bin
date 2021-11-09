/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentocc.jalo;

import de.hybris.platform.chinesepaymentocc.constants.ChinesepaymentoccConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class ChinesepaymentoccManager extends GeneratedChinesepaymentoccManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( ChinesepaymentoccManager.class.getName() );
	
	public static final ChinesepaymentoccManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (ChinesepaymentoccManager) em.getExtension(ChinesepaymentoccConstants.EXTENSIONNAME);
	}
	
}
