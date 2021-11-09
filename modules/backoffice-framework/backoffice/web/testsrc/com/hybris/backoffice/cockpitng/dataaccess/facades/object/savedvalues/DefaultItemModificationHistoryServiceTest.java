/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;


@RunWith(MockitoJUnitRunner.class)
public class DefaultItemModificationHistoryServiceTest
{
	private static final Locale LOCALE_ES_CO = Locale.forLanguageTag("es-CO");
	private static final Locale LOCALE_ES = Locale.forLanguageTag("es");
	private static final String HASTA_LA_VISTA = "hasta la vista";
	private static final String HASTA_MANIANA = "hasta maniana";

	@Spy
	@InjectMocks
	private DefaultItemModificationHistoryService modificationHistoryService;

	@Mock
	private TypeFacade typeFacade;
	@Mock
	private ModelService modelService;
	@Mock
	private ItemModelContextImpl itemModelContext;
	@Mock
	private ModelValueHistory modelValueHistory;
	@Mock
	private I18NService i18NService;
	@Mock
	private CommonI18NService commonI18NService;

	@Test
	public void shouldCreateModificationInfoWhenPrivateAttributesAreAccessible() throws TypeNotFoundException
	{
		// given
		final ProductModel product = new ProductModel(itemModelContext);
		final String code = "code";
		final String catalog = "catalog";
		final String identifier = "identifier";

		final DataType datatype = mock(DataType.class);
		final DataAttribute dataAttribute = mock(DataAttribute.class);
		when(datatype.getAttribute(code)).thenReturn(dataAttribute);
		when(datatype.getAttribute(catalog)).thenReturn(null);
		when(datatype.getAttribute(identifier)).thenReturn(dataAttribute);
		when(typeFacade.getType(product)).thenReturn(ProductModel._TYPECODE);
		when(typeFacade.load(ProductModel._TYPECODE)).thenReturn(datatype);
		when(itemModelContext.getValueHistory()).thenReturn(modelValueHistory);
		when(Boolean.valueOf(modelValueHistory.isDirty())).thenReturn(Boolean.TRUE);
		when(modelValueHistory.getDirtyAttributes()).thenReturn(Stream.of(code, catalog, identifier).collect(Collectors.toSet()));

		// when
		modificationHistoryService.createModificationInfo(product);

		// then
		verify(modelService).getAttributeValue(product, code);
		verify(modelService, never()).getAttributeValue(product, catalog);
		verify(modelService).getAttributeValue(product, identifier);
	}

	@Test
	public void shouldConvertToPersistenceLayerForLocalizedAttributeEsCOLang()
	{
		// given
		final LanguageModel languageModelEs = mock(LanguageModel.class);
		final LanguageModel languageModelEsCo = mock(LanguageModel.class);

		when(i18NService.getBestMatchingLocale(LOCALE_ES_CO)).thenReturn(LOCALE_ES_CO);
		when(i18NService.getBestMatchingLocale(LOCALE_ES)).thenReturn(LOCALE_ES);
		when(commonI18NService.getLanguage(LOCALE_ES.toString())).thenReturn(languageModelEs);
		when(commonI18NService.getLanguage(LOCALE_ES_CO.toString())).thenReturn(languageModelEsCo);

		final Language jaloEs = new LanguageStub("es", 1L);
		final Language jaloEsCo = new LanguageStub("es_CO", 2L);

		when(modelService.getSource(languageModelEs)).thenReturn(jaloEs);
		when(modelService.getSource(languageModelEsCo)).thenReturn(jaloEsCo);
		when(modelService.toPersistenceLayer(HASTA_LA_VISTA)).thenReturn(HASTA_LA_VISTA);
		when(modelService.toPersistenceLayer(HASTA_MANIANA)).thenReturn(HASTA_MANIANA);

		final Map<Locale, Object> localizedValues = new HashMap<>();
		localizedValues.put(LOCALE_ES_CO, HASTA_LA_VISTA);
		localizedValues.put(LOCALE_ES, HASTA_MANIANA);

		// when
		final Map<Language, Object> toPersist = (Map<Language, Object>) modificationHistoryService
				.convertLocalizedMapToPersistenceLayer(localizedValues);

		// then
		assertThat(toPersist).hasSize(localizedValues.size());
		assertThat(toPersist.get(jaloEsCo)).isEqualTo(HASTA_LA_VISTA);
		assertThat(toPersist.get(jaloEs)).isEqualTo(HASTA_MANIANA);
	}

