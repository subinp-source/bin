/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;

import org.apache.log4j.Logger;


public class SubscriptionPricePlan extends GeneratedSubscriptionPricePlan
{
	private static final String PAYNOW = "paynow";
	private final static Logger LOG = Logger.getLogger(SubscriptionPricePlan.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// redeclaring the price attribute and setting it to optional="true" doesn't work 
		// we set the value of price here to satisfy de.hybris.platform.europe1.jalo.PriceRow l.131
		allAttributes.put("price", Double.valueOf(0));
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	@Override
	public void setPrice(final SessionContext ctx, final Double value)
	{
		LOG.warn("The attribute [price] is read-only");
	}

	/**
	 * Returns the price of the {@link OneTimeChargeEntry} with {@link BillingEvent} <code>"paynow"</code>.
	 * 
	 * @return the price of the one time charge for <code>"paynow"</code> or <code>0</code> if no such one time charge
	 *         exists
	 */
	@Override
	public Double getPrice()
	{
		Double price = Double.valueOf(0);

		if (this.getOneTimeChargeEntries() != null)
		{
			for (final OneTimeChargeEntry chargeEntry : this.getOneTimeChargeEntries())
			{
				if (PAYNOW.equals(chargeEntry.getBillingEvent().getCode()))
				{
					price = chargeEntry.getPrice();
					break;
				}
			}
		}

		return price;
	}

	@Override
	public Double getPrice(final SessionContext ctx)
	{
		return this.getPrice();
	}

}
