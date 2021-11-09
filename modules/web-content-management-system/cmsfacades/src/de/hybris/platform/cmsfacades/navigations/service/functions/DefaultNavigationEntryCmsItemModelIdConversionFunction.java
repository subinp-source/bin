/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.functions;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.function.Function;

/**
 * Default implementation for conversion of {@link ItemModel} into {@code CMSItemModel#getUid()}
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryCmsItemModelIdConversionFunction implements Function<ItemModel, String> 
{
	@Override
	public String apply(final ItemModel itemModel) 
	{
		if (!(CMSItemModel.class.isAssignableFrom(itemModel.getClass()))) 
		{
			throw new ConversionException("Invalid CMS Component: " + itemModel);
		}
		return ((CMSItemModel) itemModel).getUid();
	}
}
