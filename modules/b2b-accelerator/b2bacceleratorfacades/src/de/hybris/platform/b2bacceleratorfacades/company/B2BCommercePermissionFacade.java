/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;


/**
 * A facade for permission management within b2b commerce
 *
 * @deprecated Since 6.0. Use {@link B2BPermissionFacade} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommercePermissionFacade extends B2BPermissionFacade
{
	// keep interface for backwards compatibility
}
