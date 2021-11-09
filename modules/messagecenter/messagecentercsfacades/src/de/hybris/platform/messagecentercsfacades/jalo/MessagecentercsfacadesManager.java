/*
 *  
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.messagecentercsfacades.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.messagecentercsfacades.constants.MessagecentercsfacadesConstants;
import org.apache.log4j.Logger;

public class MessagecentercsfacadesManager extends GeneratedMessagecentercsfacadesManager
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger( MessagecentercsfacadesManager.class.getName() );
	
	public static final MessagecentercsfacadesManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (MessagecentercsfacadesManager) em.getExtension(MessagecentercsfacadesConstants.EXTENSIONNAME);
	}
	
}
