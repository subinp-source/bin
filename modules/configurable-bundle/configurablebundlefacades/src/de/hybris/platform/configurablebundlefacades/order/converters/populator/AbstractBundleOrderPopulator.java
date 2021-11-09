/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract class for order converters for bundles. This class is responsible for sorting the order entries by the
 * bundle template they are assigned to.
 *
 * @param <SOURCE> class to populate from
 * @param <TARGET> class to populate to
 * @deprecated since 1905: The comparator compares only deprecated fields, so it is deprecated, too.
 */
@Deprecated(since = "1905", forRemoval = true)
public abstract class AbstractBundleOrderPopulator<SOURCE extends AbstractOrderModel, TARGET extends AbstractOrderData>
		extends AbstractOrderPopulator<SOURCE, TARGET>
{
	private BundleTemplateService bundleTemplateService;
	/**
	 * This method returns the given order entries sorted by the bundle number and component's position.
	 *
	 * @param entries
	 *           {@link List} of {@link AbstractOrderEntryModel}s to be sorted
	 * @return {@link List} of {@link AbstractOrderEntryModel}s ordered by the bundleNo and component.
	 */
	protected List<OrderEntryData> getSortedEntryListBasedOnBundleAndComponent(final List<OrderEntryData> entries)
	{
		Collections.sort(entries, new OrderComparator());
		return entries;
	}

	/**
	 * Arranges cart entries according to bundles.
	 */
	class OrderComparator implements Comparator<OrderEntryData> // NOSONAR
	{
		@Override
		public int compare(final OrderEntryData arg0, final OrderEntryData arg1)
		{
			return 0;
		}
	}

	protected BundleTemplateService getBundleTemplateService()
	{
		return bundleTemplateService;
	}

	@Required
	public void setBundleTemplateService(final BundleTemplateService bundleTemplateService)
	{
		this.bundleTemplateService = bundleTemplateService;
	}
}
