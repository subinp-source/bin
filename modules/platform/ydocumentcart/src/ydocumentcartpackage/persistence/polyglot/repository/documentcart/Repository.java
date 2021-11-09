/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.HandlingRelatedItemsFeature;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.criteria.ItemStateComparatorCreator;
import de.hybris.platform.persistence.polyglot.search.criteria.MatchingPredicateBuilder;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Repository implements ItemStateRepository
{
	private final QueryFactory queryFactory;
	private final Storage storage;

	public Repository(final QueryFactory queryFactory, final Storage storage)
	{
		this.queryFactory = Objects.requireNonNull(queryFactory);
		this.storage = Objects.requireNonNull(storage);
	}

	@Override
	public Entity get(final Identity id)
	{
		final Query query = queryFactory.getQuery(id);

		return storage.find(query).single().flatMap(document -> document.getEntity(id)).orElse(null);
	}

	@Override
	public EntityCreation beginCreation(final Identity id)
	{
		return new EntityCreation(id);
	}

	@Override
	public void store(final ChangeSet changeSet)
	{
		Objects.requireNonNull(changeSet, "changeSet mustn't be null");
		final Optional<EntityModification> possibleModification = EntityModification.from(changeSet);

		if (possibleModification.isPresent())
		{
			storeModification(possibleModification.get());
			return;
		}

		final Optional<EntityCreation> possibleCreation = EntityCreation.from(changeSet);
		if (possibleCreation.isPresent())
		{
			storeCreation(possibleCreation.get());
			return;
		}

		throw new UnsupportedOperationException("Unsupported changeSet '" + changeSet + "'.");
	}

	@Override
	public void remove(final ItemState itemState)
	{
		Objects.requireNonNull(itemState, "itemState mustn't be null");
		final Entity entity = Entity.from(itemState)
		                            .orElseThrow(() -> new UnsupportedOperationException(
				                            "Unsupported itemState '" + itemState + "'."));

		final Query query = queryFactory.getQuery(entity);

		final Document document = storage.find(query).single()
		                                 .orElseThrow(
				                                 () -> DocumentConcurrentModificationException.missingDocumentForRemoval(entity));

		if (document.remove(entity))
		{
			storage.remove(document);
		}
		else
		{
			storage.save(document);
		}
	}

	@Override
	public FindResult find(final Criteria criteria)
	{
		final Query query = queryFactory.getQuery(criteria);

		final Predicate<ItemStateView> matcher = new MatchingPredicateBuilder(criteria).getPredicate();
		final Comparator<ItemStateView> comparator = ItemStateComparatorCreator.getItemStateComparator(criteria);

		final List<Entity> result = storage
				.find(query)
				.stream()
				.flatMap(Document::allEntities)
				.filter(matcher)
				.sorted(comparator)
				.collect(Collectors.toUnmodifiableList());

		return DocumentFindResult.from(result, criteria);

	}

	protected void storeCreation(final EntityCreation creation)
	{
		final Query query = queryFactory.getQuery(creation);

		final Document document = storage.find(query).single()
		                                 .orElseThrow(() -> DocumentConcurrentModificationException.missingDocumentForCreation(
				                                 creation));

		document.apply(creation);

		storage.save(document);
	}

	protected void storeModification(final EntityModification modification)
	{
		final Query query = queryFactory.getQuery(modification);

		final Document document = storage.find(query).single()
		                                 .orElseThrow(
				                                 () -> DocumentConcurrentModificationException.missingDocumentForModification(
						                                 modification));

		document.apply(modification);

		storage.save(document);
	}

	@Override
	public boolean isSupported(final PolyglotFeature feature)
	{
		if (feature instanceof HandlingRelatedItemsFeature)
		{
			return isSupported((HandlingRelatedItemsFeature) feature);
		}
		return ItemStateRepository.super.isSupported(feature);
	}

	private boolean isSupported(final HandlingRelatedItemsFeature feature)
	{
		return !feature.isWritable();
	}
}
