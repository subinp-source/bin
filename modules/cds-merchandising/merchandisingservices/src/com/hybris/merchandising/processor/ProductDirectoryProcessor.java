/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.processor;

import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;


/**
 * An interface to update {@link MerchProductDirectoryConfigModel} which are stored in Catalog service
 */
public interface ProductDirectoryProcessor
{

	/**
	 * Process {@link MerchProductDirectoryConfigModel} which is encapsulates MerchProductDirectoryConfig
	 * @param merchProductDirectoryConfigModel
	 */
	void createUpdate(MerchProductDirectoryConfigModel merchProductDirectoryConfigModel);

	void delete(MerchProductDirectoryConfigModel merchProductDirectoryConfigModel);
}
