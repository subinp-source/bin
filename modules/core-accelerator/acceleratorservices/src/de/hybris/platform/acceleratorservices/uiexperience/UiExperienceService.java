/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.uiexperience;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;

/**
 * Defines services related to {@link UiExperienceLevel}
 */
public interface UiExperienceService
{
	/**
	 * Get detected ui experience level attribute value
	 * 
	 * @return the ui experience level
	 */
	UiExperienceLevel getDetectedUiExperienceLevel();

	/**
	 * Set detected ui experience level attribute value
	 * 
	 * @param uiExperienceLevel 
	 * 		the ui experience level
	 */
	void setDetectedUiExperienceLevel(UiExperienceLevel uiExperienceLevel);

	/**
	 * Get the override ui experience level attribute value
	 * 
	 * @return the ui experience level
	 */
	UiExperienceLevel getOverrideUiExperienceLevel();

	/**
	 * Set the override ui experience level attribute value if value is not null and different 
	 * from detected level, else remove the attribute
	 * 
	 * @param uiExperienceLevel
	 * 		the ui experience level
	 */
	void setOverrideUiExperienceLevel(UiExperienceLevel uiExperienceLevel);

	/**
	 * Gets the ui experience level
	 * 
	 * @return the override ui experience level, if null then gets the detected ui experience level
	 */
	UiExperienceLevel getUiExperienceLevel();
}
