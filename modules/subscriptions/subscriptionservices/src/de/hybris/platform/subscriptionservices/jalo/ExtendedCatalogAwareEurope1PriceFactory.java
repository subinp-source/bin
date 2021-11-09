/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.jalo;

import de.hybris.platform.catalog.jalo.CatalogAwareEurope1PriceFactory;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.user.User;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;


@SuppressWarnings("deprecation")
public class ExtendedCatalogAwareEurope1PriceFactory extends CatalogAwareEurope1PriceFactory implements ExtendedPriceFactory
{
	@Override
	public PriceRow getPriceRow(final AbstractOrderEntry entry) throws JaloPriceFactoryException
	{
		final SessionContext ctx = getSession().getSessionContext();
		final Product product = entry.getProduct();
		final AbstractOrder order = entry.getOrder(ctx);
		final Currency currency = order.getCurrency(ctx);
		final EnumerationValue ppg = getPPG(ctx, product);
		final User user = order.getUser();
		final EnumerationValue upg = getUPG(ctx, user);
		final Unit unit = entry.getUnit(ctx);
		final long quantity = entry.getQuantity(ctx).longValue();
		final boolean net = order.isNet().booleanValue();
		final Date date = order.getDate(ctx);
		final boolean giveAwayMode = entry.isGiveAway(ctx).booleanValue();

		return matchPriceRowForPrice(ctx, product, ppg, user, upg, quantity, unit, currency, date, net, giveAwayMode);
	}

	@Override
	public PriceRow getPriceRow(final Product product) throws JaloPriceFactoryException
	{
		final SessionContext ctx = getSession().getSessionContext();
		final User user = ctx.getUser();
		final Currency currency = ctx.getCurrency();
		final EnumerationValue ppg = getPPG(ctx, product);
		final EnumerationValue upg = getUPG(ctx, user);
		final boolean net = true;
		final Date date = new Date();

		final Collection<PriceRow> priceRows = filterPriceRows(matchPriceRowsForInfo(ctx, product, ppg, user, upg, currency, date,
				net));

		if (CollectionUtils.isNotEmpty(priceRows))
		{
			return priceRows.iterator().next();
		}
		else
		{
			return null;
		}
	}
}