	@Test
	public void shouldLogModificationOfARemovedEntityUsingPk()
	{
		//given
		final ItemModificationInfo info = mock(ItemModificationInfo.class);
		when(info.getModifiedAttributes()).thenReturn(Collections.emptySet());

		final ItemModel removedEntity = mock(ItemModel.class);
		final PK removedEntitiesPK = PK.fromLong(System.nanoTime());

		when(modelService.isRemoved(removedEntity)).thenReturn(true);
		when(removedEntity.getPk()).thenReturn(removedEntitiesPK);

		final JaloConnection jaloConnection = mock(JaloConnection.class);
		doReturn(jaloConnection).when(modificationHistoryService).getJaloConnection();

		final Item removedJalo = mock(Item.class);
		when(removedJalo.getPK()).thenReturn(removedEntitiesPK);
		when(modelService.getSource(removedEntity)).thenReturn(removedJalo);

		//when
		modificationHistoryService.logModifications(removedEntity, info);

		//then
		verify(jaloConnection).logItemRemoval(removedJalo, false);
		verifyNoMoreInteractions(jaloConnection);
	}

	@Test
	public void shouldHideEncryptedAttributes() throws TypeNotFoundException
	{
		// given
		final String typeCode = "itemTypeCode";
		final String attribute = "attribute";
		final String encrypted = "encrypted";

		final ItemModel item = mock(ItemModel.class);
		final DataType dataType = mock(DataType.class);

		doReturn(typeCode).when(typeFacade).getType(item);
		doReturn(dataType).when(typeFacade).load(typeCode);
		doReturn(modelValueHistory).when(itemModelContext).getValueHistory();
		doReturn(true).when(modelValueHistory).isDirty();
		doReturn(Stream.of(attribute, encrypted).collect(Collectors.toSet())).when(modelValueHistory).getDirtyAttributes();
		doReturn(modelValueHistory).when(itemModelContext).getValueHistory();
		final DataAttribute dataAttribute = prepareDataAttribute(item, dataType, attribute, "attributeValueOld",
				"attributeValueNew", false);
		final DataAttribute encryptedDataAttribute = prepareDataAttribute(item, dataType, encrypted, "encryptedValueOld",
				"encryptedValueNew", true);

		// when
		final ItemModificationInfo itemModificationInfo = modificationHistoryService.computeModificationsForModifiedItem(item,
				modelValueHistory, false);

		// then
		verify(modificationHistoryService).isEncrypted(item, attribute);
		verify(modificationHistoryService).isEncrypted(item, encrypted);

		assertThat(itemModificationInfo.getModifiedValue(attribute)).isEqualTo("attributeValueNew");
		assertThat(itemModificationInfo.getModifiedValue(encrypted)).isEqualTo(null);
		assertThat(itemModificationInfo.getOriginalValue(attribute)).isEqualTo("attributeValueOld");
		assertThat(itemModificationInfo.getOriginalValue(encrypted)).isEqualTo(null);
	}

	private DataAttribute prepareDataAttribute(final ItemModel item, final DataType dataType, final String attribute,
			final String attributeValueOld, final String attributeValueNew, final boolean encrypted)
	{
		final DataAttribute dataAttribute = mock(DataAttribute.class);
		doReturn(dataAttribute).when(dataType).getAttribute(attribute);
		doReturn(encrypted).when(dataAttribute).isEncrypted();
		doReturn(attributeValueOld).when(modificationHistoryService).getOriginalValue(item, attribute, modelValueHistory);
		doReturn(attributeValueNew).when(modelService).getAttributeValue(item, attribute);
		return dataAttribute;
	}

	private class LanguageStub extends Language
	{
		private final String stringRepresentation;
		private final long longPk;

		public LanguageStub(final String stringRepresentation, final long longPk)
		{
			this.stringRepresentation = stringRepresentation;
			this.longPk = longPk;
		}

		@Override
		public String toString()
		{
			return stringRepresentation;
		}

		@Override
		public PK getPK()
		{
			return de.hybris.platform.core.PK.fromLong(longPk);
		}
	}
}
