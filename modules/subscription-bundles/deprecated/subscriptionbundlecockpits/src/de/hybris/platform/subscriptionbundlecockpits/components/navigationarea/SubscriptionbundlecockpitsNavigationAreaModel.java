/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundlecockpits.components.navigationarea;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;

import de.hybris.platform.subscriptionbundlecockpits.session.impl.SubscriptionbundlecockpitsNavigationArea;


/**
 * Subscriptionbundlecockpits navigation area model.
 */
public class SubscriptionbundlecockpitsNavigationAreaModel extends DefaultNavigationAreaModel
{
	public SubscriptionbundlecockpitsNavigationAreaModel()
	{
		super();
	}

	public SubscriptionbundlecockpitsNavigationAreaModel(final AbstractUINavigationArea area)
	{
		super(area);
	}

	@Override
	public SubscriptionbundlecockpitsNavigationArea getNavigationArea()
	{
		return (SubscriptionbundlecockpitsNavigationArea) super.getNavigationArea();
	}
}
