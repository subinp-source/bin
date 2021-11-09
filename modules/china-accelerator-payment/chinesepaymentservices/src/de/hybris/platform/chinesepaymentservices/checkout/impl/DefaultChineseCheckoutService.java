/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepaymentservices.checkout.impl;

import de.hybris.platform.chinesepaymentservices.checkout.ChineseCheckoutService;
import de.hybris.platform.chinesepaymentservices.model.StockLevelReservationHistoryEntryModel;
import de.hybris.platform.chinesepaymentservices.order.dao.ChineseOrderDao;
import de.hybris.platform.chinesepaymentservices.stocklevel.impl.ChineseStockLevelReservationHistoryEntryService;
import de.hybris.platform.commerceservices.stock.strategies.impl.DefaultWarehouseSelectionStrategy;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.InsufficientStockLevelException;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.task.TaskModel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation for {@link ChineseCheckoutService}. Delivers main functionality for chinese checkout service.
 */
public class DefaultChineseCheckoutService implements ChineseCheckoutService
{

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	private StockService stockService;
	private ProductService productService;
	private ModelService modelService;
	private BaseStoreService baseStoreService;
	private DefaultWarehouseSelectionStrategy defaultWarehouseSelectionStrategy;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	private ChineseStockLevelReservationHistoryEntryService chineseStockLevelReservationHistoryEntryService;
	private ChineseOrderDao chineseOrderDao;

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public boolean reserveStock(final String orderCode, final String productCode, final int quantity,
			final Optional<PointOfServiceModel> pos) throws InsufficientStockLevelException
	{
		final ProductModel product = getProductService().getProductForCode(productCode);
		final List<WarehouseModel> warehouses = pos.isPresent() ? pos.get().getWarehouses()
				: getDefaultWarehouseSelectionStrategy().getWarehousesForBaseStore(getBaseStoreService().getCurrentBaseStore());
		for (final WarehouseModel warehouse : warehouses)
		{
			try
			{
				if (getStockService().getStockLevelAmount(product, warehouse) >= quantity)
				{
					getStockService().reserve(product, warehouse, quantity, "reserve");
					final StockLevelReservationHistoryEntryModel stockLevelReservationHistoryEntry = new StockLevelReservationHistoryEntryModel();
					stockLevelReservationHistoryEntry.setOrderCode(orderCode);
					stockLevelReservationHistoryEntry.setProduct(product);
					stockLevelReservationHistoryEntry.setWarehouse(warehouse);
					stockLevelReservationHistoryEntry.setQuantity(Integer.valueOf(quantity));
					getModelService().save(stockLevelReservationHistoryEntry);
					return true;
				}
			}
			catch (final StockLevelNotFoundException e)
			{
				continue;
			}
		}
		return false;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public void releaseStock(final String orderCode)
	{
		final List<StockLevelReservationHistoryEntryModel> stockLevelReservationHistoryEntries = getChineseStockLevelReservationHistoryEntryService()
				.getStockLevelReservationHistoryEntryByOrderCode(orderCode);
		for (final StockLevelReservationHistoryEntryModel StockLevelReservationHistoryEntry : stockLevelReservationHistoryEntries)
		{
			if (StockLevelReservationHistoryEntry != null)
			{
				getStockService().release(StockLevelReservationHistoryEntry.getProduct(),
						StockLevelReservationHistoryEntry.getWarehouse(), StockLevelReservationHistoryEntry.getQuantity().intValue(),
						"release");
				getModelService().remove(StockLevelReservationHistoryEntry);
			}
		}
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Override
	public void deleteStockLevelReservationHistoryEntry(final String orderCode)
	{
		final List<StockLevelReservationHistoryEntryModel> stockLevelReservationHistoryEntries = getChineseStockLevelReservationHistoryEntryService()
				.getStockLevelReservationHistoryEntryByOrderCode(orderCode);
		for (final StockLevelReservationHistoryEntryModel StockLevelReservationHistoryEntry : stockLevelReservationHistoryEntries)
		{
			if (StockLevelReservationHistoryEntry != null)
			{
				getModelService().remove(StockLevelReservationHistoryEntry);
			}
		}
	}

	@Override
	public Optional<TaskModel> getSubmitOrderEventTask(final String orderCode)
	{
		return Optional.ofNullable(getChineseOrderDao().findSubmitOrderEventTask(orderCode));
	}

	@Override
	public boolean authorizePayment(final String securityCode, final CartModel cartModel)
	{
		return checkIfCurrentUserIsTheCartUser(cartModel);
	}

	protected boolean checkIfCurrentUserIsTheCartUser(final CartModel cartModel)
	{
		if (cartModel != null)
		{
			return cartModel.getUser().equals(getCurrentUserForCheckout());
		}
		return false;
	}

	protected CustomerModel getCurrentUserForCheckout()
	{
		return getCheckoutCustomerStrategy().getCurrentUserForCheckout();
	}

	@Override
	public void setPaymentMode(final PaymentModeModel paymentMode, final CartModel cartModel)
	{
		cartModel.setPaymentMode(paymentMode);
		getModelService().save(cartModel);
	}

	@Override
	public OrderModel getOrderByCode(final String code)
	{
		return (OrderModel) getChineseOrderDao().findOrderByCode(code);
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	protected StockService getStockService()
	{
		return stockService;
	}

	/**
	 * @deprecated since 1905
	 */
	@Deprecated(since = "1905", forRemoval= true )
	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
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

	protected DefaultWarehouseSelectionStrategy getDefaultWarehouseSelectionStrategy()
	{
		return defaultWarehouseSelectionStrategy;
	}

	@Required
	public void setDefaultWarehouseSelectionStrategy(final DefaultWarehouseSelectionStrategy defaultWarehouseSelectionStrategy)
	{
		this.defaultWarehouseSelectionStrategy = defaultWarehouseSelectionStrategy;
	}

	protected CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	@Required
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	protected ChineseStockLevelReservationHistoryEntryService getChineseStockLevelReservationHistoryEntryService()
	{
		return chineseStockLevelReservationHistoryEntryService;
	}

	@Required
	public void setChineseStockLevelReservationHistoryEntryService(
			final ChineseStockLevelReservationHistoryEntryService chineseStockLevelReservationHistoryEntryService)
	{
		this.chineseStockLevelReservationHistoryEntryService = chineseStockLevelReservationHistoryEntryService;
	}

	protected ChineseOrderDao getChineseOrderDao()
	{
		return chineseOrderDao;
	}

	@Required
	public void setChineseOrderDao(final ChineseOrderDao chineseOrderDao)
	{
		this.chineseOrderDao = chineseOrderDao;
	}

}
