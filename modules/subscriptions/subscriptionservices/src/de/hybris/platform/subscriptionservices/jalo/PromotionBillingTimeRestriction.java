/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.AbstractPromotionRestriction;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;


public class PromotionBillingTimeRestriction extends GeneratedPromotionBillingTimeRestriction
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(PromotionBillingTimeRestriction.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	@Override
	public RestrictionResult evaluate(final SessionContext ctx, final Collection<Product> products, final Date date,
			final AbstractOrder abstractOrder)
	{
		BillingTime billingTime = null;

		if (abstractOrder != null)
		{
			billingTime = SubscriptionservicesManager.getInstance().getBillingTime(ctx, abstractOrder);
		}

		final boolean positive = isPositiveAsPrimitive(ctx);

		if ((billingTime != null) && (isInBillingTimeCollection(ctx, billingTime)))
		{
			return positive ? AbstractPromotionRestriction.RestrictionResult.ALLOW
					: AbstractPromotionRestriction.RestrictionResult.DENY;
		}
		return positive ? AbstractPromotionRestriction.RestrictionResult.DENY
				: AbstractPromotionRestriction.RestrictionResult.ALLOW;

	}

	protected boolean isInBillingTimeCollection(final SessionContext ctx, final BillingTime orderbillingTime)
	{
		final Collection<BillingTime> restrictionbillingTimes = getBillingTimes(ctx);

		if (CollectionUtils.isNotEmpty(restrictionbillingTimes) && restrictionbillingTimes.contains(orderbillingTime))
		{
			return true;
		}

		return false;
	}

	@Override
	protected Object[] getDescriptionPatternArguments(final SessionContext ctx)
	{
		return new Object[]
		{ getRestrictionType(ctx), Integer.valueOf(isPositiveAsPrimitive(ctx) ? 1 : 0), getBillingTimeNames(ctx) };
	}

	/**
	 * Concatenate the names of the restricted billing times
	 *
	 * @param ctx
	 *           {@link SessionContext}
	 * @return Concatenated billing time names
	 */
	public String getBillingTimeNames(final SessionContext ctx)
	{
		final StringBuilder billingTimeNames = new StringBuilder();
		final Collection<BillingTime> billingTimes = getBillingTimes(ctx);

		if (CollectionUtils.isNotEmpty(billingTimes))
		{
			for (final BillingTime billingTime : billingTimes)
			{
				if (billingTimeNames.length() > 0)
				{
					billingTimeNames.append(", ");
				}

				billingTimeNames.append(billingTime.getNameInCart(ctx));
			}
		}

		return billingTimeNames.toString();
	}

}
