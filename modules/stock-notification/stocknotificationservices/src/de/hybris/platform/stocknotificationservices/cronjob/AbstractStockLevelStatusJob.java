/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.stocknotificationservices.cronjob;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.customerinterestsservices.productinterest.daos.ProductInterestDao;
import de.hybris.platform.notificationservices.cronjob.AbstractNotificationJob;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stocknotificationservices.constants.StocknotificationservicesConstants;
import de.hybris.platform.stocknotificationservices.dao.BackInStockProductInterestDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.localization.Localization;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Abstract base class for sending BACK_IN_STOCK notification to customer
 */
public abstract class AbstractStockLevelStatusJob extends AbstractNotificationJob
{
	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private StockService stockService;
	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private WarehouseSelectionStrategy warehouseSelectionStrategy;

	private BaseStoreService baseStoreService;
	private CommonI18NService commonI18NService;
	private CommerceStockService commerceStockService;
	/**
	 * @deprecated Since 1905. Use {@link ProductInterestDao} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private BackInStockProductInterestDao backInStockProductInterestDao;
	private ProductInterestDao productInterestDao;
	private List<ProductInterestModel> inStockProductInterests;

	@Override
	public PerformResult perform(final CronJobModel job)
	{
		final Date now = Calendar.getInstance().getTime();
		final List<ProductInterestModel> productIntersts = getProductInterestDao()
				.findProductInterestsByNotificationType(NotificationType.BACK_IN_STOCK);
		inStockProductInterests = productIntersts.stream().filter(pi -> isProductInStock(pi, now)).collect(Collectors.toList());
		final Map<ProductModel, SiteMessageModel> messages = new ConcurrentHashMap<>();

		inStockProductInterests.forEach(productInterest -> {

			final ProductModel product = productInterest.getProduct();
			if (!messages.containsKey(product))
			{
				messages.put(product, createSiteMessage(productInterest));
			}

			final Map<String, ItemModel> data = new HashMap<>();
			data.put(StocknotificationservicesConstants.LANGUAGE, productInterest.getLanguage());
			data.put(StocknotificationservicesConstants.PRODUCT_INTEREST, productInterest);
			data.put(StocknotificationservicesConstants.PRODUCT, productInterest.getProduct());
			data.put(StocknotificationservicesConstants.BASE_SITE, productInterest.getBaseSite());
			data.put(StocknotificationservicesConstants.BASE_STORE, productInterest.getBaseStore());
			data.put(StocknotificationservicesConstants.SITE_MESSAGE, messages.get(product));


			getTaskExecutor().execute(createTask(data));
		});


		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	protected SiteMessageModel createSiteMessage(final ProductInterestModel interest)
	{
		final ProductModel product = interest.getProduct();

		final Locale locale = getCommonI18NService().getLocaleForIsoCode(interest.getLanguage().getIsocode());
		final String subject = Localization.getLocalizedMap("back_in_stock.notification.sitemessage.subject").entrySet().stream()
				.filter(map -> map.getKey().getIsocode().equals(interest.getLanguage().getIsocode())).map(map -> map.getValue())
				.collect(Collectors.joining());
		String body;
		if (Objects.nonNull(getProductStockLevel(interest)))
		{
			body = Localization.getLocalizedMap("back_in_stock.notification.sitemessage.body").entrySet().stream()
					.filter(map -> map.getKey().getIsocode().equals(interest.getLanguage().getIsocode())).map(map -> map.getValue())
					.collect(Collectors.joining());
			body = MessageFormat.format(body, product.getName(locale), getProductStockLevel(interest));
		}
		else
		{
			body = Localization.getLocalizedMap("back_in_stock.notification.sitemessage.body.forceInStock").entrySet().stream()
					.filter(map -> map.getKey().getIsocode().equals(interest.getLanguage().getIsocode())).map(map -> map.getValue())
					.collect(Collectors.joining());
			body = MessageFormat.format(body, product.getName(locale));
		}
		return getSiteMessageService().createMessage(subject, body, SiteMessageType.SYSTEM, product,
				NotificationType.BACK_IN_STOCK);
	}

	protected abstract StockNotificationTask createTask(final Map<String, ItemModel> data);

	protected boolean isProductInStock(final ProductInterestModel productInterest, final Date now)
	{
		final ProductModel product = productInterest.getProduct();
		if (product == null)
		{
			modelService.remove(productInterest);
			return false;
		}
		if (!isProductOnSale(product, now))
		{
			return false;
		}

		final BaseStoreModel currentBaseStore = productInterest.getBaseStore();
		final StockLevelStatus stockLevelStatus = getCommerceStockService().getStockLevelStatusForProductAndBaseStore(product,
				currentBaseStore);
		return StockLevelStatus.INSTOCK.equals(stockLevelStatus) || StockLevelStatus.LOWSTOCK.equals(stockLevelStatus);
	}

	protected Long getProductStockLevel(final ProductInterestModel productInterest)
	{
		final BaseStoreModel currentBaseStore = productInterest.getBaseStore();
		return getCommerceStockService().getStockLevelForProductAndBaseStore(productInterest.getProduct(), currentBaseStore);
	}

	protected boolean isProductOnSale(final ProductModel product, final Date now)
	{
		final Date onlineDate = product.getOnlineDate();
		final Date offlineDate = product.getOfflineDate();
		return (onlineDate == null || onlineDate.before(now)) && (offlineDate == null || offlineDate.after(now));
	}

	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected StockService getStockService()
	{
		return stockService;
	}

	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
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

	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected WarehouseSelectionStrategy getWarehouseSelectionStrategy()
	{
		return warehouseSelectionStrategy;
	}

	/**
	 * @deprecated Since 1905.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setWarehouseSelectionStrategy(final WarehouseSelectionStrategy warehouseSelectionStrategy)
	{
		this.warehouseSelectionStrategy = warehouseSelectionStrategy;
	}

	/**
	 * @deprecated Since 1905. Use {@link ProductInterestDao} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected BackInStockProductInterestDao getBackInStockProductInterestDao()
	{
		return backInStockProductInterestDao;
	}

	/**
	 * @deprecated Since 1905. Use {@link ProductInterestDao} instead.
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setBackInStockProductInterestDao(final BackInStockProductInterestDao backInStockProductInterestDao)
	{
		this.backInStockProductInterestDao = backInStockProductInterestDao;
	}

	public List<ProductInterestModel> getInStockProductInterests()
	{
		return inStockProductInterests;
	}

	protected ProductInterestDao getProductInterestDao()
	{
		return productInterestDao;
	}

	@Required
	public void setProductInterestDao(final ProductInterestDao productInterestDao)
	{
		this.productInterestDao = productInterestDao;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected CommerceStockService getCommerceStockService()
	{
		return commerceStockService;
	}

	@Required
	public void setCommerceStockService(final CommerceStockService commerceStockService)
	{
		this.commerceStockService = commerceStockService;
	}
}
