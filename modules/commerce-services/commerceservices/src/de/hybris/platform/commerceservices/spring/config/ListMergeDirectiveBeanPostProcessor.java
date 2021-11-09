/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.spring.config;




/**
 * Processes all List Merge Directives in the visible application context. This bean is a bean post processor to ensure
 * it is initialised by the container prior to bean
 *
 * @deprecated since 6.0, use {@link de.hybris.platform.spring.config.ListMergeDirectiveBeanPostProcessor} instead
 */
@Deprecated(since = "6.0", forRemoval = true)
public class ListMergeDirectiveBeanPostProcessor extends de.hybris.platform.spring.config.ListMergeDirectiveBeanPostProcessor
{
	//deprecated
}
