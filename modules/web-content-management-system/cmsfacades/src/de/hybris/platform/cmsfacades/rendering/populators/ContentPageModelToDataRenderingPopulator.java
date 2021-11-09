/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populate {@code AbstractPageData#setOtherProperties(Map)} for content pages
 */
public class ContentPageModelToDataRenderingPopulator implements Populator<AbstractPageModel, Map<String, Object>>
{
	private Predicate<ItemModel> contentPageTypePredicate;

	@Override
	public void populate(final AbstractPageModel source, final Map<String, Object> target) throws ConversionException
	{
		if (getContentPageTypePredicate().test(source))
		{
			target.put(ContentPageModel.LABEL, ((ContentPageModel) source).getLabel());
		}

	}

	protected Predicate<ItemModel> getContentPageTypePredicate()
	{
		return contentPageTypePredicate;
	}

	@Required
	public void setContentPageTypePredicate(final Predicate<ItemModel> contentPageTypePredicate)
	{
		this.contentPageTypePredicate = contentPageTypePredicate;
	}

}
