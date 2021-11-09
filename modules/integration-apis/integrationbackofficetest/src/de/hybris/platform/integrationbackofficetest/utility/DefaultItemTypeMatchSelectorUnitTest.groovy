/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackofficetest.utility

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationbackoffice.utility.DefaultItemTypeMatchSelector
import de.hybris.platform.integrationservices.config.ItemSearchConfiguration
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.search.ItemTypeMatch
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class DefaultItemTypeMatchSelectorUnitTest extends Specification {

    private DefaultItemTypeMatchSelector defaultItemTypeMatchSelector = new DefaultItemTypeMatchSelector()

    @Unroll
    @Test
    def "getToSelectItemTypeMatch() returns the correct ItemTypeMatch #itemTypeMatchResult to be selected in the ItemTypeMatchModal for the IntegrationObjectItemModel"() {
        given:
        def integrationObjectItemModel = Stub(IntegrationObjectItemModel) {
            getItemTypeMatch() >> itemTypeMatchOfIOI
            getType() >> typeOfIOI
        }
        and:'ItemSearchConfiguration is set'
        defaultItemTypeMatchSelector.setItemSearchConfiguration(itemSearchConfiguration)

        when:
        ItemTypeMatch itemTypeMatch = defaultItemTypeMatchSelector.getToSelectItemTypeMatch(integrationObjectItemModel)

        then:
        itemTypeMatch == itemTypeMatchResult

        where:
        itemTypeMatchOfIOI             | typeOfIOI                      | itemSearchConfiguration      | itemTypeMatchResult
        ItemTypeMatchEnum.ALL_SUBTYPES | null                           | null                         | ItemTypeMatch.ALL_SUBTYPES
        null                           | Stub(ComposedTypeModel)        | null                         | ItemTypeMatch.DEFAULT
        null                           | Stub(ComposedTypeModel)        | getItemSearchConfiguration() | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        ItemTypeMatchEnum.ALL_SUBTYPES | Stub(EnumerationMetaTypeModel) | null                         | ItemTypeMatch.ALL_SUBTYPES
        null                           | Stub(EnumerationMetaTypeModel) | null                         | ItemTypeMatch.ALL_SUBTYPES
        ItemTypeMatchEnum.ALL_SUBTYPES | getAbstractType()              | null                         | ItemTypeMatch.ALL_SUBTYPES
        null                           | getAbstractType()              | null                         | ItemTypeMatch.ALL_SUBTYPES
    }

    def getItemSearchConfiguration() {
        Stub(ItemSearchConfiguration) {
            getItemTypeMatch() >> ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        }
    }

    def getAbstractType() {
        Stub(ComposedTypeModel) {
            getAbstract() >> true
        }
    }
}
