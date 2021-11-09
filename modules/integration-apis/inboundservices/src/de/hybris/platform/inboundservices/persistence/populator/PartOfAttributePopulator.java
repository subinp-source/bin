/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

/**
 * A populator for assigning {@code owner} attribute in the referenced item values when they are part of the item being populated.
 */
public class PartOfAttributePopulator extends AbstractAttributePopulator
{
	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return attribute.isPartOf() && !attribute.isPrimitive();
	}

	@Override
	protected void populateAttribute(final ItemModel item, final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		final Locale locale = context.getContentLocale();
		final Object value = item.getProperty(attribute.getQualifier());

		final Collection<?> values = value instanceof Collection
				? (Collection<?>) value
				: Collections.singleton(value);
		values.forEach(val -> associateReferencedItemWithItsOwner(item, (ItemModel) val, attribute, locale));
	}

	private void associateReferencedItemWithItsOwner(final ItemModel item, final ItemModel referencedItem, final TypeAttributeDescriptor attribute, final Locale locale)
	{
		if (isNew(referencedItem))
		{
			final Optional<TypeAttributeDescriptor> reverseAttribute = attribute.reverse();
			reverseAttribute.ifPresentOrElse(attr -> setProperty(item, locale, referencedItem, attr), () -> setOwnerIfNotPresent(item, referencedItem));
		}
	}

	private void setOwnerIfNotPresent(final ItemModel item, final ItemModel referencedItem)
	{
		if (referencedItem.getOwner() == null)
		{
			referencedItem.setOwner(item);
		}
	}

	private void setProperty(final ItemModel item, final Locale locale, final ItemModel referencedItem, final TypeAttributeDescriptor ownerAttribute)
	{
		final String ownerAttributeQualifier = ownerAttribute.getQualifier();
		if(ownerAttribute.isLocalized() && referencedItem.getProperty(ownerAttributeQualifier, locale) == null)
		{
			referencedItem.setProperty(ownerAttributeQualifier, locale, item);
		}
		else if(!ownerAttribute.isLocalized() && referencedItem.getProperty(ownerAttributeQualifier) == null)
		{
			referencedItem.setProperty(ownerAttributeQualifier, item);
		}
	}
}
