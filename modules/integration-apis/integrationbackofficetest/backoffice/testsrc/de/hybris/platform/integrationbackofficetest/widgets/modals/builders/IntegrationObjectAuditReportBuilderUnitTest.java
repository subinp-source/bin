/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.builders;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.audit.view.impl.ReportView;
import de.hybris.platform.auditreport.model.AuditReportTemplateModel;
import de.hybris.platform.auditreport.service.ReportConversionData;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.auditreport.service.impl.AbstractTemplateViewConverterStrategy;
import de.hybris.platform.auditreport.service.impl.DefaultReportViewConverterStrategy;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationbackoffice.widgets.modals.builders.IntegrationObjectAuditReportBuilder;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class IntegrationObjectAuditReportBuilderUnitTest
{
	@InjectMocks
	private IntegrationObjectAuditReportBuilder auditReportBuilder = new IntegrationObjectAuditReportBuilder();

	@Mock
	private RendererService rendererService;
	@Mock
	private UserService userService;

	@Test
	public void testFilterReportView()
	{
		Map<String, Object> payload = new HashMap<String, Object>();
		Date timeStamp0 = new Date(10000);
		Date timeStamp1 = new Date(13000);
		Date timeStamp2 = new Date(16000);
		Date timeStamp3 = new Date(19000);
		String testUser0 = "testUser";
		String testUser1 = "testUser";
		String testUser2 = "testUser";
		String testUser3 = "testUser";
		ReportView reportViewTest0 = ReportView.builder(payload, timeStamp0, testUser0).build();
		ReportView reportViewTest1 = ReportView.builder(payload, timeStamp1, testUser1).build();
		ReportView reportViewTest2 = ReportView.builder(payload, timeStamp2, testUser2).build();
		ReportView reportViewTest3 = ReportView.builder(payload, timeStamp3, testUser3).build();
		Stream<ReportView> reportStream = Stream.of(reportViewTest0, reportViewTest1, reportViewTest2, reportViewTest3);

		Stream<ReportView> testRes = auditReportBuilder.filterReportView(reportStream);
		assertEquals(1, (testRes.toArray().length));

		timeStamp2 = new Date(15999);
		reportViewTest2 = ReportView.builder(payload, timeStamp2, testUser2).build();
		reportStream = Stream.of(reportViewTest0, reportViewTest1, reportViewTest2, reportViewTest3);
		testRes = auditReportBuilder.filterReportView(reportStream);
		assertEquals(2, (testRes.toArray().length));

		timeStamp2 = new Date(16000);
		testUser2 = "anotherTestUser";
		reportViewTest2 = ReportView.builder(payload, timeStamp2, testUser2).build();
		reportStream = Stream.of(reportViewTest0, reportViewTest1, reportViewTest2, reportViewTest3);
		testRes = auditReportBuilder.filterReportView(reportStream);
		assertEquals(3, (testRes.toArray().length));

	}

	@Test
	public void testTraversePayload()
	{
		Map<String, Object> composedTypeConfigMap = new HashMap<String, Object>();
		composedTypeConfigMap.put("ComposedType_", "test1");
		Map<String, Object> ComposedTypeOfItemMap = new HashMap<String, Object>();
		ComposedTypeOfItemMap.put("ComposedTypeOfItem", composedTypeConfigMap);
		Map<String, Object> itemTypeConfigMap = new HashMap<String, Object>();
		itemTypeConfigMap.put("returnIntegrationObjectItem", ComposedTypeOfItemMap);

		auditReportBuilder.traversePayload(itemTypeConfigMap);

		assertEquals(1, itemTypeConfigMap.size());
		assertTrue(itemTypeConfigMap.containsValue("test1"));

		Map<String, Object> anotherMap = new HashMap<String, Object>();
		anotherMap.put("returnIntegrationObjectItem", "test2");

		auditReportBuilder.traversePayload(anotherMap);
		assertEquals(1, anotherMap.size());

		anotherMap.put("returnIntegrationObjectItem", Map.of("key", "value"));
		auditReportBuilder.traversePayload(anotherMap);
		assertEquals(0, anotherMap.size());
	}

	@Test
	public void testPopulateReportGenerationContext()
	{
		IntegrationObjectModel testIntegrationObject = mock(IntegrationObjectModel.class);
		auditReportBuilder.setSelectedModel(testIntegrationObject);
		when(testIntegrationObject.getCode()).thenReturn("testIoCodes");
		UserModel testUser = mock(UserModel.class);
		when(userService.getCurrentUser()).thenReturn(testUser);
		AuditReportTemplateModel testAuditReportTemplate = mock(AuditReportTemplateModel.class);
		when(auditReportBuilder.getAuditReportTemplate()).thenReturn(testAuditReportTemplate);
		auditReportBuilder.setConfigName("IOReport");

		Map<String, Object> testResMap = auditReportBuilder.populateReportGenerationContext();

		assertTrue(testResMap.containsValue(testIntegrationObject));
		assertTrue(testResMap.containsValue("IOReport"));
		assertTrue(testResMap.containsValue(testUser));
		assertTrue(testResMap.containsValue(testAuditReportTemplate));
	}

	@Test
	public void testPopulateReportGenerationContextWithNullTemplate()
	{
		IntegrationObjectModel testIntegrationObject = mock(IntegrationObjectModel.class);
		auditReportBuilder.setSelectedModel(testIntegrationObject);
		String testConfigFileName = "testConfigFileName";
		String testTemplateFileName = "testTemplateFileName";
		when(testIntegrationObject.getCode()).thenReturn("testIoCodes");
		UserModel testUser = mock(UserModel.class);
		when(userService.getCurrentUser()).thenReturn(testUser);
		when(auditReportBuilder.getAuditReportTemplate()).thenReturn(null);

		Map<String, Object> testResMap = auditReportBuilder.populateReportGenerationContext();

		assertNull(testResMap);
	}

	@Test
	public void testEvaluateStrategiesToStreams()
	{
		final Stream<ReportView> reports = Stream.empty();
		final Map<String, Object> context = new HashMap<>();
		final AuditReportTemplateModel template = mock(AuditReportTemplateModel.class);
		context.put(AbstractTemplateViewConverterStrategy.CTX_TEMPLATE, template);

		final ReportConversionData textData = mock(ReportConversionData.class);
		final List<ReportConversionData> textDataCollection = Collections.singletonList(textData);

		ReportViewConverterStrategy testStrategy1 = mock(DefaultReportViewConverterStrategy.class);
		List<ReportViewConverterStrategy> reportViewConverterStrategies = new ArrayList();
		reportViewConverterStrategies.add(testStrategy1);
		auditReportBuilder.setReportViewConverterStrategies(reportViewConverterStrategies);
		when(testStrategy1.convert(reports, context)).thenReturn(textDataCollection);

		Map<String, InputStream> resMap = auditReportBuilder.evaluateStrategiesToStreams(reports, context);

		assertTrue(resMap.isEmpty());
	}

	@Test
	public void testEvaluateStrategiesToStreamsWithNullReturn()
	{
		final Stream<ReportView> reports = Stream.empty();
		final Map<String, Object> context = new HashMap<>();
		final AuditReportTemplateModel template = mock(AuditReportTemplateModel.class);
		context.put(AbstractTemplateViewConverterStrategy.CTX_TEMPLATE, template);

		ReportViewConverterStrategy testStrategy1 = mock(DefaultReportViewConverterStrategy.class);
		List<ReportViewConverterStrategy> reportViewConverterStrategies = new ArrayList();
		reportViewConverterStrategies.add(testStrategy1);
		auditReportBuilder.setReportViewConverterStrategies(reportViewConverterStrategies);
		when(testStrategy1.convert(reports, context)).thenReturn(null);

		Map<String, InputStream> resMap = auditReportBuilder.evaluateStrategiesToStreams(reports, context);

		assertTrue(resMap.isEmpty());
	}

	@Test
	public void testGetAuditReportTemplate()
	{
		AuditReportTemplateModel testAuditReportTemplateModel = mock(AuditReportTemplateModel.class);
		when(rendererService.getRendererTemplateForCode(anyString())).thenReturn(testAuditReportTemplateModel);
		AuditReportTemplateModel acturalAuditReportTemplateModel = auditReportBuilder.getAuditReportTemplate();
		assertEquals(acturalAuditReportTemplateModel, testAuditReportTemplateModel);
	}

	@Test
	public void testGetAuditReportTemplateWithNullReturn()
	{
		when(rendererService.getRendererTemplateForCode(anyString())).thenReturn(null);
		AuditReportTemplateModel acturalAuditReportTemplateModel = auditReportBuilder.getAuditReportTemplate();
		assertNull(acturalAuditReportTemplateModel);
	}

	@Test
	public void testGetDownloadFileName()
	{
		ItemModel itemModel = mock(IntegrationObjectModel.class);
		auditReportBuilder.setSelectedModel(itemModel);
		when(((IntegrationObjectModel) itemModel).getCode()).thenReturn("testIntegrationObjectModel");
		assertEquals(auditReportBuilder.getDownloadFileName(), "testIntegrationObjectModel");
	}

}
