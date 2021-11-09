/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.util;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;


public interface ImageHandler
{

	/**
	 * Converts image data
	 *
	 * @param configData
	 *           Source configuration data
	 * @param configurationWs
	 *           Target configuationWs DTO
	 */
	void convertImages(final ConfigurationData configData, final ConfigurationWsDTO configurationWs);

}
