/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.configurablebundlefacades.order.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.configurablebundlefacades.data.BundleTemplateData;
import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.tenant.MockTenant;
import org.fest.assertions.MapAssert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link BundleCommerceCartPopulator}
 */
@UnitTest
public class BundleCommerceCartPopulatorTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private PromotionsService promotionsService;
	@Mock
	private ModelService modelService;
	@Mock
	private Converter<PromotionResultModel, PromotionResultData> promotionResultConverter;
	@Mock
	private EntryGroupService entryGroupService;
	@Mock
	private BundleTemplateService bundleTemplateService;
	@Mock
	private Converter<BundleTemplateModel, BundleTemplateData> bundleTemplateConverter;
	@InjectMocks
	private final BundleCommerceCartPopulator<CartModel, CartData> bundleCartPopulator = new BundleCommerceCartPopulator<>();
	@Mock
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;

	private CartModel source;
	private CartData target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		bundleCartPopulator.setModelService(modelService);
		bundleCartPopulator.setPromotionResultConverter(promotionResultConverter);
		bundleCartPopulator.setOrderEntryConverter(orderEntryConverter);

		source = mock(CartModel.class);
		target = new CartData();
		source.setEntries(Collections.emptyList());
	}

	@Test
	public void shouldThrowExceptionWhenSourceIsNull()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter source can not be null");

		bundleCartPopulator.populate(null, new CartData());
	}

	@Test
	public void shouldThrowExceptionWhenTargetIsNull()
	{
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Parameter target can not be null");

		bundleCartPopulator.populate(new CartModel(), null);
	}

	@Test
	public void testPopulateNoEntries()
	{
		source.setEntries(null);

		bundleCartPopulator.populate(source, target);

		assertThat(target.getEntries()).isNullOrEmpty();
	}

	@Test
	public void testPopulateEmptyEntries()
	{
		bundleCartPopulator.populate(source, target);

		assertThat(target.getEntries()).isNullOrEmpty();
		assertThat(target.getFirstIncompleteBundleComponentsMap()).isNullOrEmpty();
		assertThat(target.getPotentialOrderPromotions()).isNullOrEmpty();
		assertThat(target.getPotentialProductPromotions()).isNullOrEmpty();
	}

	@Test
	public void testPopulateWithEntries()
	{
		final OrderEntryModel orderEntryModel1 = new OrderEntryModel();
		orderEntryModel1.setEntryGroupNumbers(Collections.singleton(1));
		final OrderEntryModel orderEntryModel2 = new OrderEntryModel();
		orderEntryModel2.setEntryGroupNumbers(Collections.singleton(1));

		final OrderEntryData orderEntryData1 = new OrderEntryData();
		orderEntryData1.setEntryGroupNumbers(Collections.singleton(1));
		final OrderEntryData orderEntryData2 = new OrderEntryData();
		orderEntryData2.setEntryGroupNumbers(Collections.singleton(1));

		final EntryGroup group = new EntryGroup();
		group.setGroupNumber(1);
		group.setExternalReferenceId("component");
		given(entryGroupService.getGroupOfType(eq(source), anyCollection(), any())).willReturn(group);
		final BundleTemplateModel bundleTemplate = new BundleTemplateModel();
		bundleTemplate.setId("component");
		given(bundleTemplateService.getBundleTemplateForCode(any())).willReturn(bundleTemplate);
		final BundleTemplateData bundleTemplateData = new BundleTemplateData();
		bundleTemplateData.setId("component");
		given(bundleTemplateConverter.convert(eq(bundleTemplate))).willReturn(bundleTemplateData);

		given(source.getEntries()).willReturn(Arrays.asList(orderEntryModel1, orderEntryModel2));
		given(orderEntryConverter.convertAll(Arrays.asList(orderEntryModel1, orderEntryModel2)))
				.willReturn(Arrays.asList(orderEntryData1, orderEntryData2));

		bundleCartPopulator.populate(source, target);

		// Some complex bundleTemplateService interactions are left for OrderComparator test
		assertThat(target.getEntries()).containsExactly(orderEntryData1, orderEntryData2);
	}

	@Test
	public void testPopulateWithIncompleteComponets()
	{
		final OrderEntryModel orderEntryModel1 = new OrderEntryModel();
		orderEntryModel1.setEntryGroupNumbers(Collections.singleton(1));

		final OrderEntryModel orderEntryModel2 = new OrderEntryModel();
		orderEntryModel2.setEntryGroupNumbers(Collections.singleton(1));

		final OrderEntryModel orderEntryModel3 = new OrderEntryModel();
		orderEntryModel3.setEntryGroupNumbers(Collections.singleton(1));

		final OrderEntryData orderEntryData1 = new OrderEntryData();
		orderEntryData1.setEntryGroupNumbers(Collections.singleton(1));
		orderEntryData1.setValid(true);
		final BundleTemplateData bundleTemplateData1 = new BundleTemplateData();
		bundleTemplateData1.setId("component1");

		final OrderEntryData orderEntryData2 = new OrderEntryData();
		orderEntryData2.setEntryGroupNumbers(Collections.singleton(2));
		orderEntryData2.setValid(false);
		final BundleTemplateData bundleTemplateData2 = new BundleTemplateData();
		bundleTemplateData2.setId("component2");

		final OrderEntryData orderEntryData3 = new OrderEntryData();
		orderEntryData3.setEntryGroupNumbers(Collections.singleton(3));
		orderEntryData3.setValid(false);
		final BundleTemplateData bundleTemplateData3 = new BundleTemplateData();
		bundleTemplateData3.setId("component3");

		given(source.getEntries()).willReturn(Arrays.asList(orderEntryModel1, orderEntryModel2, orderEntryModel3));
		given(orderEntryConverter.convertAll(Arrays.asList(orderEntryModel1, orderEntryModel2, orderEntryModel3)))
				.willReturn(Arrays.asList(orderEntryData1, orderEntryData2, orderEntryData3));

		EntryGroup group1 = new EntryGroup();
		group1.setGroupNumber(1);
		group1.setExternalReferenceId("component1");
		given(entryGroupService.getGroupOfType(eq(source), eq(Collections.singleton(1)), any())).willReturn(group1);

		EntryGroup group2 = new EntryGroup();
		group2.setGroupNumber(2);
		group2.setExternalReferenceId("component2");
		given(entryGroupService.getGroupOfType(eq(source), eq(Collections.singleton(2)), any())).willReturn(group2);

		EntryGroup group3 = new EntryGroup();
		group3.setGroupNumber(3);
		group3.setExternalReferenceId("component3");
		given(entryGroupService.getGroupOfType(eq(source), eq(Collections.singleton(3)), any())).willReturn(group3);

		BundleTemplateModel bundleTemplate1 = new BundleTemplateModel();
		bundleTemplate1.setId("component1");
		BundleTemplateModel bundleTemplate2 = new BundleTemplateModel();
		bundleTemplate2.setId("component2");
		BundleTemplateModel bundleTemplate3 = new BundleTemplateModel();
		bundleTemplate3.setId("component3");
		given(bundleTemplateService.getBundleTemplateForCode(eq("component1"))).willReturn(bundleTemplate1);
		given(bundleTemplateService.getBundleTemplateForCode(eq("component2"))).willReturn(bundleTemplate2);
		given(bundleTemplateService.getBundleTemplateForCode(eq("component3"))).willReturn(bundleTemplate3);
		given(bundleTemplateConverter.convert(eq(bundleTemplate1))).willReturn(bundleTemplateData1);
		given(bundleTemplateConverter.convert(eq(bundleTemplate2))).willReturn(bundleTemplateData2);
		given(bundleTemplateConverter.convert(eq(bundleTemplate3))).willReturn(bundleTemplateData3);

		bundleCartPopulator.populate(source, target);

		assertThat(target.getFirstIncompleteBundleComponentsMap()).hasSize(1);
		assertThat(target.getFirstIncompleteBundleComponentsMap()).includes(MapAssert.entry(2, bundleTemplateData2));
	}

	@Test
	public void testPopulateWithPotentialPromotions()
	{
		final List<PromotionResult> potentialOrderPromotions = Arrays.asList(newPromotionResult(1), newPromotionResult(2));
		final List<PromotionResult> potentialProductPromotions = Arrays.asList(newPromotionResult(3), newPromotionResult(4));
		// This wrapping is required since there is a cast to ArrayList in ModelService.addAll()
		// call from AbstractOrderPopulator.getPromotions() method.
		final List<PromotionResultModel> orderPromotionModels = new ArrayList<>(Arrays.asList(new PromotionResultModel(), new PromotionResultModel()));
		final List<PromotionResultModel> productPromotionModels = new ArrayList<>(Arrays.asList(new PromotionResultModel(), new PromotionResultModel()));
		final List<PromotionResultData> convertedOrderPromotions = Arrays.asList(new PromotionResultData(), new PromotionResultData());
		final List<PromotionResultData> convertedProductPromotions = Arrays.asList(new PromotionResultData(), new PromotionResultData());

		final PromotionOrderResults promotionOrderResults = Mockito.mock(PromotionOrderResults.class);
		given(promotionOrderResults.getPotentialOrderPromotions()).willReturn(potentialOrderPromotions);
		given(promotionOrderResults.getPotentialProductPromotions()).willReturn(potentialProductPromotions);

		given(promotionsService.getPromotionResults(any(AbstractOrderModel.class))).willReturn(promotionOrderResults);
		given(modelService.getAll(eq(potentialOrderPromotions), anyCollection())).willReturn(orderPromotionModels);
		given(modelService.getAll(eq(potentialProductPromotions), anyCollection())).willReturn(productPromotionModels);
		given(promotionResultConverter.convertAll(eq(orderPromotionModels))).willReturn(convertedOrderPromotions);
		given(promotionResultConverter.convertAll(eq(productPromotionModels))).willReturn(convertedProductPromotions);

		bundleCartPopulator.populate(source, target);

		assertThat(target.getPotentialOrderPromotions()).isEqualTo(convertedOrderPromotions);
		assertThat(target.getPotentialProductPromotions()).isEqualTo(convertedProductPromotions);

		verify(promotionsService).getPromotionResults(source);
		verify(modelService).getAll(potentialOrderPromotions, new ArrayList());
		verify(modelService).getAll(potentialProductPromotions, new ArrayList());
		verify(promotionResultConverter).convertAll(orderPromotionModels);
		verify(promotionResultConverter).convertAll(productPromotionModels);
		verifyNoMoreInteractions(promotionsService, modelService, promotionResultConverter);
	}

	// This method is needed in order for promotionResult.equals() method to work correctly.
	private PromotionResult newPromotionResult(final long pkValue)
	{
		final PK pk = PK.fromLong(pkValue);
		final MockTenant tenant = new MockTenant("tenantId");
		final PromotionResult promotionResult = mock(PromotionResult.class);

		given(promotionResult.getPK()).willReturn(pk);
		given(promotionResult.getTenant()).willReturn(tenant);

		return promotionResult;
	}
}