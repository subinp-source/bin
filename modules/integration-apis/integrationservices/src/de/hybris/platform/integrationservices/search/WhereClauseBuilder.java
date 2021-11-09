/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search;

import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.MANY_TO_MANY_SOURCE_FIELD;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.MANY_TO_MANY_TARGET_FIELD;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getAttributeType;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getClassificationAttributeModel;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getItemAlias;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelation;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getRelationType;
import static de.hybris.platform.integrationservices.search.ClauseBuilderUtil.getVirtualAttributeModel;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.integrationservices.exception.FilterByClassificationAttributeNotSupportedException;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Builds a {@code WHERE} clause for flexible search given the input.
 */
class WhereClauseBuilder
{
	private static final String WHERE = " WHERE ";
	private static final String MANY = "many";
	private static final String MANYTOMANY = "many2many";
	private static final String ONETOMANY = "one2many";

	private IntegrationObjectItemModel itemModel;
	private Map<String, Object> parameters;
	private Map<String, Object> dataItem;
	private WhereClauseConditions filter;
	private Locale locale;

	private WhereClauseBuilder()
	{
	}

	public static WhereClauseBuilder builder()
	{
		return new WhereClauseBuilder();
	}

	private static String parameterValue(final String alias, final String key, final Map<String, Object> parameters)
	{
		final String clause;
		final Object value = parameters.get(key);
		if ("null".equals(String.valueOf(value)))
		{
			clause = "IS NULL";
		}
		else
		{
			clause = "=" + " ?" + key;
		}
		return '{' + alias + ":" + key + "} " + clause;
	}

	WhereClauseBuilder withParameters(final Map<String, Object> parameters)
	{
		this.parameters = parameters;
		return this;
	}

	WhereClauseBuilder withIntegrationObjectItem(final IntegrationObjectItemModel itemModel)
	{
		this.itemModel = itemModel;
		return this;
	}

	WhereClauseBuilder withDataItem(final Map<String, Object> dataItem)
	{
		this.dataItem = dataItem;
		return this;
	}

	WhereClauseBuilder withFilter(final WhereClauseConditions filter)
	{
		this.filter = filter;
		return this;
	}

	WhereClauseBuilder withLocale(final Locale locale)
	{
		this.locale = locale;
		return this;
	}

	public String build()
	{
		processKeyConditions(dataItem);
		if (parameters == null || parameters.isEmpty())
		{
			return filterWithPrefixOrEmptyString(" WHERE");
		}
		//optionalFilterWithPrefix(" AND") permits enriching a search with additional filtering for a single item with a key
		return whereClauseForSingleItem() + filterWithPrefixOrEmptyString(" AND");
	}

	private String filterWithPrefixOrEmptyString(final String prefix)
	{
		return filter != null ? prefix + fromFilter() : "";
	}

	private String fromFilter()
	{
		if (itemModel != null)
		{
			validateFilterDoesNotContainUnsupportedAttributes();
			return filter.getConditions().stream()
			             .map(this::updateWhereClauseConditionWithAlias)
			             .map(this::combineConditionWithConjunctiveOperator)
			             .reduce("", (c1, c2) -> c1 + " " + c2);
		}
		return "";
	}

	private void validateFilterDoesNotContainUnsupportedAttributes()
	{
		validateFilterDoesNotContainClassificationAttributesInConditions();
		validateFilterDoesNotContainVirtualAttributesInConditions();
	}

	private void validateFilterDoesNotContainVirtualAttributesInConditions()
	{
		filter.getConditions().stream()
		      .map(WhereClauseCondition::getAttributeName)
		      .map(name -> getVirtualAttributeModel(itemModel, name))
		      .filter(Optional::isPresent)
		      .map(Optional::get)
		      .findFirst()
		      .ifPresent(virtualAttribute -> { throw new FilterByVirtualAttributeNotSupportedException(virtualAttribute);});
	}

	private void validateFilterDoesNotContainClassificationAttributesInConditions()
	{
		filter.getConditions().stream()
		      .map(WhereClauseCondition::getAttributeName)
		      .map(name -> getClassificationAttributeModel(itemModel, name))
		      .filter(Optional::isPresent)
		      .map(Optional::get)
		      .findFirst()
		      .ifPresent(classificationAttr -> {throw new FilterByClassificationAttributeNotSupportedException(classificationAttr);});
	}

