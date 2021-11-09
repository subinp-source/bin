/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package ydocumentcartpackage.persistence.polyglot.repository.documentcart;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;

public class CartModelsCreator
{
	private final ModelService modelService;
	private final UserService userService;
	private final CommonI18NService commonI18NService;

	public CartModelsCreator(final ModelService modelService, final UserService userService,
	                         final CommonI18NService commonI18NService)
	{
		this.modelService = modelService;
		this.userService = userService;
		this.commonI18NService = commonI18NService;
	}

	public CartModel createCart(final String cartDescription, final Double paymentCost)
	{
		final CartModel cartModel = modelService.create(CartModel.class);
		cartModel.setUser(userService.getAdminUser());
		final Date date = new Date();
		cartModel.setDate(date);
		cartModel.setCurrency(commonI18NService.getBaseCurrency());
		cartModel.setDescription(cartDescription);
		cartModel.setPaymentCost(paymentCost);
		final String uuid = UUID.randomUUID().toString();
		cartModel.setCode(uuid);
		return cartModel;
	}

	public CartEntryModel createCartEntry(final UnitModel unit, final ProductModel product, final CartModel order,
	                                      final Long quantity)
	{
		return createCartEntry(unit, product, order, quantity, RandomUtils.nextInt(1, Integer.MAX_VALUE));
	}

	public CartEntryModel createCartEntry(final UnitModel unit,
	                                      final ProductModel product,
	                                      final CartModel order,
	                                      final Long quantity,
	                                      final Integer entryNumber)
	{
		final CartEntryModel cartEntryModel = modelService.create(CartEntryModel.class);
		cartEntryModel.setOrder(order);
		cartEntryModel.setQuantity(quantity);
		cartEntryModel.setUnit(unit);
		cartEntryModel.setProduct(product);
		cartEntryModel.setEntryNumber(entryNumber);

		return cartEntryModel;
	}

	public CatalogModel createCatalog()
	{
		final CatalogModel catalog = modelService.create(CatalogModel.class);
		catalog.setId(UUID.randomUUID().toString());
		catalog.setName("catalog1");
		return catalog;
	}

	public CatalogVersionModel createCatalogVersion(final CatalogModel catalog)
	{
		final CatalogVersionModel catalogVersion = modelService.create(CatalogVersionModel.class);
		catalogVersion.setCatalog(catalog);
		catalogVersion.setVersion("version1");
		return catalogVersion;
	}

	public ProductModel createProduct(final CatalogVersionModel catalogVersion)
	{
		final ProductModel product = modelService.create(ProductModel.class);
		product.setCode("product1");
		product.setCatalogVersion(catalogVersion);
		return product;
	}

	public UnitModel createUnit()
	{
		final UnitModel unit = modelService.create(UnitModel.class);
		unit.setName("pieces");
		unit.setUnitType("type");
		unit.setCode(UUID.randomUUID().toString());
		return unit;
	}
}
