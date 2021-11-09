/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundleservices.bundle;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;

/**
 * Checks if a bundle component ({@link BundleTemplateModel}) can be edited
 */
public interface AbstractBundleComponentEditableChecker<O extends AbstractOrderModel>
{

	/**
	 * Checks if the selection dependency of the given component <code>bundleTemplate</code> in given bundle
	 * <code>bundleTemplate</code> and <code>order</code> is fulfilled. Returns the result of that check instead
	 * of throwing an exception.
	 *
	 * @param order
	 *           the order specified bundle is a part of
	 * @param bundleTemplate
	 *           the component to check selection dependency for
	 * @param entryGroupNumber
	 *           number of the entry group created from bundle template
	 */
	boolean isRequiredDependencyMet(@Nonnull final O order, @Nonnull final BundleTemplateModel bundleTemplate,
			@Nonnull final Integer entryGroupNumber);
	
	/**
	 * Checks if {@link BundleTemplateModel} has a selection criteria of auto pick type.
	 * 
	 * @param bundleTemplate
	 * @return <code>true</code> if selection criteria has auto pick type, otherwise
	 *         <code>false</code>
	 * @deprecated since 1811 - auto pick components were moved to subscriptionbundles
	 */
	@Deprecated(since = "1811", forRemoval = true)
	boolean isAutoPickComponent(@Nullable final BundleTemplateModel bundleTemplate);
}
