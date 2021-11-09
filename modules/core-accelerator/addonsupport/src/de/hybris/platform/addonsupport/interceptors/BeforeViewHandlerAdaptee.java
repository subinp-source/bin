/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;


/**
 * Allows an AddOn to code up a BeforeViewHandler adapter without needing to implement the storefront specific
 * interface.
 *
 */
public interface BeforeViewHandlerAdaptee
{
	String beforeView(HttpServletRequest request, HttpServletResponse response, ModelMap model, String viewName) throws Exception;// NOSONAR

}
