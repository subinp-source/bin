/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.ComponentTypeGroupModel;

import java.util.Arrays;
import java.util.Locale;

import com.google.common.collect.Sets;


public class ComponentTypeGroupModelBuilder
{

	private final ComponentTypeGroupModel model;

	private ComponentTypeGroupModelBuilder()
	{
		model = new ComponentTypeGroupModel();
	}

	private ComponentTypeGroupModelBuilder(ComponentTypeGroupModel model)
	{
		this.model = model;
	}

	protected ComponentTypeGroupModel getModel()
	{
		return this.model;
	}

	public static ComponentTypeGroupModelBuilder aModel()
	{
		return new ComponentTypeGroupModelBuilder();
	}

	public static ComponentTypeGroupModelBuilder fromModel(ComponentTypeGroupModel model)
	{
		return new ComponentTypeGroupModelBuilder(model);
	}

	public ComponentTypeGroupModelBuilder withCode(String code)
	{
		getModel().setCode(code);
		return this;
	}

	public ComponentTypeGroupModelBuilder withDescription(String description, Locale locale)
	{
		getModel().setDescription(description, locale);
		return this;
	}

	public ComponentTypeGroupModelBuilder withCmsComponentTypes(CMSComponentTypeModel... cmsComponentTypes)
	{
		getModel().setCmsComponentTypes(Sets.newHashSet(Arrays.asList(cmsComponentTypes)));
		return this;
	}

	public ComponentTypeGroupModel build()
	{
		return this.getModel();
	}
}
