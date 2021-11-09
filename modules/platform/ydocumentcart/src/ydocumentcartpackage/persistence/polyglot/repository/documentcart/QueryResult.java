/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public class QueryResult
{
	private final List<Document> documents;

	private QueryResult(final List<Document> documents)
	{
		this.documents = Objects.requireNonNull(documents, "documents mustn't be null.");
	}

	public static QueryResult from(final List<Document> documents)
	{
		return new QueryResult(documents);
	}

	public static QueryResult from(final Document document)
	{
		return from(List.of(document));
	}

	public static QueryResult fromNullable(final Document document)
	{
		if (document == null)
		{
			return empty();
		}
		return from(document);
	}

	public static QueryResult empty()
	{
		return from(List.of());
	}

	public Optional<Document> single()
	{
		if (documents.isEmpty())
		{
			return Optional.empty();
		}
		if (documents.size() == 1)
		{
			return Optional.of(documents.get(0));
		}

		throw new IllegalStateException("Result contains more than one document but at most one is expected.");
	}

	public Stream<Document> stream()
	{
		return documents.stream();
	}

	public QueryResult requireAtMostOneDocument()
	{
		single();
		return this;
	}
}
