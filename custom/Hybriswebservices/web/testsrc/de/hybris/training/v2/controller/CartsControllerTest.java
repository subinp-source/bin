/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.training.v2.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.AddToCartParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryGroupException;
import de.hybris.training.stock.CommerceStockFacade;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.training.stock.CommerceStockFacade;
import de.hybris.training.validation.data.CartVoucherValidationData;
import de.hybris.training.validator.CartVoucherValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CartsControllerTest
{
	private static final String REJECTED_VOUCHER_CODE_1 = "123-abc";
	private static final String REJECTED_VOUCHER_CODE_2 = "456-def";
	private static final String BASE_SITE = "myBaseSite";
	private static final String PRODUCT_CODE = "MY_PRODUCT_CODE";
	private static final long QUANTITY = 1L;
	private static final int GROUP_NUMBER = 1;
	private static final String FIELDS = "MY_FIELDS";
	private static final String NO_STOCK = "noStock";
	private static final String COUPON_STATUS_CODE = "couponNotValid";
	private static final String VOUCHER_STATUS_CODE = "voucherNotValid";
	private final CartModificationData data = new CartModificationData();
	private final CartModificationWsDTO wsDTO = new CartModificationWsDTO();
	private final List<String> voucherList = new ArrayList();

	@Mock
	private DataMapper dataMapper;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private CartVoucherValidator cartVoucherValidator;
	@Mock
	private CommerceStockFacade commerceStockFacade;
	@Mock
	private Validator addToCartEntryGroupValidator;
	@Mock
	private Validator greaterThanZeroValidator;
	@InjectMocks
	private CartsController controller;
	@Captor
	private ArgumentCaptor<AddToCartParams> addToCartParamsCaptor;

	@Before
	public void setUp()
	{
		final CartData cart = new CartData();
		cart.setAppliedVouchers(voucherList);
		given(cartFacade.getSessionCart()).willReturn(cart);
		when(dataMapper.map(data, CartModificationWsDTO.class, FIELDS)).thenReturn(wsDTO);
	}

	@Test
	public void testAddToCartEntryGroup() throws CommerceCartModificationException
	{
		when(cartFacade.addToCart(any(AddToCartParams.class))).thenReturn(data);
		when(commerceStockFacade.isStockSystemEnabled(any())).thenReturn(true);
		when(commerceStockFacade.getStockDataForProductAndBaseSite(PRODUCT_CODE, BASE_SITE)).thenReturn(null);

		final ProductWsDTO product = new ProductWsDTO();
		product.setCode(PRODUCT_CODE);
		final OrderEntryWsDTO entry = new OrderEntryWsDTO();
		entry.setProduct(product);
		entry.setQuantity(QUANTITY);

		final CartModificationWsDTO cartModificationWsDTO = controller.addToCartEntryGroup(BASE_SITE, GROUP_NUMBER, entry, FIELDS);

		verify(dataMapper).map(data, CartModificationWsDTO.class, FIELDS);
		verify(addToCartEntryGroupValidator).validate(any(), any());
		verify(greaterThanZeroValidator).validate(any(), any());
		verify(commerceStockFacade).isStockSystemEnabled(BASE_SITE);
		verify(commerceStockFacade).getStockDataForProductAndBaseSite(PRODUCT_CODE, BASE_SITE);

		verify(cartFacade).addToCart(addToCartParamsCaptor.capture());

		assertThat(cartModificationWsDTO).isSameAs(wsDTO);
		assertThat(addToCartParamsCaptor.getValue()).isNotNull().hasFieldOrPropertyWithValue("storeId", BASE_SITE)
				.hasFieldOrPropertyWithValue("productCode", PRODUCT_CODE).hasFieldOrPropertyWithValue("quantity", QUANTITY)
				.hasFieldOrPropertyWithValue("entryGroupNumbers", Set.of(GROUP_NUMBER));
	}

	@Test
	public void testAddToCartEntryGroupWithNullQuantity() throws CommerceCartModificationException
	{
		when(cartFacade.addToCart(any(AddToCartParams.class))).thenReturn(data);
		when(commerceStockFacade.isStockSystemEnabled(any())).thenReturn(true);
		when(commerceStockFacade.getStockDataForProductAndBaseSite(PRODUCT_CODE, BASE_SITE)).thenReturn(null);

		final ProductWsDTO product = new ProductWsDTO();
		product.setCode(PRODUCT_CODE);
		final OrderEntryWsDTO entry = new OrderEntryWsDTO();
		entry.setProduct(product);
		entry.setQuantity(null);

		final CartModificationWsDTO cartModificationWsDTO = controller.addToCartEntryGroup(BASE_SITE, GROUP_NUMBER, entry, FIELDS);

		verify(dataMapper).map(data, CartModificationWsDTO.class, FIELDS);
		verify(addToCartEntryGroupValidator).validate(any(), any());
		verify(greaterThanZeroValidator).validate(any(), any());
		verify(commerceStockFacade).isStockSystemEnabled(BASE_SITE);
		verify(commerceStockFacade).getStockDataForProductAndBaseSite(PRODUCT_CODE, BASE_SITE);

		verify(cartFacade).addToCart(addToCartParamsCaptor.capture());

		assertThat(cartModificationWsDTO).isSameAs(wsDTO);
		assertThat(addToCartParamsCaptor.getValue()).isNotNull().hasFieldOrPropertyWithValue("storeId", BASE_SITE)
				.hasFieldOrPropertyWithValue("productCode", PRODUCT_CODE).hasFieldOrPropertyWithValue("quantity", 1L)
				.hasFieldOrPropertyWithValue("entryGroupNumbers", Set.of(GROUP_NUMBER));
	}

	@Test
	public void testRemoveEntryGroup() throws CommerceCartModificationException
	{
		when(cartFacade.removeEntryGroup(GROUP_NUMBER)).thenReturn(data);

		controller.removeEntryGroup(GROUP_NUMBER);

		verify(greaterThanZeroValidator).validate(any(), any());
		verify(cartFacade).removeEntryGroup(GROUP_NUMBER);
	}

	@Test
	public void testRemoveNotExistingEntryGroupShouldThrowException() throws CommerceCartModificationException
	{
		final CartModificationData data = new CartModificationData();
		data.setStatusCode(CommerceCartModificationStatus.INVALID_ENTRY_GROUP_NUMBER);

		when(cartFacade.removeEntryGroup(GROUP_NUMBER)).thenReturn(data);

		// when
		final Throwable raisedException = catchThrowable(() -> controller.removeEntryGroup(GROUP_NUMBER));

		// then
		assertThat(raisedException)
				.isInstanceOf(CartEntryGroupException.class)
				.hasMessageContaining("Entry group not found");

		verify(greaterThanZeroValidator).validate(any(), any());
	}

	@Test(expected = CommerceCartModificationException.class)
	public void testValidateCartException() throws InvalidCartException, CommerceCartModificationException
	{
		given(cartFacade.validateCartData()).willThrow(new CommerceCartModificationException("TEST TEST TEST"));
		controller.validateCart(FieldSetLevelHelper.DEFAULT_LEVEL);
	}

	@Test
	public void testValidateCartOk() throws CommerceCartModificationException
	{
		//given
		given(cartFacade.validateCartData()).willReturn(Collections.emptyList());
		final CartModificationListWsDTO noErrorsResult = new CartModificationListWsDTO();
		noErrorsResult.setCartModifications(Collections.emptyList());
		final Predicate<CartModificationDataList> listShouldBeEmpty = list -> list.getCartModificationList() != null && list
				.getCartModificationList().isEmpty();
		given(dataMapper.map(argThat(new CartValidationArgumentMatcher(listShouldBeEmpty)), same(CartModificationListWsDTO.class),
				same(FieldSetLevelHelper.DEFAULT_LEVEL))).willReturn(noErrorsResult);
		//when
		final CartModificationListWsDTO response = controller.validateCart(FieldSetLevelHelper.DEFAULT_LEVEL);
		//then
		assertTrue("No modifications expected", response.getCartModifications().isEmpty());
	}

	@Test
	public void testValidateCartNoStock() throws CommerceCartModificationException
	{
		//given
		final CartModificationData noStock = createCartModificationData(NO_STOCK);
		given(cartFacade.validateCartData()).willReturn(Collections.singletonList(noStock));

		final CartModificationWsDTO noStockResponse = createCartModificationDTO(NO_STOCK, null);
		final CartModificationListWsDTO resultWithNoStockError = new CartModificationListWsDTO();
		resultWithNoStockError.setCartModifications(Collections.singletonList(noStockResponse));

		final Predicate<CartModificationDataList> listContainsNoStock = list -> list.getCartModificationList() != null &&//
				list.getCartModificationList().stream().allMatch(modification -> NO_STOCK.equals(modification.getStatusCode()));

		given(dataMapper.map(argThat(new CartValidationArgumentMatcher(listContainsNoStock)), same(CartModificationListWsDTO.class),
				same(FieldSetLevelHelper.DEFAULT_LEVEL))).willReturn(resultWithNoStockError);
		//when
		final CartModificationListWsDTO response = controller.validateCart(FieldSetLevelHelper.DEFAULT_LEVEL);
		//then
		assertEquals("One modification expected", 1, response.getCartModifications().size());
	}

	@Test
	public void testValidateVoucherRejected() throws CommerceCartModificationException
	{
		//given
		given(cartVoucherValidator.validate(anyList()))
				.willReturn(List.of(createCartVoucherData(REJECTED_VOUCHER_CODE_1), createCartVoucherData(REJECTED_VOUCHER_CODE_2)));

		given(cartFacade.validateCartData())
				.willReturn(List.of(createCartModificationData(VOUCHER_STATUS_CODE), createCartModificationData(COUPON_STATUS_CODE)));

		final CartModificationWsDTO firstValidationVoucherResponse = createCartModificationDTO(VOUCHER_STATUS_CODE,
				REJECTED_VOUCHER_CODE_1);
		final CartModificationWsDTO secondValidationVoucherResponse = createCartModificationDTO(COUPON_STATUS_CODE, null);
		final CartModificationListWsDTO validationResult = new CartModificationListWsDTO();
		validationResult.setCartModifications(List.of(firstValidationVoucherResponse, secondValidationVoucherResponse));

		final Predicate<CartModificationDataList> listWithVouchers = list -> list.getCartModificationList() != null && list
				.getCartModificationList().stream()
				.allMatch(modification -> COUPON_STATUS_CODE.equals(modification.getStatusCode()) &&//
						StringUtils.isNotBlank(modification.getStatusMessage()));

		given(dataMapper.map(argThat(new CartValidationArgumentMatcher(listWithVouchers)), same(CartModificationListWsDTO.class),
				same(FieldSetLevelHelper.DEFAULT_LEVEL))).willReturn(validationResult);
		//when
		final CartModificationListWsDTO response = controller.validateCart(FieldSetLevelHelper.DEFAULT_LEVEL);
		//then
		assertEquals("Two vouchers expected", 2, response.getCartModifications().size());
	}

	private CartModificationWsDTO createCartModificationDTO(final String statusCode, final String statusMessage)
	{
		final CartModificationWsDTO dto = new CartModificationWsDTO();
		dto.setStatusCode(statusCode);
		dto.setStatusMessage(statusMessage);
		return dto;
	}

	private CartModificationData createCartModificationData(final String statusCode)
	{
		final CartModificationData data = new CartModificationData();
		data.setStatusCode(statusCode);
		return data;
	}

	private CartVoucherValidationData createCartVoucherData(final String subject)
	{
		final CartVoucherValidationData data = new CartVoucherValidationData();
		data.setSubject(subject);
		return data;
	}

	private class CartValidationArgumentMatcher extends ArgumentMatcher<CartModificationDataList>
	{
		private final Predicate<CartModificationDataList> filter;

		public CartValidationArgumentMatcher(final Predicate<CartModificationDataList> allMatchFilter)
		{
			this.filter = allMatchFilter;
		}

		@Override
		public boolean matches(final Object argument)
		{
			return (argument instanceof CartModificationDataList) ? filter.test((CartModificationDataList) argument) : false;
		}
	}
}
