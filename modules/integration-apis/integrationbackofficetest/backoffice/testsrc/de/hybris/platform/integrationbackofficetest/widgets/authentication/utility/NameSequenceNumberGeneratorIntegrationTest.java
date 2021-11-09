package de.hybris.platform.integrationbackofficetest.widgets.authentication.utility;


import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.model.ExposedDestinationModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.integrationbackoffice.widgets.authentication.utility.impl.NameSequenceNumberGenerator;

import de.hybris.platform.integrationservices.util.IntegrationTestUtil;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;


@IntegrationTest
public class NameSequenceNumberGeneratorIntegrationTest extends ServicelayerTest
{
	@Resource
	private FlexibleSearchService flexibleSearchService;
	private NameSequenceNumberGenerator occNameConvention;
	private static final String END_POINT_TABLE = "EndPoint";
	private static final String EXPOSED_DESTINATION_TABLE = "ExposedDestination";
	private static final String IO_CODE = "iocode";

	@Before
	public void setUp() throws Exception
	{
		occNameConvention = new NameSequenceNumberGenerator(flexibleSearchService);
		insertRecords();
	}

	private void insertRecords() throws ImpExException
	{
		IntegrationTestUtil.importImpEx(
				"INSERT_UPDATE DestinationTarget ; Id[unique = true];",
				";destinationTarget-1 ;",

				"$version=v1",
				"$specUrl1=https://<your-host-name>/odata2webservices/iocode/$metadata",
				"$specUrl2=https://<your-host-name>/odata2webservices/someotheriocode/$metadata",
				"$name1=iocode-endpoint",
				"$name2=someotheriocode-endpoint",
				"INSERT_UPDATE EndPoint ; id[unique = true] ; name ; version ; specUrl;",
				";cc-iocode                     ; $name1 ; $version ; $specUrl1",
				";cc-iocode-metadata            ; $name1 ; $version ; $specUrl1",
				";cc-iocode-1-metadata          ; $name1 ; $version ; $specUrl1",
				";cc-iocode-123                 ; $name1 ; $version ; $specUrl1",
				";cc-iocode-abc-metadata        ; $name1 ; $version ; $specUrl1",


				";iocode                        ; $name1 ; $version ; $specUrl1",
				";someotheriocode               ; $name2 ; $version ; $specUrl2",
				";cc-IOCODE-1-metadata          ; $name1 ; $version ; $specUrl1",
				";cc-someotheriocode-1-metadata ; $name2 ; $version ; $specUrl2",


				"$url1=https://<your-host-name>/odata2webservices/iocode",
				"$url2=https://<your-host-name>/odata2webservices/someotheriocode",
				"$destinationTarget=destinationTarget-1",
				"$endpoint1=cc-iocode-metadata",
				"$endpoint2=cc-someotheriocode-1-metadata",
				"INSERT_UPDATE ExposedDestination ; id[unique = true] ; endpoint(id) ; destinationTarget(id) ; url;",
				";cc-iocode             ; $endpoint1 ; $destinationTarget ; $url1",
				";cc-iocode-1           ; $endpoint1 ; $destinationTarget ; $url1",
				";cc-iocode-1a          ; $endpoint1 ; $destinationTarget ; $url1",
				";cc-iocode-abc         ; $endpoint1 ; $destinationTarget ; $url1",

				";iocode                ; $endpoint1 ; $destinationTarget ; $url1",
				";someotheriocode       ; $endpoint2 ; $destinationTarget ; $url2",
				";cc-IOCODE-1           ; $endpoint1 ; $destinationTarget ; $url1",
				";cc-someotheriocode-1  ; $endpoint2 ; $destinationTarget ; $url2"
		);
	}

	@After
	public void deleteRecords()
	{
		IntegrationTestUtil.remove(ExposedDestinationModel.class, obj -> obj.getId().toLowerCase().contains("iocode"));
		IntegrationTestUtil.remove(EndpointModel.class, obj -> obj.getId().toLowerCase().contains("iocode"));
		IntegrationTestUtil.remove(DestinationTargetModel.class, obj -> obj.getId().equals("destinationTarget-1"));
	}

	@Test
	public void checkListOfEndPointIDs()
	{
		final List<String> actualEndPointIDs = callGetIDsMethod(END_POINT_TABLE, IO_CODE);

		final List<String> expectedEndPointIDs = Arrays.asList("cc-iocode-1-metadata", "cc-iocode-metadata");
		assertEquals(actualEndPointIDs, expectedEndPointIDs);
	}

	@Test
	public void checkListOfExposedDestinationIDs()
	{
		final List<String> actualExposedDestinationIDs = callGetIDsMethod(EXPOSED_DESTINATION_TABLE, IO_CODE);
		final List<String> expectedExposedDestinationIDs = Arrays.asList("cc-iocode", "cc-iocode-1");
		assertEquals(actualExposedDestinationIDs, expectedExposedDestinationIDs);
	}

	private List<String> callGetIDsMethod(final String tableName, final String ioCode)
	{
		final Method method = ReflectionUtils.findMethod(NameSequenceNumberGenerator.class, "getIDs",
				new Class<?>[]{ String.class, String.class });
		method.setAccessible(true);
		return (List<String>) ReflectionUtils.invokeMethod(method, occNameConvention, tableName, ioCode);
	}
}
