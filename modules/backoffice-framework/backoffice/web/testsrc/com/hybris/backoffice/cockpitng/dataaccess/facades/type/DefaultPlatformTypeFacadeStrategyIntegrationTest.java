/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.junit.Before;
import org.junit.Test;

import com.hybris.backoffice.cockpitng.dataaccess.facades.common.PlatformFacadeStrategyHandleCache;
import com.hybris.backoffice.cockpitng.dataaccess.facades.type.expression.AttributeExpressionResolverFactory;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.i18n.CockpitLocaleService;


@IntegrationTest
public class DefaultPlatformTypeFacadeStrategyIntegrationTest extends ServicelayerTransactionalBaseTest
{
	private DefaultPlatformTypeFacadeStrategy typeFacade;
	@Resource
	private TypeService typeService;
	@Resource
	private ModelService modelService;
	@Resource
	private VariantsService variantsService;
	@Resource
	private I18NService i18nService;
	@Resource
	private FlexibleSearchService flexibleSearchService;

	private PlatformFacadeStrategyHandleCache platformFacadeStrategyHandleCache;

	private AttributeExpressionResolverFactory attributeExpressionResolverFactory;

	@Before
	public void setUp() throws Exception
	{
		attributeExpressionResolverFactory = mock(AttributeExpressionResolverFactory.class);
		final CockpitLocaleService cockpitLocaleService = mock(CockpitLocaleService.class);
		doReturn(List.of(Locale.ENGLISH)).when(cockpitLocaleService).getAllUILocales();

		platformFacadeStrategyHandleCache = new PlatformFacadeStrategyHandleCache();
		platformFacadeStrategyHandleCache.setTypeService(typeService);

		final DefaultTypeSystemLocalizationHelper typeSystemLocalizationHelper = new DefaultTypeSystemLocalizationHelper();
		typeSystemLocalizationHelper.setCockpitLocaleService(cockpitLocaleService);
		typeSystemLocalizationHelper.setFlexibleSearchService(flexibleSearchService);

		typeFacade = new DefaultPlatformTypeFacadeStrategy();
		typeFacade.setTypeService(typeService);
		typeFacade.setI18nService(i18nService);
		typeFacade.setCockpitLocaleService(cockpitLocaleService);
		typeFacade.setModelService(modelService);
		typeFacade.setPlatformFacadeStrategyHandleCache(platformFacadeStrategyHandleCache);
		typeFacade.setResolverFactory(attributeExpressionResolverFactory);
		typeFacade.setVariantsService(variantsService);
		typeFacade.setTypeSystemLocalizationHelper(typeSystemLocalizationHelper);
	}

	@Test
	public void shouldConvertProduct()
	{
		//given
		final ComposedTypeModel type = typeService.getComposedTypeForCode("Product");

		//when
		final DataType dataType = typeFacade.convertType(type, false, new DefaultContext());

		//then
		final long attributesCount = getAttributesCount(type);
		assertThat(dataType.getCode()).isEqualTo(type.getCode());
		assertThat(dataType.getAttributes()).hasSize((int) attributesCount);
	}

	@Test
	public void shouldConvertVariantProduct()
	{
		//given
		final ComposedTypeModel type = typeService.getComposedTypeForCode("VariantProduct");

		//when
		final DataType dataType = typeFacade.convertType(type, false, new DefaultContext());

		//then
		final long attributesCount = getAttributesCount(type);
		assertThat(dataType.getCode()).isEqualTo(type.getCode());
		assertThat(dataType.getAttributes()).hasSize((int) attributesCount);
	}

	@Test
	public void shouldConvertPriceRow()
	{
		// given
		final ComposedTypeModel type = typeService.getComposedTypeForCode("PriceRow");
		final String priceGroup = "pg";
		final String userGroup = "ug";

		// when
		final DataType dataType = typeFacade.convertType(type, false, new DefaultContext());

		// then
		final long attributesCount = getAttributesCount(type);
		assertThat(dataType.getCode()).isEqualTo(type.getCode());
		assertThat(dataType.getAttributes()).hasSize((int) attributesCount);
		// re-declared attributes have correct type
		assertThat(dataType.getAttribute(priceGroup).getValueType().getCode()).isEqualTo(getAttributeTypeCode(type, priceGroup));
		assertThat(dataType.getAttribute(userGroup).getValueType().getCode()).isEqualTo(getAttributeTypeCode(type, userGroup));

	}

	@Test
	public void shouldConvertModelWithRuntimeAttribute()
	{
		final String qualifier = "runtime-" + UUID.randomUUID();
		//given
		createRuntimeAttribute(TitleModel.class,qualifier);
		final ComposedTypeModel type = typeService.getComposedTypeForCode("Title");

		//when
		final DataType dataType = typeFacade.convertType(type, false, new DefaultContext());

		//then
		assertThat(dataType.getAttribute(qualifier).isRuntimeAttribute()).isEqualTo(true);
		assertThat(dataType.getAttribute("name").isRuntimeAttribute()).isEqualTo(false);
	}

	@Test
	public void shouldConvertModelWithNonReadableAttributes()
	{
		// given
		// Because Task type have non-readable attribute named executionHourMillis
		final ComposedTypeModel type = typeService.getComposedTypeForCode("Task");

		// when
		final DataType dataType = typeFacade.convertType(type, false, new DefaultContext());

		// then
		final long attributesCount = getReadableAttributesCount(type);
		assertThat(dataType.getCode()).isEqualTo(type.getCode());
		assertThat(dataType.getAttributes()).hasSize((int) attributesCount);
	}

	private AttributeDescriptorModel createRuntimeAttribute(final Class clazz, final String qualifier)
	{
		final AttributeDescriptorModel runtimeAttribute = modelService.create(AttributeDescriptorModel.class);
		runtimeAttribute.setAttributeType(typeService.getAtomicTypeForJavaClass(String.class));
		runtimeAttribute.setEnclosingType(typeService.getComposedTypeForClass(clazz));
		runtimeAttribute.setGenerate(false);
		runtimeAttribute.setPartOf(false);
		runtimeAttribute.setQualifier(qualifier);
		modelService.saveAll();
		modelService.detachAll();
		return runtimeAttribute;
	}

	private long getAttributesCount(final ComposedTypeModel type)
	{
		return typeService.getAttributeDescriptorsForType(type)//
				.stream()//
				.filter(attr -> !BooleanUtils.isTrue(attr.getHiddenForUI()))//
				.count();
	}

	private long getReadableAttributesCount(final ComposedTypeModel type)
	{
		return typeService.getAttributeDescriptorsForType(type)//
				.stream()//
				.filter(attr -> !BooleanUtils.isTrue(attr.getHiddenForUI()))//
				.filter(attr -> BooleanUtils.isTrue(attr.getReadable()))//
				.count();
	}

	private String getAttributeTypeCode(final ComposedTypeModel type, final String qualifier)
	{
		return typeService.getAttributeDescriptor(type.getCode(), qualifier).getAttributeType().getCode();
	}
}
