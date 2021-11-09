/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.cockpitng.labels.LabelService;


public class TestLabelService implements LabelService
{
	@Override
	public String getObjectLabel(final Object object)
	{
		return null;
	}

	@Override
	public String getObjectDescription(final Object object)
	{
		return null;
	}

	@Override
	public String getObjectIconPath(final Object object)
	{
		return null;
	}
}
