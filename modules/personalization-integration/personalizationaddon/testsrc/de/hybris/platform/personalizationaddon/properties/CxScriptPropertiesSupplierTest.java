/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationaddon.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationaddon.data.CxViewActionResult;
import de.hybris.platform.personalizationaddon.data.CxViewValueCoder;
import de.hybris.platform.personalizationaddon.data.impl.Base64ValueCoder;
import de.hybris.platform.personalizationcms.data.CxCmsActionResult;
import de.hybris.platform.personalizationfacades.customersegmentation.CustomerSegmentationFacade;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.personalizationservices.data.CxAbstractActionResult;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CxScriptPropertiesSupplierTest
{
	private static final String CONTAINER = "container";
	private static final String ACTION = "action ";
	private static final String VARIATION = "variation ";
	private static final String CUSTOMIZATION = "customization ";
	private static final String NAME = "name ";

	@Mock
	private CxService cxService;

	@Mock
	private CustomerSegmentationFacade customerSegmentationFacade;

	@Mock
	private UserService userService;

	@InjectMocks
	private CxScriptPropertiesSupplier supplier;

	private final CxViewValueCoder coder = new Base64ValueCoder();

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		supplier.setCxViewValueCoder(coder);

		final UserModel user = new UserModel();

		doReturn(user).when(userService).getCurrentUser();
		doReturn(getActionResults()).when(cxService).getActionResultsFromSession(user);
		doReturn(getSegmentData()).when(customerSegmentationFacade).getSegmentsForCurrentUser();
	}

	private List<CxAbstractActionResult> getActionResults()
	{
		return Stream.of("1", "2", "3").map(id -> {
			CxCmsActionResult result = new CxCmsActionResult();
			result.setComponentId(id);
			result.setContainerId(CONTAINER);
			result.setActionCode(ACTION + id);
			result.setVariationCode(VARIATION + id);
			result.setVariationName(VARIATION + NAME + id);
			result.setCustomizationCode(CUSTOMIZATION + id);
			result.setCustomizationName(CUSTOMIZATION + NAME + id);
			return result;
		}).collect(Collectors.toList());
	}

	private List<SegmentData> getSegmentData()
	{
		return Stream.of("1", "2", "3").map(id -> {
			SegmentData result = new SegmentData();
			result.setCode(id);
			return result;
		}).collect(Collectors.toList());
	}


	@Test
	public void getEncodedActionResultsTest()
	{
		//given

		//when
		final List<CxViewActionResult> encodedActionResults = supplier.getEncodedActionResults();

		//then
		assertNotNull(encodedActionResults);
		assertEquals(3, encodedActionResults.size());

		final CxViewActionResult ar = encodedActionResults.get(1);

		assertEquals(coder.encode(CxCmsActionResult.class.getSimpleName()), ar.getType());
		assertEquals(coder.encode(ACTION + "2"), ar.getActionCode());
		assertEquals(coder.encode(VARIATION + "2"), ar.getVariationCode());
		assertEquals(coder.encode(VARIATION + NAME + "2"), ar.getVariationName());
		assertEquals(coder.encode(CUSTOMIZATION + "2"), ar.getCustomizationCode());
		assertEquals(coder.encode(CUSTOMIZATION + NAME + "2"), ar.getCustomizationName());
	}

	@Test
	public void getFormatedActionResultsTest()
	{
		//given

		//when
		final List<Object> encodedActionResults = supplier.getFormatedActionResults();

		//then
		assertNotNull(encodedActionResults);
		assertEquals(3, encodedActionResults.size());

		final Object actionResult = encodedActionResults.get(2);
		final Map<String, String> ar = (Map<String, String>) actionResult;

		assertEquals(CxCmsActionResult.class.getSimpleName(), coder.decode(ar.get("action_type")));
		assertEquals(ACTION + "3", coder.decode(ar.get("action_code")));
		assertEquals(VARIATION + "3", coder.decode(ar.get("variation_code")));
		assertEquals(VARIATION + NAME + "3", coder.decode(ar.get("variation_name")));
		assertEquals(CUSTOMIZATION + "3", coder.decode(ar.get("customization_code")));
		assertEquals(CUSTOMIZATION + NAME + "3", coder.decode(ar.get("customization_name")));
	}

	@Test
	public void getFormatedSegmentDataTest()
	{
		//given

		//when
		final List<Object> encodedSegmentData = supplier.getFormatedSegmentData();

		//then
		assertNotNull(encodedSegmentData);
		assertEquals(3, encodedSegmentData.size());

		final String segmentData = (String) encodedSegmentData.get(1);
		assertEquals("2", coder.decode(segmentData));
	}

	@Test
	public void getEncodedSegmentDataTest()
	{
		//given

		//when
		final List<SegmentData> encodedSegmentData = supplier.getEncodedSegmentData();

		//then
		assertNotNull(encodedSegmentData);
		assertEquals(3, encodedSegmentData.size());

		final SegmentData segmentData = encodedSegmentData.get(1);
		assertEquals(coder.encode("2"), segmentData.getCode());
	}
}
