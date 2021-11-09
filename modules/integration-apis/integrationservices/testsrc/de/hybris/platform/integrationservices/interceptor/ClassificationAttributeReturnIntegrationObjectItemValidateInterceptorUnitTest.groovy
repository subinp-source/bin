/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemClassificationAttributeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ClassificationAttributeReturnIntegrationObjectItemValidateInterceptorUnitTest extends Specification {

	def interceptor = new ClassificationAttributeReturnIntegrationObjectItemValidateInterceptor()

	@Shared
	def referenceAttribute = attributeWithReturnIntegrationObjectItem(ClassificationAttributeTypeEnum.REFERENCE)
	@Shared
	def stringAttribute = attributeWithReturnIntegrationObjectItem(ClassificationAttributeTypeEnum.STRING)

	@Test
	@Unroll
	def "no exception is thrown when class attribute assignment #msg"() {
		when:
		interceptor.onValidate attribute, Stub(InterceptorContext)

		then:
		noExceptionThrown()

		where:
		attribute          | msg
		referenceAttribute | "of 'reference' attributeType provides a returnIntegrationObjectItem"
		stringAttribute    | "of a primitive attributeType provides a returnIntegrationObjectItem"
	}

	@Test
	def "exception is thrown when class attribute of `reference` attributeType has null returnIntegrationObjectItem"() {
		given:
		def attributeName = 'attrName'
		def attribute = Stub(IntegrationObjectItemClassificationAttributeModel) {
			getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
				getAttributeType() >> ClassificationAttributeTypeEnum.REFERENCE
			}
			getReturnIntegrationObjectItem() >> null
			getAttributeName() >> attributeName
		}

		when:
		interceptor.onValidate attribute, Stub(InterceptorContext)

		then:
		def exception = thrown(InterceptorException)
		exception.message.contains "Missing returnIntegrationObjectItem for classification attribute [$attributeName] of 'reference' attributeType."
	}

	def attributeWithReturnIntegrationObjectItem(final ClassificationAttributeTypeEnum attributeType) {
		Stub(IntegrationObjectItemClassificationAttributeModel) {
			getClassAttributeAssignment() >> Stub(ClassAttributeAssignmentModel) {
				getAttributeType() >> attributeType
			}
			getReturnIntegrationObjectItem() >> Stub(IntegrationObjectItemModel)
		}
	}
}
