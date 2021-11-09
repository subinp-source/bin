/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basestores;

import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;


/**
 * Facade for management of base stores - Its main purpose is to retrieve base store information using existing services.
 */
public interface BaseStoreFacade
{
	/**
	 * Returns base store DTO for a given base store uid
	 *
	 * @param uid
	 * 		the base store unique identifier
	 * @return the corresponding base store
	 */
	BaseStoreData getBaseStoreByUid(String uid);
}
