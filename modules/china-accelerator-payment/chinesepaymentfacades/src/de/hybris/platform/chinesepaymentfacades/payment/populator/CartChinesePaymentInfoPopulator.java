/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentfacades.payment.populator;

import de.hybris.platform.chinesepaymentfacades.payment.data.ChinesePaymentInfoData;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.enums.ServiceType;
import de.hybris.platform.chinesepaymentservices.exceptions.InvalidPspException;
import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.PaymentModeService;

import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


/**
 * Populates {@link CartModel} to {@link CartData} and sets {@link ChinesePaymentInfoData}
 */
public class CartChinesePaymentInfoPopulator implements Populator<CartModel, CartData>
{

	private static final Logger LOG = Logger.getLogger(CartChinesePaymentInfoPopulator.class);

	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;
	private PaymentModeService paymentModeService;
	private EnumerationService enumerationService;
	private StoreSessionFacade storeSessionFacade;

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final ChinesePaymentInfoModel chinesePaymentInfomodel = source.getChinesePaymentInfo();
		if (chinesePaymentInfomodel != null)
		{
			final ChinesePaymentInfoData chinesePaymentInfoData = new ChinesePaymentInfoData();
			chinesePaymentInfoData.setId(chinesePaymentInfomodel.getCode());
			chinesePaymentInfoData.setPaymentProvider(chinesePaymentInfomodel.getPaymentProvider());
			final String iso = getStoreSessionFacade().getCurrentLanguage().getIsocode();
			final Locale currentLocale = new Locale(iso);
			final String serviceType = getEnumerationService()
					.getEnumerationName(ServiceType.valueOf(source.getChinesePaymentInfo().getServiceType().getCode()), currentLocale);
			chinesePaymentInfoData.setServiceType(serviceType);

			final String paymentProviderName = getPaymentModeService()
					.getPaymentModeForCode(source.getChinesePaymentInfo().getPaymentProvider()).getName();
			chinesePaymentInfoData.setPaymentProviderName(paymentProviderName);

			try
			{
				final ChinesePaymentService chinesePaymentService = getChinesePaymentServicesStrategy()
						.getPaymentService(chinesePaymentInfomodel.getPaymentProvider());
				chinesePaymentInfoData.setPaymentProviderLogo(
						Objects.nonNull(chinesePaymentService.getPspLogoUrl())
								? chinesePaymentService.getPspLogoUrl()
								: StringUtils.EMPTY);
			}
			catch (final InvalidPspException e)
			{
				LOG.error(e.getMessage());
				chinesePaymentInfoData.setPaymentProviderLogo(StringUtils.EMPTY);
			}
			target.setChinesePaymentInfo(chinesePaymentInfoData);
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

	protected PaymentModeService getPaymentModeService()
	{
		return paymentModeService;
	}

	@Required
	public void setPaymentModeService(final PaymentModeService paymentModeService)
	{
		this.paymentModeService = paymentModeService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

}
