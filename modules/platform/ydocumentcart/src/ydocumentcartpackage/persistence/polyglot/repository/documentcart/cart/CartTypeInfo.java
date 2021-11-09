/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.cart;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport.PolyglotJaloConverter;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence.CoreAttributes;
import de.hybris.platform.persistence.polyglot.config.MoreSpecificCondition;
import de.hybris.platform.persistence.polyglot.config.RepositoryConfig;
import de.hybris.platform.persistence.polyglot.config.supplier.PropertiesPolyglotRepositoriesConfigProvider;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.PolyglotModelFactory;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.UnknownIdentity;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.TypeSystemInfo;
import ydocumentcartpackage.persistence.polyglot.repository.documentcart.query.TypedCriteria;
import de.hybris.platform.persistence.polyglot.search.criteria.ConditionVisitor;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition.CmpOperator;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.LogicalOrCondition;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


public class CartTypeInfo implements TypeSystemInfo
{
	private static final int CART_TYPE_CODE = 43;

	private final ConcurrentHashMap<String, Reference> typeReferences = new ConcurrentHashMap<>();

	private final PropertiesPolyglotRepositoriesConfigProvider propertiesPolyglotRepositoriesConfigProvider;

	public CartTypeInfo(final PropertiesPolyglotRepositoriesConfigProvider propertiesPolyglotRepositoriesConfigProvider)
	{
		this.propertiesPolyglotRepositoriesConfigProvider = propertiesPolyglotRepositoriesConfigProvider;
	}

	@Override
	public boolean isDocumentRootId(final Identity id)
	{
		return PolyglotJaloConverter.toJaloLayer(id).getTypeCode() == CART_TYPE_CODE;
	}

	@Override
	public SingleAttributeKey getParentReferenceAttribute(final Reference itemTypeReference)
	{
		if (getTypeReference(CartEntryModel._TYPECODE).equals(itemTypeReference))
		{
			return CartAttributes.order();
		}

		final RepositoryConfig config = propertiesPolyglotRepositoriesConfigProvider.getConfigs().get(0);
		final Map<Integer, Set<MoreSpecificCondition>> conditions = config.getConditions();
		for (final Set<MoreSpecificCondition> value : conditions.values())
		{
			for (final MoreSpecificCondition moreSpecificCondition : value)
			{
				if (moreSpecificCondition.getSourceIdentity().equals(itemTypeReference.getIdentity()))
				{
					return PolyglotPersistence.getNonlocalizedKey(moreSpecificCondition.getQualifier());
				}
			}
		}

		throw new IllegalArgumentException("Unknown type reference '" + itemTypeReference + "'.");
	}

	@Override
	public TypedCriteria getTypedCriteria(final Criteria criteria)
	{
		final ParamsExtractor extractor = new ParamsExtractor(criteria.getParams());
		criteria.getCondition().visit(extractor);

		final Set<Identity> requestedTypes = criteria.getTypeIdentitySet();
		final Set<Identity> supportedTypes = new HashSet<>();
		final Multimap<Identity, SingleAttributeKey> supportedRefEntityAttributes = HashMultimap.create();

		supportedTypes.add(getTypeReference(CartEntryModel._TYPECODE).getIdentity());
		supportedTypes.add(getTypeReference(CartModel._TYPECODE).getIdentity());
		safeAddSupportedType(supportedTypes, "PromotionResult");
		safeAddSupportedType(supportedTypes, "AbstractOrderEntryProductInfo").ifPresent(identity ->
				addSupportedReferenceAttribute(supportedRefEntityAttributes, identity,
						CartAttributes.ReferenceAttributes.ORDER_ENTRY));

		safeAddSupportedType(supportedTypes, "AbstractPromotionAction").ifPresent(identity ->
				addSupportedReferenceAttribute(supportedRefEntityAttributes, identity,
						CartAttributes.ReferenceAttributes.PROMOTION_RESULT));

		safeAddSupportedType(supportedTypes, "PromotionOrderEntryConsumed").ifPresent(identity ->
				addSupportedReferenceAttribute(supportedRefEntityAttributes, identity,
						CartAttributes.ReferenceAttributes.PROMOTION_RESULT, CartAttributes.ReferenceAttributes.ORDER_ENTRY));

		supportedTypes.retainAll(requestedTypes);

		return new TypedCartCriteria(supportedTypes, supportedRefEntityAttributes, extractor.getExtractedParams());
	}

	private void addSupportedReferenceAttribute(final Multimap<Identity, SingleAttributeKey> supportedRefAttributes,
	                                            final Identity identity, final SingleAttributeKey... keys)
	{
		for (final SingleAttributeKey key : keys)
		{
			supportedRefAttributes.put(identity, key);
		}
	}

