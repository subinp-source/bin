/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ydocumentcartpackage.persistence.polyglot.repository.documentcart.Entity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.EntityCreation;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.EntityModification;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.QueryFactory;


public class BaseQueryFactory implements QueryFactory
{
	private final TypeSystemInfo typeSystemInfo;

	public BaseQueryFactory(final TypeSystemInfo typeSystemInfo)
	{
		this.typeSystemInfo = Objects.requireNonNull(typeSystemInfo, "typeSystemInfo mustn't be null.");
	}

	@Override
	public BaseQuery getQuery(final Identity id)
	{
		if (typeSystemInfo.isDocumentRootId(id))
		{
			return new QueryByRootId(id);
		}
		return new QueryByEntityId(id);
	}

	@Override
	public BaseQuery getQuery(final Entity entity)
	{
		return new QueryWithKnownDocument(entity.getDocument());
	}

	@Override
	public BaseQuery getQuery(final EntityCreation creation)
	{
		if (typeSystemInfo.isDocumentRootId(creation.getId()))
		{
			return new QueryForNewDocumentInstantiation(creation.getId());
		}

		final SingleAttributeKey parentKey = typeSystemInfo.getParentReferenceAttribute(creation.getTypeReference());
		final Identity parentId = TypeSystemInfo.toIdentity(creation.get(parentKey))
		                                        .orElseThrow(() -> new IllegalStateException(
				                                        "Couldn't extract parent id for creation '" + creation + "'."));

		return getQuery(parentId);
	}

	@Override
	public BaseQuery getQuery(final EntityModification modification)
	{
		return new QueryWithKnownDocument(modification.getDocument());
	}

	@Override
	public BaseQuery getQuery(final Criteria criteria)
	{
		final TypedCriteria typedCriteria = typeSystemInfo.getTypedCriteria(criteria);

		if (!typedCriteria.containsAnySupportedType())
		{
			return new QueryWithKnownEmptyResult();
		}

		final Optional<Identity> possibleRootId = typedCriteria.getRootId();
		if (possibleRootId.isPresent())
		{
			return new QueryByRootId(possibleRootId.get());
		}

		final Map<SingleAttributeKey, Object> possibleRootUniqueParams = typedCriteria.getRootUniqueParams();
		if (!possibleRootUniqueParams.isEmpty())
		{
            final EntityCondition conditions = EntityCondition.from(typedCriteria.getSupportedTypes(), possibleRootUniqueParams);
			return new QueryByUniqueRootAttributes(conditions);
		}

		final Optional<Identity> possibleEmbeddedItemId = typedCriteria.getEntityId();
		if (possibleEmbeddedItemId.isPresent())
		{
			return new QueryByEntityId(possibleEmbeddedItemId.get());
		}

		if (typedCriteria.onlyRootRequested())
		{
		    final EntityCondition conditions = EntityCondition.from(typedCriteria.getSupportedTypes(), criteria.getCondition(),
					criteria.getParams());
			return new QueryByRootAttributes(conditions);
		}

		final EntityCondition conditions = EntityCondition.from(typedCriteria.getSupportedTypes(), criteria.getCondition(),
				criteria.getParams());

		return new QueryByEntityAttributes(conditions);
	}
}
