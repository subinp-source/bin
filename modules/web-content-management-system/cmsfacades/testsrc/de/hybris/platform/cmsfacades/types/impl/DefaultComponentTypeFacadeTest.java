/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.common.service.SessionSearchRestrictionsDisabler;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminPageService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminTypeRestrictionsService;
import de.hybris.platform.cmsfacades.common.validator.FacadeValidationService;
import de.hybris.platform.cmsfacades.data.CMSComponentTypesForPageSearchData;
import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.types.validator.ComponentTypeForPageSearchValidator;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.util.collections.Sets;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.TYPE_REQUIRED_FIELDS;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultComponentTypeFacadeTest
{
    private final static boolean IS_READ_ONLY = false;
    private final static String PAGE_ID = "somePageID";
    private final static String TYPE_ALLOWED_CODE_1 = "SomeValidCode";
    private final static String TYPE_ALLOWED_CODE_2 = "OtherValidCode";
    private final static String TYPE_DISALLOWED_CODE = "SomeInvalidCode";
    private final static String COMPONENT_NAME_1 = "ComponentName1";
    private final static String COMPONENT_NAME_3 = "ComponentName3";
    private final static String COMPONENT_NAME_4 = "OtherName4";
    private final static List<String> REQUIRED_FIELDS = Arrays.asList("Field1", "Field2");


    @InjectMocks
    @Spy
    private DefaultComponentTypeFacade componentTypeFacade;

    @Mock
    private CMSAdminPageService cmsAdminPageService;

    @Mock
    private CMSAdminSiteService cmsAdminSiteService;

    @Mock
    private CMSAdminTypeRestrictionsService cmsAdminTypeRestrictionsService;

    @Mock
    private CMSComponentTypesForPageSearchData searchData;

    @Mock
    private SessionSearchRestrictionsDisabler sessionSearchRestrictionDisabler;

    @Mock
    private FacadeValidationService facadeValidationService;

    @Mock
    private ComponentTypeForPageSearchValidator validator;

    @Mock
    private PageableData pageableData;

    @Mock
    private AbstractPageModel page;

    @Mock
    private CMSComponentTypeModel typeAllowed1;

    @Mock
    private CMSComponentTypeModel typeAllowed2;

    @Mock
    private ComponentTypeData componentType1;

    @Mock
    private ComponentTypeData componentType2;

    @Mock
    private ComponentTypeData componentType3;

    @Mock
    private ComponentTypeData componentType4;

    private List<ComponentTypeData> componentTypesList;
    private ArgumentCaptor<Map> typeContextCaptor = ArgumentCaptor.forClass(Map.class);

    @Before
    public void setUp()
    {
        when(searchData.getPageId()).thenReturn(PAGE_ID);
        when(searchData.getRequiredFields()).thenReturn(REQUIRED_FIELDS);
        when(searchData.getMask()).thenReturn("");
        when(searchData.isReadOnly()).thenReturn(IS_READ_ONLY);

        when(pageableData.getCurrentPage()).thenReturn(0);
        when(pageableData.getPageSize()).thenReturn(3);

        when(typeAllowed1.getCode()).thenReturn(TYPE_ALLOWED_CODE_1);
        when(typeAllowed2.getCode()).thenReturn(TYPE_ALLOWED_CODE_2);

        when(componentType1.getCode()).thenReturn(TYPE_ALLOWED_CODE_1);
        when(componentType1.getName()).thenReturn(COMPONENT_NAME_1);
        when(componentType2.getCode()).thenReturn(TYPE_DISALLOWED_CODE);
        when(componentType3.getCode()).thenReturn(TYPE_ALLOWED_CODE_2);
        when(componentType3.getName()).thenReturn(COMPONENT_NAME_3);
        when(componentType4.getCode()).thenReturn(TYPE_ALLOWED_CODE_2);
        when(componentType4.getName()).thenReturn(COMPONENT_NAME_4);

        when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_ID)).thenReturn(page);
        when(cmsAdminSiteService.getTypeContext()).thenReturn(null);
        when(cmsAdminTypeRestrictionsService.getTypeRestrictionsForPage(page)).thenReturn(Sets.newSet(typeAllowed1, typeAllowed2));

        componentTypesList = Arrays.asList(componentType1, componentType2, componentType3, componentType4);
        doReturn(componentTypesList).when(componentTypeFacade).getAllCmsItemTypes(List.of(StructureTypeCategory.COMPONENT), IS_READ_ONLY);

        doAnswer(new Answer<Collection<ContentCatalogModel>>()
        {
            @Override
            public Collection answer(final InvocationOnMock invocation) throws Throwable
            {
                final Supplier<Collection<ContentCatalogModel>> supplier = (Supplier) invocation.getArguments()[0];
                return supplier.get();
            }
        }).when(sessionSearchRestrictionDisabler).execute(any());
    }

    @Test(expected = CMSItemNotFoundException.class)
    public void shouldThrowCMSItemNotFoundExceptionWhenPageNotFound() throws Exception
    {
        // GIVEN
        when(cmsAdminPageService.getPageForIdFromActiveCatalogVersion(PAGE_ID)).thenThrow(UnknownIdentifierException.class);

        // WHEN / THEN
        componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);
    }

    @Test
    public void shouldAddRequiredFieldsToTypeContext() throws Exception
    {
        // WHEN
        componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        verify(cmsAdminSiteService).setTypeContext(typeContextCaptor.capture());
        assertThat(typeContextCaptor.getValue().size(), is(1));
        assertEquals(typeContextCaptor.getValue().get(TYPE_REQUIRED_FIELDS), REQUIRED_FIELDS);
    }

    @Test
    public void shouldKeepPreviousValuesInTypeContext() throws Exception
    {
        // GIVEN
        final String existingItemKey = "someKey";
        final String existingItem = "someValue";
        final Map<String, Object> existingMap = new HashMap<>();
        existingMap.put(existingItemKey, existingItem);

        when(cmsAdminSiteService.getTypeContext()).thenReturn(existingMap);

        // WHEN
        componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        verify(cmsAdminSiteService).setTypeContext(typeContextCaptor.capture());
        assertThat(typeContextCaptor.getValue().size(), is(2));
        assertEquals(typeContextCaptor.getValue().get(TYPE_REQUIRED_FIELDS), REQUIRED_FIELDS);
        assertEquals(typeContextCaptor.getValue().get(existingItemKey), existingItem);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowAValidationExceptionWhenValidationFails() throws Exception
    {
        // GIVEN
        doThrow(ValidationException.class).when(facadeValidationService).validate(validator, searchData);

        // WHEN / THEN
        componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);
    }

    @Test
    public void shouldOnlyReturnValidComponentTypes() throws Exception
    {
        // WHEN
        final SearchResult<ComponentTypeData> result = componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        assertThat(result.getTotalCount(), is(3));
        assertThat(result.getRequestedCount(), is(3));
        assertThat(result.getCount(), is(3));
        assertThat(result.getResult(), hasItems(componentType1, componentType3, componentType4));
    }

    @Test
    public void shouldReturnOnlyRequestedPagingData() throws Exception
    {
        // GIVEN
        when(pageableData.getCurrentPage()).thenReturn(1);
        when(pageableData.getPageSize()).thenReturn(2);

        // WHEN
        final SearchResult<ComponentTypeData> result = componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        assertThat(result.getTotalCount(), is(3));
        assertThat(result.getRequestedCount(), is(2));
        assertThat(result.getCount(), is(1));
        assertThat(result.getResult(), hasItems(componentType4));
    }

    @Test
    public void shouldFilterByMaskOnCode() throws Exception
    {
        // GIVEN
        when(searchData.getMask()).thenReturn("Other");

        // WHEN
        final SearchResult<ComponentTypeData> result = componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        assertThat(result.getTotalCount(), is(2));
        assertThat(result.getRequestedCount(), is(3));
        assertThat(result.getCount(), is(2));
        assertThat(result.getResult(), hasItems(componentType3, componentType4));
    }

    @Test
    public void shouldFilterByMaskOnName() throws Exception
    {
        // GIVEN
        when(searchData.getMask()).thenReturn("Component");

        // WHEN
        final SearchResult<ComponentTypeData> result = componentTypeFacade.getAllComponentTypesForPage(searchData, pageableData);

        // THEN
        assertThat(result.getTotalCount(), is(2));
        assertThat(result.getRequestedCount(), is(3));
        assertThat(result.getCount(), is(2));
        assertThat(result.getResult(), hasItems(componentType1, componentType3));
    }
}
