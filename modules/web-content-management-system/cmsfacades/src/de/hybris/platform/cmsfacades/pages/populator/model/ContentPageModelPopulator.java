/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.populator.model;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.data.ContentPageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * Converts an {@link ContentPageModel} page to a {@link ContentPageData} dto
 * 
 * @deprecated since 6.6
 */
@Deprecated(since = "6.6", forRemoval = true)
public class ContentPageModelPopulator implements Populator<ContentPageModel, ContentPageData>
{
	@Override
	public void populate(final ContentPageModel source, final ContentPageData target) throws ConversionException
	{
		target.setLabel(source.getLabel());
	}
}
