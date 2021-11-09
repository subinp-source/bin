/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.interceptor

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.servicelayer.interceptor.InterceptorContext
import de.hybris.platform.servicelayer.interceptor.InterceptorException
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class IntegrationObjectItemItemTypeMatchValidateInterceptorUnitTest extends Specification {

    private static final String ITEM_CODE = "ioiCode"

    def interceptor = new IntegrationObjectItemItemTypeMatchValidateInterceptor()

    @Test
    @Unroll
    def "no exception thrown when an IntegrationObjectItem of an #type type is being created with an itemTypeMatch set to #typeMatch"() {
        when:
        interceptor.onValidate item, Stub(InterceptorContext)

        then:
        noExceptionThrown()

        where:
        type       | typeMatch                                 | item
        'composed' | null                                      | item(typeMatch)
        'composed' | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | item(typeMatch)
        'composed' | ItemTypeMatchEnum.ALL_SUBTYPES            | item(typeMatch)
        'composed' | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | item(typeMatch)
        'abstract' | null                                      | abstractItem(typeMatch)
        'abstract' | ItemTypeMatchEnum.ALL_SUBTYPES            | abstractItem(typeMatch)
        'enum'     | null                                      | enumerationItem(typeMatch)
        'enum'     | ItemTypeMatchEnum.ALL_SUBTYPES            | enumerationItem(typeMatch)


    }

    @Test
    @Unroll
    def "throws an exception when an IntegrationObjectItem of an #type type is being created with an itemTypeMatch set to #typeMatch"() {
        when:
        interceptor.onValidate item, Stub(InterceptorContext)

        then:
        def e = thrown InterceptorException
        e.message.contains "An IntegrationObjectItem of type $ITEM_CODE cannot have an itemTypeMatch" +
                " set to $typeMatch. The permitted itemTypeMatch values for an item of this type are [ALL_SUBTYPES]"

        where:
        type       | typeMatch                                 | item
        'enum'     | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | enumerationItem(typeMatch)
        'enum'     | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | enumerationItem(typeMatch)
        'abstract' | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | abstractItem(typeMatch)
        'abstract' | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | abstractItem(typeMatch)
    }

    def enumerationItem(final itemTypeMatch = null) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> ITEM_CODE
            getType() >> Stub(EnumerationMetaTypeModel) {
                getCode() >> ITEM_CODE
            }
            getItemTypeMatch() >> itemTypeMatch
            getAllowedItemTypeMatches() >> [ItemTypeMatchEnum.ALL_SUBTYPES]
        }
    }

    def abstractItem(final itemTypeMatch = null) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> ITEM_CODE
            getType() >> Stub(EnumerationMetaTypeModel) {
                getCode() >> ITEM_CODE
                getAbstract() >> true
            }
            getItemTypeMatch() >> itemTypeMatch
            getAllowedItemTypeMatches() >> [ItemTypeMatchEnum.ALL_SUBTYPES]
        }
    }

    def item(final itemTypeMatch = null) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> ITEM_CODE
            getType() >> Stub(ComposedTypeModel) {
                getCode() >> ITEM_CODE
                getAbstract() >> false
            }
            getItemTypeMatch() >> itemTypeMatch
            getAllowedItemTypeMatches() >> composedTypeAllowedEnumValues()
        }
    }

    private static List<ItemTypeMatchEnum> composedTypeAllowedEnumValues() {
        [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES,
         ItemTypeMatchEnum.ALL_SUBTYPES,
         ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE]
    }
}
