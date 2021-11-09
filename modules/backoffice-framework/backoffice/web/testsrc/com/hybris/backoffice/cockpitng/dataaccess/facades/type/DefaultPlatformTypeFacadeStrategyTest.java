/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.TypeOfCollectionEnum;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPlatformTypeFacadeStrategyTest
{
	@Spy
	@InjectMocks
	private DefaultPlatformTypeFacadeStrategy testSubject;

	@Mock
	private TypeService typeService;

	@Test
	public void shouldReturnNullWhenAttributeNotFoundOnType()
	{
		// given
		when(typeService.getAttributeDescriptor(eq("type"), eq("qualifier"))).thenThrow(UnknownIdentifierException.class);

		// when
		final String attributeDescription = testSubject.getAttributeDescription("type", "qualifier");

		// then
		assertThat(attributeDescription).isNull();
	}

	@Test
	public void shouldReturnCollectionTypeWhenPlatformTypeIsCollection()
	{
		//when
		final DataType.Type type  = testSubject.convertPlatformCollectionTypeToDatatypeType(TypeOfCollectionEnum.COLLECTION);

		//then
		assertThat(type).isEqualTo(DataType.Type.COLLECTION);
	}

	@Test
	public void shouldReturnListTypeWhenPlatformTypeIsList()
	{
		//when
		final DataType.Type type  = testSubject.convertPlatformCollectionTypeToDatatypeType(TypeOfCollectionEnum.LIST);

		//then
		assertThat(type).isEqualTo(DataType.Type.LIST);
	}

	@Test
	public void shouldReturnSetTypeWhenPlatformTypeIsSet()
	{
		//when
		final DataType.Type type = testSubject.convertPlatformCollectionTypeToDatatypeType(TypeOfCollectionEnum.SET);

		//then
		assertThat(type).isEqualTo(DataType.Type.SET);
	}

	@Test
	public void shouldCreateCollectionTypeBuilderUsingConvertedDatatype()
	{
		//when
		testSubject.createCollectionTypeBuilder("dataType", TypeOfCollectionEnum.COLLECTION);

		//then
		final ArgumentCaptor<TypeOfCollectionEnum> captor = ArgumentCaptor.forClass(TypeOfCollectionEnum.class);
		verify(testSubject).convertPlatformCollectionTypeToDatatypeType(captor.capture());
		assertThat(captor.getValue()).isEqualTo(TypeOfCollectionEnum.COLLECTION);
	}

}
