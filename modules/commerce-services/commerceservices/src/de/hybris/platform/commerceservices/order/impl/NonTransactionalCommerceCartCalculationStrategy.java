/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

 import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;


/**
 * Non-transactional strategy to calculate the cart when not in checkout status.
 */
public class NonTransactionalCommerceCartCalculationStrategy extends DefaultCommerceCartCalculationStrategy
{
	@Override
	public boolean calculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();

 		validateParameterNotNull(cartModel, "Cart model cannot be null");

 		boolean recalculated = false;

 		if (getCalculationService().requiresCalculation(cartModel))
		{
			try
			{
				parameter.setRecalculate(false);
				beforeCalculate(parameter);
				getCalculationService().calculate(cartModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
						AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
			}
			catch (final CalculationException calculationException)
			{
				throw new IllegalStateException(
						"Cart model " + cartModel.getCode() + " was not calculated due to: " + calculationException.getMessage(),
						calculationException);
			}
			finally
			{
				afterCalculate(parameter);
			}
			recalculated = true;

 			if (isCalculateExternalTaxes())
			{
				getExternalTaxesService().calculateExternalTaxes(cartModel);
			}
		}

 		return recalculated;
	}

 	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter)
	{
		final CartModel cartModel = parameter.getCart();
		try
		{
			parameter.setRecalculate(true);
			beforeCalculate(parameter);
			getCalculationService().recalculate(cartModel);
			getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
					AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
		}
		catch (final CalculationException calculationException)
		{
			throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ", cartModel.getCode(),
					calculationException.getMessage()), calculationException);
		}
		finally
		{
			afterCalculate(parameter);
		}
		return true;
	}
}