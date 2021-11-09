/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.resolvers;

import static org.mockito.AdditionalMatchers.lt;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.proxy.DataQualityCalculationServiceProxy;


@RunWith(MockitoJUnitRunner.class)
public class DataQualityValueResolverTest
{

	@InjectMocks
	private DataQualityValueResolver testSubject;

	@Mock
	private DataQualityCalculationServiceProxy dataQualityCalculationServiceProxy;

	@Mock
	private QualifierProvider qualifierProvider;

	@Mock
	private SessionService sessionService;

	@Mock
	private JaloSession jaloSession;

	@Before
	public void setUp() throws Exception
	{
		when(sessionService.getRawSession(any())).thenReturn(jaloSession);
	}

	@Test
	public void shouldNotAllowNegativeDataQuality() throws Exception
	{
		// given
		final InputDocument document = mock(InputDocument.class);
		final IndexedProperty property = mock(IndexedProperty.class);
		when(dataQualityCalculationServiceProxy.calculate(any(), any())).thenReturn(Optional.of(-1.0));

		// when
		testSubject.resolve(document, mock(IndexerBatchContext.class), Collections.singletonList(property), mock(ItemModel.class));

		// then
		verify(document, never()).addField(any(), lt(0.0), any());
		verify(document).addField(any(), eq(0.0), any());
	}
}
