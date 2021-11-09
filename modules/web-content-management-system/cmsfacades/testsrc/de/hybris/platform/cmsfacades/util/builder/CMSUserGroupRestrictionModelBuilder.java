/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.Arrays;


public class CMSUserGroupRestrictionModelBuilder
{
	private final CMSUserGroupRestrictionModel model;

	private CMSUserGroupRestrictionModelBuilder()
	{
		model = new CMSUserGroupRestrictionModel();
	}

	private CMSUserGroupRestrictionModelBuilder(final CMSUserGroupRestrictionModel model)
	{
		this.model = model;
	}

	protected CMSUserGroupRestrictionModel getModel()
	{
		return this.model;
	}

	public static CMSUserGroupRestrictionModelBuilder aModel()
	{
		return new CMSUserGroupRestrictionModelBuilder();
	}

	public static CMSUserGroupRestrictionModelBuilder fromModel(final CMSUserGroupRestrictionModel model)
	{
		return new CMSUserGroupRestrictionModelBuilder(model);
	}

	public CMSUserGroupRestrictionModelBuilder withCatalogVersion(final CatalogVersionModel model)
	{
		getModel().setCatalogVersion(model);
		return this;
	}

	public CMSUserGroupRestrictionModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}

	public CMSUserGroupRestrictionModelBuilder withName(final String name)
	{
		getModel().setName(name);
		return this;
	}

	public CMSUserGroupRestrictionModelBuilder withUserGroups(final UserGroupModel... userGroups)
	{
		getModel().setUserGroups(Arrays.asList(userGroups));
		return this;
	}

	public CMSUserGroupRestrictionModelBuilder withIncludeSubgroups(final boolean isIncludeSubgroups)
	{
		getModel().setIncludeSubgroups(isIncludeSubgroups);
		return this;
	}

	public CMSUserGroupRestrictionModelBuilder withPages(final AbstractPageModel... pages)
	{
		getModel().setPages(Arrays.asList(pages));
		return this;
	}

	public CMSUserGroupRestrictionModel build()
	{
		return this.getModel();
	}
}
