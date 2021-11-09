/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company.refactoring.impl;

import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;


public final class B2BCompanyUtils
{
	public static B2BSelectionData createB2BSelectionData(final String code, final boolean selected, final boolean active)
	{
		final B2BSelectionData b2BSelectionData = new B2BSelectionData();
		b2BSelectionData.setId(code);
		b2BSelectionData.setNormalizedCode(code == null ? null : code.replaceAll("\\W", "_"));
		b2BSelectionData.setSelected(selected);
		b2BSelectionData.setActive(active);
		return b2BSelectionData;
	}
}
