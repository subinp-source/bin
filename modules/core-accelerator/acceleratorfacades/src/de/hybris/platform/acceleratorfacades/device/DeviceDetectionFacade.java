/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorfacades.device;

import de.hybris.platform.acceleratorfacades.device.data.DeviceData;

import javax.servlet.http.HttpServletRequest;


/**
 * Facade that handles device detection
 */
public interface DeviceDetectionFacade
{
	/**
	 * Initialise the device detection for the specified request.
	 * 
	 * @param request
	 *           the request
	 */
	void initializeRequest(HttpServletRequest request);

	/**
	 * Get the Device that was detected for the current request. Must be called within a request context.
	 * 
	 * @return the detected device data
	 */
	DeviceData getCurrentDetectedDevice();
}
