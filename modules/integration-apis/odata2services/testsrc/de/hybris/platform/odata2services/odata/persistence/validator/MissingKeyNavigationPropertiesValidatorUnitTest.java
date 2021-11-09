/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package de.hybris.platform.odata2services.odata.persistence.validator;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyNavigationPropertyException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmAnnotationAttribute;
import org.apache.olingo.odata2.api.edm.EdmAnnotations;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MissingKeyNavigationPropertiesValidatorUnitTest
{
	@InjectMocks
	private MissingKeyNavigationPropertiesValidator validator;

	@Mock
	private EdmEntityType entityType;
	@Mock
	private ODataEntry oDataEntry;

	@Before
	public void setUp() throws EdmException
	{
		when(entityType.getName()).thenReturn("Type");
	}
	
	@Test
	public void shouldThrowExceptionWhenNonNullableKeyNavigationPropertiesIsNull() throws EdmException
	{
		final String keyNavName = "keyNav1";
		when(entityType.getNavigationPropertyNames()).thenReturn(Collections.singletonList(keyNavName));
		createKeyNavigationProperty(keyNavName, unique(), nullable(false));

		final Map<String, Object> propertiesMap = new HashMap<>();
		when(oDataEntry.getProperties()).thenReturn(propertiesMap);

		assertThatThrownBy(() -> validator.beforeItemLookup(entityType, oDataEntry))
				.isInstanceOf(MissingKeyNavigationPropertyException.class)
				.hasFieldOrPropertyWithValue("errorCode", "missing_key")
				.hasMessage("Key NavigationProperty [keyNav1] is required for EntityType [Type].");
	}

	@Test
	public void shouldNotThrowExceptionWhenNullableKeyNavigationPropertiesIsNull() throws EdmException
	{
		final String keyNavName = "keyNav1";
		when(entityType.getNavigationPropertyNames()).thenReturn(Collections.singletonList(keyNavName));
		createKeyNavigationProperty(keyNavName, unique(), nullable(true));

		final Map<String, Object> propertiesMap = new HashMap<>();
		when(oDataEntry.getProperties()).thenReturn(propertiesMap);

		validator.beforeItemLookup(entityType, oDataEntry);
	}

	@Test
	public void checkKeyNavigationProperties() throws EdmException
	{
		final String keyNavName = "property1";

		when(entityType.getNavigationPropertyNames()).thenReturn(Collections.singletonList(keyNavName));
		createKeyNavigationProperty(keyNavName, unique(), nullable(false));

		final Map<String, Object> propertiesMap = new HashMap<>();
		propertiesMap.put(keyNavName, new Object());
		when(oDataEntry.getProperties()).thenReturn(propertiesMap);

		validator.beforeItemLookup(entityType, oDataEntry);
	}

	private void createKeyNavigationProperty(final String name, final EdmAnnotationAttribute... annotationAttributes) throws EdmException
	{
		final EdmNavigationProperty p = mock(EdmNavigationProperty.class);
		when(entityType.getNavigationPropertyNames()).thenReturn(Collections.singletonList(name));
		when(p.getName()).thenReturn(name);

		when(entityType.getProperty(name)).thenReturn(p);
		final EdmAnnotations annotations = mock(EdmAnnotations.class);
		when(annotations.getAnnotationAttributes()).thenReturn(Arrays.asList(annotationAttributes));
		when(p.getAnnotations()).thenReturn(annotations);
	}

	private EdmAnnotationAttribute unique()
	{
		return annotationAttribute("s:IsUnique", true);
	}

	private EdmAnnotationAttribute nullable(final boolean value)
	{
		return annotationAttribute("Nullable", value);
	}

	private EdmAnnotationAttribute annotationAttribute(final String nullable, final boolean value)
	{
		final EdmAnnotationAttribute nullableAnnotationAttribute = mock(EdmAnnotationAttribute.class);
		when(nullableAnnotationAttribute.getName()).thenReturn(nullable);
		when(nullableAnnotationAttribute.getText()).thenReturn(String.valueOf(value));
		return nullableAnnotationAttribute;
	}
}
