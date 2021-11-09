/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;

import java.util.Optional;


public interface TypeSystemInfo
{
	static Optional<Identity> toIdentity(final Object obj)
	{
		Identity id;
		if (obj instanceof Identity)
		{
			id = (Identity) obj;
		}
		else if (obj instanceof Reference)
		{
			id = ((Reference) obj).getIdentity();
		}
		else
		{
			return Optional.empty();
		}
		return id.isKnown() ? Optional.of(id) : Optional.empty();
	}

	boolean isDocumentRootId(Identity id);

	SingleAttributeKey getParentReferenceAttribute(Reference itemTypeReference);

	TypedCriteria getTypedCriteria(Criteria criteria);

}
