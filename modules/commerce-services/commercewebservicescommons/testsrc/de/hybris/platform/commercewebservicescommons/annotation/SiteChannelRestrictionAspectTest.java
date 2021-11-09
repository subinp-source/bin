/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.annotation;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;


@UnitTest
public class SiteChannelRestrictionAspectTest
{
	private static final String ALLOWED_SITE_CHANNELS_PROPERTY = "api.compatibility.b2c.channels";
	private static final String[] ALLOWED_SITE_CHANNELS = { "B2C" };
	private static final String NOT_VALID_PROPERTY = "not.valid.property";

	@Mock
	private ProceedingJoinPoint proceedingJoinPoint;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private MethodSignature signature;

	@Mock
	private BaseSiteService baseSiteService;

	private SiteChannelRestrictionAspect siteChannelRestrictionAspect;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		siteChannelRestrictionAspect = new SiteChannelRestrictionAspect(baseSiteService, configurationService);

		given(proceedingJoinPoint.getSignature()).willReturn(signature);

		final Configuration configuration = mock(Configuration.class);
		given(configuration.getStringArray(eq(ALLOWED_SITE_CHANNELS_PROPERTY))).willReturn(ALLOWED_SITE_CHANNELS);
		given(configuration.getStringArray(eq(NOT_VALID_PROPERTY))).willReturn(ArrayUtils.EMPTY_STRING_ARRAY);
		given(configuration.getStringArray(eq(StringUtils.EMPTY))).willReturn(ArrayUtils.EMPTY_STRING_ARRAY);

		given(configurationService.getConfiguration()).willReturn(configuration);
	}

	@Test
	public void testMethodWithValidAnnotation() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(SiteChannel.B2C);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithValidChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test
	public void testMethodWithoutAnnotation() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(SiteChannel.B2C);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithoutAnnotation"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test(expected = AccessDeniedException.class)
	public void testMethodWithValidAnnotationAndNoBaseSite() throws Throwable
	{
		given(baseSiteService.getCurrentBaseSite()).willReturn(null);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithValidChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test(expected = AccessDeniedException.class)
	public void testMethodWithValidAnnotationAndNoBaseSiteChannel() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(null);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithValidChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test(expected = AccessDeniedException.class)
	public void testMethodWithValidAnnotationAndNotMatchingBaseSite() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(SiteChannel.B2B);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithValidChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test(expected = AccessDeniedException.class)
	public void testMethodWithEmptyAnnotation() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(SiteChannel.B2C);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod())
				.willReturn(SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithEmptyChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	@Test(expected = AccessDeniedException.class)
	public void testMethodWithNonValidAnnotation() throws Throwable
	{
		final BaseSiteModel currentBaseSite = mock(BaseSiteModel.class);
		given(currentBaseSite.getChannel()).willReturn(SiteChannel.B2C);
		given(baseSiteService.getCurrentBaseSite()).willReturn(currentBaseSite);

		given(signature.getMethod()).willReturn(
				SiteChannelRestrictionAnnotationContainingClass.class.getMethod("mockMethodWithNonValidChannelsProperty"));

		siteChannelRestrictionAspect.validateSiteChannel(proceedingJoinPoint);
	}

	//Mock class for testing
	private class SiteChannelRestrictionAnnotationContainingClass
	{
		@SiteChannelRestriction(allowedSiteChannelsProperty = ALLOWED_SITE_CHANNELS_PROPERTY)
		public void mockMethodWithValidChannelsProperty()
		{
			// Mock method for testing SiteChannelRestriction with filled allowedSiteChannelsProperty
		}

		@SiteChannelRestriction
		public void mockMethodWithEmptyChannelsProperty()
		{
			// Mock method for testing SiteChannelRestriction with no allowedSiteChannelsProperty
		}

		@SiteChannelRestriction(allowedSiteChannelsProperty = NOT_VALID_PROPERTY)
		public void mockMethodWithNonValidChannelsProperty()
		{
			// Mock method for testing SiteChannelRestriction with non-valid allowedSiteChannelsProperty
		}

		public void mockMethodWithoutAnnotation()
		{
			// Mock method for testing Aspect without SiteChannelRestriction, theoretically should never happen
		}
	}
}
