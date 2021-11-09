/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.smarteditaddon.service;

import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS;
import static de.hybris.platform.smarteditaddon.constants.SmarteditContractHTMLAttributes.SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cmsfacades.data.ItemData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.smarteditaddon.cms.services.impl.CMSSmartEditDynamicAttributeService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * The DefaultSmartEditEmailTemplateService is used to render email pages based on their velocity templates for
 * pages and components.
 */
@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class SmartEditEmailTemplateServiceTest
{

	private final String pageId = "mockPageId";

	@Mock
	private EmailPageModel emailPageModel;

	@Mock
	private Document document;

	@Mock
	private ItemData pageItemData;

	@Mock
	private CatalogVersionModel catalogVersionModel;

	@Mock
	private ItemData catalogVersionItemData;

	@Mock
	private ContentSlotData contentSlotData;

	@Mock
	private ContentSlotModel contentSlotModel;

	@Mock
	private CMSPageService cmsPageService;

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	@Mock
	private CMSSmartEditDynamicAttributeService cmsDynamicAttributeService;

	@InjectMocks
	private SmartEditEmailTemplateService smartEditEmailTemplateService;


	@Before
	public void setup()
	{
		when(emailPageModel.getUid()).thenReturn("someEmailPageUid");
		when(pageItemData.getItemId()).thenReturn("somePageId");
		when(catalogVersionItemData.getItemId()).thenReturn("someCatalogVersionItem");
	}

	@Test
	public void addSmartEditPageBodyAttributes_appendsBodyCssClasses() throws CMSItemNotFoundException
	{
		when(cmsPageService.getPageForId(pageId)).thenReturn(emailPageModel);
		when(uniqueItemIdentifierService.getItemData(emailPageModel)).thenReturn(Optional.of(pageItemData));
		when(emailPageModel.getCatalogVersion()).thenReturn(catalogVersionModel);
		when(uniqueItemIdentifierService.getItemData(catalogVersionModel)).thenReturn(Optional.of(catalogVersionItemData));

		final Element body = new Element("body");
		when(document.body()).thenReturn(body);

		smartEditEmailTemplateService.addSmartEditPageBodyAttributes(document, emailPageModel);
		final Set<String> classNames = body.classNames();

		assertTrue(classNames.contains(SMARTEDIT_CONTRACT_PAGE_UID_PARTIAL_CLASS + emailPageModel.getUid()));
		assertTrue(classNames.contains(SMARTEDIT_CONTRACT_PAGE_UUID_PARTIAL_CLASS + pageItemData.getItemId()));
		assertTrue(classNames.contains(SMARTEDIT_CONTRACT_CATALOG_VERSION_UUID_PARTIAL_CLASS + catalogVersionItemData.getItemId()));
	}

	@Test(expected = IllegalStateException.class)
	public void wrapContentSlots_willThrowWhenMissingSlot()
	{
		final Element element = new Element("div");
		element.html("${ctx.cmsSlotContents.XYZ}");
		final Elements elements = new Elements(element);

		when(document.select(Mockito.anyString())).thenReturn(elements);

		smartEditEmailTemplateService.wrapContentSlots(document, Lists.emptyList());
	}

	@Test
	public void wrapContentSlots_CorrectlyAppendsSlotAttributes()
	{
		final String attrKey1 = "somekey1";
		final String attrValue1 = "somevalue1";
		final String attrKey2 = "somekey2";
		final String attrValue2 = "somevalue2";
		final String SLOT_NAME = "xyz";
		final Element element = new Element("div");
		final Elements elements = new Elements(element);
		final Map<String, String> componentAttributes = Map.of(
				attrKey1, attrValue1,
				attrKey2, attrValue2);

		element.html("${ctx.cmsSlotContents." + SLOT_NAME + "}");

		when(document.select(Mockito.anyString())).thenReturn(elements);
		when(contentSlotData.getPosition()).thenReturn(SLOT_NAME);
		when(contentSlotData.getContentSlot()).thenReturn(contentSlotModel);
		when(contentSlotData.getCMSComponents()).thenReturn(Lists.emptyList());

		when(cmsDynamicAttributeService.getDynamicContentSlotAttributes(contentSlotModel, null, null))
				.thenReturn(componentAttributes);

		smartEditEmailTemplateService.wrapContentSlots(document, Lists.newArrayList(contentSlotData));

		assertTrue(element.html().contains(attrKey1 + "=\"" + attrValue1 + "\""));
		assertTrue(element.html().contains(attrKey2 + "=\"" + attrValue2 + "\""));
	}



}