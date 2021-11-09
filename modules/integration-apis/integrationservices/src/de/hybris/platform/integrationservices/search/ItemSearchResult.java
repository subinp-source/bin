/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Result of searching items in the platform. This result is immutable except it does not guarantee immutability
 * of the items returned by {@link #getItems()} method.
 */
public class ItemSearchResult<T>
{
	private final List<T> items;
	private final Integer totalCount;

	private ItemSearchResult(final List<T> items, final Integer cnt)
	{
		this.items = Collections.unmodifiableList(items);
		this.totalCount = cnt;
	}

	/**
	 * Creates new item search result instance from the specified Flexible Search result.
	 *
	 * @param result flexible search result.
	 * @param <T>    type of the items searched.
	 * @return new item search result instance.
	 */
	public static <T> ItemSearchResult<T> createFrom(final SearchResult<T> result)
	{
		return createWith(result.getResult(), result.getTotalCount());
	}

	/**
	 * Creates new item search result instance with total number of matching items existing in the platform.
	 *
	 * @param items      items found. Usually in this scenario this is a subset of all items existing in the platform, which match the
	 *                   {@link ItemSearchRequest}.
	 * @param totalCount total number of items persisted in the platform, which match the criteria specified by the {@link ItemSearchRequest}
	 * @param <T>        type of the items searched.
	 * @return new item search result instance.
	 * @see #getTotalCount()}
	 */
	public static <T> ItemSearchResult<T> createWith(final List<T> items, final int totalCount)
	{
		return new ItemSearchResult<>(items, totalCount);
	}

	/**
	 * Creates new item search result instance when total count of matching items is not needed.
	 *
	 * @param items items found
	 * @param <T>   type of the items searched
	 * @return new item search result instance.
	 */
	public static <T> ItemSearchResult<T> createWith(final List<T> items)
	{
		return new ItemSearchResult<>(items, null);
	}

	/**
	 * Retrieves items existing in the platform and matching the request conditions.
	 *
	 * @return all items matching the {@link ItemSearchRequest} conditions or a subset of all items in case of a paged request.
	 * When no items match the conditions, an empty list is returned.
	 */
	public List<T> getItems()
	{
		return items;
	}

	/**
	 * Retrieves total number of items existing in the platform and matching the {@link ItemSearchRequest} conditions.
	 *
	 * @return number of items matching the conditions in the platform. This number may be greater than the number of items returned
	 * by {@link #getItems()} method when {@link PaginationParameters} were used in the request. If count was not requested, then
	 * {@code Optional.empty()} is returned.
	 */
	public Optional<Integer> getTotalCount()
	{
		return Optional.ofNullable(totalCount);
	}

	/**
	 * Transforms this result into a result containing different kind of items.
	 *
	 * @param f   a function to be applied to each item contained in this result
	 * @param <R> type of the entries in the transformed result
	 * @return a result containing entries, which were transformed from the original result by applying the specified function.
	 */
	public <R> ItemSearchResult<R> map(final Function<T, R> f)
	{
		final List<R> mapped = getItems().stream()
		                                 .map(f)
		                                 .collect(Collectors.toList());
		return ItemSearchResult.createWith(mapped, totalCount);
	}
}
