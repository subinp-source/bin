/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades;

/**
 * Handling of the Expert Mode. This supports user to see
 * also technical names and hidden fields on the UI layer.
 */
public interface ConfigurationExpertModeFacade
{
	/**
	 * Check if the current user belongs to the expert
	 * mode user group.
	 *
	 * @return If user is allowed return TRUE
	 */
	boolean isExpertModeAllowedForCurrentUser();

	/**
	 * Check if expert mode is currently activated.
	 *
	 * @return TRUE If the expert mode is activated
	 */
	boolean isExpertModeActive();

	/**
	 * Enable expert mode for the product configuration,
	 * so that technical information are shown in the UI.
	 */
	void enableExpertMode();

	/**
	 * Disable the expert mode.
	 */
	void disableExpertMode();
}
