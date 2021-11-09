/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * Describes platform type in the context of an integration object item type. It may represent type of an integration object item
 * or a primitive, or other types, which integration object item attributes may have.
 * This metadata can be used for converting a custom payload to and from the platform's
 * {@link de.hybris.platform.core.model.ItemModel}
 */
public interface TypeDescriptor
{
	/**
	 * Reads value of the integration object code.
	 * @return integration object code this item belongs to.
	 */
	@NotNull
	String getIntegrationObjectCode();

	/**
	 * Reads value of the integration object item code.
	 * @return integration object item type code. Keep in mind this value may be different from the type code of the item the
	 * integration object item represents. For example, type code of the item in the platform may be "Product" but integration
	 * object item type could be called "IntegrationProduct".
	 * @see #getTypeCode()
	 */
	@NotNull
	String getItemCode();

	/**
	 * Reads code (name) of the integration object item type in the type system.
	 * @return type code of the item in the type system. Keep in mind this value may be different from the integration objec item
	 * code. For example, type code of the item in the type system may be "Product" but integration
	 * object item code may be different, e.g "IntegrationProduct".
	 * @see #getItemCode()
	 */
	@NotNull
	String getTypeCode();

	/**
	 * Retrieves descriptor of the specified attribute.
	 * @param attrName name of the integration object item attribute to be retrieved. Keep in mind that name of the attribute
	 * in an integration object item may be different from the name of the corresponding attribute in the type system.
	 * @return attribute descriptor for the given name or {@code Optional.empty()}, if the item type does not have an attribute
	 * with such name.
	 */
	@NotNull
	Optional<TypeAttributeDescriptor> getAttribute(String attrName);

	/**
	 * Retrieves all attributes defined in an integration object item.
	 * @return a collection of all attributes in the integration object item or an empty collection, if this description is for
	 * a primitive type or the item does not have attributes.
	 */
	@NotNull
	Collection<TypeAttributeDescriptor> getAttributes();

	/**
	 * Determines whether this descriptor is for type representing integration object item or for a primitive type.
	 * @return {@code true}, if this descriptor is for a primitive type, e.g. Integer, String, etc; {@code false}, otherwise.
	 */
	boolean isPrimitive();

	/**
	 * Determines whether this descriptor is for a type representing integration object item or for a map type.
	 * @return {@code true}, if this descriptor is for a map type; {@code false}, otherwise.
	 */
	default boolean isMap()
	{
		return false;
	}

	/**
	 * Determines whether this descriptor is for a type representing type system enumeration.
	 * @return {@code true}, if this descriptor is for an enumeration type; {@code false} otherwise.
	 */
	boolean isEnumeration();

	/**
	 * Determines whether this descriptor is an abstract item type
	 * @return {@code true}, if the item type of this descriptor has {@code abstract == true}
	 */
	boolean isAbstract();

	/**
	 * Determines whether the specified object is an instance of the type presented by this type descriptor.
	 * @param obj an object to evaluate.
	 * @return {@code true}, if the specified object is an instance of the type presented by this type descriptor; {@code false},
	 * if the object is {@code null} or cannot be described by this type descriptor.
	 */
	boolean isInstance(Object obj);

	/**
	 * Determines whether this descriptor is for the root item in the Integration Object
	 * @return {@code true}, if this descriptor is the root; {@code false} otherwise.
	 */
	boolean isRoot();

	/**
	 * Retrieves key descriptor of this item type.
	 * @return descriptor of the key identifying instances of this item type.
	 */
	KeyDescriptor getKeyDescriptor();

	/**
	 * Retrieves attribute path from this item type to the root item type in the same integration object.
	 * @return a list of all possible paths in order from the shorter ones to the longer paths. If this type descriptor does not
	 * contain attribute(s) that eventually lead to the root item type, then an empty list is returned.
	 * @see #isRoot()
	 * @see IntegrationObjectDescriptor#getRootItemType()
	 */
	List<ReferencePath> getPathsToRoot();

	/**
	 * Determines whether it's possible to navigate from an integration item described by this type descriptor to the root item
	 * of the integration object.
	 * @return {@code true}, if this type descriptor describes a root item type in the integration object or it has a {@link ReferencePath}
	 * to the root item type.
	 * @see #isRoot()
	 * @see IntegrationObjectDescriptor#getRootItemType()
	 * @see #getPathsToRoot()
	 */
	boolean hasPathToRoot();
}
