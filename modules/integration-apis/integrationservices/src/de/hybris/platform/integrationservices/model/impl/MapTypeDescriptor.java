/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.KeyDescriptor;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

public class MapTypeDescriptor implements TypeDescriptor
{
	private final MapTypeModel mapTypeModel;
	private final IntegrationObjectModel integrationObject;


	MapTypeDescriptor(final IntegrationObjectItemAttributeModel mapTypeAttribute)
	{
		Preconditions.checkArgument(mapTypeAttribute != null, "Non-null integration object item attribute is required");
		Preconditions.checkArgument(mapTypeAttribute.getAttributeDescriptor().getAttributeType() instanceof MapTypeModel, "AttributeDescriptor of MapTypeModel is required");
		integrationObject = mapTypeAttribute.getIntegrationObjectItem().getIntegrationObject();
		mapTypeModel = (MapTypeModel) mapTypeAttribute.getAttributeDescriptor().getAttributeType();
	}

	/**
	 * Creates an instance of this type descriptor
	 *
	 * @param mapTypeAttribute attribute model that is of a MapType
	 * @return a descriptor for the given model.
	 * <p>Note: the implementation does not guarantee creation of a new instance for every invocation. The implementation may
	 * change to cache the values for efficiency.</p>
	 */
	static TypeDescriptor create(final IntegrationObjectItemAttributeModel mapTypeAttribute)
	{
		return new MapTypeDescriptor(mapTypeAttribute);
	}

	@Override
	public @NotNull String getIntegrationObjectCode()
	{
		return integrationObject.getCode();
	}

	@Override
	public @NotNull String getItemCode()
	{
		return getTypeCode();
	}

	@Override
	public @NotNull String getTypeCode()
	{
		return mapTypeModel.getCode();
	}

	@Override
	public @NotNull Optional<TypeAttributeDescriptor> getAttribute(final String attrName)
	{
		return Optional.empty();
	}

	@Override
	public @NotNull Collection<TypeAttributeDescriptor> getAttributes()
	{
		return new HashSet<>();
	}

	@Override
	public boolean isPrimitive()
	{
		return false;
	}

	@Override
	public boolean isMap()
	{
		return true;
	}

	@Override
	public boolean isEnumeration()
	{
		return false;
	}

	@Override
	public boolean isAbstract()
	{
		return false;
	}

	@Override
	public boolean isInstance(final Object obj)
	{
		return obj instanceof Map;
	}

	@Override
	public boolean isRoot()
	{
		return false;
	}

	@Override
	public KeyDescriptor getKeyDescriptor()
	{
		return new NullKeyDescriptor();
	}

	@Override
	public List<ReferencePath> getPathsToRoot()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean hasPathToRoot()
	{
		return ! getPathsToRoot().isEmpty();
	}
}
