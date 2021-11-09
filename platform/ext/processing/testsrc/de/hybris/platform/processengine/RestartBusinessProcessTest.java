/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.processengine;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.impl.BusinessProcessServiceDao;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@IntegrationTest
public class RestartBusinessProcessTest extends ServicelayerBaseTest
{
	private static final String WAIT_NODE = "waitForever";
	private static final String START_NODE = "start";
	private static final String BEFORE_TEST = "beforeTest";

	private static final long WAIT_TIMEOUT = TimeUnit.SECONDS.toMillis(60);

	@Resource
	private BusinessProcessService businessProcessService;

	@Resource
	private PreWaitAction preWaitAction;

	@Resource
	private PostWaitAction postWaitAction;

	@Resource
	private ModelService modelService;

	@Resource
	private BusinessProcessServiceDao businessProcessServiceDao;

	@After
	public void tearDown() throws Exception
	{
		preWaitAction.reset();
		postWaitAction.reset();
	}

	/**
	 * HORST-1336 Repair business process duplicated TaskCondition problem
	 */
	@Test
	public void restartingProcessStoppedOnWaitNodeShouldNotFail() throws InterruptedException, TimeoutException
	{
		// given
		final String uuid = UUID.randomUUID().toString();
		final BusinessProcessModel processModel = businessProcessService.createProcess(uuid, "repairBusinessProcessDefinition");

		// when
		businessProcessService.startProcess(processModel);
		businessProcessService.triggerEvent(BusinessProcessEvent.newEvent(uuid + "_StartTest"));
		waitForAction(processModel, WAIT_NODE);

		// then preWait action was invoked and we're at wait node
		assertThatActionsWereInvokedNTimes(1, 0);

		// and when
		businessProcessService.restartProcess(processModel, START_NODE);
		waitForAction(processModel, BEFORE_TEST);
		businessProcessService.triggerEvent(BusinessProcessEvent.newEvent(uuid + "_StartTest"));
		waitForAction(processModel, WAIT_NODE);

		// then preWait action was once again invoked and we're at wait node
		assertThatActionsWereInvokedNTimes(2, 0);

		// and when
		businessProcessService.triggerEvent(BusinessProcessEvent.newEvent(uuid + "_SomethingToHappen"));
		waitForState(processModel, ProcessState.SUCCEEDED);

		// then postWait
		assertThatActionsWereInvokedNTimes(2, 1);
	}

	private void assertThatActionsWereInvokedNTimes(final int slowActionInvocations, final int afterWaitActionInvocations)
	{
		assertThat(preWaitAction.getInvocations()).isEqualTo(slowActionInvocations);
		assertThat(postWaitAction.getInvocations()).isEqualTo(afterWaitActionInvocations);
	}

	private void waitForAction(final BusinessProcessModel model, final String action)
			throws InterruptedException, TimeoutException
	{
		waitForCondition(r -> {

			try
			{
				final List<String> actions = businessProcessServiceDao.findBusinessProcessTaskActions(model.getPk());
				return actions.size() == 1 && actions.get(0).equals(action);
			}
			catch (final NoSuchElementException ex)
			{
				return Boolean.FALSE;
			}
		});
	}

	private void waitForState(final BusinessProcessModel model, final ProcessState state)
			throws InterruptedException, TimeoutException
	{
		waitForCondition(r -> {
			modelService.refresh(model);
			return model.getProcessState().equals(state);
		});
	}

	private void waitForCondition(final Function<Long, Boolean> condition) throws TimeoutException, InterruptedException
	{
		final long start = System.currentTimeMillis();
		long round = 1;
		while (!condition.apply(round))
		{
			if (System.currentTimeMillis() - start > WAIT_TIMEOUT)
			{
				throw new TimeoutException();
			}
			round++;
			Thread.sleep(Math.min(100, WAIT_TIMEOUT / 100));
		}
	}

	public static class PreWaitAction extends AbstractProceduralAction<BusinessProcessModel>
	{
		private static final Logger LOG = LoggerFactory.getLogger(PreWaitAction.class);
		private final AtomicLong invocations = new AtomicLong(0);

		@Override
		public void executeAction(final BusinessProcessModel process) throws RetryLaterException, InterruptedException
		{
			Thread.sleep(500);
			final long l = invocations.incrementAndGet();
			LOG.info("PreWaitAction#{}, execute, invocations: {}", this.hashCode(), l);
		}

		public long getInvocations()
		{
			final long l = invocations.get();
			LOG.info("PreWaitAction#{}, getInvocations, invocations: {}", this.hashCode(), l);
			return l;
		}

		public void reset()
		{
			invocations.set(0);
		}
	}

	public static class PostWaitAction extends AbstractProceduralAction<BusinessProcessModel>
	{
		private static final Logger LOG = LoggerFactory.getLogger(PostWaitAction.class);
		private final AtomicLong invocations = new AtomicLong(0);

		@Override
		public void executeAction(final BusinessProcessModel process)
		{
			final long l = invocations.incrementAndGet();
			LOG.info("PostWaitAction#{}, execute, invocations: {}", this.hashCode(), l);
		}

		public long getInvocations()
		{
			final long l = invocations.get();
			LOG.info("PostWaitAction#{}, getInvocations, invocations: {}", this.hashCode(), l);
			return l;
		}

		public void reset()
		{
			invocations.set(0);
		}
	}

}
