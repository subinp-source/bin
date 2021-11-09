/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.consignmenttrackingfacades.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.consignmenttrackingservices.service.ConsignmentTrackingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultConsignmentTrackingFacadeTest
{

	private DefaultConsignmentTrackingFacade consignmentTrackingFacade;

	@Mock
	private ConsignmentTrackingService consignmentTrackingService;

	@Mock
	private Converter<ConsignmentModel, ConsignmentData> consignmentConverter;

	private ConsignmentModel consignmentModel;
	private List<ConsignmentModel> models;
	private static final String ORDER_CODE = "10001000";
	private static final String CONSIGNMENT_CODE = "a10001000";

	@Before
	public void prepare()
	{

		MockitoAnnotations.initMocks(this);

		consignmentModel = new ConsignmentModel();
		models = new ArrayList<>(0);
		models.add(consignmentModel);

		consignmentTrackingFacade = new DefaultConsignmentTrackingFacade();
		consignmentTrackingFacade.setConsignmentConverter(consignmentConverter);
		consignmentTrackingFacade.setConsignmentTrackingService(consignmentTrackingService);
	}

	@Test
	public void testGetConsignmentByCode()
	{
		final ConsignmentData consignmentData = mock(ConsignmentData.class);

		given(consignmentTrackingService.getConsignmentForCode(ORDER_CODE, CONSIGNMENT_CODE)).willReturn(
				Optional.of(consignmentModel));
		given(consignmentConverter.convert(consignmentModel)).willReturn(consignmentData);

		final Optional<ConsignmentData> optional = consignmentTrackingFacade.getConsignmentByCode(ORDER_CODE, CONSIGNMENT_CODE);
		Assert.assertEquals(consignmentData, optional.get());

	}

	@Test
	public void testGetConsignmentByOrder()
	{
		final ConsignmentData data = new ConsignmentData();
		final List<ConsignmentData> consignmentDataList = new ArrayList<>();
		consignmentDataList.add(data);

		given(consignmentTrackingService.getConsignmentsForOrder(ORDER_CODE))
				.willReturn(models);
		given(consignmentConverter.convertAll(models)).willReturn(consignmentDataList);

		final List<ConsignmentData> datas = consignmentTrackingFacade.getConsignmentsByOrder(ORDER_CODE);
		Assert.assertEquals(data, datas.get(0));

	}

	@Test
	public void testGetConsignmentByOrderNoconsignments()
	{
		given(consignmentTrackingService.getConsignmentsForOrder(ORDER_CODE)).willReturn(Collections.emptyList());
		final List<ConsignmentData> datas = consignmentTrackingFacade.getConsignmentsByOrder(ORDER_CODE);
		Assert.assertEquals(0, datas.size());
	}

	@Test
	public void testGetTrackingUrlForConsignment() throws MalformedURLException
	{
		final URL url = new URL("https://www.hybris.com");

		given(consignmentTrackingService.getConsignmentForCode(ORDER_CODE, CONSIGNMENT_CODE))
				.willReturn(Optional.of(consignmentModel));
		given(consignmentTrackingService.getTrackingUrlForConsignment(consignmentModel)).willReturn(url);

		final String result = consignmentTrackingFacade.getTrackingUrlForConsignmentCode(ORDER_CODE, CONSIGNMENT_CODE);
		Assert.assertEquals(url.toString(), result);
	}

	@Test
	public void testGetTrackingUrlForConsignmentWithNullUrl()
	{
		given(consignmentTrackingService.getConsignmentForCode(ORDER_CODE, CONSIGNMENT_CODE))
				.willReturn(Optional.of(consignmentModel));
		given(consignmentTrackingService.getTrackingUrlForConsignment(consignmentModel)).willReturn(null);

		final String result = consignmentTrackingFacade.getTrackingUrlForConsignmentCode(ORDER_CODE, CONSIGNMENT_CODE);
		Assert.assertEquals("about:blank", result);
	}
}
