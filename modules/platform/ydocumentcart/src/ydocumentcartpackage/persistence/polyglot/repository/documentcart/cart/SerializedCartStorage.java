/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart;

import de.hybris.platform.persistence.polyglot.PolyglotPersistence.CoreAttributes;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.DocumentConcurrentModificationException;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryResult;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.EntityCondition;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.serializer.Serializer;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.storage.BaseStorage;


public class SerializedCartStorage extends BaseStorage
{
	private static final Logger LOG = LoggerFactory.getLogger(SerializedCartStorage.class);

	private final Serializer serializer;

	private final ConcurrentHashMap<Identity, SerializedCart> carts = new ConcurrentHashMap<>();

	public SerializedCartStorage(final Serializer serializer)
	{
		this.serializer = Objects.requireNonNull(serializer, "serializer mustn't be null.");
	}

	@Override
	protected Document instantiateNewDocument(final Identity rootId)
	{
		return new Document(rootId);
	}

	@Override
	public void save(final Document document)
	{
		final SerializedCart storedCart = carts.get(document.getRootId());

		if (document.isNew() && storedCart != null)
		{
			throw DocumentConcurrentModificationException.documentAlreadyExist(document);
		}
		if (!document.isNew() && storedCart == null)
		{
			throw DocumentConcurrentModificationException.documentHasBeenRemoved(document);
		}
		if (storedCart != null && storedCart.version != document.getVersion())
		{
			throw DocumentConcurrentModificationException.documentHasBeenModified(document);
		}

		final SerializedCart cartToStore = new SerializedCart(document);

		carts.compute(document.getRootId(), (id, currentCart) -> {
			if (currentCart != storedCart)
			{
				throw DocumentConcurrentModificationException.documentHasBeenModified(document);
			}
			return cartToStore;
		});
	}

	@Override
	public void remove(final Document document)
	{
		final SerializedCart storedCart = carts.get(document.getRootId());
		if (storedCart == null)
		{
			throw DocumentConcurrentModificationException.documentHasBeenRemoved(document);
		}
		if (storedCart.version != document.getVersion())
		{
			throw DocumentConcurrentModificationException.documentHasBeenModified(document);
		}
		if (!carts.remove(storedCart.cartId, storedCart))
		{
			throw DocumentConcurrentModificationException.documentHasBeenModified(document);
		}
	}

	@Override
	protected QueryResult findByRootId(final Identity id)
	{
		return QueryResult.fromNullable(Optional.ofNullable(carts.get(id)).map(SerializedCart::toDocument).orElse(null));
	}

	@Override
	protected QueryResult findByRootAttributes(final EntityCondition condition)
	{
		final Predicate<ItemStateView> predicate = condition.getPredicate();

		return QueryResult.from(carts.values().stream().map(ItemStateAdapter::new).filter(predicate)
		                             .map(ItemStateAdapter::toDocument).collect(Collectors.toList()));
	}

	@Override
	protected QueryResult findByEntityId(final Identity id)
	{
		final SerializedCart cart = carts.get(id);
		if (cart != null)
		{
			return QueryResult.from(cart.toDocument());
		}
		return QueryResult.fromNullable(
				carts.values().stream().filter(c -> c.contains(id)).map(SerializedCart::toDocument).findFirst().orElse(null));
	}

	@Override
	protected QueryResult findByEntityAttributes(final EntityCondition condition)
	{
		LOG.warn("Searching by entity attributes '{}'.", condition);
		final Predicate<ItemStateView> predicate = condition.getPredicate();

		return QueryResult.from(carts.values().stream().map(SerializedCart::toDocument).flatMap(Document::allEntities)
		                             .filter(predicate).map(Entity::getDocument).distinct().collect(Collectors.toList()));
	}

	private static class ItemStateAdapter implements ItemState
	{
		private final SerializedCart cart;
		private Entity deserializedRootEntity;

		public ItemStateAdapter(final SerializedCart cart)
		{
			this.cart = cart;
		}

		public Document toDocument()
		{
			if (deserializedRootEntity != null)
			{
				return deserializedRootEntity.getDocument();
			}
			return cart.toDocument();
		}

		@Override
		public <T> T get(final Key key)
		{
			if (CoreAttributes.isPk(key))
			{
				return (T) cart.cartId;
			}
			if (CoreAttributes.isVersion(key))
			{
				return (T) Long.valueOf(cart.version);
			}
			if (CoreAttributes.isType(key))
			{
				return (T) cart.typeRef;
			}
			if (CartAttributes.isCode(key))
			{
				return (T) cart.code;
			}
			if (CartAttributes.isGuid(key))
			{
				return (T) cart.guid;
			}
			if (deserializedRootEntity == null)
			{
				LOG.warn("Deserializing root entity because of '{}' attribute.", key);
				deserializedRootEntity = cart.toDocument().getRootEntity();
			}
			return deserializedRootEntity.get(key);
		}

		@Override
		public ChangeSet beginModification()
		{
			throw new UnsupportedOperationException();
		}
	}

	private class SerializedCart
	{
		private final String cartJSON;
		private final Identity cartId;
		private final long version;
		private final Set<Identity> embeddedIds;
		private final String code;
		private final String guid;
		private final Reference typeRef;

		public SerializedCart(final Document document)
		{
			this.version = document.getVersion() + 1;
			this.cartJSON = serializer.serializeWithOverriddenVersion(document, version);
			this.embeddedIds = document.getEntityIds();
			this.cartId = document.getRootId();

			final Entity cartEntity = document.getEntity(document.getRootId()).get();

			this.guid = cartEntity.get(CartAttributes.guid());
			this.code = cartEntity.get(CartAttributes.code());
			this.typeRef = cartEntity.get(CoreAttributes.type());
		}

		public boolean contains(final Identity id)
		{
			return embeddedIds.contains(id);
		}

		public Document toDocument()
		{
			return serializer.deserialize(cartJSON);
		}
	}

}
