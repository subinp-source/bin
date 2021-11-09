package de.hybris.platform.integrationservices.model

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel
import de.hybris.platform.core.model.type.ComposedTypeModel
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import org.junit.Test
import spock.lang.Specification
import spock.lang.Unroll

@UnitTest
class AllowedItemTypeMatchesAttributeHandlerUnitTest extends Specification {

    def handler = new AllowedItemTypeMatchesAttributeHandler()

    @Test
    @Unroll
    def "returns #applicableItemTypeMatchEnums when modelType is #desc"() {
        expect:
        handler.get(type).containsAll(applicableItemTypeMatchEnums)

        where:
        desc          | type              | applicableItemTypeMatchEnums
        'enumeration' | enumerationItem() | [ItemTypeMatchEnum.ALL_SUBTYPES]
        'abstract'    | abstractItem()    | [ItemTypeMatchEnum.ALL_SUBTYPES]
        'item'        | item()            | [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatchEnum.ALL_SUBTYPES, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE]
    }

    def enumerationItem() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(EnumerationMetaTypeModel)
        }
    }

    def abstractItem() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(EnumerationMetaTypeModel) {
                getAbstract() >> true
            }
        }
    }

    def item() {
        Stub(IntegrationObjectItemModel) {
            getType() >> Stub(ComposedTypeModel)
        }
    }
}

