/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.cms2.model.ComponentTypeGroupModel;
import de.hybris.platform.cms2.model.contents.ContentSlotNameModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;


public class ContentSlotNameModelBuilder
{

	private final ContentSlotNameModel model;

	private ContentSlotNameModelBuilder()
	{
		model = new ContentSlotNameModel();
	}

	private ContentSlotNameModelBuilder(ContentSlotNameModel model)
	{
		this.model = model;
	}

	protected ContentSlotNameModel getModel()
	{
		return this.model;
	}

	public static ContentSlotNameModelBuilder aModel()
	{
		return new ContentSlotNameModelBuilder();
	}

	public static ContentSlotNameModelBuilder fromModel(ContentSlotNameModel model)
	{
		return new ContentSlotNameModelBuilder(model);
	}

	public ContentSlotNameModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public ContentSlotNameModelBuilder withTemplate(PageTemplateModel template)
	{
		getModel().setTemplate(template);
		return this;
	}

	public ContentSlotNameModelBuilder withCompTypeGroup(ComponentTypeGroupModel compTypeGroup)
	{
		getModel().setCompTypeGroup(compTypeGroup);
		return this;
	}

	public ContentSlotNameModel build()
	{
		return this.getModel();
	}
}
