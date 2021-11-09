/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.services.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationPricingStrategy;
import de.hybris.platform.sap.productconfig.services.strategies.intf.ProductConfigurationCartEntryValidationStrategy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@SuppressWarnings("javadoc")
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCartValidationHookTest
{
	@Mock
	private ProductConfigurationPricingStrategy productConfigurationPricingStrategy;
	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;
	@Mock
	private ProductConfigurationCartEntryValidationStrategy productConfigurationCartEntryValidationStrategy;
	@InjectMocks
	private DefaultCartValidationHook classUnderTest;
	private final CommerceCartParameter parameter = new CommerceCartParameter();
	private final List<CommerceCartModification> modifications = new ArrayList<CommerceCartModification>();
	private final CommerceCartModification successfulModification = new CommerceCartModification();
	private final CommerceCartModification errorModification = new CommerceCartModification();
	@Mock
	private CartModel cart;
	@Mock
	private AbstractOrderEntryModel entry1;
	@Mock
	private AbstractOrderEntryModel entry2;
	@Mock
	private ProductModel product1;
	@Mock
	private ProductModel product2;

	@Before
	public void setup()
	{
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(product1)).thenReturn(true);
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(product2)).thenReturn(false);
		when(entry1.getProduct()).thenReturn(product1);
		when(entry2.getProduct()).thenReturn(product2);
		when(entry1.getPk()).thenReturn(PK.fromLong(1));
		when(entry2.getPk()).thenReturn(PK.fromLong(2));
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		entries.add(entry1);
		entries.add(entry2);
		when(cart.getEntries()).thenReturn(entries);
		parameter.setCart(cart);

		successfulModification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
		successfulModification.setEntry(entry1);
		errorModification.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
		errorModification.setEntry(entry2);
		modifications.add(successfulModification);
		modifications.add(errorModification);
	}

	@Test
	public void testBeforeValidateHook()
	{
		classUnderTest.beforeValidateCart(parameter, modifications);
		verify(productConfigurationPricingStrategy).updateCartEntryPrices(entry1, false, null);
		verify(productConfigurationPricingStrategy, times(0)).updateCartEntryPrices(entry2, false, null);
		verify(commerceCartService).calculateCart(parameter);
	}

	@Test
	public void testAfterValidateHookNoConfigurationProblems()
	{
		classUnderTest.afterValidateCart(parameter, modifications);
		assertEquals(2, modifications.size());
		assertEquals(CommerceCartModificationStatus.SUCCESS, modifications.get(0).getStatusCode());
		assertEquals(CommerceCartModificationStatus.NO_STOCK, modifications.get(1).getStatusCode());
	}

	@Test
	public void testAfterValidateHookConfigurationProblem()
	{
		final CommerceCartModification configurationModification = new CommerceCartModification();
		configurationModification.setStatusCode(CommerceCartModificationStatus.CONFIGURATION_ERROR);
		configurationModification.setEntry(entry1);
		when(productConfigurationCartEntryValidationStrategy.validateConfiguration(entry1)).thenReturn(configurationModification);
		classUnderTest.afterValidateCart(parameter, modifications);
		assertEquals(2, modifications.size());
		assertEquals(CommerceCartModificationStatus.NO_STOCK, modifications.get(0).getStatusCode());
		assertEquals(entry2, modifications.get(0).getEntry());
		assertEquals(CommerceCartModificationStatus.CONFIGURATION_ERROR, modifications.get(1).getStatusCode());
		assertEquals(entry1, modifications.get(1).getEntry());
	}

	@Test
	public void testAfterValidateHookIgnoreConfigurableEntryWithOtherProblem()
	{
		final CommerceCartModification configurationModification = new CommerceCartModification();
		configurationModification.setStatusCode(CommerceCartModificationStatus.CONFIGURATION_ERROR);
		configurationModification.setEntry(entry1);
		when(productConfigurationCartEntryValidationStrategy.validateConfiguration(entry1)).thenReturn(configurationModification);

		final CommerceCartModification anotherConfigurationModification = new CommerceCartModification();
		anotherConfigurationModification.setStatusCode(CommerceCartModificationStatus.CONFIGURATION_ERROR);
		anotherConfigurationModification.setEntry(entry2);
		when(productConfigurationCartEntryValidationStrategy.validateConfiguration(entry2))
				.thenReturn(anotherConfigurationModification);

		// second product is also configurable
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(product2)).thenReturn(true);


		classUnderTest.afterValidateCart(parameter, modifications);
		assertEquals(2, modifications.size());
		assertEquals(CommerceCartModificationStatus.NO_STOCK, modifications.get(0).getStatusCode());
		assertEquals(entry2, modifications.get(0).getEntry());
		assertEquals(CommerceCartModificationStatus.CONFIGURATION_ERROR, modifications.get(1).getStatusCode());
		assertEquals(entry1, modifications.get(1).getEntry());
	}

	@Test(expected = IllegalStateException.class)
	public void testRetrieveModificationForEntryNotFound()
	{
		final AbstractOrderEntryModel anotherEntry = mock(AbstractOrderEntryModel.class);
		when(anotherEntry.getPk()).thenReturn(PK.fromLong(3));
		classUnderTest.retrieveModificationForEntry(anotherEntry, modifications);
	}
}
