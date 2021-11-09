/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;


public class MediaModelBuilder
{

	private final MediaModel model;

	private MediaModelBuilder()
	{
		model = new MediaModel();
	}

	private MediaModelBuilder(final MediaModel model)
	{
		this.model = model;
	}

	protected MediaModel getModel()
	{
		return this.model;
	}

	public static MediaModelBuilder aModel()
	{
		return new MediaModelBuilder();
	}

	public static MediaModelBuilder fromModel(final MediaModel model)
	{
		return new MediaModelBuilder(model);
	}

	public MediaModelBuilder withCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		getModel().setCatalogVersion(catalogVersion);
		return this;
	}

	public MediaModelBuilder withMimeType(final String mimetype)
	{
		getModel().setMime(mimetype);
		return this;
	}

	public MediaModelBuilder withRealFileName(final String realFilename)
	{
		getModel().setRealFileName(realFilename);
		return this;
	}

	public MediaModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public MediaModelBuilder withInternalUrl(final String url)
	{
		getModel().setInternalURL(url);
		return this;
	}

	public MediaModelBuilder withAltText(final String altText)
	{
		getModel().setAltText(altText);
		return this;
	}

	public MediaModelBuilder withDescription(final String description)
	{
		getModel().setDescription(description);
		return this;
	}

	public MediaModelBuilder withMediaFormat(final MediaFormatModel mediaFormat)
	{
		getModel().setMediaFormat(mediaFormat);
		return this;
	}

	public MediaModel build()
	{
		return this.getModel();
	}

}
