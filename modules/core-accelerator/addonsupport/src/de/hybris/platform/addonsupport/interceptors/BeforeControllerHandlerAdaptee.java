/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;


public interface BeforeControllerHandlerAdaptee
{
	boolean beforeController(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception;// NOSONAR

}
