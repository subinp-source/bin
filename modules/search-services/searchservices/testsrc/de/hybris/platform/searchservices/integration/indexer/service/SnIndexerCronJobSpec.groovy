/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.indexer.service

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.cronjob.model.JobModel
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao
import de.hybris.platform.searchservices.enums.SnDocumentOperationType
import de.hybris.platform.searchservices.integration.indexer.AbstractSnIndexerSpec
import de.hybris.platform.searchservices.model.FlexibleSearchSnIndexerItemSourceModel
import de.hybris.platform.searchservices.model.FullSnIndexerCronJobModel
import de.hybris.platform.searchservices.model.IncrementalSnIndexerCronJobModel
import de.hybris.platform.searchservices.model.SnIndexTypeModel
import de.hybris.platform.searchservices.model.SnIndexerItemSourceOperationModel
import de.hybris.platform.searchservices.search.data.SnSearchQuery
import de.hybris.platform.searchservices.search.service.SnSearchRequest
import de.hybris.platform.searchservices.search.service.SnSearchResponse
import de.hybris.platform.searchservices.search.service.SnSearchService
import de.hybris.platform.servicelayer.cronjob.CronJobService
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel
import de.hybris.platform.servicelayer.model.ModelService

import java.nio.charset.StandardCharsets

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnIndexerCronJobSpec extends AbstractSnIndexerSpec {

	private static final String FULL_INDEXER_JOB_CODE = "fullSnIndexerJob"
	private static final String INCREMENTAL_INDEXER_JOB_CODE = "incrementalSnIndexerJob"

	private static final String FULL_QUERY = "SELECT {p:pk} FROM {Product AS p}"
	private static final String INCREMENTAL_QUERY = "SELECT {p:pk} FROM {Product AS p} WHERE {p.modifiedtime} > ?startTime"

	private static final long PRODUCTS_COUNT = 6
	private static final long PRODUCTS_CREATED_COUNT = 2

	@Resource
	private ModelService modelService

	@Resource
	private CronJobService cronJobService

	@Resource
	private SnIndexTypeDao snIndexTypeDao

	@Resource
	private SnSearchService snSearchService

	def loadData() {
		loadDefaultData()
	}

	@Test
	public void runFullCronJobOperation() {
		given:
		final FullSnIndexerCronJobModel cronJobModel = createFullIndexerCronJobModel(FULL_QUERY)

		when:
		cronJobService.performCronJob(cronJobModel, true)
		modelService.refresh(cronJobModel)

		final SnSearchQuery searchQuery = new SnSearchQuery()
		final SnSearchRequest searchRequest = snSearchService.createSearchRequest(INDEX_TYPE_ID, searchQuery)
		final SnSearchResponse searchResponse = snSearchService.search(searchRequest)

		then:
		cronJobModel.getStatus() == CronJobStatus.FINISHED
		cronJobModel.getResult() == CronJobResult.SUCCESS

		searchResponse != null
		searchResponse.getSearchResult() != null
		searchResponse.getSearchResult().getTotalSize() == PRODUCTS_COUNT
	}

	@Test
	public void failToRunIncrementalCronJobBeforeFullOperation() {
		given:
		final IncrementalSnIndexerCronJobModel cronJobModel = createIncrementalIndexerCronJobModel(INCREMENTAL_QUERY)

		when:
		cronJobService.performCronJob(cronJobModel, true)
		modelService.refresh(cronJobModel)

		then:
		cronJobModel.getStatus() == CronJobStatus.FINISHED
		cronJobModel.getResult() == CronJobResult.FAILURE
	}

	@Test
	public void runIncrementalCronJobAfterFullOperation() {
		given:
		final FullSnIndexerCronJobModel fullCronJobModel = createFullIndexerCronJobModel(FULL_QUERY)
		final IncrementalSnIndexerCronJobModel incrementalCronJobModel = createIncrementalIndexerCronJobModel(
			INCREMENTAL_QUERY)

		when:
		cronJobService.performCronJob(fullCronJobModel, true)

		importData("/searchservices/test/integration/snCatalogAddProduct1.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogUpdateProduct1.impex", StandardCharsets.UTF_8.name())

		cronJobService.performCronJob(incrementalCronJobModel, true)
		modelService.refresh(incrementalCronJobModel)

		final SnSearchQuery searchQuery = new SnSearchQuery()
		final SnSearchRequest searchRequest = snSearchService.createSearchRequest(INDEX_TYPE_ID, searchQuery)
		final SnSearchResponse searchResponse = snSearchService.search(searchRequest)

		then:
		incrementalCronJobModel.getStatus() == CronJobStatus.FINISHED
		incrementalCronJobModel.getResult() == CronJobResult.SUCCESS

		searchResponse != null
		searchResponse.getSearchResult() != null
		searchResponse.getSearchResult().getTotalSize() == PRODUCTS_COUNT + PRODUCTS_CREATED_COUNT
	}

	@Test
	public void runIncrementalCronJobAfterFullAndIncrementalOperation() {
		given:
		final FullSnIndexerCronJobModel fullCronJobModel = createFullIndexerCronJobModel(FULL_QUERY)
		final IncrementalSnIndexerCronJobModel incrementalCronJobModel = createIncrementalIndexerCronJobModel(
			INCREMENTAL_QUERY)

		when:
		cronJobService.performCronJob(fullCronJobModel, true)

		importData("/searchservices/test/integration/snCatalogAddProduct1.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogUpdateProduct1.impex", StandardCharsets.UTF_8.name())

		cronJobService.performCronJob(incrementalCronJobModel, true)

		importData("/searchservices/test/integration/snCatalogAddProduct2.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snCatalogUpdateProduct2.impex", StandardCharsets.UTF_8.name())

		cronJobService.performCronJob(incrementalCronJobModel, true)
		modelService.refresh(incrementalCronJobModel)

		final SnSearchQuery searchQuery = new SnSearchQuery()
		final SnSearchRequest searchRequest = snSearchService.createSearchRequest(INDEX_TYPE_ID, searchQuery)
		final SnSearchResponse searchResponse = snSearchService.search(searchRequest)

		then:
		incrementalCronJobModel.getStatus() == CronJobStatus.FINISHED
		incrementalCronJobModel.getResult() == CronJobResult.SUCCESS

		searchResponse != null
		searchResponse.getSearchResult() != null
		searchResponse.getSearchResult().getTotalSize() == PRODUCTS_COUNT + PRODUCTS_CREATED_COUNT + PRODUCTS_CREATED_COUNT
	}

	protected JobModel findIndexerJobModel(String code) {
		try {
			return cronJobService.getJob(code)
		}
		catch (final ModelNotFoundException | UnknownIdentifierException e) {
			final ServicelayerJobModel jobModel = modelService.create(ServicelayerJobModel.class)
			jobModel.setCode(code)
			jobModel.setSpringId(code)

			modelService.save(jobModel)

			return jobModel
		}
	}

	protected FullSnIndexerCronJobModel createFullIndexerCronJobModel(final String query) {
		final JobModel jobModel = findIndexerJobModel(FULL_INDEXER_JOB_CODE)
		final SnIndexTypeModel indexTypeModel = snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID).get()

		FlexibleSearchSnIndexerItemSourceModel itemSource = modelService.create(FlexibleSearchSnIndexerItemSourceModel.class)
		itemSource.setQuery(query)

		final FullSnIndexerCronJobModel cronJob = modelService.create(FullSnIndexerCronJobModel.class)
		cronJob.setJob(jobModel)
		cronJob.setIndexType(indexTypeModel)
		cronJob.setIndexerItemSource(itemSource)

		modelService.save(cronJob)

		return cronJob
	}

	protected IncrementalSnIndexerCronJobModel createIncrementalIndexerCronJobModel(final String query) {
		final JobModel jobModel = findIndexerJobModel(INCREMENTAL_INDEXER_JOB_CODE)
		final SnIndexTypeModel indexTypeModel = snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID).get()

		FlexibleSearchSnIndexerItemSourceModel itemSource = modelService.create(FlexibleSearchSnIndexerItemSourceModel.class)
		itemSource.setQuery(query)

		SnIndexerItemSourceOperationModel itemSourceOperation = modelService.create(SnIndexerItemSourceOperationModel.class)
		itemSourceOperation.setDocumentOperationType(SnDocumentOperationType.CREATE_UPDATE)
		itemSourceOperation.setIndexerItemSource(itemSource)

		List<SnIndexerItemSourceOperationModel> itemSourceOperations = List.of(itemSourceOperation)

		final IncrementalSnIndexerCronJobModel cronJobModel = modelService.create(IncrementalSnIndexerCronJobModel.class)
		cronJobModel.setJob(jobModel)
		cronJobModel.setIndexType(indexTypeModel)
		cronJobModel.setIndexerItemSourceOperations(itemSourceOperations)

		modelService.save(cronJobModel)

		return cronJobModel
	}
}
