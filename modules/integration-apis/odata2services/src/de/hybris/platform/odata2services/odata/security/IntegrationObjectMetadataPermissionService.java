/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.security;

import org.apache.olingo.odata2.api.processor.ODataContext;

/**
 * This Service provides a convenience method to determine permissions to access metadata on integration objects.
 */
public interface IntegrationObjectMetadataPermissionService
{
	/**
	 * Checks if the current user can read the metadata of the integration object present in the ODataContext.
	 * The implementation may throw a {@link RuntimeException} if the user does not have the permission.
	 *
	 * @param oDataContext OData context {@link ODataContext}
	 */
	void checkMetadataPermission(ODataContext oDataContext);
}
