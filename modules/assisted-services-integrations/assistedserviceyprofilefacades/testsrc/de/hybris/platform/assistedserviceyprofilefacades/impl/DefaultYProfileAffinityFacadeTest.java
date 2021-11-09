/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedserviceyprofilefacades.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedserviceyprofilefacades.data.CategoryAffinityParameterData;
import de.hybris.platform.assistedserviceyprofilefacades.data.ProductAffinityParameterData;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.yaasyprofileconnect.constants.YaasyprofileconnectConstants;
import de.hybris.platform.yaasyprofileconnect.yaas.Affinities;
import de.hybris.platform.yaasyprofileconnect.yaas.Affinity;
import de.hybris.platform.yaasyprofileconnect.yaas.Insights;
import de.hybris.platform.yaasyprofileconnect.yaas.Profile;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class DefaultYProfileAffinityFacadeTest extends ServicelayerTransactionalTest
{
	@Resource(name = "yProfileAffinityFacade")
	private DefaultYProfileAffinityFacade defaultYProfileAffinityFacade;

	@Test
	public void getProductAffinitiesTest()
	{
		final DefaultYProfileAffinityFacade spy = spy(defaultYProfileAffinityFacade);
		when(spy.getProfileData(YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY)).thenReturn(Optional.empty());

		assertTrue(spy.getProductAffinities(null).isEmpty());

		final Profile profile = new Profile();
		when(spy.getProfileData(YaasyprofileconnectConstants.SCHEMA_COMMERCE_PRODUCT_AFFINITY)).thenReturn(Optional.of(profile));

		assertTrue(spy.getProductAffinities(null).isEmpty());

		final Insights insights = new Insights();
		profile.setInsights(insights);

		assertTrue(spy.getProductAffinities(null).isEmpty());

		final Affinities affinities = new Affinities();
		insights.setAffinities(affinities);
		assertTrue(spy.getProductAffinities(null).isEmpty());


		final Map<String, Affinity> products = new LinkedHashMap<>();
		products.put("1", new Affinity());
		affinities.setProducts(products);
		final ProductAffinityParameterData parameterData = new ProductAffinityParameterData();
		parameterData.setSizeLimit(10);

		assertEquals(1, spy.getProductAffinities(parameterData).size());

	}

	@Test
	public void getCategoryAffinitiesTest()
	{
		final DefaultYProfileAffinityFacade spy = spy(defaultYProfileAffinityFacade);
		when(spy.getProfileData(YaasyprofileconnectConstants.SCHEMA_COMMERCE_CATEGORY_AFFINITY)).thenReturn(Optional.empty());

		assertTrue(spy.getCategoryAffinities(null).isEmpty());

		final Profile profile = new Profile();
		when(spy.getProfileData(YaasyprofileconnectConstants.SCHEMA_COMMERCE_CATEGORY_AFFINITY)).thenReturn(Optional.of(profile));

		assertTrue(spy.getCategoryAffinities(null).isEmpty());

		final Insights insights = new Insights();
		profile.setInsights(insights);

		assertTrue(spy.getCategoryAffinities(null).isEmpty());

		final Affinities affinities = new Affinities();
		insights.setAffinities(affinities);
		assertTrue(spy.getProductAffinities(null).isEmpty());


		final Map<String, Affinity> categories = new LinkedHashMap<>();
		categories.put("1", new Affinity());
		affinities.setCategories(categories);
		final CategoryAffinityParameterData parameterData = new CategoryAffinityParameterData();
		parameterData.setSizeLimit(10);

		assertEquals(1, spy.getCategoryAffinities(parameterData).size());
	}
}
