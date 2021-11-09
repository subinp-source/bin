/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.search

import de.hybris.platform.searchservices.constants.SearchservicesConstants
import de.hybris.platform.searchservices.integration.AbstractSnIntegrationSpec
import de.hybris.platform.searchservices.search.data.AbstractSnFacetFilter
import de.hybris.platform.searchservices.search.data.AbstractSnFacetRequest
import de.hybris.platform.searchservices.search.data.AbstractSnFacetResponse
import de.hybris.platform.searchservices.search.data.AbstractSnQuery
import de.hybris.platform.searchservices.search.data.AbstractSnRankRule
import de.hybris.platform.searchservices.search.data.SnAndQuery
import de.hybris.platform.searchservices.search.data.SnBucketsFacetFilter
import de.hybris.platform.searchservices.search.data.SnEqualQuery
import de.hybris.platform.searchservices.search.data.SnGreaterThanOrEqualQuery
import de.hybris.platform.searchservices.search.data.SnGreaterThanQuery
import de.hybris.platform.searchservices.search.data.SnLessThanOrEqualQuery
import de.hybris.platform.searchservices.search.data.SnLessThanQuery
import de.hybris.platform.searchservices.search.data.SnMatchQuery
import de.hybris.platform.searchservices.search.data.SnMatchTermQuery
import de.hybris.platform.searchservices.search.data.SnMatchTermsQuery
import de.hybris.platform.searchservices.search.data.SnNotEqualQuery
import de.hybris.platform.searchservices.search.data.SnNotQuery
import de.hybris.platform.searchservices.search.data.SnOrQuery
import de.hybris.platform.searchservices.search.data.SnPromotedHitsRankRule
import de.hybris.platform.searchservices.search.data.SnQueryFunctionRankRule
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetRequest
import de.hybris.platform.searchservices.search.data.SnRangeBucketsFacetResponse
import de.hybris.platform.searchservices.search.data.SnRangeQuery
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.data.SnSearchResult
import de.hybris.platform.searchservices.search.data.SnSort
import de.hybris.platform.searchservices.search.data.SnSortExpression
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetRequest
import de.hybris.platform.searchservices.search.data.SnTermBucketsFacetResponse
import de.hybris.platform.searchservices.search.service.SnSearchRequest
import de.hybris.platform.searchservices.search.service.SnSearchResponse
import de.hybris.platform.searchservices.search.service.SnSearchService

import java.nio.charset.StandardCharsets
import java.util.function.Consumer

import javax.annotation.Resource

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory

import spock.lang.Shared


abstract class AbstractSnSearchSpec extends AbstractSnIntegrationSpec {

	protected static final String DOC1_ID = "doc1"
	protected static final String DOC1_CODE = "doc1"
	protected static final String DOC1_COLOR = "RED"
	protected static final String DOC1_SIZE = "M"

	protected static final String DOC2_ID = "doc2"
	protected static final String DOC2_CODE = "doc2"
	protected static final String DOC2_COLOR = "GREEN"
	protected static final String DOC2_SIZE = "M"

	protected static final String DOC3_ID = "doc3"
	protected static final String DOC3_CODE = "doc3"
	protected static final String DOC3_COLOR = "GREEN"
	protected static final String DOC3_SIZE = "L"

	protected static final String DOC4_ID = "doc4"

	@Resource
	SnSearchService snSearchService

	@Shared
	ObjectMapper objectMapper

	def setupSpec() {
		objectMapper = new ObjectMapper()
		objectMapper.addMixIn(AbstractSnQuery, SnQueryMixin)
		objectMapper.addMixIn(AbstractSnRankRule, SnRankRuleMixin)
		objectMapper.addMixIn(AbstractSnFacetRequest, SnFacetRequestMixin)
		objectMapper.addMixIn(AbstractSnFacetResponse, SnFacetResponseMixin)
		objectMapper.addMixIn(AbstractSnFacetFilter, SnFacetFilterMixin)
	}

	def loadCoreData() {
		importData("/searchservices/test/integration/snLanguages.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCurrencies.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfiguration.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfigurationAddLanguages.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfigurationAddCurrencies.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexType.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexTypeAddFields.impex", StandardCharsets.UTF_8.name())
	}

	def loadDefaultData() {
		importData("/searchservices/test/integration/snCatalogBase.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogSampleProducts.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/search/snIndexTypeUpdatesForSearch.impex", StandardCharsets.UTF_8.name())
	}

	SnSearchResult executeSearchQuery(String indexTypeId, Closure creator, Closure... decorators) {
		final SnSearchQuery searchQuery = creator()

		for (Consumer<SnSearchQuery> decorator : decorators) {
			decorator.accept(searchQuery)
		}

		final SnSearchRequest searchRequest = snSearchService.createSearchRequest(indexTypeId, searchQuery)
		final SnSearchResponse searchResponse = snSearchService.search(searchRequest)

		return searchResponse.getSearchResult()
	}

