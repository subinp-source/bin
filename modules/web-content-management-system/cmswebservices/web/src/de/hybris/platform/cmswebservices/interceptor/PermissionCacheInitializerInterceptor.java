/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.interceptor;

import de.hybris.platform.cms2.permissions.PermissionCachedCRUDService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 *  Default interceptor to run before the controller's execution to initialize the permission cache.
 *  Reason: part of the code can be run in executeInLocalView which means that everything
 *  that is put in the cache during executeInLocalView execution will never be available outside.
 *  So we can not initialize the cache inside executeInLocalView. That's why it's initialized here.
 */
public class PermissionCacheInitializerInterceptor extends HandlerInterceptorAdapter
{
	private PermissionCachedCRUDService permissionCachedCRUDService;

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception
	{
		getPermissionCachedCRUDService().initCache();
		return true;
	}

	public PermissionCachedCRUDService getPermissionCachedCRUDService()
	{
		return permissionCachedCRUDService;
	}

	@Required
	public void setPermissionCachedCRUDService(final PermissionCachedCRUDService permissionCachedCRUDService)
	{
		this.permissionCachedCRUDService = permissionCachedCRUDService;
	}
}
