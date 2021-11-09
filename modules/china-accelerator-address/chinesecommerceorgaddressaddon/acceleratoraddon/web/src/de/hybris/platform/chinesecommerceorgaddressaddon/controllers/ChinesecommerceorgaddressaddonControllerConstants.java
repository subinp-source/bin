/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesecommerceorgaddressaddon.controllers;

import de.hybris.platform.chinesecommerceorgaddressaddon.constants.ChinesecommerceorgaddressaddonConstants;


/**
 */
public interface ChinesecommerceorgaddressaddonControllerConstants
{
	// implement here controller constants used by this extension
	interface Views
	{
		String _AddonPrefix = "addon:/chinesecommerceorgaddressaddon/";


		interface Fragments
		{
			interface Account
			{
				String CountryAddressForm = _AddonPrefix + "fragments/address/countryAddressForm";
				String ChineseAddressForm = _AddonPrefix + "fragments/address/chineseAddressForm";
			}
		}

		interface Pages
		{
			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
			}

			interface MyCompany
			{
				String ADD_ON_PREFIX = "addon:";
				String VIEW_PAGE_PREFIX = ADD_ON_PREFIX + "/" + ChinesecommerceorgaddressaddonConstants.EXTENSIONNAME + "/";
				String MyCompanyManageUnitAddAddressPage = VIEW_PAGE_PREFIX + "pages/company/myCompanyManageUnitAddAddressPage";
			}
		}
	}
}