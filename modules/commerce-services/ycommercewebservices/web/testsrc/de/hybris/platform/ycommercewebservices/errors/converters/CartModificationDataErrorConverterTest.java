/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ycommercewebservices.errors.converters;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;


@UnitTest
public class CartModificationDataErrorConverterTest
{
	private static final String OTHER_ERROR = "OtherError";
	private static final String NO_STOCK_MESSAGE = "cart.noStock";
	private static final String LOW_STOCK_MESSAGE = "cart.lowStock";
	private static final String OTHER_MESSAGE = "cart.OtherError";
	private static final long OUT_OF_STOCK_VALUE = 0;
	private static final long LOW_STOCK_VALUE = 10;
	private static final long PRODUCT_QUANTITY = 20;
	private static final String PRODUCT_CODE = "123456";
	private static final Integer ENTRY_NUMBER = Integer.valueOf(1);
	private final ErrorWsDTO errorWsDTO = new ErrorWsDTO();
	private CartModificationDataErrorConverter cartModificationDataErrorConverter = new CartModificationDataErrorConverter();
	private CartModificationData cartModificationData;
	@Mock
	private OrderEntryData entry;
	@Mock
	private ProductData product;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private MessageSource messageSource;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		cartModificationDataErrorConverter = new CartModificationDataErrorConverter();
		cartModificationDataErrorConverter.setCommerceCommonI18NService(commerceCommonI18NService);
		cartModificationDataErrorConverter.setMessageSource(messageSource);

		cartModificationData = new CartModificationData();
		cartModificationData.setEntry(entry);
		cartModificationData.setQuantity(PRODUCT_QUANTITY);

