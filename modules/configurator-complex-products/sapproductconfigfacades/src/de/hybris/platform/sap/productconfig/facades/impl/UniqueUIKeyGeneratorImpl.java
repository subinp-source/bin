/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.UniqueUIKeyGenerator;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticQualifier;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;


/**
 * Default implementation of the {@link UniqueUIKeyGenerator}.<br>
 * Re-Uses a single StringBuilder instance per thread to save heap memory. This class gets called quite often within a
 * single requests, up to a few million times for large models.
 */
public class UniqueUIKeyGeneratorImpl implements UniqueUIKeyGenerator
{
	/**
	 * Separator for UIKey components, such as Instance, group and cstic key.
	 *
	 * @deprecated Use methods of this class directly, to split/build keys
	 */
	@Deprecated(since = "20.05", forRemoval = true)
	public static final String KEY_SEPARATOR = "@";

	/**
	 * Instance key consists of name and id, which are separated by this separator.
	 * 
	 * @deprecated Use methods of this class directly, to split/build keys
	 */
	@Deprecated(since = "20.05", forRemoval = true)
	public static final String INSTANCE_SEPERATOR = "-";


	/**
	 * Separator for UIKey components, such as Instance, group and cstic key.
	 */
	private static final String KEY_SEPARATOR_INTERNAL = "@";
	/**
	 * to use the key separator in a split statement, we have to escape it.
	 */
	private static final String KEY_SEPARATOR_SPLIT = "\\" + KEY_SEPARATOR_INTERNAL;

	/**
	 * Instance key consists of name and id, which are separated by this separator.
	 */
	private static final String INSTANCE_SEPARATOR_INTERNAL = "-";

	private static final ThreadLocal<StringBuilder> keyBuilder = ThreadLocal.withInitial(UniqueUIKeyGeneratorImpl::initialValue);
	public static final int MINIMUM_STRING_BUILDER_CAPACITY = 128;
	public static final int STRING_BUILDER_CAPACITY_THRESHOLD = 1024;
	public static final int EXPECTED_GROUP_PARTS = 2;
	public static final int EXPECTED_INSTANCE_PARTS = 2;

	protected static StringBuilder initialValue()
	{
		return new StringBuilder(MINIMUM_STRING_BUILDER_CAPACITY);
	}

	@Override
	public String generateGroupIdForInstance(final InstanceModel instance)
	{
		return generateGroupIdForGroup(instance, null);
	}


	@Override
	public String generateGroupIdForGroup(final InstanceModel instance, final CsticGroup csticModelGroup)
	{
		final StringBuilder strBuilder = getStrBuilder();
		strBuilder.append(instance.getId());
		strBuilder.append(this.getInstanceSeparator());
		strBuilder.append(instance.getName());
		if (csticModelGroup != null)
		{
			strBuilder.append(this.getKeySeparator());
			strBuilder.append(csticModelGroup.getName());
		}
		return strBuilder.toString();
	}


	@Override
	public String retrieveInstanceId(final String uiGroupId)
	{
		return uiGroupId.substring(0, uiGroupId.indexOf(this.getInstanceSeparator()));
	}


	@Override
	public String generateCsticId(final CsticModel model, final CsticValueModel value, final String prefix)
	{
		final StringBuilder strBuilder = getStrBuilder();
		strBuilder.append(prefix);
		strBuilder.append(this.getKeySeparator());
		strBuilder.append(model.getName());
		if (value != null)
		{
			strBuilder.append(this.getKeySeparator());
			strBuilder.append(value.getName());
		}

		return strBuilder.toString();
	}

	protected StringBuilder getStrBuilder()
	{
		final StringBuilder strBuilder = keyBuilder.get();
		strBuilder.setLength(0);
		if (strBuilder.capacity() > STRING_BUILDER_CAPACITY_THRESHOLD)
		{
			strBuilder.trimToSize();
			strBuilder.ensureCapacity(MINIMUM_STRING_BUILDER_CAPACITY);
		}
		return strBuilder;
	}


	@Override
	public CsticQualifier splitId(final String csticUiKey)
	{
		final String[] splitInstanceGroup = csticUiKey.split(this.getKeySeparatorSplit(), 2);
		final String[] splittedInstancePart = splitInstanceGroup[0].split(this.getInstanceSeparator(), 2);
		final String[] splittedGroupPart = splitInstanceGroup[1].split(this.getKeySeparatorSplit());
		if (splittedGroupPart.length != EXPECTED_GROUP_PARTS || splittedInstancePart.length != EXPECTED_INSTANCE_PARTS)
		{
			throw new IllegalArgumentException(
					"provided cstic ui key '" + csticUiKey + "' is not of form 'instancedId-InstanceName.groupName.csticName");
		}
		final CsticQualifier csticQualifier = new CsticQualifier();
		csticQualifier.setInstanceId(splittedInstancePart[0]);
		csticQualifier.setInstanceName(splittedInstancePart[1]);
		csticQualifier.setGroupName(splittedGroupPart[0]);
		csticQualifier.setCsticName(splittedGroupPart[1]);
		return csticQualifier;
	}


	@Override
	public String generateId(final CsticQualifier csticQualifier)
	{
		final StringBuilder strBuilder = getStrBuilder();
		strBuilder.append(csticQualifier.getInstanceId());
		strBuilder.append(this.getInstanceSeparator());
		strBuilder.append(csticQualifier.getInstanceName());
		strBuilder.append(this.getKeySeparator());
		strBuilder.append(csticQualifier.getGroupName());
		strBuilder.append(this.getKeySeparator());
		strBuilder.append(csticQualifier.getCsticName());
		return strBuilder.toString();
	}

	@Override
	public String extractInstanceNameFromGroupId(final String groupId)
	{
		final int instanceSeparatorIndex = groupId.indexOf(this.getInstanceSeparator());
		int keySeparatorIndex = groupId.indexOf(this.getKeySeparator());
		if (instanceSeparatorIndex != -1)
		{
			if (keySeparatorIndex == -1)
			{
				keySeparatorIndex = groupId.length();
			}
			return groupId.substring(instanceSeparatorIndex + 1, keySeparatorIndex);

		}
		return null;
	}

	@Override
	public String getInstanceSeparator()
	{
		return INSTANCE_SEPARATOR_INTERNAL;
	}

	@Override
	public String getKeySeparator()
	{
		return KEY_SEPARATOR_INTERNAL;
	}

	@Override
	public String getKeySeparatorSplit()
	{
		return KEY_SEPARATOR_SPLIT;
	}

}
