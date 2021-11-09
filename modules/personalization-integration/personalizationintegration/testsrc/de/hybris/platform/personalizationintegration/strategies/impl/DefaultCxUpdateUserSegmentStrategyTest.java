/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.personalizationintegration.strategies.impl;


import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.segment.UserSegmentsProvider;
import de.hybris.platform.personalizationintegration.service.CxIntegrationMappingService;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.configuration.CxConfigurationService;
import de.hybris.platform.personalizationservices.consent.CxConsentService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCxUpdateUserSegmentStrategyTest
{
	private static final String PROVIDER_ID = "provider";
	private static final String ANOTHER_PROVIDER_ID = "anotherProvider";

	private final DefaultCxUpdateUserSegmentStrategy updateUserSegmentStrategy = new DefaultCxUpdateUserSegmentStrategy();
	@Mock
	CxConfigurationService cxConfigurationService;
	@Mock
	Configuration configuration;
	@Mock
	UserSegmentsProvider provider;
	@Mock
	UserSegmentsProvider anotherProvider;
	@Mock
	CxConsentService cxConsentService;
	@Mock
	UserModel user;
	List<SegmentMappingData> segmentMappingList;
	MappingData expectedMappingData;
	@Mock
	private CxIntegrationMappingService cxIntegrationMappingService;
	@Mock
	private ConfigurationService configurationService;
	private List<UserSegmentsProvider> providers;

	@Before
	public void init()
	{
		MockitoAnnotations.initMocks(this);
		expectedMappingData = new MappingData();
		providers = new ArrayList<>();
		providers.add(provider);
		Mockito.when(provider.getProviderId()).thenReturn(PROVIDER_ID);
		Mockito.when(anotherProvider.getProviderId()).thenReturn(ANOTHER_PROVIDER_ID);

		updateUserSegmentStrategy.setProviders(providers);
		updateUserSegmentStrategy.setConfigurationService(configurationService);
		updateUserSegmentStrategy.setCxIntegrationMappingService(cxIntegrationMappingService);
		updateUserSegmentStrategy.setCxConfigurationService(cxConfigurationService);
		updateUserSegmentStrategy.setCxConsentService(cxConsentService);
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(cxConfigurationService.getConfiguration()).thenReturn(Optional.empty());
		Mockito.when(cxConfigurationService.getConfiguration(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(cxConsentService.userHasActiveConsent(user)).thenReturn(Boolean.TRUE);
	}

	@Test
	public void updateUserSegmentTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false),
				Mockito.any(CxCalculationContext.class));
	}

	@Test
	public void updateUserSegmentWithDuplicatedSegmensTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment1", "segment2", "segment1", "segment3", "segment2");
		final List<SegmentMappingData> expectedList = createSegmentMappingList(PROVIDER_ID, "segment1", "segment2", "segment3");
		expectedMappingData.setSegments(expectedList);

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false),
				Mockito.any(CxCalculationContext.class));
	}

	@Test
	public void updateUserSegmentWhenNullDataFromProviderTest()
	{
		//given
		Mockito.when(provider.getUserSegments(user)).thenReturn(null);

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(0)).assignSegmentsToUser(Mockito.eq(user), Mockito.any(),
				Mockito.eq(false));
	}

	@Test
	public void updateUserSegmentWithTwoProvidersTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");

		providers.add(anotherProvider);
		final List<SegmentMappingData> anotherSegmentMappingList = createSegmentMappingList(ANOTHER_PROVIDER_ID, "segment1",
				"segment2");
		Mockito.when(anotherProvider.getUserSegments(user)).thenReturn(anotherSegmentMappingList);
		expectedMappingData.getSegments().addAll(anotherSegmentMappingList);

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false),
				Mockito.any(CxCalculationContext.class));
	}



	@Test
	public void updateUserSegmentForTwoProvidersWithNullSegments()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");
		final CxCalculationContext context = createCalculationContext(ANOTHER_PROVIDER_ID, PROVIDER_ID);

		providers.add(anotherProvider);
		Mockito.when(anotherProvider.getUserSegments(user)).thenReturn(null);

		//when
		updateUserSegmentStrategy.updateUserSegments(user, context);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false), Mockito.eq(context));
		Assert.assertTrue(context.getSegmentUpdateProviders().size() == 1);
		Assert.assertTrue(context.getSegmentUpdateProviders().stream().noneMatch(p -> p.equals(ANOTHER_PROVIDER_ID)));
	}


	@Test
	public void testReturnBiggestAffinity()
	{
		//given
		segmentMappingList = createSegmentMappingList(PROVIDER_ID, "segment1", "segment1");
		segmentMappingList.get(0).setAffinity(BigDecimal.valueOf(0, 5));
		segmentMappingList.get(1).setAffinity(BigDecimal.ONE);
		Mockito.when(provider.getUserSegments(user)).thenReturn(segmentMappingList);
		final List<SegmentMappingData> expectedList = createSegmentMappingList(PROVIDER_ID, "segment1");
		expectedMappingData.setSegments(expectedList);
		final ArgumentCaptor<MappingData> mappingDataArgument = ArgumentCaptor.forClass(MappingData.class);

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				mappingDataArgument.capture(), Mockito.eq(false), Mockito.any(CxCalculationContext.class));
		final MappingData returnedMappingData = mappingDataArgument.getValue();
		Assert.assertNotNull(returnedMappingData);
		Assert.assertNotNull(returnedMappingData.getSegments());
		Assert.assertEquals(1, returnedMappingData.getSegments().size());
		Assert.assertEquals("segment1", returnedMappingData.getSegments().get(0).getCode());
		Assert.assertEquals(BigDecimal.ONE, returnedMappingData.getSegments().get(0).getAffinity());
	}

	@Test
	public void dontUpdateUserSegmentForEmptyProviderListTest()
	{
		//given
		updateUserSegmentStrategy.setProviders(Collections.emptyList());

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verifyZeroInteractions(cxIntegrationMappingService);
	}

	@Test
	public void cleanUserSegmentIfNotGivenConsentTest()
	{
		//given
		Mockito.when(cxConsentService.userHasActiveConsent(user)).thenReturn(Boolean.FALSE);
		expectedMappingData.setSegments(Collections.emptyList());

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false));
	}

	@Test
	public void updateUserSegmentWhenOneProviderThrowExceptionTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");

		providers.add(anotherProvider);
		Mockito.when(anotherProvider.getUserSegments(user)).thenThrow(new RuntimeException("Provider error"));

		//when
		updateUserSegmentStrategy.updateUserSegments(user);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false),
				Mockito.any(CxCalculationContext.class));
	}

	@Test
	public void updateUserSegmentWithNullContextTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");

		//when
		updateUserSegmentStrategy.updateUserSegments(user, null);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false),
				Mockito.any(CxCalculationContext.class));
	}

	@Test
	public void updateUserSegmentWithEmptyContextTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");
		final CxCalculationContext context = createCalculationContext(PROVIDER_ID);

		//when
		updateUserSegmentStrategy.updateUserSegments(user, context);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false), Mockito.eq(context));
	}

	@Test
	public void updateUserSegmentForSelectedProviderTest()
	{
		//given
		configureSegmentMappingForProvider("segment1", "segment2");
		final CxCalculationContext context = createCalculationContext(PROVIDER_ID);

		providers.add(anotherProvider);
		final List<SegmentMappingData> anotherSegmentMappingList = createSegmentMappingList(ANOTHER_PROVIDER_ID, "segment1",
				"segment2");
		Mockito.when(anotherProvider.getUserSegments(user)).thenReturn(anotherSegmentMappingList);

		//when
		updateUserSegmentStrategy.updateUserSegments(user, context);

		//then
		Mockito.verify(cxIntegrationMappingService, Mockito.times(1)).assignSegmentsToUser(Mockito.eq(user),
				Mockito.argThat(new MappingDataMatcher(expectedMappingData)), Mockito.eq(false), Mockito.eq(context));
	}

	@Test
	public void addProviderPrefixForSegmentWhenPropertySetTest()
	{
		//given
		final String providerSegmentPrefixPropertyKey = String.format("personalizationintegration.provider.%s.prefix",
				provider.getProviderId());
		final String prefix = "testPrefix";
		Mockito.when(configurationService.getConfiguration().getString(providerSegmentPrefixPropertyKey)).thenReturn(prefix);
		final CxCalculationContext context = createCalculationContext(ANOTHER_PROVIDER_ID, PROVIDER_ID);

		configureSegmentMappingForProvider("segment1", "segment2");

		//when
		final List<SegmentMappingData> segmentsFromProvider = updateUserSegmentStrategy.getSegmentsFromProvider(user, provider,
				context);

		//then
		for (final SegmentMappingData segment : segmentsFromProvider)
		{
			Assert.assertTrue(segment.getCode().startsWith(prefix));
		}
	}

	@Test
	public void addProviderPrefixForSegmentWhenPropertyAndSeparatorSetTest()
	{
		//given
		final String providerSegmentPrefixPropertyKey = String.format("personalizationintegration.provider.%s.prefix",
				provider.getProviderId());
		final String providerSegmentSeparatorPropertyKey = String.format("personalizationintegration.provider.%s.separator",
				provider.getProviderId());
		final String prefix = "testPrefix";
		final String separator = "testSeparator";
		Mockito.when(configurationService.getConfiguration().getString(providerSegmentPrefixPropertyKey)).thenReturn(prefix);
		Mockito.when(configurationService.getConfiguration().getString(providerSegmentSeparatorPropertyKey)).thenReturn(separator);

		configureSegmentMappingForProvider("segment1", "segment2");

		//when
		final List<SegmentMappingData> segmentsFromProvider = updateUserSegmentStrategy.getSegmentsFromProvider(user, provider,
				null);

		//then
		for (final SegmentMappingData segment : segmentsFromProvider)
		{
			Assert.assertTrue(segment.getCode().startsWith(prefix + separator));
		}
	}

	@Test
	public void dontAddProviderPrefixForSegmentWhenPropertyEmptyTest()
	{
		//given
		final String segmentCode = "segment";

		final String providerSegmentPrefixPropertyKey = String.format("personalizationintegration.provider.%s.prefix",
				provider.getProviderId());
		Mockito.when(configurationService.getConfiguration().getString(providerSegmentPrefixPropertyKey))
				.thenReturn(StringUtils.EMPTY);
		final CxCalculationContext context = createCalculationContext(ANOTHER_PROVIDER_ID, PROVIDER_ID);

		configureSegmentMappingForProvider(segmentCode);

		//when
		final List<SegmentMappingData> segmentsFromProvider = updateUserSegmentStrategy.getSegmentsFromProvider(user, provider,
				context);

		//then
		Assert.assertEquals(1, segmentsFromProvider.size());
		Assert.assertEquals(segmentCode, segmentsFromProvider.get(0).getCode());
	}

	protected void configureSegmentMappingForProvider(final String... segments)
	{
		segmentMappingList = createSegmentMappingList(PROVIDER_ID, segments);
		Mockito.when(provider.getUserSegments(user)).thenReturn(segmentMappingList);

		expectedMappingData.setSegments(new ArrayList<>());
		expectedMappingData.getSegments().addAll(segmentMappingList);
	}

	protected List<SegmentMappingData> createSegmentMappingList(final String providerId, final String... segments)
	{
		final List<SegmentMappingData> segmentMappingList = new ArrayList<>();
		for (final String segment : segments)
		{
			segmentMappingList.add(createSegmentMapping(providerId, segment, BigDecimal.ONE));
		}
		return segmentMappingList;
	}

	protected SegmentMappingData createSegmentMapping(final String providerId, final String segmentCode, final BigDecimal affinity)
	{
		final SegmentMappingData segmentMapping = new SegmentMappingData();
		segmentMapping.setCode(segmentCode);
		segmentMapping.setAffinity(affinity);
		segmentMapping.setProvider(providerId);
		return segmentMapping;
	}

	protected CxCalculationContext createCalculationContext(final String... providers)
	{
		final CxCalculationContext context = new CxCalculationContext();
		context.setSegmentUpdateProviders(Stream.of(providers).collect(Collectors.toSet()));
		return context;
	}

	protected class MappingDataMatcher extends ArgumentMatcher<MappingData>
	{
		MappingData expectedData;

		public MappingDataMatcher(final MappingData expectedData)
		{
			super();
			this.expectedData = expectedData;
		}

		@Override
		public boolean matches(final Object object)
		{
			if (object instanceof MappingData)
			{
				final MappingData mappingData = (MappingData) object;
				if (expectedData.getSegments() == mappingData.getSegments()
						|| (CollectionUtils.isEmpty(mappingData.getSegments()) && CollectionUtils.isEmpty(expectedData.getSegments())))
				{
					return true;
				}

				if (expectedData.getSegments() == null || mappingData.getSegments() == null
						|| expectedData.getSegments().size() != mappingData.getSegments().size())
				{
					return false;
				}
				return expectedData.getSegments().stream()//
						.map(s -> checkIfContains(mappingData, s))//
						.allMatch(contains -> contains == Boolean.TRUE);
			}

			return false;
		}

		private Boolean checkIfContains(final MappingData mappingData, final SegmentMappingData segmentMapping)
		{
			return mappingData.getSegments().stream()//
					.anyMatch(s -> StringUtils.equals(s.getCode(), segmentMapping.getCode())
							&& StringUtils.equals(s.getProvider(), segmentMapping.getProvider()));
		}
	}
}
