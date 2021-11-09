/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence.CoreAttributes;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;


public class EntityCreation implements ChangeSet
{
	private final Identity id;
	private final Map<SingleAttributeKey, Object> attributes = new HashMap<>();

	public EntityCreation(final Identity id)
	{
		this.id = Objects.requireNonNull(id, "id mustn't be null");
	}

	public static Optional<EntityCreation> from(final ChangeSet changeSet)
	{
		return Optional.of(changeSet).filter(EntityCreation.class::isInstance).map(EntityCreation.class::cast);
	}

	public Identity getId()
	{
		return id;
	}

	public Reference getTypeReference()
	{
		return (Reference) attributes.get(CoreAttributes.type());
	}

	@Override
	public void set(final SingleAttributeKey key, final Object value)
	{
		Objects.requireNonNull(key, "key mustn't be null.");

		if (CoreAttributes.isPk(key) || CoreAttributes.isVersion(key) || value == null)
		{
			return;
		}

		attributes.put(key, value);
	}

	public Object get(final Key key)
	{
		if (CoreAttributes.isPk(key))
		{
			return id;
		}

		return attributes.get(key);
	}

	public void forEveryAttribute(final BiConsumer<SingleAttributeKey, Object> consumer)
	{
		Objects.requireNonNull(consumer, "consumer mustn't be null");
		attributes.forEach(consumer);
	}
}
