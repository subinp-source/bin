/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.object;

import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultPlatformPermissionAwareObjectFacadeTest
{
	@Spy
	@InjectMocks
	DefaultPlatformPermissionAwareObjectFacade defaultPlatformPermissionAwareObjectFacade;

	@Test
	public void shouldGetModifiedProperties()
	{
		// given
		final AbstractItemModel objectToSave = mock(AbstractItemModel.class);
		final ItemModelContext itemModelContext = mock(ItemModelContext.class);

		final Map<Locale, Set<String>> dirtyLocalizedAttributes = prepareDirtyLocalizedAttributes();
		final Set<String> dirtyAttributes = prepareDirtyAttributes();

		when(itemModelContext.getDirtyLocalizedAttributes()).thenReturn(dirtyLocalizedAttributes);
		when(itemModelContext.getDirtyAttributes()).thenReturn(dirtyAttributes);

		doReturn(itemModelContext).when(objectToSave).getItemModelContext();

		final DataType dataType = mock(DataType.class);
		when(dataType.getAttribute(anyString())).thenReturn(mock(DataAttribute.class));

		doReturn(dataType).when(defaultPlatformPermissionAwareObjectFacade).loadDataType(objectToSave);

		// when
		final Set<String> modifiedProperties = defaultPlatformPermissionAwareObjectFacade.getModifiedProperties(objectToSave,
				mock(Context.class));

		// then
		final Set<String> allAttributes = getAllAttributes(dirtyLocalizedAttributes, dirtyAttributes);
		assertThat(modifiedProperties).contains(allAttributes.stream().toArray(String[]::new));
	}

	@Test
	public void shouldIgnorePropertiesWithDefaultValue()
	{
		// given
		final AbstractItemModel objectToSave = mock(AbstractItemModel.class);
		final ItemModelContextImpl itemModelContext = mock(ItemModelContextImpl.class);

		final Map<Locale, Set<String>> dirtyLocalizedAttributes = Map.of();
		final String dirtyAttributeWithDefaultValueQualifier = "approvalStatus";
		final String dirtyAttributeWithoutDefaultValueQualifier = "code";

		final DataAttribute dirtyAttributeWithDefaultValue = mock(DataAttribute.class);
		when(dirtyAttributeWithDefaultValue.getQualifier()).thenReturn(dirtyAttributeWithDefaultValueQualifier);
		when(dirtyAttributeWithDefaultValue.getDefaultValue()).thenReturn("check");
		when(itemModelContext.getPropertyValue(dirtyAttributeWithDefaultValueQualifier)).thenReturn("check");

		final DataAttribute dirtyAttributeWithoutDefaultValue = mock(DataAttribute.class);
		when(dirtyAttributeWithoutDefaultValue.getQualifier()).thenReturn(dirtyAttributeWithoutDefaultValueQualifier);
		when(dirtyAttributeWithoutDefaultValue.getDefaultValue()).thenReturn(null);
		when(itemModelContext.getPropertyValue(dirtyAttributeWithoutDefaultValueQualifier)).thenReturn(null);

		final Set<String> dirtyAttributes = Set.of(dirtyAttributeWithDefaultValueQualifier,
				dirtyAttributeWithoutDefaultValueQualifier);

		when(itemModelContext.getDirtyLocalizedAttributes()).thenReturn(dirtyLocalizedAttributes);
		when(itemModelContext.getDirtyAttributes()).thenReturn(dirtyAttributes);
		when(itemModelContext.isNew()).thenReturn(true);

		doReturn(itemModelContext).when(objectToSave).getItemModelContext();

		final DataType dataType = mock(DataType.class);
		doReturn(dataType).when(defaultPlatformPermissionAwareObjectFacade).loadDataType(objectToSave);
		when(dataType.getAttribute(dirtyAttributeWithDefaultValueQualifier)).thenReturn(dirtyAttributeWithDefaultValue);
		when(dataType.getAttribute(dirtyAttributeWithoutDefaultValueQualifier)).thenReturn(dirtyAttributeWithoutDefaultValue);

		// when
		final Set<String> modifiedProperties = defaultPlatformPermissionAwareObjectFacade.getModifiedProperties(objectToSave,
				mock(Context.class));

		// then
		assertThat(modifiedProperties).hasSize(1);
	}

	private Map<Locale, Set<String>> prepareDirtyLocalizedAttributes()
	{
		final Map<Locale, Set<String>> dirtyLocalizedAttributes = new HashMap<>();
		dirtyLocalizedAttributes.put(Locale.ENGLISH, new HashSet<>(Arrays.asList("en-attr1", "en-attr2", "en-attr3")));
		dirtyLocalizedAttributes.put(Locale.FRENCH, new HashSet<>(Arrays.asList("fr-attr1", "fr-attr2", "fr-attr3")));

		return dirtyLocalizedAttributes;
	}

	private Set<String> prepareDirtyAttributes()
	{
		return new HashSet<>(Arrays.asList("attr1", "attr2", "attr3"));
	}

	private Set<String> getAllAttributes(final Map<Locale, Set<String>> localizedAttributes, final Set<String> attributes)
	{
		final Collection<String> dirtyLocalizedAttributes = localizedAttributes.entrySet().stream()
				.flatMap(locAttr -> locAttr.getValue().stream()).collect(toSet());

		return Stream.of(attributes, dirtyLocalizedAttributes).flatMap(Collection::stream).collect(toSet());
	}
}
