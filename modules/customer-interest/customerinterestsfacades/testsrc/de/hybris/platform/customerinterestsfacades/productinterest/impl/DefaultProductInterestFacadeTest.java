/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestEntryData;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestRelationData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.customerinterestsservices.productinterest.ProductInterestService;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.ObjectUtils;



@UnitTest
public class DefaultProductInterestFacadeTest
{
	private DefaultProductInterestFacade productInterestFacade;
	private ProductInterestData productInterest;

	@Mock
	private ProductInterestService productInterestService;
	@Mock
	private Converter<ProductInterestModel, ProductInterestData> productInterestConverter;
	@Mock
	private Converter<ProductInterestData, ProductInterestModel> productInterestReverseConverter;
	@Mock
	private ProductService productService;
	@Mock
	private UserService userService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private Converter<Entry<ProductModel, List<ProductInterestEntryData>>, ProductInterestRelationData> productInterestRelationConverter;
	@Mock
	private Converter<Entry<NotificationType, Date>, ProductInterestEntryData> productInterestEntryConverter;

	private ProductModel product;
	private BaseStoreModel baseStore;
	private CustomerModel customer;
	private BaseSiteModel baseSite;
	private ProductData productData;
	private NotificationPreferenceData preference;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		preference = new NotificationPreferenceData();
		preference.setChannel(NotificationChannel.SMS);
		preference.setValue("123456789");
		preference.setEnabled(true);

		productInterest = new ProductInterestData();
		productInterest.setNotificationType(NotificationType.NOTIFICATION);
		productInterest.setNotificationChannels(Arrays.asList(preference));
		productData = new ProductData();

		productData.setCode("13810");
		productInterest.setProduct(productData);

		productInterestFacade = Mockito.spy(new DefaultProductInterestFacade());
		productInterestFacade.setBaseSiteService(baseSiteService);
		productInterestFacade.setBaseStoreService(baseStoreService);
		productInterestFacade.setProductInterestConverter(productInterestConverter);
		productInterestFacade.setProductInterestReverseConverter(productInterestReverseConverter);
		productInterestFacade.setProductInterestService(productInterestService);
		productInterestFacade.setUserService(userService);
		productInterestFacade.setProductService(productService);

		product = new ProductModel();
		product.setCode("13810");
		Mockito.when(productService.getProductForCode(Mockito.anyString())).thenReturn(product);

		baseStore = new BaseStoreModel();
		Mockito.when(baseStoreService.getCurrentBaseStore()).thenReturn(baseStore);

		customer = new CustomerModel();
		Mockito.when(userService.getCurrentUser()).thenReturn(customer);