	SnSearchQuery createSearchQuery() {
		return new SnSearchQuery()
	}

	def addDefaultSearchQuerySort(SnSearchQuery searchQuery) {
		SnSortExpression scoreSortExpression = new SnSortExpression()
		scoreSortExpression.setExpression(SearchservicesConstants.SCORE_FIELD)
		scoreSortExpression.setAscending(false)

		SnSortExpression codeSortExpression = new SnSortExpression()
		codeSortExpression.setExpression(SearchservicesConstants.ID_FIELD)
		codeSortExpression.setAscending(true)

		SnSort sort = new SnSort()
		sort.setApplyPromotedHits(true)
		sort.setHighlightPromotedHits(true)
		sort.setExpressions(List.of(scoreSortExpression, codeSortExpression))

		searchQuery.setSort(sort)
	}

	List<SnSearchSpec> loadSearchSpecJson(String resource) {
		URL url = AbstractSnSearchSpec.class.getResource(resource)
		final TypeFactory typeFactory = objectMapper.getTypeFactory()
		final CollectionType type = typeFactory.constructCollectionType(List.class, SnSearchSpec.class)

		return objectMapper.readValue(url, type)
	}

	protected final List<SnQuerySpec> loadQuerySpecJson(String resource) {
		URL url = AbstractSnSearchDataDrivenSpec.class.getResource(resource)
		final TypeFactory typeFactory = objectMapper.getTypeFactory()
		final CollectionType type = typeFactory.constructCollectionType(List.class, SnQuerySpec.class)

		return objectMapper.readValue(url, type)
	}

	static class SnSearchSpec {
		String testId
		List<String> languages
		List<Map<String, Object>> fields
		String description
		SnSearchQuery data
		Object expectedData
	}

	static class SnQuerySpec {
		String testId
		String description
		AbstractSnQuery data
		List<String> matchingDocuments
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes([
		@JsonSubTypes.Type(value = SnAndQuery, name = SnAndQuery.TYPE),
		@JsonSubTypes.Type(value = SnOrQuery, name = SnOrQuery.TYPE),
		@JsonSubTypes.Type(value = SnNotQuery, name = SnNotQuery.TYPE),
		@JsonSubTypes.Type(value = SnEqualQuery, name = SnEqualQuery.TYPE),
		@JsonSubTypes.Type(value = SnNotEqualQuery, name = SnNotEqualQuery.TYPE),
		@JsonSubTypes.Type(value = SnMatchTermQuery, name = SnMatchTermQuery.TYPE),
		@JsonSubTypes.Type(value = SnMatchTermsQuery, name = SnMatchTermsQuery.TYPE),
		@JsonSubTypes.Type(value = SnMatchQuery, name = SnMatchQuery.TYPE),
		@JsonSubTypes.Type(value = SnGreaterThanOrEqualQuery, name = SnGreaterThanOrEqualQuery.TYPE),
		@JsonSubTypes.Type(value = SnGreaterThanQuery, name = SnGreaterThanQuery.TYPE),
		@JsonSubTypes.Type(value = SnLessThanOrEqualQuery, name = SnLessThanOrEqualQuery.TYPE),
		@JsonSubTypes.Type(value = SnLessThanQuery, name = SnLessThanQuery.TYPE),
		@JsonSubTypes.Type(value = SnRangeQuery, name = SnRangeQuery.TYPE)
	])
	static class SnQueryMixin {
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes([
		@JsonSubTypes.Type(value = SnQueryFunctionRankRule, name = SnQueryFunctionRankRule.TYPE),
		@JsonSubTypes.Type(value = SnPromotedHitsRankRule, name = SnPromotedHitsRankRule.TYPE)
	])
	static class SnRankRuleMixin {
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes([
		@JsonSubTypes.Type(value = SnTermBucketsFacetRequest, name = SnTermBucketsFacetRequest.TYPE),
		@JsonSubTypes.Type(value = SnRangeBucketsFacetRequest, name = SnRangeBucketsFacetRequest.TYPE)
	])
	static class SnFacetRequestMixin {
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes([
		@JsonSubTypes.Type(value = SnTermBucketsFacetResponse, name = SnTermBucketsFacetResponse.TYPE),
		@JsonSubTypes.Type(value = SnRangeBucketsFacetResponse, name = SnRangeBucketsFacetResponse.TYPE)
	])
	static class SnFacetResponseMixin {
	}

	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
	@JsonSubTypes([
		@JsonSubTypes.Type(value = SnBucketsFacetFilter, name = SnBucketsFacetFilter.TYPE),
	])
	static class SnFacetFilterMixin {
	}
}
