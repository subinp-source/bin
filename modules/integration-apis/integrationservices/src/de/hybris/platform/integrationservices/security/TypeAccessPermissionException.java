/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.security;

import javax.validation.constraints.NotNull;

/**
 * Throws this exception when the user lacks privileges in accessing the given item type.
 */
public class TypeAccessPermissionException extends RuntimeException
{
	private static final String ERR_MSG = "The user lacks %s privileges for accessing the %s.";
	private final String itemType;
	private final String permission;

	/**
	 * Instantiates this exception.
	 * @param itemType item type, to which the user violates access permissions.
	 * @param permission permission the user does not have in order to access the {@code itemType}. For example, read,
	 *                   write, etc.
	 */
	public TypeAccessPermissionException(final String itemType, @NotNull final String permission)
	{
		super(message(itemType, permission));
		this.itemType = itemType;
		this.permission = permission;
	}

	/**
	 * Instantiates this exception.
	 * @param permission permission the user does not have in order to access the Integration Object. For example, read,
	 *                   write, etc.
	 */
	public TypeAccessPermissionException(@NotNull final String permission)
	{
		this(null, permission);
	}

	/**
	 * Retrieves protected item type.
	 * @return item type the user attempted to access without having a corresponding permission.
	 * @see #getPermission()
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * Retrieves violated permission
	 * @return permission the user lacks in order to access the item type.
	 * @see #getItemType()
	 */
	public String getPermission() {
		return permission;
	}

	private static String message(final String itemType, final String permission)
	{
		final var typeInfo = itemType != null
				? itemType + " type"
				: "Integration Object types";

		return String.format(ERR_MSG, permission, typeInfo);
	}
}
