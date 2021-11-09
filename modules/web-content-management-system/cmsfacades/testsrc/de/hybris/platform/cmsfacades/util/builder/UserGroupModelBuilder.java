/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.Locale;


public class UserGroupModelBuilder
{
	private final UserGroupModel model;

	private UserGroupModelBuilder()
	{
		this.model = new UserGroupModel();
	}

	private UserGroupModelBuilder(final UserGroupModel model)
	{
		this.model = model;
	}

	protected UserGroupModel getModel()
	{
		return this.model;
	}

	public static UserGroupModelBuilder aModel()
	{
		return new UserGroupModelBuilder();
	}

	public static UserGroupModelBuilder fromModel(final UserGroupModel model)
	{
		return new UserGroupModelBuilder(model);
	}

	public UserGroupModel build()
	{
		return getModel();
	}

	public UserGroupModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setLocName(name, locale);
		return this;
	}

	public UserGroupModelBuilder withUid(final String uid)
	{
		getModel().setUid(uid);
		return this;
	}
}
