/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineselogisticaddon.constants;



public interface ControllerConstants
{
	/**
	 * Class with view name constants
	 */
	interface Views
	{
		String _AddonPrefix = "addon:/chineselogisticaddon/";
		interface Pages
		{

			interface MultiStepCheckout
			{
				String ChooseDeliveryMethodPage = _AddonPrefix + "pages/checkout/multi/chooseDeliveryMethodPage";
			}

		}
	}
}
