package de.hybris.platform.outboundsyncbackoffice.widgets.models.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.audit.internal.config.DefaultAuditConfigService;
import de.hybris.platform.audit.view.AuditViewService;
import de.hybris.platform.auditreport.service.ReportViewConverterStrategy;
import de.hybris.platform.commons.renderer.RendererService;
import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.outboundsync.model.OutboundChannelConfigurationModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
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

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@IntegrationTest
public class OutboundChannelConfigurationAuditReportBuilderIntegrationTest extends ServicelayerTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
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

	private OutboundChannelConfigurationAuditReportBuilder auditReportBuilder = new OutboundChannelConfigurationAuditReportBuilder();

	@Before
	public void setUp() throws Exception
	{

		final ClassLoader classLoader = getClass().getClassLoader();
		final File file = new File(
				classLoader.getResource("outboundsyncbackoffice-OutboundChannelConfiguration-audit.xml").getFile());
		final String content = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);

		IntegrationTestUtil.importImpEx("INSERT_UPDATE IntegrationObject; code[unique = true]; integrationType(code)\n" +
				"                               ; OutboundProductTest    ; INBOUND\n" +
				"\n" +
				"INSERT_UPDATE IntegrationObjectItem; integrationObject(code)[unique = true]; code[unique = true]; type(code)     ; root[default = false]\n" +
				"                                   ; OutboundProductTest                       ; Product            ; Product        ; true\n" +
				"                                   ; OutboundProductTest                       ; Catalog            ; Catalog        ;\n" +
				"                                   ; OutboundProductTest                       ; CatalogVersion     ; CatalogVersion ;\n" +
				"                                   ; OutboundProductTest                       ; Category           ; Category       ;\n" +
				"\n" +
				"INSERT_UPDATE IntegrationObjectItemAttribute; integrationObjectItem(integrationObject(code), code)[unique = true]; attributeName[unique = true]; attributeDescriptor(enclosingType(code), qualifier); returnIntegrationObjectItem(integrationObject(code), code); unique[default = false]\n" +
				"                                            ; OutboundProductTest:Catalog                                            ; id                          ; Catalog:id                                         ;\n" +
				"                                            ; OutboundProductTest:CatalogVersion                                     ; catalog                     ; CatalogVersion:catalog                             ; OutboundProductTest:Catalog\n" +
				"                                            ; OutboundProductTest:CatalogVersion                                     ; version                     ; CatalogVersion:version                             ;\n" +
				"                                            ; OutboundProductTest:CatalogVersion                                     ; active                      ; CatalogVersion:active                              ;\n" +
				"                                            ; OutboundProductTest:Product                                            ; code                        ; Product:code                                       ;\n" +
				"                                            ; OutboundProductTest:Product                                            ; catalogVersion              ; Product:catalogVersion                             ; OutboundProductTest:CatalogVersion\n" +
				"                                            ; OutboundProductTest:Product                                            ; name                        ; Product:name                                       ;\n" +
				"                                            ; OutboundProductTest:Product                                            ; supercategories             ; Product:supercategories                            ; OutboundProductTest:Category\n" +
				"                                            ; OutboundProductTest:Category                                           ; code                        ; Category:code                                      ;\n" +
				"                                            ; OutboundProductTest:Category                                           ; name                        ; Category:name                                      ;\n" +
				"\n" +
				"INSERT_UPDATE OutboundSyncStreamConfigurationContainer; id[unique = true]       ;\n" +
				"                                                      ; outboundSyncDataStreamsTest ;\n" +
				"\n" +
				"INSERT_UPDATE OutboundSyncJob; code[unique = true]  ; streamConfigurationContainer(id)\n" +
				"                             ; odataOutboundSyncJobTest ; outboundSyncDataStreamsTest\n" +
				"\n" +
				"INSERT_UPDATE OutboundSyncCronJob; code[unique = true] ; job(code)            ; sessionLanguage(isoCode)[default = en]\n" +
				"                                 ; outboundSyncCronJobTest ; odataOutboundSyncJobTest ;\n" +
				"\n" +
				"INSERT_UPDATE BasicCredential; id[unique = true]   ; username; password\n" +
				"                             ; BasicCredentialTest ; abc     ; 123\n" +
				"\n" +
				"INSERT_UPDATE Endpoint; id[unique = true]; version[unique = true]; name         ; specUrl\n" +
				"                      ; local-hybrisTest     ; unknown               ; local-hybrisTest ; https://localhost:9002/odata2webservices/OutboundProduct/$metadata?Product\n" +
				"\n" +
				"INSERT_UPDATE DestinationTarget; id[unique = true]\n" +
				"                               ; stoutoutboundtest1\n" +
				"\n" +
				"INSERT_UPDATE ConsumedDestination; id[unique = true]; url                  ; endpoint(id, version); credential(id); destinationTarget(id)[default = stoutoutboundtest1]\n" +
				"                                 ; platform-basicTest   ; https://localhost:9002/odata2webservices/OutboundProduct/Products ; local-hybrisTest:unknown ; BasicCredentialTest\n" +
				"\n" +
				"INSERT_UPDATE OutboundSyncStreamConfiguration; streamId[unique = true]; container(id)           ; itemTypeForStream(code); outboundChannelConfiguration(code)\n" +
				"                                             ; productStream          ; outboundSyncDataStreamsTest ; Product                ; outboundChannelConfigTest\n" +
				"\n" +
				"INSERT_UPDATE OutboundChannelConfiguration; code[unique = true]; integrationObject(code); destination(id)\n" +
				"                                          ; outboundChannelConfigTest    ; OutboundProductTest        ; platform-basicTest");

		importCsv("/impex/essentialdata-DefaultAuditReportBuilderTemplate.impex",
				"UTF-8");  //This file is too big to use IntegrationTestUtil.importImpEx.

		auditConfigService.storeConfiguration("OutboundChannelConfigurationReport", content);
		auditReportBuilder.setAuditViewService(auditViewService);
		auditReportBuilder.setCommonI18NService(commonI18NService);
		auditReportBuilder.setRendererService(rendererService);
		auditReportBuilder.setReportViewConverterStrategies(reportViewConverterStrategies);
		auditReportBuilder.setUserService(userService);
		auditReportBuilder.setConfigName("OutboundChannelConfigurationReport");
		auditReportBuilder.setIsDownload(false);
	}

	@Test
	public void generateAndCompareAuditReportTest() throws IOException
	{
		// generate first audit report from one integration object
		final SearchResult<OutboundChannelConfigurationModel> search = flexibleSearchService.search(
				"SELECT PK FROM {OutboundChannelConfiguration} WHERE (p_code = 'outboundChannelConfigTest')");
		final OutboundChannelConfigurationModel outboundChannelConfigurationModel = search.getResult().get(0);
		final Map<String, InputStream> reportGenerateRes = auditReportBuilder.generateAuditReport(
				outboundChannelConfigurationModel);
		byte[] arr1 = null;
		assertEquals(1, reportGenerateRes.values().size());
		for (InputStream inputStream : reportGenerateRes.values())
		{
			arr1 = inputStream.readAllBytes();
		}
		if (arr1 == null)
		{
			fail("Audit report is not generated. No data found.");
		}

		//  if baseline changed, update it
		//  Files.write(Paths.get("./outboundChannelConfigAuditReportBuilderBaseline.html"), arr1);

		final JsonParser parser = new JsonParser();

		// get json object from data just fetched
		String htmlContent = new String(arr1);
		htmlContent = htmlContent.substring(htmlContent.indexOf("<script>") + 8, htmlContent.indexOf("</script>"));
		htmlContent = htmlContent.substring(htmlContent.indexOf("=") + 1, htmlContent.lastIndexOf(";"));
		htmlContent = htmlContent.substring(htmlContent.indexOf("["), htmlContent.lastIndexOf("]") + 1);

		ClassLoader classLoader = getClass().getClassLoader();
		String htmlPath = "test/text/OutboundChannelConfigAuditReportBuilderBaseline.html";
		File file = new File(classLoader.getResource(htmlPath).getFile());
		String htmlContentBaseline = Files.readString(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
		htmlContentBaseline = htmlContentBaseline.substring(htmlContentBaseline.indexOf("<script>") + 8,
				htmlContentBaseline.indexOf("</script>"));
		htmlContentBaseline = htmlContentBaseline.substring(htmlContentBaseline.indexOf("=") + 1,
				htmlContentBaseline.lastIndexOf(";"));
		htmlContentBaseline = htmlContentBaseline.substring(htmlContentBaseline.indexOf("["),
				htmlContentBaseline.lastIndexOf("]") + 1);
		JsonArray jsonObjectListNew = parser.parse(htmlContent).getAsJsonArray();
		JsonObject jsonObjectNew = jsonObjectListNew.get(jsonObjectListNew.size() - 1).getAsJsonObject();
		JsonArray jsonObjectListBaseline = parser.parse(htmlContentBaseline).getAsJsonArray();
		JsonObject jsonObjectBaseline = jsonObjectListBaseline.get(jsonObjectListBaseline.size() - 1).getAsJsonObject();

		JsonObject OBChannelConfigNew = jsonObjectNew.getAsJsonObject("payload").getAsJsonObject("OutboundChannelConfiguration");
		JsonObject OBChannelConfigBaseline = jsonObjectBaseline.getAsJsonObject("payload")
		                                                       .getAsJsonObject("OutboundChannelConfiguration");
		assertEquals(OBChannelConfigNew.get("code").toString(), OBChannelConfigBaseline.get("code").toString());

		JsonObject ConsumedDestinationNew = OBChannelConfigNew.getAsJsonObject("ConsumedDestination_id : platform-basicTest");
		JsonObject ConsumedDestinationBase = OBChannelConfigBaseline.getAsJsonObject(
				"ConsumedDestination_id : platform-basicTest");
		assertEquals(ConsumedDestinationNew.getAsJsonObject("Credential_id : BasicCredentialTest").toString(),
				ConsumedDestinationBase.getAsJsonObject("Credential_id : BasicCredentialTest").toString());
		assertEquals(ConsumedDestinationNew.getAsJsonObject("DestinationTarget_id : stoutoutboundtest1").toString(),
				ConsumedDestinationBase.getAsJsonObject("DestinationTarget_id : stoutoutboundtest1").toString());
		assertEquals(ConsumedDestinationNew.getAsJsonObject("Endpoint_id : local-hybrisTest").toString(),
				ConsumedDestinationBase.getAsJsonObject("Endpoint_id : local-hybrisTest").toString());
		assertEquals(ConsumedDestinationNew.getAsJsonObject("Endpoint_id : local-hybrisTest").toString(),
				ConsumedDestinationBase.getAsJsonObject("Endpoint_id : local-hybrisTest").toString());
		assertEquals(ConsumedDestinationNew.get("url").toString(),
				ConsumedDestinationBase.get("url").toString());
		assertEquals(ConsumedDestinationNew.get("active").toString(),
				ConsumedDestinationBase.get("active").toString());
		assertEquals(ConsumedDestinationNew.get("additional Properties").toString(),
				ConsumedDestinationBase.get("additional Properties").toString());

		JsonObject IntegrationObjectNew = OBChannelConfigNew.getAsJsonObject("IntegrationObject_id : OutboundProductTest");
		JsonObject IntegrationObjectBaseline = OBChannelConfigBaseline.getAsJsonObject(
				"IntegrationObject_id : OutboundProductTest");
		assertEquals(IntegrationObjectNew.toString(), IntegrationObjectBaseline.toString());

		JsonObject OutboundSyncStreamConfigurationNew = OBChannelConfigNew.getAsJsonObject(
				"OutboundSyncStreamConfiguration_id : productStream");
		JsonObject OutboundSyncStreamConfigurationNewBaseline = OBChannelConfigBaseline.getAsJsonObject(
				"OutboundSyncStreamConfiguration_id : productStream");
		assertEquals(OutboundSyncStreamConfigurationNew.getAsJsonObject(
				"OutboundSyncStreamConfigurationContainer_id : outboundSyncDataStreamsTest").toString(),
				OutboundSyncStreamConfigurationNewBaseline.getAsJsonObject(
						"OutboundSyncStreamConfigurationContainer_id : outboundSyncDataStreamsTest").toString());
	}
}

