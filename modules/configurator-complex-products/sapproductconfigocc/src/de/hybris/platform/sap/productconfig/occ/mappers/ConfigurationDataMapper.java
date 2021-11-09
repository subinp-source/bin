/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.mappers;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import ma.glasnost.orika.MappingContext;


public class ConfigurationDataMapper extends AbstractCustomMapper<ConfigurationData, ConfigurationWsDTO>
{

	protected static final String FIELD_ROOT_PRODUCT = "rootProduct";
	protected static final String FIELD_KB_KEY = "kbKey";

	@Override
	public void mapAtoB(final ConfigurationData configurationData, final ConfigurationWsDTO configurationWsDto,
			final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(FIELD_KB_KEY, getAType(), configurationData, FIELD_ROOT_PRODUCT, getBType(), configurationWsDto);
		try
		{
			if (shouldMap(configurationData, configurationWsDto, context) && configurationData.getKbKey() != null)
			{
				configurationWsDto.setRootProduct(configurationData.getKbKey().getProductCode());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final ConfigurationWsDTO configurationWsDto, final ConfigurationData configurationData,
			final MappingContext context)
	{

		context.beginMappingField(FIELD_ROOT_PRODUCT, getBType(), configurationWsDto, FIELD_KB_KEY, getAType(), configurationData);
		try
		{
			if (shouldMap(configurationWsDto, configurationData, context) && configurationWsDto.getRootProduct() != null)
			{
				final KBKeyData kbKey = new KBKeyData();
				kbKey.setProductCode(configurationWsDto.getRootProduct());
				configurationData.setKbKey(kbKey);
			}
		}
		finally
		{
			context.endMappingField();
		}

	}
}
