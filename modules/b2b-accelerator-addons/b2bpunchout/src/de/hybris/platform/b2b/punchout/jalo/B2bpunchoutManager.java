/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.jalo;

import de.hybris.platform.b2b.punchout.constants.B2bpunchoutConstants;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import org.apache.log4j.Logger;

public class B2bpunchoutManager extends GeneratedB2bpunchoutManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( B2bpunchoutManager.class.getName() );
	
	public static final B2bpunchoutManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (B2bpunchoutManager) em.getExtension(B2bpunchoutConstants.EXTENSIONNAME);
	}
	
}
