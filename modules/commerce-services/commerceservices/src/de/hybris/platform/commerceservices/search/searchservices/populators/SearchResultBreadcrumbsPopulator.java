/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Populates search result breadcrumbs
 */
public class SearchResultBreadcrumbsPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, FacetSearchPageData<SolrSearchQueryData, SearchResultValueData>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final FacetSearchPageData<SolrSearchQueryData, SearchResultValueData> target)
	{
		final List<BreadcrumbData<SolrSearchQueryData>> breadcrumbs = buildBreadcrumbs(target.getCurrentQuery(),
				source.getSnSearchResult().getFacets());

		target.setBreadcrumbs(breadcrumbs);
	}

	protected List<BreadcrumbData<SolrSearchQueryData>> buildBreadcrumbs(final SolrSearchQueryData snSearchQueryData,
			final List<AbstractSnFacetResponse> facets)
	{
		final List<BreadcrumbData<SolrSearchQueryData>> result = new ArrayList<>();

		if (CollectionUtils.isEmpty(facets))
		{
			return result;
		}

		for (final AbstractSnFacetResponse facetResponse : facets)
		{
			final List<? extends AbstractSnBucketResponse> selectedBuckets;
			if (facetResponse instanceof SnTermBucketsFacetResponse)
			{
				selectedBuckets = ((SnTermBucketsFacetResponse) facetResponse).getSelectedBuckets();
			}
			else if (facetResponse instanceof SnRangeBucketsFacetResponse)
			{
				selectedBuckets = ((SnRangeBucketsFacetResponse) facetResponse).getSelectedBuckets();
			}
			else
			{
				selectedBuckets = null;
			}

			CollectionUtils.emptyIfNull(selectedBuckets).forEach(selectedBucket -> {
				final BreadcrumbData<SolrSearchQueryData> breadcrumbData = new BreadcrumbData<>();
				breadcrumbData.setFacetCode(facetResponse.getExpression());
				breadcrumbData.setFacetName(facetResponse.getName());

				breadcrumbData.setFacetValueCode(selectedBucket.getId());
				breadcrumbData.setFacetValueName(selectedBucket.getName());

				breadcrumbData.setRemoveQuery(refineQueryRemoveFacet(snSearchQueryData, facetResponse, selectedBucket));
				result.add(breadcrumbData);
			});
		}

		return result;
	}

	protected SolrSearchQueryData refineQueryRemoveFacet(final SolrSearchQueryData snSearchQueryData,
			final AbstractSnFacetResponse facet, final AbstractSnBucketResponse selectedFacetValue)
	{
		final List<SolrSearchQueryTermData> newTerms = new ArrayList<>(
				CollectionUtils.emptyIfNull(snSearchQueryData.getFilterTerms()));

		// Remove the term for the specified facet
		final Iterator<SolrSearchQueryTermData> iterator = newTerms.iterator();
		while (iterator.hasNext())
		{
			final SolrSearchQueryTermData term = iterator.next();
			if (facet.getExpression().equals(term.getKey()) && selectedFacetValue.getId().equals(term.getValue()))
			{
				iterator.remove();
			}
		}

		// Build the new query data
		final SolrSearchQueryData result = createSearchQueryData(snSearchQueryData);
		result.setFilterTerms(newTerms);
		return result;
	}

	protected SolrSearchQueryData createSearchQueryData(final SolrSearchQueryData source)
	{
		final SolrSearchQueryData target = new SolrSearchQueryData();
		target.setFreeTextSearch(source.getFreeTextSearch());
		target.setCategoryCode(source.getCategoryCode());
		target.setSort(source.getSort());
		target.setFilterTerms(source.getFilterTerms());
		target.setFilterQueries(source.getFilterQueries());
		return target;
	}

}
