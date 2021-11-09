/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.customerlocation;

import de.hybris.platform.acceleratorservices.store.data.UserLocationData;


/**
 * Customer location facade. It is used for retrieving customer location data.
 * 
 */
public interface CustomerLocationFacade
{
	/**
	 * Sets the user's location data which consists of the searchQuery of the preferred location and/or the geoPoint of the user
	 * If the searchQuery is null or only consists of spaces, it will be set as a blank string instead.
	 *
	 * @param userLocationData
	 *           user's location data
	 */
	void setUserLocationData(UserLocationData userLocationData);

	/**
	 * Gets the user's location data which consists of the searchQuery of the preferred location and/or the geoPoint of the user
	 *
	 * @return the user's location data
	 */
	UserLocationData getUserLocationData();
}
