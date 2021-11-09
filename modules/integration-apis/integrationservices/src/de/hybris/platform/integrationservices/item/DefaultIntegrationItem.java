/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.item;

import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * Default implementation of the {@link IntegrationItem} interface
 */
public class DefaultIntegrationItem implements IntegrationItem
{
	private static final String NULL_KEY_SUBSTITUTE = "\u0002\u0003"; // SOT + ETX (start of text followed by end of text)
	private static final StringToLocaleConverter TAG_CONVERTER = new StringToLocaleConverter();

	private final TypeDescriptor itemType;
	private String integrationKey;
	private final Map<String, Object> attributeValues;
	private final IntegrationItem containerItem;

	/**
	 * Instantiates this integration item.
	 *
	 * @param type descriptor of the item type this new instance will have
	 * @param key  integration key of this item. Integration key can be {@code null}
	 */
	public DefaultIntegrationItem(@NotNull final TypeDescriptor type, final String key)
	{
		this(type, key, null);
		integrationKey = key;
	}

	/**
	 * Instantiates this integration item.
	 *
	 * @param type          descriptor of the item type this new instance will have
	 * @param containerItem the integration item which contains {@code this} item
	 */
	public DefaultIntegrationItem(final TypeDescriptor type, final IntegrationItem containerItem)
	{
		this(type, null, containerItem);
	}

	private DefaultIntegrationItem(@NotNull final TypeDescriptor type, final String key, final IntegrationItem containerItem)
	{
		Preconditions.checkArgument(type != null, "Item type is required for integration items");
		itemType = type;
		attributeValues = new HashMap<>();
		this.containerItem = containerItem;
		integrationKey = key;
	}

	@Override
	public String getIntegrationObjectCode()
	{
		return itemType.getIntegrationObjectCode();
	}

	@Nonnull
	@Override
	public TypeDescriptor getItemType()
	{
		return itemType;
	}

	@Override
	public String getIntegrationKey()
	{
		return integrationKey;
	}

	@Override
	public Collection<TypeAttributeDescriptor> getAttributes()
	{
		final var typeDescriptor = getItemType();
		return attributeValues.keySet().stream()
		                      .map(typeDescriptor::getAttribute)
		                      .filter(Optional::isPresent)
		                      .map(Optional::get)
		                      .collect(Collectors.toSet());
	}

	/**
	 * {@inheritDoc}
	 * <p>This method does enforce any invariants, e.g. attribute existence, match of the
	 * value type to the declared in the metadata, etc.</p>
	 */
	@Override
	public boolean setAttribute(final String attrName, final Object value)
	{
		final Optional<TypeAttributeDescriptor> attribute = itemType.getAttribute(attrName);
		attribute.ifPresent(desc -> attributeValues.put(attrName, value));
		return attribute.isPresent();
	}

	@Override
	public Object getAttribute(final String name)
	{
		return attributeValues.get(name);
	}

	@Override
	public Object getAttribute(final TypeAttributeDescriptor attr)
	{
		return getAttribute(attr.getAttributeName());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException when the attribute is not localized
	 */
	@Override
	public Object getLocalizedAttribute(final String attribute, final String lang)
	{
		return getLocalizedAttribute(attribute, TAG_CONVERTER.convert(lang));
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException when the attribute is not localized
	 */
	@Override
	public Object getLocalizedAttribute(final String attribute, final Locale lang)
	{
		return itemType.getAttribute(attribute)
		               .map(attr -> getLocalizedAttribute(attr, lang))
		               .orElse(null);
	}

	private Object getLocalizedAttribute(final TypeAttributeDescriptor attribute, final Locale lang)
	{
		Preconditions.checkArgument(attribute.isLocalized(), "Attribute [" + attribute + "] is not localized");
		final Object value = getAttribute(attribute.getAttributeName());
		return value instanceof LocalizedValue
				? ((LocalizedValue) value).get(lang)
				: null;
	}

	@Override
	public IntegrationItem getReferencedItem(final String attribute)
	{
		return itemType.getAttribute(attribute)
		               .map(this::getReferencedItem)
		               .orElse(null);
	}

	@Override
	public IntegrationItem getReferencedItem(final TypeAttributeDescriptor attribute)
	{
		Preconditions.checkArgument(!attribute.isCollection(),
				attribute + " does not contain a single item but a collection. Use getReferencedItems() instead.");
		final Collection<IntegrationItem> items = getReferencedItems(attribute);
		return items.size() == 1
				? items.iterator().next()
				: null;
	}

	@Override
	public Collection<IntegrationItem> getReferencedItems(final String attribute)
	{
		return itemType.getAttribute(attribute)
		               .map(this::getReferencedItems)
		               .orElse(Collections.emptyList());
	}

	@Override
	public Collection<IntegrationItem> getReferencedItems(final TypeAttributeDescriptor attribute)
	{
		Preconditions.checkArgument(!attribute.getAttributeType().isPrimitive(),
				attribute + " does not refer to another IntegrationItem");
		final Object value = getAttribute(attribute.getAttributeName());
		return value instanceof Collection
				? ((Collection<?>) value).stream()
				                         .map(IntegrationItem.class::cast)
				                         .collect(Collectors.toList())
				: Collections.singleton((IntegrationItem) value);
	}

	/**
	 * Sets the integrationKey on this item.
	 *
	 * @param key integration key of this item. Integration key can be {@code null}
	 */
	public void setIntegrationKey(final String key)
	{
		integrationKey = key;
	}

	@Override
	public Optional<IntegrationItem> getContextItem(final TypeDescriptor contextItemDescriptor)
	{
		return containerItem != null
				? getContextItem(contextItemDescriptor, containerItem)
				: Optional.empty();
	}

	private Optional<IntegrationItem> getContextItem(final TypeDescriptor contextItemDescriptor,
	                                                 @NotNull final IntegrationItem integrationItem)
	{
		return integrationItem.getItemType().equals(contextItemDescriptor) ?
				Optional.of(copyWithOnlyKeyAttributes(integrationItem)) :
				integrationItem.getContextItem(contextItemDescriptor);
	}

	private IntegrationItem copyWithOnlyKeyAttributes(final IntegrationItem containerItem)
	{
		final TypeDescriptor containerItemType = containerItem.getItemType();
		final IntegrationItem copy = new DefaultIntegrationItem(containerItemType,
				containerItem.getIntegrationKey());
		containerItem.getAttributes().stream()
		             .filter(TypeAttributeDescriptor::isKeyAttribute)
		             .forEach(attribute -> copy.setAttribute(attribute.getAttributeName(),
				             getAttributeValue(containerItem, attribute)));
		return copy;
	}

	private Object getAttributeValue(final IntegrationItem containerItem, final TypeAttributeDescriptor attribute)
	{
		final Object attributeValue = containerItem.getAttribute(attribute.getAttributeName());
		return attributeValue instanceof IntegrationItem ?
				copyWithOnlyKeyAttributes((IntegrationItem) attributeValue)
				: attributeValue;
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
			final DefaultIntegrationItem that = (DefaultIntegrationItem) o;
			return itemType.equals(that.itemType) && Objects.equals(integrationKey, that.integrationKey);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		final String key = integrationKey != null
				? integrationKey
				: NULL_KEY_SUBSTITUTE; // without this value null calculates to hash code value of 0 - same as for ""
		return new HashCodeBuilder()
				.append(itemType)
				.append(key)
				.build();
	}

	@Override
	public String toString()
	{
		return "IntegrationItem{" +
				"integrationKey='" + integrationKey + '\'' +
				'}';
	}
}
