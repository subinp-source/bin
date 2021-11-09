/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorcms.component.container.impl;

import de.hybris.platform.acceleratorcms.component.container.CMSComponentContainerStrategy;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.containers.AbstractCMSComponentContainerModel;

import java.util.List;


/**
 * @deprecated since 1811, please use
 *             {@link de.hybris.platform.cms2.strategies.impl.LegacyCMSComponentContainerStrategy}
 */
@Deprecated(since = "1811", forRemoval = true)
public class LegacyCMSComponentContainerStrategy implements CMSComponentContainerStrategy
{
	@Override
	public List<AbstractCMSComponentModel> getDisplayComponentsForContainer(final AbstractCMSComponentContainerModel container)
	{
		return (List) container.getCurrentCMSComponents();
	}
}
