/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Document;

import java.util.Objects;
import java.util.Optional;


public class QueryWithKnownDocument extends QueryByRootId
{
	private final Document document;

	public QueryWithKnownDocument(final Document document)
	{
		super(Objects.requireNonNull(document, "document mustn't be null.").getRootId());
		this.document = document;
	}

	@Override
	public Optional<Document> getKnownDocument()
	{
		return Optional.of(document);
	}
}
