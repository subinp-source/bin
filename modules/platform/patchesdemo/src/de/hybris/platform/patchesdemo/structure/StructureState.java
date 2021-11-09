/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.patchesdemo.structure;

/**
 * Enumeration that defines structure state; it may be used in different objects to indicate for which milestone given
 * object was introduced.
 */
public enum StructureState implements de.hybris.platform.patches.organisation.StructureState
{
	V1, V2, V3, LAST;

	@Override
	public boolean isAfter(final de.hybris.platform.patches.organisation.StructureState structureState)
	{
		if (this == structureState)
		{
			return false;
		}
		for (final de.hybris.platform.patches.organisation.StructureState iterateValue : values())
		{
			if (structureState.equals(iterateValue))
			{
				return true;
			}
			if (this.equals(iterateValue))
			{
				return false;
			}
		}
		return false;
	}
}
