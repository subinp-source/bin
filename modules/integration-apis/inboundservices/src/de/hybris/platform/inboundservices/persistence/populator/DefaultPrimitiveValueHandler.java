/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.inboundservices.persistence.PrimitiveValueHandler;
import de.hybris.platform.integrationservices.exception.InvalidAttributeValueException;
import de.hybris.platform.integrationservices.item.LocalizedValue;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.service.IntegrationLocalizationService;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.common.collect.Lists;

public class DefaultPrimitiveValueHandler implements PrimitiveValueHandler
{
	private IntegrationLocalizationService localizationService;
	private final ValueHandler defaultHandler = new DefaultValueHandler();
	private final List<ValueHandler> handlers = Lists.newArrayList(
			new CalendarValueHandler(), new LocalizedValueHandler(),
			new CharacterValueHandler(), new BigIntegerValueHandler());

	@Override
	public Object convert(final TypeAttributeDescriptor attribute, final Object value)
	{
		final ValueHandler handler = handlers.stream()
		                                     .filter(h -> h.isApplicable(attribute, value))
		                                     .findFirst()
		                                     .orElse(defaultHandler);
		return handler.handleAttribute(attribute, value);
	}

	/**
	 * A handler for converting a particular value type to a format accepted the by model service.
	 */
	private interface ValueHandler
	{
		boolean isApplicable(TypeAttributeDescriptor attribute, Object value);

		Object handleAttribute(TypeAttributeDescriptor attribute, Object value);
	}

	private static class CalendarValueHandler implements ValueHandler
	{
		private final Calendar endOf9999 = new Calendar.Builder()
				.setDate(9999, Calendar.DECEMBER, 31)
				.setTimeOfDay(23, 59, 59)
				.build();

		@Override
		public boolean isApplicable(final TypeAttributeDescriptor attribute, final Object value)
		{
			return value instanceof Calendar;
		}

		@Override
		public Object handleAttribute(final TypeAttributeDescriptor attribute, final Object value)
		{
			return adjustDateIfGreaterThanYear9999(value);
		}

		private Date adjustDateIfGreaterThanYear9999(final Object value)
		{
			final Calendar payloadCalendar = (Calendar) value;
			return payloadCalendar.compareTo(endOf9999) > 0 ?
					endOf9999.getTime() :
					payloadCalendar.getTime();
		}
	}

	private class LocalizedValueHandler implements ValueHandler
	{
		@Override
		public boolean isApplicable(final TypeAttributeDescriptor attribute, final Object value)
		{
			return attribute.isLocalized() && value instanceof LocalizedValue;
		}

		@Override
		public Object handleAttribute(final TypeAttributeDescriptor attribute, final Object value)
		{
			final var payloadValue = (LocalizedValue) value;
			final Map<Locale, Object> attrValue = new HashMap<>();
			payloadValue.forEach((k, v) -> {
				getLocalizationService().validateLocale(k);
				attrValue.put(k, convert(attribute, v));
			});
			return attrValue;
		}
	}

	private static class CharacterValueHandler implements ValueHandler
	{
		@Override
		public boolean isApplicable(final TypeAttributeDescriptor attribute, final Object value)
		{
			return isCharacterType(attribute);
		}

		private boolean isCharacterType(final TypeAttributeDescriptor attribute)
		{
			return Character.class.getName().equals(attribute.getAttributeType().getTypeCode());
		}

		@Override
		public Object handleAttribute(final TypeAttributeDescriptor attribute, final Object value)
		{
			return asCharacter(attribute, value);
		}

		private Character asCharacter(final TypeAttributeDescriptor attribute, final Object value)
		{
			if (value instanceof Character || value == null)
			{
				return (Character) value;
			}
			final String str = String.valueOf(value);
			if (str.length() == 1)
			{
				return str.charAt(0);
			}
			throw new InvalidAttributeValueException(value, attribute);
		}
	}

	private static class BigIntegerValueHandler implements ValueHandler
	{
		@Override
		public boolean isApplicable(final TypeAttributeDescriptor attribute, final Object value)
		{
			return value != null && BigInteger.class.getName().equals(attribute.getAttributeType().getTypeCode());
		}

		@Override
		public Object handleAttribute(final TypeAttributeDescriptor attribute, final Object value)
		{
			return new BigInteger(String.valueOf(value));
		}
	}

	private static class DefaultValueHandler implements ValueHandler
	{
		@Override
		public boolean isApplicable(final TypeAttributeDescriptor attribute, final Object value)
		{
			return true;
		}

		@Override
		public Object handleAttribute(final TypeAttributeDescriptor attribute, final Object value)
		{
			return value;
		}
	}

	protected IntegrationLocalizationService getLocalizationService()
	{
		return localizationService;
	}

	public void setLocalizationService(final IntegrationLocalizationService localizationService)
	{
		this.localizationService = localizationService;
	}
}
