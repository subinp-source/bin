/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.solrfacetsearch.solr.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.daos.SolrIndexOperationDao;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultSolrIndexOperationServiceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Mock
	private SolrIndexOperationDao solrIndexOperationDao;

	@Mock
	private ModelService modelService;

	@Mock
	private TimeService timeService;

	private DefaultSolrIndexOperationService solrIndexOperationService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		solrIndexOperationService = new DefaultSolrIndexOperationService();
		solrIndexOperationService.setSolrIndexOperationDao(solrIndexOperationDao);
		solrIndexOperationService.setModelService(modelService);
		solrIndexOperationService.setTimeService(timeService);
	}

	@Test
	public void startOperationWithInvalidIndex() throws Exception
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		solrIndexOperationService.startOperation(null, 2, IndexOperation.UPDATE, false);
	}

	@Test
	public void getLastIndexOperationTimeWithInvalidIndex() throws Exception
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		solrIndexOperationService.getLastIndexOperationTime(null);
	}
}
