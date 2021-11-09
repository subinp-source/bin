/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.widgets.modals.generator;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemVirtualAttributeModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectVirtualAttributeDescriptorModel;
import de.hybris.platform.odata2webservices.enums.IntegrationType;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;

public final class DefaultIntegrationObjectImpexGenerator implements IntegrationObjectImpexGenerator
{
	private static final String ENDL = " \r\n";
	private static final String GENERIC_INTEGRATION_OBJECT_HEADER =
			"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)";
	private static final String GENERIC_INTEGRATION_OBJECT_ITEM_HEADER =
			"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; " +
					"itemTypeMatch(code)";
	private static final String GENERIC_INTEGRATION_OBJECT_ITEM_ATTRIBUTE_HEADER =
			"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; " +
					"attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); " +
					"returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false]";
	private static final String GENERIC_INTEGRATION_OBJECT_ITEM_CLASSIFICATION_ATTRIBUTE_HEADER =
			"INSERT_UPDATE IntegrationObjectItemClassificationAttribute; " +
					"integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; " +
					"classAttributeAssignment(classificationClass(catalogVersion(catalog(id), version), code), classificationAttribute(systemVersion(catalog(id), version), code));" +
					"returnIntegrationObjectItem(integrationObject(code), code); autocreate[default = false]";
	private static final String GENERIC_INTEGRATION_OBJECT_ITEM_VIRTUAL_ATTRIBUTE_HEADER =
			"INSERT_UPDATE IntegrationObjectItemVirtualAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; " +
					"attributeName[unique = true]; retrievalDescriptor(code)";
	private static final String GENERIC_INTEGRATION_OBJECT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_HEADER =
			"INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation; type(code)";
	private static final String HEADER_TAB_DELIMITER = "\t; ";
	private static final String HEADER_SPACE_DELIMITER = "; ";
	private StringBuilder impexBuilder;
	private IntegrationObjectModel integrationObject;
	private String integrationObjectCode;
	private Set<IntegrationObjectItemModel> integrationObjectItems;
	private int longestIntegrationObjectItemLength;
	private int longestItemMatchTypeStringLength;
	private Set<IntegrationObjectVirtualAttributeDescriptorModel> virtualAttributeDescriptors;
	private int longestVirtualAttributeLength;

	@Override
	public String generateImpex(final IntegrationObjectModel selectedIntegrationObject)
	{
		impexBuilder = new StringBuilder();
		integrationObject = selectedIntegrationObject;
		integrationObjectCode = integrationObject.getCode();
		integrationObjectItems = integrationObject.getItems();

		final int[] longestStrings = calculateLongestStrings(integrationObjectItems);
		longestIntegrationObjectItemLength = longestStrings[0];
		longestItemMatchTypeStringLength = longestStrings[1];

		constructIntegrationObjectBlock();
		constructIntegrationObjectItemBlock();
		constructIntegrationObjectItemAttributeBlock();

		if (Boolean.TRUE.equals(integrationObject.getClassificationAttributesPresent()))
		{
			constructIntegrationObjectItemClassificationAttributeBlock();
		}

		longestVirtualAttributeLength = calculateVirtualAttributeColumnLength(integrationObjectItems);
		if (longestVirtualAttributeLength > 0)
		{
			constructIntegrationObjectItemVirtualAttributeBlock();
			if (CollectionUtils.isNotEmpty(virtualAttributeDescriptors))
			{
				constructIntegrationObjectVirtualAttributeDescriptorBlock();
			}
		}

		return impexBuilder.toString();
	}

	private void constructIntegrationObjectVirtualAttributeDescriptorBlock()
	{
		final int[] longestStrings = calculateLongestVirtualDescriptorStrings(virtualAttributeDescriptors);
		final int longestDescriptorCode = longestStrings[0];
		final int longestLogicLocation = longestStrings[1];
		impexBuilder.append(ENDL);
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_VIRTUAL_ATTRIBUTE_DESCRIPTOR_HEADER);
		impexBuilder.append(ENDL);
		for (final IntegrationObjectVirtualAttributeDescriptorModel descriptor : virtualAttributeDescriptors)
		{
			final String descriptorCode = descriptor.getCode();
			final String logicLocation = descriptor.getLogicLocation();
			final TypeModel type = descriptor.getType();
			impexBuilder.append("; ");
			impexBuilder.append(descriptorCode);
			addWhitespace(longestDescriptorCode, descriptorCode.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(logicLocation);
			addWhitespace(longestLogicLocation, logicLocation.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(type != null ? type.getCode() : "");
			impexBuilder.append(ENDL);
		}
	}

	private void constructIntegrationObjectItemVirtualAttributeBlock()
	{
		virtualAttributeDescriptors = new HashSet<>();
		impexBuilder.append(ENDL);
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_ITEM_VIRTUAL_ATTRIBUTE_HEADER);
		impexBuilder.append(ENDL);
		for (final IntegrationObjectItemModel item : integrationObjectItems)
		{
			constructIntegrationObjectItemVirtualAttributeLine(item);
		}
	}

