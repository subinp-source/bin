/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.impl;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.workflow.WorkflowActionService;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hybris.backoffice.workflow.WorkflowSearchData;
import com.hybris.cockpitng.testing.util.BeanLookup;
import com.hybris.cockpitng.testing.util.BeanLookupFactory;
import com.hybris.cockpitng.testing.util.CockpitTestUtil;


@IntegrationTest
public class DefaultWorkflowFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultWorkflowFacadeIntegrationTest.class);
	private static final String DATE_FORMAT = "%s %d:%d:%d";
	private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final Date TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE = toDate("2019-01-02 12:00:00");
	private static final Date TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE = toDate("2019-01-04 12:00:00");
	private static final String FIRST_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT = toDatabaseDateFormat("2019-01-01 12:00:00");
	private static final String SECOND_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT = toDatabaseDateFormat("2019-01-03 12:00:00");
	private static final String THIRD_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT = toDatabaseDateFormat("2019-01-05 12:00:00");
	private static final String USER_ID = "userName";
	private static final String TENANT_ID = "junit";
	private static final int TOTAL_WORKFLOWS_COUNT = 18;
	private static final int PLANNED_WORKFLOWS_COUNT = 6;
	private static final int RUNNING_WORKFLOWS_COUNT = 6;
	private static final int TERMINATED_WORKFLOWS_COUNT = 6;
	private static final int PAGE_SIZE = 50;

	private DefaultWorkflowFacade testSubject;

	@Resource
	private BackofficeWorkflowService backofficeWorkflowService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	@Resource
	private UserService userService;

	@Resource
	private WorkflowActionService workflowActionService;

	@Resource
	private WorkflowTemplateService workflowTemplateService;

	@Resource
	private WorkflowAttachmentService workflowAttachmentService;

	@Resource
	private WorkflowProcessingService workflowProcessingService;

	private List<WorkflowModel> allWorkflows;
	private List<WorkflowModel> preparedWorkflows;
	private List<WorkflowModel> runningWorkflows;
	private List<WorkflowModel> terminatedWorkflows;

	private static Date toDate(final String value)
	{
		final LocalDateTime localDateTime = getLocalDateTimeFromStringUsingPattern(value, LOCAL_DATE_TIME_FORMAT);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private static String toDatabaseDateFormat(final String value)
	{
		final LocalDateTime localDateTime = getLocalDateTimeFromStringUsingPattern(value, LOCAL_DATE_TIME_FORMAT);
		return String.format(DATE_FORMAT, localDateTime.toLocalDate().toString(), localDateTime.getHour(),
				localDateTime.getMinute(), localDateTime.getSecond());
	}

	private static LocalDateTime getLocalDateTimeFromStringUsingPattern(final String value, final String pattern)
	{
		return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
	}

	@Before
	public void setUp() throws Exception
	{
		testSubject = new DefaultWorkflowFacade();
		testSubject.setUserService(userService);
		testSubject.setWorkflowActionService(workflowActionService);
		testSubject.setWorkflowTemplateService(workflowTemplateService);
		testSubject.setWorkflowService(backofficeWorkflowService);
		testSubject.setWorkflowAttachmentService(workflowAttachmentService);
		testSubject.setWorkflowProcessingService(workflowProcessingService);

		createCoreData();
		createDefaultCatalog();
		importData("/impex/defaultWorkflowFacadeIntegrationTestData.impex", StandardCharsets.UTF_8.name());

		allWorkflows = fetchWorkflowsData().getResult();
		runningWorkflows = filterWorkflowsOfGivenStatus(allWorkflows, WorkflowStatus.RUNNING);
		terminatedWorkflows = filterWorkflowsOfGivenStatus(allWorkflows, WorkflowStatus.TERMINATED);
		preparedWorkflows = filterWorkflowsOfGivenStatus(allWorkflows, WorkflowStatus.PLANNED);

		updateWorkflowsWithGivenModifiedTimestamp(preparedWorkflows, FIRST_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);
		updateWorkflowsWithGivenModifiedTimestamp(runningWorkflows, SECOND_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);
		updateWorkflowsWithGivenModifiedTimestamp(terminatedWorkflows, THIRD_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);

		userService.setCurrentUser(userService.getUserForUID(USER_ID));
	}

	@Test
	public void shouldReturnAllWorkflowsWhenDateFromAndDateToAreBothNotSet()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults()).isNotEmpty();
		assertThat(result.getAllResults().size()).isEqualTo(TOTAL_WORKFLOWS_COUNT);
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(allWorkflows));
	}

	@Test
	public void shouldReturnOnlyPreparedWorkflowsWhenDateFromAndDateToAreBothNotSetAndOnlyPlannedStatusIsSelected()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.PLANNED));
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(PLANNED_WORKFLOWS_COUNT);
		assertThat(areAllWorkflowsOfGivenStatus(result.getAllResults(), WorkflowStatus.PLANNED)).isTrue();
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(preparedWorkflows));
	}

	@Test
	public void shouldReturnOnlyRunningWorkflowsWhenDateFromAndDateToAreBothNotSetAndOnlyRunningStatusIsSelected()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.RUNNING));
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(RUNNING_WORKFLOWS_COUNT);
		assertThat(areAllWorkflowsOfGivenStatus(result.getAllResults(), WorkflowStatus.RUNNING)).isTrue();
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(runningWorkflows));
	}

	@Test
	public void shouldReturnOnlyTerminatedWorkflowsWhenDateFromAndDateToAreBothNotSetAndOnlyTerminatedStatusIsSelected()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.TERMINATED));
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(TERMINATED_WORKFLOWS_COUNT);
		assertThat(areAllWorkflowsOfGivenStatus(result.getAllResults(), WorkflowStatus.TERMINATED)).isTrue();
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(terminatedWorkflows));
	}

	@Test
	public void shouldReturnOnlySubsetOfUpdatedTerminatedWorkflowsWhenDateFromAndDateToAreBothSetAndOnlyTerminatedStatusIsSelected()
	{
		// given
		final SearchResult searchResult = fetchWorkflowsData();

		final List<WorkflowModel> allTerminatedWorkflows = filterWorkflowsOfGivenStatus(searchResult.getResult(),
				WorkflowStatus.TERMINATED);
		final List<WorkflowModel> firstTerminatedWorkflowsSubset = allTerminatedWorkflows.subList(0, 2);
		final List<WorkflowModel> secondTerminatedWorkflowsSubset = allTerminatedWorkflows.subList(2, 4);
		final List<WorkflowModel> thirdTerminatedWorkflowsSubset = allTerminatedWorkflows.subList(4, 6);

		updateWorkflowsWithGivenModifiedTimestamp(firstTerminatedWorkflowsSubset, FIRST_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);
		updateWorkflowsWithGivenModifiedTimestamp(secondTerminatedWorkflowsSubset, SECOND_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);
		updateWorkflowsWithGivenModifiedTimestamp(thirdTerminatedWorkflowsSubset, THIRD_UPDATE_TIMESTAMP_IN_DATABASE_FORMAT);

		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.TERMINATED));
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		workflowSearchData.setDateTo(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(2);
		assertThat(areAllWorkflowsOfGivenStatus(result.getAllResults(), WorkflowStatus.TERMINATED)).isTrue();
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(secondTerminatedWorkflowsSubset));
	}

	@Test
	public void shouldNotReturnAnyWorkflowWhenDateFromAndDateToAreBothNullAndNoStatusIsSelected()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.noneOf(WorkflowStatus.class));
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults()).isEmpty();
	}

	@Test
	public void shouldReturnOnlyWorkflowsModifiedFromDateSetWhenOnlyDateFromIsSet()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(TOTAL_WORKFLOWS_COUNT - PLANNED_WORKFLOWS_COUNT);

		final List<WorkflowModel> runningAndTerminatedWorkflows = new ArrayList<>(runningWorkflows);
		runningAndTerminatedWorkflows.addAll(terminatedWorkflows);

		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(runningAndTerminatedWorkflows));
	}

	@Test
	public void shouldReturnOnlyWorkflowsModifiedToDateSetWhenOnlyDateToIsSet()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(PLANNED_WORKFLOWS_COUNT);
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(preparedWorkflows));
	}

	@Test
	public void shouldNotReturnAnyWorkflowWhenDateFromIsBiggerThanDateTo()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		workflowSearchData.setDateTo(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults()).isEmpty();
	}

	@Test
	public void shouldReturnOnlyWorkflowsFromSelectedRangeWhenBothDatesAreSet()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		workflowSearchData.setDateTo(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(RUNNING_WORKFLOWS_COUNT);
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(runningWorkflows));
	}

	@Test
	public void shouldReturnPagedWorkflowsModifiedFromDateSetWhenOnlyDateFromIsSet()
	{
		// given
		final int workflowSearchDataPageSize = 5;
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(workflowSearchDataPageSize, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_FIRST_AND_BEFORE_SECOND_UPDATE);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		final int expectedWorkflowsCount = TOTAL_WORKFLOWS_COUNT - PLANNED_WORKFLOWS_COUNT;
		assertThat(result.getAllResults().size()).isEqualTo(expectedWorkflowsCount);

		final int expectedPagesCount = expectedWorkflowsCount / workflowSearchDataPageSize
				+ (expectedWorkflowsCount % workflowSearchDataPageSize == 0 ? 0 : 1);

		int pagesCount = 0;
		boolean hasCheckedAllPages = false;
		while (!hasCheckedAllPages)
		{
			pagesCount++;

			if (result.hasNextPage())
			{
				result.nextPage();
			}
			else
			{
				hasCheckedAllPages = true;
			}
		}

		assertThat(pagesCount).isEqualTo(expectedPagesCount);

		final List<WorkflowModel> runningAndTerminatedWorkflows = new ArrayList<>(runningWorkflows);
		runningAndTerminatedWorkflows.addAll(terminatedWorkflows);

		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(runningAndTerminatedWorkflows));
	}

	@Test
	public void shouldNotReturnAnyRunningWorkflowWhenDateFilterIsSetToExcludeThemAndStatusSelectionIsNotSet()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, WorkflowStatus.getAll());
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(isAnyWorkflowOfGivenStatus(result.getAllResults(), WorkflowStatus.RUNNING)).isFalse();
		assertThat(getPKsOf(result.getAllResults())).doesNotContainAnyElementsOf(getPKsOf(runningWorkflows));
	}

	@Test
	public void shouldNotReturnAnyWorkflowWhenDateFilterIsSetToExcludeRunningWorkflowsAndStatusSelectionIsSetToRunning()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.RUNNING));
		workflowSearchData.setDateFrom(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		workflowSearchData.setDateTo(null);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults()).isEmpty();
	}

	@Test
	public void shouldReturnOnlyRunningWorkflowsWhenDateFilterIsSetToIncludeThemAndStatusSelectionIsSetToRunning()
	{
		// given
		final WorkflowSearchData workflowSearchData = new WorkflowSearchData(PAGE_SIZE, EnumSet.of(WorkflowStatus.RUNNING));
		workflowSearchData.setDateFrom(null);
		workflowSearchData.setDateTo(TIMESTAMP_AFTER_SECOND_AND_BEFORE_THIRD_UPDATE);
		instantiateWorkflowSearchPageable(workflowSearchData);

		// when
		final WorkflowSearchPageable result = (WorkflowSearchPageable) testSubject.getWorkflows(workflowSearchData);

		// then
		assertThat(result.getAllResults().size()).isEqualTo(RUNNING_WORKFLOWS_COUNT);
		assertThat(areAllWorkflowsOfGivenStatus(result.getAllResults(), WorkflowStatus.RUNNING)).isTrue();
		assertThat(getPKsOf(result.getAllResults())).containsOnlyElementsOf(getPKsOf(runningWorkflows));
	}

	private void instantiateWorkflowSearchPageable(final WorkflowSearchData workflowSearchData)
	{
		final WorkflowSearchPageable workflowSearchPageable = new WorkflowSearchPageable(workflowSearchData);
		workflowSearchPageable.setWorkflowService(backofficeWorkflowService);
		final BeanLookup beanLookup = BeanLookupFactory.createBeanLookup()
				.registerBean("workflowSearchPageable", workflowSearchPageable).getLookup();
		CockpitTestUtil.mockBeanLookup(beanLookup);
	}

	private SearchResult<WorkflowModel> fetchWorkflowsData()
	{
		return flexibleSearchService
				.search("SELECT { " + WorkflowModel.PK + "}, {modifiedtime} FROM {" + WorkflowModel._TYPECODE + "}");
	}

	private List<WorkflowModel> filterWorkflowsOfGivenStatus(final List<WorkflowModel> workflows,
			final WorkflowStatus workflowStatus)
	{
		return workflows.stream().filter(workflow -> testSubject.getWorkflowStatus(workflow).equals(workflowStatus))
				.collect(Collectors.toList());
	}

	private void updateWorkflowsWithGivenModifiedTimestamp(final List<WorkflowModel> workflows, final String newModifiedTimestamp)
	{
		final String tablePrefix = Config.getParameter("db.tableprefix");
		try (final Connection connection = Registry.getTenantByID(TENANT_ID).getDataSource().getConnection())
		{
			try (final PreparedStatement statement = connection.prepareStatement(
					"UPDATE " + tablePrefix + "cronjobs SET MODIFIEDTS = ? WHERE PK = ?", ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE))
			{
				final List<String> workflowsPKs = getPKsOf(workflows);
				workflowsPKs.forEach(pk -> {
					try
					{
						statement.setString(1, newModifiedTimestamp);
						statement.setString(2, pk);
						statement.execute();
					}
					catch (final SQLException e)
					{
						LOG.error(e.getMessage(), e);
					}
				});
			}
		}
		catch (final SQLException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	private List<String> getPKsOf(final List<WorkflowModel> workflows)
	{
		return workflows.stream().map(WorkflowModel::getPk).map(PK::toString).collect(Collectors.toList());
	}

	private boolean areAllWorkflowsOfGivenStatus(final List<WorkflowModel> workflows, final WorkflowStatus workflowStatus)
	{
		return workflows.stream().allMatch(workflow -> testSubject.getWorkflowStatus(workflow).equals(workflowStatus));
	}

	private boolean isAnyWorkflowOfGivenStatus(final List<WorkflowModel> workflows, final WorkflowStatus workflowStatus)
	{
		return workflows.stream().anyMatch(workflow -> testSubject.getWorkflowStatus(workflow).equals(workflowStatus));
	}

}
