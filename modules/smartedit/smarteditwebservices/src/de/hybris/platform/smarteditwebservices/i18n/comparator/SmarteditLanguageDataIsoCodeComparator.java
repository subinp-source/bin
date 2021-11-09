/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditwebservices.i18n.comparator;

import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Required;


/**
 * Compare {@link SmarteditLanguageData} by isocode
 */
public class SmarteditLanguageDataIsoCodeComparator implements Comparator<SmarteditLanguageData>
{
	private boolean nullIsLess;

	@Override
	public int compare(final SmarteditLanguageData left, final SmarteditLanguageData right)
	{
		return compare(left.getIsoCode(), right.getIsoCode(), isNullIsLess());
	}

	protected int compare(final String left, final String right, final boolean nullIsLess)
	{
		if (left == null)
		{
			return nullIsLess ? -1 : 1;
		}
		if (right == null)
		{
			return nullIsLess ? 1 : -1;
		}
		return left.compareTo(right);
	}

	protected boolean isNullIsLess()
	{
		return nullIsLess;
	}

	@Required
	public void setNullIsLess(final boolean nullIsLess)
	{
		this.nullIsLess = nullIsLess;
	}

}
