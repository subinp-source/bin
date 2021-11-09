/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.customerinterestsfacades.productinterest.populators;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.customerinterestsfacades.data.ProductInterestData;
import de.hybris.platform.customerinterestsservices.model.ProductInterestModel;
import de.hybris.platform.notificationfacades.data.NotificationPreferenceData;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductInterestReversePopulatorTest
{
	private ProductInterestReversePopulator productInterestReversePopulator;
	@Mock
	private ProductService productService;
	@Mock
	private UserService userService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private CommonI18NService commonI18NService;

	private final String expiryDay = "customerinterestsservices.expiryDay";

	private ProductInterestData testProductInterestData;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		productInterestReversePopulator = new ProductInterestReversePopulator();
		productInterestReversePopulator.setProductService(productService);
		productInterestReversePopulator.setUserService(userService);
		productInterestReversePopulator.setBaseStoreService(baseStoreService);
		productInterestReversePopulator.setBaseSiteService(baseSiteService);
		productInterestReversePopulator.setCommonI18NService(commonI18NService);
		final LanguageModel value = new LanguageModel();
		Mockito.when(commonI18NService.getCurrentLanguage()).thenReturn(value);

		testProductInterestData = new ProductInterestData();
		testProductInterestData.setNotificationType(NotificationType.NOTIFICATION);
		final ProductData productData = new ProductData();
		productData.setCode("138427");
		testProductInterestData.setProduct(productData);

		final NotificationPreferenceData preference = new NotificationPreferenceData();
		preference.setChannel(NotificationChannel.EMAIL);
		preference.setEnabled(true);

		testProductInterestData.setNotificationChannels(Collections.singletonList(preference));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReversePopulateProductInteresWithSourceNull()
	{
		final ProductInterestModel productInterestModel = new ProductInterestModel();
		final ProductInterestData productInterestData = null;
		productInterestReversePopulator.populate(productInterestData, productInterestModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testReversePopulateProductInteresWithTargetNull()
	{
		final ProductInterestModel productInterestModel = null;
		final ProductInterestData productInterestData = new ProductInterestData();
		productInterestReversePopulator.populate(productInterestData, productInterestModel);
	}

	@Test
	public void testPopulateWithExistTarget()
	{
		final ProductInterestModel target = new ProductInterestModel();

		productInterestReversePopulator.populate(testProductInterestData, target);

		assertEquals(NotificationType.NOTIFICATION, target.getNotificationType());
		assertEquals(NotificationChannel.EMAIL, target.getNotificationChannels().iterator().next());
		Assert.assertNotNull(target.getLanguage());
	}

}
