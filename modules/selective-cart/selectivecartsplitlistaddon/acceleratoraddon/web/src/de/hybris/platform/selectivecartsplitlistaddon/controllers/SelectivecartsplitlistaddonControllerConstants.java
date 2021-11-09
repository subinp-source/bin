/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.selectivecartsplitlistaddon.controllers;

import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.selectivecartsplitlistaddon.model.components.SaveForLaterCMSComponentModel;


/**
 * Selectivecartsplitlistaddon controller constants
 */
public interface SelectivecartsplitlistaddonControllerConstants
{
	// implement here controller constants used by this extension
	String ADDON_PREFIX = "addon:/selectivecartsplitlistaddon";

	/**
	 * Class with action name constants
	 */
	interface Actions
	{
		interface Cms // NOSONAR
		{
			String _Prefix = "/view/"; // NOSONAR
			String _Suffix = "Controller"; // NOSONAR

			/**
			 * Default CMS component controller
			 */
			String SaveForLaterComponent = _Prefix + SaveForLaterCMSComponentModel._TYPECODE + _Suffix; // NOSONAR
			String MiniCartComponent = _Prefix + MiniCartComponentModel._TYPECODE + _Suffix; // NOSONAR
		}
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Cms // NOSONAR
		{
			String ComponentPrefix = "cms/"; // NOSONAR
		}

		interface Fragments
		{
			interface Cart // NOSONAR
			{
				String MiniCartPanel = "fragments/cart/miniCartPanel"; // NOSONAR
				String CartPopup = "fragments/cart/cartPopup"; // NOSONAR
			}
		}
	}
}
