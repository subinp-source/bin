/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.model.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.type.AtomicTypeModel
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DescriptorUtilsUnitTest extends Specification {

    @Test
    @Unroll
    def "item model extracted from a type descriptor is #res when the type descriptor is #condition"() {
        expect:
        DescriptorUtils.extractModelFrom(type) == res

        where:
        condition         | type                                                        | res
        'null'            | null                                                        | Optional.empty()
        'for a primitive' | PrimitiveTypeDescriptor.create('io', Stub(AtomicTypeModel)) | Optional.empty()
        'for an item'     | ItemTypeDescriptor.create(Stub(IntegrationObjectItemModel)) | Optional.of(type.itemTypeModel)
    }
}
