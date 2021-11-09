/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.jalo.actions;

import de.hybris.platform.jalo.SessionContext;

import org.apache.log4j.Logger;


public class SimpleCMSAction extends GeneratedSimpleCMSAction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SimpleCMSAction.class.getName());

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link de.hybris.platform.cms2.servicelayer.services.CMSComponentService#isContainer(String)} instead.
	 */
	@Deprecated(since = "5.2")
	@Override
	public Boolean isContainer(final SessionContext sessionContext)
	{
		return Boolean.FALSE;
	}
}
