/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl;

import static de.hybris.platform.integrationservices.model.impl.SettableUtils.isReadOnly;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.integrationservices.model.AttributeSettableChecker;
import de.hybris.platform.integrationservices.model.SettableAttributeHandler;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;

/**
 * Default implementation of the {@link AttributeSettableChecker}
 */
class DefaultAttributeSettableChecker implements AttributeSettableChecker
{
	private static final Logger LOG = Log.getLogger(DefaultAttributeSettableChecker.class);
	private final TypeService typeService;
	private List<SettableAttributeHandler> settableHandlers;

	public DefaultAttributeSettableChecker(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	public void setSettableHandlers(final List<SettableAttributeHandler> settableHandlers)
	{
		this.settableHandlers = Objects.requireNonNullElse(settableHandlers, Collections.emptyList());
	}

	@Override
	public boolean isSettable(final ItemModel item, final TypeAttributeDescriptor attribute)
	{
		final String qualifier = attribute.getQualifier();
		final AttributeDescriptorModel attributeDescriptorModel = getAttributeDescriptor(item, qualifier);
		final boolean settable = settableHandlers.stream()
		                                         .filter(handler -> handler.isApplicable(item))
		                                         .map(handler -> handler.isSettable(attributeDescriptorModel))
		                                         .allMatch(BooleanUtils::isTrue);
		logIfNotSettable(settable, attributeDescriptorModel, attribute);
		return settable;
	}

	private AttributeDescriptorModel getAttributeDescriptor(final ItemModel item, final String qualifier)
	{
		return typeService.getAttributeDescriptor(item.getItemtype(), qualifier);
	}

	private void logIfNotSettable(final boolean settable, final AttributeDescriptorModel descriptor,
	                              final TypeAttributeDescriptor attribute)
	{
		if (!settable)
		{
			LOG.debug(isReadOnly(descriptor) ?
							"Attribute [{}.{}] is read-only, therefore not settable." :
							"Attribute [{}.{}] cannot be changed.",
					attribute.getTypeDescriptor().getItemCode(),
					attribute.getAttributeName());
		}
	}
}
