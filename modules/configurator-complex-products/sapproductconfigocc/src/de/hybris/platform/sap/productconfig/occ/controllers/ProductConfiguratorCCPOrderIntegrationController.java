/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import de.hybris.platform.sap.productconfig.facades.ConfigurationOrderIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationOverviewWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@Api(tags = "Product Configurator CCP Order Integration")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/orders")
public class ProductConfiguratorCCPOrderIntegrationController
{
	private static final Logger LOG = Logger.getLogger(ProductConfiguratorCCPOrderIntegrationController.class);

	@Resource(name = "sapProductConfigOrderIntegrationFacade")
	private ConfigurationOrderIntegrationFacade configurationOrderIntegrationFacade;
	@Resource(name = "sapProductConfigOverviewFacade")
	private ConfigurationOverviewFacade configurationOverviewFacade;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	@GetMapping(value = "/{orderId}/entries/{entryNumber}" + "/"
			+ SapproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE
			+ SapproductconfigoccControllerConstants.CONFIG_OVERVIEW)
	@ResponseBody
	@ApiOperation(nickname = "getConfigurationOverviewForOrderEntry", value = "Gets a product configuration overview of an order entry", notes = "Gets a configuration overview, a simplified, condensed read-only view on the product configuration of an order entry. Only the selected attribute values are present here")
	@ApiBaseSiteIdAndUserIdParam
	public ConfigurationOverviewWsDTO getConfigurationOverviewForOrderEntry(//
			@ApiParam(required = true, value = "The order id. Each order has a unique identifier.") //
			@PathVariable("orderId") final String orderId, //
			@ApiParam(required = true, value = "The entry number. Each entry in an order has an entry number. Order entries are numbered in ascending order, starting with zero (0).") //
			@PathVariable("entryNumber") final int entryNumber)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getConfigurationOverviewForOrderEntry: '" + logParam("orderId", sanitize(orderId)) + ","
					+ logParam("entryNumber", String.valueOf(entryNumber)) + "'");
		}

		ConfigurationOverviewData configOverviewData = getConfigurationOrderIntegrationFacade().getConfiguration(orderId,
				entryNumber);
		if (configOverviewData != null && configOverviewData.getId() != null)
		{
			configOverviewData = getConfigurationOverviewFacade().getOverviewForConfiguration(configOverviewData.getId(),
					configOverviewData);
			configOverviewData.setSourceDocumentId(orderId);
		}
		else
		{
				LOG.error("getConfigurationOverviewForOrderEntry: cannot retrieve configuration information for '"
					+ logParam("orderId", sanitize(orderId)) + "," + logParam("entryNumber", String.valueOf(entryNumber)) + "'");
			throw new NotFoundException("Cannot retrieve configuration information for orderId=" + sanitize(orderId)
					+ ", entryNumber=" + entryNumber);

		}
		return getDataMapper().map(configOverviewData, ConfigurationOverviewWsDTO.class);
	}


	protected ConfigurationOrderIntegrationFacade getConfigurationOrderIntegrationFacade()
	{
		return configurationOrderIntegrationFacade;
	}

	public void setConfigurationOrderIntegrationFacade(
			final ConfigurationOrderIntegrationFacade configurationOrderIntegrationFacade)
	{
		this.configurationOrderIntegrationFacade = configurationOrderIntegrationFacade;
	}

	protected ConfigurationOverviewFacade getConfigurationOverviewFacade()
	{
		return configurationOverviewFacade;
	}

	public void setConfigurationOverviewFacade(final ConfigurationOverviewFacade configurationOverviewFacade)
	{
		this.configurationOverviewFacade = configurationOverviewFacade;
	}

	protected static String logParam(final String paramName, final String paramValue)
	{
		return paramName + " = " + paramValue;
	}

	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}
}
