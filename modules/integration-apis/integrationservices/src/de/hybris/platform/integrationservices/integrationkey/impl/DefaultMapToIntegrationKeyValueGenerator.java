/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.integrationkey.impl;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROP_DIV;
import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_TYPE_DIV;

import de.hybris.platform.integrationservices.integrationkey.KeyAttributeValue;
import de.hybris.platform.integrationservices.integrationkey.KeyValue;
import de.hybris.platform.integrationservices.model.KeyAttribute;
import de.hybris.platform.integrationservices.model.KeyDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Generates integration key value for a given {@link TypeDescriptor} from the attribute values represented by a {@code Map<String, Object>},
 * where the map key is the attribute name, and the map value is the corresponding attribute value.
 */
public class DefaultMapToIntegrationKeyValueGenerator
		extends AbstractIntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>>
{
	private static final int START_DATE_OFFSET = 6;
	private static final int END_DATE_OFFSET = 2;

	protected static final Pattern pattern = Pattern.compile("/Date\\((\\d+)\\)/");

	@Override
	public String generate(final TypeDescriptor itemType, final Map<String, Object> item)
	{
		return itemType != null && item != null ? getSerializedKey(itemType, item) : "";
	}

	private String getSerializedKey(final TypeDescriptor itemType, final Map<String, Object> entry)
	{
		final KeyDescriptor keyDescriptor = itemType.getKeyDescriptor();
		final KeyValue key = keyDescriptor.calculateKey(entry);

		return serializeKeyToString(key);
	}

	private String serializeKeyToString(final KeyValue key)
	{
		final String keyStr = key.getKeyAttributeValues().stream()
		                         .sorted(Comparator.comparing(this::keyAttributeComparableCode))
		                         .map(KeyAttributeValue::getValue)
		                         .map(this::transformValueToString)
		                         .map(this::encodeValue)
		                         .reduce("", (p, n) -> p + INTEGRATION_KEY_PROP_DIV + n);
		return keyStr.length() > 0
				? keyStr.substring(INTEGRATION_KEY_PROP_DIV.length()) // keyStr starts with it
				: keyStr;
	}

	private String keyAttributeComparableCode(final KeyAttributeValue value)
	{
		final KeyAttribute attribute = value.getAttribute();
		return attribute.getItemCode() + INTEGRATION_KEY_TYPE_DIV + attribute.getName();
	}

	@Override
	protected String transformValueToString(final Object attributeValue)
	{
		if (attributeValue instanceof String && pattern.matcher((String) attributeValue).matches())
		{
			final String date = attributeValue.toString();
			return date.substring(START_DATE_OFFSET, date.length() - END_DATE_OFFSET);
		}
		else if (attributeValue instanceof Calendar)
		{
			return String.valueOf(((Calendar) attributeValue).getTimeInMillis());
		}
		return String.valueOf(attributeValue);
	}
}
