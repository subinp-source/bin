/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.populator.data;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link CMSNavigationNodeModel}'s uid attribute with a generated key if the source's
 * uid is empty.
 *
 * @deprecated since 1811, please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
 */
@Deprecated(since = "1811", forRemoval = true)
public class NavigationNodeDataToModelUidGenerationPopulator implements Populator<NavigationNodeData, CMSNavigationNodeModel>
{
	private static final String DEFAULT_UID_PREFIX = "navnode_";
	private KeyGenerator processCodeGenerator;

	@Override
	public void populate(final NavigationNodeData source, final CMSNavigationNodeModel target) throws ConversionException
	{
		if (StringUtils.isEmpty(source.getUid()))
		{
			target.setUid(DEFAULT_UID_PREFIX + String.valueOf(processCodeGenerator.generate()));
		}
		else
		{
			target.setUid(source.getUid());
		}
	}

	protected KeyGenerator getProcessCodeGenerator()
	{
		return processCodeGenerator;
	}

	@Required
	public void setProcessCodeGenerator(final KeyGenerator processCodeGenerator)
	{
		this.processCodeGenerator = processCodeGenerator;
	}
}
