/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.rendering.populators;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.data.PagePreviewCriteriaData;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2.servicelayer.data.ContentSlotData;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cms2.servicelayer.services.CMSPreviewService;
import de.hybris.platform.cmsfacades.cmsitems.attributeconverters.UniqueIdentifierAttributeToDataContentConverter;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.data.PageContentSlotData;
import de.hybris.platform.cmsfacades.rendering.cache.RenderingCacheService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PageModelToDataRenderingPopulatorTest
{
	// --------------------------------------------------------------------------
	// Variables
	// --------------------------------------------------------------------------
	private final String PAGE_UID = "some page uid";
	private final String PAGE_UUID = "some page uuid";
	private final String PAGE_TITLE = "some page title";
	private final String PAGE_DESCRIPTION = "some page description";
	private final String PAGE_TEMPLATE = "page template";
	private final String PAGE_NAME = "some page name";
	private final String PAGE_TYPE = "some page type";
	private final String CATALOG_VERSION_UUID = "catalog version uuid";

	@Mock
	private CatalogVersionModel catalogVersion;

	@Mock
	private ContentSlotData slot1;

	@Mock
	private ContentSlotData slot2;

	@Mock
	private ContentSlotData slot3;

	@Mock
	private ContentSlotData slot4;

	@Mock
	private PageContentSlotData slot1Representation;

	@Mock
	private PageContentSlotData slot2Representation;

	@Mock
	private PageContentSlotData slot3Representation;

	@Mock
	private PageContentSlotData slot4Representation;

	@Mock
	private ContentSlotModel contentSlotModel1;

	@Mock
	private ContentSlotModel contentSlotModel2;

	@Mock
	private ContentSlotModel contentSlotModel3;

	@Mock
	private ContentSlotModel contentSlotModel4;

	@Mock
	private AbstractPageModel pageModel;

	@Mock
	private PageTemplateModel pageTemplate;

	@Mock
	private PagePreviewCriteriaData pagePreviewCriteriaData;

	@Mock
	private CMSPageService cmsPageService;

	@Mock
	private CMSPreviewService cmsPreviewService;

	@Mock
	private Converter<ContentSlotData, PageContentSlotData> contentSlotRenderingConverter;

	@Mock
	private UniqueIdentifierAttributeToDataContentConverter<ItemModel> uniqueIdentifierAttributeToDataContentConverter;

	@Mock
	private RenderingCacheService<PageContentSlotData> renderingCacheService;

	@Captor
	protected ArgumentCaptor<Map<String, Object>> captor;

	@Mock
	private Populator abstractPagePopulator;
	@Mock
	private Populator contentPagePopulator;

	@InjectMocks
	private PageModelToDataRenderingPopulator pageRenderingPopulator;

	private AbstractPageData pageData;
	private Date TWO_YEARS_AGO;
	private Date ONE_YEAR_AGO;
	private Date ONE_YEAR_FROM_NOW;

	// --------------------------------------------------------------------------
	// Test Setup
	// --------------------------------------------------------------------------
	@Before
	public void setUp()
	{
		pageRenderingPopulator.setOtherPropertiesPopulators(Arrays.asList(abstractPagePopulator, contentPagePopulator));
		pageData = new AbstractPageData();

		when(pageModel.getDefaultPage()).thenReturn(false);
		when(pageModel.getUid()).thenReturn(PAGE_UID);
		when(pageModel.getName()).thenReturn(PAGE_NAME);
		when(pageModel.getDescription()).thenReturn(PAGE_DESCRIPTION);
		when(pageModel.getTitle()).thenReturn(PAGE_TITLE);
		when(pageModel.getItemtype()).thenReturn(PAGE_TYPE);
		when(pageModel.getMasterTemplate()).thenReturn(pageTemplate);
		when(pageModel.getCatalogVersion()).thenReturn(catalogVersion);
		when(pageTemplate.getUid()).thenReturn(PAGE_TEMPLATE);

		when(cmsPreviewService.getPagePreviewCriteria()).thenReturn(pagePreviewCriteriaData);

		// Slots
		when(cmsPageService.getContentSlotsForPage(pageModel, pagePreviewCriteriaData))
				.thenReturn(Arrays.asList(slot1, slot2, slot3, slot4));
		when(slot1.getContentSlot()).thenReturn(contentSlotModel1);
		when(slot2.getContentSlot()).thenReturn(contentSlotModel2);
		when(slot3.getContentSlot()).thenReturn(contentSlotModel3);
		when(slot4.getContentSlot()).thenReturn(contentSlotModel4);

		when(contentSlotModel1.getActive()).thenReturn(true);
		when(contentSlotModel2.getActive()).thenReturn(true);
		when(contentSlotModel3.getActive()).thenReturn(true);
		when(contentSlotModel4.getActive()).thenReturn(true);

		// Converter
		when(renderingCacheService.cacheOrElse(any(), any())).then(params -> {
			final ContentSlotModel renderingData = params.getArgumentAt(0, ContentSlotModel.class);

			if (renderingData.equals(contentSlotModel1))
			{
				return slot1Representation;
			}
			else if (renderingData.equals(contentSlotModel2))
			{
				return slot2Representation;
			}
			else if (renderingData.equals(contentSlotModel3))
			{
				return slot3Representation;
			}
			else if (renderingData.equals(contentSlotModel4))
			{
				return slot4Representation;
			}

			return slot4Representation;
		});

		// Sample times
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		ONE_YEAR_AGO = cal.getTime();
		cal.add(Calendar.YEAR, -1);
		TWO_YEARS_AGO = cal.getTime();
		cal.add(Calendar.YEAR, 3);
		ONE_YEAR_FROM_NOW = cal.getTime();

		// Uuid
		when(uniqueIdentifierAttributeToDataContentConverter.convert(pageModel)).thenReturn(PAGE_UUID);
		when(uniqueIdentifierAttributeToDataContentConverter.convert(catalogVersion)).thenReturn(CATALOG_VERSION_UUID);
	}

	// --------------------------------------------------------------------------
	// Tests
	// --------------------------------------------------------------------------
	@Test
	public void whenPopulatorIsCalled_ThenItPopulatesAllTheRequiredProperties()
	{
		// WHEN
		pageRenderingPopulator.populate(pageModel, pageData);

		// THEN
		assertThat(pageData.getUid(), is(PAGE_UID));
		assertThat(pageData.getUuid(), is(PAGE_UUID));
		assertThat(pageData.getLocalizedTitle(), is(PAGE_TITLE));
		assertThat(pageData.getTemplate(), is(PAGE_TEMPLATE));
		assertThat(pageData.getName(), is(PAGE_NAME));
		assertThat(pageData.getLocalizedDescription(), is(PAGE_DESCRIPTION));
		assertThat(pageData.getTypeCode(), is(PAGE_TYPE));
		assertThat(pageData.getDefaultPage(), is(false)); // By default it should be false
		assertThat(pageData.getCatalogVersionUuid(), is(CATALOG_VERSION_UUID));

		assertThat(pageData.getContentSlots(),
				contains(slot1Representation, slot2Representation, slot3Representation, slot4Representation));
	}

	@Test
	public void whenPopulatorIsCalledThenOtherPropertiesFieldIsPopulated()
	{
		// WHEN
		pageRenderingPopulator.populate(pageModel, pageData);

		// THEN
		verify(abstractPagePopulator).populate(eq(pageModel), captor.capture());
		verify(contentPagePopulator).populate(eq(pageModel), captor.capture());
		assertThat(pageData.getOtherProperties(), is(captor.getValue()));
	}

	@Test
	public void givenDefaultPage_WhenPopulatorIsCalled_ThenItPopulatesThePageAsDefault()
	{
		// GIVEN
		makePageDefault();

		// WHEN
		pageRenderingPopulator.populate(pageModel, pageData);

		// THEN
		assertThat(pageData.getDefaultPage(), is(true));
	}

	@Test
	public void givenInactiveSlot_WhenPopulatorIsCalled_ThenItPopulatesTheSlotsWithoutTheInactiveOnes()
	{
		// GIVEN
		setSlotAsInactive(slot2);
		setContentSlotActivePeriod(slot3, TWO_YEARS_AGO, ONE_YEAR_FROM_NOW); // Still valid
		setContentSlotActivePeriod(slot4, TWO_YEARS_AGO, ONE_YEAR_AGO); // Not valid

		// WHEN
		pageRenderingPopulator.populate(pageModel, pageData);

		// THEN
		assertThat(pageData.getContentSlots(), contains(slot1Representation, slot3Representation));
	}

	// --------------------------------------------------------------------------
	// Helper Methods
	// --------------------------------------------------------------------------
	protected void makePageDefault()
	{
		when(pageModel.getDefaultPage()).thenReturn(true);
	}

	protected void setSlotAsInactive(final ContentSlotData slotData)
	{
		when(slotData.getContentSlot().getActive()).thenReturn(false);
	}

	protected void setContentSlotActivePeriod(final ContentSlotData slotData, final Date activeFrom, final Date activeUntil)
	{
		when(slotData.getContentSlot().getActiveFrom()).thenReturn(activeFrom);
		when(slotData.getContentSlot().getActiveUntil()).thenReturn(activeUntil);
	}
}
