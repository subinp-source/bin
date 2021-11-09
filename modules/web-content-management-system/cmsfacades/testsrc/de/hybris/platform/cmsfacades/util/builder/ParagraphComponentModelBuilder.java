/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;

import java.util.Locale;


public class ParagraphComponentModelBuilder {

	private final CMSParagraphComponentModel model;

	private ParagraphComponentModelBuilder()
	{
		model = new CMSParagraphComponentModel();
	}

	private ParagraphComponentModelBuilder(CMSParagraphComponentModel model)
	{
		this.model = model;
	}

	protected CMSParagraphComponentModel getModel()
	{
		return this.model;
	}

	public static ParagraphComponentModelBuilder aModel()
	{
		return new ParagraphComponentModelBuilder();
	}

	public static ParagraphComponentModelBuilder fromModel(CMSParagraphComponentModel model)
	{
		return new ParagraphComponentModelBuilder(model);
	}

	public ParagraphComponentModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public ParagraphComponentModelBuilder withCatalogVersion(CatalogVersionModel cv)
	{
		getModel().setCatalogVersion(cv);
		return this;
	}

	public ParagraphComponentModelBuilder withContent(String content)
	{
		getModel().setContent(content);
		return this;
	}

	public ParagraphComponentModelBuilder withContent(String content, Locale locale)
	{
		getModel().setContent(content, locale);
		return this;
	}

	public ParagraphComponentModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public CMSParagraphComponentModel build()
	{
		return this.getModel();
	}
}
