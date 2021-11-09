/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;

/**
 * A JUnit {@link org.junit.Rule} for tracking items created in the course of an integration test execution.
 * The rule must be created with a set of item types to keep track of. Before the tests run,
 * this rule will capture pre-existing items for each of the types specified; after the tests it
 * cleans all items that were created by the test, ie. they are not pre-existent to the test.
 */
public class ItemTracker extends ExternalResource
{
	private final Map<Class<? extends ItemModel>, TypeItemsTracker<? extends ItemModel>> trackedItemTypes;
	private final List<TypeItemsTracker<? extends ItemModel>> trackedTypesInOrder;

	private ItemTracker(final List<Class<? extends ItemModel>> types)
	{
		trackedTypesInOrder = types.stream()
		                           .map(TypeItemsTracker::new)
		                           .collect(Collectors.toList());
		trackedItemTypes = trackedTypesInOrder.stream()
		                                      .collect(Collectors.toMap(k -> k.trackedType, v -> v));
	}

	/**
	 * Creates this rule by specifying types of the items to be tracked. Be mindful of the number of types included
	 * because this rules keeps all items pre-existing for each of the types in memory.
	 *
	 * @param types types, for which items should be tracked by this rule. Sometimes, items of one type cannot
	 *              be deleted while items of other types exist. In this case make sure the dependent items are cleaned last, e.g.
	 *              {@code "Product", "CatalogVersion", "Catalog"}
	 * @return a rule initialized with the item types.
	 */
	@SafeVarargs
	public static ItemTracker track(final Class<? extends ItemModel>... types)
	{
		return new ItemTracker(List.of(types));
	}

	@Override
	protected void before()
	{
		trackedTypesInOrder.forEach(TypeItemsTracker::captureExistingItems);
	}

	@Override
	protected void after()
	{
		trackedTypesInOrder.forEach(TypeItemsTracker::clean);
	}

	/**
	 * Checks whether item specified by the condition was created during the test execution.
	 *
	 * @param type      type of the item to check.
	 * @param condition a condition the created item should match, e.g. a unique field match, etc.
	 * @param <T>       {@link ItemModel} subclass.
	 * @return {@code true}, if there is at least one item that did not exist before the test but exists at the time of this
	 * method call; {@code false} otherwise.
	 * @throws IllegalArgumentException if the specified type is not tracked
	 */
	public <T extends ItemModel> boolean isCreated(final Class<T> type, final Predicate<T> condition)
	{
		return getTracker(type)
				.map(t -> t.createdItemExists(condition))
				.orElseThrow(() -> new IllegalArgumentException(type + " is not tracked"));
	}

	/**
	 * Retrieves items created by the test
	 *
	 * @param type type of the items to retrieve
	 * @return a list of all items of the specified time created by the test or an empty list, if no items created.
	 * @throws IllegalArgumentException if the specified type is not tracked
	 */
	public <T extends ItemModel> List<T> getCreatedItems(final Class<T> type)
	{
		return getTracker(type)
				.map(TypeItemsTracker::findCreatedItems)
				.orElseThrow(() -> new IllegalArgumentException(type + " is not tracked"));
	}

	private <T extends ItemModel> Optional<TypeItemsTracker<T>> getTracker(final Class<T> type)
	{
		return Optional.ofNullable((TypeItemsTracker<T>) trackedItemTypes.get(type));
	}

	private static class TypeItemsTracker<T extends ItemModel>
	{
		private static final Logger LOG = Log.getLogger(ItemTracker.class);

		private final Class<T> trackedType;
		private final Set<PK> preExistingPKs;

		private TypeItemsTracker(final Class<T> type)
		{
			trackedType = type;
			preExistingPKs = new HashSet<>();
		}

		public void captureExistingItems()
		{
			preExistingPKs.clear();
			final var items = IntegrationTestUtil.findAll(trackedType).stream()
			                                     .map(ItemModel::getPk)
			                                     .collect(Collectors.toSet());
			LOG.info("{}: found {} existing item(s)", trackedType.getSimpleName(), items.size());
			preExistingPKs.addAll(items);
		}

		public void clean()
		{
			final var created = findCreatedItems();
			LOG.info("{}: deleting {} created item(s)", trackedType.getSimpleName(), created.size());
			created.forEach(IntegrationTestUtil::remove);
		}

		public boolean createdItemExists(final Predicate<T> condition)
		{
			return findCreatedItems().stream().anyMatch(condition);
		}

		private List<T> findCreatedItems()
		{
			return IntegrationTestUtil.findAll(trackedType).stream()
			                          .filter(this::isCreated)
			                          .collect(Collectors.toList());
		}

		private boolean isCreated(final T item)
		{
			return !preExistingPKs.contains(item.getPk());
		}
	}
}