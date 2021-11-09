/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cronjob.jalo;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractCronJobEvent;
import de.hybris.platform.servicelayer.event.events.AfterCronJobCrashAbortEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.internal.model.ScriptingJobModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.StrandedItemsRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class AbortingCronJobsIntegrationTest extends ServicelayerBaseTest
{

	private final List<AfterCronJobCrashAbortEvent> events = new ArrayList<>();
	private final AbstractEventListener<AfterCronJobCrashAbortEvent> listener = new AbstractEventListener<>()
	{
		@Override
		protected void onEvent(final AfterCronJobCrashAbortEvent event)
		{
			events.add(event);
		}
	};
	private CronJobManager testCronJobManager;
	private StrandedItemsRegistry strandedCronJobsRegistry;
	private int currentNodeId;
	private int otherNodeId;
	private int staleNodeId;
	private int closedNodeId;
	@Resource
	private ModelService modelService;
	@Resource
	private EventService eventService;

	@Before
	public void setUp() throws Exception
	{
		CronJobManager.getInstance().stopConjobEngine();

		testCronJobManager = new CronJobManager();
		strandedCronJobsRegistry = testCronJobManager.getStrandedCronJobsRegistry();
		currentNodeId = Registry.getClusterID();

		otherNodeId = Registry.getClusterID() + 521;
		staleNodeId = Registry.getClusterID() + 18;
		closedNodeId = Registry.getClusterID() + 184;
	}

	@After
	public void tearDown() throws Exception
	{
		eventService.unregisterEventListener(listener);
		events.clear();

		CronJobManager.getInstance().startupCronjobEngine();
	}


	@Test
	public void shouldAbortCronJobOnRunningOnGivenNode()
	{
		final PK pk = createRunningCronJob(currentNodeId + 1).getPk();

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(currentNodeId + 1), 100);

		assertCronJobAborted(pk);
	}

	@Test
	public void shouldAbortCronJobOnRunningRestartedOnGivenNode()
	{
		final PK pk = createRunningRestartCronJob(currentNodeId + 1).getPk();

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(currentNodeId + 1), 100);

		assertCronJobAborted(pk);
	}

	@Test
	public void shouldAbortCronJobRunningOnlyOnStaleNodesWhenNoActiveNodeIdsIsProvided()
	{
		final PK staleNodeCJPk = createRunningCronJob(staleNodeId).getPk();
		final CronJobModel cjClosedNode = createRunningCronJob(closedNodeId);
		final CronJobModel cjCurrentNode = createRunningCronJob(currentNodeId);
		final CronJobModel cjOtherNode = createRunningCronJob(otherNodeId);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjClosedNode, cjCurrentNode, cjOtherNode);

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(staleNodeId), 100);

		assertCronJobAborted(staleNodeCJPk);
		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldAbortCronJobRunningOnNonExistingNode()
	{
		final PK staleNodeCJPk = createRunningCronJob(staleNodeId).getPk();
		final PK closedNodeCJPk = createRunningCronJob(closedNodeId).getPk();
		final CronJobModel cjCurrentNode = createRunningCronJob(currentNodeId);
		final CronJobModel cjOtherNode = createRunningCronJob(otherNodeId);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjCurrentNode, cjOtherNode);

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(staleNodeId),
				Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobAborted(staleNodeCJPk);
		assertCronJobAborted(closedNodeCJPk);
		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldAbortCronJobRunningOnNonExistingNodeWithoutStaleNodes()
	{
		final CronJobModel cjStaleNode = createRunningCronJob(staleNodeId);
		final PK closedNodeCJPk = createRunningCronJob(closedNodeId).getPk();
		final CronJobModel cjCurrentNode = createRunningCronJob(currentNodeId);
		final CronJobModel cjOtherNode = createRunningCronJob(otherNodeId);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjStaleNode, cjCurrentNode, cjOtherNode);

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(), Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobAborted(closedNodeCJPk);
		assertCronJobsNotChanged(untouchedCronJobs);
	}

	private Map<PK, Long> getPkAndVersion(final CronJobModel... cronJobModels)
	{
		return Stream.of(cronJobModels)
		             .peek(cj -> modelService.detach(cj))
		             .collect(Collectors.toMap(CronJobModel::getPk,
				             cj -> cj.getItemModelContext().getPersistenceVersion()));
	}


	@Test
	public void shouldNotAbortCronJobsInStatusesOtherThanRunning()
	{
		final CronJobModel cjAborted = createNotRunningCronJobWithStatus(CronJobStatus.ABORTED);
		final CronJobModel cjFinished = createNotRunningCronJobWithStatus(CronJobStatus.FINISHED);
		final CronJobModel cjPaused = createNotRunningCronJobWithStatus(CronJobStatus.PAUSED);
		final CronJobModel cjUnknown = createNotRunningCronJobWithStatus(CronJobStatus.UNKNOWN);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjAborted, cjFinished, cjPaused, cjUnknown);


		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(currentNodeId), 100);

		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldNotAbortCronJobsInStatusesOtherThanRunningWhenUsingActiveNodeIds()
	{
		final CronJobModel cjAborted = createNotRunningCronJobWithStatus(CronJobStatus.ABORTED);
		final CronJobModel cjFinished = createNotRunningCronJobWithStatus(CronJobStatus.FINISHED);
		final CronJobModel cjPaused = createNotRunningCronJobWithStatus(CronJobStatus.PAUSED);
		final CronJobModel cjUnknown = createNotRunningCronJobWithStatus(CronJobStatus.UNKNOWN);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjAborted, cjFinished, cjPaused, cjUnknown);


		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(staleNodeId),
				Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldNotAbortCronJobsInStatusesOtherThanRunningWhenUsingOnlyActiveNodeIds()
	{
		final CronJobModel cjAborted = createNotRunningCronJobWithStatus(CronJobStatus.ABORTED);
		final CronJobModel cjFinished = createNotRunningCronJobWithStatus(CronJobStatus.FINISHED);
		final CronJobModel cjPaused = createNotRunningCronJobWithStatus(CronJobStatus.PAUSED);
		final CronJobModel cjUnknown = createNotRunningCronJobWithStatus(CronJobStatus.UNKNOWN);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjAborted, cjFinished, cjPaused, cjUnknown);


		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(), Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldAbortRunningCronJobsMarkedToBeChecked()
	{
		final PK cronJob1PK = createRunningCronJob(currentNodeId).getPk();
		final CronJobModel cronJob2 = createRunningCronJob(currentNodeId);
		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cronJob2);

		strandedCronJobsRegistry.markStrandedItem(cronJob1PK);

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertCronJobAborted(cronJob1PK);
		assertCronJobsNotChanged(untouchedCronJobs);
	}

	@Test
	public void shouldAbortCronJobOnRunningRestartedMarkedToBeChecked()
	{
		final PK cronJob1PK = createRunningRestartCronJob(currentNodeId).getPk();
		final CronJobModel cronJob2 = createRunningRestartCronJob(currentNodeId);
		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cronJob2);

		strandedCronJobsRegistry.markStrandedItem(cronJob1PK);

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertCronJobAborted(cronJob1PK);
		assertCronJobsNotChanged(untouchedCronJobs);
	}


	@Test
	public void shouldCleanInvalidEntries()
	{
		assertThat(strandedCronJobsRegistry.getStrandedItems()).isEmpty();
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(1));
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(2));
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(3));
		assertThat(strandedCronJobsRegistry.getStrandedItems()).hasSize(3);

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertThat(strandedCronJobsRegistry.getStrandedItems()).isEmpty();
	}

	@Test
	public void shouldCleanInvalidEntriesAndAbortValidOnes()
	{
		final PK cronJob1PK = createRunningRestartCronJob(currentNodeId).getPk();
		final PK cronJob2PK = createRunningRestartCronJob(currentNodeId).getPk();

		assertThat(strandedCronJobsRegistry.getStrandedItems()).isEmpty();
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(1));
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(2));
		strandedCronJobsRegistry.markStrandedItem(PK.fromLong(3));
		strandedCronJobsRegistry.markStrandedItem(cronJob1PK);
		strandedCronJobsRegistry.markStrandedItem(cronJob2PK);
		assertThat(strandedCronJobsRegistry.getStrandedItems()).hasSize(5);

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertThat(strandedCronJobsRegistry.getStrandedItems()).isEmpty();

		assertCronJobAborted(cronJob1PK);
		assertCronJobAborted(cronJob2PK);
	}


	@Test
	public void shouldAbortRunningCronJobsMarkedToBeCheckedWithCurrentRunningOnClusterNode()
	{
		final PK cronJob1PK = createRunningCronJob(currentNodeId).getPk();
		final CronJobModel cronJob2 = createRunningCronJob(otherNodeId);
		final CronJobModel cronJob3 = createRunningCronJob(staleNodeId);
		final CronJobModel cronJob4 = createRunningCronJob(closedNodeId);
		final PK cronJob5PK = createRunningRestartCronJob(currentNodeId).getPk();
		final CronJobModel cronJob6 = createRunningRestartCronJob(otherNodeId);
		final CronJobModel cronJob7 = createRunningRestartCronJob(staleNodeId);
		final CronJobModel cronJob8 = createRunningRestartCronJob(closedNodeId);
		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cronJob2, cronJob3, cronJob4, cronJob6, cronJob7, cronJob8);

		strandedCronJobsRegistry.markStrandedItem(cronJob1PK);
		strandedCronJobsRegistry.markStrandedItem(cronJob5PK);

		untouchedCronJobs.keySet().forEach(cronJobPK -> strandedCronJobsRegistry.markStrandedItem(cronJobPK));

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertCronJobAborted(cronJob1PK);
		assertCronJobAborted(cronJob5PK);
		assertCronJobsNotChanged(untouchedCronJobs);
		assertThat(strandedCronJobsRegistry.getStrandedItems()).isEmpty();
	}


	@Test
	public void shouldAbortAllRunningCronJobsIfMaxCronJobsCountIsZero()
	{
		final PK cronJob1Pk = createRunningCronJob(currentNodeId).getPk();
		final PK cronJob2Pk = createRunningCronJob(currentNodeId).getPk();

		strandedCronJobsRegistry.markStrandedItem(cronJob1Pk);
		strandedCronJobsRegistry.markStrandedItem(cronJob2Pk);

		strandedCronJobsRegistry.checkStrandedItems(0);

		assertCronJobAborted(cronJob1Pk);
		assertCronJobAborted(cronJob2Pk);
	}

	@Test
	public void shouldNotAbortCronJobsInStatusesOtherThanRunningWhenAbortingLocalCronJobs()
	{
		final CronJobModel cjAborted = createNotRunningCronJobWithStatus(CronJobStatus.ABORTED);
		final CronJobModel cjFinished = createNotRunningCronJobWithStatus(CronJobStatus.FINISHED);
		final CronJobModel cjPaused = createNotRunningCronJobWithStatus(CronJobStatus.PAUSED);
		final CronJobModel cjUnknown = createNotRunningCronJobWithStatus(CronJobStatus.UNKNOWN);

		final Map<PK, Long> untouchedCronJobs = getPkAndVersion(cjAborted, cjFinished, cjPaused, cjUnknown);
		untouchedCronJobs.keySet().forEach(cronJobPK -> strandedCronJobsRegistry.markStrandedItem(cronJobPK));

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertCronJobsNotChanged(untouchedCronJobs);
	}


	@Test
	public void shouldAbortOnlyAsManyCronJobsAsDefinedInParameter()
	{
		final List<PK> cronJobs = Stream.generate(() -> createRunningCronJob(currentNodeId))
		                                .limit(10).map(AbstractItemModel::getPk)
		                                .collect(Collectors.toList());


		cronJobs.forEach(pk -> {
			modelService.detach(pk);
			strandedCronJobsRegistry.markStrandedItem(pk);
		});

		strandedCronJobsRegistry.checkStrandedItems(7);

		final Map<CronJobStatus, Long> statuses = cronJobs.stream()
		                                                  .map(pk -> (CronJobModel) modelService.get(pk))
		                                                  .collect(Collectors.groupingBy(CronJobModel::getStatus,
				                                                  Collectors.counting()));

		assertThat(statuses).containsEntry(CronJobStatus.RUNNING, 3L)
		                    .containsEntry(CronJobStatus.ABORTED, 7L);
	}

	@Test
	public void shouldPublishAbortAfterCrashEvent()
	{
		eventService.registerEventListener(listener);

		final PK pk = createRunningCronJob(staleNodeId).getPk();

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(staleNodeId), 100);

		assertCronJobAborted(pk);

		assertThat(events).hasSize(1).extracting(AbstractCronJobEvent::getCronJobPK).contains(pk);
	}

	@Test
	public void shouldPublishAbortAfterCrashEventForLocallyCheckedCronJobs()
	{

		eventService.registerEventListener(listener);

		final PK pk = createRunningCronJob(currentNodeId).getPk();
		strandedCronJobsRegistry.markStrandedItem(pk);

		strandedCronJobsRegistry.checkStrandedItems(100);

		assertCronJobAborted(pk);

		assertThat(events).hasSize(1).extracting(AbstractCronJobEvent::getCronJobPK).contains(pk);
	}

	@Test
	public void shouldPublishAbortAfterCrashEventWhenUsingActiveNodeIds()
	{

		eventService.registerEventListener(listener);

		final PK staleCJPk = createRunningCronJob(staleNodeId).getPk();
		final PK closedCJPk = createRunningCronJob(closedNodeId).getPk();

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(staleNodeId),
				Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobAborted(staleCJPk);
		assertCronJobAborted(closedCJPk);

		assertThat(events).hasSize(2)
		                  .extracting(AbstractCronJobEvent::getCronJobPK)
		                  .containsExactlyInAnyOrder(staleCJPk, closedCJPk);
	}

	@Test
	public void shouldPublishAbortAfterCrashEventWhenUsingOnlyActiveNodeIds()
	{

		eventService.registerEventListener(listener);

		final PK closedCJPk = createRunningCronJob(closedNodeId).getPk();

		testCronJobManager.abortRunningCronJobsForClusterNodes(Set.of(), Set.of(staleNodeId, currentNodeId, otherNodeId), 100);

		assertCronJobAborted(closedCJPk);

		assertThat(events).hasSize(1).extracting(AbstractCronJobEvent::getCronJobPK).containsExactlyInAnyOrder(closedCJPk);
	}

	private void assertCronJobsNotChanged(final Map<PK, Long> cjAborted)
	{
		for (final Map.Entry<PK, Long> cronJob : cjAborted.entrySet())
		{
			assertCronJobNotChanged(cronJob.getKey(), cronJob.getValue());
		}
	}

	private void assertCronJobAborted(final PK pk)
	{
		final CronJobModel item = modelService.get(pk);

		assertThat(item).isNotNull();
		assertThat(item.getStatus()).isEqualTo(CronJobStatus.ABORTED);
	}

	private void assertCronJobNotChanged(final PK pk, final long version)
	{
		final CronJobModel item = modelService.get(pk);

		assertThat(item).isNotNull();
		assertThat(item.getItemModelContext().getPersistenceVersion()).isEqualTo(version);
	}


	private CronJobModel createCronJob()
	{

		final ScriptingJobModel job = modelService.create(ScriptingJobModel.class);
		job.setCode(UUID.randomUUID().toString());
		job.setScriptURI("media://someScript");

		final CronJobModel cronJob = modelService.create(CronJobModel.class);
		cronJob.setCode(UUID.randomUUID().toString());
		cronJob.setJob(job);

		modelService.saveAll(job, cronJob);

		return cronJob;
	}

	private CronJobModel createRunningCronJob(final int runningOnClusterNodeId)
	{
		final CronJobModel cronJob = createCronJob();

		cronJob.setStatus(CronJobStatus.RUNNING);
		cronJob.setRunningOnClusterNode(runningOnClusterNodeId);

		modelService.save(cronJob);

		return cronJob;
	}

	private CronJobModel createRunningRestartCronJob(final int runningOnClusterNodeId)
	{
		final CronJobModel cronJob = createCronJob();

		cronJob.setStatus(CronJobStatus.RUNNINGRESTART);
		cronJob.setRunningOnClusterNode(runningOnClusterNodeId);

		modelService.save(cronJob);

		return cronJob;
	}

	private CronJobModel createNotRunningCronJobWithStatus(final CronJobStatus status)
	{
		final CronJobModel cronJob = createCronJob();

		cronJob.setStatus(status);

		modelService.save(cronJob);
		return cronJob;
	}
}
