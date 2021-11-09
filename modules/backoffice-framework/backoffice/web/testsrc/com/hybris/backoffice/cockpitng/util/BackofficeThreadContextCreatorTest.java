/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.MediaUtil;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Optional;
import java.util.concurrent.Semaphore;

import javax.servlet.ServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.zkoss.zk.ui.Executions;

import com.hybris.backoffice.user.BackofficeRoleService;
import com.hybris.cockpitng.modules.core.impl.CockpitModuleComponentDefinitionService;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@IntegrationTest
public class BackofficeThreadContextCreatorTest extends ServicelayerTransactionalTest
{

	private static final String BACKOFFICE_ROLE = "backofficeRole";
	private static final String ATTRIBUTE_NAME = "attribute";
	private static final Object ATTRIBUTE_VALUE = new Object();

	@Spy
	@InjectMocks
	private BackofficeThreadContextCreator creator;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private UserService userService;

	@Mock
	private I18NService i18nService;

	@Mock
	private CockpitModuleComponentDefinitionService componentDefinitionService;

	@Mock
	private BackofficeRoleService backofficeRoleService;

	@Mock
	private BackofficeThreadRequestAttributes attributes;


	@Before
	public void setUp()
	{
		initMocks(this);
		CockpitTestUtil.mockZkEnvironment();
		when(backofficeRoleService.getActiveRole()).thenReturn(Optional.of(BACKOFFICE_ROLE));
		creator.setAttributes(Collections.singletonList(attributes));

		final Enumeration<String> attributeNames = Collections.enumeration(Collections.singleton(ATTRIBUTE_NAME));
		final ServletRequest request = (ServletRequest) Executions.getCurrent().getNativeRequest();
		when(request.getAttributeNames()).thenReturn(attributeNames);
		when(request.getAttribute(ATTRIBUTE_NAME)).thenReturn(ATTRIBUTE_VALUE);
		when(attributes.getAttributeNames(any())).thenReturn(attributeNames);
	}

	@Test
	public void shouldCopyThreadLocalMediaRenderersFromOriginalThread() throws InterruptedException
	{
		// given
		final Semaphore executorLock = new Semaphore(0);
		final Semaphore threadLocalLock = new Semaphore(0);

		final MediaUtil.PublicMediaURLRenderer publicRendererOriginal = mock(MediaUtil.PublicMediaURLRenderer.class);
		final MediaUtil.SecureMediaURLRenderer secureRendererOriginal = mock(MediaUtil.SecureMediaURLRenderer.class);

		MediaUtil.setCurrentPublicMediaURLRenderer(publicRendererOriginal);
		MediaUtil.setCurrentSecureMediaURLRenderer(secureRendererOriginal);
		MediaUtil.setCurrentRequestSSLModeEnabled(true);

		// when
		creator.execute(() -> {
			try
			{
				threadLocalLock.acquire();

				// then
				final MediaUtil.PublicMediaURLRenderer publicRenderer = MediaUtil.getCurrentPublicMediaURLRenderer();
				final MediaUtil.SecureMediaURLRenderer secureRenderer = MediaUtil.getCurrentSecureMediaURLRenderer();
				final boolean sslEnabled = MediaUtil.isCurrentRequestSSLModeEnabled();

				assertThat(sslEnabled).isTrue();
				assertThat(publicRenderer).isSameAs(publicRendererOriginal);
				assertThat(secureRenderer).isSameAs(secureRendererOriginal);
			}
			catch (final InterruptedException e)
			{
				throw new IllegalStateException(e);
			}
			finally
			{
				executorLock.release();
			}
		});
		MediaUtil.unsetCurrentPublicMediaURLRenderer();
		MediaUtil.unsetCurrentSecureMediaURLRenderer();
		MediaUtil.setCurrentRequestSSLModeEnabled(false);
		threadLocalLock.release();
		executorLock.acquire();
	}

	@Test
	public void shouldCopyBackofficeRoleFromOriginalThread() throws InterruptedException
	{
		// given
		final Semaphore executorLock = new Semaphore(0);
		final Semaphore threadLocalLock = new Semaphore(0);

		// when
		creator.execute(() -> {
			try
			{
				threadLocalLock.acquire();

				// then
				final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
				verify(backofficeRoleService).setActiveRole(captor.capture());
				assertThat(captor.getValue()).isEqualTo(BACKOFFICE_ROLE);
			}
			catch (final InterruptedException e)
			{
				throw new IllegalStateException(e);
			}
			finally
			{
				executorLock.release();
			}
		});
		threadLocalLock.release();
		executorLock.acquire();
	}

	@Test
	public void shouldCopyRequiredAttributesFromOriginalRequest() throws InterruptedException
	{
		// given
		final Semaphore executorLock = new Semaphore(0);
		final Semaphore threadLocalLock = new Semaphore(0);
		final Object request = Executions.getCurrent().getNativeRequest();

		// when
		creator.execute(() -> {
			try
			{
				threadLocalLock.acquire();

				// then
				final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
				assertThat(requestAttributes).isNotNull();

				final Object currentRequest = requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
				assertThat(currentRequest).isNotSameAs(request);

				final Object attributeValue = requestAttributes.getAttribute(ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
				assertThat(attributeValue).isSameAs(ATTRIBUTE_VALUE);
			}
			catch (final InterruptedException e)
			{
				throw new IllegalStateException(e);
			}
			finally
			{
				executorLock.release();
			}
		});
		threadLocalLock.release();
		executorLock.acquire();
	}
}
