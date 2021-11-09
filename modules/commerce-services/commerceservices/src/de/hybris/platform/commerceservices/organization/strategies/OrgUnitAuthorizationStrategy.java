/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.organization.strategies;

import de.hybris.platform.core.model.user.UserModel;


/**
 * Authorization strategy for Organization Unit users.
 */
public interface OrgUnitAuthorizationStrategy
{
	/**
	 * Check if user is allowed to create.
	 *
	 * @param user
	 *           the {@link UserModel} to check
	 */
	void validateCreatePermission(UserModel user);

	/**
	 * Check if user is allowed to edit.
	 *
	 * @param user
	 *           the {@link UserModel} to check
	 */
	void validateEditPermission(UserModel user);

	/**
	 * Check if user is allowed to view.
	 *
	 * @param user
	 *           the {@link UserModel} to check
	 */
	void validateViewPermission(UserModel user);

	/**
	 * Check if user is allowed to edit parent unit.
	 *
	 * @param user
	 *           the user
	 */
	void validateEditParentPermission(UserModel user);

	/**
	 * Check if user is allowed to edit.
	 *
	 * @param user
	 *           the user
	 * @return true, if user is allowed to edit, false otherwise
	 */
	boolean canEditUnit(UserModel user);

	/**
	 * Check if user is allowed to edit parent unit.
	 *
	 * @param user
	 *           the user
	 * @return true, if user is allowed to edit parent unit, false otherwise
	 */
	boolean canEditParentUnit(UserModel user);
}
