/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle.impl;

import de.hybris.platform.configurablebundleservices.bundle.AbstractBundleComponentEditableChecker;
import de.hybris.platform.configurablebundleservices.model.AutoPickBundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * Extended implementation of {@link AbstractBundleComponentEditableChecker}, which supports autopick components.
 */
public abstract class AutoPickBundleComponentEditableChecker<O extends AbstractOrderModel>
	extends DefaultAbstractBundleComponentEditableChecker<O>
{

	@Override
	public boolean isAutoPickComponent(final BundleTemplateModel bundleTemplate)
	{
		return (bundleTemplate != null && bundleTemplate.getBundleSelectionCriteria() instanceof AutoPickBundleSelectionCriteriaModel);
	}

}
