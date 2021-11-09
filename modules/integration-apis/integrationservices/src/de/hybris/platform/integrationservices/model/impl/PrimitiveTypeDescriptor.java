/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.integrationservices.model.KeyDescriptor;
import de.hybris.platform.integrationservices.model.ReferencePath;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;

/**
 * {@inheritDoc}
 * <p>This implementation is effectively immutable and therefore is thread safe</p>
 * <p>Reuse this implementation through composition not inheritance</p>
 */
public class PrimitiveTypeDescriptor implements TypeDescriptor
{
	private final String integrationObjectCode;
	private final AtomicTypeModel typeModel;

	PrimitiveTypeDescriptor(final String objCode, final AtomicTypeModel type)
	{
		Preconditions.checkArgument(objCode != null, "Non-null integration object code is required");
		Preconditions.checkArgument(type != null, "Non-null atomic type model is required");
		integrationObjectCode = objCode;
		typeModel = type;
	}

	/**
	 * Creates an instance of this type descriptor
	 *
	 * @param objCode code of the integration object within which this item type is used.
	 * @param model a model to get a {@code PrimitiveTypeDescriptor} for
	 * @return a descriptor for the given model.
	 * <p>Note: the implementation does not guarantee creation of a new instance for every invocation. The implementation may
	 * change to cache the values for efficiency.</p>
	 */
	public static TypeDescriptor create(final String objCode, final AtomicTypeModel model)
	{
		return new PrimitiveTypeDescriptor(objCode, model);
	}


	@Override
	public String getIntegrationObjectCode()
	{
		return integrationObjectCode;
	}

	@Override
	public String getItemCode()
	{
		return getTypeCode();
	}

	@Override
	public String getTypeCode()
	{
		return typeModel.getCode();
	}

	@Override
	public Optional<TypeAttributeDescriptor> getAttribute(final String attrName)
	{
		return Optional.empty();
	}

	@Override
	public Collection<TypeAttributeDescriptor> getAttributes()
	{
		return new ArrayList<>(0);
	}

	@Override
	public boolean isPrimitive()
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
		return typeModel.getJavaClass().isInstance(obj);
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
		return false;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o != null && getClass() == o.getClass())
		{
			final PrimitiveTypeDescriptor that = (PrimitiveTypeDescriptor) o;
			return Objects.equals(typeModel.getCode(), that.typeModel.getCode());
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return typeModel.getCode().hashCode();
	}

	@Override
	public String toString()
	{
		return "PrimitiveTypeDescriptor{" +
				typeModel.getCode() +
				'}';
	}
}
