/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;


public class ProductInterestReversePopulator implements Populator<ProductInterestData, ProductInterestModel>
{
	private ProductService productService;
	private UserService userService;
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;

	@Override
	public void populate(final ProductInterestData source, final ProductInterestModel target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setBaseStore(getBaseStoreService().getCurrentBaseStore());
		target.setProduct(getProductService().getProductForCode(source.getProduct().getCode()));
		target.setCustomer((CustomerModel) getUserService().getCurrentUser());
		target.setBaseSite(getBaseSiteService().getCurrentBaseSite());
		target.setLanguage(getCommonI18NService().getCurrentLanguage());


		target.setNotificationType(source.getNotificationType());
		if (CollectionUtils.isNotEmpty(source.getNotificationChannels()))
		{
			target.setNotificationChannels(
					source.getNotificationChannels().stream().filter(c -> c.isEnabled()).map(c -> c.getChannel())
							.collect(Collectors.toSet()));
		}
	}


	protected ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

}
