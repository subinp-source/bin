/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Test suite for {@link DefaultCartLoaderStrategy}
 */
@UnitTest
public class DefaultCartLoaderStrategyTest
{
	static final String CURRENT_CART_ID = "current";
	static final String CART_GUID = "6d868385adf11f729b6e30acd2c44195ccd6e882";
	static final String CART_CODE = "00000001";

	private DefaultCartLoaderStrategy cartLoaderStrategy;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private UserService userService;
	@Mock
	private CommerceCartService commerceCartService;
	@Mock
	private CartService cartService;
	@Mock
	private BaseSiteModel currentBaseSiteModel;
	@Mock
	private BaseSiteModel otherBaseSiteModel;
	@Mock
	private CustomerModel customerUserModel;
	@Mock
	private CustomerModel anonymousUserModel;
	@Mock
	private CartModel cartModel;
	@Mock
	private CommerceCommonI18NService commerceCommonI18NService;
	@Mock
	private CurrencyModel currencyModel;
	@Mock
	private CurrencyModel otherCurrencyModel;
	@Mock
	private ModelService modelService;
	@Mock
	private UserModel userModel;
	@Captor
	private ArgumentCaptor<CommerceCartParameter> parameterCaptor;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		cartLoaderStrategy = new DefaultCartLoaderStrategy();
		cartLoaderStrategy.setBaseSiteService(baseSiteService);
		cartLoaderStrategy.setUserService(userService);
		cartLoaderStrategy.setCommerceCartService(commerceCartService);
		cartLoaderStrategy.setCommerceCommonI18NService(commerceCommonI18NService);
		cartLoaderStrategy.setCartService(cartService);
		cartLoaderStrategy.setModelService(modelService);
		cartLoaderStrategy.setCartRefreshedByDefault(true);

