/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.rulebuilderbackoffice.editors;

import java.util.Comparator;


/**
 * Comparator for {@link ValidationInfoModel}.
 *
 */
public class ValidationInfoComparator implements Comparator<ValidationInfoModel>
{
	@Override
	public int compare(final ValidationInfoModel o1, final ValidationInfoModel o2)

	{
		final ValidationInfoSeverity thisSeverity = o1.getSeverity() == null ? ValidationInfoSeverity.NONE : o1.getSeverity();
		final ValidationInfoSeverity otherSeverity = o2.getSeverity() == null ? ValidationInfoSeverity.NONE : o2.getSeverity();

		return thisSeverity.compareTo(otherSeverity);
	}
}
