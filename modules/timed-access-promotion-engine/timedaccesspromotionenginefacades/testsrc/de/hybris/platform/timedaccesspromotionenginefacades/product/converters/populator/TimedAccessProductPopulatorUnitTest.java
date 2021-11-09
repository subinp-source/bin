/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.timedaccesspromotionenginefacades.product.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.timedaccesspromotionengineservices.FlashBuyService;
import de.hybris.platform.timedaccesspromotionengineservices.model.FlashBuyCouponModel;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TimedAccessProductPopulator}
 */
@UnitTest
public class TimedAccessProductPopulatorUnitTest
{
	@Mock
	private PromotionsService promotionsService;
	@Mock
	private TimeService timeService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private FlashBuyService flashBuyService;
	@Mock
	private FlashBuyCouponModel coupon;
	@Mock
	private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;
	
	private TimedAccessProductPopulator timedAccessProductPopulator;

	private static final String COUPON_CODE = "couponId";
	private static final String PROMOTION_CODE = "promotionCode";
	final ProductModel source = mock(ProductModel.class);
	final ProductData result = new ProductData();
	private Optional<FlashBuyCouponModel> couponOptional;
	private Collection<PromotionData> promotions;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		timedAccessProductPopulator = new TimedAccessProductPopulator(promotionsService, timeService, baseSiteService,
				flashBuyService, promotionsConverter);

		couponOptional = Optional.of(coupon);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulatorWithNullSource()
	{
		timedAccessProductPopulator.populate(null, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulatorWithNullTarget()
	{
		timedAccessProductPopulator.populate(source, null);
	}

	@Test
	public void testPopulate()
	{

		final PromotionGroupModel defaultPromotionGroup = mock(PromotionGroupModel.class);
		final Date currentDate = DateUtils.round(new Date(), Calendar.MINUTE);
		final AbstractPromotionModel abstractPromotionModel = mock(AbstractPromotionModel.class);
		final PromotionData promotionData = mock(PromotionData.class);
		final BaseSiteModel baseSiteModel = mock(BaseSiteModel.class);
		given(timeService.getCurrentTime()).willReturn(currentDate);
		given(baseSiteService.getCurrentBaseSite()).willReturn(baseSiteModel);
		given(baseSiteModel.getDefaultPromotionGroup()).willReturn(defaultPromotionGroup);
		given(promotionsConverter.convertAll(Collections.singletonList(abstractPromotionModel)))
				.willReturn(Collections.singletonList(promotionData));
		Mockito
				.<List<? extends AbstractPromotionModel>> when(promotionsService
						.getAbstractProductPromotions(Collections.singletonList(defaultPromotionGroup), source, true, currentDate))
				.thenReturn(Collections.singletonList(abstractPromotionModel));

		Mockito.when(coupon.getCouponId()).thenReturn(COUPON_CODE);
		Mockito.when(abstractPromotionModel.getCode()).thenReturn(PROMOTION_CODE);
		Mockito.when(promotionData.getCode()).thenReturn(PROMOTION_CODE);
		Mockito.when(flashBuyService.getFlashBuyCouponByPromotionCode(PROMOTION_CODE)).thenReturn(couponOptional);
		Mockito.when(coupon.getActive()).thenReturn(Boolean.TRUE);

		timedAccessProductPopulator.populate(source, result);
		Assert.assertEquals(result.getTimedAccessPromotion().getCode(), promotionData.getCode());
	}
}
