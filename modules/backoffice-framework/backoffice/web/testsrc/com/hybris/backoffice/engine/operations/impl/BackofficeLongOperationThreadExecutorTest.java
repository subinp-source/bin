/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.engine.operations.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import de.hybris.platform.util.threadpool.PoolableThread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeLongOperationThreadExecutorTest {

    @Spy
    private BackofficeLongOperationThreadExecutor executor;

    @Mock
    private PoolableThread thread;

    @Test
    public void executeShouldUsePoolableThreadAndCallTheMethod()
    {
        //given
        doReturn(thread).when(executor).getPoolableThread();

        //when
        final Runnable runnable = () -> {
            //NOOP
        };
        executor.execute(runnable);

        //then
        verify(thread).execute(runnable);
    }

}