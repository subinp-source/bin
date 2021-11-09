/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.products.service.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.namedquery.NamedQuery;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.cms2.namedquery.service.NamedQueryService;
import de.hybris.platform.cmsfacades.products.service.ProductCategorySearchService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link ProductCategorySearchService} using {@link de.hybris.platform.cmsfacades.namedquery.service.NamedQueryService}.
 */
public class DefaultProductCategorySearchService implements ProductCategorySearchService
{
	public static final String NAMED_QUERY_CATEGORY_SEARCH_BY_TEXT = "namedQueryProductCategoryByText";
	public static final String NAMED_QUERY_CATEGORY_SEARCH_BY_TEXT_OUTER_JOIN_LP = "namedQueryProductCategoryByTextOuterJoinLP";

	private I18NService i18NService;
	private NamedQueryService namedQueryService;

	@Override
	public SearchResult<CategoryModel> findProductCategories(final String text, final PageableData pageableData, final CatalogVersionModel catalogVersion)
	{
		final NamedQuery namedQuery = getNamedQueryForProductCategorySearch(text, pageableData, catalogVersion);
		return getNamedQueryService().getSearchResult(namedQuery);
	}

	/**
	 * Get the NamedQuery data bean for searching product categories.
	 * @param text the free text search that will be used in category name, description and code
	 * @param pageableData the pageable data
	 * @param catalogVersion the catalog version where the category lives
	 * @return the named query bean
	 */
	protected NamedQuery getNamedQueryForProductCategorySearch(final String text, final PageableData pageableData, final CatalogVersionModel catalogVersion)
	{
		ServicesUtil.validateParameterNotNull(pageableData, "PageableData object cannot be null.");
		ServicesUtil.validateParameterNotNull(catalogVersion, "CatalogVersion object cannot be null.");
		final String queryText = StringUtils.isEmpty(text) ? "%%" : "%" + text + "%";
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put(CategoryModel.NAME, queryText);
		parameters.put(CategoryModel.DESCRIPTION, queryText);
		parameters.put(CategoryModel.CODE, queryText);
		parameters.put(CategoryModel.CATALOGVERSION, catalogVersion);

		if (StringUtils.isEmpty(pageableData.getSort()))
		{
			pageableData.setSort(CategoryModel.NAME);
		}
		final List<Sort> sortList = Arrays.asList(new Sort().withParameter(pageableData.getSort()).withDirection(SortDirection.ASC));

		final Locale currentLocale = getI18NService().getCurrentLocale();
		final NamedQuery namedQuery = new NamedQuery();
		if (currentLocale != null)
		{
			namedQuery.withQueryName(NAMED_QUERY_CATEGORY_SEARCH_BY_TEXT_OUTER_JOIN_LP);
		} else {
			namedQuery.withQueryName(NAMED_QUERY_CATEGORY_SEARCH_BY_TEXT);
		}

		namedQuery.withCurrentPage(pageableData.getCurrentPage())
				.withPageSize(pageableData.getPageSize())
				.withParameters(parameters)
				.withSort(sortList);

		return namedQuery;
	}

	protected NamedQueryService getNamedQueryService()
	{
		return namedQueryService;
	}

	@Required
	public void setNamedQueryService(final NamedQueryService namedQueryService)
	{
		this.namedQueryService = namedQueryService;
	}

	protected I18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final I18NService i18NService)
	{
		this.i18NService = i18NService;
	}
}
