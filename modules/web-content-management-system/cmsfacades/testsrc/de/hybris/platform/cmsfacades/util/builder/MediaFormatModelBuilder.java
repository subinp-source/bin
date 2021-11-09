/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.core.model.media.MediaFormatModel;


public class MediaFormatModelBuilder
{

	private final MediaFormatModel model;

	private MediaFormatModelBuilder()
	{
		model = new MediaFormatModel();
	}

	private MediaFormatModelBuilder(final MediaFormatModel model)
	{
		this.model = model;
	}

	protected MediaFormatModel getModel()
	{
		return this.model;
	}

	public static MediaFormatModelBuilder aModel()
	{
		return new MediaFormatModelBuilder();
	}

	public static MediaFormatModelBuilder fromModel(final MediaFormatModel model)
	{
		return new MediaFormatModelBuilder(model);
	}

	public MediaFormatModelBuilder withQualifier(final String qualifier)
	{
		getModel().setQualifier(qualifier);
		return this;
	}

	public MediaFormatModel build()
	{
		return this.getModel();
	}

}
