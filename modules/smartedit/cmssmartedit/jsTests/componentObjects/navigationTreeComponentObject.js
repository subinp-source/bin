/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = {
    collapseButtonClass: 'sap-icon--slim-arrow-down',
    expandButtonClass: 'sap-icon--slim-arrow-right',
    moreButtonClass: 'sap-icon--overflow',
    addNewTopLevelButton: 'Create Top Level',

    _getNode: function(nodeLabel) {
        return by.xpath("//span[.='" + nodeLabel + "']/preceding-sibling::a");
    },
    _getNodeNames: function(array, rows, index) {
        var deferred = protractor.promise.defer();
        if (rows[index]) {
            rows[index].getText().then(
                function(value) {
                    array.push(value);
                    if (index < rows.length - 1) {
                        this._getNodeNames(array, rows, index + 1).then(function(array) {
                            deferred.fulfill(array);
                        });
                    } else {
                        deferred.fulfill(array);
                    }
                }.bind(this)
            );
        } else {
            deferred.fulfill(array);
        }
        return deferred.promise;
    },
    _getNodeHandle: function(nodeLabel) {
        return by.xpath(
            "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                nodeLabel +
                "')]/ancestor::li[contains(@class, 'cdk-drag')][1]"
        );
    },
    _getNodeTitle: function(nodeLabel) {
        var deferred = protractor.promise.defer();
        var selector =
            "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
            nodeLabel +
            "')]/ancestor::div/div[contains(@class, 'se-tree-node__title')]/span";
        element(by.xpath(selector))
            .getText()
            .then(function(value) {
                deferred.fulfill(value);
            });
        return deferred.promise;
    },
    clickOnLinkHavingClass: function(nodeLabel, cssClass) {
        return browser.click(
            by.xpath(
                "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                    nodeLabel +
                    "')]/ancestor::div[contains(@class, 'se-tree-node')]/div[1]/a/span[contains(@class, '" +
                    cssClass +
                    "')]"
            )
        );
    },
    clickOnLinkHavingContent: function(nodeLabel, anchorLabel) {
        return browser.click(
            by.xpath(
                "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                    nodeLabel +
                    "']/ancestor::div[contains(@class, 'se-tree-node')]/div[1]/a/[contains(., '" +
                    anchorLabel +
                    "')]"
            )
        );
    },
    clickOnLinkHavingClassInRenderer: function(nodeLabel, cssClass) {
        return browser.click(
            by.xpath(
                "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                    nodeLabel +
                    "')]/ancestor::div/div/se-dropdown-menu/descendant::div[contains(@class, '" +
                    cssClass +
                    "')]"
            )
        );
    },
    clickNodeByText: function(nodeLabel) {
        return browser.click(this._getNode(nodeLabel));
    },
    expand: function(nodeLabel) {
        return this.clickOnLinkHavingClass(nodeLabel, this.expandButtonClass);
    },
    collapse: function(nodeLabel) {
        return this.clickOnLinkHavingClass(nodeLabel, this.collapseButtonClass);
    },
    waitForyTreeToBePresent: function() {
        browser.waitForPresence(by.xpath('//se-tree'));
    },
    getChildrenNames: function(nodeLabel) {
        this.waitForyTreeToBePresent();
        var deferred = protractor.promise.defer();
        var selector;
        if (nodeLabel) {
            selector = by.xpath(
                "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                    nodeLabel +
                    "')]/ancestor::se-tree-node-renderer/se-tree-node/ol/li/se-tree-node-renderer/div/div[2]/div/div[2]"
            );
        } else {
            selector = by.xpath("//div[contains(@class, 'se-tree-node__name')]/span");
        }
        browser.waitForPresence(selector);

        element.all(selector).then(
            function(children) {
                this._getNodeNames([], children, 0).then(function(array) {
                    deferred.fulfill(array);
                });
            }.bind(this)
        );
        return deferred.promise;
    },
    getNodeTitles: function(nodeLabels) {
        var deferred = protractor.promise.defer();
        var promises = [];

        nodeLabels.forEach(
            function(nodeLabel) {
                this._getNodeTitle(nodeLabel).then(function(value) {
                    promises.push(value);
                    deferred.fulfill(promises);
                });
            }.bind(this)
        );

        return deferred.promise;
    },
    getChildrenCount: function(nodeLabel) {
        return this.getChildrenNames(nodeLabel).then(function(children) {
            return children.length;
        });
    },
    getNthChild: function(nodeLabel, index) {
        return this.getChildrenNames(nodeLabel).then(function(children) {
            return children[index];
        });
    },
    clickMoreMenu: function(nodeLabel) {
        return this.clickOnLinkHavingClassInRenderer(nodeLabel, this.moreButtonClass);
    },
    getMoreMenuOptions: function(nodeLabel) {
        var deferred = protractor.promise.defer();
        var selector;
        if (nodeLabel) {
            selector = by.xpath(
                "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                    nodeLabel +
                    "')]/ancestor::div/div/se-dropdown-menu/descendant::ul/se-dropdown-menu-item/li"
            );
        } else {
            selector = by.xpath('//se-tree/div/ol/li/div/div');
        }
        browser.waitForPresence(selector);
        element.all(selector).then(
            function(options) {
                this._getNodeNames([], options, 0).then(function(array) {
                    deferred.fulfill(array);
                });
            }.bind(this)
        );
        return deferred.promise;
    },
    getMoreMenuItem: function(nodeLabel, option) {
        return by.xpath(
            "//div[contains(@class, 'se-tree-node__name')]/span[contains(., '" +
                nodeLabel +
                "')]/ancestor::div/div/se-dropdown-menu/descendant::a[contains(., '" +
                option +
                "')]"
        );
    },
    getMoreMenuOptionsCount: function(nodeLabel) {
        return this.getMoreMenuOptions(nodeLabel).then(function(children) {
            return children.length;
        });
    },
    clickMoreMenuItem: function(nodeLabel, option) {
        browser.click(this.getMoreMenuItem(nodeLabel, option));
    },
    navigateToFirstCatalogNavigationEditor: function() {
        var landingPage = require('../pageObjects/landingPagePageObject.js');
        return landingPage.actions.navigateToFirstNavigationManagementPage();
    },
    clickAddNewTopLevel: function() {
        browser.click(by.cssContainingText('span', this.addNewTopLevelButton));
        return browser.waitUntilModalAppears();
    },
    assertNodeHasOrderedChildren: function(node, expectedChildren) {
        // yjQuery escapes angular lifecycle, wait for children to be updated
        browser.waitUntil(
            function() {
                return this.getChildrenNames(node).then(
                    function(children) {
                        return children.toString() === expectedChildren.toString();
                    },
                    function() {
                        return false;
                    }
                );
            }.bind(this)
        );
        this.getChildrenNames(node).then(function(children) {
            expect(children.toString()).toEqual(expectedChildren.toString());
        });
    },
    startDraggingNode: function(nodeLabel) {
        var node = browser.findElement(this._getNodeHandle(nodeLabel));
        return browser
            .actions()
            .mouseMove(node)
            .mouseDown()
            .perform()
            .then(
                function() {
                    return browser.sleep(this.dragDelay);
                }.bind(this)
            );
    },
    moveMouseToNode: function(nodeLabel) {
        var node = browser.findElement(this._getNodeHandle(nodeLabel));
        return browser
            .actions()
            .mouseMove(node)
            .perform()
            .then(
                function() {
                    return browser
                        .actions()
                        .mouseMove(this.dragAndDropOffsetFix)
                        .perform();
                }.bind(this)
            );
    },
    mouseUp: function() {
        return browser
            .actions()
            .mouseUp()
            .perform();
    },
    dragAndDropOffsetFix: {
        x: 1,
        y: -11
    },
    dragDelay: 200,
    modal: function() {
        return element(by.css('.fd-modal'));
    },
    modalIsOpen: function() {
        return this.modal() !== null;
    },
    confirmModal: function() {
        return browser.click(element(by.css('#confirmOk')));
    },
    cancelModal: function() {
        return browser.click(element(by.css('#confirmCancel')));
    },
    clickOnDeleteConfirmation: function(buttonMessage) {
        var selector =
            buttonMessage === 'Ok'
                ? '.se-confirmation-dialog #confirmOk'
                : '.se-confirmation-dialog #confirmCancel';
        return browser.click(element(by.css(selector)));
    },
    getDeleteConfirmationMessage: function() {
        return element(by.id('confirmationModalDescription')).getText();
    },

    // Elements
    getAddNewTopLevelButton: function() {
        return browser.findElement(by.css('.se-navigation-editor-tree__header'));
    },

    // Assertions
    assertNavigationManagementPageIsDisplayed: function() {
        expect(this.getAddNewTopLevelButton().isPresent()).toBe(true);
    },

    getRowByName: function(rowName) {
        return browser.findElement(
            by.cssContainingText(
                '.desktopLayout > ol[data-ui-tree-nodes] > li div[ui-tree-handle]',
                rowName
            )
        );
    },

    getDropdownItemByRowAndLabel: function(rowName, optionLabel) {
        return this.getRowByName(rowName)
            .all(by.cssContainingText('.se-dropdown-menu__list a', optionLabel))
            .first();
    }
};
