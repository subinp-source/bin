/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.email.dao.EmailPageDao;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 */
@UnitTest
public class DefaultCMSEmailPageServiceTest
{
	private DefaultCMSEmailPageService cmsEmailPageService;

	@Mock
	private EmailPageDao emailPageDao;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		cmsEmailPageService = new DefaultCMSEmailPageService();
		cmsEmailPageService.setEmailPageDao(emailPageDao);
	}

	@Test
	public void testGetEmailPageForFrontendTemplate()
	{
		final EmailPageModel emailPageModel = mock(EmailPageModel.class);
		final CatalogVersionModel catalogVersionModel = mock(CatalogVersionModel.class);
		given(emailPageDao.findEmailPageByFrontendTemplateName(Mockito.anyString(), any(CatalogVersionModel.class))).willReturn(
				emailPageModel);
		final EmailPageModel result = cmsEmailPageService.getEmailPageForFrontendTemplate("testTemplate", catalogVersionModel);

		Assert.assertEquals(emailPageModel, result);

	}

}
