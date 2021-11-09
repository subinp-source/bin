/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.promotions.dao;

import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;


public interface PromotionsDao extends Dao
{
	AbstractPromotionModel getPromotionForCode(String code);
}
