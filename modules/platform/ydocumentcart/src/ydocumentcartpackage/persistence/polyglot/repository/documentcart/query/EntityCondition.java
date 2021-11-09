/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart.query;

import de.hybris.platform.persistence.polyglot.search.criteria.AbstractToStringVisitor;
import de.hybris.platform.persistence.polyglot.search.criteria.Condition;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition;
import de.hybris.platform.persistence.polyglot.search.criteria.Conditions.ComparisonCondition.CmpOperator;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.search.criteria.MatchingPredicateBuilder;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;

import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;


public class EntityCondition
{
	private static final Set<Identity> ALL_TYPES = new HashSet<>();

	private final Set<Identity> typeIds;
	private final Condition condition;
	private final Map<String, Object> params;

	private EntityCondition(final Set<Identity> typeIds, final Condition condition, final Map<String, Object> params)
	{
		this.typeIds = typeIds;
		this.condition = condition;
		this.params = params;
	}

	public static EntityCondition from(final SingleAttributeKey attribute, final Object value)
	{
		Objects.requireNonNull(attribute, "attribute mustn't be null");
		Objects.requireNonNull(value, "value mustn't be null");

		final ComparisonCondition condition = Conditions.cmp(attribute, CmpOperator.EQUAL, attribute.getQualifier());

		return new EntityCondition(ALL_TYPES, condition, Map.of(attribute.getQualifier(), value));
	}

	public static EntityCondition from(final Set<Identity> requestedTypes, final Map<SingleAttributeKey, Object> params)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		final List<Condition> eqConditions = new ArrayList<>();

		params.forEach((key, value) -> {
			values.put(key.getQualifier(), params.get(key));
			eqConditions.add(Conditions.cmp(key, CmpOperator.EQUAL, key.getQualifier()));
		});

		if (eqConditions.size() == 1)
		{
			return new EntityCondition(requestedTypes, eqConditions.get(0), values);
		}
		return new EntityCondition(requestedTypes, Conditions.and(eqConditions), values);
	}

	public static EntityCondition from(final Set<Identity> requestedTypes, final Condition condition,
	                                   final Map<String, Object> params)
	{
		return new EntityCondition(requestedTypes, condition, params);
	}

	public Predicate<ItemStateView> getPredicate()
	{
		return new MatchingPredicateBuilder(typeIds, condition, params).getPredicate();
	}

	public Set<Identity> getTypeIds()
	{
		return typeIds;
	}

	public Condition getCondition()
	{
		return condition;
	}

	public Map<String, Object> getParams()
	{
		return params;
	}

	@Override
	public String toString()
	{
		final ToStringVisitor visitor = new ToStringVisitor(params);

		condition.visit(visitor);

		return visitor.getString();
	}

	static class ToStringVisitor extends AbstractToStringVisitor
	{
		private final Map<String, Object> params;

		public ToStringVisitor(final Map<String, Object> params)
		{
			this.params = params;
		}

		@Override
		protected String asString(final ComparisonCondition condition)
		{
			final Object value = condition.getParamNameToCompare().map(params::get).orElse(null);
			return new StringBuilder(condition.getKey().toString()).append(condition.getOperator().getOperatorStr()).append(value)
			                                                       .toString();
		}
	}
}
