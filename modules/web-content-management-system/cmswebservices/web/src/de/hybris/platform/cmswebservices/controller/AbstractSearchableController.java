/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.controller;

import de.hybris.platform.cmswebservices.data.NamedQueryData;
import de.hybris.platform.cmswebservices.namedquery.NamedQuerySearchable;

import javax.servlet.http.HttpServletRequest;


/**
 * Abstract controller providing searching/querying helper methods.
 * This class can implement all sorts of searchable interfaces and provide helper functions to extending controllers.
 */
public abstract class AbstractSearchableController implements NamedQuerySearchable
{
	private static final String PARAM_NAMED_QUERY = "namedQuery";
	private static final String PARAM_PAGE_SIZE = "pageSize";
	private static final String PARAM_CURRENT_PAGE = "currentPage";
	private static final String PARAM_SORT = "sort";
	private static final String PARAM_PARAMETERS = "params";

	@Override
	public NamedQueryData getNamedQuery(HttpServletRequest request)
	{
		final NamedQueryData namedQuery = new NamedQueryData();
		namedQuery.setNamedQuery(request.getParameter(PARAM_NAMED_QUERY));
		namedQuery.setSort(request.getParameter(PARAM_SORT));
		namedQuery.setParams(request.getParameter(PARAM_PARAMETERS));
		namedQuery.setPageSize(request.getParameter(PARAM_PAGE_SIZE));
		namedQuery.setCurrentPage(request.getParameter(PARAM_CURRENT_PAGE));
		return namedQuery;
	}



}
