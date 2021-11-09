/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.config.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.ViewAttributeDescriptorModel;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.zkoss.lang.Strings;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListView;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.testing.AbstractCockpitngUnitTest;
import com.hybris.cockpitng.testing.annotation.NullSafeWidget;



@NullSafeWidget
@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeListViewConfigFallbackStrategyTest
		extends AbstractCockpitngUnitTest<DefaultBackofficeListViewConfigFallbackStrategy>
{
	private static final String TYPE_CODE = "typeCode";
	private static final String QUALIFIER = "qualifier";

	@Spy
	@InjectMocks
	private DefaultBackofficeListViewConfigFallbackStrategy testSubject;

	@Mock
	private TypeService typeService;

	@Mock
	private TypeFacade typeFacade;

	@Mock
	private ConfigContext context;

	@Mock
	private AtomicTypeModel atomicTypeModel;

	@Mock
	private ViewTypeModel viewTypeModel;

	@Mock
	private ComposedTypeModel composedTypeModel;

	@Mock
	private ViewAttributeDescriptorModel viewAttributeDescriptorModel;

	@Mock
	private DataAttribute dataAttribute;

	@Mock
	private DataType dataType;

	@Test
	public void loadFallbackConfiguration()
	{
		// given
		doThrow(IllegalArgumentException.class).when(typeService).getTypeForCode(null);

		// when
		final ListView listView = testSubject.loadFallbackConfiguration(new DefaultConfigContext(), ListView.class);

		// then
		assertThat(listView).isNotNull();
	}

	@Test
	public void shouldNotAddColumnsForEmptyTypeCode()
	{
		// given
		when(context.getAttribute(any())).thenReturn(Strings.EMPTY);

		// when
		final ListView result = testSubject.loadFallbackConfiguration(context, ListView.class);

		// then
		assertThat(result.getColumn()).isEmpty();
	}

	@Test
	public void shouldNotAddColumnsForUnrecognizedTypeModel()
	{
		// given
		when(context.getAttribute(any())).thenReturn(TYPE_CODE);
		when(typeService.getTypeForCode(TYPE_CODE)).thenReturn(atomicTypeModel);

		//
		final ListView result = testSubject.loadFallbackConfiguration(context, ListView.class);

		// then
		assertThat(result.getColumn()).isEmpty();
	}

	@Test
	public void shouldAddColumnsForViewTypeModel()
	{
		// given
		final List<ViewAttributeDescriptorModel> viewAttributeDescriptorModelList = Lists.newArrayList(viewAttributeDescriptorModel);

		when(context.getAttribute(any())).thenReturn(TYPE_CODE);
		when(typeService.getTypeForCode(TYPE_CODE)).thenReturn(viewTypeModel);
		when(viewAttributeDescriptorModel.getQualifier()).thenReturn(QUALIFIER);
		when(viewTypeModel.getColumns()).thenReturn(viewAttributeDescriptorModelList);

		// when
		final ListView result = testSubject.loadFallbackConfiguration(context, ListView.class);

		// then
		assertThat(result.getColumn()).isNotEmpty();
		assertThat(result.getColumn().stream().findFirst().get().getQualifier().equals(QUALIFIER)).isTrue();
	}

	@Test
	public void shouldAddColumnsForComposedTypeModel() throws TypeNotFoundException
	{
		//given
		final List<DataAttribute> dataAttributeList = Lists.newArrayList(dataAttribute);

		when(context.getAttribute(any())).thenReturn(TYPE_CODE);
		when(typeService.getTypeForCode(TYPE_CODE)).thenReturn(composedTypeModel);
		when(typeFacade.load(any())).thenReturn(dataType);
		when(dataType.getAttributes()).thenReturn(dataAttributeList);
		when(dataAttribute.getQualifier()).thenReturn(QUALIFIER);
		when(dataAttribute.isMandatory()).thenReturn(true);
		when(viewAttributeDescriptorModel.getQualifier()).thenReturn(QUALIFIER);

		// when
		final ListView result = testSubject.loadFallbackConfiguration(context, ListView.class);

		// then
		assertThat(result.getColumn()).isNotEmpty().hasSize(1);
		assertThat(result.getColumn().stream().findFirst().get().getQualifier().equals(QUALIFIER)).isTrue();
	}
}
