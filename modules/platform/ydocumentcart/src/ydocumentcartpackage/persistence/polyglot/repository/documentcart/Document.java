/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.util.RelationsInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity.EntityBuilder;


public class Document
{
	private final Identity rootId;
	private final long version;
	private final Map<Identity, Entity> entities = new HashMap<>();
	private final DocumentRelationsHandler documentRelationsHandler;

	public Document(final Identity rootId)
	{
		this(rootId, 0L);
	}

	public Document(final Identity rootId, final long version)
	{
		this(rootId, version, new DocumentRelationsHandler());
	}

	Document(final Identity rootId, final long version, final DocumentRelationsHandler drh)
	{
		Objects.requireNonNull(drh, "documentRelationsHandler mustn't be null.");
		Objects.requireNonNull(rootId, "rootId mustn't be null.");
		this.rootId = rootId;
		this.version = version;
		this.documentRelationsHandler = drh;
	}

	public Identity getRootId()
	{
		return rootId;
	}

	public long getVersion()
	{
		return version;
	}

	public boolean isNew()
	{
		return version == 0;
	}

	public Entity getRootEntity()
	{
		return getEntity(getRootId()).orElseThrow(() -> new IllegalStateException("Root entity doesn't exist."));
	}

	public boolean containsEntity(final Identity id)
	{
		return entities.containsKey(id);
	}

	public Optional<Entity> getEntity(final Identity id)
	{
		return Optional.ofNullable(entities.get(id));
	}

	public void addEntity(final Entity entity)
	{
		entities.put(entity.getId(), entity);
	}

	public Set<Identity> getEntityIds()
	{
		return Set.copyOf(entities.keySet());
	}

	public Stream<Entity> allEntities()
	{
		return entities.values().stream();
	}

	public boolean remove(final Entity entity)
	{
		entities.remove(entity.getId());
		return rootId.equals(entity.getId());
	}

	public Collection<Reference> getRelatedItems(final RelationsInfo relationsInfo)
	{
		return documentRelationsHandler.getRelatedItems(relationsInfo, entities);
	}

	public void apply(final EntityCreation creation)
	{
		final EntityBuilder builder = newEntityBuilder();

		builder.withId(creation.getId());
		creation.forEveryAttribute(builder::withAttribute);

		addEntity(builder.build());
	}

	public void apply(final EntityModification modification)
	{
		final Entity existingEntity = entities.get(modification.getId());

		modification.forEveryChange(existingEntity::set);
	}

	public EntityBuilder newEntityBuilder()
	{
		return Entity.builder(this);
	}


}

