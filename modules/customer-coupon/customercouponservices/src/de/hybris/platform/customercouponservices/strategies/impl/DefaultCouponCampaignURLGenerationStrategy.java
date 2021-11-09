/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customercouponservices.strategies.impl;

import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.customercouponservices.strategies.CouponCampaignURLGenerationStrategy;
import de.hybris.platform.util.Config;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;


/**
 * Default implementation of {@link CouponCampaignURLGenerationStrategy}
 *
 * @deprecated Since 2005        
 */
@Deprecated(since = "2005", forRemoval= true )
public class DefaultCouponCampaignURLGenerationStrategy implements CouponCampaignURLGenerationStrategy
{

	private static final String URL_PREFIX_KEY = "coupon.claiming.url.prefix";

	@Override
	public String generate(final CustomerCouponModel coupon)
	{
		if (Objects.isNull(coupon))
		{
			return StringUtils.EMPTY;
		}

		final String urlPrefix = getUrlPrefix();
		final String couponId = coupon.getCouponId();
		if (StringUtils.isEmpty(couponId) || StringUtils.isEmpty(urlPrefix))
		{
			return StringUtils.EMPTY;
		}

		return urlPrefix + couponId;
	}

	protected String getUrlPrefix()
	{
		return Config.getString(URL_PREFIX_KEY, StringUtils.EMPTY);
	}
}
