/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search;

import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

class ClauseBuilderUtil
{
	private static final String MANY = "many";
	static final String MANY_TO_MANY_SOURCE_FIELD = "source";
	static final String MANY_TO_MANY_TARGET_FIELD = "target";

	private ClauseBuilderUtil()
	{
	}

	static Optional<AttributeDescriptorModel> getAttributeDescriptorModelFromFilterAndType(final WhereClauseCondition filter,
	                                                                                       final ComposedTypeModel itemType)
	{
		if (filter != null)
		{
			final String attributeName = filter.getAttributeName();
			return Stream.concat(itemType.getInheritedattributedescriptors().stream(),
					itemType.getDeclaredattributedescriptors().stream())
			             .filter(attr -> attr instanceof RelationDescriptorModel)
			             .filter(attr -> attributeRelationRoleMatchesAttributeName((RelationDescriptorModel) attr, attributeName))
			             .findAny();
		}
		return Optional.empty();
	}

	static String getRelationAlias(final AttributeDescriptorModel attributeDescriptorModel)
	{
		return getRelationName(attributeDescriptorModel).toLowerCase(Locale.ENGLISH);
	}

	static String getItemAlias(final IntegrationObjectItemModel itemModel)
	{
		return itemModel.getType().getCode().toLowerCase(Locale.ENGLISH);
	}

	static boolean isManyToManyRelation(final AttributeDescriptorModel attributeDescriptorModel)
	{
		if (attributeDescriptorModel instanceof RelationDescriptorModel)
		{
			final RelationDescriptorModel relationDescriptorModel = (RelationDescriptorModel) attributeDescriptorModel;
			return isManySourceRelation(relationDescriptorModel) && isManyTargetRelation(relationDescriptorModel);
		}
		return false;
	}

	static Optional<RelationDescriptorModel> getRelation(final ComposedTypeModel itemType, final String typeCode)
	{
		return Stream.concat(itemType.getInheritedattributedescriptors().stream(),
				itemType.getDeclaredattributedescriptors().stream())
		             .filter(attr -> attr instanceof RelationDescriptorModel)
		             .map(RelationDescriptorModel.class::cast)
		             .filter(attr -> (attr.getRelationType().getTargetType().getCode().equalsIgnoreCase(typeCode)
				             || attr.getRelationType().getSourceType().getCode().equalsIgnoreCase(typeCode)))
		             .findAny();
	}

	static String getRelationType(final RelationDescriptorModel relationDescriptorModel)
	{
		if (relationDescriptorModel.getIsSource())
		{
			return relationDescriptorModel.getRelationType().getSourceTypeCardinality().getCode() + "2" +
					relationDescriptorModel.getRelationType().getTargetTypeCardinality().getCode();
		}
		else
		{
			return relationDescriptorModel.getRelationType().getTargetTypeCardinality().getCode() + "2" +
					relationDescriptorModel.getRelationType().getSourceTypeCardinality().getCode();
		}
	}

	static String getRelationName(final AttributeDescriptorModel attributeDescriptorModel)
	{
		final RelationDescriptorModel relationDescriptorModel = (RelationDescriptorModel) attributeDescriptorModel;
		return relationDescriptorModel.getRelationName();
	}

	static boolean isAttributeSource(final RelationDescriptorModel attr, final String attributeName)
	{
		final String sourceTypeRole = attr.getRelationType().getSourceTypeRole();
		return sourceTypeRole != null && sourceTypeRole.equalsIgnoreCase(attributeName);
	}

	private static boolean isAttributeTarget(final RelationDescriptorModel attr, final String attributeName)
	{
		final String targetTypeRole = attr.getRelationType().getTargetTypeRole();
		return targetTypeRole != null && targetTypeRole.equalsIgnoreCase(attributeName);
	}

	private static boolean attributeRelationRoleMatchesAttributeName(final RelationDescriptorModel attr,
	                                                                 final String attributeName)
	{
		return isAttributeSource(attr, attributeName) || isAttributeTarget(attr, attributeName);
	}

	private static boolean isManySourceRelation(final RelationDescriptorModel relationDescriptorModel)
	{
		return relationDescriptorModel.getRelationType().getSourceTypeCardinality().getCode().equals(MANY);
	}

	private static boolean isManyTargetRelation(final RelationDescriptorModel relationDescriptorModel)
	{
		return relationDescriptorModel.getRelationType().getTargetTypeCardinality().getCode().equals(MANY);
	}

	/**
	 * Attribute name in the integration object may differ from the name of the corresponding attribute in the type system.
	 * This method ensures that the type system name is used in the search conditions by replacing the original condition that
	 * normally uses integration object attribute name with a analogous condition that uses type system name for the attribute.
	 * If the type system attribute name is localized, it will be decorated with the provided language.
	 *
	 * @param condition original condition to be converted to use type system attribute name.
	 * @param itemModel integration object item defining type containing the attribute and that will be used for the attribute name
	 *                  conversion.
	 * @param locale    acceptable language locale.
	 * @return a translated condition, if the attribute was found in the specified {@code itemModel} or, otherwise, the original
	 * condition.
	 */
	static WhereClauseCondition changeConditionToUsePlatformAttributeName(@NotNull final WhereClauseCondition condition,
	                                                                      @NotNull final IntegrationObjectItemModel itemModel,
	                                                                      @NotNull final Locale locale)
	{
		return getAttributeModel(itemModel, condition.getAttributeName())
				.map(IntegrationObjectItemAttributeModel::getAttributeDescriptor)
				.filter(AttributeDescriptorModel::getLocalized)
				.map(AttributeDescriptorModel::getQualifier)
				.map(decorateQualifierWithLanguage(locale))
				.map(condition::changeAttributeName)
				.orElseGet(() -> changeConditionToUsePlatformAttributeName(condition, itemModel));
	}

