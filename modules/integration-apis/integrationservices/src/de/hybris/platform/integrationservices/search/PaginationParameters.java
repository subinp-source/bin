/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import java.util.Objects;

/**
 * Specifies page parameters for retrieving items stored in the platform. It can be used for reading items in
 * batches (pages), where {@link #getPageSize()} specifies items per page and {@link #getPageStart()} specifies how many items should
 * be skipped and not included in the page result.
 */
public class PaginationParameters
{
	private final int pageStart;
	private final int pageSize;

	private PaginationParameters(final int start, final int size)
	{
		pageStart = Math.max(start, 0);
		pageSize = Math.max(size, 0);
	}

	/**
	 * Creates new instance of {@code PaginationParameters} with specified characteristics.
	 *
	 * @param start specifies position of the first item to be returned in the search result within the search result set.
	 *              For example, if there are 200 items in the persistent storage and this parameter is equal to 100, then first 100
	 *              items will be skipped and the result will contain items beginning from the 100th item.
	 *              <p>Any negative or zero value means the result should not skip items and that it will contain items beginning
	 *              from the very first item in the result set.</p>
	 * @param size  number of items to include in the result set. Any negative number is treated as {@code 0}.
	 *              <p>This parameter allows to limit number of items returned, so that the application would not attempt to load
	 *              too many items matching the search criteria into the memory and result in the {@link OutOfMemoryError}.</p>
	 * @return a value object with the specified parameters.
	 */
	public static PaginationParameters create(final int start, final int size)
	{
		return new PaginationParameters(start, size);
	}

	/**
	 * Retrieves number of items to be skipped in the result set.
	 *
	 * @return requested items will be read from the persistent storage, then, if offset is greater than {@code 0}, the {@code offset} number
	 * of items at the beginning of the result set will be removed.
	 * @see #create(int, int)
	 */
	public int getPageStart()
	{
		return pageStart;
	}

	/**
	 * Retrieves number of items to return in the search result.
	 *
	 * @return number of items to be retrieved. The search result may contain fewer number of items or maybe even not items, but it
	 * should not contain more items then the page size specified.
	 * @see #create(int, int)
	 */
	public int getPageSize()
	{
		return pageSize;
	}

	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o != null && getClass() == o.getClass())
		{
			final PaginationParameters that = (PaginationParameters) o;
			return pageStart == that.pageStart && pageSize == that.pageSize;
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(pageStart, pageSize);
	}

	@Override
	public String toString()
	{
		return "PaginationParameters{" +
				"offset=" + pageStart +
				", pageSize=" + pageSize +
				'}';
	}
}