	private WhereClauseCondition translateConditionToPlatformName(final WhereClauseCondition condition)
	{
		return ClauseBuilderUtil.changeConditionToUsePlatformAttributeName(condition, itemModel, locale);
	}

	private String combineConditionWithConjunctiveOperator(final WhereClauseCondition condition)
	{
		return condition.getCondition() + (StringUtils.isNotBlank(condition.getConjunctiveOperatorString())
				? (" " + condition.getConjunctiveOperatorString())
				: "");
	}

	private WhereClauseCondition updateWhereClauseConditionWithAlias(final WhereClauseCondition condition)
	{
		final ComposedTypeModel itemType = itemModel.getType();
		final Optional<String> filterTypeCode = getAttributeType(itemModel, condition.getAttributeName());
		return filterTypeCode
				.map(typeCode -> getRelation(itemType, typeCode))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(relation -> handleRelations(relation, condition))
				.orElseGet(() -> updateOneToOneWhereClauseCondition(translateConditionToPlatformName(condition)));
	}

	private WhereClauseCondition handleRelations(final RelationDescriptorModel relationDescriptorModel,
	                                             final WhereClauseCondition condition)
	{
		final String relationType = getRelationType(relationDescriptorModel);
		if (relationType.equalsIgnoreCase(MANYTOMANY))
		{
			return updateManyToManyWhereClauseCondition(relationDescriptorModel, condition);
		}
		else if (relationType.equalsIgnoreCase(ONETOMANY))
		{
			return updateOneToManyWhereClauseCondition(relationDescriptorModel, condition);
		}
		return null;
	}

	private WhereClauseCondition updateManyToManyWhereClauseCondition(final RelationDescriptorModel attributeDescriptorModel,
	                                                                  final WhereClauseCondition filter)
	{
		return Boolean.TRUE.equals(attributeDescriptorModel.getIsSource())
				? new ItemWhereClauseCondition(attributeDescriptorModel, MANY_TO_MANY_TARGET_FIELD, filter)
				: new ItemWhereClauseCondition(attributeDescriptorModel, MANY_TO_MANY_SOURCE_FIELD, filter);
	}

	private WhereClauseCondition updateOneToManyWhereClauseCondition(final RelationDescriptorModel relationDescriptorModel,
	                                                                 final WhereClauseCondition filter)
	{
		return new ItemWhereClauseCondition(filter.changeAttributeName(getManyCodeAsAlias(relationDescriptorModel)));
	}

	private WhereClauseCondition updateOneToOneWhereClauseCondition(final WhereClauseCondition filter)
	{
		return new ItemWhereClauseCondition(itemModel, filter);
	}

	private String whereClauseForSingleItem()
	{
		final String itemAlias = getItemAlias(itemModel);
		final StringBuilder builder = new StringBuilder(WHERE);
		parameters.keySet().forEach(key -> appendParameterValue(itemAlias, builder, key));
		return builder.toString();
	}

	private void appendParameterValue(final String alias, final StringBuilder builder, final String key)
	{
		if (builder.length() > WHERE.length())
		{
			builder.append(" AND ");
		}
		builder.append(parameterValue(alias, key, parameters));
	}

	private void processKeyConditions(final Map<String, Object> obj)
	{
		if (dataItem != null)
		{
			Preconditions.checkState(itemModel != null, "Integration object item is not specified yet");
			itemModel.getUniqueAttributes().stream()
			         .filter(attr -> itemModel.equals(attr.getIntegrationObjectItem()))
			         .forEach(attr -> withParameter(attr.getAttributeDescriptor().getQualifier(), attributeValue(attr, obj)));
		}
	}

	private void withParameter(final String key, final Object value)
	{
		parameters.put(key, value);
	}

	protected Object attributeValue(final IntegrationObjectItemAttributeModel attr, final Map<String, Object> item)
	{
		final Object value = item.get(attr.getAttributeName());
		return value instanceof Calendar
				? ((Calendar) value).getTime()
				: value;
	}

	private String getManyCodeAsAlias(final RelationDescriptorModel relationDescriptorModel)
	{
		if (relationDescriptorModel.getRelationType().getSourceTypeCardinality().getCode().equals(MANY))
		{
			return relationDescriptorModel.getRelationType().getSourceType().getCode().toLowerCase(Locale.ENGLISH);
		}
		return relationDescriptorModel.getRelationType().getTargetType().getCode().toLowerCase(Locale.ENGLISH);
	}
}