	private static WhereClauseCondition changeConditionToUsePlatformAttributeName(@NotNull final WhereClauseCondition condition,
	                                                                              @NotNull final IntegrationObjectItemModel itemModel)
	{
		return getAttributeModel(itemModel, condition.getAttributeName())
				.map(IntegrationObjectItemAttributeModel::getAttributeDescriptor)
				.map(AttributeDescriptorModel::getQualifier)
				.map(isDateType(condition,
						itemModel) ? condition::changeAttributeNameAndFormatDateValue : condition::changeAttributeName)
				.orElse(condition);
	}

	private static boolean isDateType(final @NotNull WhereClauseCondition condition,
	                                  final @NotNull IntegrationObjectItemModel itemModel)
	{
		return getAttributeModel(itemModel, condition.getAttributeName())
				.map(IntegrationObjectItemAttributeModel::getAttributeDescriptor)
				.filter(attributeDescriptorModel -> Date.class.equals(attributeDescriptorModel.getPersistenceClass()))
				.isPresent();
	}

	/**
	 * Attribute name in the integration object may differ from the name of the corresponding attribute in the type system.
	 * This method returns the type system attribute name to be used in the order by expression.
	 * If the type system attribute name is localized, it will be decorated with the provided language.
	 *
	 * @param orderExpression original order by expression that has the localized attribute name to be converted to the type system attribute name.
	 * @param itemModel       object item defining type containing the attribute and that will be used for the attribute name conversion.
	 * @param locale          acceptable language locale.
	 * @return a type system localized attribute name decorated with the language, e.g., name[en]
	 */
	static String changeOrderByToUsePlatformAttributeName(@NotNull final OrderExpression orderExpression,
	                                                      @NotNull final IntegrationObjectItemModel itemModel,
	                                                      @NotNull final Locale locale)
	{
		return getAttributeModel(itemModel, orderExpression.getOrderBy())
				.map(IntegrationObjectItemAttributeModel::getAttributeDescriptor)
				.filter(AttributeDescriptorModel::getLocalized)
				.map(AttributeDescriptorModel::getQualifier)
				.map(decorateQualifierWithLanguage(locale))
				.orElseGet(() -> changeOrderByToUsePlatformAttributeName(orderExpression.getOrderBy(), itemModel));
	}

	private static String changeOrderByToUsePlatformAttributeName(@NotNull final String attributeName,
	                                                              @NotNull final IntegrationObjectItemModel itemModel)
	{
		return getAttributeModel(itemModel, attributeName)
				.map(IntegrationObjectItemAttributeModel::getAttributeDescriptor)
				.map(AttributeDescriptorModel::getQualifier)
				.orElse(attributeName);
	}

	private static Function<String, String> decorateQualifierWithLanguage(final @NotNull Locale locale)
	{
		return qualifier -> new StringBuilder().append(qualifier).append("[").append(locale).append("]").toString();
	}

	private static Optional<IntegrationObjectItemAttributeModel> getAttributeModel(
			final IntegrationObjectItemModel itemModel,
			final String attributeName)
	{
		return itemModel.getAttributes().stream()
		                .filter(attr -> attr.getAttributeName().equals(attributeName))
		                .findAny();
	}

	public static Optional<String> getAttributeType(
			final IntegrationObjectItemModel itemModel,
			final String attributeName)
	{
		return itemModel.getAttributes().stream()
		                .map(attr -> getTypeCodeFromName(attr, attributeName))
		                .filter(Objects::nonNull)
		                .findAny();
	}

	private static String getTypeCodeFromName(IntegrationObjectItemAttributeModel attributeModel, String attributeName)
	{
		if (attributeModel.getAttributeName().equals(attributeName))
		{
			if (attributeModel.getAttributeDescriptor().getAttributeType() instanceof CollectionTypeModel)
			// IAPI-3670. eg: InboundProduct: europe1Prices is a Dynamic Attribute and CollectionType.
			{
				return ((CollectionTypeModel) attributeModel.getAttributeDescriptor()
				                                            .getAttributeType()).getElementType().getCode();
			}
			else if (attributeModel.getAttributeDescriptor().getAttributeType() instanceof ComposedTypeModel)
			{
				return attributeModel.getAttributeDescriptor().getAttributeType().getCode();
			}
		}
		return null;
	}


	/**
	 * Searches for a classification attribute model in the specified integration object item model.
	 *
	 * @param itemModel     an integration object item model to search for the classification attribute in.
	 * @param attributeName name of the classification attribute to find.
	 * @return the matching classification attribute or {@code Optional.empty()}, if the item model does not have a classification
	 * attribute with the specified name.
	 */
	static Optional<IntegrationObjectItemClassificationAttributeModel> getClassificationAttributeModel(
			final IntegrationObjectItemModel itemModel,
			final String attributeName)
	{
		return itemModel.getClassificationAttributes().stream()
		                .filter(attr -> attr.getAttributeName().equals(attributeName))
		                .findAny();
	}

	/**
	 * Searches for a virtual attribute model in the specified integration object item model.
	 *
	 * @param itemModel     an integration object item model to search for the virtual attribute in.
	 * @param attributeName name of the virtual attribute to find.
	 * @return the matching virtual attribute or {@code Optional.empty()}, if the item model does not have a virtual
	 * attribute with the specified name.
	 */
	static Optional<IntegrationObjectItemVirtualAttributeModel> getVirtualAttributeModel(
			final IntegrationObjectItemModel itemModel,
			final String attributeName)
	{
		return itemModel.getVirtualAttributes().stream()
		                .filter(attr -> attr.getAttributeName().equals(attributeName))
		                .findAny();
	}
}