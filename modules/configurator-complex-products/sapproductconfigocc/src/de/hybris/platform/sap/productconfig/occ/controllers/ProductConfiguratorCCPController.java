/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.occ.controllers;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationPricingFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.PriceDataPair;
import de.hybris.platform.sap.productconfig.facades.PriceValueUpdateData;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UniqueUIKeyGenerator;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.occ.ConfigurationOverviewWsDTO;
import de.hybris.platform.sap.productconfig.occ.ConfigurationSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.ConfigurationWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.CsticValueSupplementsWsDTO;
import de.hybris.platform.sap.productconfig.occ.PriceSummaryWsDTO;
import de.hybris.platform.sap.productconfig.occ.util.ImageHandler;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.MoreObjects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.sap.productconfig.facades.ConfigurationFacade}.
 */
@Controller
@Api(tags = "Product Configurator CCP")
public class ProductConfiguratorCCPController
{

	private static final Logger LOG = Logger.getLogger(ProductConfiguratorCCPController.class);
	@Resource(name = "sapProductConfigFacade")
	private ConfigurationFacade configFacade;
	@Resource(name = "sapProductConfigPricingFacade")
	private ConfigurationPricingFacade configPricingFacade;
	@Resource(name = "sapProductConfigOverviewFacade")
	private ConfigurationOverviewFacade configOverviewFacade;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "sapProductConfigUiKeyGenerator")
	private UniqueUIKeyGenerator uniqueUiKeyGenerator;
	@Resource(name = "sapProductConfigImageHandler")
	private ImageHandler imageHandler;

	protected UniqueUIKeyGenerator getUniqueUIKeyGenerator()
	{
		return uniqueUiKeyGenerator;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	@RequestMapping(value = SapproductconfigoccControllerConstants.GET_CONFIG_FOR_PRODUCT, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getDefaultProductConfiguration", value = "Gets the default product configuration for a complex product", notes = "Returns the default product configuration for a given complex product. This means that a new instance of the configuration runtime object is created that is equipped with the default values from the configuration model. This API always returns the _entire_ group hierarchy, whereas it's capable of both including all attributes or only those for the first group. This is controlled by query attribute provideAllAttributes")
	@ApiBaseSiteIdParam
	public ConfigurationWsDTO getDefaultConfiguration(//
			@ApiParam(value = "Product code", required = true) //
			@PathVariable
			final String productCode,
			@ApiParam(value = "If this parameter is provided and its value is true, attributes for all groups are returned. Otherwise, attributes only for the first group are considered.") //
			@RequestParam(defaultValue = "false", required = false)
			final boolean provideAllAttributes)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getDefaultConfiguration: pCode=" + sanitize(productCode));
		}
		final ConfigurationData configData = readDefaultConfiguration(productCode);
		if (!provideAllAttributes)
		{
			final String firstGroupId = determineFirstGroupId(configData.getGroups());
			filterGroups(configData, firstGroupId);
		}
		return mapDTOData(configData);
	}

	@RequestMapping(value = SapproductconfigoccControllerConstants.CONFIGURE_URL, method = RequestMethod.PATCH)
	@ResponseBody
	@ApiOperation(nickname = "updateProductConfiguration", value = "Updates a product configuration", notes = "Updates a product configuration. It's possible to send only the changed parts of the configuration, for example a single value change for an attribute. These changes must include their entire path through the configuration (the group they belong to and its parent groups)")
	@ApiBaseSiteIdParam
	public ConfigurationWsDTO updateConfiguration(//
			@ApiParam(value = "Configuration identifier", required = true) //
			@PathVariable("configId")
			final String configId, //
			@RequestBody(required = true)
			final ConfigurationWsDTO updatedConfiguration)
	{
		final ConfigurationData configurationData = getDataMapper().map(updatedConfiguration, ConfigurationData.class);
		configurationData.setConfigId(configId);
		final String requestedGroupId = determineFirstGroupId(configurationData.getGroups());
		getConfigFacade().updateConfiguration(configurationData);
		final ConfigurationData updatedBackendConfiguration = getConfigFacade().getConfiguration(configurationData);
		filterGroups(updatedBackendConfiguration, requestedGroupId);

		return mapDTOData(updatedBackendConfiguration);
	}

	@RequestMapping(value = SapproductconfigoccControllerConstants.CONFIGURE_URL, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getProductConfiguration", value = "Gets a product configuration", notes = "Returns a product configuration, specified by its id. In case this call is done in the context of a logged-in session, the call ensures that the configuration is only returned if the user is authorized to view the configuration")
	@ApiBaseSiteIdParam
	public ConfigurationWsDTO getConfiguration(//
			@ApiParam(value = "Configuration identifier", required = true) //
			@PathVariable("configId")
			final String configId,
			@ApiParam(value = "If the parameter is provided only the attributes of the requested group are returned. If the parameter is not provided, attributes for all groups are returned.") //
			@RequestParam(required = false)
			final String groupId)
	{
		final ConfigurationData configurationData = new ConfigurationData();
		configurationData.setConfigId(configId);
		final ConfigurationData backendConfiguration = getConfigFacade().getConfiguration(configurationData);
		if (groupId != null && !groupId.isEmpty())
		{
			filterGroups(backendConfiguration, groupId);
		}
		return mapDTOData(backendConfiguration);
	}


	@RequestMapping(value = SapproductconfigoccControllerConstants.GET_CONFIGURE_OVERVIEW_URL, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getProductConfigurationOverview", value = "Gets a product configuration overview", notes = "Gets a configuration overview, a simplified, condensed read-only view on the product configuration. Only the selected attribute values are present here")
	@ApiBaseSiteIdParam
	public ConfigurationOverviewWsDTO getConfigurationOverview(//
			@ApiParam(value = "Configuration identifier", required = true) //
			@PathVariable("configId")
			final String configId)
	{
		final ConfigurationOverviewData overviewData = getConfigOverviewFacade().getOverviewForConfiguration(configId, null);

		final ConfigurationOverviewWsDTO configurationOverviewWs = getDataMapper().map(overviewData,
				ConfigurationOverviewWsDTO.class);
		configurationOverviewWs.setTotalNumberOfIssues(getConfigFacade().getNumberOfErrors(configId));
		return configurationOverviewWs;
	}

	@RequestMapping(value = SapproductconfigoccControllerConstants.GET_PRICING_URL, method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getProductConfigurationPricing", value = "Gets prices for a product configuration", notes = "Gets price elements on configuration level and on attribute value level if present. Those price elements include e.g. the configuration base price and the sum of selected options")
	@ApiBaseSiteIdParam
	public ConfigurationSupplementsWsDTO getPricing(//
			@ApiParam(value = "Configuration identifier", required = true) //
			@PathVariable("configId")
			final String configId, //
			@ApiParam(value = "Specifies the group for which the value prices are requested. In case not specified, no value prices are returned") //
			@RequestParam(required = false)
			final String groupId)
	{
		final PricingData priceSummary = getConfigPricingFacade().getPriceSummary(configId);

		final List<String> valuePricingInput = groupId != null ? compileValuePriceInput(getUiGroup(configId, groupId))
				: Collections.emptyList();
		final List<PriceValueUpdateData> valuePrices = getConfigPricingFacade().getValuePrices(valuePricingInput, configId);

		return compilePricingResult(configId, priceSummary, valuePrices);
	}

	protected ConfigurationOverviewFacade getConfigOverviewFacade()
	{
		return configOverviewFacade;
	}

	protected void setConfigOverviewFacade(final ConfigurationOverviewFacade configOverviewFacade)
	{
		this.configOverviewFacade = configOverviewFacade;
	}

	protected ConfigurationWsDTO mapDTOData(final ConfigurationData configData)
	{
		final ConfigurationWsDTO configurationWs = getDataMapper().map(configData, ConfigurationWsDTO.class);
		getImageHandler().convertImages(configData, configurationWs);
		configurationWs.setTotalNumberOfIssues(getConfigFacade().getNumberOfErrors(configData.getConfigId()));
		return configurationWs;
	}

	protected ConfigurationData readDefaultConfiguration(final String productCode)
	{
		final KBKeyData kbKey = new KBKeyData();
		kbKey.setProductCode(productCode);
		return getConfigFacade().getConfiguration(kbKey);
	}

	protected ConfigurationFacade getConfigFacade()
	{
		return configFacade;
	}

	protected void setConfigFacade(final ConfigurationFacade configFacade)
	{
		this.configFacade = configFacade;
	}

	/**
	 * Filters group data of given configuration by removing characteristic data from groups that are not matching the
	 * requested group Id. No filtering if requested group id is null.
	 *
	 * @param configData
	 *           configuration data to filter
	 * @param requestedGroupId
	 *           the group id, for which data should be completely returned
	 */
	protected void filterGroups(final ConfigurationData configData, final String requestedGroupId)
	{
		if (requestedGroupId != null)
		{
			filterGroups(configData.getGroups(), requestedGroupId);
		}
	}

	/**
	 * Filters the given group list by removing characteristic data from groups that are not matching the requested group
	 * Id. Sub-groups are also taken into account. No filtering if requested group id is null.
	 *
	 * @param groups
	 *           list of groups to filter
	 * @param requestedGroupId
	 *           the group id, for which data should be completely returned
	 */
	protected void filterGroups(final List<UiGroupData> groups, final String requestedGroupId)
	{
		if (requestedGroupId != null)
		{
			groups.stream().filter(group -> isNotRequestedGroup(group, requestedGroupId)).forEach(this::deleteCstics);
			groups.stream().filter(this::hasSubGroups).forEach(group -> filterGroups(group.getSubGroups(), requestedGroupId));
		}
	}

	protected void deleteCstics(final UiGroupData group)
	{
		group.setCstics(new ArrayList<CsticData>());
	}

	protected boolean isNotRequestedGroup(final UiGroupData group, final String requestedGroupId)
	{
		if (requestedGroupId == null)
		{
			return false;
		}
		return !group.getId().equals(requestedGroupId);
	}

	protected boolean hasSubGroups(final UiGroupData group)
	{
		return (group.getSubGroups() != null && !group.getSubGroups().isEmpty());

	}

	/**
	 * Searches for the first group with visible characteristics. It first searches in the groups of the given list
	 * before searching in subgroups.
	 *
	 * @param uiGroups
	 *           list of UI groups
	 * @return returns id of first group
	 */
	protected String determineFirstGroupId(final List<UiGroupData> uiGroups)
	{
		if (uiGroups != null)
		{
			final Optional<UiGroupData> result = uiGroups.stream()
					.filter(group -> group.getCstics() != null && !group.getCstics().isEmpty()).findFirst();
			if (result.isPresent())
			{
				return result.get().getId();
			}

			for (final UiGroupData uiGroup : uiGroups)
			{
				final String uiGroupResultId = determineFirstGroupId(uiGroup.getSubGroups());
				if (uiGroupResultId != null)
				{
					return uiGroupResultId;
				}
			}
		}

		return null;
	}

	protected ConfigurationSupplementsWsDTO compilePricingResult(final String configId, final PricingData priceSummary,
			final List<PriceValueUpdateData> valuePrices)
	{

		final ConfigurationSupplementsWsDTO pricingResult = new ConfigurationSupplementsWsDTO();
		final List<CsticSupplementsWsDTO> csticSupplementsWsDTOs = new ArrayList();
		valuePrices.stream().forEach(valuePrice -> csticSupplementsWsDTOs.add(createAttributeSupplementDTO(valuePrice)));
		final PriceSummaryWsDTO priceSummaryResult = getDataMapper().map(priceSummary, PriceSummaryWsDTO.class);

		pricingResult.setAttributes(csticSupplementsWsDTOs);
		pricingResult.setPriceSummary(priceSummaryResult);
		pricingResult.setConfigId(configId);

		if (priceSummary.getCurrentTotal() == null)
		{
			pricingResult.setPricingError(true);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("compile pricing result, pricing error: " + pricingResult.isPricingError());
		}
		return pricingResult;
	}

	protected CsticSupplementsWsDTO createAttributeSupplementDTO(final PriceValueUpdateData valuePrice)
	{
		final CsticSupplementsWsDTO result = this.getDataMapper().map(valuePrice, CsticSupplementsWsDTO.class);
		result.setPriceSupplements(createPriceSupplements(valuePrice.getPrices()));
		return result;
	}

	protected List<CsticValueSupplementsWsDTO> createPriceSupplements(final Map<String, PriceDataPair> prices)
	{
		final Map<String, PriceDataPair> productInfoStatusMapConsiderNull = MoreObjects.firstNonNull(prices,
				Collections.emptyMap());

		return productInfoStatusMapConsiderNull.entrySet().stream().map(entry -> convertEntrytoWsDTO(entry))
				.collect(Collectors.toList());
	}

	protected CsticValueSupplementsWsDTO convertEntrytoWsDTO(final Entry<String, PriceDataPair> entry)
	{
		final CsticValueSupplementsWsDTO result = new CsticValueSupplementsWsDTO();
		result.setPriceValue(getDataMapper().map(entry.getValue().getPriceValue(), PriceWsDTO.class));
		result.setObsoletePriceValue(getDataMapper().map(entry.getValue().getObsoletePriceValue(), PriceWsDTO.class));
		result.setAttributeValueKey(entry.getKey());
		return result;
	}

	protected UiGroupData getUiGroup(final String configId, final String groupId)
	{
		final ConfigurationData configurationRequest = new ConfigurationData();
		configurationRequest.setConfigId(configId);
		final ConfigurationData configurationData = getConfigFacade().getConfiguration(configurationRequest);
		return getUiGroup(configurationData.getGroups(), groupId);
	}

	protected Stream<UiGroupData> getFlattened(final UiGroupData uiGroup)
	{
		final List<UiGroupData> subGroups = uiGroup.getSubGroups();
		if (subGroups != null)
		{
			return Stream.concat(Stream.of(uiGroup), subGroups.stream().flatMap(group -> getFlattened(group)));
		}
		else
		{
			return Stream.of(uiGroup);
		}
	}

	protected UiGroupData getUiGroup(final List<UiGroupData> groupList, final String groupId)
	{

		final Optional<UiGroupData> optionalUiGroup = groupList.stream().flatMap(group -> getFlattened(group))
				.filter(group -> groupId.equals(group.getId())).findAny();
		if (optionalUiGroup.isPresent())
		{
			return optionalUiGroup.get();
		}

		throw new IllegalStateException("No group present for: " + groupId);
	}

	protected List<String> compileValuePriceInput(final UiGroupData uiGroup)
	{
		final List<String> result = new ArrayList<>();
		final List<CsticData> cstics = uiGroup.getCstics();
		if (cstics != null)
		{
			cstics.forEach(cstic -> result.add(compileCsticKey(cstic, uiGroup)));
		}
		return result;
	}

	protected String compileCsticKey(final CsticData cstic, final UiGroupData uiGroup)
	{

		return new StringBuilder(uiGroup.getId()).append(getUniqueUIKeyGenerator().getKeySeparator()).append(cstic.getName())
				.toString();
	}


	protected ConfigurationPricingFacade getConfigPricingFacade()
	{
		return configPricingFacade;
	}

	protected ImageHandler getImageHandler()
	{
		return imageHandler;
	}

}
