/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.solrsearch.resolvers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.when;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationCatalogVersionValueProviderTest
{

	private static final String CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME = "fieldName1";
	private static final String CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME_2 = "fieldName2";
	private final PK classificationSystemVersionPK = PK.fromLong(1L);
	private final PK classificationSystemVersionPK2 = PK.fromLong(2L);

	@InjectMocks
	private ClassificationCatalogVersionValueProvider classificationCatalogVersionValueProvider;
	@Mock
	private FieldNameProvider fieldNameProvider;
	@Mock
	private IndexConfig indexConfig;
	@Mock
	private IndexedProperty indexedProperty;
	@Mock
	private ProductModel product;
	@Mock
	private ClassificationSystemVersionModel classificationSystemVersion;
	@Mock
	private ClassificationSystemVersionModel classificationSystemVersion2;
	@Mock
	private ClassificationClassModel classificationClass;


	@Test
	public void shouldReturnAllClassificationSystemVersionsOfProduct() throws FieldValueProviderException
	{
		//given
		when(product.getCatalogVersion()).thenReturn(classificationSystemVersion);

		when(classificationClass.getCatalogVersion()).thenReturn(classificationSystemVersion2);
		final List<ClassificationClassModel> classificationClasses = Collections.singletonList(classificationClass);
		when(product.getClassificationClasses()).thenReturn(classificationClasses);

		when(fieldNameProvider.getFieldNames(same(indexedProperty), any())).thenReturn(Arrays.asList(CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME, CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME_2));

		when(classificationSystemVersion.getPk()).thenReturn(classificationSystemVersionPK);
		when(classificationSystemVersion2.getPk()).thenReturn(classificationSystemVersionPK2);

		//when
		final Collection<FieldValue> classificationSystemVersionValues = classificationCatalogVersionValueProvider.getFieldValues(indexConfig, indexedProperty, product);

		//then
		assertThat(classificationSystemVersionValues).hasSize(4);
		assertThat(classificationSystemVersionValues).extracting(FieldValue::getFieldName, FieldValue::getValue).contains(new Tuple(CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME, 1L));
		assertThat(classificationSystemVersionValues).extracting(FieldValue::getFieldName, FieldValue::getValue).contains(new Tuple(CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME, 2L));
		assertThat(classificationSystemVersionValues).extracting(FieldValue::getFieldName, FieldValue::getValue).contains(new Tuple(CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME_2, 1L));
		assertThat(classificationSystemVersionValues).extracting(FieldValue::getFieldName, FieldValue::getValue).contains(new Tuple(CLASSIFICATION_SYSTEM_VERSION_FIELD_NAME_2, 2L));
	}

}