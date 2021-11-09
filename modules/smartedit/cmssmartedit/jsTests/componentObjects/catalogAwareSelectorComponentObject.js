/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var catalogAwareSelector = {};

    catalogAwareSelector.constants = {
        DRAG_AND_DROP_OFFSET_FIX: {
            x: 1,
            y: 10
        },
        DRAG_DELAY: 200
    };

    catalogAwareSelector.elements = {
        getCatalogAwareSelectorRoot: function(selectorId) {
            return element(by.css('se-catalog-aware-selector[data-id="' + selectorId + '"]'));
        },
        getAddItemsButton: function(selectorId) {
            return catalogAwareSelector.elements
                .getCatalogAwareSelectorRoot(selectorId)
                .element(by.css('#catalog-aware-selector-add-item'));
        },
        getAddMoreItemsButton: function(selectorId) {
            return catalogAwareSelector.elements
                .getCatalogAwareSelectorRoot(selectorId)
                .element(by.cssContainingText('button', 'Add More'));
        },

        // -- Panel --
        getCatalogVersionDropdown: function() {
            return element(
                by.css(
                    'y-select[data-id="se-catalog-version-selector-dropdown"] .se-generic-editor-dropdown__match2'
                )
            );
        },
        getCatalogVersionOption: function(catalogVersion) {
            return element(
                by.cssContainingText(
                    'y-select[data-id="se-catalog-version-selector-dropdown"] ul li .y-select-default-item',
                    catalogVersion
                )
            );
        },
        getItemSearchBox: function() {
            return element(by.css('#se-items-selector-dropdown input'));
        },
        getItemSearchBoxOption: function(itemName) {
            return element
                .all(
                    by.cssContainingText(
                        '#se-items-selector-dropdown .se-product-row .se-product-row__product',
                        itemName
                    )
                )
                .first();
        },
        getAddButton: function() {
            return element(by.cssContainingText('button', 'se.cms.catalogaware.panel.button.add'));
        },

        // -- List --
        getListItems: function(selectorId) {
            return element.all(
                by.css(
                    'se-catalog-aware-selector[data-id="' +
                        selectorId +
                        '"] se-editable-list ol li.angular-ui-tree-node'
                )
            );
        },
        getItemInList: function(selectorId, itemName, catalogVersion) {
            return catalogAwareSelector.elements
                .getCatalogAwareSelectorRoot(selectorId)
                .element(
                    by.xpath(
                        '//*[*[normalize-space(text())="' +
                            itemName +
                            '"] and *[contains(normalize-space(text()),"' +
                            catalogVersion +
                            '")]]'
                    )
                );
        },
        getItemMoreMenuButton: function(selectorId, itemIndex) {
            return catalogAwareSelector.elements
                .getListItems(selectorId)
                .get(itemIndex)
                .element(by.css('.se-dropdown-more-menu .se-dropdown-more-menu__toggle'));
        },
        getExpandedDropdownMenu: function() {
            return element(by.css('.se-dropdown-more-menu .se-dropdown-menu__list'));
        },
        getMoveUpButton: function() {
            return catalogAwareSelector.elements
                .getExpandedDropdownMenu()
                .element(by.cssContainingText('li a', 'Move Up'));
        },
        getMoveDownButton: function() {
            return catalogAwareSelector.elements
                .getExpandedDropdownMenu()
                .element(by.cssContainingText('li a', 'Move Down'));
        },
        getDeleteItemButton: function() {
            return catalogAwareSelector.elements
                .getExpandedDropdownMenu()
                .element(by.cssContainingText('li a', 'Delete'));
        }
    };

    catalogAwareSelector.actions = {
        openSelector: function(selectorId) {
            var addItemsButton = catalogAwareSelector.elements.getAddItemsButton(selectorId);
            return addItemsButton.isDisplayed().then(function(isDisplayed) {
                if (isDisplayed) {
                    return browser.click(addItemsButton);
                } else {
                    return browser.click(
                        catalogAwareSelector.elements.getAddMoreItemsButton(selectorId)
                    );
                }
            });
        },
        openCatalogVersionDropdown: function() {
            return browser.click(catalogAwareSelector.elements.getCatalogVersionDropdown());
        },
        selectCatalogVersionInDropdown: function(catalogVersion) {
            return browser.click(
                catalogAwareSelector.elements.getCatalogVersionOption(catalogVersion)
            );
        },
        openItemSearchBox: function() {
            return browser.click(catalogAwareSelector.elements.getItemSearchBox());
        },
        selectItemInSearchBox: function(itemName) {
            return browser.click(catalogAwareSelector.elements.getItemSearchBoxOption(itemName));
        },
        selectItem: function(itemName) {
            return catalogAwareSelector.actions.openItemSearchBox().then(function() {
                catalogAwareSelector.actions.selectItemInSearchBox(itemName);
            });
        },
        selectCatalogVersion: function(catalogVersion) {
            return catalogAwareSelector.actions.openCatalogVersionDropdown().then(function() {
                catalogAwareSelector.actions.selectCatalogVersionInDropdown(catalogVersion);
            });
        },
        selectItemsInCatalogVersion: function(items, catalogVersion) {
            return catalogAwareSelector.actions
                .selectCatalogVersion(catalogVersion)
                .then(function() {
                    items.forEach(function(item) {
                        catalogAwareSelector.actions.selectItem(item);
                    });
                });
        },
        selectItems: function(selectorId, itemsByCategory) {
            return catalogAwareSelector.actions.openSelector(selectorId).then(function() {
                var promisesToResolve = [];
                for (var category in itemsByCategory) {
                    if (itemsByCategory.hasOwnProperty(category)) {
                        promisesToResolve.push(
                            catalogAwareSelector.actions.selectItemsInCatalogVersion(
                                itemsByCategory[category],
                                category
                            )
                        );
                    }
                }

                return protractor.promise.all(promisesToResolve);
            });
        },
        clickAddItemsButton: function() {
            return browser.click(catalogAwareSelector.elements.getAddButton());
        },

        // -- List --
        moveItemUp: function(selectorId, itemIndex) {
            return browser
                .click(catalogAwareSelector.elements.getItemMoreMenuButton(selectorId, itemIndex))
                .then(function() {
                    return browser.click(catalogAwareSelector.elements.getMoveUpButton());
                });
        },
        moveItemDown: function(selectorId, itemIndex) {
            return browser
                .click(catalogAwareSelector.elements.getItemMoreMenuButton(selectorId, itemIndex))
                .then(function() {
                    return browser.click(catalogAwareSelector.elements.getMoveDownButton());
                });
        },
        deleteItem: function(selectorId, itemIndex) {
            return browser
                .click(catalogAwareSelector.elements.getItemMoreMenuButton(selectorId, itemIndex))
                .then(function() {
                    return browser.click(catalogAwareSelector.elements.getDeleteItemButton());
                });
        },
        grabItemAtPosition: function(selectorId, itemPosition) {
            return browser
                .actions()
                .mouseMove(catalogAwareSelector.elements.getListItems(selectorId).get(itemPosition))
                .mouseDown()
                .perform()
                .then(function() {
                    return browser.sleep(catalogAwareSelector.constants.DRAG_DELAY);
                });
        },
        moveToItemAtPosition: function(selectorId, itemPosition) {
            var itemToMove = catalogAwareSelector.elements
                .getListItems(selectorId)
                .get(itemPosition);
            return browser
                .actions()
                .mouseMove(itemToMove)
                .mouseMove(catalogAwareSelector.constants.DRAG_AND_DROP_OFFSET_FIX)
                .perform();
        },
        dropItem: function() {
            return browser
                .actions()
                .mouseUp()
                .perform();
        },
        dragAndDropItemToPosition: function(selectorId, originalIndex, newIndex) {
            return catalogAwareSelector.actions
                .grabItemAtPosition(selectorId, originalIndex)
                .then(function() {
                    return catalogAwareSelector.actions
                        .moveToItemAtPosition(selectorId, newIndex)
                        .then(function() {
                            return browser.sleep(catalogAwareSelector.constants.DRAG_DELAY);
                        })
                        .then(function() {
                            return catalogAwareSelector.actions.dropItem();
                        });
                });
        }
    };

    catalogAwareSelector.assertions = {
        isEmpty: function(selectorId) {
            expect(catalogAwareSelector.elements.getAddItemsButton(selectorId).isDisplayed()).toBe(
                true,
                'Expected catalog aware selector to be empty.'
            );
        },
        isItemSelected: function(selectorId, itemName, catalogVersion) {
            browser.waitForPresence(
                catalogAwareSelector.elements.getItemInList(selectorId, itemName, catalogVersion)
            );
        },
        itemsAreSelected: function(selectorId, itemsList) {
            for (var catalogVersion in itemsList) {
                if (itemsList.hasOwnProperty(catalogVersion)) {
                    catalogAwareSelector.assertions.isItemSelected(
                        selectorId,
                        itemsList[catalogVersion],
                        catalogVersion
                    );
                }
            }
        },
        itemsAreInRightOrder: function(selectorId, expectedItems) {
            var itemsList = catalogAwareSelector.elements.getListItems(selectorId);
            for (var i = 0; i < expectedItems.length; i++) {
                var item = itemsList.get(i);
                expect(item.getText()).toContain(
                    expectedItems[i],
                    'Expected ' + expectedItems[i] + ' to be in right order.'
                );
            }
        },
        addItemsButtonIsNotDisplayed: function(selectorId) {
            expect(
                browser.isAbsent(catalogAwareSelector.elements.getAddItemsButton(selectorId))
            ).toBe(true);
        }
    };

    catalogAwareSelector.utils = {
        getElementSize: function(element) {
            return element.getSize();
        }
    };

    return catalogAwareSelector;
})();
