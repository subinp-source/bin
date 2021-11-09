/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
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

import de.hybris.platform.integrationservices.exception.LocalizedKeyAttributeException;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyMetadataGenerator;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.KeyAttribute;
import de.hybris.platform.integrationservices.model.impl.ItemTypeDescriptor;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class DefaultAlphabeticalIntegrationKeyMetadataGenerator implements IntegrationKeyMetadataGenerator
{
	@Override
	public String generateKeyMetadata(final IntegrationObjectItemModel item)
	{
		Preconditions.checkArgument(item != null, "Cannot generate integration key metadata for null");

		return buildKeyAlias(item);
	}

	/**
	 * @deprecated In queue for removal
	 */
	@Deprecated(since="1905.06-CEP", forRemoval=true)
	protected String buildKeyMetadata(final Collection<IntegrationObjectItemAttributeModel> uniqueAttributes)
	{
		final StringBuilder keyMetadata = uniqueAttributes.stream()
				.filter(this::nonLocalizable)
				.map(attr ->
						String.format("%s%s%s",
								attr.getIntegrationObjectItem().getCode(),
								INTEGRATION_KEY_TYPE_DIV,
								attr.getAttributeName()))
				.distinct()
				.sorted()
				.map(StringBuilder::new)
				.reduce(new StringBuilder(), (a, b) -> a.append(b).append(INTEGRATION_KEY_PROP_DIV));

		return StringUtils.chop(keyMetadata.toString());
	}

	/**
	 * @deprecated In queue for removal
	 */
	@Deprecated(since="1905.06-CEP", forRemoval=true)
	private boolean nonLocalizable(final IntegrationObjectItemAttributeModel a)
	{
		if(a.getAttributeDescriptor().getLocalized())
		{
			throw new LocalizedKeyAttributeException(a);
		}
		return true;
	}

	protected String buildKeyAlias(final IntegrationObjectItemModel item)
	{

		final StringBuilder stringBuilder = ItemTypeDescriptor.create(item)
				.getKeyDescriptor()
				.getKeyAttributes()
				.stream()
				.map(this::formatAliasPart)
				.distinct()
				.sorted()
				.map(StringBuilder::new)
				.reduce(
						new StringBuilder(), (a, b) -> a.append(b).append(INTEGRATION_KEY_PROP_DIV)
				);

		return StringUtils.chop(stringBuilder.toString());
	}

	protected String formatAliasPart(final KeyAttribute keyAttribute)
	{
		return String.format("%s%s%s",
				keyAttribute.getItemCode(),
				INTEGRATION_KEY_TYPE_DIV,
				keyAttribute.getName());
	}
}
