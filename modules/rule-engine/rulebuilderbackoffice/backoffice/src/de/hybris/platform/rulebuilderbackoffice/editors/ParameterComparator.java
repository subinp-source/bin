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

import org.apache.commons.lang.math.NumberUtils;


/**
 * Comparator for {@link ParameterModel}.
 *
 */
public class ParameterComparator implements Comparator<ParameterModel>
{
	@Override
	public int compare(final ParameterModel o1, final ParameterModel o2)
	{
		final Integer thisPriority = o1.getPriority() == null ? NumberUtils.INTEGER_ZERO : o1.getPriority();
		final Integer otherPriority = o2.getPriority() == null ? NumberUtils.INTEGER_ZERO : o2.getPriority();

		return otherPriority.compareTo(thisPriority);
	}
}
