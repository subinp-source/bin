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

package de.hybris.platform.odata2services.odata.schema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;

import com.google.common.base.Preconditions;

/**
 * Any generator responsible for entity type key generation. This class provides a template for key generation. This template uses
 * only simple properties for the key; navigation properties are not even considered as candidates for the inclusion in the key.
 * The implementations can just choose which properties make a key and which don't by implementing {@link #isKey(SimpleProperty)}
 * method.
 */
public abstract class KeyGenerator implements SchemaElementGenerator<Optional<Key>, List<Property>>
{
	/**
	 * Generates a key object from a set of entity attributes received as a parameter
	 *
	 * @param entityProperties used to generate the key
	 * @return Key generated containing the property refs of the valid entity attributes received as a parameter
	 */
	@Override
	public Optional<Key> generate(final List<Property> entityProperties)
	{
		final List<SimpleProperty> simpleProperties = getSimpleProperties(entityProperties);
		final List<PropertyRef> keyPropRefs = simpleProperties.stream()
		                                                      .filter(this::isKey)
		                                                      .map(this::toPropertyRef)
		                                                      .collect(Collectors.toList());
		return keyPropRefs.isEmpty() ? Optional.empty() : Optional.of(createKey(keyPropRefs));
	}

	/**
	 * Determines whether the specified property is a key property. Any property that is determined to be a key property will be
	 * included into the generated entity type key.
	 * @param simpleProperty a property to be checked.
	 * @return {@code true}, if the property is a key property; {@code false} otherwise.
	 * @see #createKey(List)
	 */
	protected abstract boolean isKey(final SimpleProperty simpleProperty);

	/**
	 * Creates a entity type key
	 * @param keyProperties references to the properties selected to be the key properties
	 * @return a key containing all the specified property references.
	 * @see #isKey(SimpleProperty)
	 */
	protected Key createKey(final List<PropertyRef> keyProperties)
	{
		return new Key().setKeys(keyProperties);
	}

	private PropertyRef toPropertyRef(final SimpleProperty property)
	{
		return new PropertyRef().setName(property.getName());
	}

	protected static List<SimpleProperty> getSimpleProperties(final List<Property> entityProperties)
	{
		Preconditions.checkArgument(entityProperties != null, "Cannot generate a key out of null entityProperties");
		return entityProperties.stream()
		                       .filter(SimpleProperty.class::isInstance)
		                       .map(SimpleProperty.class::cast)
		                       .collect(Collectors.toList());
	}
}
