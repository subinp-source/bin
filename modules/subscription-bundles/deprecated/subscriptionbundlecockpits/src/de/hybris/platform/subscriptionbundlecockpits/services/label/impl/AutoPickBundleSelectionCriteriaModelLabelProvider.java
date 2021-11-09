/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionbundlecockpits.services.label.impl;

import de.hybris.platform.configurablebundlecockpits.services.label.impl.BundleSelectionCriteriaModelLabelProvider;
import de.hybris.platform.configurablebundleservices.model.AutoPickBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;

/**
 * Label provider implementation for {@link BundleSelectionCriteriaModel} and sub-types
 */
public class AutoPickBundleSelectionCriteriaModelLabelProvider extends BundleSelectionCriteriaModelLabelProvider
{
	@Override
	protected String getItemLabel(final BundleSelectionCriteriaModel selectionCriteria)
	{
		String label = "";
		if (selectionCriteria instanceof AutoPickBundleSelectionCriteriaModel)
		{
			label = getL10NService().getLocalizedString("cockpit.bundleselection.automaticpicked");
		}
		else
		{
			super.getItemLabel(selectionCriteria);
		}

		return label;
	}
}
