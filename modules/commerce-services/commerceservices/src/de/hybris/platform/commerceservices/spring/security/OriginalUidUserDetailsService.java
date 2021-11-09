/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.spring.security;

import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;


/**
 * accelerator specific implementation for providing user data access
 */
public class OriginalUidUserDetailsService extends CoreUserDetailsService
{
	@Override
	public CoreUserDetails loadUserByUsername(final String username)
	{
		return super.loadUserByUsername(username.toLowerCase());
	}
}
