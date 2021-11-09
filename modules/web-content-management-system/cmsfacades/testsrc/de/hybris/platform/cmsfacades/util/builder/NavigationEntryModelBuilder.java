/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.core.model.ItemModel;


public class NavigationEntryModelBuilder
{
	private final CMSNavigationEntryModel model;

	private NavigationEntryModelBuilder(final CMSNavigationEntryModel model)
	{
		this.model = model;
	}

	private NavigationEntryModelBuilder()
	{
		this.model = new CMSNavigationEntryModel();
	}

	public CMSNavigationEntryModel getModel()
	{
		return model;
	}

	public static NavigationEntryModelBuilder aModel()
	{
		return new NavigationEntryModelBuilder();
	}

	public static NavigationEntryModelBuilder fromModel(final CMSNavigationEntryModel model)
	{
		return new NavigationEntryModelBuilder(model);
	}

	public NavigationEntryModelBuilder withName(final String name)
	{
		getModel().setName(name);
		return this;
	}

	public NavigationEntryModelBuilder withItem(final ItemModel item)
	{
		getModel().setItem(item);
		return this;
	}

	public NavigationEntryModelBuilder withNavigationNode(final CMSNavigationNodeModel navigationNode)
	{
		getModel().setNavigationNode(navigationNode);
		return this;
	}

	public NavigationEntryModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public NavigationEntryModelBuilder withCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		getModel().setCatalogVersion(catalogVersion);
		return this;
	}

	public CMSNavigationEntryModel build()
	{
		return this.getModel();
	}
}
