/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.cmsitems.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commercefacades.basesite.data.BaseSiteData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AccBaseSitePopulatorTest
{

	public static final String STOREFRONT_URL_ENCODING_ATTRIBUTE = "storefront";
	public static final String LANGUAGE_URL_ENCODING_ATTRIBUTE = "language";
	public static final List<String> URL_ENCODING_ATTRIBUTES = Collections
			.unmodifiableList(Arrays.asList(STOREFRONT_URL_ENCODING_ATTRIBUTE, LANGUAGE_URL_ENCODING_ATTRIBUTE));

	@InjectMocks
	private final AccBaseSitePopulator accBaseSitePopulator = new AccBaseSitePopulator();

	@Mock
	private CMSSiteModel cmsSiteModel;
	private BaseSiteData baseSiteData;

	@Before
	public void setUp()
	{
		baseSiteData = new BaseSiteData();
		when(cmsSiteModel.getUrlEncodingAttributes()).thenReturn(URL_ENCODING_ATTRIBUTES);
	}

	@Test
	public void populate()
	{
		accBaseSitePopulator.populate(cmsSiteModel, baseSiteData);
		final List<String> urlEncodingAttributes = baseSiteData.getUrlEncodingAttributes();
		assertThat(urlEncodingAttributes, is(URL_ENCODING_ATTRIBUTES));
		assertThat(urlEncodingAttributes, contains(STOREFRONT_URL_ENCODING_ATTRIBUTE, LANGUAGE_URL_ENCODING_ATTRIBUTE));
	}
}
