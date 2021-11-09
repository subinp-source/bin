/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.integration;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.indexer.impl.DefaultIndexerService;
import de.hybris.platform.solrfacetsearch.search.BoostField.BoostType;
import de.hybris.platform.solrfacetsearch.search.Document;
import de.hybris.platform.solrfacetsearch.search.FacetSearchService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchQuery.QueryOperator;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;


@IntegrationTest
public class SearchQueryBoostsTest extends AbstractIntegrationTest
{
	@Resource
	private CatalogVersionService catalogVersionService;

	@Resource
	private DefaultIndexerService indexerService;

	@Resource
	private FacetSearchService facetSearchService;

	private static final String PRODUCT_CODE_FIELD = "code";
	private static final String PRODUCT_STRING_FIELD = "stringField";
	private static final String PRODUCT_TEXT_FIELD = "textField";
	private static final String PRODUCT_BOOLEAN_FIELD = "booleanField";
	private static final String PRODUCT_INTEGER_FIELD = "integerField";
	private static final String PRODUCT_LONG_FIELD = "longField";
	private static final String PRODUCT_FLOAT_FIELD = "floatField";
	private static final String PRODUCT_DOUBLE_FIELD = "doubleField";
	private static final String PRODUCT_DATE_FIELD = "dateField";
	private static final String SCORE_FIELD = "score";


	private static final Float DEFAULT_SCORE = Float.valueOf(1);

	private static final String PRODUCT1_CODE = "product1";
	private static final String PRODUCT2_CODE = "product2";

	@Override
	protected void loadData()
			throws ImpExException, IOException, FacetConfigServiceException, SolrServiceException, SolrServerException
	{
		importConfig("/test/integration/SearchQueryBoostsTest.csv");
	}

