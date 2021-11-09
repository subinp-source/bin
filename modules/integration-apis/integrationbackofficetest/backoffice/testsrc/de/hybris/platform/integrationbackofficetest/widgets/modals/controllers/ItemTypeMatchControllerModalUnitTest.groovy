/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.core.PK
import de.hybris.platform.integrationbackoffice.utility.DefaultItemTypeMatchSelector
import de.hybris.platform.integrationbackoffice.utility.ItemTypeMatchSelector
import de.hybris.platform.integrationbackoffice.widgets.modals.controllers.ItemTypeMatchModalController
import de.hybris.platform.integrationservices.enums.ItemTypeMatchEnum
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel
import de.hybris.platform.integrationservices.search.ItemTypeMatch
import org.junit.Test
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Listbox
import org.zkoss.zul.Listcell
import org.zkoss.zul.Listitem
import spock.lang.Specification
import spock.lang.Unroll


@UnitTest
class ItemTypeMatchControllerModalUnitTest extends Specification {

    private static final String DROP_DOWN_COLUMN_ID_SUFFIX = "_ddc"
    private static final String DROP_DOWN_BOX_ID_SUFFIX = "_ddb"
    private ItemTypeMatchModalController itemTypeMatchModalController = new ItemTypeMatchModalController()

    @Test
    def "getListItem() creates correct list item presentation for a given item model"() {
        given:
        def integrationObjectItemModel = Stub(IntegrationObjectItemModel) {
            getCode() >> "IOI1"
            getPk() >> PK.fromLong(8000042)
            getAllowedItemTypeMatches() >> [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE, ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES]

        }
        and: 'the item type match list has an option selected'
        def itemTypeMatchSelector = Stub(ItemTypeMatchSelector) {
            getToSelectItemTypeMatch(integrationObjectItemModel) >> ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES
        }
        itemTypeMatchModalController.itemTypeMatchSelector = itemTypeMatchSelector

        when:
        Listitem listItem = itemTypeMatchModalController.getListItem(integrationObjectItemModel)

        then: 'Each row of the ItemTypeMatch modal has a value equal to the integrationObjectItemModel'
        listItem.value == integrationObjectItemModel
        and: 'the first column of a row in the modal has the label with integrationObjectItemModel code'
        listItem.children[0].label == integrationObjectItemModel.code
        and: 'the second column of a row in the modal is a dropdown column that has an expected ID generated'
        listItem.children[1].id == integrationObjectItemModel.pk.hex + DROP_DOWN_COLUMN_ID_SUFFIX
        and: 'The dropdown column has the select options'
        with(listItem.children[1].children[0]) {
            and: 'The number of options available is same as integrationObjectItemModel allowedItemTypeMatches'
            items.size() == integrationObjectItemModel.allowedItemTypeMatches.size()
            and: 'The select element has an expected ID generated'
            id == integrationObjectItemModel.getPk().getHex() + DROP_DOWN_BOX_ID_SUFFIX
            and: 'Out of all available options only one is selected'
            selectedCount == 1
            and: 'The selected option has a label same as integrationObjectItemModal itemTypeMatch if it is not null or what is set as default'
            selectedItem.label == itemTypeMatchSelector.getToSelectItemTypeMatch(integrationObjectItemModel).toString()
        }
    }

    @Unroll
    @Test
    def "The current ItemTypeMatchEnum is #originalItemTypeMatch and selected one is #SelectedItemTypeMatch then itemTypeMatchChangeDetector.isDirty is #isDirty"() {
        given:
        def integrationObjectItemModel = Stub(IntegrationObjectItemModel) {
            getCode() >> "IOI1"
            getPk() >> PK.fromLong(8000042)
            getAllowedItemTypeMatches() >> [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE, ItemTypeMatchEnum.ALL_SUBTYPES]
            getItemTypeMatch() >> originalItemTypeMatch
        }
        def dropDownBox = Stub(Listbox) {
            getId() >> integrationObjectItemModel.pk.hex + DROP_DOWN_BOX_ID_SUFFIX
            getSelectedItem() >> Stub(Listitem) {
                getLabel() >> SelectedItemTypeMatch
            }
        }
        def dropDownColumn = Stub(Listcell) {
            getId() >> integrationObjectItemModel.pk.hex + DROP_DOWN_COLUMN_ID_SUFFIX
            getChildren() >> [dropDownBox]
        }
        def aRow = Stub(Listitem) {
            getValue() >> integrationObjectItemModel
            getChildren() >> [Stub(Listcell), dropDownColumn]
        }

        when:
        def itemTypeMatchChangeDetector = itemTypeMatchModalController.createChangeDetectorForEachRowOfTheModal(aRow)

        then:
        itemTypeMatchChangeDetector.isDirty() == isDirty

        where:
        originalItemTypeMatch                     | SelectedItemTypeMatch                     | isDirty
        null                                      | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | true
        null                                      | ItemTypeMatchEnum.ALL_SUBTYPES            | true
        null                                      | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | true
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | false
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | true
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | true
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.ALL_SUBTYPES            | true
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatchEnum.ALL_SUBTYPES            | false
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.ALL_SUBTYPES            | true
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | true
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | true
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | false
    }

