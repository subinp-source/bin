/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.hook;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2services.odata.persistence.hook.impl.DefaultPersistHookExecutor;
import de.hybris.platform.odata2services.odata.persistence.hook.impl.PersistenceHookRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPersistHookExecutorUnitTest
{

	@InjectMocks
	private final DefaultPersistHookExecutor executor = new DefaultPersistHookExecutor();
	@Mock
	private PersistenceHookRegistry registry;
	@Mock
	private ItemModel item;
	@Mock
	private PrePersistHook prePersistHook;
	@Mock
	private PostPersistHook postPersistHook;
	private static final String PRE_PERSIST_HOOK_NAME = "BEFORE_SAVE";
	private static final String POST_PERSIST_HOOK_NAME = "AFTER_SAVE";
	private static final String INTEGRATION_KEY = "integratinonKey|Value";

	@Test
	public void testRunPrePersistHookExecutes()
	{
		givenPrePersistHook(PRE_PERSIST_HOOK_NAME);

		executor.runPrePersistHook(PRE_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verify(prePersistHook).execute(item);
	}

	@Test
	public void testPrePersistHooksExceptionIncludesHookNameInMessage()
	{
		final RuntimeException prePersistHookException = new RuntimeException("Expected test exception");
		doThrow(prePersistHookException).when(prePersistHook).execute(item);
		givenPrePersistHook(PRE_PERSIST_HOOK_NAME);

		assertThatThrownBy(() -> executor.runPrePersistHook(PRE_PERSIST_HOOK_NAME, item, INTEGRATION_KEY))
				.isInstanceOf(PrePersistHookException.class)
				.hasMessageContaining(PRE_PERSIST_HOOK_NAME)
				.hasCause(prePersistHookException);
	}

	@Test
	public void testRunPostPersistHook()
	{
		givenPostPersistHook(POST_PERSIST_HOOK_NAME);

		executor.runPostPersistHook(POST_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verify(postPersistHook).execute(item);
	}

	@Test
	public void testExceptionThrownInsidePostPersistHook()
	{
		givenPostPersistHook(POST_PERSIST_HOOK_NAME);
		final RuntimeException postPersistHookException = new RuntimeException("Expected test exception");

		doThrow(postPersistHookException).when(postPersistHook).execute(item);
		givenPostPersistHook(POST_PERSIST_HOOK_NAME);

		assertThatThrownBy(() -> executor.runPostPersistHook(POST_PERSIST_HOOK_NAME, item, INTEGRATION_KEY))
				.isInstanceOf(PostPersistHookException.class)
				.hasMessageContaining(POST_PERSIST_HOOK_NAME)
				.hasCause(postPersistHookException);
	}

	@Test
	public void testIgnoresNullPrePersistHookName()
	{
		givenPostPersistHook(POST_PERSIST_HOOK_NAME);

		executor.runPrePersistHook(PRE_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verifyZeroInteractions(prePersistHook);
	}

	@Test
	public void testIgnoresEmptyPrePersistHookName()
	{
		givenPostPersistHook(POST_PERSIST_HOOK_NAME);

		executor.runPrePersistHook(PRE_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verifyZeroInteractions(prePersistHook);
	}

	@Test
	public void testIgnoresNullPostPersistHookName()
	{
		givenPrePersistHook(PRE_PERSIST_HOOK_NAME);

		executor.runPostPersistHook(POST_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verifyZeroInteractions(prePersistHook);
	}

	@Test
	public void testIgnoresEmptyPostPersistHookName()
	{
		givenPrePersistHook(PRE_PERSIST_HOOK_NAME);

		executor.runPostPersistHook(POST_PERSIST_HOOK_NAME, item, INTEGRATION_KEY);

		verifyZeroInteractions(prePersistHook);
	}

	private void givenPostPersistHook(final String hookName)
	{
		when(registry.getPostPersistHook(hookName, INTEGRATION_KEY)).thenReturn(postPersistHook);
	}

	private void givenPrePersistHook(final String hookName)
	{
		when(registry.getPrePersistHook(hookName, INTEGRATION_KEY)).thenReturn(prePersistHook);
	}
}