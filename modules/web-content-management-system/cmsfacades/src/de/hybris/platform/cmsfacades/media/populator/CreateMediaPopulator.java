/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.media.populator;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate the {@link MediaModel} from the {@link MediaData} .
 */
public class CreateMediaPopulator implements Populator<MediaData, MediaModel>
{
	private CatalogVersionService catalogVersionService;

	@Override
	public void populate(final MediaData source, final MediaModel target) throws ConversionException
	{
		target.setAltText(source.getAltText());
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());

		try
		{
			final CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(source.getCatalogId(),
					source.getCatalogVersion());
			target.setCatalogVersion(catalogVersion);
		}
		catch (UnknownIdentifierException | AmbiguousIdentifierException | IllegalArgumentException e)
		{
			throw new ConversionException("Unable to find a catalogVersion for catalogId [" + source.getCatalogId()
			+ "] and versionId [" + source.getCatalogVersion() + "]", e);
		}
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

}
