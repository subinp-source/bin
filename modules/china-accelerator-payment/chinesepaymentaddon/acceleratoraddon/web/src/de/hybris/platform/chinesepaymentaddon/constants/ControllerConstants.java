/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentaddon.constants;

import de.hybris.platform.chinesepaymentaddon.model.PayNowActionModel;

public interface ControllerConstants
{

	interface Views
	{
		String _AddonPrefix = "addon:/chinesepaymentaddon/";

		interface Pages
		{

			interface Checkout
			{
				String CheckoutConfirmationPage = _AddonPrefix + "pages/checkout/checkoutConfirmationPage";
			}

			interface MultiStepCheckout
			{
				String ChooseDeliveryMethodPage = "pages/checkout/multi/chooseDeliveryMethodPage";
				String ChoosePickupLocationPage = "pages/checkout/multi/choosePickupLocationPage";
				String AddPaymentMethodPage = _AddonPrefix + "pages/checkout/multi/addPaymentMethodPage";
				String CheckoutSummaryPage = _AddonPrefix + "pages/checkout/multi/checkoutSummaryPage";
				String HostedOrderPageErrorPage = _AddonPrefix + "pages/checkout/multi/hostedOrderPageErrorPage";
				String HostedOrderPostPage = _AddonPrefix + "pages/checkout/multi/hostedOrderPostPage";
				String SilentOrderPostPage = _AddonPrefix + "pages/checkout/multi/silentOrderPostPage";
				String GiftWrapPage = "pages/checkout/multi/giftWrapPage";
				String HopPaymentPage = _AddonPrefix + "pages/checkout/multi/hopPaymentPage";
				String PaymentFailedPage = _AddonPrefix + "pages/checkout/multi/paymentFailedPage";
				String HopReturnPage = _AddonPrefix + "pages/checkout/multi/hopReturnPage";
			}
		}

		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";
			String PayNowAction = _Prefix + PayNowActionModel._TYPECODE + _Suffix; // NOSONAR
		}
	}
}
