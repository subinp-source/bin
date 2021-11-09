/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.servicelayer.interceptor.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ContentSlotForPageRemoveInterceptorTest
{
	@InjectMocks
	private ContentSlotForPageRemoveInterceptor interceptor;

	@Mock
	private RelatedPagePrepareInterceptor relatedPagePrepareInterceptor;

	@Mock
	private ContentSlotForPageModel contentSlotForPage;

	@Mock
	private InterceptorContext interceptorContext;

	@Test
	public void shouldCallRelatedPagePrepareInterceptor() throws InterceptorException
	{
		// WHEN
		interceptor.onRemove(contentSlotForPage, interceptorContext);

		// THEN
		Mockito.verify(relatedPagePrepareInterceptor).onPrepare(contentSlotForPage, interceptorContext);
	}
}
