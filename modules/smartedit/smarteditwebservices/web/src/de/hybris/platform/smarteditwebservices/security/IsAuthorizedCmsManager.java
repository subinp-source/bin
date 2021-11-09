/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Annotation for securing rest endpoints. <br>
 * Only users that have role cmsmanager or admin can access this endpoint.
 */
@Target(
{ java.lang.annotation.ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('ROLE_BASECMSMANAGERGROUP') or hasRole('ROLE_ADMINGROUP')")
public @interface IsAuthorizedCmsManager
{
	// empty
}