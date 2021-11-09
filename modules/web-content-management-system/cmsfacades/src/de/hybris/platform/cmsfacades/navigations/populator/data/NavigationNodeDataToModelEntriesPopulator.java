/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.populator.data;

import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.cmsfacades.navigations.service.NavigationEntryService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link CMSNavigationNodeModel} entries, assuming that it will always replace the
 * existing ones.
 *
 * @deprecated since 1811, please use {@link de.hybris.platform.cmsfacades.cmsitems.CMSItemFacade} instead.
 */
@Deprecated(since = "1811", forRemoval = true)
public class NavigationNodeDataToModelEntriesPopulator implements Populator<NavigationNodeData, CMSNavigationNodeModel>
{
	private NavigationEntryService navigationEntryService;

	@Override
	public void populate(final NavigationNodeData source, final CMSNavigationNodeModel target) throws ConversionException
	{
		if (CollectionUtils.isEmpty(source.getEntries()))
		{
			return;
		}
		target.setEntries(source.getEntries().stream()
				.map(entryData -> getNavigationEntryService().createNavigationEntry(entryData, target.getCatalogVersion()))
				.collect(Collectors.toList()));
	}

	protected NavigationEntryService getNavigationEntryService()
	{
		return navigationEntryService;
	}

	@Required
	public void setNavigationEntryService(final NavigationEntryService navigationEntryService)
	{
		this.navigationEntryService = navigationEntryService;
	}
}
