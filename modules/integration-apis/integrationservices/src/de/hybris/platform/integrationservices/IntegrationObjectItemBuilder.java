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

package de.hybris.platform.integrationservices;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * A builder for creating {@link IntegrationObjectItemModel}.
 */
public class IntegrationObjectItemBuilder
{
	private TypeService typeService;
	private String code;

	private final Set<IntegrationObjectItemAttributeModel> attributes = new HashSet<>();
	private ComposedTypeModel objectType;
	private IntegrationObjectModel integrationObject;

	public static IntegrationObjectItemBuilder item()
	{
		return new IntegrationObjectItemBuilder();
	}

	public IntegrationObjectItemBuilder forType(final String type)
	{
		return forType(getTypeService().getComposedTypeForCode(type));
	}

	public IntegrationObjectItemBuilder withAttribute(final IntegrationObjectItemAttributeBuilder spec)
	{
		return withAttribute(spec.forObjectOfType(objectType).build());
	}

	public IntegrationObjectItemBuilder withCode(final String code)
	{
		this.code = code;
		return this;
	}

	public IntegrationObjectItemBuilder withIntegrationObjectCode(final String code)
	{
		return withIntegrationObject(IntegrationObjectBuilder.integrationObject().withCode(code));
	}

	public IntegrationObjectItemBuilder withIntegrationObject(final IntegrationObjectBuilder spec)
	{
		integrationObject = spec.build();
		return this;
	}

	private IntegrationObjectItemBuilder forType(final ComposedTypeModel type)
	{
		objectType = type;
		attributes.forEach(attr -> attr.getAttributeDescriptor().setEnclosingType(objectType));
		return this;
	}

	private IntegrationObjectItemBuilder withAttribute(final IntegrationObjectItemAttributeModel attr)
	{
		attributes.add(attr);
		return this;
	}

	public IntegrationObjectItemModel build()
	{
		final IntegrationObjectItemModel obj = new IntegrationObjectItemModel();
		obj.setType(objectType);
		obj.setCode(StringUtils.defaultIfBlank(code, deriveDefaultTypeCode()));
		obj.setAttributes(attributes);
		obj.setIntegrationObject(integrationObject);
		return obj;
	}

	private String deriveDefaultTypeCode()
	{
		return objectType != null
				? objectType.getCode()
				: null;
	}

	private TypeService getTypeService()
	{
		if (typeService == null)
		{
			typeService = Registry.getApplicationContext().getBean("typeService", TypeService.class);
		}
		return typeService;
	}
}
