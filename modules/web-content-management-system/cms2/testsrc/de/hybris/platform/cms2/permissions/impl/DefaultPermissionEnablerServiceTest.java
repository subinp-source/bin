/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.permissions.impl;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.data.AttributePermissionForType;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertThat;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPermissionEnablerServiceTest
{

	private final String TYPE_CODE = "typeCode";
	private final String SUPER_TYPE_CODE = "superTpeCode";
	private final String OTHER_TYPE_CODE = "otherTypeCode";
	private final String UNKNOWN_TYPE_CODE = "unknownTypeCode";

	private final String ATTRIBUTE_1 = "attr1";
	private final String ATTRIBUTE_2 = "attr2";
	private final String ATTRIBUTE_3 = "attr3";
	private final String ATTRIBUTE_4 = "attr4";
	private final String ATTRIBUTE_5 = "attr5";
	private final String ATTRIBUTE_6 = "attr6";

	@Spy
	@InjectMocks
	private DefaultPermissionEnablerService service;

	@Mock
	private TypeService typeService;

	@Mock
	private AttributePermissionForType permissionForType;

	@Mock
	private AttributePermissionForType permissionForSuperType;

	@Mock
	private AttributePermissionForType permissionForOtherType;

	@Mock
	private ComposedTypeModel composedType;

	@Mock
	private ComposedTypeModel superComposedType;

	@Mock
	private ComposedTypeModel otherComposedType;

	@Before
	public void setup()
	{

		when(service.getPermissionConfigs()).thenReturn(Arrays.asList(permissionForType, permissionForOtherType, permissionForSuperType));

		when(permissionForType.getTypecode()).thenReturn(TYPE_CODE);
		when(permissionForType.getInclude()).thenReturn(String.join(",", ATTRIBUTE_1));
		when(permissionForType.getExclude()).thenReturn(String.join(",", ATTRIBUTE_2));

		when(permissionForSuperType.getTypecode()).thenReturn(SUPER_TYPE_CODE);
		when(permissionForSuperType.getInclude()).thenReturn(String.join(",", ATTRIBUTE_2, ATTRIBUTE_3));

		when(permissionForOtherType.getTypecode()).thenReturn(OTHER_TYPE_CODE);
		when(permissionForOtherType.getInclude()).thenReturn(String.join(",", ATTRIBUTE_4));
		when(permissionForOtherType.getExclude()).thenReturn(String.join(",", ATTRIBUTE_5));

		when(typeService.getComposedTypeForCode(TYPE_CODE)).thenReturn(composedType);
		when(composedType.getAllSuperTypes()).thenReturn(Arrays.asList(superComposedType));

		when(typeService.getComposedTypeForCode(SUPER_TYPE_CODE)).thenReturn(superComposedType);
		when(superComposedType.getAllSuperTypes()).thenReturn(Arrays.asList());

		when(typeService.getComposedTypeForCode(OTHER_TYPE_CODE)).thenReturn(otherComposedType);
		when(otherComposedType.getAllSuperTypes()).thenReturn(Arrays.asList());

		when(superComposedType.getCode()).thenReturn(SUPER_TYPE_CODE);

	}

	@Test
	public void buildNeverCreatesConfigurationWhenCheckingAllTypesAndAttributes()
	{
		when(service.getCheckingAllAttributes()).thenReturn(true);
		when(service.getCheckingAllTypes()).thenReturn(true);

		service.buildTypeConfigurations().get();

		verify(typeService, never()).getComposedTypeForCode(any());
	}

	@Test
	public void buildNeverCreatesConfigurationWhenEmptyPermissionsConfigIsProvided()
	{
		when(service.getCheckingAllAttributes()).thenReturn(true);
		when(service.getCheckingAllTypes()).thenReturn(false);
		when(service.getPermissionConfigs()).thenReturn(new ArrayList<>());

		service.buildTypeConfigurations().get();

		verify(typeService, never()).getComposedTypeForCode(any());
	}

	@Test
	public void buildCallsTypeServiceAndCreatesConfigurationForEachProvidedPermissionConfig()
	{
		when(service.getCheckingAllAttributes()).thenReturn(true);
		when(service.getCheckingAllTypes()).thenReturn(false);

		service.buildTypeConfigurations().get();

		verify(typeService, times(3)).getComposedTypeForCode(any());
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).size(), is(3));
	}

	@Test
	public void buildSetsConfigurationContainingTypecodeAndIncludedAttributesForEachConfiguration()
	{
		when(service.getCheckingAllAttributes()).thenReturn(false);
		when(service.getCheckingAllTypes()).thenReturn(true);

		service.buildTypeConfigurations().get();

		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(0).getTypecode(), is(TYPE_CODE));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(0).getInclude(), hasSize(2));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(0).getInclude(), containsInAnyOrder(ATTRIBUTE_1, ATTRIBUTE_3));

		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(1).getTypecode(), is(OTHER_TYPE_CODE));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(1).getInclude(), hasSize(1));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(1).getInclude(), contains(ATTRIBUTE_4));

		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(2).getTypecode(), is(SUPER_TYPE_CODE));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(2).getInclude(), hasSize(2));
		assertThat(service.getAllTypePermissionConfigs(service.getPermissionConfigs()).get(2).getInclude(), containsInAnyOrder(ATTRIBUTE_2, ATTRIBUTE_3));
	}

	@Test
	public void returnsTrueIfAllTypesAreVerifiable() throws Exception
	{
		when(service.getCheckingAllTypes()).thenReturn(true);
		when(service.getCheckingAllAttributes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isTypeVerifiable(TYPE_CODE);

		assertTrue(isVerifiable);
	}

	@Test
	public void returnsTrueIfGivenTypeIsVerifiable() throws Exception
	{
		when(service.getCheckingAllTypes()).thenReturn(false);
		when(service.getCheckingAllAttributes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isTypeVerifiable(TYPE_CODE);

		assertTrue(isVerifiable);
	}

	@Test
	public void returnsFalseIfGivenTypeIsNotVerifiable() throws Exception
	{
		when(service.getCheckingAllTypes()).thenReturn(false);
		when(service.getCheckingAllAttributes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isTypeVerifiable(UNKNOWN_TYPE_CODE);

		assertFalse(isVerifiable);
	}

	@Test
	public void returnsTrueIfAllAttributesAreVerifiable() throws Exception
	{
		when(service.getCheckingAllAttributes()).thenReturn(true);
		when(service.getCheckingAllTypes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isAttributeVerifiable(TYPE_CODE, ATTRIBUTE_1);

		assertTrue(isVerifiable);
	}

	@Test
	public void returnsTrueIfGivenAttributeIsVerifiable() throws Exception
	{
		when(service.getCheckingAllAttributes()).thenReturn(false);
		when(service.getCheckingAllTypes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isAttributeVerifiable(TYPE_CODE, ATTRIBUTE_1);

		assertTrue(isVerifiable);
	}

	@Test
	public void returnsFalseIfGivenAttributeIsNotVerifiable() throws Exception
	{
		when(service.getCheckingAllAttributes()).thenReturn(false);
		when(service.getCheckingAllTypes()).thenReturn(true);

		service.afterPropertiesSet();
		boolean isVerifiable = service.isAttributeVerifiable(TYPE_CODE, ATTRIBUTE_6);

		assertFalse(isVerifiable);
	}

}