    @Test
    def "detects the integrationObjectItemModel whose ItemTypeMatchEnum has changed"() {
        given:
        def integrationObjectItemModel1 = getIntegrationItemModel("IOI1", 8000042, null)
        def integrationObjectItemModel2 = getIntegrationItemModel("IOI2", 8000043, ItemTypeMatchEnum.ALL_SUBTYPES)
        def firstRow = getARow(integrationObjectItemModel1)
        def secondRow = getARow(integrationObjectItemModel2)
        itemTypeMatchModalController.itemTypeMatcherListBox = Stub(Listbox) {
            getItems() >> [firstRow, secondRow]
        }

        when:
        List<IntegrationObjectItemModel> changedList = itemTypeMatchModalController.getChangedIntegrationObjectItems()

        then:
        changedList.size() == 1
        changedList[0].code == integrationObjectItemModel1.code
    }

    @Unroll
    @Test
    def "When the inputs to createDropDownOption is option #allowedItemTypeMatchOpt and the option to select for the IOI is #itemTypeMatchToSelectOpt then the option selected is #isSelected"() {
        given:
        ItemTypeMatchEnum allowedItemTypeMatch = allowedItemTypeMatchOpt
        ItemTypeMatch itemTypeMatchToSelect = itemTypeMatchToSelectOpt

        when:
        def listItem = itemTypeMatchModalController.createDropDownOption(allowedItemTypeMatch, itemTypeMatchToSelect)

        then:
        listItem.isSelected() == isSelected

        where:
        allowedItemTypeMatchOpt                   | itemTypeMatchToSelectOpt              | isSelected
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | true
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatch.ALL_SUBTYPES            | true
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | true
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatch.ALL_SUBTYPES            | false
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | false
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | false
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatch.RESTRICT_TO_ITEM_TYPE   | false
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatch.ALL_SUB_AND_SUPER_TYPES | false
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatch.ALL_SUBTYPES            | false
        ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES | ItemTypeMatch.DEFAULT                 | true
        ItemTypeMatchEnum.ALL_SUBTYPES            | ItemTypeMatch.DEFAULT                 | false
        ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE   | ItemTypeMatch.DEFAULT                 | false
    }

    @Unroll
    @Test
    def "When integrationItemModel.getAllowedItemTypeMatches() is #allowedItemTypeMatches then the number of options in the drop down is #size"() {
        given:
        def itemTypeMatchSelector = Stub(DefaultItemTypeMatchSelector)
        itemTypeMatchModalController.itemTypeMatchSelector = itemTypeMatchSelector

        def integrationItemModel = Stub(IntegrationObjectItemModel) {
            given:
            getCode() >> "IOI1"
            getPk() >> PK.fromLong(8000042)
            getAllowedItemTypeMatches() >> allowedItemTypeMatches
        }

        when:
        def listCell = itemTypeMatchModalController.createItemTypeMatchDropDown(integrationItemModel)

        then: 'list contains 1 option'
        listCell.children.size() == 1
        and: 'the list option has expected ID generated'
        listCell.children[0].id == integrationItemModel.pk.hex + DROP_DOWN_BOX_ID_SUFFIX
        and: 'the list option is associated with an ON_SELECT event listener'
        listCell.children[0].getEventListeners(Events.ON_SELECT)
        and:
        listCell.children[0].children.size() == size
        def labels = listCell.children[0].items.collect({ it.label })
        def allowedItemTypes = allowedItemTypeMatches.collect({ it.toString() })
        labels.containsAll allowedItemTypes

        where:
        allowedItemTypeMatches                                                                                               | size
        [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE, ItemTypeMatchEnum.ALL_SUBTYPES] | 3
        [ItemTypeMatchEnum.ALL_SUBTYPES]                                                                                     | 1
    }

    def getIntegrationItemModel(String code, long pk, ItemTypeMatchEnum itemTypeMatchEnum) {
        Stub(IntegrationObjectItemModel) {
            getCode() >> code
            getPk() >> PK.fromLong(pk)
            getAllowedItemTypeMatches() >> [ItemTypeMatchEnum.ALL_SUB_AND_SUPER_TYPES, ItemTypeMatchEnum.RESTRICT_TO_ITEM_TYPE, ItemTypeMatchEnum.ALL_SUBTYPES]
            getItemTypeMatch() >> itemTypeMatchEnum
        }
    }

    def getARow(IntegrationObjectItemModel integrationObjectItemModel) {
        def dropDownBox = Stub(Listbox) {
            getId() >> integrationObjectItemModel.getPk().getHex() + ItemTypeMatchModalController.DROP_DOWN_BOX_ID_SUFFIX
            getSelectedItem() >> Stub(Listitem) {
                getLabel() >> ItemTypeMatchEnum.ALL_SUBTYPES.toString()
            }
        }
        def dropDownColumn = Stub(Listcell) {
            getId() >> integrationObjectItemModel.getPk().getHex() + ItemTypeMatchModalController.DROP_DOWN_COLUMN_ID_SUFFIX
            getChildren() >> [dropDownBox]
        }
        Stub(Listitem) {
            getValue() >> integrationObjectItemModel
            getChildren() >> [Stub(Listcell), dropDownColumn]
        }
    }
}