/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.addon.component.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.jsp.PageContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.hybris.merchandising.addon.model.MerchandisingCarouselComponentModel;
import com.hybris.merchandising.constants.MerchandisingaddonConstants;
import com.hybris.merchandising.model.MerchProductDirectoryConfigModel;
import com.hybris.merchandising.service.MerchProductDirectoryConfigService;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.apiregistryservices.model.AbstractDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.DestinationTargetModel;
import de.hybris.platform.apiregistryservices.model.EndpointModel;
import de.hybris.platform.apiregistryservices.strategies.ConsumedDestinationLocatorStrategy;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;

/**
 * Test suite for {@code MerchandisingComponentRenderer}.
 *
 */
@UnitTest
public class MerchandisingComponentRendererTest {

	private static final String CAROUSEL_ID = "123";
	private static final String COMPONENT_ID = "componentID";
	private static final String SERVICE_URL = "serviceUrl";
	private static final String SERVICE_URL_VALUE = "https://myserviceurl";
	private static final String COMPONENT_NAME = "name";
	private static final String COMPONENT_NAME_VALUE = "merchCarousel";
	private static final String CURRENCY = "currency";
	private static final String UNENCODED_CURRENCY_SYMBOL = "£";
	private static final String ENCODED_CURRENCY_SYMBOL = "&pound;";
	
	MerchandisingComponentRenderer<MerchandisingCarouselComponentModel> renderer;
	
	CommerceCommonI18NService commerceCommonI18NService;
	MerchProductDirectoryConfigService productDirectoryConfigService;

	@Before
	public void setUp() {
		renderer = new MerchandisingComponentRenderer<>();

		final ModelService mockModelService = mock(ModelService.class);
		renderer.setModelService(mockModelService);

		final CMSComponentService mockCmsComponentService = mock(CMSComponentService.class);
		renderer.setCmsComponentService(mockCmsComponentService);

		final TypeService mockTypeService = mock(TypeService.class);
		final ComposedTypeModel composedModel = mock(ComposedTypeModel.class);
		Mockito.when(mockTypeService.getComposedTypeForCode(anyString())).thenReturn(composedModel);
		Mockito.when(composedModel.getExtensionName()).thenReturn("merchandisingaddon");
		renderer.setTypeService(mockTypeService);

		final UiExperienceService mockUiExperienceService = mock(UiExperienceService.class);
		Mockito.when(mockUiExperienceService.getUiExperienceLevel()).thenReturn(UiExperienceLevel.DESKTOP);
		renderer.setUiExperienceService(mockUiExperienceService);

		final Map<UiExperienceLevel, String> uiExperienceMap = new HashMap<>();
		uiExperienceMap.put(UiExperienceLevel.DESKTOP, "desktop");
		renderer.setUiExperienceViewPrefixMap(uiExperienceMap);

		final ConsumedDestinationModel model = mock(ConsumedDestinationModel.class);
		final AbstractDestinationModel destinationModel = mock(AbstractDestinationModel.class);
		final EndpointModel endpoint = mock(EndpointModel.class);
		Mockito.when(endpoint.getId()).thenReturn(MerchandisingaddonConstants.STRATEGY_SERVICE);
		Mockito.when(destinationModel.getEndpoint()).thenReturn(endpoint);
		final List<AbstractDestinationModel> destinations = new ArrayList<>();
		
		Mockito.when(destinationModel.getUrl()).thenReturn(SERVICE_URL_VALUE);
		destinations.add(destinationModel);
		final DestinationTargetModel targetModel = mock(DestinationTargetModel.class);
		Mockito.when(targetModel.getDestinations()).thenReturn(destinations);

		Mockito.when(model.getDestinationTarget()).thenReturn(targetModel);
		final ConsumedDestinationLocatorStrategy locatorStrategy = mock(ConsumedDestinationLocatorStrategy.class);
		Mockito.when(locatorStrategy.lookup(MerchandisingaddonConstants.STRATEGY_SERVICE)).thenReturn(model);
	
		renderer.setConsumedDestinationLocatorStrategy(locatorStrategy);
		
		final CurrencyModel currency = mock(CurrencyModel.class);
		when(currency.getSymbol()).thenReturn(UNENCODED_CURRENCY_SYMBOL);
		final MerchProductDirectoryConfigModel productDirectory = mock(MerchProductDirectoryConfigModel.class);
		when(productDirectory.getCurrency()).thenReturn(currency);
		final Optional<MerchProductDirectoryConfigModel> prodDir = Optional.of(productDirectory);

		productDirectoryConfigService = mock(MerchProductDirectoryConfigService.class);
		when(productDirectoryConfigService.getMerchProductDirectoryConfigForCurrentBaseSite()).thenReturn(prodDir);

		commerceCommonI18NService = mock(CommerceCommonI18NService.class);
		when(commerceCommonI18NService.getDefaultCurrency()).thenReturn(currency);
		renderer.setProductDirectoryConfigService(productDirectoryConfigService);
		renderer.setCommerceCommonI18NService(commerceCommonI18NService);
	}

	@Test
	public void testGetVariablesToExpose() 
	{
		testRendering(ENCODED_CURRENCY_SYMBOL);
	}

	@Test
	public void testGetVariablesToExposeNoProductDirectory() {
		when(productDirectoryConfigService.getMerchProductDirectoryConfigForCurrentBaseSite())
				.thenReturn(Optional.empty());
		testRendering(ENCODED_CURRENCY_SYMBOL);
	}

	@Test
	public void testGetVariablesToExposeNoProductDirectoryNoDefaultCurrency() {
		when(productDirectoryConfigService.getMerchProductDirectoryConfigForCurrentBaseSite())
				.thenReturn(Optional.empty());
		when(commerceCommonI18NService.getDefaultCurrency()).thenReturn(null);
		testRendering("");
	}

	private void testRendering(final String expectedCurrency) {
		final MerchandisingCarouselComponentModel carousel = new MerchandisingCarouselComponentModel();
		carousel.setUid(CAROUSEL_ID);
		carousel.setName(COMPONENT_NAME_VALUE);
		final PageContext context = mock(PageContext.class);
		try {
			final Map<String, Object> variablesToExpose = renderer.getVariablesToExpose(context, carousel);
			assertNotNull("Expected variables to expose to not be null", variablesToExpose);
			assertEquals("Expected variablesToExpose to contain component ID", CAROUSEL_ID,
					variablesToExpose.get(COMPONENT_ID));
			assertEquals("Expected variablesToExpose to contain service URL", SERVICE_URL_VALUE,
					variablesToExpose.get(SERVICE_URL));
			assertEquals("Expected variablesToExpose to contain Name", COMPONENT_NAME_VALUE,
					variablesToExpose.get(COMPONENT_NAME));
			assertEquals("Expected variablesToExpose to contain currency", expectedCurrency,
					variablesToExpose.get(CURRENCY));
		} catch (final Exception e) {
			fail(e.getMessage());
		}
	}
}
