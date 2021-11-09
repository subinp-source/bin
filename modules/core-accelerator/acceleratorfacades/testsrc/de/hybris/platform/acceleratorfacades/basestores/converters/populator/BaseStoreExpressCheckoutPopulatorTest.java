/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.basestores.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.store.BaseStoreModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


/**
 * Unit test for {@link BaseStoreExpressCheckoutPopulator}
 */
@UnitTest
public class BaseStoreExpressCheckoutPopulatorTest
{

	@Mock
	private BaseStoreModel baseStoreModel;
	private BaseStoreData baseStoreData;
	private BaseStoreExpressCheckoutPopulator populator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.baseStoreData = new BaseStoreData();
		this.populator = new BaseStoreExpressCheckoutPopulator();
		when(baseStoreModel.getExpressCheckoutEnabled()).thenReturn(Boolean.TRUE);
	}

	@Test
	public void testPopulateExpressCheckout()
	{
		this.populator.populate(baseStoreModel, baseStoreData);
		assertTrue("Wrong express checkout value", baseStoreData.isExpressCheckoutEnabled());
	}

}
