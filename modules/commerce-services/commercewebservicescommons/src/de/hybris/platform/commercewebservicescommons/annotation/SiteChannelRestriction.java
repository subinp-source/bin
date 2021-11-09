/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercewebservicescommons.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation for setting allowed site channels for a specific method
 * <p>
 * SiteChannelRestriction is used to define the property which contains the list of allowed site channels and restrict access
 * to methods based on the channel of the current base site. {@code AccessDeniedException} is thrown if the current base
 * site channel is not included in allowed site channels.
 * <p>
 * Leaving {@code allowedSiteChannelsProperty} blank means that the annotated method has no allowed site channels which
 * will cause {@code AccessDeniedException} being thrown from any base site channel.
 * <p>
 * {@code AccessDeniedException} will be also thrown if there's no current base site for annotated methods
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SiteChannelRestriction
{
	/**
	 * Returns the property which contains the list of allowed site channels (e.g. api.compatibility.b2c.channels).
	 *
	 * @return SiteChannel[] The allowed site channels
	 */
	String allowedSiteChannelsProperty() default "";
}
