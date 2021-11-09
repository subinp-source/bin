/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.QuoteEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationCopyStrategy;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultProductConfigCloneAbstractOrderHookTest
{
	public static class CartModelSubClass extends CartModel
	{
		//
	}

	public static class QuoteModelSubClass extends QuoteModel
	{
		//
	}

	@InjectMocks
	private DefaultProductConfigCloneAbstractOrderHook classUnderTest;
	@Mock
	private ConfigurationCopyStrategy configCopyStrategy;
	@Mock
	private CartModel original;
	@Mock
	private QuoteModel originalQuote;
	@Mock
	private OrderModel originalOrder;
	@Mock
	private AbstractOrderModel clonedAbstractOrder;
	@Mock
	private ConfigurationAbstractOrderEntryLinkStrategy configurationAbstractOrderEntryLinkStrategy;
	@Mock
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;
	@Mock
	private AbstractOrderEntryModel entry;
	@Mock
	private CartModelSubClass originalSubClass;
	@Mock
	private QuoteModelSubClass originalQuoteSubClass;

	private final Class abstractOrderClassResult = QuoteModel.class;
	private final Class abstractOrderClassResultOrderModel = OrderModel.class;
	private final Class abstractOrderClassResultCartModel = CartModel.class;
	private final Class abstractOrderEntryClassResult = QuoteEntryModel.class;
	private static final long cartKey = 123;
	private final PK cartEntryPk = PK.fromLong(cartKey);
	private static final String configId = "S1";

	@Before
	public void initialize()
	{
		when(original.getEntries()).thenReturn(Arrays.asList(entry));
		when(originalQuote.getEntries()).thenReturn(Arrays.asList(entry));
		when(entry.getPk()).thenReturn(cartEntryPk);
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(String.valueOf(cartKey))).thenReturn(configId);
	}



	@Test
	public void testIsCleanupNeeded()
	{
		assertTrue(classUnderTest.isCleanUpNeeded(original, abstractOrderClassResult));
	}

	@Test
	public void testIsCleanupNeededQuoteToQuote()
	{
		assertFalse(classUnderTest.isCleanUpNeeded(originalQuote, abstractOrderClassResult));
	}

	@Test
	public void testIsCleanupNeededCartToOrder()
	{
		assertFalse(classUnderTest.isCleanUpNeeded(original, OrderModel.class));
	}

	@Test
	public void testIsCleanupNeededSubClasses()
	{

		class QuoteModelSubClass extends QuoteModel
		{
			//
		}

		assertTrue(classUnderTest.isCleanUpNeeded(originalSubClass, QuoteModelSubClass.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCleanupArgumentNull()
	{
		classUnderTest.cleanUp(null);
	}

	@Test
	public void testCleanUp()
	{
		classUnderTest.cleanUp(original);
		verify(original).getEntries();
	}


	@Test
	public void testCleanUpEntryRemoveSessionArtifacts()
	{
		classUnderTest.cleanUpEntry(entry);
		verify(configurationAbstractOrderIntegrationStrategy).finalizeCartEntry(entry);
	}

	@Test
	public void testCleanUpEntryRemoveSessionArtifactsNullPk()
	{
		when(entry.getPk()).thenReturn(null);
		classUnderTest.cleanUpEntry(entry);
		verify(configurationAbstractOrderEntryLinkStrategy, never()).removeSessionArtifactsForCartEntry(String.valueOf(cartKey));
	}

	@Test
	public void testCleanUpEntryNonConfigurable()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(String.valueOf(cartKey))).thenReturn(null);
		classUnderTest.cleanUpEntry(entry);
	}

	@Test
	public void testCleanUpEntryNonConfigurableNoRemovalSessionArtifacts()
	{
		when(configurationAbstractOrderEntryLinkStrategy.getConfigIdForCartEntry(String.valueOf(cartKey))).thenReturn(null);
		classUnderTest.cleanUpEntry(entry);
		verify(configurationAbstractOrderEntryLinkStrategy, never()).removeSessionArtifactsForCartEntry(String.valueOf(cartKey));
	}

	@Test
	public void testCloneEntries()
	{
		classUnderTest.beforeCloneEntries(original);
		verify(configurationAbstractOrderIntegrationStrategy).finalizeCartEntry(entry);
	}

	@Test
	public void testCloneEntriesWrongSourceType()
	{
		classUnderTest.beforeCloneEntries(originalQuote);
		verify(configurationAbstractOrderIntegrationStrategy, never()).finalizeCartEntry(entry);
	}

	@Test
	public void testCleanUpNeededOnlySource()
	{
		assertTrue(classUnderTest.isCleanUpNeeded(original));
	}

	@Test
	public void testCleanUpNeededOnlySourceSubclass()
	{
		assertTrue(classUnderTest.isCleanUpNeeded(originalSubClass));
	}

	@Test
	public void testCleanUpNeededOnlyWrongSource()
	{
		assertFalse(classUnderTest.isCleanUpNeeded(originalQuote));
	}

	@Test
	public void testCloneFromQuoteToCart()
	{
		classUnderTest.afterClone(originalQuote, clonedAbstractOrder, abstractOrderClassResultCartModel);
		verify(configCopyStrategy).finalizeClone(any(), any());
	}

	@Test
	public void testCloneFromOrderToCart()
	{
		classUnderTest.afterClone(originalOrder, clonedAbstractOrder, abstractOrderClassResultCartModel);
		verify(configCopyStrategy).finalizeClone(any(), any());
	}

	@Test
	public void testIsFinalizeCloneNeededFromQuoteToCart()
	{
		assertTrue(classUnderTest.isFinalizeCloneNeeded(originalQuote, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsFinalizeCloneNeededFromOrderToCart()
	{
		assertTrue(classUnderTest.isFinalizeCloneNeeded(originalOrder, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsFinalizeCloneNeededFromQuoteToQuote()
	{
		assertTrue(classUnderTest.isFinalizeCloneNeeded(originalQuote, abstractOrderClassResult));
	}

	@Test
	public void testIsFinalizeCloneNeededFromOrderToOrder()
	{
		assertFalse(classUnderTest.isFinalizeCloneNeeded(originalOrder, abstractOrderClassResultOrderModel));
	}

	@Test
	public void testIsFinalizeCloneNeededFromCartToCart()
	{
		assertTrue(classUnderTest.isFinalizeCloneNeeded(original, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsFinalizeCloneNeededSubClasses()
	{
		assertTrue(classUnderTest.isFinalizeCloneNeeded(originalQuoteSubClass, CartModelSubClass.class));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromCartToCart()
	{
		assertTrue(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(original, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromQuoteToCart()
	{
		assertTrue(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(originalQuote, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromOrderToCart()
	{
		assertTrue(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(originalOrder, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromOrderToOrder()
	{
		assertFalse(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(originalOrder, abstractOrderClassResultOrderModel));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromQuoteToQuote()
	{
		assertFalse(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(originalQuote, abstractOrderClassResult));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromCartToOrder()
	{
		assertFalse(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(original, abstractOrderClassResultOrderModel));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromCartToQuote()
	{
		assertFalse(classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(original, abstractOrderClassResult));
	}

	@Test
	public void testIsQuoteOrOrderOrCartToCartCloneProcessFromOtherToCart()
	{
		assertFalse(
				classUnderTest.isQuoteOrOrderOrCartToCartCloneProcess(new AbstractOrderModel(), abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsQuoteToQuoteCloneProcessFromQuoteToQuote()
	{
		assertTrue(classUnderTest.isQuoteToQuoteCloneProcess(originalQuote, abstractOrderClassResult));
	}

	@Test
	public void testIsQuoteToQuoteCloneProcessFromQuoteToCart()
	{
		assertFalse(classUnderTest.isQuoteToQuoteCloneProcess(originalQuote, abstractOrderClassResultCartModel));
	}

	@Test
	public void testIsQuoteToQuoteCloneProcessFromCartToQuote()
	{
		assertFalse(classUnderTest.isQuoteToQuoteCloneProcess(original, abstractOrderClassResult));
	}

	@Test
	public void testBeforeCloneDoesNothing()
	{
		classUnderTest.beforeClone(null, null);
		verifyZeroInteractions(configurationAbstractOrderEntryLinkStrategy, configurationAbstractOrderIntegrationStrategy);
	}

	@Test
	public void testAfterCloneEntriesDoesNothing()
	{
		classUnderTest.afterCloneEntries(null, null);
		verifyZeroInteractions(configurationAbstractOrderEntryLinkStrategy, configurationAbstractOrderIntegrationStrategy);
	}
}
