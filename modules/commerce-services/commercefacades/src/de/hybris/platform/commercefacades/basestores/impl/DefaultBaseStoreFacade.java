/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.basestores.impl;

import de.hybris.platform.commercefacades.basestore.data.BaseStoreData;
import de.hybris.platform.commercefacades.basestores.BaseStoreFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Default implementation of {@link BaseStoreFacade}
 */
public class DefaultBaseStoreFacade implements BaseStoreFacade
{
	private BaseStoreService baseStoreService;
	private Converter<BaseStoreModel, BaseStoreData> baseStoreConverter;

	@Override
	public BaseStoreData getBaseStoreByUid(final String uid)
	{
		validateParameterNotNullStandardMessage("Base Store Name", uid);

		final BaseStoreModel baseStore = getBaseStoreService().getBaseStoreForUid(uid);

		return getBaseStoreConverter().convert(baseStore);
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected Converter<BaseStoreModel, BaseStoreData> getBaseStoreConverter()
	{
		return baseStoreConverter;
	}

	@Required
	public void setBaseStoreConverter(final Converter<BaseStoreModel, BaseStoreData> baseStoreConverter)
	{
		this.baseStoreConverter = baseStoreConverter;
	}
}
