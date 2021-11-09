/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.cmsfacades.data.NavigationNodeData;
import de.hybris.platform.cmsfacades.rendering.visibility.RenderingVisibilityService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link NavigationNodeData}'s base attributes with attributes from
 * {@link CMSNavigationNodeModel} for rendering purpose
 */
public class NavigationNodeModelToDataRenderingPopulator implements Populator<CMSNavigationNodeModel, NavigationNodeData>
{
	private Converter<CMSNavigationEntryModel, NavigationEntryData> navigationEntryModelToDataConverter;
	private RenderingVisibilityService renderingVisibilityService;
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	@Override
	public void populate(final CMSNavigationNodeModel source, final NavigationNodeData target)
	{
		target.setUid(source.getUid());
		target.setUuid(getUniqueIdentifierAttributeToDataContentConverter().convert(source));
		target.setName(source.getName());
		target.setLocalizedTitle(source.getTitle());

		final List<NavigationEntryData> navigationEntries = source.getEntries().stream() //
				.filter(entry -> getRenderingVisibilityService().isVisible(entry.getItem())) //
				.map(getNavigationEntryModelToDataConverter()::convert) //
				.collect(Collectors.toList());
		target.setEntries(navigationEntries);
	}

	protected Converter<CMSNavigationEntryModel, NavigationEntryData> getNavigationEntryModelToDataConverter()
	{
		return navigationEntryModelToDataConverter;
	}

	@Required
	public void setNavigationEntryModelToDataConverter(
			final Converter<CMSNavigationEntryModel, NavigationEntryData> navigationEntryModelToDataConverter)
	{
		this.navigationEntryModelToDataConverter = navigationEntryModelToDataConverter;
	}

	protected RenderingVisibilityService getRenderingVisibilityService()
	{
		return renderingVisibilityService;
	}

	@Required
	public void setRenderingVisibilityService(
			final RenderingVisibilityService renderingVisibilityService)
	{
		this.renderingVisibilityService = renderingVisibilityService;
	}

	protected UniqueIdentifierAttributeToDataContentConverter<ItemModel> getUniqueIdentifierAttributeToDataContentConverter()
	{
		return uniqueIdentifierAttributeToDataContentConverter;
	}

	@Required
	public void setUniqueIdentifierAttributeToDataContentConverter(
			final UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter)
	{
		this.uniqueIdentifierAttributeToDataContentConverter = uniqueIdentifierAttributeToDataContentConverter;
	}
}
