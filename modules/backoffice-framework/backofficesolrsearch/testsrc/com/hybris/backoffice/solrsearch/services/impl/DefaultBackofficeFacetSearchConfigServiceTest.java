/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.services.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.hybris.backoffice.solrsearch.daos.SolrFacetSearchConfigDAO;
import de.hybris.platform.core.model.type.ComposedTypeModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeFacetSearchConfigServiceTest {

    public static final String TYPE = "Type";
    public static final String SUB_TYPE = "SubType";

    @Spy
    @InjectMocks
    private DefaultBackofficeFacetSearchConfigService service;

    @Mock
    private ComposedTypeModel typeModel;
    @Mock
    private ComposedTypeModel subTypeModel;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel configType;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel configSubtype;
    @Mock
    private SolrFacetSearchConfigDAO solrFacetSearchConfigDAO;

    @Test
    public void findMatchingConfigShouldReturnResultOfRecursiveLookup()
    {
        //given
        final Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs = new ArrayList<>();

        configs.add(configType);
        configs.add(configSubtype);

        doReturn(typeModel).when(configType).getIndexedType();
        doReturn(subTypeModel).when(configSubtype).getIndexedType();

        doReturn(TYPE).when(typeModel).getCode();
        doReturn(SUB_TYPE).when(subTypeModel).getCode();

        doReturn(configSubtype).when(service).findMatchingConfigRecursively(any(), any());

        //when
        final BackofficeIndexedTypeToSolrFacetSearchConfigModel result = service.findMatchingConfig(typeModel, configs);

        //then
        assertThat(result).isSameAs(configSubtype);
    }

    @Test
    public void findMatchingConfigShouldReturnNullOnEmptyConfig()
    {
        //when
        final BackofficeIndexedTypeToSolrFacetSearchConfigModel result = service.findMatchingConfig(typeModel, new ArrayList<>());

        //then
        assertThat(result).isNull();
    }

    @Test
    public void findMatchingConfigShouldReturnTheOnlyElementWithNoFurtherChecks()
    {
        //given
        final Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs = new ArrayList<>();

        configs.add(configType);

        //when
        final BackofficeIndexedTypeToSolrFacetSearchConfigModel result = service.findMatchingConfig(typeModel, configs);

        //then
        assertThat(result).isSameAs(configType);
        verify(service, never()).findMatchingConfigRecursively(any(), any());
    }

    @Test
    public void shouldReturnEmptyCollectionWhenNoFacetSearchConfigIsPresent()
    {
        // given
        when(solrFacetSearchConfigDAO.findAllSearchConfigs()).thenReturn(Collections.emptyList());

        // when
        final Collection<ComposedTypeModel> composedTypes = service.getAllMappedTypes();

        // then
        assertThat(composedTypes).isEmpty();
    }

    @Test
    public void shouldReturnEmptyCollectionWhenFacetSearchConfigIsPresentAndDoesNotHaveIndexedType()
    {
        // given
        final Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs = Collections.emptyList();
        when(configType.getIndexedType()).thenReturn(null);
        when(solrFacetSearchConfigDAO.findAllSearchConfigs()).thenReturn(configs);

        // when
        final Collection<ComposedTypeModel> composedTypes = service.getAllMappedTypes();

        // then
        assertThat(composedTypes).isEmpty();
    }

    @Test
    public void shouldReturnComposedTypeModelWhenFacetSearchConfigIsPresentAndHasIndexedType()
    {
        // given
        final ComposedTypeModel model = mock(ComposedTypeModel.class);
        when(configType.getIndexedType()).thenReturn(model);
        final Collection<BackofficeIndexedTypeToSolrFacetSearchConfigModel> configs = List.of(configType);
        when(solrFacetSearchConfigDAO.findAllSearchConfigs()).thenReturn(configs);

        // when
        final Collection<ComposedTypeModel> composedTypes = service.getAllMappedTypes();

        // then
        assertThat(composedTypes).isNotEmpty();
        assertThat(composedTypes.size()).isEqualTo(1);
    }

}