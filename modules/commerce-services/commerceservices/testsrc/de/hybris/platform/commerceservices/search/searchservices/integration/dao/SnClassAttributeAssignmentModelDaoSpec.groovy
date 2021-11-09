/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.integration.dao

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel
import de.hybris.platform.catalog.model.classification.ClassificationClassModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.classification.ClassificationSystemService
import de.hybris.platform.commerceservices.search.searchservices.dao.SnClassAttributeAssignmentModelDao
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test

import javax.annotation.Resource
import java.nio.charset.StandardCharsets

@IntegrationTest
public class SnClassAttributeAssignmentModelDaoSpec extends ServicelayerSpockSpecification {

	static final String CLASSIFICATION_SYSTEM = "SnClassification"
	static final String CLASSIFICATION_SYSTEM_VERSION = "1.0"

	@Resource
	ClassificationSystemService classificationSystemService

	@Resource
	SnClassAttributeAssignmentModelDao snClassAttributeAssignmentModelDao

	@Test
	def "Get non existing class attribute assignment"() {
		given:
		importData("/commerceservices/test/integration/snClassificationSystem.impex", StandardCharsets.UTF_8.name())
		importData("/commerceservices/test/integration/snClassificationSystemAddAttribute.impex", StandardCharsets.UTF_8.name())

		ClassificationSystemVersionModel classificationSystemVersion = classificationSystemService.getSystemVersion(CLASSIFICATION_SYSTEM, CLASSIFICATION_SYSTEM_VERSION)
		ClassificationClassModel classificationClass = classificationSystemService.getClassForCode(classificationSystemVersion, "fields")
		ClassificationAttributeModel classificationAttribute = classificationSystemService.getAttributeForCode(classificationSystemVersion, "field_unbound")

		when:
		Optional<ClassAttributeAssignmentModel> result = snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classificationClass, classificationAttribute)

		then:
		result.isEmpty()
	}

	@Test
	def "Get class attribute assignment"() {
		given:
		importData("/commerceservices/test/integration/snClassificationSystem.impex", StandardCharsets.UTF_8.name())

		ClassificationSystemVersionModel classificationSystemVersion = classificationSystemService.getSystemVersion(CLASSIFICATION_SYSTEM, CLASSIFICATION_SYSTEM_VERSION)
		ClassificationClassModel classificationClass = classificationSystemService.getClassForCode(classificationSystemVersion, "fields")
		ClassificationAttributeModel classificationAttribute = classificationSystemService.getAttributeForCode(classificationSystemVersion, "field_string")

		when:
		Optional<ClassAttributeAssignmentModel> result = snClassAttributeAssignmentModelDao.findClassAttributeAssignmentByClassAndAttribute(classificationClass, classificationAttribute)

		then:
		result.isPresent()
		result.get().getClassificationClass() == classificationClass
		result.get().getClassificationAttribute() == classificationAttribute
	}
}
