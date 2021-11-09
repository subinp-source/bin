/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model;

import de.hybris.platform.integrationservices.model.impl.DelegatingAttributeValueAccessor;
import de.hybris.platform.integrationservices.model.impl.NullAttributeValueGetter;
import de.hybris.platform.integrationservices.model.impl.NullAttributeValueSetter;

import java.util.Optional;

/**
 * Describes an item type attribute from the integration object item point of view. Provides metadata necessary for conversion
 * of the payloads to/from the {@link de.hybris.platform.core.model.ItemModel}.
 */
public interface TypeAttributeDescriptor
{
	/**
	 * Gets name of the attribute described by this descriptor.
	 *
	 * @return name of the attribute as it was defined for the integration object.
	 */
	String getAttributeName();

	/**
	 * Gets name of this attribute in the platform type system.
	 *
	 * @return name of this attribute in the type system.
	 */
	String getQualifier();

	/**
	 * Determines whether this attribute contains a single value or multiple values (a collection of values).
	 *
	 * @return {@code true}, if this attribute contains a collection of primitive or complex type values; {@code false}, if this
	 * attribute contains only a single primitive or complex type value.
	 */
	boolean isCollection();

	/**
	 * Retrieves type of this attribute value(s).
	 *
	 * @return type of this attribute value, which may be a primitive type or a complex type referring an item in the type system;
	 * or a type of the referenced element(s) in case, if this attribute contains a collection of values or represents a
	 * one-to-one, one-to-many or many-to-many relationship.
	 * @see #isCollection()
	 */
	TypeDescriptor getAttributeType();

	/**
	 * Retrieves descriptor of the type, this attribute is associated with.
	 *
	 * @return descriptor of the item type that contains the attribute described by this attribute descriptor.
	 */
	TypeDescriptor getTypeDescriptor();

	/**
	 * <p>Reverses the relation described by this attribute descriptor and retrieves an attribute descriptor defined in the
	 * {@link #getAttributeType()} type descriptor with {@link #getTypeDescriptor()} attribute type.</p>
	 * <p>For example, type {@code Parent} has attribute {@code children} that refers a collection of type {@code Child}; and
	 * type {@code Child} has an attribute {@code parent} referring back to {@code Parent} type. Then attribute descriptor for
	 * {@code parent} attribute is reverse for {@code children} attribute descriptor.</p>
	 * <p>Another example, type {@code Organization} has attribute {@code addresses} that refers a collection of type
	 * {@code Address}; but type {@code Address} does not refer back to type {@code Organization}. In this case there is no
	 * reverse attribute for {@code addresses} attribute descriptor.</p>
	 *
	 * @return an optional containing a descriptor for the attribute in type returned by {@link #getAttributeType()}, which refers
	 * back to the type retrieved by calling {@link #getTypeDescriptor()} on this attribute descriptor; or an empty optional
	 * that attribute type of this descriptor does not refer back to the type containing this attribute descriptor.
	 */
	default Optional<TypeAttributeDescriptor> reverse()
	{
		return Optional.empty();
	}

	/**
	 * Determines whether the attribute represented by this descriptor can take {@code null} values.
	 *
	 * @return {@code true}, if the attribute can take {@code null} values; {@code false}, if the attribute value is required.
	 */
	boolean isNullable();

	/**
	 * Determines whether the item referenced by this attribute should be a part of the attribute's item model or not.
	 * This defines in particular whether the nested referenced item will be created whenever the "container" item is
	 * persisted (part of) or it can and should exist and persist independently (not a part of).
	 * For example, a {@code Car} has an attribute {@code engine}, which refers item {@code Engine}. If the business model
	 * is interested in cars only and does not care about engines outside of the car model, then the attribute should be
	 * defined with {@code partOf == true}. If the engine has independent existence, e.g. engine can be sold without a car,
	 * then the attribute should defined with {@code partOf == false}.
	 * <p>In other words, this attribute descriptor defines a relation between an owner and the owned item. Thus in the example
	 * above {@code Engine} is owned by {@code Car} as it does not have independent existence in the model.</p>
	 *
	 * @return {@code true}, if the item referenced by this attribute descriptor is an integral part of its owner model.
	 * @see #isAutoCreate()
	 */
	default boolean isPartOf()
	{
		return false;
	}

