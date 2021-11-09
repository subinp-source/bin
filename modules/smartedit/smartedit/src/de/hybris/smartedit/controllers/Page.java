/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.smartedit.controllers;

/**
 * @deprecated since 2005, no longer needed.
 */
@Deprecated(since = "2005", forRemoval = true)
public enum Page
{

	LOGIN_PAGE("loginPage"), //
	SMART_EDIT_ROOT_PAGE("index");

	private final String viewName;

	Page(final String viewName)
	{
		this.viewName = viewName;
	}

	public String getViewName()
	{
		return viewName;
	}
}
