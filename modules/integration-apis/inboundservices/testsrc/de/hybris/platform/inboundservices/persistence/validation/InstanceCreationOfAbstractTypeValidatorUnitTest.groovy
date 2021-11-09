/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.inboundservices.persistence.PersistenceContext
import de.hybris.platform.integrationservices.item.IntegrationItem
import de.hybris.platform.integrationservices.model.TypeDescriptor
import de.hybris.platform.integrationservices.search.ItemSearchRequest
import org.junit.Test
import spock.lang.Specification

@UnitTest
class InstanceCreationOfAbstractTypeValidatorUnitTest extends Specification {

    def validator = new InstanceCreationOfAbstractTypeValidator()

    @Test
    def 'validator throws exception when item type is abstract'() {
        given:
        def request = persistenceRequestWithAbstractType()

        when:
        validator.validate(request)

        then:
        thrown InstanceCreationOfAbstractTypeException
    }
    @Test
    def 'validator does not throw exception when item type is not abstract'() {
        given:
        def contextItem = Stub ItemModel
        def request = persistenceRequest(contextItem)

        when:
        validator.validate(request)

        then:
        noExceptionThrown()
    }

    private PersistenceContext persistenceRequestWithAbstractType() {
        Stub(PersistenceContext) {
            getIntegrationItem() >> Stub(IntegrationItem) {
                getItemType() >> Stub(TypeDescriptor) {
                    isAbstract() >> true
                }
            }
        }
    }

    private PersistenceContext persistenceRequest(final ItemModel itemModel) {
        Mock(PersistenceContext) {
            getContextItem() >> Optional.ofNullable(itemModel)
            toItemSearchRequest() >> Stub(ItemSearchRequest)
            isItemCanBeCreated() >> true
            getIntegrationItem() >> Stub(IntegrationItem) {
                getItemType() >> Stub(TypeDescriptor) {
                    isAbstract() >> false
                    getItemCode() >> "code"
                }
            }
        }
    }

}