	@Test
	public void defaultScore() throws Exception
	{
		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			// NOOP
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertScore(DEFAULT_SCORE, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void singleMultiplicativeBoost() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void multipleMultiplicativeBoosts() throws Exception
	{
		// given
		final Float boost1 = 2f;
		final Float boost2 = 3f;
		final Float expectedScore = DEFAULT_SCORE * boost1 * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost2, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void singleAdditiveBoost() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE + boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost, BoostType.ADDITIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void multipleAdditiveBoosts() throws Exception
	{
		// given
		final Float boost1 = 2f;
		final Float boost2 = 3f;
		final Float expectedScore = DEFAULT_SCORE + boost1 + boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.ADDITIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost2, BoostType.ADDITIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void multipleBoostsSameDocument1() throws Exception
	{
		// given
		final Float boost1 = 2f;
		final Float boost2 = 5f;
		final Float expectedScore = (DEFAULT_SCORE + boost1) * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.ADDITIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost2, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void multipleBoostsSameDocument2() throws Exception
	{
		// given
		final Float boost1 = 2f;
		final Float boost2 = 5f;
		final Float expectedScore = (DEFAULT_SCORE + boost1) * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost2, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.ADDITIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void multipleBoostsDifferentDocuments() throws Exception
	{
		// given
		final Float boost1 = 3f;
		final Float expectedScore1 = (DEFAULT_SCORE + boost1) * boost1;

		final Float boost2 = 4f;
		final Float expectedScore2 = (DEFAULT_SCORE + boost2) * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.ADDITIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT2_CODE, boost2, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT2_CODE, boost2, BoostType.ADDITIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore2, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(expectedScore1, document2);
	}

	@Test
	public void multipleBoostsDifferentDocumentsWithFilterQuery() throws Exception
	{
		// given
		final Float boost1 = 3f;

		final Float boost2 = 4f;
		final Float expectedScore2 = (DEFAULT_SCORE + boost2) * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.ADDITIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT2_CODE, boost2, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT2_CODE, boost2, BoostType.ADDITIVE);
			searchQuery.addFilterQuery(PRODUCT_CODE_FIELD, PRODUCT2_CODE);
		});

		// then
		assertEquals(1, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore2, document1);
	}

	@Test
	public void singleMultiplicativeFractionBoost() throws Exception
	{
		// given
		final Float boost = 0.5f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(DEFAULT_SCORE, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(expectedScore, document2);

	}

	@Test
	public void multipleMultiplicativeFractionBoosts() throws Exception
	{
		// given
		final Float boost1 = 0.05f;
		final Float boost2 = 0.2f;
		final Float expectedScore = DEFAULT_SCORE * boost1 * boost2;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost1, BoostType.MULTIPLICATIVE);
			searchQuery.addBoost(PRODUCT_CODE_FIELD, QueryOperator.EQUAL_TO, PRODUCT1_CODE, boost2, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(DEFAULT_SCORE, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(expectedScore, document2);
	}

	@Test
	public void boostOnStringProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_STRING_FIELD, QueryOperator.MATCHES, "a", boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);

	}

	@Test
	public void boostOnStringProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_STRING_FIELD, QueryOperator.MATCHES, "b", boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnTextProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_TEXT_FIELD, QueryOperator.MATCHES, "a", boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnTextProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_TEXT_FIELD, QueryOperator.MATCHES, "b", boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnBooleanProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_BOOLEAN_FIELD, QueryOperator.EQUAL_TO, Boolean.FALSE, boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnBooleanProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_BOOLEAN_FIELD, QueryOperator.EQUAL_TO, Boolean.TRUE, boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnIntegerProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_INTEGER_FIELD, QueryOperator.EQUAL_TO, Integer.valueOf(1), boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnIntegerProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_INTEGER_FIELD, QueryOperator.EQUAL_TO, Integer.valueOf(2), boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnLongProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_LONG_FIELD, QueryOperator.EQUAL_TO, Long.valueOf(1), boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnLongProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_LONG_FIELD, QueryOperator.EQUAL_TO, Long.valueOf(2), boost, BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnFloatProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_FLOAT_FIELD, QueryOperator.LESS_THAN, Float.valueOf(1.5f), boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnFloatProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_FLOAT_FIELD, QueryOperator.GREATER_THAN, Float.valueOf(1.5f), boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnDoubleProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_DOUBLE_FIELD, QueryOperator.LESS_THAN, Double.valueOf(1.5d), boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnDoubleProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_DOUBLE_FIELD, QueryOperator.GREATER_THAN, Double.valueOf(1.5d), boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnDateProperty1() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_DATE_FIELD, QueryOperator.LESS_THAN, "2019-01-01T00:00:00.000+01:00", boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT1_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT2_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	@Test
	public void boostOnDateProperty2() throws Exception
	{
		// given
		final Float boost = 2f;
		final Float expectedScore = DEFAULT_SCORE * boost;

		// when
		final SearchResult searchResult = executeSearchQuery(searchQuery -> {
			searchQuery.addBoost(PRODUCT_DATE_FIELD, QueryOperator.GREATER_THAN, "2019-01-01T00:00:00.000+01:00", boost,
					BoostType.MULTIPLICATIVE);
		});

		// then
		assertEquals(2, searchResult.getNumberOfResults());

		final Document document1 = searchResult.getDocuments().get(0);
		assertProductCode(PRODUCT2_CODE, document1);
		assertScore(expectedScore, document1);

		final Document document2 = searchResult.getDocuments().get(1);
		assertProductCode(PRODUCT1_CODE, document2);
		assertScore(DEFAULT_SCORE, document2);
	}

	protected SearchResult executeSearchQuery(final Consumer<SearchQuery> action) throws Exception
	{
		final CatalogVersionModel onlineCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				ONLINE_CATALOG_VERSION + getTestId());
		final CatalogVersionModel stagedCatalogVersion = catalogVersionService.getCatalogVersion(HW_CATALOG,
				STAGED_CATALOG_VERSION + getTestId());

		final FacetSearchConfig facetSearchConfig = getFacetSearchConfig();
		final IndexedType indexedType = facetSearchConfig.getIndexConfig().getIndexedTypes().values().iterator().next();

		indexerService.performFullIndex(facetSearchConfig);

		final SearchQuery searchQuery = facetSearchService.createPopulatedSearchQuery(facetSearchConfig, indexedType);
		searchQuery.setCatalogVersions(Arrays.asList(onlineCatalogVersion, stagedCatalogVersion));
		searchQuery.getFields().addAll(Arrays.asList(SCORE_FIELD, PRODUCT_DATE_FIELD, PRODUCT_BOOLEAN_FIELD, PRODUCT_CODE_FIELD));

		action.accept(searchQuery);

		return facetSearchService.search(searchQuery);
	}

	protected void assertProductCode(final String expectedProductCode, final Document document)
	{
		assertEquals(expectedProductCode, document.getFields().get(PRODUCT_CODE_FIELD));
	}

	protected void assertScore(final float expectedScore, final Document document)
	{
		assertEquals(Float.valueOf(expectedScore), document.getFields().get(SCORE_FIELD));
	}
}
