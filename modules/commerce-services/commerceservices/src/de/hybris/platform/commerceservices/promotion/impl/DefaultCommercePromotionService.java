/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.promotion.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.commerceservices.promotion.CommercePromotionService;
import de.hybris.platform.commerceservices.promotion.dao.CommercePromotionDao;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;

/**
 * Default implementation of {@link CommercePromotionService}
 */
public class DefaultCommercePromotionService implements CommercePromotionService
{
	private CommercePromotionDao commercePromotionDao;

	@Override
	public AbstractPromotionModel getPromotion(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<AbstractPromotionModel> promotions = getCommercePromotionDao().findPromotionForCode(code);

		validateIfSingleResult(promotions, format("Promotion with code '%s' not found!", code),
				format("Promotion code '%s' is not unique, %d promotion found!", code, Integer.valueOf(promotions.size())));

		return promotions.get(0);
	}

	@Override
	public List<ProductPromotionModel> getProductPromotions()
	{
		return getCommercePromotionDao().findProductPromotions();
	}

	@Override
	public List<OrderPromotionModel> getOrderPromotions()
	{
		return getCommercePromotionDao().findOrderPromotions();
	}

	@Override
	public List<ProductPromotionModel> getProductPromotions(final Collection<PromotionGroupModel> promotionGroups)
	{
		validateParameterNotNull(promotionGroups, "Parameter promotionGroups must not be null");
		return getCommercePromotionDao().findProductPromotions(promotionGroups);
	}

	@Override
	public List<OrderPromotionModel> getOrderPromotions(final Collection<PromotionGroupModel> promotionGroups)
	{
		validateParameterNotNull(promotionGroups, "Parameter promotionGroups must not be null");
		return getCommercePromotionDao().findOrderPromotions(promotionGroups);
	}

	protected CommercePromotionDao getCommercePromotionDao()
	{
		return commercePromotionDao;
	}

	@Required
	public void setCommercePromotionDao(final CommercePromotionDao commercePromotionDao)
	{
		this.commercePromotionDao = commercePromotionDao;
	}
}
