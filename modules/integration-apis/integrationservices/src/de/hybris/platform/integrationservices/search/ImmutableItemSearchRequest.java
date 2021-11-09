/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * Immutable value object for representing a search request
 */
public class ImmutableItemSearchRequest implements ItemSearchRequest
{
	@NotNull
	private final TypeDescriptor typeDescriptor;
	private IntegrationItem integrationItem;
	private PaginationParameters paginationParameters;
	private WhereClauseConditions filter;
	private List<OrderExpression> orderBy;
	private Locale locale;
	private boolean totalCount;
	private boolean countOnly;

	private ImmutableItemSearchRequest(final TypeDescriptor type)
	{
		Preconditions.checkArgument(type != null, "TypeDescriptor must be specified for a ImmutableItemSearchRequest");
		typeDescriptor = type;
	}

	/**
	 * Instantiates a new builder for creating instances of an {@link ImmutableItemSearchRequest}
	 *
	 * @return the {@link Builder}
	 */
	public static Builder itemSearchRequestBuilder()
	{
		return new Builder();
	}

	/**
	 * Instantiates a new builder for creating instances of an {@link ImmutableItemSearchRequest} from
	 * an existing {@link ItemSearchRequest}
	 * @param request An existing request to build from
	 * @return the {@link Builder}
	 */
	public static Builder itemSearchRequestBuilder(final ItemSearchRequest request)
	{
		Preconditions.checkArgument(request != null, "ItemSearchRequest cannot be null");
		return new Builder(request);
	}

	@Override
	public Optional<PaginationParameters> getPaginationParameters()
	{
		return Optional.ofNullable(paginationParameters);
	}

	@Override
	public @NotNull TypeDescriptor getTypeDescriptor()
	{
		return typeDescriptor;
	}

	@Override
	public Optional<IntegrationItem> getRequestedItem()
	{
		return Optional.ofNullable(integrationItem);
	}

	@Override
	public WhereClauseConditions getFilter()
	{
		return filter;
	}

	@Override
	public List<OrderExpression> getOrderBy()
	{
		return orderBy != null ? Collections.unmodifiableList(orderBy) : Collections.emptyList();
	}

	@Override
	public Locale getAcceptLocale()
	{
		return locale;
	}

	@Override
	public boolean includeTotalCount()
	{
		return totalCount;
	}

	@Override
	public boolean isCountOnly()
	{
		return countOnly;
	}

	/**
	 * A builder for {@link ImmutableItemSearchRequest}
	 */
	public static class Builder
	{
		private TypeDescriptor typeDescriptor;
		private IntegrationItem integrationItem;
		private PaginationParameters pageParameters;
		private WhereClauseConditions whereClause;
		private List<OrderExpression> orderBy;
		private Locale locale;
		private boolean totalCount;
		private boolean countOnly;

		public Builder()
		{
		}

		public Builder(ItemSearchRequest request)
		{
			request.getRequestedItem().ifPresent(this::integrationItem);
			itemType(request.getTypeDescriptor());
			filter(request.getFilter());
			orderBy(request.getOrderBy());
			locale(request.getAcceptLocale());
			countOnly(request.isCountOnly());
			totalCount(request.includeTotalCount());
			request.getPaginationParameters().ifPresent(this::pageParameters);
		}

		private Builder integrationItem(final IntegrationItem item)
		{
			final var type = item != null ? item.getItemType() : null;
			integrationItem = item;
			return itemType(type);
		}

		public Builder withIntegrationItem(final IntegrationItem item)
		{
			return integrationItem(item);
		}

		private Builder itemType(final TypeDescriptor type)
		{
			typeDescriptor = type;
			return this;
		}

		public Builder withItemType(final TypeDescriptor type)
		{
			return itemType(type);
		}

		private Builder filter(final WhereClauseConditions filter)
		{
			whereClause = filter;
			return this;
		}

		public Builder withFilter(final WhereClauseConditions filter)
		{
			return filter(filter);
		}

		private Builder orderBy(final List<OrderExpression> orderByExpressions)
		{
			orderBy = orderByExpressions;
			return this;
		}

		public Builder withOrderBy(final List<OrderExpression> orderByExpressions)
		{
			return orderBy(orderByExpressions);
		}

		private Builder locale(final Locale locale)
		{
			this.locale = locale;
			return this;
		}

		public Builder withLocale(final Locale locale)
		{
			return locale(locale);
		}

		private Builder countOnly(final boolean value)
		{
			countOnly = value;
			return totalCount(value);
		}

		public Builder withCountOnly()
		{
			return countOnly(true);
		}

		public Builder withNoCountOnly()
		{
			return countOnly(false);
		}

		private Builder totalCount(final boolean value)
		{
			totalCount = value;
			return this;
		}
		
		public Builder withTotalCount()
		{
			return totalCount(true);
		}

		public Builder withNoTotalCount() {
			return totalCount(false);
		}

		private Builder pageParameters(final PaginationParameters params)
		{
			pageParameters = params;
			return this;
		}

		public Builder withPageParameters(final PaginationParameters params)
		{
			return pageParameters(params);
		}

		public ImmutableItemSearchRequest build()
		{
			final var request = new ImmutableItemSearchRequest(typeDescriptor);
			request.integrationItem = integrationItem;
			request.paginationParameters = pageParameters;
			request.filter = whereClause;
			request.orderBy = orderBy;
			request.totalCount = totalCount;
			request.countOnly = countOnly;
			request.locale = locale;
			return request;
		}
	}
}
