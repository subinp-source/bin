/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionenginefacades.product.converters.populator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.Assert;


/**
 * Populate the timedAccess for product data.
 */
public class TimedAccessProductPopulator implements Populator<ProductModel, ProductData>
{
	private final PromotionsService promotionsService;
	private final TimeService timeService;
	private final BaseSiteService baseSiteService;
	private final FlashBuyService flashBuyService;
	private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;

	public TimedAccessProductPopulator(final PromotionsService promotionsService, final TimeService timeService,
			final BaseSiteService baseSiteService, final FlashBuyService flashBuyService,
			final Converter<AbstractPromotionModel, PromotionData> promotionsConverter)
	{
		this.promotionsService = promotionsService;
		this.timeService = timeService;
		this.baseSiteService = baseSiteService;
		this.flashBuyService = flashBuyService;
		this.promotionsConverter = promotionsConverter;
	}

	@Override
	public void populate(final ProductModel productModel, final ProductData productData)
	{
		Assert.notNull(productModel, "Parameter source cannot be null.");
		Assert.notNull(productData, "Parameter target cannot be null.");

		final List<PromotionData> promotions = new ArrayList<>();
		final List<PromotionData> flashBuyPromotions = new ArrayList<>();

		final BaseSiteModel baseSiteModel = getBaseSiteService().getCurrentBaseSite();

		if (baseSiteModel != null)
		{
			final PromotionGroupModel defaultPromotionGroup = baseSiteModel.getDefaultPromotionGroup();
			final Date currentTimeRoundedToMinute = DateUtils.round(getTimeService().getCurrentTime(), Calendar.MINUTE);

			if (defaultPromotionGroup != null)
			{
				promotions.addAll(getPromotionsConverter().convertAll(getPromotionsService().getAbstractProductPromotions(
						Collections.singletonList(defaultPromotionGroup), productModel, true, currentTimeRoundedToMinute)));
			}
		}

		if (CollectionUtils.isNotEmpty(promotions))
		{

			promotions.forEach(p -> getFlashBuyService().getFlashBuyCouponByPromotionCode(p.getCode()).ifPresent(coupon -> {
				if (coupon.getActive())
				{
					flashBuyPromotions.add(p);
				}
			}));

		}

		if (CollectionUtils.isNotEmpty(flashBuyPromotions))
		{
			productData.setTimedAccessPromotion(flashBuyPromotions.get(0));
		}

	}

	protected PromotionsService getPromotionsService()
	{
		return promotionsService;
	}

	protected TimeService getTimeService()
	{
		return timeService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	protected FlashBuyService getFlashBuyService()
	{
		return flashBuyService;
	}

	protected Converter<AbstractPromotionModel, PromotionData> getPromotionsConverter()
	{
		return promotionsConverter;
	}

	public void setPromotionsConverter(final Converter<AbstractPromotionModel, PromotionData> promotionsConverter)
	{
		this.promotionsConverter = promotionsConverter;
	}

}
