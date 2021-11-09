/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;


public class ContentSlotForPageModelBuilder
{

	private final ContentSlotForPageModel model;

	private ContentSlotForPageModelBuilder()
	{
		model = new ContentSlotForPageModel();
	}

	private ContentSlotForPageModelBuilder(ContentSlotForPageModel model)
	{
		this.model = model;
	}

	protected ContentSlotForPageModel getModel()
	{
		return this.model;
	}

	public static ContentSlotForPageModelBuilder aModel()
	{
		return new ContentSlotForPageModelBuilder();
	}

	public static ContentSlotForPageModelBuilder fromModel(ContentSlotForPageModel model)
	{
		return new ContentSlotForPageModelBuilder(model);
	}

	public ContentSlotForPageModelBuilder withCatalogVersion(CatalogVersionModel model)
	{
		getModel().setCatalogVersion(model);
		return this;
	}

	public ContentSlotForPageModelBuilder withContentSlot(ContentSlotModel contentSlot)
	{
		getModel().setContentSlot(contentSlot);
		return this;
	}

	public ContentSlotForPageModelBuilder withPage(AbstractPageModel page)
	{
		getModel().setPage(page);
		return this;
	}

	public ContentSlotForPageModelBuilder withPosition(String position)
	{
		getModel().setPosition(position);
		return this;
	}

	public ContentSlotForPageModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public ContentSlotForPageModel build()
	{
		return this.getModel();
	}
}