		baseSite = new BaseSiteModel();
		Mockito.when(baseSiteService.getCurrentBaseSite()).thenReturn(baseSite);
	}

	@Test
	public void saveProductInterestTest()
	{
		final ProductInterestModel productInterestModel = new ProductInterestModel();
		productInterestModel.setNotificationChannels(Collections.singleton(NotificationChannel.EMAIL));

		Mockito.when(productInterestFacade.getProductInterest(productInterest.getProduct().getCode(),
				productInterest.getNotificationType())).thenReturn(Optional.of(productInterestModel));

		productInterestFacade.saveProductInterest(productInterest);

		Mockito.verify(productInterestFacade.getProductInterestReverseConverter(), Mockito.times(1)).convert(productInterest,
				productInterestModel);
		Mockito.verify(productInterestFacade.getProductInterestService(), Mockito.times(1))
				.saveProductInterest(productInterestModel);

	}

	@Test
	public void removeProductInterest()
	{
		final ProductInterestModel productInterestModel = new ProductInterestModel();
		productInterestModel.setNotificationChannels(Collections.emptySet());

		Mockito.when(productInterestFacade.getProductInterest(productInterest.getProduct().getCode(),
				productInterest.getNotificationType())).thenReturn(Optional.of(productInterestModel));

		productInterestFacade.removeProductInterest(productInterest);

		Mockito.verify(productInterestFacade.getProductInterestService(), Mockito.times(1))
				.removeProductInterest(productInterestModel);
	}

	@Test
	public void getProductsByCustomerInterests()
	{
		final PageableData pageableData = new PageableData();

		final Map<ProductModel, Map<NotificationType, Date>> productModelMap = new HashMap<>();
		final Map<NotificationType, Date> notificationTypeMap = new HashMap<>();

		final Date expectedDate = new Date(20000000000L);
		notificationTypeMap.put(NotificationType.NOTIFICATION, expectedDate);

		productModelMap.put(product, notificationTypeMap);

		Mockito.when(productInterestService.getProductsByCustomerInterests(Mockito.any())).thenReturn(productModelMap);
		productInterestFacade.setProductInterestService(productInterestService);

		final ProductInterestRelationData productInterestRelation = new ProductInterestRelationData();
		final ProductData productData = new ProductData();
		productData.setCode("p1");
		productInterestRelation.setProduct(productData);
		final List<ProductInterestRelationData> productInterestRelations = new ArrayList<>();
		productInterestRelations.add(productInterestRelation);

		final List<ProductInterestEntryData> productInterestEntrys = new ArrayList<>();
		final ProductInterestEntryData productInterestEntryData = new ProductInterestEntryData();
		productInterestEntryData.setInterestType(NotificationType.NOTIFICATION.getCode());
		productInterestEntryData.setDateAdded(new Date());
		productInterestEntrys.add(productInterestEntryData);
		Mockito.when(productInterestEntryConverter.convertAll(Mockito.any())).thenReturn(productInterestEntrys);
		productInterestFacade.setProductInterestEntryConverter(productInterestEntryConverter);

		Mockito.when(productInterestRelationConverter.convertAll(Mockito.any())).thenReturn(productInterestRelations);
		productInterestFacade.setProductInterestRelationConverter(productInterestRelationConverter);

		final ProductInterestData productInterestData = new ProductInterestData();
		final NotificationPreferenceData notificationPreferenceData = new NotificationPreferenceData();
		notificationPreferenceData.setChannel(NotificationChannel.SMS);
		notificationPreferenceData.setEnabled(true);
		final List<NotificationPreferenceData> notificaitonPreferenceDatas = new ArrayList();
		notificaitonPreferenceDatas.add(notificationPreferenceData);
		productInterestData.setNotificationChannels(notificaitonPreferenceDatas);

		Mockito.doReturn(Optional.of(productInterestData)).when(productInterestFacade)
				.getProductInterestDataForCurrentCustomer(Mockito.any(), Mockito.any());

		final List<ProductInterestRelationData> productInterestsOfCustomerList = productInterestFacade
				.getProductsByCustomerInterests(pageableData);

		Assert.assertEquals(1, productInterestsOfCustomerList.size());

		final ProductInterestRelationData productInterestsOfCustomer = productInterestsOfCustomerList.get(0);
		final String actualCode = productInterestsOfCustomer.getProduct().getCode();

		Assert.assertEquals("p1", actualCode);

	}

	@Test
	public void getProductsCountByCustomerInterests()
	{
		final PageableData pageableData = new PageableData();
		final int expectedCount = 10;
		Mockito.when(productInterestService.getProductsCountByCustomerInterests(pageableData)).thenReturn(expectedCount);

		final int actualCount = productInterestFacade.getProductsCountByCustomerInterests(pageableData);

		Assert.assertEquals(expectedCount, actualCount);
	}

	@Test
	public void getProductInterestRelation_productInterest_is_empty()
	{
		final ProductModel product = new ProductModel();
		Mockito.doReturn(product).when(productService).getProductForCode(Mockito.any());
		final Map<ProductModel, Map<NotificationType, Date>> productInterestMap = new HashMap();
		productInterestMap.put(product, null);
		final Map<NotificationType, Date> interestTypeMap = new HashMap();
		Mockito.doReturn(productInterestMap).when(productInterestService).findProductInterestsByCustomer();

		final ProductInterestRelationData productInterestRelationData = productInterestFacade
				.getProductInterestRelation("testProduct");

		Assert.assertTrue(ObjectUtils.isEmpty(productInterestRelationData.getProduct()));
	}

	@Test
	public void getProductInterestRelation_success()
	{
		//mock getProductForCode
		final ProductModel product = new ProductModel();
		Mockito.doReturn(product).when(productService).getProductForCode(Mockito.any());

		//mock findProductInterestsByCustomer
		final Map<NotificationType, Date> interestTypeMap = new HashMap();
		interestTypeMap.put(NotificationType.NOTIFICATION, new Date());
		final Map<ProductModel, Map<NotificationType, Date>> productInterestMap = new HashMap();
		productInterestMap.put(product, interestTypeMap);
		Mockito.doReturn(productInterestMap).when(productInterestService).findProductInterestsByCustomer();
		productInterestFacade.setProductInterestService(productInterestService);

		//mock getProductInterestEntryConverter
		final List<ProductInterestEntryData> productInterestEntries = new ArrayList();
		final ProductInterestEntryData productInterestEntryData = new ProductInterestEntryData();
		productInterestEntryData.setInterestType(NotificationType.NOTIFICATION.getCode());
		productInterestEntryData.setDateAdded(new Date());
		Mockito.when(productInterestEntryConverter.convertAll(Mockito.any())).thenReturn(productInterestEntries);
		productInterestFacade.setProductInterestEntryConverter(productInterestEntryConverter);

		//mock getProductInterestDataForCurrentCustomer
		final ProductInterestData productInterestData = new ProductInterestData();
		productInterestData.setNotificationType(NotificationType.NOTIFICATION);
		final Optional<ProductInterestData> productInterestDatao = Optional.of(productInterestData);
		Mockito.doReturn(productInterestDatao).when(productInterestFacade).getProductInterestDataForCurrentCustomer(Mockito.any(),
				Mockito.any());

		final ProductInterestRelationData productInterestRelation = new ProductInterestRelationData();
		final ProductData productData = new ProductData();
		productData.setCode("p1");
		productInterestRelation.setProduct(productData);

		//mock getProductInterestRelationConverter
		Mockito.when(productInterestRelationConverter.convert(Mockito.any())).thenReturn(productInterestRelation);
		productInterestFacade.setProductInterestRelationConverter(productInterestRelationConverter);

		final ProductInterestRelationData productInterestRelationData = productInterestFacade
				.getProductInterestRelation("testProduct");

		Assert.assertEquals(productInterestRelationData.getProduct().getCode(), "p1");
	}

	@Test
	public void getProductInterestsForNotificationType_success()
	{
		final List<ProductInterestModel> productInterests = new ArrayList();
		final ProductInterestModel productInterest1 = new ProductInterestModel();
		final ProductModel product1 = new ProductModel();
		product1.setCode("p1");
		productInterest1.setProduct(product1);
		productInterest1.setNotificationType(NotificationType.NOTIFICATION);
		productInterests.add(productInterest1);
		final ProductInterestModel productInterest2 = new ProductInterestModel();
		final ProductModel product2 = new ProductModel();
		product2.setCode("p2");
		productInterest2.setProduct(product2);
		productInterest2.setNotificationType(NotificationType.NOTIFICATION);
		productInterests.add(productInterest2);

		Mockito.doReturn(productInterests).when(productInterestService).getProductInterests(Mockito.any(), Mockito.any(),
				Mockito.any());

		productInterestFacade.setProductInterestService(productInterestService);
		Mockito.doReturn(Collections.EMPTY_LIST).when(productInterestConverter).convertAll(Mockito.any());
		productInterestFacade.getProductInterestsForNotificationType("p1", NotificationType.NOTIFICATION);

		Mockito.verify(productInterestFacade.getProductInterestConverter(), Mockito.times(1)).convertAll(Mockito.any());
	}

	@Test
	public void getPaginatedProductInterestsForNotificationType_success()
	{
		final List<ProductInterestRelationData> productInterestRelations = new ArrayList();
		final ProductInterestRelationData productInterestRelation1 = new ProductInterestRelationData();
		final ProductData product1 = new ProductData();
		product1.setCode("p1");
		productInterestRelation1.setProduct(product1);
		final List<ProductInterestEntryData> productInterestEntrys1 = new ArrayList();
		final ProductInterestEntryData productInterestEntry1 = new ProductInterestEntryData();
		productInterestEntry1.setDateAdded(new Date());
		productInterestEntry1.setInterestType(NotificationType.NOTIFICATION.name());
		productInterestEntrys1.add(productInterestEntry1);
		productInterestRelation1.setProductInterestEntry(productInterestEntrys1);
		productInterestRelations.add(productInterestRelation1);
		final ProductInterestRelationData productInterestRelation2 = new ProductInterestRelationData();
		final ProductData product2 = new ProductData();
		product2.setCode("p2");
		productInterestRelation2.setProduct(product2);
		final List<ProductInterestEntryData> productInterestEntrys2 = new ArrayList();
		final ProductInterestEntryData productInterestEntry2 = new ProductInterestEntryData();
		productInterestEntry2.setDateAdded(new Date());
		productInterestEntry2.setInterestType(NotificationType.NOTIFICATION.name());
		productInterestEntrys2.add(productInterestEntry2);
		productInterestRelation2.setProductInterestEntry(productInterestEntrys2);
		productInterestRelations.add(productInterestRelation2);

		Mockito.doReturn(productInterestRelations).when(productInterestFacade).buildProductInterestRelationData(Mockito.any());
		final SearchPageData searchPageData = new SearchPageData();
		final PaginationData pagination = new PaginationData();
		pagination.setCurrentPage(0);
		pagination.setPageSize(10);
		pagination.setNeedsTotal(false);
		searchPageData.setPagination(pagination);
		final SearchPageData<ProductInterestRelationData> searchPage = productInterestFacade
				.getPaginatedProductInterestsForNotificationType("p1", NotificationType.NOTIFICATION, searchPageData);


		Assert.assertEquals(searchPage.getResults().get(0).getProduct().getCode(), "p1");
	}
}
