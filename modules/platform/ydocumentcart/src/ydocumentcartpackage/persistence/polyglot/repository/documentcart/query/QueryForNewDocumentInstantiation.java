/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.model.Identity;

import java.util.Objects;


public class QueryForNewDocumentInstantiation extends QueryByRootId
{

	public QueryForNewDocumentInstantiation(final Identity rootId)
	{
		super(Objects.requireNonNull(rootId, "rootId mustn't be null."));
	}

	@Override
	public boolean isDocumentInstantiation()
	{
		return true;
	}

}
