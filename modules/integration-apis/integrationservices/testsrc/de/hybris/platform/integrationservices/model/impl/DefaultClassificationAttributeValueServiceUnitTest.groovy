/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel
import de.hybris.platform.classification.ClassificationSystemService
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import de.hybris.platform.servicelayer.model.ModelService
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultClassificationAttributeValueServiceUnitTest extends Specification {

    def classificationSystemService = Stub ClassificationSystemService
    def modelService = Mock ModelService
    def service = new DefaultClassificationAttributeValueService(classificationSystemService, modelService)

    @Test
    @Unroll
    def "find returns an empty result when #condition"() {
        expect:
        service.find(systemVersion, code).empty

        where:
        condition               | systemVersion                          | code
        'systemVersion is null' | null                                   | "code"
        'code is null'          | Stub(ClassificationSystemVersionModel) | null
        'code is empty'         | Stub(ClassificationSystemVersionModel) | ''
    }

    @Test
    def 'find returns a result when a classification attribute value has the given system version and code'() {
        given:
        def systemVersion = Stub(ClassificationSystemVersionModel)
        def code = 'myValue'

        and: 'classification attribute value is found'
        def classificationAttributeValue = Stub ClassificationAttributeValueModel
        classificationSystemService.getAttributeValueForCode(systemVersion, code) >> classificationAttributeValue

        when:
        def result = service.find(systemVersion, code)

        then:
        result.isPresent()
        result.get() == classificationAttributeValue
    }

    @Test
    def 'find returns an empty result when no classification attribute value matches the system version and code'() {
        given: 'classification attribute value is not found'
        classificationSystemService.getAttributeValueForCode(_ as ClassificationSystemVersionModel, _ as String) >>
                { throw new UnknownIdentifierException("IGNORE - Testing exception") }

        when:
        def result = service.find Stub(ClassificationSystemVersionModel), 'myCode'

        then:
        result.empty
    }

    @Test
    @Unroll
    def "can't create classification attribute value when #argument is #condition"() {
        when:
        service.create(systemVersion, code)

        then:
        def e = thrown IllegalArgumentException
        e.message == "$argument must be provided to create ClassificationAttributeValue"

        where:
        argument        | condition | systemVersion                          | code
        'SystemVersion' | 'null'    | null                                   | "code"
        'Code'          | 'null'    | Stub(ClassificationSystemVersionModel) | null
        'Code'          | 'empty'   | Stub(ClassificationSystemVersionModel) | ''
    }

    @Test
    def 'classification attribute value created'() {
        given:
        def systemVersion = Stub ClassificationSystemVersionModel
        def code = 'myValue'

        when:
        def attrValue = service.create(systemVersion, code)

        then:
        1 * modelService.save(_ as ClassificationAttributeValueModel)
        with(attrValue) {
            systemVersion == systemVersion
            code == code
        }
    }
}
