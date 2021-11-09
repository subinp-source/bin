/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.search.SnSearchException;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;


/**
 * Implementation of {@link SnSearchListener} that uses the session catalog versions for filtering.
 */
public class CatalogVersionFilterSnSearchListener implements SnSearchListener
{
	protected static final String SEPARATOR = ":";

	private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionFilterSnSearchListener.class);

	private CatalogTypeService catalogTypeService;
	private CatalogVersionService catalogVersionService;

	@Override
	public void beforeSearch(final SnSearchContext context) throws SnSearchException
	{
		if (catalogTypeService.isCatalogVersionAwareType(context.getIndexType().getItemComposedType()))
		{
			final Collection<CatalogVersionModel> catalogVersions = catalogVersionService.getSessionCatalogVersions();
			final Map<String, SnField> fields = context.getIndexType().getFields();

			if (CollectionUtils.isNotEmpty(catalogVersions) && fields.containsKey(SearchservicesConstants.CATALOG_VERSION_FIELD))
			{
				final List<Object> catalogVersionTerms = catalogVersions.stream().map(catalogVersion -> {
					final CatalogModel catalog = catalogVersion.getCatalog();
					return new StringBuilder().append(catalog.getId()).append(SEPARATOR).append(catalogVersion.getVersion())
							.toString();
				}).collect(Collectors.toList());

				final SnMatchTermsQuery filterQuery = new SnMatchTermsQuery();
				filterQuery.setExpression(SearchservicesConstants.CATALOG_VERSION_FIELD);
				filterQuery.setValues(catalogVersionTerms);

				final SnFilter filter = new SnFilter();
				filter.setQuery(filterQuery);

				final SnSearchQuery searchQuery = context.getSearchRequest().getSearchQuery();
				searchQuery.getFilters().add(filter);
			}
			else if (CollectionUtils.isNotEmpty(catalogVersions))
			{
				LOG.warn("Catalog version filter disabled, field '{}' not found for index type {}",
						SearchservicesConstants.CATALOG_VERSION_FIELD, context.getIndexType().getId());
			}
		}
	}

	@Override
	public void afterSearch(final SnSearchContext context) throws SnSearchException
	{
		// NOOP
	}

	@Override
	public void afterSearchError(final SnSearchContext context) throws SnSearchException
	{
		// NOOP
	}

	public CatalogTypeService getCatalogTypeService()
	{
		return catalogTypeService;
	}

	@Required
	public void setCatalogTypeService(final CatalogTypeService catalogTypeService)
	{
		this.catalogTypeService = catalogTypeService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}
