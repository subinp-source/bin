/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.config.bundlesources;

import java.util.Locale;
import java.util.Map;


/**
 * Add-on resource bundle source interface. By default used by DefaultAddonResourceBundleSource.
 */
public interface JavaScriptMessageResourcesAccessor
{

	/**
	 * Getting messages from all sources
	 * 
	 * @return java.util.Map
	 */
	Map<String, String> getAllMessages(Locale locale);

	/**
	 * Getting addOnName for given Resources
	 * 
	 * @return String
	 */
	String getAddOnName();

}
