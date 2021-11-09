/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;

import java.util.List;


/**
 * Provides methods for checking completeness and consistency of groups.
 */
public interface GroupStatusChecker
{

	/**
	 * Checks group for completeness.
	 *
	 * @param group
	 *           group to check
	 * @return true if all required characteristics have a value assigned.
	 */
	boolean checkCompleteness(final UiGroupData group);

	/**
	 * Checks list of groups for completeness.
	 *
	 * @param subGroups
	 *           list of groups
	 * @return true if all groups are marked as complete
	 */
	boolean checkCompletenessForSubGroups(final List<UiGroupData> subGroups);

	/**
	 * Checks group for consistency, i.e. no characteristic of group is involved in a conflict.
	 *
	 * @param group
	 *           group to check
	 * @return true if no characteristic of group is involved in a conflict
	 */
	boolean checkConsistency(final CsticGroup group);

	/**
	 * Checks list of groups for consistency, i.e. no characteristic of each group is involved in a conflict.
	 *
	 * @param subGroups
	 *           list of groups
	 * @return true if no characteristic of each group is involved in a conflict
	 */
	boolean checkConsistencyForSubGroups(final List<UiGroupData> subGroups);

}