	/**
	 * Determines whether the item referenced by this attribute should be persisted when the item with this attribute is persisted.
	 * Unlike {@link #isPartOf()} this method does not require the referenced item to be an integral part of the owner item.
	 * The referenced item may have its independent existence in the domain model and yet it will be persisted together with the
	 * item holding an attribute described by this descriptor.
	 *
	 * @return {@code true}, if the attribute's item should be included within the item holding an attribute described by
	 * this descriptor; {@code false}, if the referenced item should not be persisted with the item holding an attribute described
	 * by this descriptor.
	 * @see #isPartOf()
	 */
	boolean isAutoCreate();

	/**
	 * Determines whether the value of this descriptor can be localized. This method may need to be used with {@link #isMap()} to
	 * determine whether the attribute supports multiple locale specific values. Some attributes may be localized but do not
	 * support multi-locale values. For example, a simple Numeric or Boolean attribute can be declared as localized.
	 *
	 * @return true if localized, else false
	 */
	boolean isLocalized();

	/**
	 * Determines whether the value of this descriptor is a primitive type
	 *
	 * @return true if it's a primitive type, else false
	 */
	boolean isPrimitive();

	/**
	 * Determines whether the attribute is referring to a map of values. Typically used to determiine in conjunction with
	 * {@link #isLocalized()} to determine whether the attribute can hold multiple locale specific values. However, it can be used
	 * by itself.
	 *
	 * @return {@code true}, if the attribute refers to a map; {@code false} otherwise.
	 */
	default boolean isMap()
	{
		return false;
	}

	/**
	 * Determines whether this attribute value can be changed after it has been initialized.
	 *
	 * @return {@code true}, if this attribute value can change any time; {@code false}, if this attribute is read-only or only
	 * initial value can be set (immutable attribute).
	 * @deprecated use {@link #isSettable(Object)} instead
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	boolean isWritable();

	/**
	 * Determines whether this attribute permits initial value set on new item instances.
	 *
	 * @return {@code true}, if the attribute value is mutable or it is immutable but can be initialized; {@code false}, otherwise.
	 * @deprecated use {@link #isSettable(Object)} instead
	 */
	@Deprecated(since = "1905.11-CEP", forRemoval = true)
	boolean isInitializable();

	/**
	 * Indicates whether this attribute's value can be set.
	 *
	 * @return {@code true} if value is settable, {@code false} otherwise
	 */
	default boolean isSettable(final Object item)
	{
		return true;
	}

	/**
	 * Determines whether this attribute is a key attribute
	 *
	 * @return {@code true}, if the attribute is a key attribute, otherwise, false
	 */
	default boolean isKeyAttribute()
	{
		return false;
	}

	/**
	 * Determines whether this attribute is readable
	 *
	 * @return {@code true} if the attribute is readable, false otherwise. Returns {@code true} by default for backwards compatibility
	 */
	default boolean isReadable()
	{
		return true;
	}

	/**
	 * Gets the {@link CollectionDescriptor}
	 *
	 * @return A collection descriptor
	 */
	CollectionDescriptor getCollectionDescriptor();

	/**
	 * Gets a map descriptor for this attribute.
	 *
	 * @return {@code Optional} containing a {@code MapDescriptor}, if this attribute is of Map type; otherwise returns {@code Optional.empty()}
	 * @see #isMap()
	 */
	default Optional<MapDescriptor> getMapDescriptor()
	{
		return Optional.empty();
	}

	/**
	 * Gets the {@link AttributeValueAccessor} for accessing this attribute's value
	 *
	 * @return The AttributeValueAccessor
	 */
	default AttributeValueAccessor accessor()
	{
		return new DelegatingAttributeValueAccessor(new NullAttributeValueGetter(), new NullAttributeValueSetter());
	}
}
