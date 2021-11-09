/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.commerceservices.enums.CountryType;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import java.math.BigDecimal;
import java.util.List;


/**
 * Service for checkout and place order functionality
 */
public interface CommerceCheckoutService
{
	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead
	 *
	 * @param cartModel
	 *           the cart
	 * @param addressModel
	 *           the address, a null address will cause the delivery address to be removed from the cart.
	 * @return true if successful
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean setDeliveryAddress(CartModel cartModel, AddressModel addressModel);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead Sets the given address as delivery address on the cart and also marks the address as delivery
	 *             address. A null address will cause removal of deliveryAddress from the cart.
	 *
	 * @param cartModel
	 *           the cart
	 * @param addressModel
	 *           the address a null address will cause the delivery address to be removed from the cart.
	 * @param flagAsDeliveryAddress
	 *           true to mark the given address as delivery address
	 * @return true if successful
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean setDeliveryAddress(CartModel cartModel, AddressModel addressModel, boolean flagAsDeliveryAddress);

	/**
	 * Sets the given address as delivery address on the cart and also marks the address as delivery address. A null
	 * address will cause removal of deliveryAddress from the cart.
	 *
	 * @param parameter
	 *           A parameter object
	 * @return true if successful
	 */
	boolean setDeliveryAddress(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #setDeliveryAddress(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead. Sets the given delivery mode on the cart
	 *
	 * @param cartModel
	 *           the cart
	 * @param deliveryModeModel
	 *           the delivery mode
	 * @return true if successful
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean setDeliveryMode(CartModel cartModel, DeliveryModeModel deliveryModeModel);

	/**
	 * Sets the given delivery mode on the cart
	 *
	 * @param parameter
	 *           A parameter object for cart and deliverymode
	 * @return true if successful
	 */
	boolean setDeliveryMode(CommerceCheckoutParameter parameter);


	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #validateDeliveryMode(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead Validates the current delivery mode on the cart and clears if not valid the cart is then
	 *             calculated via
	 *             {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param cartModel
	 *           the cart
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	void validateDeliveryMode(CartModel cartModel);

	/**
	 * Validates the current delivery mode on the cart and clears if not valid the cart is then calculated via
	 * {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param parameter
	 *           The parameter object holding the cart
	 */
	void validateDeliveryMode(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #setPaymentInfo(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead. Sets the given payment info on the cart
	 *
	 * @param cartModel
	 *           the cart
	 * @param paymentInfoModel
	 *           the payment details
	 * @return true if successful
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean setPaymentInfo(CartModel cartModel, PaymentInfoModel paymentInfoModel);

	/**
	 * Sets the given payment info on the cart
	 *
	 * @param parameter
	 *           A parameter object for cart and payment details
	 *
	 * @return true if successful
	 */
	boolean setPaymentInfo(final CommerceCheckoutParameter parameter);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #authorizePayment(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             Authorizes the total amount of the cart
	 *
	 * @param cartModel
	 *           the cart
	 * @param securityCode
	 *           the cv2 number
	 * @param paymentProvider
	 *           the payment provider that will be used to authorize
	 * @return the payment transaction entry
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	PaymentTransactionEntryModel authorizePayment(CartModel cartModel, String securityCode, String paymentProvider);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #authorizePayment(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             Authorizes specified amount
	 *
	 * @param cartModel
	 *           the cart
	 * @param securityCode
	 *           the cv2 number
	 * @param paymentProvider
	 *           the payment provider that will be used to authorize
	 * @param amount
	 *           the amount to authorize
	 * @return the payment transaction entry
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	PaymentTransactionEntryModel authorizePayment(CartModel cartModel, String securityCode, String paymentProvider,
			BigDecimal amount);

	/**
	 * Authorizes the total amount of the cart if
	 * {@link de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter#authorizationAmount} is null
	 * otherwise the passed in amout is authorized
	 *
	 * @param parameter
	 *           A parameter object holding the cart, security code, payment provider and optionaly authorization amount.
	 * @return A payment transaction entry.
	 */
	PaymentTransactionEntryModel authorizePayment(CommerceCheckoutParameter parameter);


	/**
	 * @param cartModel
	 *           the cart
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *            if the order cannot be placed
	 * @deprecated Since 5.2. Use
	 *             {@link #placeOrder(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead Creates an order for the given cart
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	OrderModel placeOrder(CartModel cartModel) throws InvalidCartException;


	/**
	 * @deprecated Since 5.2. Creates an order for the given cart
	 *
	 * @param cartModel
	 *           the cart
	 * @param salesApplication
	 *           the sales application that placed the order
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *            if the order cannot be placed
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	OrderModel placeOrder(CartModel cartModel, SalesApplication salesApplication) throws InvalidCartException;

	/**
	 * Creates an order for the given cart
	 *
	 *
	 *
	 *
	 * @param parameter
	 * @return the order that has been created
	 * @throws de.hybris.platform.order.InvalidCartException
	 *            if the order cannot be placed
	 */
	CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException;

	/**
	 * Get the payment provider name
	 *
	 * @return the payment provider
	 */
	String getPaymentProvider();

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #removeDeliveryMode(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead Removes delivery mode from cart. the cart is then calculated via
	 *             {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param cartModel
	 *           the cart
	 * @return true if the deliver mode is removed
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	boolean removeDeliveryMode(CartModel cartModel);

	/**
	 * Removes delivery mode from cart. the cart is then calculated via
	 * {@link CommerceCartCalculationStrategy#calculateCart(de.hybris.platform.core.model.order.CartModel)}
	 *
	 * @param parameter
	 *           A parameter object holding the cart
	 * @return true if success
	 */
	boolean removeDeliveryMode(CommerceCheckoutParameter parameter);

	/**
	 * @deprecated Since 5.2. Use
	 *             {@link #calculateCart(de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter)}
	 *             instead.
	 *
	 *             Calculates the cartModel if the calculated flag is false.
	 *
	 * @param cartModel
	 *           The current user's cartModel
	 */
	@Deprecated(since = "5.2", forRemoval = true)
	void calculateCart(CartModel cartModel);

	/**
	 * Calculates the cartModel if the calculated flag is false.
	 *
	 * @param parameter
	 *           The parameter object for the current user's cartModel
	 */
	void calculateCart(final CommerceCheckoutParameter parameter);

	/**
	 * Get countries.
	 *
	 * @param countryType
	 *           If the value of type equals to shipping, then return shipping countries. If the value of type equals to
	 *           billing, then return billing countries. If the value of type is not given, return all countries.
	 * @return list of countries.
	 */
	List<CountryModel> getCountries(CountryType countryType);
}