	private Optional<Identity> safeAddSupportedType(final Set<Identity> supportedTypes, final String typeName)
	{
		try
		{
			final Identity supportedType = getTypeReference(typeName).getIdentity();
			supportedTypes.add(supportedType);
			return Optional.of(supportedType);
		}
		catch (final JaloItemNotFoundException e)
		{
			// nothing to do here
		}
		return Optional.empty();
	}

	private final Reference getTypeReference(final String typeName)
	{
		return typeReferences.computeIfAbsent(typeName, t -> {
			final ComposedType type = TypeManager.getInstance().getComposedType(t);
			return PolyglotPersistence.getReferenceTo(PolyglotJaloConverter.toPolyglotLayer(type.getPK()));
		});
	}

	private final Reference safeGetTypeReference(final String typeName)
	{
		try
		{
			return getTypeReference(typeName);
		}
		catch (final JaloItemNotFoundException e)
		{
			return PolyglotModelFactory.getReferenceTo(UnknownIdentity.instance());
		}
	}

	private class TypedCartCriteria implements TypedCriteria
	{

		private final Set<Identity> typeIds;
		private final Multimap<Identity, SingleAttributeKey> refAttributes;
		private final Map<SingleAttributeKey, Object> params;

		TypedCartCriteria(final Set<Identity> typeIds, final Multimap<Identity, SingleAttributeKey> refAttributes,
		                  final Map<SingleAttributeKey, Object> params)
		{
			this.typeIds = typeIds;
			this.refAttributes = refAttributes;
			this.params = params;
		}

		@Override
		public boolean containsAnySupportedType()
		{
			return !typeIds.isEmpty();
		}

		@Override
		public Optional<Identity> getRootId()
		{
			if (onlyRootRequested())
			{
				return TypeSystemInfo.toIdentity(convertStringIntoIdentity(params.get(CoreAttributes.pk())));
			}
			if (directReferenceToRootFromCartEntryRequested() || directReferenceToRootFromPromotionResultRequested())
			{
				return TypeSystemInfo.toIdentity(convertStringIntoIdentity(params.get(CartAttributes.order())));
			}
			return Optional.empty();
		}

		@Override
		public Map<SingleAttributeKey, Object> getRootUniqueParams()
		{
			if (onlyRootRequested())
			{
				final Object code = params.get(CartAttributes.code());
				if (code != null)
				{
					return Map.of(CartAttributes.code(), code);
				}
			}
			return Map.of();
		}

		@Override
		public Optional<Identity> getEntityId()
		{
			if (typeIds.size() == 1)
			{
				final Identity typeIdIdentity = typeIds.stream().findFirst().get();

				if (refAttributes.containsKey(typeIdIdentity))
				{
					final Map.Entry<SingleAttributeKey, Object> entry = params.entrySet().stream().findFirst().get();
					final Object param = entry.getValue();
					if (refAttributes.get(typeIdIdentity).contains(entry.getKey()))
					{
						return TypeSystemInfo.toIdentity(convertStringIntoIdentity(param));
					}
					return TypeSystemInfo.toIdentity(param);
				}
				return TypeSystemInfo.toIdentity(convertStringIntoIdentity(params.get(CoreAttributes.pk())));
			}

			return Optional.empty();
		}

		private Object convertStringIntoIdentity(final Object paramValue)
		{
			if (paramValue instanceof String)
			{
				return PolyglotModelFactory.identityFromLong(Long.valueOf((String) paramValue));
			}
			return paramValue;
		}

		@Override
		public Set<Identity> getSupportedTypes()
		{
			return typeIds;
		}

		@Override
		public boolean onlyRootRequested()
		{
			return typeIds.size() == 1 && typeIds.contains(getTypeReference(CartModel._TYPECODE).getIdentity());
		}

		private boolean directReferenceToRootFromCartEntryRequested()
		{
			return typeIds.size() == 1 && typeIds.contains(getTypeReference(CartEntryModel._TYPECODE).getIdentity());
		}

		private boolean directReferenceToRootFromPromotionResultRequested()
		{
			return typeIds.size() == 1 && typeIds.contains(safeGetTypeReference("PromotionResult").getIdentity());
		}

	}

	private static class ParamsExtractor implements ConditionVisitor
	{
		private final Map<String, Object> values;
		private final HashMap<SingleAttributeKey, Object> extractedParams = new HashMap<>();
		private boolean tooComplex = false;

		public ParamsExtractor(final Map<String, Object> values)
		{
			this.values = Objects.requireNonNull(values, "values mustn't be null.");
		}

		@Override
		public void accept(final ComparisonCondition condition)
		{
			if (tooComplex || condition.getOperator() != CmpOperator.EQUAL)
			{
				return;
			}
			final Object value = condition.getParamNameToCompare().map(values::get).orElse(null);
			extractedParams.put(condition.getKey(), value);
		}

		@Override
		public void accept(final LogicalOrCondition condition)
		{
			tooComplex = true;
		}

		public Map<SingleAttributeKey, Object> getExtractedParams()
		{
			return tooComplex ? Map.of() : extractedParams;
		}
	}


}