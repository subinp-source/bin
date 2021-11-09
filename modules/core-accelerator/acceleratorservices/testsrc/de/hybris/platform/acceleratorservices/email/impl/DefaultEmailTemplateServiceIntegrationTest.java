/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.email.impl;

/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorservices.email.data.EmailPageData;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageTemplateModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.commons.enums.RendererTypeEnum;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultEmailTemplateServiceIntegrationTest extends ServicelayerTest
{

	private final String PAGE_UID = "somePageUid";

	@Resource
	private ModelService modelService;

	@Resource
	private MediaService mediaService;

	@Resource
	private CatalogVersionService catalogVersionService;

	private EmailPageModel emailPageModel;
	private CatalogVersionModel catalogVersionModel;


	@Resource
	private DefaultEmailTemplateService defaultEmailTemplateService;


	@Before
	public void setUp() throws Exception
	{
		// media is needed to render the template
		importCsv("/acceleratorservices/test/testMedias.csv", "utf-8");

		final CMSSiteModel cmsSiteModel = modelService.create(CMSSiteModel.class);
		cmsSiteModel.setName("cmsSiteModel");
		cmsSiteModel.setUid("cmsSiteModel");
		cmsSiteModel.setActive(Boolean.TRUE);

		final CurrencyModel currency1 = modelService.create(CurrencyModel.class);
		currency1.setIsocode("PL");
		currency1.setSymbol("PL");

		final Set<CurrencyModel> currencies = new HashSet<CurrencyModel>();
		currencies.add(currency1);

		final BaseStoreModel store1 = modelService.create(BaseStoreModel.class);
		store1.setCurrencies(currencies);
		store1.setUid("store1");

		final List<BaseStoreModel> stores = new ArrayList<BaseStoreModel>();
		stores.add(store1);

		cmsSiteModel.setStores(stores);

		final LanguageModel languageModel = modelService.create(LanguageModel.class);
		languageModel.setIsocode("JA");
		modelService.save(languageModel);
		final List<LanguageModel> languagesModel = new ArrayList<LanguageModel>();
		languagesModel.add(languageModel);

		final ContentCatalogModel contentCatalog = modelService.create(ContentCatalogModel.class);
		contentCatalog.setId("contentCatalogDefaultEmailTemplateServiceIntegrationTest");
		modelService.save(contentCatalog);

		catalogVersionModel = modelService.create(CatalogVersionModel.class);
		catalogVersionModel.setVersion("1.0");
		catalogVersionModel.setLanguages(languagesModel);
		catalogVersionModel.setCatalog(contentCatalog);
		modelService.save(catalogVersionModel);
		final CatalogVersionModel model2 = modelService.create(CatalogVersionModel.class);
		model2.setVersion("2.0");
		model2.setLanguages(languagesModel);
		model2.setCatalog(contentCatalog);
		modelService.save(model2);

		final Set<CatalogVersionModel> catalogVersions = new HashSet<CatalogVersionModel>();
		catalogVersions.add(catalogVersionModel);
		catalogVersions.add(model2);
		contentCatalog.setCatalogVersions(catalogVersions);
		modelService.save(contentCatalog);

		contentCatalog.setActiveCatalogVersion(model2);
		modelService.save(contentCatalog);

		final List<ContentCatalogModel> contents = new ArrayList<ContentCatalogModel>();
		contents.add(contentCatalog);

		cmsSiteModel.setContentCatalogs(contents);
		modelService.save(cmsSiteModel);


		final MediaModel mediaModel = mediaService.getMedia("media1");

		final RendererTemplateModel rendererTemplateModel = modelService.create(RendererTemplateModel.class);
		rendererTemplateModel.setCode("renderCode");
		rendererTemplateModel.setRendererType(RendererTypeEnum.VELOCITY);
		rendererTemplateModel.setDefaultContent(mediaModel);
		rendererTemplateModel.setContextClass("java.util.Map");

		final EmailPageTemplateModel emailPageTemplateModel = modelService.create(EmailPageTemplateModel.class);
		emailPageTemplateModel.setUid("testTemplate");
		emailPageTemplateModel.setFrontendTemplateName("testTemplate");
		emailPageTemplateModel.setActive(Boolean.TRUE);
		emailPageTemplateModel.setCatalogVersion(catalogVersionModel);
		emailPageTemplateModel.setName("testTemplate");
		emailPageTemplateModel.setHtmlTemplate(rendererTemplateModel);
		modelService.save(emailPageTemplateModel);

		emailPageModel = modelService.create(EmailPageModel.class);
		emailPageModel.setCatalogVersion(catalogVersionModel);
		emailPageModel.setUid(PAGE_UID);
		emailPageModel.setMasterTemplate(emailPageTemplateModel);
		modelService.save(emailPageModel);

		catalogVersionService.setSessionCatalogVersions(catalogVersions);
	}


	@Test
	public void testGetPageTemplate() throws Exception
	{
		final EmailPageData emailPageData = new EmailPageData();
		emailPageData.setPageUid(PAGE_UID);
		final String res = defaultEmailTemplateService.getPageTemplate(emailPageData);
		assertEquals("sampleMediaFile.txt", res);
	}

}
