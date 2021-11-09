/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.GroupStatusChecker;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


public class GroupStatusCheckerImpl implements GroupStatusChecker
{

	@Override
	public boolean checkCompleteness(final UiGroupData group)
	{
		if (group != null && CollectionUtils.isNotEmpty(group.getCstics()))
		{
			return group.getCstics().stream().noneMatch(cstic -> cstic.isRequired() && checkEmpty(cstic));
		}
		return true;
	}

	@Override
	public boolean checkCompletenessForSubGroups(final List<UiGroupData> subGroups)
	{
		if (CollectionUtils.isNotEmpty(subGroups))
		{
			return subGroups.stream().noneMatch(subGroup -> !subGroup.isComplete());

		}
		return true;
	}

	@Override
	public boolean checkConsistency(final CsticGroup group)
	{
		if (group != null && CollectionUtils.isNotEmpty(group.getCstics()))
		{
			return group.getCstics().stream().noneMatch(cstic -> !cstic.isConsistent());
		}
		return true;
	}

	@Override
	public boolean checkConsistencyForSubGroups(final List<UiGroupData> subGroups)
	{
		if (CollectionUtils.isNotEmpty(subGroups))
		{
			return subGroups.stream().noneMatch(subGroup -> !subGroup.isConsistent());
		}
		return true;
	}

	protected boolean checkEmpty(final CsticData csticData)
	{
		final String value = csticData.getValue();
		if (UiType.CHECK_BOX_LIST == csticData.getType() || UiType.MULTI_SELECTION_IMAGE == csticData.getType())
		{
			for (final CsticValueData csticValue : csticData.getDomainvalues())
			{
				if (csticValue.isSelected())
				{
					return false;
				}
			}
			return true;
		}
		return StringUtils.isBlank(value);
	}

}
