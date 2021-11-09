/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.integration.cis.subscription.service.CreditCardMappingService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.integration.cis.subscription.util.MapUtils;


/**
 * Implements the functionality of converting the credit card codes in hybris to those of the provider
 */
public class DefaultCreditCardMappingService implements CreditCardMappingService
{
	private static final String VENDOR_MAPPING_PATH = "/vendor-mapping.properties";
	private static final String REQUEST_CONVERT_KEY = "%s.creditcard.%s.request";

	/**
	 * Load vendor credit card mapping from property file.
	 *
	 * @return vendor credit card mapping
	 */
	public Map<String, String> initialize()
	{
		final Map<String, String> vendorCCMapping = new HashMap<String, String>();

		for (final Entry<Object, Object> entry : MapUtils.loadPropertiesToSet(VENDOR_MAPPING_PATH))
		{
			vendorCCMapping.put((String) entry.getKey(), (String) entry.getValue());
		}

		return vendorCCMapping;
	}

	@Override
	public boolean convertCCToProviderSpecificName(@Nonnull final Collection<CardTypeData> creditCards, final String vendor)
	{
		validateParameterNotNullStandardMessage("creditCards", creditCards);

		boolean converted = false;
		final Map<String, String> vendorCCMapping = initialize();

		for (final CardTypeData cardType : creditCards)
		{
			final String key = String.format(REQUEST_CONVERT_KEY, vendor.toLowerCase(), cardType.getCode().toLowerCase());

			if (vendorCCMapping.containsKey(key))
			{
				final String mappingValue = vendorCCMapping.get(key);

				if (StringUtils.isNotEmpty(mappingValue))
				{
					cardType.setCode(mappingValue);
					converted = true;
				}
			}
		}

		return converted;
	}

}
