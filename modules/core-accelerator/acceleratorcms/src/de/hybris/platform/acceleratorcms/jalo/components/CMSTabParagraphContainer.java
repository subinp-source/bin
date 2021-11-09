/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.jalo.components;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.SessionContext;

import java.util.List;


/**
 * Container for tab paragraph components.
 */
public class CMSTabParagraphContainer extends GeneratedCMSTabParagraphContainer
{
	@Override
	public List<SimpleCMSComponent> getCurrentCMSComponents(final SessionContext ctx)
	{
		return getSimpleCMSComponents(ctx);
	}
}
