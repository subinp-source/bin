/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.rules.rao.provider;

import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.sap.productconfig.rules.rao.BaseStoreRAO;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Collections;
import java.util.Set;


/**
 * provider for the {@link BaseStoreRAO}
 */
public class ProductConfigBaseStoreRAOProvider implements RAOProvider
{
	private BaseStoreService baseStoreService;

	@Override
	public Set<BaseStoreRAO> expandFactModel(final Object modelFact)
	{
		final Set<BaseStoreRAO> raoSet;
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		if (baseStore != null)
		{
			final BaseStoreRAO baseStoreRAO = new BaseStoreRAO();
			baseStoreRAO.setUid(baseStore.getUid());
			raoSet = Collections.singleton(baseStoreRAO);
		}
		else
		{
			raoSet = Collections.emptySet();
		}
		return raoSet;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * @param baseStoreService
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}
}