	private void constructIntegrationObjectItemClassificationAttributeBlock()
	{
		final int longestClassificationAttributeLength = calculateClassificationAttributeColumnsLength(integrationObjectItems);
		final int longestClassAssignmentStringLength = calculateClassAssignmentColumnsLength(integrationObjectItems);
		impexBuilder.append(ENDL);
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_ITEM_CLASSIFICATION_ATTRIBUTE_HEADER);
		impexBuilder.append(ENDL);
		for (final IntegrationObjectItemModel item : integrationObjectItems)
		{
			constructIntegrationObjectItemClassificationAttributeLine(longestClassificationAttributeLength,
					longestClassAssignmentStringLength, item);
		}
	}

	private void constructIntegrationObjectItemAttributeBlock()
	{
		final int[] lengths = calculateAttributeColumnsLength(integrationObjectItems);
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_ITEM_ATTRIBUTE_HEADER);
		impexBuilder.append(ENDL);
		for (final IntegrationObjectItemModel item : integrationObjectItems)
		{
			constructIntegrationObjectItemAttributeLine(lengths, item);
		}
	}

	private void constructIntegrationObjectItemBlock()
	{
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_ITEM_HEADER);
		impexBuilder.append(ENDL);
		for (final IntegrationObjectItemModel item : integrationObjectItems)
		{
			constructIntegrationObjectItemLine(item);
		}
		impexBuilder.append(ENDL);
	}

	private void constructIntegrationObjectBlock()
	{
		impexBuilder.append(GENERIC_INTEGRATION_OBJECT_HEADER);
		impexBuilder.append(ENDL);
		impexBuilder.append(HEADER_SPACE_DELIMITER);
		impexBuilder.append(integrationObjectCode);
		impexBuilder.append(HEADER_SPACE_DELIMITER);
		final IntegrationType integrationType = integrationObject.getIntegrationType();
		impexBuilder.append(integrationType != null ? integrationType.getCode() : "");
		impexBuilder.append(ENDL).append(ENDL);
	}

	private void constructIntegrationObjectItemLine(final IntegrationObjectItemModel item)
	{
		final String currentItemName = item.getType().getCode();
		final String itemTypeMatch = item.getItemTypeMatch() != null ? item.getItemTypeMatch().getCode() : "";
		impexBuilder.append(HEADER_SPACE_DELIMITER);
		impexBuilder.append(integrationObjectCode);
		impexBuilder.append(HEADER_TAB_DELIMITER);
		impexBuilder.append(currentItemName);
		addWhitespace(longestIntegrationObjectItemLength, currentItemName.length(), impexBuilder);
		impexBuilder.append(HEADER_TAB_DELIMITER);
		impexBuilder.append(currentItemName);
		addWhitespace(longestIntegrationObjectItemLength, currentItemName.length(), impexBuilder);
		impexBuilder.append(HEADER_TAB_DELIMITER);
		impexBuilder.append(!Boolean.TRUE.equals(item.getRoot()) ? "" : item.getRoot().toString());
		impexBuilder.append(HEADER_TAB_DELIMITER);
		impexBuilder.append(itemTypeMatch);
		addWhitespace(longestItemMatchTypeStringLength, itemTypeMatch.length(), impexBuilder);
		impexBuilder.append(HEADER_TAB_DELIMITER);
		impexBuilder.append(ENDL);
	}

	private void constructIntegrationObjectItemAttributeLine(final int[] lengths, final IntegrationObjectItemModel item)
	{
		final int longestAttributeIndex = 0;
		final int longestDescriptorIndex = 1;
		final int longestReturnIOIndex = 2;
		final String currentItemName = item.getCode();
		for (final IntegrationObjectItemAttributeModel attribute : item.getAttributes())
		{
			final String attributeName = attribute.getAttributeName();
			final String attributeDescriptor = currentItemName + ":" + attribute.getAttributeDescriptor().getQualifier();
			final String returnIntegrationObject = (attribute.getReturnIntegrationObjectItem() != null) ?
					(integrationObjectCode + ":" + attribute.getReturnIntegrationObjectItem().getCode()) : "";
			final String isUnique = (BooleanUtils.isTrue(attribute.getUnique())) ? attribute.getUnique().toString() : "";
			final String autocreate = (BooleanUtils.isTrue(attribute.getAutoCreate())) ?
					attribute.getAutoCreate().toString() : "";
			constructIntegrationObjectItemSection(currentItemName);
			impexBuilder.append(attributeName);
			addWhitespace(lengths[longestAttributeIndex], attributeName.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(attributeDescriptor);
			addWhitespace(lengths[longestDescriptorIndex], attributeDescriptor.length() - 1, impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(returnIntegrationObject);
			addWhitespace(lengths[longestReturnIOIndex], (attribute.getReturnIntegrationObjectItem() != null) ?
					attribute.getReturnIntegrationObjectItem().getCode().length() :
					-(integrationObjectCode.length() + 1), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(isUnique);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(autocreate);
			impexBuilder.append(ENDL);
		}
	}

	private void constructIntegrationObjectItemClassificationAttributeLine(final int longestClassificationAttributeLength,
	                                                                       final int longestClassAssignmentStringLength,
	                                                                       final IntegrationObjectItemModel item)
	{
		final String currentItemName = item.getCode();
		for (final IntegrationObjectItemClassificationAttributeModel classificationAttribute : item.getClassificationAttributes())
		{
			final ClassAttributeAssignmentModel classAttributeAssignmentModel = classificationAttribute.getClassAttributeAssignment();
			final String attributeName = classificationAttribute.getAttributeName();
			final String classAttributeAssignment = determineClassAssignment(classAttributeAssignmentModel);
			final String referenceType = (classAttributeAssignmentModel.getReferenceType() != null) ?
					(integrationObjectCode + ":" + classAttributeAssignmentModel.getReferenceType().getCode()) : "";
			final String autocreate = (BooleanUtils.isTrue(classificationAttribute.getAutoCreate())) ?
					classificationAttribute.getAutoCreate().toString() : "";
			constructIntegrationObjectItemSection(currentItemName);
			impexBuilder.append(attributeName);
			addWhitespace(longestClassificationAttributeLength, attributeName.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(classAttributeAssignment);
			addWhitespace(longestClassAssignmentStringLength, classAttributeAssignment.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(referenceType);
			addWhitespace(longestIntegrationObjectItemLength, referenceType.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(autocreate);
			impexBuilder.append(ENDL);
		}
	}

	private void constructIntegrationObjectItemVirtualAttributeLine(final IntegrationObjectItemModel item)
	{
		final String currentItemName = item.getCode();
		for (final IntegrationObjectItemVirtualAttributeModel virtualAttribute : item.getVirtualAttributes())
		{
			final String attributeName = virtualAttribute.getAttributeName();
			final IntegrationObjectVirtualAttributeDescriptorModel retrievalDescriptor = virtualAttribute.getRetrievalDescriptor();
			virtualAttributeDescriptors.add(retrievalDescriptor);
			constructIntegrationObjectItemSection(currentItemName);
			impexBuilder.append(attributeName);
			addWhitespace(longestVirtualAttributeLength, attributeName.length(), impexBuilder);
			impexBuilder.append(HEADER_TAB_DELIMITER);
			impexBuilder.append(retrievalDescriptor.getCode());
			impexBuilder.append(ENDL);
		}
	}

	private void constructIntegrationObjectItemSection(final String integrationObjectItemCode)
	{
		final String integrationObjectItem = integrationObjectCode + ":" + integrationObjectItemCode;
		impexBuilder.append("; ");
		impexBuilder.append(integrationObjectItem);
		addWhitespace(longestIntegrationObjectItemLength, integrationObjectItemCode.length(), impexBuilder);
		impexBuilder.append(HEADER_TAB_DELIMITER);
	}

	private void addWhitespace(final int longestStringLength, final int length, final StringBuilder impexBuilder)
	{
		int whitespaceNeeded = longestStringLength - length;
		while (whitespaceNeeded > 0)
		{
			impexBuilder.append(" ");
			whitespaceNeeded--;
		}
	}

	private int[] calculateLongestVirtualDescriptorStrings(
			final Set<IntegrationObjectVirtualAttributeDescriptorModel> virtualAttributeDescriptors)
	{
		int longestDescriptorCode = 0;
		int longestLogicLocation = 0;
		for (final IntegrationObjectVirtualAttributeDescriptorModel virtualAttributeDescriptor : virtualAttributeDescriptors)
		{
			final int descriptorCodeLength = virtualAttributeDescriptor.getCode().length();
			final int logicLocationLength = virtualAttributeDescriptor.getLogicLocation().length();
			if (descriptorCodeLength > longestDescriptorCode)
			{
				longestDescriptorCode = descriptorCodeLength;
			}
			if (logicLocationLength > longestLogicLocation)
			{
				longestLogicLocation = logicLocationLength;
			}
		}
		return new int[]{ longestDescriptorCode, longestLogicLocation };
	}

	private int calculateVirtualAttributeColumnLength(final Set<IntegrationObjectItemModel> integrationObjectItems)
	{
		int longestVirtualAttribute = 0;
		for (final IntegrationObjectItemModel integrationObjectItem : integrationObjectItems)
		{
			for (final IntegrationObjectItemVirtualAttributeModel virtualAttribute : integrationObjectItem.getVirtualAttributes())
			{
				if (virtualAttribute.getAttributeName().length() > longestVirtualAttribute)
				{
					longestVirtualAttribute = virtualAttribute.getAttributeName().length();
				}
			}
		}
		return longestVirtualAttribute;
	}

	private int calculateClassAssignmentColumnsLength(final Set<IntegrationObjectItemModel> items)
	{
		int longestClassAssignment = 0;
		for (final IntegrationObjectItemModel item : items)
		{
			for (final IntegrationObjectItemClassificationAttributeModel attr : item.getClassificationAttributes())
			{
				final ClassAttributeAssignmentModel classAttributeAssignmentModel = attr.getClassAttributeAssignment();
				final String classAttributeAssignment = determineClassAssignment(classAttributeAssignmentModel);
				if (classAttributeAssignment.length() > longestClassAssignment)
				{
					longestClassAssignment = classAttributeAssignment.length();
				}
			}
		}
		return longestClassAssignment;
	}

	private String determineClassAssignment(final ClassAttributeAssignmentModel classAttributeAssignmentModel)
	{
		final ClassificationAttributeModel classificationAttributeModel = classAttributeAssignmentModel.getClassificationAttribute();
		final ClassificationSystemVersionModel systemVersionModel = classificationAttributeModel.getSystemVersion();
		final String system = systemVersionModel.getCatalog().getId();
		final String version = systemVersionModel.getVersion();
		final String systemVersion = system + ":" + version;
		final String classCode = classAttributeAssignmentModel.getClassificationClass().getCode();
		final String attributeCode = classificationAttributeModel.getCode();
		return systemVersion + ":" + classCode + ":" + systemVersion + ":" + attributeCode;
	}

	private int calculateClassificationAttributeColumnsLength(final Set<IntegrationObjectItemModel> items)
	{
		int longestAttributeName = 0;
		for (final IntegrationObjectItemModel item : items)
		{
			for (final IntegrationObjectItemClassificationAttributeModel attr : item.getClassificationAttributes())
			{
				if (attr.getAttributeName().length() > longestAttributeName)
				{
					longestAttributeName = attr.getAttributeName().length();
				}
			}
		}
		return longestAttributeName;
	}

	private int[] calculateAttributeColumnsLength(final Set<IntegrationObjectItemModel> items)
	{
		int longestAttribute = 0;
		int longestDescriptor = 0;
		int longestReturnIO = 0;
		for (final IntegrationObjectItemModel item : items)
		{
			for (final IntegrationObjectItemAttributeModel attr : item.getAttributes())
			{
				longestAttribute = Math.max(longestAttribute, attr.getAttributeName().length());
				longestDescriptor = Math.max(longestDescriptor, (item.getCode().length() + attr.getAttributeName().length()));
				if (attr.getReturnIntegrationObjectItem() != null)
				{
					longestReturnIO = Math.max(longestReturnIO, attr.getReturnIntegrationObjectItem().getCode().length());
				}
			}
		}
		return new int[]{ longestAttribute, longestDescriptor, longestReturnIO };
	}

	private int[] calculateLongestStrings(final Set<IntegrationObjectItemModel> items)
	{
		int longestItemMatchType = 0;
		int longestItem = 0;
		for (final IntegrationObjectItemModel item : items)
		{
			longestItem = Math.max(longestItem, item.getCode().length());
			final String itemTypeMatchCode = item.getItemTypeMatch() != null ? item.getItemTypeMatch().getCode() : "";
			longestItemMatchType = Math.max(longestItemMatchType, itemTypeMatchCode.length());
		}
		return new int[]{ longestItem, longestItemMatchType };
	}
}