		given(commerceCommonI18NService.getCurrentCurrency()).willReturn(currencyModel);
		given(cartModel.getCurrency()).willReturn(currencyModel);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSiteModel);
	}

	@Test(expected = CartException.class)
	public void testEmptyCartId() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);

		try
		{
			cartLoaderStrategy.loadCart("");
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.INVALID, CartException.INVALID, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testNullCartId() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);

		try
		{
			cartLoaderStrategy.loadCart(null);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.INVALID, CartException.INVALID, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testNoUserInSession()
	{
		given(userService.getCurrentUser()).willReturn(null);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = IllegalStateException.class)
	public void testNoBaseSiteInSession()
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(baseSiteService.getCurrentBaseSite()).willReturn(null);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = AccessDeniedException.class)
	public void testFailWhenNonCustomerUserAccessingCart()
	{
		given(userService.getCurrentUser()).willReturn(userModel);

		cartLoaderStrategy.loadCart(CART_GUID);
	}

	@Test(expected = AccessDeniedException.class)
	public void testAnonymousUserCurrentCart()
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);
	}

	@Test
	public void testAnonymousUser() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartService.getSessionCart()).willReturn(cartModel);

		cartLoaderStrategy.loadCart(CART_GUID);

		verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
	}

	@Test(expected = CartException.class)
	public void testAnonymousUserRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.restoreCart(any(CommerceCartParameter.class)))
				.willThrow(new CommerceCartRestorationException("Couldn't restore cart: " + CART_GUID));

		try
		{
			cartLoaderStrategy.loadCart(CART_GUID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.INVALID, CartException.INVALID, e.getReason());
			verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test
	public void testValidateBaseSiteFromLoadedCart() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);

		cartLoaderStrategy.loadCart(CART_GUID);

		verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
	}

	@Test(expected = CartException.class)
	public void testValidateBaseSiteFromLoadedCartMismatch() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(otherBaseSiteModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getGuid()).willReturn(CART_GUID);

		try
		{
			cartLoaderStrategy.loadCart(CART_GUID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.NOT_FOUND, CartException.NOT_FOUND, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testValidateBaseSiteFromLoadedCartMismatchWhenCustomer() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		given(cartModel.getSite()).willReturn(otherBaseSiteModel);

		try
		{
			cartLoaderStrategy.loadCart(CART_CODE);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.NOT_FOUND, CartException.NOT_FOUND, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testAnonymousUserCartNotFound() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(null);

		try
		{
			cartLoaderStrategy.loadCart(CART_GUID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.NOT_FOUND, CartException.NOT_FOUND, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test
	public void testCustomerUserCurrentCart() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel))
				.willReturn(cartModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);

		cartLoaderStrategy.loadCart(CURRENT_CART_ID);

		verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCurrentCartRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel))
				.willReturn(cartModel);
		given(commerceCartService.restoreCart(any(CommerceCartParameter.class)))
				.willThrow(new CommerceCartRestorationException("Couldn't restore cart: " + CURRENT_CART_ID));
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);

		try
		{
			cartLoaderStrategy.loadCart(CURRENT_CART_ID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.INVALID, CartException.INVALID, e.getReason());
			verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCurrentCartNotFound() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForGuidAndSiteAndUser(null, currentBaseSiteModel, customerUserModel)).willReturn(null);

		try
		{
			cartLoaderStrategy.loadCart(CURRENT_CART_ID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.NOT_FOUND, CartException.NOT_FOUND, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test
	public void testCustomerUser() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartService.getSessionCart()).willReturn(cartModel);

		cartLoaderStrategy.loadCart(CART_CODE);

		verify(commerceCartService).restoreCart(parameterCaptor.capture());
		Assert.assertFalse("Cart should be recalculated during restoration", parameterCaptor.getValue().isIgnoreRecalculation());
	}

	@Test(expected = CartException.class)
	public void testCustomerUserRestorationFailed() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(commerceCartService.restoreCart(any(CommerceCartParameter.class)))
				.willThrow(new CommerceCartRestorationException("Couldn't restore cart: " + CART_GUID));

		try
		{
			cartLoaderStrategy.loadCart(CART_CODE);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.INVALID, CartException.INVALID, e.getReason());
			verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testCustomerUserCartNotFound() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(null);

		try
		{
			cartLoaderStrategy.loadCart(CART_CODE);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.NOT_FOUND, CartException.NOT_FOUND, e.getReason());
			verify(commerceCartService, never()).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testFailWithErrorWhenAnonymousCartExpired() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(anonymousUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(true);
		given(commerceCartService.getCartForGuidAndSite(CART_GUID, currentBaseSiteModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(anonymousUserModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getGuid()).willReturn("Different_guid");

		try
		{
			cartLoaderStrategy.loadCart(CART_GUID);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.EXPIRED, CartException.EXPIRED, e.getReason());
			verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	@Test(expected = CartException.class)
	public void testFailWithErrorWhenCartExpired() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(anonymousUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getUser()).willReturn(customerUserModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn("Different_code");

		try
		{
			cartLoaderStrategy.loadCart(CART_CODE);
		}
		catch (final CartException e)
		{
			Assert.assertEquals("The exception reason should be " + CartException.EXPIRED, CartException.EXPIRED, e.getReason());
			verify(commerceCartService).restoreCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	/**
	 * @throws CalculationException
	 * @deprecated not used
	 */
	@Deprecated(since = "2011", forRemoval = true)
	@Test
	public void testApplyCurrencyToCartAndRecalculate() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCurrency()).willReturn(otherCurrencyModel);

		cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		verify(commerceCartService).recalculateCart(any(CommerceCartParameter.class));
	}

	/**
	 * @throws CalculationException
	 * @deprecated not used
	 */
	@Deprecated(since = "2011", forRemoval = true)
	@Test(expected = CartException.class)
	public void testApplyCurrencyToCartAndRecalculateWithException() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCurrency()).willReturn(otherCurrencyModel);
		doThrow(new CalculationException("Some calculation exception")).when(commerceCartService)
				.recalculateCart(any(CommerceCartParameter.class));

		try
		{
			cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		}
		catch (final Exception e)
		{
			verify(commerceCartService).recalculateCart(any(CommerceCartParameter.class));
			throw e;
		}
	}

	/**
	 * @throws CalculationException
	 * @deprecated not used
	 */
	@Deprecated(since = "2011", forRemoval = true)
	@Test
	public void testApplyCurrencyToCartAndNoRecalculate() throws CalculationException
	{
		given(cartService.getSessionCart()).willReturn(cartModel);

		cartLoaderStrategy.applyCurrencyToCartAndRecalculateIfNeeded();
		verify(commerceCartService, times(0)).recalculateCart(any(CommerceCartParameter.class));
	}

	// tests when refresh=false

	@Test
	public void testRestoreWhenCartWithDifferentCurrency() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		// cart not expired
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		// cart has different currency
		given(cartModel.getCurrency()).willReturn(otherCurrencyModel);

		cartLoaderStrategy.loadCart(CART_CODE, false);

		verify(commerceCommonI18NService).getCurrentCurrency();
		verify(commerceCartService).restoreCart(parameterCaptor.capture());
		Assert.assertFalse("Cart should be recalculated during restoration", parameterCaptor.getValue().isIgnoreRecalculation());
	}

	@Test
	public void testNoRestoreWhenCartWithTheSameCurrency() throws CommerceCartRestorationException
	{
		given(userService.getCurrentUser()).willReturn(customerUserModel);
		given(userService.isAnonymousUser(customerUserModel)).willReturn(false);
		given(commerceCartService.getCartForCodeAndUser(CART_CODE, customerUserModel)).willReturn(cartModel);
		given(cartModel.getSite()).willReturn(currentBaseSiteModel);
		// cart not expired
		given(cartService.getSessionCart()).willReturn(cartModel);
		given(cartModel.getCode()).willReturn(CART_CODE);
		// cart has different currency
		given(cartModel.getCurrency()).willReturn(currencyModel);

		cartLoaderStrategy.loadCart(CART_CODE, false);

		verify(commerceCommonI18NService).getCurrentCurrency();
		verify(commerceCartService).restoreCart(parameterCaptor.capture());
		Assert.assertTrue("Cart should not be recalculated during restoration", parameterCaptor.getValue().isIgnoreRecalculation());
	}
}
