/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressaddon.constants;



public interface ControllerConstants
{

	interface Views
	{
		String _AddonPrefix = "addon:/chineseaddressaddon/";


		interface Fragments
		{
			interface Account
			{
				String CountryAddressForm = _AddonPrefix + "fragments/address/countryAddressForm";
				String ChineseAddress = _AddonPrefix + "fragments/address/chineseAddress";
				String ChineseAddressInShippingItem = _AddonPrefix + "fragments/address/chineseAddressInShippingItem";
			}
		}


		interface Pages
		{

			interface Checkout
			{
				String CheckoutConfirmationPage = _AddonPrefix + "pages/checkout/checkoutConfirmationPage";
			}

			interface MultiStepCheckout
			{
				String AddEditDeliveryAddressPage = _AddonPrefix + "pages/checkout/multi/addEditDeliveryAddressPage";
			}
		}
	}
}
