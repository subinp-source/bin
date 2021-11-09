/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationEntryModel;
import de.hybris.platform.cms2.model.navigation.CMSNavigationNodeModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSNavigationDao;
import de.hybris.platform.cmsfacades.util.builder.NavigationEntryModelBuilder;
import de.hybris.platform.core.model.ItemModel;

import org.springframework.beans.factory.annotation.Required;


public class CMSNavigationEntryModelMother extends AbstractModelMother<CMSNavigationEntryModel>
{
	private CMSNavigationDao navigationDao;

	public CMSNavigationEntryModel createEntryAndAddToNavigationNode(final CMSNavigationNodeModel navigationNode,
			final CatalogVersionModel catalogVersion,
			final ItemModel itemModel,
			final String uid)
	{
		return getOrSaveAndReturn(
				() -> getNavigationDao().findNavigationEntryByUid(uid, catalogVersion),
				() -> NavigationEntryModelBuilder.aModel().withUid(uid).withNavigationNode(navigationNode)
						.withCatalogVersion(catalogVersion).withItem(itemModel).build());
	}

	public CMSNavigationDao getNavigationDao()
	{
		return navigationDao;
	}

	@Required
	public void setNavigationDao(final CMSNavigationDao navigationDao)
	{
		this.navigationDao = navigationDao;
	}
}
