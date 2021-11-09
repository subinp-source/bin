/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl;

import de.hybris.platform.outboundservices.decorator.OutboundRequestDecorator;

/**
 * Decorates SAP CPI outbound requests with CSRF tokens.
 * @deprecated this class has moved to {@link de.hybris.platform.outboundservices.decorator.impl.csrf.DefaultCsrfOutboundRequestDecorator}
 */
@Deprecated(since = "1905.08-CEP", forRemoval = true)
public class DefaultCsrfOutboundRequestDecorator
		extends de.hybris.platform.outboundservices.decorator.impl.csrf.DefaultCsrfOutboundRequestDecorator
		implements OutboundRequestDecorator
{
}
