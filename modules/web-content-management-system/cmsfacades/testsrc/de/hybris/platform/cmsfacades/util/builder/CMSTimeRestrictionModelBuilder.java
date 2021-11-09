/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSTimeRestrictionModel;

import java.util.Arrays;
import java.util.Date;


public class CMSTimeRestrictionModelBuilder
{
	private final CMSTimeRestrictionModel model;

	private CMSTimeRestrictionModelBuilder()
	{
		model = new CMSTimeRestrictionModel();
	}

	private CMSTimeRestrictionModelBuilder(CMSTimeRestrictionModel model)
	{
		this.model = model;
	}

	protected CMSTimeRestrictionModel getModel()
	{
		return this.model;
	}

	public static CMSTimeRestrictionModelBuilder aModel()
	{
		return new CMSTimeRestrictionModelBuilder();
	}

	public static CMSTimeRestrictionModelBuilder fromModel(CMSTimeRestrictionModel model)
	{
		return new CMSTimeRestrictionModelBuilder(model);
	}

	public CMSTimeRestrictionModelBuilder withCatalogVersion(CatalogVersionModel model)
	{
		getModel().setCatalogVersion(model);
		return this;
	}

	public CMSTimeRestrictionModelBuilder withUid(String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public CMSTimeRestrictionModelBuilder withName(String name)
	{
		getModel().setName(name);
		return this;
	}

	public CMSTimeRestrictionModelBuilder withActiveFrom(Date activeFrom)
	{
		getModel().setActiveFrom(activeFrom);
		return this;
	}

	public CMSTimeRestrictionModelBuilder withActiveUntil(Date activeUntil)
	{
		getModel().setActiveUntil(activeUntil);
		return this;
	}

	public CMSTimeRestrictionModelBuilder withPages(AbstractPageModel... pages)
	{
		getModel().setPages(Arrays.asList(pages));
		return this;
	}

	public CMSTimeRestrictionModelBuilder withComponents(AbstractCMSComponentModel... components)
	{
		getModel().setComponents(Arrays.asList(components));
		return this;
	}

	public CMSTimeRestrictionModel build()
	{
		return this.getModel();
	}
}
