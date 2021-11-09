/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.order.dao.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.basecommerce.util.BaseCommerceBaseTest;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.impl.DefaultBaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultAcceleratorConsignmentDaoTest extends BaseCommerceBaseTest
{

	@Resource
	private DefaultAcceleratorConsignmentDao defaultAcceleratorConsignmentDao;

	@Resource
	private DefaultBaseSiteService defaultBaseSiteService;

	@Resource
	private ModelService modelService;

	@Resource
	private WarehouseService warehouseService;

	@Resource
	private AddressService addressService;

	@Resource
	private UserService userService;

	@Resource
	private CustomerAccountService customerAccountService;

	@Resource
	private BaseStoreService baseStoreService;

	@Before
	public void setUp() throws ImpExException
	{
		ServicelayerTest.importCsv("/acceleratorservices/test/consignmentDaoTestData.csv", "windows-1252");

	}

	/**
	 * see ACC-6734
	 */
	@Test
	public void shouldGetConsignmentForGivenStatusAndBaseSite()
	{
		final BaseSiteModel site = defaultBaseSiteService.getBaseSiteForUID("storetemplate");
		Assert.assertNotNull(site);
		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.PICKPACK, ConsignmentStatus.READY), List.of(site));
		assertThat(consignments).hasSize(1);
		assertThat(consignments.get(0).getOrder()).isNotNull();
		assertThat(consignments.get(0).getOrder().getSite()).isEqualTo(site);
	}

	@Test
	public void shouldGetConsignmentForGivenBaseSite()
	{
		final BaseSiteModel site = defaultBaseSiteService.getBaseSiteForUID("storetemplate");
		Assert.assertNotNull(site);
		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(), List.of(site));
		assertThat(consignments).hasSize(1);
		assertThat(consignments.get(0).getOrder()).isNotNull();
		assertThat(consignments.get(0).getOrder().getSite()).isEqualTo(site);
	}

	@Test
	public void shouldGetConsignmentForGivenStatus()
	{
		final BaseSiteModel site = defaultBaseSiteService.getBaseSiteForUID("storetemplate");
		Assert.assertNotNull(site);
		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.PICKPACK, ConsignmentStatus.READY), List.of());
		assertThat(consignments).hasSize(1);
		assertThat(consignments.get(0).getOrder()).isNotNull();
		assertThat(consignments.get(0).getOrder().getSite()).isEqualTo(site);
	}

	@Test
	public void shouldGetNoConsignmentForGivenWrongStatus()
	{
		final BaseSiteModel site = defaultBaseSiteService.getBaseSiteForUID("storetemplate");
		Assert.assertNotNull(site);
		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.READY), List.of());
		assertThat(consignments).hasSize(0);
	}

	@Test
	public void shouldGetNoConsignmentForGivenWrongBaseSite()
	{
		final BaseSiteModel wrongBaseSite = modelService.create(BaseSiteModel.class);
		wrongBaseSite.setUid("wrongSite");
		modelService.save(wrongBaseSite);

		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.PICKPACK), List.of(wrongBaseSite));
		assertThat(consignments).hasSize(0);
	}

	@Test
	public void shouldGetNoConsignmentForGivenWrongStatusAndWrongBaseSite()
	{
		final BaseSiteModel wrongBaseSite = modelService.create(BaseSiteModel.class);
		wrongBaseSite.setUid("wrongSite");
		modelService.save(wrongBaseSite);

		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.READY), List.of(wrongBaseSite));
		assertThat(consignments).hasSize(0);
	}

	@Test
	public void shouldGetNoConsignmentWithNoOrderJoined()
	{
		final AddressModel clonedAddress = cloneAddressModelForUserCode("user1");

		final WarehouseModel defaultWarehouse = warehouseService.getWarehouseForCode("defaultWarehouse");
		assertThat(defaultWarehouse).isNotNull();


		final ConsignmentModel consignmentWithNoOrder = modelService.create(ConsignmentModel.class);
		consignmentWithNoOrder.setCode("noOrder");
		consignmentWithNoOrder.setShippingAddress(clonedAddress);
		consignmentWithNoOrder.setWarehouse(defaultWarehouse);
		consignmentWithNoOrder.setStatus(ConsignmentStatus.CANCELLED);

		modelService.save(consignmentWithNoOrder);

		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.CANCELLED), List.of());
		assertThat(consignments).hasSize(0);
	}


	@Test
	public void shouldGetBothConsignmentWithGivenStatus()
	{
		final AddressModel clonedAddress = cloneAddressModelForUserCode("user1");

		final WarehouseModel defaultWarehouse = warehouseService.getWarehouseForCode("defaultWarehouse");
		assertThat(defaultWarehouse).isNotNull();

		final BaseStoreModel defaultstore = baseStoreService.getBaseStoreForUid("defaultstore");
		assertThat(defaultstore).isNotNull();

		final OrderModel order1 = customerAccountService.getOrderForCode("order1", defaultstore);
		Assert.assertNotNull(order1);

		final ConsignmentModel consignmentWithNoOrder = modelService.create(ConsignmentModel.class);
		consignmentWithNoOrder.setCode("order1consignment2");
		consignmentWithNoOrder.setShippingAddress(clonedAddress);
		consignmentWithNoOrder.setWarehouse(defaultWarehouse);
		consignmentWithNoOrder.setOrder(order1);
		consignmentWithNoOrder.setStatus(ConsignmentStatus.CANCELLED);

		modelService.save(consignmentWithNoOrder);

		final List<ConsignmentModel> consignments = defaultAcceleratorConsignmentDao.findConsignmentsForStatus(
				List.of(ConsignmentStatus.CANCELLED, ConsignmentStatus.PICKPACK), List.of());
		assertThat(consignments).hasSize(2)
		                        .extracting(ConsignmentModel::getCode)
		                        .containsExactlyInAnyOrder("order1consignment2", "consignment1");
	}

	private AddressModel cloneAddressModelForUserCode(final String userCode)
	{
		final Collection<AddressModel> addresses = addressService.getAddressesForOwner(userService.getUserForUID(userCode));

		assertThat(addresses).isNotEmpty();

		final AddressModel clonedAddress = addressService.cloneAddress(addresses.iterator().next());
		assertThat(clonedAddress).isNotNull();

		modelService.save(clonedAddress);
		return clonedAddress;
	}
}