		given(entry.getProduct()).willReturn(product);
		given(entry.getEntryNumber()).willReturn(ENTRY_NUMBER);
		given(product.getCode()).willReturn(PRODUCT_CODE);
		given(messageSource.getMessage(eq(NO_STOCK_MESSAGE), any(Object[].class), any(Locale.class))).willReturn(NO_STOCK_MESSAGE);
		given(messageSource.getMessage(eq(LOW_STOCK_MESSAGE), any(Object[].class), any(Locale.class)))
				.willReturn(LOW_STOCK_MESSAGE);
		given(messageSource.getMessage(eq(NO_STOCK_MESSAGE), any(Object[].class), anyString(), any(Locale.class)))
				.willReturn(NO_STOCK_MESSAGE);
		given(messageSource.getMessage(eq(LOW_STOCK_MESSAGE), any(Object[].class), anyString(), any(Locale.class)))
				.willReturn(LOW_STOCK_MESSAGE);
		given(messageSource.getMessage(eq(OTHER_MESSAGE), any(Object[].class), anyString(), any(Locale.class)))
				.willReturn(OTHER_MESSAGE);
	}

	@Test
	public void testConvertWhenOutOfStockWithProvidedEntryNumber()
	{
		cartModificationData.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
		cartModificationData.setQuantityAdded(OUT_OF_STOCK_VALUE);

		final List<ErrorWsDTO> result = cartModificationDataErrorConverter.convert(cartModificationData);

		assertEquals("Error result should exist", 1, result.size());
		final ErrorWsDTO error = result.get(0);
		assertEquals("Error type should be insufficient stock error", CartModificationDataErrorConverter.TYPE, error.getType());
		assertEquals("Subject type should be entry", CartModificationDataErrorConverter.SUBJECT_TYPE, error.getSubjectType());
		assertEquals("Subject should be entry number", ENTRY_NUMBER.toString(), error.getSubject());
		assertEquals("Reason should be no stock", CartModificationDataErrorConverter.NO_STOCK, error.getReason());
		assertEquals("Incorrect no stock message", NO_STOCK_MESSAGE, error.getMessage());
	}

	@Test
	public void testConvertWhenOutOfStockWithoutEntryNumber()
	{
		cartModificationData.setStatusCode(CommerceCartModificationStatus.NO_STOCK);
		cartModificationData.setQuantityAdded(OUT_OF_STOCK_VALUE);
		given(entry.getEntryNumber()).willReturn(null);

		final List<ErrorWsDTO> result = cartModificationDataErrorConverter.convert(cartModificationData);

		assertEquals("Error result should exist", 1, result.size());
		final ErrorWsDTO error = result.get(0);
		assertEquals("Error type should be insufficient stock error", CartModificationDataErrorConverter.TYPE, error.getType());
		assertEquals("Subject type should be product", CartModificationDataErrorConverter.SUBJECT_TYPE_PRODUCT,
				error.getSubjectType());
		assertEquals("Subject should be product code", PRODUCT_CODE, error.getSubject());
		assertEquals("Reason should be no stock", CartModificationDataErrorConverter.NO_STOCK, error.getReason());
		assertEquals("Incorrect no stock message", NO_STOCK_MESSAGE, error.getMessage());
	}

	@Test
	public void testConvertWhenLowStock()
	{
		cartModificationData.setStatusCode(CommerceCartModificationStatus.LOW_STOCK);
		cartModificationData.setQuantityAdded(LOW_STOCK_VALUE);

		final List<ErrorWsDTO> result = cartModificationDataErrorConverter.convert(cartModificationData);

		assertEquals("Error result should exist", 1, result.size());
		final ErrorWsDTO error = result.get(0);
		assertEquals("Error type should be insufficient stock error", CartModificationDataErrorConverter.TYPE, error.getType());
		assertEquals("Subject type should be entry", CartModificationDataErrorConverter.SUBJECT_TYPE, error.getSubjectType());
		assertEquals("Subject should be entry number", ENTRY_NUMBER.toString(), error.getSubject());
		assertEquals("Reason should be no stock", CartModificationDataErrorConverter.LOW_STOCK, error.getReason());
		assertEquals("Incorrect low stock message", LOW_STOCK_MESSAGE, error.getMessage());
	}

	@Test
	public void testConvertWhenOtherError()
	{
		cartModificationData.setStatusCode(OTHER_ERROR);
		given(entry.getEntryNumber()).willReturn(null);

		final List<ErrorWsDTO> result = cartModificationDataErrorConverter.convert(cartModificationData);

		assertEquals("Error result should exist", 1, result.size());
		final ErrorWsDTO error = result.get(0);
		assertEquals("Error type should be generic validation error", CartModificationDataErrorConverter.TYPE_OTHER,
				error.getType());
		assertEquals("Subject type should be product", CartModificationDataErrorConverter.SUBJECT_TYPE_PRODUCT,
				error.getSubjectType());
		assertEquals("Subject should be product code", PRODUCT_CODE, error.getSubject());
		assertEquals("Reason should be provided", OTHER_ERROR, error.getReason());
		assertEquals("Error message should be provided", OTHER_MESSAGE, error.getMessage());
	}

	@Test
	public void testPopulateSubjectFromEntryNumber()
	{
		cartModificationDataErrorConverter.populateSubject(cartModificationData, errorWsDTO);
		assertEquals("Subject should be entry number if it exist", ENTRY_NUMBER.toString(), errorWsDTO.getSubject());
		assertEquals("Subject type should be entry if entry number exist", CartModificationDataErrorConverter.SUBJECT_TYPE_ENTRY,
				errorWsDTO.getSubjectType());

	}

	@Test
	public void testPopulateSubjectFromProductCode()
	{
		given(entry.getEntryNumber()).willReturn(null);
		cartModificationDataErrorConverter.populateSubject(cartModificationData, errorWsDTO);
		assertEquals("Subject should be product code if product exist", PRODUCT_CODE, errorWsDTO.getSubject());
		assertEquals("Subject type should be product if product exist", CartModificationDataErrorConverter.SUBJECT_TYPE_PRODUCT,
				errorWsDTO.getSubjectType());
	}

	@Test
	public void testPopulateSubjectFromFallback()
	{
		given(entry.getEntryNumber()).willReturn(null);
		given(entry.getProduct()).willReturn(null);
		cartModificationDataErrorConverter.populateSubject(cartModificationData, errorWsDTO);
		assertNull("Subject should be null if no entry number or product", errorWsDTO.getSubject());
		assertNull("Subject type should be null if if no entry number or product", errorWsDTO.getSubjectType());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertNoEntry()
	{
		cartModificationData.setEntry(null);
		cartModificationDataErrorConverter.convert(cartModificationData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertNoStatusCode()
	{
		cartModificationData.setStatusCode(null);
		cartModificationDataErrorConverter.convert(cartModificationData);
	}

	@Test
	public void testGetMessageFallbackNoEntryNumberNoProduct()
	{
		cartModificationData.setStatusCode(OTHER_ERROR);
		given(entry.getEntryNumber()).willReturn(null);
		given(entry.getProduct()).willReturn(null);
		final String message = cartModificationDataErrorConverter.getMessage(cartModificationData);
		assertEquals("Fallback error message should exist when no entry number or product", OTHER_MESSAGE, message);
	}
}
