/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.annotation;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.site.BaseSiteService;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;


/**
 * Aspect implementation for restricting an annotated OCC endpoint based on site channel
 */
public class SiteChannelRestrictionAspect
{
	private BaseSiteService baseSiteService;
	private ConfigurationService configurationService;

	public SiteChannelRestrictionAspect(final BaseSiteService baseSiteService, final ConfigurationService configurationService)
	{
		this.baseSiteService = baseSiteService;
		this.configurationService = configurationService;
	}

	/**
	 * Logic of the pointcut for restricting an OCC endpoint annotated with {@code SiteChannelRestriction} based on site channel,
	 * throws {@code AccessDeniedException} if the channel of the current base site is not included in allowed site channels
	 *
	 * @param proceedingJoinPoint
	 * 		is the join point which matches the defined pointcut
	 * @return target method invocation of {@param proceedingJoinPoint}
	 * @throws Throwable
	 */
	public Object validateSiteChannel(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable
	{
		final BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();
		final Method method = getMethod(proceedingJoinPoint);

		if (!isBaseSiteAllowed(currentBaseSite, method))
		{
			throw new AccessDeniedException("It's not allowed to execute this call from the current channel");
		}

		return proceedingJoinPoint.proceed();
	}

	/**
	 * Returns the intercepted method annotated with {@code SiteChannelRestriction}
	 *
	 * @param proceedingJoinPoint
	 * 		is the intercepted joint point based on {@code SiteChannelRestriction} annotation
	 * @return method annotated with {@code SiteChannelRestriction}
	 */
	protected Method getMethod(final ProceedingJoinPoint proceedingJoinPoint)
	{
		final MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		return signature.getMethod();
	}

	/**
	 * Checks if the site channel of a base site is an allowed site channel
	 *
	 * @param baseSite
	 * 		is the given base site
	 * @return true if the given base site's channel is in allowed site channels, false otherwise
	 */
	protected boolean isBaseSiteAllowed(final BaseSiteModel baseSite, final Method method)
	{
		return Optional.ofNullable(baseSite).map(BaseSiteModel::getChannel)
				.map(siteChannel -> isSiteChannelAllowed(siteChannel, method)).orElse(false);
	}

	/**
	 * Checks if the given site channel is an allowed site channel
	 *
	 * @param siteChannel
	 * 		is the given site channel
	 * @param method
	 * 		is the executed method
	 * @return true if the given site channel is in allowed site channels, false otherwise
	 */
	protected boolean isSiteChannelAllowed(final SiteChannel siteChannel, final Method method)
	{
		String[] allowedSiteChannels = getMethodAllowedSiteChannelsProperty(method);
		return Arrays.stream(allowedSiteChannels)
				.anyMatch(allowedSiteChannel -> StringUtils.equals(allowedSiteChannel, siteChannel.getCode()));
	}

	/**
	 * Gets method allowed site channels value based on {@code SiteChannelRestriction} annotation. Allowed site
	 * channels are read from properties files (project.properties, local.properties).
	 * <p>
	 * If there is no annotation {@code SiteChannelRestriction} defined for the method, all site channels are returned
	 * <p>
	 * If there is no {@code allowedSiteChannelsProperty} or no value for property with given name, an empty array will be returned
	 */
	protected String[] getMethodAllowedSiteChannelsProperty(final Method method)
	{
		final SiteChannelRestriction siteChannelRestriction = AnnotationUtils.findAnnotation(method, SiteChannelRestriction.class);
		if (siteChannelRestriction != null)
		{
			String allowedSiteChannelsProperty = siteChannelRestriction.allowedSiteChannelsProperty();
			return Optional.ofNullable(getConfigurationService().getConfiguration().getStringArray(allowedSiteChannelsProperty))
					.orElse(ArrayUtils.EMPTY_STRING_ARRAY);
		}
		return Arrays.stream(SiteChannel.values()).map(SiteChannel::getCode).toArray(String[]::new);
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
