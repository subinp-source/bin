/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.searchservices.data.SnSearchResultConverterData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse;
import de.hybris.platform.searchservices.search.data.SnFacetFilterMode;
import de.hybris.platform.searchservices.search.data.SnRangeBucketResponse;
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.data.SnTermBucketResponse;
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse;
import de.hybris.platform.searchservices.util.ConverterUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Populates search result facets
 */
public class SearchResultFacetsPopulator implements
		Populator<SnSearchResultConverterData<SnSearchResult>, ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>>
{
	@Override
	public void populate(final SnSearchResultConverterData<SnSearchResult> source,
			final ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel> target)
	{
		if (containsSearchResult(source))
		{
			target.setFacets(buildFacets(source, target.getCurrentQuery()));
		}
	}

	protected boolean containsSearchResult(final SnSearchResultConverterData<SnSearchResult> source)
	{
		return source.getSnSearchResult() != null;
	}

	protected List<FacetData<SolrSearchQueryData>> buildFacets(final SnSearchResultConverterData<SnSearchResult> source,
			final SolrSearchQueryData searchQuery)
	{
		final SnSearchResult searchResult = source.getSnSearchResult();
		final List<AbstractSnFacetResponse> searchResultFacets = searchResult.getFacets();

		final List<AbstractSnBucketsFacetResponse> bucketResponses = searchResultFacets.stream()
				.filter(AbstractSnBucketsFacetResponse.class::isInstance).map(AbstractSnBucketsFacetResponse.class::cast)
				.collect(Collectors.toList());

		return ConverterUtils.convertAll(bucketResponses, bResp -> convertBucketsFacetResponse(bResp, searchQuery));
	}

	protected boolean isMultiselect(final AbstractSnBucketsFacetResponse facet)
	{
		return SnFacetFilterMode.MULTISELECT.equals(facet.getFilterMode());
	}

	protected FacetData<SolrSearchQueryData> convertBucketsFacetResponse(final AbstractSnBucketsFacetResponse source,
			final SolrSearchQueryData searchQuery)
	{
		final FacetData<SolrSearchQueryData> target = new FacetData<>();
		target.setCode(source.getExpression());
		target.setName(source.getName());
		target.setVisible(true);
		target.setMultiSelect(isMultiselect(source));

		if (source instanceof SnTermBucketsFacetResponse)
		{
			final SnTermBucketsFacetResponse termBucketsFacetResponse = (SnTermBucketsFacetResponse) source;

			final Set<String> selectedBucketIds = CollectionUtils.emptyIfNull(termBucketsFacetResponse.getSelectedBuckets()).stream()
					.map(SnTermBucketResponse::getId).collect(Collectors.toSet());

			target.setTopValues(ConverterUtils.convertAll(termBucketsFacetResponse.getTopBuckets(),
					bucket -> convertBucket(termBucketsFacetResponse, bucket, selectedBucketIds, searchQuery)));
			target.setValues(ConverterUtils.convertAll(termBucketsFacetResponse.getBuckets(),
					bucket -> convertBucket(termBucketsFacetResponse, bucket, selectedBucketIds, searchQuery)));
		}
		else if (source instanceof SnRangeBucketsFacetResponse)
		{
			final SnRangeBucketsFacetResponse rangeBucketsFacetResponse = (SnRangeBucketsFacetResponse) source;

			final Set<String> selectedBucketIds = CollectionUtils.emptyIfNull(rangeBucketsFacetResponse.getSelectedBuckets())
					.stream().map(SnRangeBucketResponse::getId).collect(Collectors.toSet());

			target.setValues(ConverterUtils.convertAll(rangeBucketsFacetResponse.getBuckets(),
					bucket -> convertBucket(rangeBucketsFacetResponse, bucket, selectedBucketIds, searchQuery)));
		}

		return target;
	}

	protected FacetValueData<SolrSearchQueryData> convertBucket(final AbstractSnBucketsFacetResponse facet,
			final AbstractSnBucketResponse bucket, final Set<String> selectedBucketIds, final SolrSearchQueryData searchQueryData)
	{
		final FacetValueData<SolrSearchQueryData> facetValueData = new FacetValueData<>();
		facetValueData.setCode(bucket.getId());
		facetValueData.setName(bucket.getName());
		facetValueData.setCount(bucket.getCount() != null ? bucket.getCount() : 0);
		facetValueData.setSelected(selectedBucketIds.contains(bucket.getId()));

		if (facetValueData.isSelected())
		{
			// Query to remove, rather than add facet
			facetValueData.setQuery(refineQueryRemoveFacet(searchQueryData, facet.getExpression(), bucket.getId()));
		}
		else
		{
			// Query to add the facet
			facetValueData.setQuery(refineQueryAddFacet(searchQueryData, facet.getExpression(), bucket.getId()));
		}

		return facetValueData;
	}

	protected SolrSearchQueryData refineQueryAddFacet(final SolrSearchQueryData searchQueryData, final String facet,
			final String facetValue)
	{
		final SolrSearchQueryTermData newTerm = new SolrSearchQueryTermData();
		newTerm.setKey(facet);
		newTerm.setValue(facetValue);

		final List<SolrSearchQueryTermData> filterTerms = getFilterTerms(searchQueryData);

		final List<SolrSearchQueryTermData> newTerms = new ArrayList<>(filterTerms);
		newTerms.add(newTerm);

		// Build the new query data
		final SolrSearchQueryData result = cloneSearchQueryData(searchQueryData);
		result.setFilterTerms(newTerms);
		return result;
	}

	protected SolrSearchQueryData refineQueryRemoveFacet(final SolrSearchQueryData searchQueryData, final String facet,
			final String facetValue)
	{
		final List<SolrSearchQueryTermData> newTerms = getFilterTerms(searchQueryData);

		// Remove the term for the specified facet
		final Iterator<SolrSearchQueryTermData> iterator = newTerms.iterator();
		while (iterator.hasNext())
		{
			final SolrSearchQueryTermData term = iterator.next();
			if (facet.equals(term.getKey()) && facetValue.equals(term.getValue()))
			{
				iterator.remove();
			}
		}

		// Build the new query data
		final SolrSearchQueryData result = cloneSearchQueryData(searchQueryData);
		result.setFilterTerms(newTerms);
		return result;
	}

	protected List<SolrSearchQueryTermData> getFilterTerms(final SolrSearchQueryData searchQueryData)
	{
		if(searchQueryData != null)
		{
			return new ArrayList<>(CollectionUtils.emptyIfNull(searchQueryData.getFilterTerms()));
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Shallow clone of the source SearchQueryData
	 *
	 * @param source the instance to clone
	 * @return the shallow clone
	 */
	protected SolrSearchQueryData cloneSearchQueryData(final SolrSearchQueryData source)
	{
		final SolrSearchQueryData target = new SolrSearchQueryData();
		if(source != null)
		{
			target.setFreeTextSearch(source.getFreeTextSearch());
			target.setCategoryCode(source.getCategoryCode());
			target.setSort(source.getSort());
			target.setFilterTerms(source.getFilterTerms());
		}
		return target;
	}
}
