/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.integration.indexer.dao

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.searchservices.admin.dao.SnIndexTypeDao
import de.hybris.platform.searchservices.indexer.dao.SnIndexerCronJobDao
import de.hybris.platform.searchservices.integration.indexer.AbstractSnIndexerSpec
import de.hybris.platform.searchservices.model.SnIndexTypeModel

import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId

import javax.annotation.Resource

import org.junit.Test


@IntegrationTest
public class SnIndexerCronjobDaoSpec extends AbstractSnIndexerSpec {

	@Resource
	SnIndexTypeDao snIndexTypeDao

	@Resource
	SnIndexerCronJobDao snIndexerCronJobDao

	SnIndexTypeModel indexTypeModel
	SnIndexTypeModel indexTypeModel1
	SnIndexTypeModel indexTypeModel2

	def setup() {
		createTestData()
		loadData()

		indexTypeModel = snIndexTypeDao.findIndexTypeById(INDEX_TYPE_ID).orElseThrow()
		indexTypeModel1 = snIndexTypeDao.findIndexTypeById(INDEX_TYPE_1_ID).orElseThrow()
		indexTypeModel2 = snIndexTypeDao.findIndexTypeById(INDEX_TYPE_2_ID).orElseThrow()
	}

	def cleanup() {
		deleteTestData()
	}

	def loadData() {
		importData("/searchservices/test/integration/snIndexConfiguration.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfiguration1.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexConfiguration2.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexType.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexType1.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/snIndexType2.impex", StandardCharsets.UTF_8.name())
		importData("/searchservices/test/integration/indexer/dao/snCronJob.impex", StandardCharsets.UTF_8.name())
	}

	@Test
	public void testMaxFullLastSuccessfulStartTimeNonExisting() {
		when:
		Optional<Date> result = snIndexerCronJobDao.getMaxFullLastSuccessfulStartTime(indexTypeModel)

		then:
		result.isEmpty()
	}

	@Test
	public void testMaxFullLastSuccessfulStartTimeNoStartTime() {
		when:
		Optional<Date> result = snIndexerCronJobDao.getMaxFullLastSuccessfulStartTime(indexTypeModel2)

		then:
		result.isEmpty()
	}

	@Test
	public void testMaxFullLastSuccessfulStartTime() {
		when:
		Optional<Date> result = snIndexerCronJobDao.getMaxFullLastSuccessfulStartTime(indexTypeModel1)

		then:
		result.isPresent()
		result.get() == Date.from(LocalDateTime.of(2019, 1, 1, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant())
	}
}
