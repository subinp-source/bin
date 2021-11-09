/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofile.controllers;

/**
 */
public interface ControllerConstants
{
	String ADDON_PREFIX = "addon:/chineseprofileaddon/";

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Fragments
		{
			interface Account
			{
				String CountryAddressForm = ADDON_PREFIX + "fragments/address/countryAddressForm";
			}

			interface Checkout
			{
				String TermsAndConditionsPopup = "fragments/checkout/termsAndConditionsPopup";
			}

		}

		interface Pages
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";

				String ChineseMobileProfileBindingPage = ADDON_PREFIX + "pages/account/chineseMobileProfileBindingPage";

				String ChineseMobileRegisterBindingPage = ADDON_PREFIX + "pages/account/chineseMobileRegisterBindingPage";

				String ChineseMobileUnbindingPage = ADDON_PREFIX + "pages/account/chineseMobileUnbindingPage";

				String VerificationCodeMockPage = ADDON_PREFIX + "pages/mock/showVerificationCodePage";
			}
		}
	}
}
