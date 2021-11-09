/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageUISeverity;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessage;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessagePromoType;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessageSeverity;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessageSource;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ProductConfigMessageSourceSubType;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticValueModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ProductConfigMessageBuilder;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.productconfig.services.strategies.lifecycle.intf.ConfigurationAbstractOrderEntryLinkStrategy;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@SuppressWarnings("javadoc")
@UnitTest
public class ConfigurationMessageMapperImplTest
{
	private static final String NAME = "A";
	private static final String DESCRIPTION = "B";
	private static final String PRODUCT_CODE = "product_123";

	@InjectMocks
	private ConfigurationMessageMapperImpl classUnderTest = new ConfigurationMessageMapperImpl();

	private ConfigModel configModel;
	private ConfigurationData configData;
	private ConfigurationOverviewData configOverviewData;
	private ProductConfigMessageBuilder builder;
	@Mock
	private ValueFormatTranslator valueFormatTranslator;
	@Mock
	private ConfigurationAbstractOrderEntryLinkStrategy mockConfigurationAbstractOrderEntryLinkStrategy;
	@Mock
	private ProductConfigurationService mockProductConfigurationService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		configModel = new ConfigModelImpl();
		configData = new ConfigurationData();
		configOverviewData = new ConfigurationOverviewData();
		builder = new ProductConfigMessageBuilder();
		builder.appendBasicFields("a_test_message", "messagekey123", ProductConfigMessageSeverity.INFO);
		builder.appendSourceAndType(ProductConfigMessageSource.ENGINE, ProductConfigMessageSourceSubType.DEFAULT);
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn(null);
		when(mockProductConfigurationService.getTotalNumberOfIssues(configModel)).thenReturn(0);
	}

	@Test
	public void testMapMessagesFromModelToDataEmpty()
	{
		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertTrue(configData.getMessages().isEmpty());
	}

	@Test
	public void testMapMessagesFromModelToOverviewDataEmpty()
	{
		configModel.setComplete(true);
		configModel.setConsistent(true);
		classUnderTest.mapMessagesFromModelToData(configOverviewData, configModel);
		assertTrue(configOverviewData.getMessages().isEmpty());
	}

	@Test
	public void testMapMessagesFromModelToDraftCompleteConsistentConfigDataWithoutMessages()
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setComplete(true);
		configModel.setConsistent(true);
		configModel.setMessages(Collections.EMPTY_SET);
		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertTrue(configData.getMessages().isEmpty());
	}

	@Test
	public void testMapMessagesFromModelToDraftCompleteInconsistentConfigDataWithMessages()
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setComplete(true);
		configModel.setConsistent(false);
		final Set<ProductConfigMessage> messages = new HashSet<>();
		final ProductConfigMessage message = builder.build();
		messages.add(message);
		configModel.setMessages(messages);

		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertFalse(configData.getMessages().isEmpty());
		assertEquals(2, configData.getMessages().size());
	}

	@Test
	public void testMapMessagesFromModelToDraftCompleteConsistentConfigData()
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setComplete(true);
		configModel.setConsistent(true);
		final Set<ProductConfigMessage> messages = new HashSet<>();
		final ProductConfigMessage message = builder.build();
		messages.add(message);
		configModel.setMessages(messages);

		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertFalse(configData.getMessages().isEmpty());
		assertEquals(1, configData.getMessages().size());
	}

	@Test
	public void testMapMessagesFromModelToDraftIncompleteConsistentConfigDataWithAnotherErrors()
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		when(mockProductConfigurationService.getTotalNumberOfIssues(configModel)).thenReturn(1);
		configModel.setComplete(false);
		configModel.setConsistent(true);

		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertTrue(configData.getMessages().isEmpty());
	}

	@Test
	public void testMapMessagesFromModelToDraftInIncompleteInconsistentConfigDataWithMessages()
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setComplete(false);
		configModel.setConsistent(false);
		final Set<ProductConfigMessage> messages = new HashSet<>();
		final ProductConfigMessage message = builder.build();
		messages.add(message);
		configModel.setMessages(messages);

		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertFalse(configData.getMessages().isEmpty());
		assertEquals(2, configData.getMessages().size());
	}

	@Test
	public void testMapMessagesFromModelToDataThrowsUnsupportedEncodingExceptionException() throws UnsupportedEncodingException
	{
		classUnderTest = spy(classUnderTest);
		willThrow(UnsupportedEncodingException.class).given(classUnderTest)
				.encodeHTML(classUnderTest.getLocalizedText(ConfigurationMessageMapperImpl.UNRESOLVABLE_ISSUES_MSG));

		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setConsistent(true);
		classUnderTest.mapMessagesFromModelToData(configData, configModel);
	}

	@Test
	public void testMapMessagesFromModelToDataForInvisibleMandatoryCstic() throws UnsupportedEncodingException
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setConsistent(true);

		classUnderTest.mapMessagesFromModelToData(configData, configModel);
		assertNotNull(configData.getMessages());
		assertFalse(configData.getMessages().isEmpty());
		assertEquals(1, configData.getMessages().size());
		assertEquals(ProductConfigMessageUISeverity.ERROR, configData.getMessages().get(0).getSeverity());
		assertEquals(getMessage(ConfigurationMessageMapperImpl.UNRESOLVABLE_ISSUES_MSG),
				configData.getMessages().get(0).getMessage());
	}

	@Test
	public void testMapMessagesFromModelToOverviewDataForInvisibleMandatoryCstic() throws UnsupportedEncodingException
	{
		when(mockConfigurationAbstractOrderEntryLinkStrategy.getCartEntryForDraftConfigId(anyString())).thenReturn("cartEntry");
		configModel.setConsistent(true);

		classUnderTest.mapMessagesFromModelToData(configOverviewData, configModel);
		assertNotNull(configOverviewData.getMessages());
		assertFalse(configOverviewData.getMessages().isEmpty());
		assertEquals(1, configOverviewData.getMessages().size());
		assertEquals(ProductConfigMessageUISeverity.ERROR, configOverviewData.getMessages().get(0).getSeverity());
		assertEquals(getMessage(ConfigurationMessageMapperImpl.UNRESOLVABLE_ISSUES_MSG),
				configOverviewData.getMessages().get(0).getMessage());
	}

	@Test
	public void testMapMessagesFromModelToOverviewData() throws UnsupportedEncodingException
	{
		classUnderTest.mapMessagesFromModelToData(configOverviewData, configModel);
		assertNotNull(configOverviewData.getMessages());
		assertFalse(configOverviewData.getMessages().isEmpty());
		assertEquals(1, configOverviewData.getMessages().size());
		assertEquals(ProductConfigMessageUISeverity.ERROR, configOverviewData.getMessages().get(0).getSeverity());
		assertEquals(getMessage(ConfigurationMessageMapperImpl.UNRESOLVABLE_ISSUES_MSG),
				configOverviewData.getMessages().get(0).getMessage());
	}

	protected String getMessage(final String unresolvableIssuesMsg) throws UnsupportedEncodingException
	{
		return classUnderTest.encodeHTML(classUnderTest.getLocalizedText(unresolvableIssuesMsg));
	}

	@Test
	public void testMapMessagesFromModelToDataInfo()
	{
		builder.appendSeverity(ProductConfigMessageSeverity.INFO);
		addMessageToConfig(configModel, builder.build());

		classUnderTest.mapMessagesFromModelToData(configData, configModel);

		assertEquals(1, configData.getMessages().size());
		assertEquals("a_test_message", configData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, configData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataWarning()
	{
		builder.appendSeverity(ProductConfigMessageSeverity.WARNING);
		addMessageToConfig(configModel, builder.build());

		classUnderTest.mapMessagesFromModelToData(configData, configModel);

		assertEquals(1, configData.getMessages().size());
		assertEquals("a_test_message", configData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.INFO, configData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataError()
	{
		builder.appendSeverity(ProductConfigMessageSeverity.ERROR);
		addMessageToConfig(configModel, builder.build());

		classUnderTest.mapMessagesFromModelToData(configData, configModel);

		assertEquals(1, configData.getMessages().size());
		assertEquals("a_test_message", configData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.ERROR, configData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToOverviewDataError()
	{
		builder.appendSeverity(ProductConfigMessageSeverity.ERROR);
		addMessageToConfig(configModel, builder.build());

		configModel.setComplete(true);
		configModel.setConsistent(true);
		classUnderTest.mapMessagesFromModelToData(configOverviewData, configModel);
		when(mockProductConfigurationService.getTotalNumberOfIssues(configModel)).thenReturn(1);

		assertEquals(1, configOverviewData.getMessages().size());
		assertEquals("a_test_message", configOverviewData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.ERROR, configOverviewData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataEndcode()
	{
		builder.appendMessage("><img src=x onerror=alert(1)>");
		addMessageToConfig(configModel, builder.build());

		classUnderTest.mapMessagesFromModelToData(configData, configModel);

		assertEquals(1, configData.getMessages().size());
		assertEquals("&gt;&lt;img&#x20;src&#x3d;x&#x20;onerror&#x3d;alert&#x28;1&#x29;&gt;",
				configData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, configData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataEndcodeErr() throws UnsupportedEncodingException
	{
		builder.appendMessage("\uffff");
		addMessageToConfig(configModel, builder.build());

		classUnderTest = spy(classUnderTest);
		willThrow(UnsupportedEncodingException.class).given(classUnderTest).encodeHTML("\uffff");

		classUnderTest.mapMessagesFromModelToData(configData, configModel);

		assertEquals(1, configData.getMessages().size());
		assertNull(configData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, configData.getMessages().get(0).getSeverity());
	}


	@Test
	public void testMapMessagesFromModelToDataForCsticEmptyMessages()
	{
		final CsticModel csticModel = new CsticModelImpl();
		final CsticData csticData = new CsticData();
		classUnderTest.mapMessagesFromModelToData(csticData, csticModel);

		assertEquals(0, csticData.getMessages().size());
	}


	@Test
	public void testMapMessagesFromModelToDataForCstic()
	{
		final CsticModel csticModel = new CsticModelImpl();
		csticModel.getMessages().add(builder.build());

		final CsticData csticData = new CsticData();
		classUnderTest.mapMessagesFromModelToData(csticData, csticModel);

		assertEquals(1, csticData.getMessages().size());
		assertEquals("a_test_message", csticData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, csticData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataForCsticPromoMessage()
	{
		final CsticModel csticModel = new CsticModelImpl();
		builder.appendPromoType(ProductConfigMessagePromoType.PROMO_OPPORTUNITY);
		csticModel.getMessages().add(builder.build());

		final CsticData csticData = new CsticData();
		classUnderTest.mapMessagesFromModelToData(csticData, csticModel);

		assertEquals(1, csticData.getMessages().size());
		assertEquals("a_test_message", csticData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, csticData.getMessages().get(0).getSeverity());
		assertEquals(ProductConfigMessagePromoType.PROMO_OPPORTUNITY, csticData.getMessages().get(0).getPromoType());
	}

	@Test
	public void testMapMessagesFromModelToDataForCsticExtended() throws ParseException
	{

		final CsticModel csticModel = new CsticModelImpl();
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		final Date newDate = formatter.parse("2020/05/31");
		final String date = "5/31/20";
		when(valueFormatTranslator.formatDate(newDate)).thenReturn(date);
		builder.appendPromotionFields(ProductConfigMessagePromoType.PROMO_OPPORTUNITY, "a_text_extended", newDate);
		csticModel.getMessages().add(builder.build());

		final CsticData csticData = new CsticData();
		classUnderTest.mapMessagesFromModelToData(csticData, csticModel);

		assertEquals(1, csticData.getMessages().size());
		assertEquals("a_test_message", csticData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, csticData.getMessages().get(0).getSeverity());
		assertEquals(ProductConfigMessagePromoType.PROMO_OPPORTUNITY, csticData.getMessages().get(0).getPromoType());
		assertEquals("a_text_extended", csticData.getMessages().get(0).getExtendedMessage());
		assertEquals(date, csticData.getMessages().get(0).getEndDate());
	}

	@Test
	public void testMapMessagesFromModelToDataForCsticValueEmptyMessages()
	{
		final CsticValueModel valueModel = new CsticValueModelImpl();
		final CsticValueData valueData = new CsticValueData();

		classUnderTest.mapMessagesFromModelToData(valueData, valueModel);
		assertEquals(0, valueData.getMessages().size());

	}

	@Test
	public void testMapMessagesFromModelToDataForCsticValue()
	{
		final CsticValueModel valueModel = new CsticValueModelImpl();
		valueModel.getMessages().add(builder.build());

		final CsticValueData valueData = new CsticValueData();
		classUnderTest.mapMessagesFromModelToData(valueData, valueModel);

		assertEquals(1, valueData.getMessages().size());
		assertEquals("a_test_message", valueData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, valueData.getMessages().get(0).getSeverity());
	}

	@Test
	public void testMapMessagesFromModelToDataForCsticValuePromoMessage()
	{
		final CsticValueModel valueModel = new CsticValueModelImpl();
		builder.appendPromoType(ProductConfigMessagePromoType.PROMO_APPLIED);
		valueModel.getMessages().add(builder.build());

		final CsticValueData valueData = new CsticValueData();
		classUnderTest.mapMessagesFromModelToData(valueData, valueModel);

		assertEquals(1, valueData.getMessages().size());
		assertEquals("a_test_message", valueData.getMessages().get(0).getMessage());
		assertEquals(ProductConfigMessageUISeverity.CONFIG, valueData.getMessages().get(0).getSeverity());
		assertEquals(ProductConfigMessagePromoType.PROMO_APPLIED, valueData.getMessages().get(0).getPromoType());
	}

	protected void addMessageToConfig(final ConfigModel configModel, final ProductConfigMessage message)
	{
		final Set<ProductConfigMessage> messages = configModel.getMessages();
		messages.add(message);
		configModel.setMessages(messages);
	}
}
