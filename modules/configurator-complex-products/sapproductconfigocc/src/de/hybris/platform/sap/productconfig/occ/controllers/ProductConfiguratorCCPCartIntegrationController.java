/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.ProductConfigOrderEntryWsDTO;
import de.hybris.platform.sap.productconfig.occ.util.ImageHandler;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
@Api(tags = "Product Configurator CCP Cart Integration")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/carts")
public class ProductConfiguratorCCPCartIntegrationController
{
	private static final String PRODUCT_CODE = "', product code: '";
	private static final String CONFIG_ID = "configId: ";
	private static final Logger LOG = Logger.getLogger(ProductConfiguratorCCPCartIntegrationController.class);

	@Resource(name = "sapProductConfigCartIntegrationFacade")
	private ConfigurationCartIntegrationFacade configCartFacade;

	@Resource(name = "sapProductConfigFacade")
	private ConfigurationFacade configFacade;

	@Resource(name = "commerceWebServicesCartFacade2")
	private CartFacade cartFacade;

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Resource(name = "sapProductConfigImageHandler")
	private ImageHandler imageHandler;

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}


	@RequestMapping(value = "/{cartId}/entries/"
			+ SapproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(nickname = "createCartEntryConfiguration", value = "Adds a product configuration to the cart", notes = "Adds a product configuration to the cart. The root product of the configuration is added as a cart entry, in addition the configuration is attached to the new entry")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO addCartEntry(//
			@ApiParam(value = "Base site identifier") //
			@PathVariable
			final String baseSiteId, //
			@ApiParam(value = "Request body parameter that contains attributes for creating the order entry, like quantity, product code and configuration identifier", required = true) //
			@RequestBody
			final ProductConfigOrderEntryWsDTO entry) throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData = addCartEntryInternal(entry);
		return getDataMapper().map(cartModificationData, CartModificationWsDTO.class);
	}

	protected CartModificationData addCartEntryInternal(final ProductConfigOrderEntryWsDTO entry)
			throws CommerceCartModificationException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(
					"addCartEntry: '" + logParam(CONFIG_ID, entry.getConfigId()) + PRODUCT_CODE + entry.getProduct().getCode() + "'");
		}

		CartModificationData cartModificationData = null;
		cartModificationData = getConfigCartFacade().addProductConfigurationToCart(entry.getProduct().getCode(),
				entry.getQuantity(), entry.getConfigId());


		if (LOG.isDebugEnabled())
		{
			LOG.debug("addCartEntry: '" + logParam(CONFIG_ID, entry.getConfigId()) + PRODUCT_CODE + entry.getProduct().getCode()
					+ "' was successful");
		}

		return cartModificationData;
	}

	protected static String logParam(final String paramName, final String paramValue)
	{
		return paramName + " = " + paramValue;
	}


	@RequestMapping(value = "/{cartId}/entries/{entryNumber}/"
			+ SapproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE, method = RequestMethod.PUT)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "replaceCartEntryConfiguration", value = "Updates the configuration of a cart entry", notes = "Updates the configuration. The entire configuration attached to the cart entry is replaced by the configuration specified in the request body. Possible only if the configuration change has been initiated by the corresponding GET method before")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateCartEntry(@ApiParam(value = "Base site identifier.")
	@PathVariable
	final String baseSiteId,
			@ApiParam(required = true, value = "The entry number. Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero (0).")
			@PathVariable
			final int entryNumber, @ApiParam(value = "Product configuration order entry with config id and order entry attributes")
			@RequestBody(required = true)
			final ProductConfigOrderEntryWsDTO entry)
	{
		entry.setEntryNumber(entryNumber);
		final CartModificationData cartModificationData = updateCartEntryInternal(entry);
		return getDataMapper().map(cartModificationData, CartModificationWsDTO.class);
	}

	protected CartModificationData updateCartEntryInternal(final ProductConfigOrderEntryWsDTO entry)

	{
		final String configId = entry.getConfigId();
		final String productCode = entry.getProduct().getCode();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCartEntry: '" + logParam(CONFIG_ID, configId) + PRODUCT_CODE + productCode + "'");
		}

		final CartModificationData cartModificationData = getConfigCartFacade().updateProductConfigurationInCart(productCode,
				configId);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateCartEntry: '" + logParam(CONFIG_ID, configId) + PRODUCT_CODE + productCode + "' was successful");
		}
		return cartModificationData;
	}

	@RequestMapping(value = "/{cartId}/entries/{entryNumber}/"
			+ SapproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getCartEntryConfiguration", value = "Gets the configuration of a cart entry", notes = "Returns the configuration of a cart entry and ensures that the entry can later be updated with the configuration and its changes")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public ConfigurationWsDTO configureCartEntry(//
			@ApiParam(value = "Base site identifier.") //
			@PathVariable
			final String baseSiteId, //
			@ApiParam(required = true, value = "The entry number. Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero (0).") //
			@PathVariable
			final int entryNumber)
	{
		final ConfigurationData configData = configureCartEntryInternal(entryNumber);
		final ConfigurationWsDTO configurationWs = getDataMapper().map(configData, ConfigurationWsDTO.class);
		getImageHandler().convertImages(configData, configurationWs);
		configurationWs.setTotalNumberOfIssues(getConfigFacade().getNumberOfErrors(configData.getConfigId()));
		return configurationWs;
	}

	protected ConfigurationData configureCartEntryInternal(final long entryNumber)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("configureCartEntry with entryNumber '" + logParam("entryNumber", String.valueOf(entryNumber)) + "'");
		}
		final OrderEntryData orderEntry = getCartEntryForNumber(getSessionCart(), entryNumber);
		final ConfigurationData configData = getConfigCartFacade().configureCartItem(orderEntry.getItemPK());

		return configData;
	}

	protected KBKeyData getKbKey(final ProductData product)
	{
		final KBKeyData kbKeyData = new KBKeyData();
		kbKeyData.setProductCode(product.getCode());
		return kbKeyData;
	}

	protected OrderEntryData getCartEntryForNumber(final CartData sessionCart, final long entryNumber)
	{
		final Long requestedEntryNumberAsLong = Long.valueOf(entryNumber);
		final Integer requestedEntryNumber = Integer.valueOf(requestedEntryNumberAsLong.intValue());

		final Optional<OrderEntryData> cartEntry = sessionCart.getEntries()//
				.stream()//
				.filter(entry -> requestedEntryNumber.equals(entry.getEntryNumber()))//
				.findAny();
		if (cartEntry.isPresent())
		{
			return cartEntry.get();
		}
		throw new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(entryNumber));
	}


	protected CartData getSessionCart()
	{
		return getCartFacade().getSessionCart();
	}

	protected ConfigurationCartIntegrationFacade getConfigCartFacade()
	{
		return configCartFacade;
	}

	public void setConfigCartFacade(final ConfigurationCartIntegrationFacade configCartFacade)
	{
		this.configCartFacade = configCartFacade;
	}


	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	protected ImageHandler getImageHandler()
	{
		return imageHandler;
	}

	protected ConfigurationFacade getConfigFacade()
	{
		return configFacade;
	}

	protected void setConfigFacade(final ConfigurationFacade configFacade)
	{
		this.configFacade = configFacade;
	}

}