/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.event;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.tx.AfterSaveEvent;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * An AbstractEvent that wraps the {@link AfterSaveEvent}
 */
public class ItemSavedEvent extends AbstractEvent
{

	private final ItemSavedEventType type;

	private final PK pk;

	/**
	 * Instantiates an ItemSavedEvent
	 *
	 * @param event The {@link AfterSaveEvent} that is wrapped.
	 */
	public ItemSavedEvent(final AfterSaveEvent event)
	{
		super(event.getPk());
		this.pk = event.getPk();
		this.type = ItemSavedEventType.fromEventTypeCode(event.getType());
	}

	/**
	 * Retrieves the type of the event.
	 * @return {@link ItemSavedEventType}
	 */
	public ItemSavedEventType getType()
	{
		return type;
	}

	/**
	 * Retrieves the saved item pk
	 * @return pk
	 */
	public PK getSavedItemPk()
	{
		return pk;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final ItemSavedEvent that = (ItemSavedEvent) o;
		return new EqualsBuilder()
				.append(getSavedItemPk(), that.getSavedItemPk())
				.isEquals();
	}

	@Override
	public String toString()
	{
		return "ItemSavedEvent{pk=" + this.getSavedItemPk() + ", type=" + this.getType() + '}';
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(getSavedItemPk());
	}
}
