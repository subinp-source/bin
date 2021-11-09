/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncJobModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;


public class CatalogVersionSyncJobModelBuilder
{
	private final CatalogVersionSyncJobModel model;

	private CatalogVersionSyncJobModelBuilder()
	{
		model = new CatalogVersionSyncJobModel();
	}

	private CatalogVersionSyncJobModelBuilder(final CatalogVersionSyncJobModel model)
	{
		this.model = model;
	}

	protected CatalogVersionSyncJobModel getModel()
	{
		return this.model;
	}

	public static CatalogVersionSyncJobModelBuilder aModel()
	{
		return new CatalogVersionSyncJobModelBuilder();
	}

	public static CatalogVersionSyncJobModelBuilder fromModel(final CatalogVersionSyncJobModel model)
	{
		return new CatalogVersionSyncJobModelBuilder(model);
	}

	public CatalogVersionSyncJobModelBuilder withTargetCatalogVersion(final CatalogVersionModel targetCatalogVersion)
	{
		getModel().setTargetVersion(targetCatalogVersion);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withSourceCatalogVersion(final CatalogVersionModel sourceCatalogVersion)
	{
		getModel().setSourceVersion(sourceCatalogVersion);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withActive(final boolean active)
	{
		getModel().setActive(active);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withSyncPrincipalsOnly(final boolean syncPrincipalsOnly)
	{
		getModel().setSyncPrincipalsOnly(syncPrincipalsOnly);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withSessionUser(final UserModel user)
	{
		getModel().setSessionUser(user);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withRootTypes(final List<ComposedTypeModel> rootTypes)
	{
		getModel().setRootTypes(rootTypes);
		return this;
	}

	public CatalogVersionSyncJobModelBuilder withSyncPrincipals(final List<PrincipalModel> syncPrincipals)
	{
		getModel().setSyncPrincipals(syncPrincipals);
		return this;
	}

	public CatalogVersionSyncJobModel build()
	{
		return this.getModel();
	}
}
