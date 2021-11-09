/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.constants;

/**
 * Global class for all B2boccaddon constants. You can add global constants for your extension into this class.
 */
public final class B2boccaddonConstants extends GeneratedB2boccaddonConstants
{
	public static final String EXTENSIONNAME = "b2boccaddon";
	public static final String OCC_REWRITE_OVERLAPPING_BASE_SITE_USER_PATH = "#{ ${occ.rewrite.overlapping.paths.enabled:false} ? '/{baseSiteId}/orgUsers/{userId}' : '/{baseSiteId}/users/{userId}'}";
	public static final String OCC_REWRITE_OVERLAPPING_PRODUCTS_PATH = "#{ ${occ.rewrite.overlapping.paths.enabled:false} ? '/{baseSiteId}/orgProducts' : '/{baseSiteId}/products'}";

	private B2boccaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}
}
