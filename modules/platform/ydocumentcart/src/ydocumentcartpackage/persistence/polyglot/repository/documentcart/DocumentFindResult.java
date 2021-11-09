/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.List;
import java.util.stream.Stream;


public class DocumentFindResult implements FindResult
{
	private final List<Entity> allEntities;
	private final int skip;
	private final int limit;

	private DocumentFindResult(final List<Entity> allEntities, final int skip, final int limit)
	{
		this.allEntities = allEntities;
		this.skip = skip;
		this.limit = limit;
	}

	public static DocumentFindResult from(final List<Entity> allEntities, final Criteria criteria)
	{
		final int requestedStart = criteria.getStart();
		final int requestedCount = criteria.getCount();

		final int skip = requestedStart < 0 ? 0 : requestedStart;
		final int limit = requestedCount < 0 ? allEntities.size() : requestedCount;

		return new DocumentFindResult(allEntities, skip, limit);
	}

	@Override
	public int getCount()
	{
		return Math.min(limit, Math.max(0, getTotalCount() - skip));
	}

	@Override
	public int getTotalCount()
	{
		return allEntities.size();
	}

	@Override
	public Stream<ItemStateView> getResult()
	{
		return allEntities.stream().skip(skip).limit(limit).map(ItemStateView.class::cast);
	}
}