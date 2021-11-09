/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.populator;

import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.exceptions.InvalidPspException;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.data.PaymentModeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Populates {@link PaymentModeModel} to {@link PaymentModeData}
 */
public class ChinesePaymentModePopulator implements Populator<PaymentModeModel, PaymentModeData>
{
	private static final Logger LOG = Logger.getLogger(ChinesePaymentModePopulator.class);

	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;

	@Override
	public void populate(final PaymentModeModel source, final PaymentModeData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			try
			{
				final ChinesePaymentService chinesePaymentService = getChinesePaymentServicesStrategy()
						.getPaymentService(source.getCode());
				target.setPspLogoUrl(Objects.nonNull(chinesePaymentService.getPspLogoUrl())
						? chinesePaymentService.getPspLogoUrl()
						: StringUtils.EMPTY);
			}
			catch (final InvalidPspException e)
			{
				LOG.error(e.getMessage());
				target.setPspLogoUrl(StringUtils.EMPTY);
			}
		}
	}

	protected ChinesePaymentServicesStrategy getChinesePaymentServicesStrategy()
	{
		return chinesePaymentServicesStrategy;
	}

	@Required
	public void setChinesePaymentServicesStrategy(final ChinesePaymentServicesStrategy chinesePaymentServicesStrategy)
	{
		this.chinesePaymentServicesStrategy = chinesePaymentServicesStrategy;
	}



}
