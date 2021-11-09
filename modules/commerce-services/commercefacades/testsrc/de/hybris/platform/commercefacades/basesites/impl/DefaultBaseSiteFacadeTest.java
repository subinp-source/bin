/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basesites.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultBaseSiteFacadeTest
{

	@InjectMocks
	private final DefaultBaseSiteFacade baseSiteFacade = new DefaultBaseSiteFacade();

	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private BaseSiteModel baseSiteModel;
	@Mock
	private Converter<BaseSiteModel, BaseSiteData> baseSiteConverter;
	@Mock
	private BaseSiteData baseSiteData;

	@Before
	public void setUp() throws Exception
	{
		when(baseSiteService.getAllBaseSites()).thenReturn(Collections.singletonList(baseSiteModel));

		when(baseSiteConverter.convertAll(Collections.singletonList(baseSiteModel)))
				.thenReturn(Collections.singletonList(baseSiteData));
	}

	@Test
	public void getAllBaseSites()
	{
		//When
		final List<BaseSiteData> baseSiteDataList = baseSiteFacade.getAllBaseSites();
		//Then
		assertEquals(1, baseSiteDataList.size());
		assertThat(baseSiteDataList, hasItems(baseSiteData));
	}
}
