/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A breadcrumb trail through the site.
 *
 * @see Breadcrumb
 *
 */
public class Breadcrumbs implements Serializable
{
	private static final long serialVersionUID = 1L;
	private List<Breadcrumb> listOfBreadcrumbs;

	/**
	 * Default constructor.
	 */
	public Breadcrumbs()
	{
		listOfBreadcrumbs = new ArrayList<Breadcrumb>();
	}

	public List<Breadcrumb> getListOfBreadcrumbs()
	{
		return this.listOfBreadcrumbs;
	}

	public void setListOfBreadcrumbs(final List<Breadcrumb> listOfBreadcrumbs)
	{
		this.listOfBreadcrumbs = listOfBreadcrumbs;
	}


	/**
	 * Adds a given Breadcrumb to the current 'trail'.
	 *
	 * @param breadcrumb
	 *           - e.g. a category being navigated through.
	 */
	public void addBreadcrumb(final Breadcrumb breadcrumb)
	{
		this.listOfBreadcrumbs.add(breadcrumb);
	}

	/**
	 * Adds a given Breadcrumb to the current 'trail'.
	 *
	 * @param url
	 *           - the category url
	 * @param name
	 *           - the category name
	 *
	 */
	public void addBreadcrumb(final String url, final String name)
	{
		this.listOfBreadcrumbs.add(new Breadcrumb(url, name));
	}

	/**
	 * Returns true if breadcrumbs ArrayList isn't empty.
	 *
	 * @return whether breadcrumb list is populated.
	 */
	public boolean hasBreadcrumbs()
	{
		return listOfBreadcrumbs != null && !listOfBreadcrumbs.isEmpty();
	}
}
