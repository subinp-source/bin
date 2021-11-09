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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.order.exceptions.IllegalQuoteStateException;
import de.hybris.platform.commerceservices.order.strategies.QuoteActionValidationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.data.QuoteEntryStatus;
import de.hybris.platform.sap.productconfig.services.impl.CPQConfigurableChecker;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderIntegrationStrategy;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class ProductConfigurationQuoteActionValidationStrategyImplTest
{
	private static final String PRODUCT_CODE = "product";
	private final ProductConfigurationQuoteActionValidationStrategyImpl classUnderTest = new ProductConfigurationQuoteActionValidationStrategyImpl();
	private final String quoteCode = "1224";
	@Mock
	private QuoteActionValidationStrategy quoteActionValidationStrategy;
	@Mock
	private ConfigurationAbstractOrderIntegrationStrategy configurationAbstractOrderIntegrationStrategy;
	@Mock
	private QuoteModel quoteModel;
	@Mock
	private CustomerModel customerModel;
	@Mock
	private BaseStoreModel baseStore;
	@Mock
	private AbstractOrderEntryModel quoteEntry;
	@Mock
	private ConfigModel configurationModel;
	@Mock
	private CPQConfigurableChecker cpqConfigurableChecker;
	@Mock
	private ProductModel productModel;

	private final QuoteAction quoteAction = QuoteAction.CHECKOUT;



	@Before
	public void initialize()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setDefaultQuoteActionValidationStrategy(quoteActionValidationStrategy);
		classUnderTest.setConfigurationAbstractOrderIntegrationStrategy(configurationAbstractOrderIntegrationStrategy);

		classUnderTest.setCpqConfigurableChecker(cpqConfigurableChecker);

		when(quoteModel.getEntries()).thenReturn(Collections.singletonList(quoteEntry));
		when(quoteEntry.getProduct()).thenReturn(productModel);
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);
		when(configurationAbstractOrderIntegrationStrategy.getConfigurationForAbstractOrderEntryForOneTimeAccess(quoteEntry))
				.thenReturn(configurationModel);
		when(configurationModel.isComplete()).thenReturn(true);
		when(configurationModel.isConsistent()).thenReturn(true);
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any())).thenReturn(true);
	}


	@Test
	public void testDefaultQuoteActionValidationStrategy()
	{
		assertEquals(quoteActionValidationStrategy, classUnderTest.getDefaultQuoteActionValidationStrategy());
	}

	@Test
	public void testConfigurationAbstractOrderIntegrationStrategy()
	{
		assertEquals(configurationAbstractOrderIntegrationStrategy,
				classUnderTest.getConfigurationAbstractOrderIntegrationStrategy());
	}

	@Test
	public void testConfigChecker()
	{
		assertEquals(cpqConfigurableChecker, classUnderTest.getCpqConfigurableChecker());
	}


	@Test
	public void testHasConfigurationIssues()
	{
		final boolean result = classUnderTest.hasConfigurationIssue(quoteEntry);
		assertFalse(result);
	}

	@Test
	public void testHasConfigurationIssuesNonConfigurableProduct()
	{
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any())).thenReturn(false);
		assertFalse(classUnderTest.hasConfigurationIssue(quoteEntry));
	}

	@Test
	public void testHasConfigurationIssuesIncompleteConfiguration()
	{
		when(configurationModel.isComplete()).thenReturn(false);
		assertTrue(classUnderTest.hasConfigurationIssue(quoteEntry));
	}

	@Test
	public void testHasConfigurationIssuesConflictingConfiguration()
	{
		when(configurationModel.isConsistent()).thenReturn(false);
		assertTrue(classUnderTest.hasConfigurationIssue(quoteEntry));
	}

	@Test
	public void testGetQuoteEntryStatus()
	{
		final QuoteEntryStatus quoteEntryStatus = classUnderTest.getQuoteEntryStatus(quoteEntry);
		assertNotNull(quoteEntryStatus);
		assertEquals(PRODUCT_CODE, quoteEntryStatus.getProductCode());
		assertFalse(quoteEntryStatus.getHasConfigurationIssue());
	}

	@Test
	public void testGetSingleConfigurationIssueNoIssues()
	{
		final Optional<QuoteEntryStatus> configurationIssue = classUnderTest.getSingleConfigurationIssue(quoteModel);
		assertNotNull(configurationIssue);
		assertFalse(configurationIssue.isPresent());
	}

	@Test
	public void testGetSingleConfigurationIssueIssuesExist()
	{
		when(configurationModel.isConsistent()).thenReturn(false);
		final Optional<QuoteEntryStatus> configurationIssue = classUnderTest.getSingleConfigurationIssue(quoteModel);
		assertNotNull(configurationIssue);
		assertTrue(configurationIssue.isPresent());
		assertEquals(PRODUCT_CODE, configurationIssue.get().getProductCode());
	}

	@Test
	public void testGetSingleConfigurationIssueNoConfigurableProducts()
	{
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any())).thenReturn(false);
		final Optional<QuoteEntryStatus> configurationIssue = classUnderTest.getSingleConfigurationIssue(quoteModel);
		assertNotNull(configurationIssue);
		assertFalse(configurationIssue.isPresent());
	}

	@Test
	public void testGetSingleConfigurationIssueNoEntriesInQuote()
	{
		when(quoteModel.getEntries()).thenReturn(Collections.emptyList());
		final Optional<QuoteEntryStatus> configurationIssue = classUnderTest.getSingleConfigurationIssue(quoteModel);
		assertNotNull(configurationIssue);
		assertFalse(configurationIssue.isPresent());
	}

	@Test
	public void testValidateNoIssues()
	{
		classUnderTest.validate(quoteAction, quoteModel, customerModel);
		Mockito.verify(quoteActionValidationStrategy, Mockito.times(1)).validate(quoteAction, quoteModel, customerModel);
	}

	@Test
	public void testValidateNoConfigurableProducts()
	{
		when(cpqConfigurableChecker.isCPQConfiguratorApplicableProduct(Mockito.any())).thenReturn(false);
		classUnderTest.validate(quoteAction, quoteModel, customerModel);
		Mockito.verify(quoteActionValidationStrategy, Mockito.times(1)).validate(quoteAction, quoteModel, customerModel);
	}

	@Test
	public void testValidateDifferentQuoteAction()
	{
		when(configurationModel.isConsistent()).thenReturn(false);
		classUnderTest.validate(QuoteAction.APPROVE, quoteModel, customerModel);
		Mockito.verify(quoteActionValidationStrategy, Mockito.times(1)).validate(QuoteAction.APPROVE, quoteModel, customerModel);
	}

	@Test
	public void testValidateThereAreIssues()
	{
		when(configurationModel.isConsistent()).thenReturn(false);
		try
		{
			classUnderTest.validate(quoteAction, quoteModel, customerModel);
		}
		catch (final IllegalQuoteStateException ex)
		{
			assertNotNull(ex.getLocalizedMessage());
			assertEquals(classUnderTest.getLocalizedText(PRODUCT_CODE), ex.getLocalizedMessage());
		}
		Mockito.verify(quoteActionValidationStrategy, Mockito.times(0)).validate(quoteAction, quoteModel, customerModel);
	}


}
