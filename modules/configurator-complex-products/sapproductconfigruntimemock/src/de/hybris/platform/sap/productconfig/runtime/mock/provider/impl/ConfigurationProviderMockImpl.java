/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.mock.provider.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationEngineException;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.ProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Configuration;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMock;
import de.hybris.platform.sap.productconfig.runtime.mock.ConfigMockFactory;
import de.hybris.platform.sap.productconfig.runtime.mock.data.ConfigurationId;
import de.hybris.platform.sap.productconfig.runtime.mock.util.ModelCloneFactory;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class ConfigurationProviderMockImpl implements ConfigurationProvider
{

	private static final String YSAP_SIMPLE_POC_KB = "YSAP_SIMPLE_POC_KB";
	private static final String WEC_DRAGON_BUS = "WEC_DRAGON_BUS";
	public static final int MAX_EXTERNAL_CONFIGS = 50;
	protected boolean removePricesFromConfiguration = true;

	/**
	 * prefix for static delta prices
	 */
	public static final String STATIC_DELTA_PRICES = "staticDeltaPrices";

	private static final Logger LOG = Logger.getLogger(ConfigurationProviderMockImpl.class);

	private ConfigMockFactory configMockFactory;
	private ProviderFactory providerFactory;



	private static final class ExtConfigState
	{
		private ConfigModel model;
		private ConfigMock mock;
	}

	protected Map<String, ExtConfigState> extConfigState = new HashMap<>();
	protected Map<String, ExtConfigState> extConfigStateOld = new HashMap<>();
	protected final Map<String, ConfigModel> createdConfigs = new HashMap<>();
	private final Map<String, ConfigMock> createdMocks = new HashMap<>();

	private String lastReleasedConfigId;

	private ConfigModel lastReleasedConfig;
	private static Set<String> currentModifications = new HashSet<>();

	private static final String DUMMY_XML_OPEN = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOLUTION><CONFIGURATION CFGINFO=\"\" CLIENT=\"000\" COMPLETE=\"F\" CONSISTENT=\"T\" KBBUILD=\"3\" KBNAME=\"DUMMY_KB\" KBPROFILE=\"DUMMY_KB\" KBVERSION=\"3800\" LANGUAGE=\"E\" LANGUAGE_ISO=\"EN\" NAME=\"SCE 5.0\" ROOT_NR=\"1\" SCEVERSION=\" \"><INST AUTHOR=\"5\" CLASS_TYPE=\"300\" COMPLETE=\"F\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"01\" NR=\"1\" OBJ_KEY=\"DUMMY_KB\" OBJ_TXT=\"Dummy KB\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"><CSTICS><CSTIC AUTHOR=\"8\" CHARC=\"DUMMY_CSTIC\" CHARC_TXT=\"Dummy CStic\" VALUE=\"8\" VALUE_TXT=\"Value 8\"/></CSTICS></INST><PARTS/><NON_PARTS/></CONFIGURATION><SALES_STRUCTURE><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"1\" INSTANCE_NR=\"1\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"\"/></SALES_STRUCTURE>";
	private static final String DUMMY_XML_CLOSE = "</SOLUTION>";
	// XML-Tags for adding test properties to external configuration, e.g.
	// config id: <TEST_PROPERTIES><CONFIG_ID>1</CONFIG_ID></TEST_PROPERTIES>
	private static final String DUMMY_XML_TEST_PROPERTIES_OPEN = "<TEST_PROPERTIES";
	private static final String DUMMY_XML_TEST_PROPERTIES_CLOSE = "/>";
	private static final String DUMMY_XML_TEST_PROP_CONFIG_ID_OPEN = " CONFIG_ID=\"";
	private static final String DUMMY_XML_TEST_PROP_CONFIG_ID_CLOSE = "\"";
	protected static final String INVALID_RT_VERSION = "3700";
	protected static final String SEPARATOR = "@";
	protected static final int PARTS_UUID = 0;
	protected static final int PARTS_PRODUCT = 1;
	protected static final int PARTS_NUMBER_MINIMUM = 2;
	protected static final int PARTS_NUMBER_MAXIMUM = 3;

	public void setRemovePricesFromConfiguration(final boolean removePricesFromConfiguration)
	{
		this.removePricesFromConfiguration = removePricesFromConfiguration;
	}

	@Override
	public ConfigModel createDefaultConfiguration(final KBKey kbKey)
	{
		final String sapProductId = kbKey.getProductCode();
		return createConfigMock(sapProductId, null, null);
	}

	protected ConfigModel createConfigMock(final String sapProductId, final String variantProductCode, final String configIdToUse)
	{
		long startTime = 0;
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
		}
		final String variantId = variantProductCode != null ? String.format(" (%s)", variantProductCode) : "";
		LOG.info("create default MOCKED config session for:" + sapProductId + variantId);
		final ConfigMock mock1 = getConfigMockFactory().createConfigMockForProductCode(sapProductId, variantProductCode);
		mock1.setConfigId(generateConfigId(sapProductId, variantId, configIdToUse));

		final ConfigMock mock = mock1;
		final ConfigModel model = mock.createDefaultConfiguration();
		model.getRootInstance().setName(sapProductId);
		// use config ID as KB ID for MOCK
		model.setKbId(model.getId());
		initVersion(model);
		if (mock1.isChangeabeleVariant())
		{
			model.setKbKey(new KBKeyImpl(variantProductCode, sapProductId, "MOCK", null));
		}
		else
		{
			model.setKbKey(new KBKeyImpl(sapProductId, sapProductId, "MOCK", "3800"));
		}
		model.setKbBuildNumber("12");

		setAuthorSystemForAllCstics(model.getRootInstance());
		mock.showDeltaPrices(getProviderFactory().getPricingParameter().showDeltaPrices(sapProductId));
		//check default state
		mock.checkModel(model);

		createdConfigs.put(model.getId(), model);
		createdMocks.put(model.getId(), mock);
		final ConfigModel clonedModel = ModelCloneFactory.cloneConfigModel(model);
		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("CREATE MOCK took " + duration + " ms");
		}


		createdConfigs.put(STATIC_DELTA_PRICES + model.getId(), model);
		if (removePricesFromConfiguration)
		{
			removePrices(clonedModel);
		}
		return clonedModel;
	}

	protected void removePrices(final ConfigModel model)
	{
		model.setBasePrice(PriceModel.PRICE_NA);
		model.setCurrentTotalPrice(PriceModel.PRICE_NA);
		model.setSelectedOptionsPrice(PriceModel.PRICE_NA);

		// remove all delta prices
		removeDeltaPrices(model.getRootInstance());
	}

	protected void removeDeltaPrices(final InstanceModel instance)
	{
		for (final CsticModel cstic : instance.getCstics())
		{
			if (cstic.getAssignableValues() != null)
			{
				for (final CsticValueModel value : cstic.getAssignableValues())
				{
					value.setDeltaPrice(PriceModel.PRICE_NA);
					value.setValuePrice(PriceModel.PRICE_NA);
				}
			}
		}
		for (final InstanceModel subInstance : instance.getSubInstances())
		{
			removeDeltaPrices(subInstance);
		}
	}

	protected void setAuthorSystemForAllCstics(final InstanceModel instance)
	{
		final List<CsticModel> cstics = instance.getCstics();
		for (final CsticModel cstic : cstics)
		{
			cstic.setAuthor(CsticModel.AUTHOR_SYSTEM);
		}
		for (final InstanceModel subInstance : instance.getSubInstances())
		{
			setAuthorSystemForAllCstics(subInstance);
		}
	}

	protected void setAuthorUserForAllChangedCstics(final InstanceModel instance)
	{
		final List<CsticModel> cstics = instance.getCstics();
		for (final CsticModel cstic : cstics)
		{
			if (cstic.isChangedByFrontend())
			{
				cstic.setAuthor(CsticModel.AUTHOR_USER);
			}
			else
			{
				cstic.setAuthor(CsticModel.AUTHOR_SYSTEM);
			}
		}
		for (final InstanceModel subInstance : instance.getSubInstances())
		{
			setAuthorUserForAllChangedCstics(subInstance);
		}
	}

	protected String getAllValuePricesForAssignedValues(final ConfigModel configModel)
	{
		final StringBuilder result = new StringBuilder("");
		final List<CsticModel> cstics = configModel.getRootInstance().getCstics();
		cstics.stream().flatMap(cstic -> cstic.getAssignedValues().stream())
				.forEach(value -> result.append("/").append(value.getValuePrice().getPriceValue()));
		return result.toString();
	}

	@Override
	public boolean updateConfiguration(final ConfigModel model)
	{

		if (!currentModifications.add(model.getId()))
		{
			throw new ConcurrentModificationException("Concurrent modification detected for model with ID " + model.getId());
		}
		try
		{

			long startTime = 0;
			if (LOG.isDebugEnabled())
			{
				startTime = System.currentTimeMillis();
			}

			setAuthorUserForAllChangedCstics(model.getRootInstance());

			// clone the model so that updates returns "new" independent
			// instances, as SSC would do
			final ConfigModel updatedModel = ModelCloneFactory.cloneConfigModel(model);

			createdConfigs.put(updatedModel.getId(), updatedModel);

			final ConfigMock configMock = createdMocks.get(model.getId());
			configMock
					.showDeltaPrices(getProviderFactory().getPricingParameter().showDeltaPrices(model.getRootInstance().getName()));
			configMock.checkModel(updatedModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Value prices at assigned value level: " + getAllValuePricesForAssignedValues(updatedModel));
			}

			if (LOG.isDebugEnabled())
			{
				final long duration = System.currentTimeMillis() - startTime;
				LOG.debug("UPDATE MOCK took " + duration + " ms");
			}
		}
		finally
		{
			currentModifications.remove(model.getId());
		}
		return true;
	}

	@Override
	public ConfigModel retrieveConfigurationModel(final String configId) throws ConfigurationEngineException
	{
		long startTime = 0;
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
		}
		ensureConfigIsKnown(configId);
		final ConfigModel clonedModel = ModelCloneFactory.cloneConfigModel(createdConfigs.get(configId));


		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("GET MOCK took " + duration + " ms");
		}
		if (removePricesFromConfiguration)
		{
			removePrices(clonedModel);
		}
		return clonedModel;
	}

	protected void ensureConfigIsKnown(final String configId) throws ConfigurationEngineException
	{
		if (!createdConfigs.containsKey(configId))
		{
			final ConfigurationId structuredConfigId = getStructuredConfigIdFromString(configId);
			createConfigMock(structuredConfigId.getProductId(), structuredConfigId.getVariantId(), configId);
			LOG.warn("Configuration " + configId + "was not found and had to be restored in its default state");
		}
	}

	@Override
	public String retrieveExternalConfiguration(final String configId) throws ConfigurationEngineException
	{
		ensureConfigIsKnown(configId);
		final ExtConfigState state = new ExtConfigState();
		state.mock = createdMocks.get(configId);
		state.model = createdConfigs.get(configId);

		ensureNotToManyExtConfigs();
		extConfigState.put(configId, state);
		String dummyExtConfig = DUMMY_XML_OPEN + DUMMY_XML_TEST_PROPERTIES_OPEN + DUMMY_XML_TEST_PROP_CONFIG_ID_OPEN
				+ StringEscapeUtils.escapeXml10(configId) + DUMMY_XML_TEST_PROP_CONFIG_ID_CLOSE + DUMMY_XML_TEST_PROPERTIES_CLOSE
				+ DUMMY_XML_CLOSE;
		//check on validity
		if (!extConfigBelongsToValidProduct(configId))
		{
			LOG.info("External representation that points to non-existing RT version");
			dummyExtConfig = dummyExtConfig.replaceAll("KBVERSION=\"3800\"", "KBVERSION=\"" + INVALID_RT_VERSION + "\"");
			final KBKey oldKey = state.model.getKbKey();
			state.model.setKbKey(new KBKeyImpl(oldKey.getProductCode(), oldKey.getKbName(), "MOCK", INVALID_RT_VERSION));
		}
		return dummyExtConfig;

	}

	protected boolean extConfigBelongsToValidProduct(final String configId) throws ConfigurationEngineException
	{
		final ConfigModel configurationModel = retrieveConfigurationModel(configId);
		return configurationModel == null || !configurationModel.getRootInstance().getName().equals(WEC_DRAGON_BUS);
	}

	protected void ensureNotToManyExtConfigs()
	{
		if (extConfigState.size() > MAX_EXTERNAL_CONFIGS)
		{
			extConfigStateOld.clear();
			final Map<String, ExtConfigState> tmp = extConfigStateOld;
			extConfigStateOld = extConfigState;
			extConfigState = tmp;
		}
	}

	public void setI18NService(final I18NService notNeededYet)
	{
		// empty
	}

	@Override
	public ConfigModel createConfigurationFromExternalSource(final Configuration extConfig)
	{
		throw new UnsupportedOperationException(
				"createConfigurationFromExternalSource(Configuration extConfig) not supported by this mock");
	}

	@Override
	public ConfigModel createConfigurationFromExternalSource(final KBKey kbKey, final String extConfig)
	{
		String configId = null;
		// Extract config id from external configuration to retrieve
		// ConfigModel from session.
		configId = extractConfigIdFromExtConfig(extConfig);
		try
		{
			ensureConfigIsKnown(configId);
		}
		catch (final ConfigurationEngineException ex)
		{
			throw new IllegalArgumentException(
					"Mock failed to re-store configuration state. This mock can only restore an external configuration, if it was created within the same session",
					ex);
		}


		final ExtConfigState oldState;
		if (extConfigState.containsKey(configId))
		{
			oldState = extConfigState.get(configId);
		}
		else
		{
			oldState = extConfigStateOld.get(configId);
		}
		if (null != oldState)
		{
			createdMocks.put(configId, oldState.mock);
			createdConfigs.put(configId, oldState.model);
		}
		// Return ConfigModel from session using extracted id
		final ConfigModel configModel = ModelCloneFactory.cloneConfigModel(createdConfigs.get(configId));
		ConfigurationId structuredConfigId;
		try
		{
			structuredConfigId = getStructuredConfigIdFromString(configId);
		}
		catch (final ConfigurationEngineException e)
		{
			throw new IllegalStateException(e);
		}
		final String newConfigId = generateConfigId(structuredConfigId.getProductId(), structuredConfigId.getVariantId(), null);
		configModel.setId(newConfigId);
		configModel.setKbId(newConfigId);
		createdConfigs.put(newConfigId, configModel);
		createdMocks.put(newConfigId, createdMocks.get(configId));
		createdConfigs.put(STATIC_DELTA_PRICES + newConfigId, configModel);
		return configModel;

	}

	protected ConfigurationId getStructuredConfigIdFromString(final String configId) throws ConfigurationEngineException
	{
		final ConfigurationId structuredConfigId = new ConfigurationId();
		final String[] parts = configId.split(SEPARATOR);
		if (parts.length < PARTS_NUMBER_MINIMUM || parts.length > PARTS_NUMBER_MAXIMUM)
		{
			throw new ConfigurationEngineException(String.format("No valid configId: '%s'", configId));
		}
		structuredConfigId.setUid(parts[PARTS_UUID]);
		structuredConfigId.setProductId(parts[PARTS_PRODUCT]);
		if (parts.length > PARTS_NUMBER_MINIMUM)
		{
			structuredConfigId.setVariantId(parts[PARTS_NUMBER_MINIMUM]);
		}
		return structuredConfigId;
	}

	protected String getConfigIdFromStructured(final ConfigurationId configId)
	{
		return new StringBuilder(configId.getUid()).append(SEPARATOR).append(configId.getProductId()).append(SEPARATOR)
				.append(configId.getVariantId()).toString();
	}

	protected String generateConfigId(final String productId, final String variantId, final String configIdToBeUsed)
	{
		if (null != configIdToBeUsed)
		{
			return configIdToBeUsed;
		}
		final ConfigurationId configId = new ConfigurationId();
		configId.setUid(UUID.randomUUID().toString());
		configId.setProductId(productId);
		configId.setVariantId(variantId);
		return getConfigIdFromStructured(configId);
	}



	/**
	 * Extracts the config id from an external configuration string. The config id is stored as child element of the
	 * TEST_PROPERTIES tag. Example: <TEST_PROPERTIES><CONFIG_ID>1</CONFIG_ID></TEST_PROPERTIES>
	 *
	 * @param extConfig
	 * @return configId
	 */
	protected String extractConfigIdFromExtConfig(final String extConfig)
	{
		return parseAttributeValue(extConfig, "TEST_PROPERTIES", "CONFIG_ID");
	}

	protected String parseAttributeValue(final String extConfig, final String tag, final String attribute)
	{
		String attrValue = null;
		try
		{
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			final InputStream source = new ByteArrayInputStream(extConfig.getBytes("UTF-8"));
			final Document doc = dBuilder.parse(source);
			final Node testPropertiesNode = doc.getElementsByTagName(tag).item(0);
			final NamedNodeMap attrList = testPropertiesNode.getAttributes();
			final Node namedItem = attrList.getNamedItem(attribute);
			attrValue = namedItem.getNodeValue();

		}
		catch (ParserConfigurationException | SAXException | IOException e)
		{
			LOG.warn(e);
		}
		return attrValue;
	}

	public ConfigModel getCachedConfig(final String configId)
	{
		if (configId.equals(lastReleasedConfigId))
		{
			return lastReleasedConfig;
		}
		return createdConfigs.get(configId);
	}

	@Override
	public void releaseSession(final String configId)
	{
		lastReleasedConfigId = configId;
		lastReleasedConfig = createdConfigs.get(configId);
		createdConfigs.remove(configId);
		createdMocks.remove(configId);
		LOG.info("Mock Configuration session released, sessionID: " + configId);

	}

	/**
	 * Simple returns the default configuration of the base product as a variant
	 */
	@Override
	public ConfigModel retrieveConfigurationFromVariant(final String baseProductCode, final String variantProductCode)
	{
		return createConfigMock(baseProductCode, variantProductCode, null);
	}

	@Override
	public boolean isKbForDateExists(final String productCode, final Date kbDate)
	{
		return false;
	}

	/**
	 * @return the runTimeConfigMockFactory
	 */
	public ConfigMockFactory getConfigMockFactory()
	{
		return configMockFactory;
	}

	/**
	 * @param configMockFactory
	 *           the runTimeConfigMockFactory to set
	 */
	public void setConfigMockFactory(final ConfigMockFactory configMockFactory)
	{
		this.configMockFactory = configMockFactory;
	}

	protected ProviderFactory getProviderFactory()
	{
		return providerFactory;
	}

	public void setProviderFactory(final ProviderFactory providerFactory)
	{
		this.providerFactory = providerFactory;
	}

	@Override
	public String changeConfiguration(final ConfigModel model) throws ConfigurationEngineException
	{
		initVersion(model);
		updateConfiguration(model);
		return incrementVersion(model);
	}

	protected void initVersion(final ConfigModel model)
	{
		if (null == model.getVersion())
		{
			model.setVersion("0");
		}
	}

	protected String incrementVersion(final ConfigModel model)
	{
		return String.valueOf(Integer.valueOf(model.getVersion()) + 1);
	}

	@Override
	public void releaseSession(final String configId, final String version)
	{
		releaseSession(configId);

	}

	@Override
	public KBKey extractKbKey(final String productCode, final String externalConfig)
	{
		final String kbVersion = parseAttributeValue(externalConfig, "CONFIGURATION", "KBVERSION");
		return new KBKeyImpl(productCode, productCode, "MOCK", kbVersion);
	}

	@Override
	public boolean isKbVersionExists(final KBKey kbKey)
	{
		return null == kbKey.getKbVersion() || "3800".equals(kbKey.getKbVersion());
	}

	@Override
	public boolean isKbVersionValid(final KBKey kbKey)
	{
		return isKbVersionExists(kbKey)
				&& !(kbKey.getProductCode().equals(WEC_DRAGON_BUS) && kbKey.getKbName().equals(YSAP_SIMPLE_POC_KB));
	}
}
