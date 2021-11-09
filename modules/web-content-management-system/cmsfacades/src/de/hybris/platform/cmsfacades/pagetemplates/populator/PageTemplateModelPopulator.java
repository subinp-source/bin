/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pagetemplates.populator;

import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmsfacades.data.PageTemplateData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link PageTemplateData} from the {@link PageTemplateModel}
 */
public class PageTemplateModelPopulator implements Populator<PageTemplateModel, PageTemplateData>
{
	private UniqueItemIdentifierService uniqueItemIdentifierService;
	@Override
	public void populate(final PageTemplateModel source, final PageTemplateData target) throws ConversionException
	{

		target.setUid(source.getUid());
		getUniqueItemIdentifierService().getItemData(source).ifPresent(itemData ->{
			target.setUuid(itemData.getItemId());
		});
		target.setName(source.getName());
		target.setFrontEndName(source.getFrontendTemplateName());
		target.setPreviewIcon((source.getPreviewIcon() != null) ? source.getPreviewIcon().getURL() : null);
	}

	@Required
	public void setUniqueItemIdentifierService(UniqueItemIdentifierService uniqueItemIdentifierService)
	{
		this.uniqueItemIdentifierService = uniqueItemIdentifierService;
	}

	protected UniqueItemIdentifierService getUniqueItemIdentifierService()
	{
		return uniqueItemIdentifierService;
	}

}
