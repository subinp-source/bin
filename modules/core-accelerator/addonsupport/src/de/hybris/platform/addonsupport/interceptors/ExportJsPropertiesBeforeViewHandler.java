/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.interceptors;

import de.hybris.platform.addonsupport.config.javascript.BeforeViewJsPropsHandlerAdaptee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

public class ExportJsPropertiesBeforeViewHandler extends BeforeViewJsPropsHandlerAdaptee
{
	@Override
	public String beforeViewJsProps(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model,
			final String viewName)
	{
		return viewName;
	}
}
