/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.AbstractOrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Modify the cart converter to show the first invalid bundle component in the cart (if it exists).
 *
 * @since 6.4
 */
public class BundleCommerceCartPopulator<S extends CartModel, T extends CartData> extends AbstractOrderPopulator<S, T>
{
	private static final Logger LOG = Logger.getLogger(BundleCommerceCartPopulator.class);
	private BundleTemplateService bundleTemplateService;
	private Converter<BundleTemplateModel, BundleTemplateData> bundleTemplateConverter;

	/**
	 * Modify populate method to set the first incomplete bundle component in the cart {@link CartModel}
	 */
	@Override
	public void populate(final S source, final T target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (target.getEntries() == null || target.getEntries().isEmpty()) {
			addEntries(source, target);
		}
		setFirstIncompleteComponent(source, target);
		addPromotions(source, target);
	}

	/**
	 * Find first incomplete bundle component of each package
	 */
	protected void setFirstIncompleteComponent(final S source, final T target)
	{
		final Map<Integer, BundleTemplateData> firstInvalids = new HashMap<>();
		target.getEntries().stream()
				.filter(entry -> !entry.isValid())
				.findAny()
				.ifPresent(entry -> {
					final EntryGroup entryGroup = getEntryGroup(source, entry);
					if(entryGroup != null)
					{
   					final BundleTemplateModel bundleTemplate = getBundleTemplateService().getBundleTemplateForCode(
   							entryGroup.getExternalReferenceId());
   					final BundleTemplateData bundleTemplateData = getBundleTemplateConverter().convert(bundleTemplate);
   					firstInvalids.put(entryGroup.getGroupNumber(), bundleTemplateData);
					}
				});
		target.setFirstIncompleteBundleComponentsMap(firstInvalids);
	}
	
	/**
	 * Find first incomplete bundle component of each package
	 */
	protected EntryGroup getEntryGroup(final S source, final OrderEntryData entry)
	{
		try
		{
			return getEntryGroupService().getGroupOfType(source, entry.getEntryGroupNumbers(), GroupType.CONFIGURABLEBUNDLE);
		}
		catch (final IllegalArgumentException e)
		{
			LOG.info("Group of type CONFIGURABLEBUNDLE is not found");
			return null;
		}
	}

	@Override
	protected void addPromotions(final AbstractOrderModel source, final AbstractOrderData target)
	{
		addPromotions(source, getPromotionsService().getPromotionResults(source), target);
	}

	@Override
	protected void addPromotions(final AbstractOrderModel source, final PromotionOrderResults promoOrderResults,
			final AbstractOrderData target)
	{
		if (promoOrderResults != null)
		{
			final CartData cartData = (CartData) target;
			cartData.setPotentialOrderPromotions(getPromotions(promoOrderResults.getPotentialOrderPromotions()));
			cartData.setPotentialProductPromotions(getPromotions(promoOrderResults.getPotentialProductPromotions()));
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
	
	protected Converter<BundleTemplateModel, BundleTemplateData> getBundleTemplateConverter()
	{
		return bundleTemplateConverter;
	}

	@Required
	public void setBundleTemplateConverter(final Converter<BundleTemplateModel, BundleTemplateData> bundleTemplateConverter)
	{
		this.bundleTemplateConverter = bundleTemplateConverter;
	}
}
