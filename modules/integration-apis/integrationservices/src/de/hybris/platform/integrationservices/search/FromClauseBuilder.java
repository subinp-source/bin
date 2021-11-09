/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.MANY_TO_MANY_SOURCE_FIELD;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.MANY_TO_MANY_TARGET_FIELD;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getAttributeType;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getItemAlias;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelation;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelationAlias;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelationName;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelationType;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class FromClauseBuilder
{
	private static final String MANY = "many";
	private static final String SELECT_FROM_STATEMENT = "SELECT DISTINCT {%s:" + ItemModel.PK + "} FROM {%s%s AS %s%s%s}";
	private static final String JOIN_STATEMENT = "JOIN %s AS %s ON {%s:" + ItemModel.PK + "} = {%s:%s}";
	private static final String MANYTOMANY = "many2many";
	private static final String ONETOMANY = "one2many";

	private WhereClauseConditions filter;
	private IntegrationObjectItemModel itemModel;
	private ItemTypeMatch itemTypeMatch;

	private FromClauseBuilder()
	{
	}

	static FromClauseBuilder builder()
	{
		return new FromClauseBuilder();
	}

	FromClauseBuilder withFilter(final WhereClauseConditions filter)
	{
		this.filter = filter;
		return this;
	}

	FromClauseBuilder withIntegrationObjectItem(final IntegrationObjectItemModel itemModel)
	{
		this.itemModel = itemModel;
		return this;
	}

	FromClauseBuilder withTypeHierarchyRestriction(final ItemTypeMatch itemTypeMatch)
	{
		this.itemTypeMatch = itemTypeMatch;
		return this;
	}

	String build()
	{
		if (itemModel != null)
		{
			final String joins = createJoinStatements();
			return createSelectStatement(joins);
		}
		return "";
	}

	private String createSelectStatement(final String joins)
	{
		final ComposedTypeModel itemType = itemModel.getType();
		final String itemAlias = getItemAlias(itemModel);
		final String queryItemHierarchy = getQueryItemHierarchy();
		final String spaceBeforeJoins = joins.isEmpty() ? "" : " ";
		return String.format(SELECT_FROM_STATEMENT,
				itemAlias,
				itemType.getCode(),
				queryItemHierarchy,
				itemAlias,
				spaceBeforeJoins,
				joins);
	}

	/**
	 * @return Query item hierarchy symbol
	 */
	private String getQueryItemHierarchy()
	{
		final ComposedTypeModel itemType = itemModel.getType();
		return itemType instanceof EnumerationMetaTypeModel || itemType.getAbstract()
				? ItemTypeMatch.ALL_SUBTYPES.getValue()
				: getItemTypeMatch();
	}

	private String getItemTypeMatch()
	{
		return itemTypeMatch != null ? itemTypeMatch.getValue() : ItemTypeMatch.DEFAULT.getValue();
	}

	private String createJoinStatements()
	{
		if (filter != null)
		{
			return filter.getConditions().stream()
			             .map(this::buildJoinStatement)
			             .filter(StringUtils::isNotBlank)
			             .reduce("", this::combineStatements);
		}
		return "";
	}

	private String combineStatements(final String s1, final String s2)
	{
		return (s1.isEmpty() ? "" : (s1 + " ")) + s2;
	}

	private String buildJoinStatement(final WhereClauseCondition condition)
	{
		final ComposedTypeModel itemType = itemModel.getType();
		final Optional<String> filterTypeCode = getAttributeType(itemModel, condition.getAttributeName());
		return filterTypeCode
				.flatMap(typeCode -> getRelation(itemType, typeCode))
				.map(this::handleRelations)
				.orElse("");
	}

	private String handleRelations(final RelationDescriptorModel relationDescriptorModel)
	{
		String relationType = getRelationType(relationDescriptorModel);
		if (relationType.equalsIgnoreCase(MANYTOMANY))
		{
			return buildJoinStatementFromAttribute(relationDescriptorModel);
		}
		else if (relationType.equalsIgnoreCase(ONETOMANY))
		{
			return buildJoinStatementForOneToMany(relationDescriptorModel);
		}
		return "";
	}

	private String buildJoinStatementFromAttribute(final RelationDescriptorModel attributeDescriptorModel)
	{
		final String fieldName = getRelationFieldName(attributeDescriptorModel);
		final String relationName = getRelationName(attributeDescriptorModel);
		final String relationAlias = getRelationAlias(attributeDescriptorModel);
		return String.format(JOIN_STATEMENT,
				relationName,
				relationAlias,
				getItemAlias(itemModel),
				relationAlias,
				fieldName);
	}

	private String buildJoinStatementForOneToMany(final RelationDescriptorModel attributeDescriptorModel)
	{
		String manyTypeCode = "";
		if (attributeDescriptorModel.getRelationType().getSourceTypeCardinality().getCode().equals(MANY))
		{
			manyTypeCode = attributeDescriptorModel.getRelationType().getSourceType().getCode();
		}
		else
		{
			manyTypeCode = attributeDescriptorModel.getRelationType().getTargetType().getCode();
		}
		return String.format(JOIN_STATEMENT,
				manyTypeCode,
				manyTypeCode.toLowerCase(Locale.ENGLISH),
				getItemAlias(itemModel),
				manyTypeCode.toLowerCase(Locale.ENGLISH),
				getItemAlias(itemModel));
	}

	private String getRelationFieldName(final RelationDescriptorModel attributeDescriptorModel)
	{
		return attributeDescriptorModel.getIsSource() ? MANY_TO_MANY_SOURCE_FIELD : MANY_TO_MANY_TARGET_FIELD;
	}
}
