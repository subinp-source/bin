/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event;

import java.util.Arrays;

/**
 * Values for {@link ItemSavedEvent} event type.
 */
public enum ItemSavedEventType
{
	UPDATE(1),
	CREATE(4);

	private final int type;

	ItemSavedEventType(final int type)
	{
		this.type = type;
	}

	/**
	 * @return The integer value corresponding to the event type
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * This converts {@link de.hybris.platform.tx.AfterSaveEvent} event type integer to an enum value to be used by {@link ItemSavedEvent}
	 *
	 * @param type An integer that represents the event type from {@link de.hybris.platform.tx.AfterSaveEvent}
	 * @return Value of the converted event type
	 */
	public static ItemSavedEventType fromEventTypeCode(final int type)
	{
		return Arrays.stream(values())
		             .filter(ev -> ev.getType() == type)
		             .findFirst()
		             .orElseThrow(() -> new InvalidEventTypeException(type));
	}
}
