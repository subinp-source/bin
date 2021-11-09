/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.widgets.modals.builders;

import static de.hybris.platform.integrationservices.util.IntegrationTestUtil.importImpEx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.platform.audit.internal.config.DefaultAuditConfigService;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.widgets.modals.builders.IntegrationObjectAuditReportBuilder;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.IntegrationObjectTestUtil;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class IntegrationObjectAuditReportBuilderIntegrationTest extends ServicelayerTest
{

	@Resource
	private AuditViewService auditViewService;
	@Resource
	private RendererService rendererService;
	@Resource
	private CommonI18NService commonI18NService;
	@Resource
	private UserService userService;
	@Resource
	private List<ReportViewConverterStrategy> reportViewConverterStrategies;
	@Resource
	private DefaultAuditConfigService auditConfigService;

	private IntegrationObjectAuditReportBuilder auditReportBuilder = new IntegrationObjectAuditReportBuilder();

	@Before
	public void setUp() throws Exception
	{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("integrationbackofficetest-integrationobject-audit.xml").getFile());
		String content = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

		setCompileSubtypeSetTestImpex007();
		setAuditReportTest008();
		importCsv("/impex/essentialdata-DefaultAuditReportBuilderTemplate.impex", "UTF-8");

		auditConfigService.storeConfiguration("IOReport", content);
		auditReportBuilder.setAuditViewService(auditViewService);
		auditReportBuilder.setCommonI18NService(commonI18NService);
		auditReportBuilder.setRendererService(rendererService);
		auditReportBuilder.setReportViewConverterStrategies(reportViewConverterStrategies);
		auditReportBuilder.setUserService(userService);
		auditReportBuilder.setConfigName("IOReport");
		auditReportBuilder.setIsDownload(false);
	}

	@After
	public void tearDown()
	{
		IntegrationTestUtil.removeAll(IntegrationObjectModel.class);
	}

	private void setCompileSubtypeSetTestImpex007() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; CompileSubtypeSetTestImpex007; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code) ",
				"; CompileSubtypeSetTestImpex007	; Order	; Order	; true	; ALL_SUB_AND_SUPER_TYPES	;  ",
				"; CompileSubtypeSetTestImpex007	; User 	; User 	; 	; ALL_SUB_AND_SUPER_TYPES	;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; CompileSubtypeSetTestImpex007:Order	; code	; Order:code	;                                   	; true	;  ",
				"; CompileSubtypeSetTestImpex007:Order	; user	; Order:user	; CompileSubtypeSetTestImpex007:User	; 	;  ",
				"; CompileSubtypeSetTestImpex007:User 	; uid 	; User:uid  	;                                   	; true	;  ",

				"INSERT_UPDATE IntegrationObjectItemVirtualAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; retrievalDescriptor(code) ",
				"; CompileSubtypeSetTestImpex007:Order	; testVA	; retrieveModelScript ",

				"INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation; type(code) ",
				"; retrieveModelScript	; model://modelScript	; java.lang.String ",

				"INSERT_UPDATE Script; code[unique = true]; scriptType(code); content ",
				"; modelScript		 ;GROOVY		   ; 'hello world from model' "

		);
	}

	private void setAuditReportTest008() throws ImpExException
	{
		importImpEx(
				"INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code) ",
				"; AuditReportTest008; INBOUND ",

				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code); root[default = false]; itemTypeMatch(code) ",
				"; AuditReportTest008	; Order	; Order	; true	; ALL_SUB_AND_SUPER_TYPES	;  ",
				"; AuditReportTest008	; User 	; User 	; 	; ALL_SUB_AND_SUPER_TYPES	;  ",

				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]; autoCreate[default = false] ",
				"; AuditReportTest008:Order	; code	; Order:code	;                                   	; true	;  ",
				"; AuditReportTest008:Order	; user	; Order:user	; AuditReportTest008:User	; 	;  ",
				"; AuditReportTest008:User 	; uid 	; User:uid  	;                                   	; true	;  ",

				"INSERT_UPDATE IntegrationObjectItemVirtualAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; retrievalDescriptor(code) ",
				"; AuditReportTest008:Order	; testVA	; retrieveModelScript ",

				"INSERT_UPDATE IntegrationObjectVirtualAttributeDescriptor; code[unique = true]; logicLocation; type(code) ",
				"; retrieveModelScript	; model://modelScript	; java.lang.String ",

				"INSERT_UPDATE Script; code[unique = true]; scriptType(code); content ",
				"; modelScript		 ;GROOVY		   ; 'hello world from model' "
		);
	}

	private String getContentBaseline()
	{
		return "{\n" +
				"                \"payload\": {\n" +
				"                    \"IntegrationObject\": {\n" +
				"                        \"code\": \"AuditReportTest008\",\n" +
				"                        \"IntegrationObjectItems\": [{\n" +
				"                            \"code\": \"User\",\n" +
				"                            \"root\": false,\n" +
				"                            \"IntegrationObjectItemClassification\": [],\n" +
				"                            \"IntegrationObjectItemAttributes\": [{\n" +
				"                                \"isUnique\": true,\n" +
				"                                \"attributeName\": \"uid\",\n" +
				"                                \"autoCreate\": false\n" +
				"                            }],\n" +
				"                            \"ComposedTypeOfItem\": {\"ComposedType_\": \"User\"},\n" +
				"                            \"VirtualAttribute\": []\n" +
				"                        }, {\n" +
				"                            \"code\": \"Order\",\n" +
				"                            \"root\": true,\n" +
				"                            \"IntegrationObjectItemClassification\": [],\n" +
				"                            \"IntegrationObjectItemAttributes\": [{\n" +
				"                                \"isUnique\": true,\n" +
				"                                \"attributeName\": \"code\",\n" +
				"                                \"autoCreate\": false\n" +
				"                            }, {\n" +
				"                                \"isUnique\": false,\n" +
				"                                \"attributeName\": \"user\",\n" +
				"                                \"autoCreate\": false,\n" +
				"                                \"ComposedType_\": \"User\"\n" +
				"                            }],\n" +
				"                            \"ComposedTypeOfItem\": {\"ComposedType_\": \"Order\"},\n" +
				"                            \"VirtualAttribute\": [{\"attributeName\": \"testVA\"}]\n" +
				"                        }]\n" +
				"                    }\n" +
				"                },\n" +
				"                \"changingUser\": \"anonymous\",\n" +
				"                \"context\": {\"changedObfuscatedAttributes\": []},\n" +
				"                \"timestamp\": 1597249639848\n" +
				"            }";
	}

	@Test
	public void generateAndCompareAuditReportTest() throws IOException
	{
		// generate first audit report from one integration object
		IntegrationObjectModel integrationObjectModel = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CompileSubtypeSetTestImpex007");
		assertNotNull(integrationObjectModel);
		Map<String, InputStream> reportGenerateRes = auditReportBuilder.generateAuditReport(integrationObjectModel);
		byte[] arr1 = null;
		for (InputStream inputStream : reportGenerateRes.values())
		{
			arr1 = inputStream.readAllBytes();
		}

		// generate second audit report from same integration object as the first one
		IntegrationObjectModel integrationObjectModelCopy = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"CompileSubtypeSetTestImpex007");
		assertNotNull(integrationObjectModel);
		Map<String, InputStream> reportGenerateResCopy = auditReportBuilder.generateAuditReport(integrationObjectModelCopy);
		byte[] arr2 = null;
		for (InputStream inputStream : reportGenerateResCopy.values())
		{
			arr2 = inputStream.readAllBytes();
		}

		// generate third audit report from different integration object
		IntegrationObjectModel integrationObjectModelAnother = IntegrationObjectTestUtil.findIntegrationObjectByCode(
				"AuditReportTest008");
		assertNotNull(integrationObjectModel);
		Map<String, InputStream> reportGenerateResAnother = auditReportBuilder.generateAuditReport(integrationObjectModelAnother);
		byte[] arr3 = null;
		for (InputStream inputStream : reportGenerateResAnother.values())
		{
			arr3 = inputStream.readAllBytes();
		}

		assertEquals(integrationObjectModel, integrationObjectModelCopy);
		assertNotNull(arr1);
		assertNotNull(arr2);
		assertEquals(getReportContent(new String(arr1)), getReportContent(new String(arr2)));

		assertNotNull(arr3);
		assertNotEquals(integrationObjectModel, integrationObjectModelAnother);
		assertNotEquals(getReportContent(new String(arr1)), getReportContent(new String(arr3)));

		// get json object from data just fetched
		String htmlContent = getReportContent(new String(arr3));
		final JsonParser parser = new JsonParser();
		final JsonObject jsonObjectNew = parser.parse(htmlContent).getAsJsonObject();

		// get json object from baseline
		String htmlContentBaseline = getContentBaseline();
		final JsonObject jsonObjectBaseline = parser.parse(htmlContentBaseline).getAsJsonObject();

		// compare json data
		assertEquals(getReportUser(jsonObjectBaseline), getReportUser(jsonObjectNew));
		assertEquals(getIOCode(jsonObjectBaseline), getIOCode(jsonObjectNew));
		JsonArray itemBaselineList = getIOItemList(jsonObjectBaseline);
		JsonArray itemList = getIOItemList(jsonObjectNew);
		assertEquals(itemList.size(), itemBaselineList.size());

		JsonElement userIOIABaseline = getUserIOItem(itemBaselineList).get("IntegrationObjectItemAttributes")
		                                                              .getAsJsonArray()
		                                                              .get(0);
		JsonElement userIOIANew = getUserIOItem(itemList).get("IntegrationObjectItemAttributes").getAsJsonArray().get(0);
		assertEquals(userIOIABaseline.toString(), userIOIANew.toString());

		String virtualAttributeNameNew = getOrderIOItem(itemList).get("VirtualAttribute")
		                                                         .getAsJsonArray()
		                                                         .get(0)
		                                                         .getAsJsonObject()
		                                                         .get("attributeName")
		                                                         .getAsString();
		String virtualAttributeNameBaseline = getOrderIOItem(itemBaselineList).get("VirtualAttribute")
		                                                                      .getAsJsonArray()
		                                                                      .get(0)
		                                                                      .getAsJsonObject()
		                                                                      .get("attributeName")
		                                                                      .getAsString();
		assertEquals(virtualAttributeNameNew, virtualAttributeNameBaseline);

	}

	private JsonObject getUserIOItem(JsonArray itemList)
	{
		if (itemList.get(0).getAsJsonObject().get("code").getAsString().equalsIgnoreCase("user"))
		{
			return itemList.get(0).getAsJsonObject();
		}
		return itemList.get(1).getAsJsonObject();
	}

	private JsonObject getOrderIOItem(JsonArray itemList)
	{
		if (itemList.get(0).getAsJsonObject().get("code").getAsString().equalsIgnoreCase("order"))
		{
			return itemList.get(0).getAsJsonObject();
		}
		return itemList.get(1).getAsJsonObject();
	}

	private String getReportContent(String htmlFile)
	{
		htmlFile = htmlFile.substring(htmlFile.indexOf("<script>") + 8, htmlFile.indexOf("</script>"));
		htmlFile = htmlFile.substring(htmlFile.indexOf("=") + 1, htmlFile.lastIndexOf(";"));
		return htmlFile.substring(htmlFile.indexOf("[") + 1, htmlFile.lastIndexOf("]"));
	}

	private String getReportUser(final JsonObject jsonObject)
	{
		return jsonObject.get("changingUser").toString();
	}

	private String getIOCode(final JsonObject jsonObject)
	{
		return jsonObject.getAsJsonObject("payload").getAsJsonObject("IntegrationObject").get("code").toString();
	}

	private JsonArray getIOItemList(final JsonObject jsonObject)
	{
		return jsonObject.getAsJsonObject("payload").getAsJsonObject("IntegrationObject")
		                 .getAsJsonArray("IntegrationObjectItems");
	}

}
