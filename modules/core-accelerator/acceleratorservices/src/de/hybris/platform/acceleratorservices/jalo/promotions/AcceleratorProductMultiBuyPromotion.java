/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.jalo.promotions;


import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.ProductMultiBuyPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class AcceleratorProductMultiBuyPromotion extends GeneratedAcceleratorProductMultiBuyPromotion
{
	private static final Logger LOG = Logger.getLogger(AcceleratorProductMultiBuyPromotion.class);

	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{

		final List<PromotionResult> promotionResults = new ArrayList<PromotionResult>();
		final PromotionsManager.RestrictionSetResult restrictRes = findEligibleProductsInBasket(ctx, promoContext);
		final List<Product> products = restrictRes.getAllowedProducts();

		if (restrictRes.isAllowedToContinue() && !restrictRes.getAllowedProducts().isEmpty())
		{
			final Double promotionPriceValue = this.getPriceForOrder(ctx, this.getBundlePrices(ctx), promoContext.getOrder(),
					ProductMultiBuyPromotion.BUNDLEPRICES);
			if (promotionPriceValue != null)
			{
				final int triggerSize = this.getQualifyingCount(ctx).intValue();
				final PromotionOrderView pov = promoContext.createView(ctx, this, products);

				// Keep consuming items until exhausted
				if (pov.getTotalQuantity(ctx) >= triggerSize)
				{
					promotionResults.add(consumeItems(ctx, promoContext, promotionPriceValue, triggerSize, pov));
				}

				// Check for partial firing
				final long remaining = pov.getTotalQuantity(ctx);
				if (remaining > 0)
				{
					promoContext.startLoggingConsumed(this);
					pov.consume(ctx, remaining);

					final BigDecimal certainty = BigDecimal.valueOf(remaining).divide(new BigDecimal(triggerSize), 10,
							RoundingMode.HALF_UP);
					final PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, this,
							promoContext.getOrder(), certainty.floatValue());
					currResult.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed(this, false));
					promotionResults.add(currResult);
				}
			}
		}

		return promotionResults;
	}

	protected PromotionResult consumeItems(final SessionContext ctx, final PromotionEvaluationContext promoContext,
			final Double promotionPriceValue, final int triggerSize, final PromotionOrderView pov)
	{
		final long allQualifyingCount = pov.getTotalQuantity(ctx) / triggerSize;

		promoContext.startLoggingConsumed(this);
		pov.consumeFromHead(ctx, PromotionEvaluationContext.createPriceComparator(ctx), allQualifyingCount * triggerSize);

		// Now work out the discount
		double thisPromoTotal = 0.0D;
		final List<PromotionOrderEntryConsumed> consumedEntries = promoContext.finishLoggingAndGetConsumed(this, true);
		if (consumedEntries != null && !consumedEntries.isEmpty())
		{
			for (final PromotionOrderEntryConsumed poec : consumedEntries)
			{
				thisPromoTotal += poec.getEntryPrice(ctx);
			}
		}

		final double adjustment = promotionPriceValue.doubleValue() * allQualifyingCount - thisPromoTotal;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("(" + getPK() + ") evaluate: totalValueOfConsumedEntries=[" + thisPromoTotal + "] promotionPriceValue=["
					+ promotionPriceValue + "] adjustment=[" + adjustment + "]");
		}

		Helper.adjustUnitPrices(ctx, promoContext, consumedEntries, promotionPriceValue.doubleValue(), thisPromoTotal);

		final PromotionResult currResult = PromotionsManager.getInstance().createPromotionResult(ctx, this, promoContext.getOrder(),
				1.0F);
		currResult.setConsumedEntries(ctx, consumedEntries);
		currResult.addAction(ctx, PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, adjustment));
		return currResult;
	}
}
