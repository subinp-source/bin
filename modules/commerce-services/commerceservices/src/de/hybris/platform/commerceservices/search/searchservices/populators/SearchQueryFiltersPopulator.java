/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchQueryConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchFilterQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter;
import de.hybris.platform.searchservices.search.data.SnFilter;
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery;
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery;
import de.hybris.platform.searchservices.search.data.SnMatchType;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Populates search query filters
 */
public class SearchQueryFiltersPopulator implements Populator<SnSearchQueryConverterData, SnSearchQuery>
{
	public static final String ALL_CATEGORIES_FIELD = "allCategories";

	@Override
	public void populate(final SnSearchQueryConverterData source, final SnSearchQuery target)
	{
		final SolrSearchQueryData searchQuery = source.getSearchQuery();

		addFacetFilters(searchQuery.getFilterTerms(), target);
		addFilters(searchQuery.getFilterQueries(), target);
		addCategoryFilter(searchQuery.getCategoryCode(), target);
	}

	protected void addFacetFilters(final List<SolrSearchQueryTermData> sourceFilterTerms, final SnSearchQuery target)
	{
		if (CollectionUtils.isNotEmpty(sourceFilterTerms))
		{
			final Map<String, List<String>> filterTerms = sourceFilterTerms.stream().collect(Collectors
					.groupingBy(SolrSearchQueryTermData::getKey,
							Collectors.mapping(SolrSearchQueryTermData::getValue, Collectors.toList())));

			for (final Entry<String, List<String>> entry : filterTerms.entrySet())
			{
				if (StringUtils.equals(entry.getKey(), ALL_CATEGORIES_FIELD))
				{
					CollectionUtils.emptyIfNull(entry.getValue()).forEach(e -> addCategoryFilter(e, target));
				}
				else
				{
					final SnBucketsFacetFilter facetFilter = new SnBucketsFacetFilter();
					facetFilter.setFacetId(entry.getKey());
					facetFilter.setBucketIds(entry.getValue());

					target.getFacetFilters().add(facetFilter);
				}
			}
		}
	}

	protected void addFilters(final List<SolrSearchFilterQueryData> sourceFilterQueries, final SnSearchQuery target)
	{
		if (CollectionUtils.isNotEmpty(sourceFilterQueries))
		{
			for (final SolrSearchFilterQueryData sourceFilterQuery : sourceFilterQueries)
			{
				final SnMatchTermsQuery filterQuery = new SnMatchTermsQuery();
				filterQuery.setExpression(sourceFilterQuery.getKey());
				filterQuery.setValues(List.copyOf(sourceFilterQuery.getValues()));
				filterQuery.setMatchType(
						FilterQueryOperator.AND.equals(sourceFilterQuery.getOperator()) ? SnMatchType.ALL : SnMatchType.ANY);

				final SnFilter filter = new SnFilter();
				filter.setQuery(filterQuery);

				target.getFilters().add(filter);
			}
		}
	}

	protected void addCategoryFilter(final String sourceCategoryCode, final SnSearchQuery target)
	{
		if (StringUtils.isNotBlank(sourceCategoryCode))
		{
			final SnMatchTermQuery filterQuery = new SnMatchTermQuery();
			filterQuery.setExpression(ALL_CATEGORIES_FIELD);
			filterQuery.setValue(sourceCategoryCode);

			final SnFilter filter = new SnFilter();
			filter.setQuery(filterQuery);

			target.getFilters().add(filter);
		}
	}
}
