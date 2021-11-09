/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = (function() {
    var landingPage = require('./landingPagePageObject');
    var pageStatus = require('../componentObjects/pageStatusComponentObject');

    var NUMBER_OF_ARROWS_IN_PAGINATION_LIST = 2;
    var syncPanel = e2e.componentObjects.synchronizationPanel;
    var confirmationModal = e2e.componentObjects.confirmationModal;
    var pageList = {};
    var MOVETOTRASH_LABEL = 'Move to Trash';

    pageList.selectors = {
        getColumnHeaderForKeySelector: function(key) {
            return by.css(
                '.se-paged-list-table thead tr:first-child .se-paged-list__header-' + key
            );
        },

        getFirstRowForKeySelector: function(key) {
            return by.css('.se-paged-list-table tbody tr:first-child .se-paged-list-item-' + key);
        },

        getLastRowForKeySelector: function(key) {
            return by.css('.se-paged-list-table tbody tr:last-child .se-paged-list-item-' + key);
        },
        getTotalPageCountSelector: function() {
            return by.css('.se-page-list__page-count');
        },

        getRestrictionsTooltipSelector: function() {
            return by.css('.se-popover');
        }
    };

    pageList.elements = {
        getTotalPageCount: function() {
            browser.waitForPresence(
                element(by.css('.se-paged-list__page-count-wrapper')),
                'cannot find page list count item'
            );
            return element(by.css('.se-page-list__page-count'));
        },

        getTotalPageCountByValue: function(value) {
            return element(
                by.cssContainingText('.se-page-list__page-count', '(' + value.toString() + ')')
            );
        },

        getDisplayedPageCount: function() {
            return element.all(by.css('.se-paged-list-table tbody tr')).count();
        },

        getPaginationCount: function() {
            return element
                .all(by.css('.fd-pagination__link'))
                .count()
                .then(function(count) {
                    return count - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
                });
        },

        getPageDropdownMenu: function() {
            return element(by.css('ul.dropdown-menu'));
        },

        getDropdownSyncButton: function() {
            return element(by.cssContainingText('.se-dropdown-menu__list li', 'Sync'));
        },

        getDropdownEditButton: function() {
            return element(by.cssContainingText('.se-dropdown-menu__list li', 'Edit'));
        },

        getDropdownMoveToTrashButton: function() {
            return element(by.cssContainingText('.se-dropdown-menu__list li', 'Move to Trash'));
        },

        getModalDialog: function() {
            return element(by.css('.modal-dialog'));
        },

        getConfirmationDialog: function() {
            return element(by.css('.se-confirmation-dialog'));
        },

        getModalSyncPanel: function() {
            return element(by.css('.modal-dialog synchronization-panel'));
        },

        getSynchronizableItemsForPage: function() {
            return element.all(by.css('.se-sync-panel .se-sync-panel__sync-info__row'));
        },

        getModalSyncPanelSyncButton: function() {
            return element(by.css('.modal-dialog #sync'));
        },

        getClickableModalSyncPanelSyncButton: function() {
            return element(by.css('.modal-dialog #sync:not([disabled])'));
        },

        getSyncedPageSyncIcon: function() {
            return element(
                by.css('.se-paged-list__table-body > tr:nth-child(2) page-list-sync-icon .IN_SYNC')
            );
        },

        getFirstPageSyncIcon: function() {
            return element(
                by.css('.se-paged-list__table-body > tr:first-child page-list-sync-icon .IN_SYNC')
            );
        },

        getSearchInput: function() {
            return element(by.css('.ySEPage-list-search-input'));
        },

        clearSearchFilter: function(searchKeys) {
            return browser.click(element(by.css('.se-input-group__clear-btn')));
        },

        getColumnHeaderForKey: function(key) {
            return element(pageList.selectors.getColumnHeaderForKeySelector(key));
        },

        getFirstRowForKey: function(key) {
            return element(pageList.selectors.getFirstRowForKeySelector(key));
        },

        getLastRowForKey: function(key) {
            return element(pageList.selectors.getLastRowForKeySelector(key));
        },

        getLinkForKeyAndRow: function(key, row, selector) {
            return element(
                by.css(
                    '.se-paged-list-table tbody tr:nth-child(' +
                        row +
                        ') .se-paged-list-item-' +
                        key +
                        ' ' +
                        selector
                )
            );
        },

        getCatalogName: function() {
            return element(by.css('.se-page-list__sub-title'));
        },

        getRestrictionsIconForHomePage: function() {
            return element(by.cssContainingText('tr', 'homepage')).element(
                by.css('.restrictionPageListIcon')
            );
        },

        getRestrictionsIconForPageById: function(id) {
            return element(by.cssContainingText('tr', id)).element(
                by.css('img.restrictionPageListIcon')
            );
        },

        getRestrictionsTooltip: function() {
            return element(pageList.selectors.getRestrictionsTooltipSelector());
        },

        getCatalogPageListLink: function(catalog) {
            return element(by.css('.page-list-link-item a[data-ng-href*=' + catalog + ']'));
        },

        getAddNewPageButton: function() {
            return browser.element(by.css('.se-page-list__add'));
        },

        getPageListTrashLink: function() {
            return element(by.css('.se-page-list__page-link--right '));
        },

        getRowByPageName: function(pageName) {
            return browser.findElement(pageList.utils.getPageRowQuery(pageName));
        },

        getDropDownButtonByPageName: function(pageName) {
            return pageList.elements
                .getRowByPageName(pageName)
                .element(by.css('.se-dropdown-more-menu__toggle'));
        },

        getTrashViewLink: function() {
            return browser.findElement(by.css('.se-page-list__page-link--right a'), true);
        },

        getFailureAlert: function() {
            return element(by.css('.fd-alert.fd-alert--error'));
        },

        getDropdownButtonByName: function(buttonName) {
            return element(by.cssContainingText('.se-dropdown-menu__list li', buttonName));
        },

        // Trashed Page List Items
        getDropdownPermanentlyDeleteButton: function() {
            return element(
                by.cssContainingText('.se-dropdown-menu__list li', 'Permanently Delete')
            );
        },

        getDropdownRestoreButton: function() {
            var item = element(by.cssContainingText('.se-dropdown-menu__list li', 'Restore'));
            browser.waitForPresence(
                item,
                'Expected Restore option to be available in the dropdown.'
            );

            return item;
        },

        getDropdownSyncPageStatusButton: function() {
            return element(by.cssContainingText('.se-dropdown-menu__list li', 'Sync'));
        },

        getRestrictionsForPage: function(pageName) {
            return pageList.elements
                .getRowByPageName(pageName)
                .element(by.css('restrictions-viewer'));
        },

        getRestrictionsInViewer: function() {
            return element.all(by.css('.se-restrictions-list .se-restriction__item'));
        }
    };

    pageList.actions = {
        navigateToFirstOnlineCatalogFromPageList: function() {
            browser.waitForPresence(
                element(by.css('.page-list-link-item a[data-ng-href*=Online]'))
            );
            return element(by.css('.page-list-link-item a[data-ng-href*=Online]')).click();
        },

        navigateToFirstStagedCatalogPageList: function() {
            var pageListLink = pageList.elements.getCatalogPageListLink('Staged');
            browser.waitForPresence(pageListLink);
            return browser.click(pageListLink);
        },

        moveToRestrictionsIconForAdvertisePage: function() {
            return browser
                .actions()
                .mouseMove(pageList.elements.getRestrictionsIconForAdvertisePage())
                .perform();
        },

        moveToRestrictionsIconForHomePage: function() {
            browser.waitForPresence(pageList.elements.getRestrictionsIconForHomePage());
            return browser
                .actions()
                .mouseMove(pageList.elements.getRestrictionsIconForHomePage())
                .perform();
        },

        moveToRestrictionsIconForPageById: function(id) {
            browser.waitForPresence(pageList.elements.getRestrictionsIconForPageById(id));
            return browser
                .actions()
                .mouseMove(pageList.elements.getRestrictionsIconForPageById(id))
                .perform();
        },

        openPageDropdownByPageName: function(pageName) {
            return browser
                .click(pageList.elements.getDropDownButtonByPageName(pageName))
                .then(function() {
                    return browser.waitForPresence(element(by.css('.se-dropdown-menu__list')));
                });
        },

        closePageDropdownByPageName: function(pageName) {
            return browser
                .actions()
                .mouseMove(pageList.elements.getDropDownButtonByPageName(pageName))
                .mouseMove({ x: -10, y: -10 })
                .click()
                .perform();
        },

        navigateToIndex: function(index) {
            return browser
                .executeScript('window.scrollTo(0,document.body.scrollHeight);')
                .then(function() {
                    return browser
                        .click(
                            element(by.cssContainingText('.fd-pagination__link', index.toString()))
                        )
                        .then(function() {
                            return browser.waitUntilNoModal();
                        });
                });
        },

        searchForPage: function(query, columnHeader, expectedNumber) {
            pageList.elements.getSearchInput().clear();
            pageList.elements.getSearchInput().sendKeys(query);

            pageList.assertions.totalPageCount(expectedNumber);
            expect(pageList.elements.getDisplayedPageCount()).toBe(expectedNumber);

            pageList.elements
                .getFirstRowForKey(columnHeader)
                .getText()
                .then(function(text) {
                    expect(text.toLowerCase().indexOf(query) >= 0).toBeTruthy();
                });
        },

        syncPageFromSyncModal: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            pageList.actions.openSyncModalFromActiveDropdown();
            browser.waitForAbsence(element(by.css('body > .modal.ng-animate')));
            syncPanel.checkItem('All Slots and Page Information');
            pageList.actions.clickSyncPageModalSyncButton();
            browser.waitForPresence(
                by.css(".se-sync-panel__sync-info__row.active span[data-status='IN_SYNC']")
            );
        },

        clickOnColumnHeader: function(key) {
            return browser
                .click(pageList.selectors.getColumnHeaderForKeySelector(key))
                .then(function() {
                    return browser.waitUntilNoModal();
                });
        },

        clickSyncPageModalSyncButton: function() {
            browser.click(pageList.elements.getClickableModalSyncPanelSyncButton());
        },

        openSyncModalFromActiveDropdown: function() {
            browser.waitForPresence(
                pageList.elements.getDropdownSyncButton(),
                'Expected sync option to be available in the dropdown.'
            );
            browser.click(
                pageList.elements.getDropdownSyncButton(),
                'Could not click on the sync option in the dropdown.'
            );
            browser.waitForPresence(
                pageList.elements.getModalDialog(),
                'Expected the presence of a modal window.'
            );
            browser.waitForPresence(
                pageList.elements.getModalSyncPanel(),
                'Expected the presence of a synchronization panel inside the modal.'
            );
        },

        openTrashedPageList: function() {
            return browser.click(pageList.elements.getPageListTrashLink());
        },

        clickOnTrashViewLink: function() {
            return browser.click(pageList.elements.getTrashViewLink());
        },

        bringTrashViewLinkIntoView: function() {
            return browser.bringElementIntoView(pageList.elements.getTrashViewLink());
        },

        // Trashed Page List Items
        permanentlyDeletePageByName: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            pageList.actions.showConfirmationModalForPermanentDelete();
            browser.waitForAbsence(element(by.css('body > .modal.ng-animate')));
            confirmationModal.actions.confirmConfirmationModal();
        },

        showConfirmationModalForPermanentDelete: function() {
            browser.waitForPresence(
                pageList.elements.getDropdownPermanentlyDeleteButton(),
                'Expected Permanently Delete option to be available in the dropdown.'
            );
            browser.click(
                pageList.elements.getDropdownPermanentlyDeleteButton(),
                'Could not click on the Permanently Delete option in the dropdown.'
            );
            browser.waitForPresence(
                pageList.elements.getConfirmationDialog(),
                'Expected the presence of confirmation modal window.'
            );
        },

        restorePageByName: function(pageName) {
            return pageList.actions.openPageDropdownByPageName(pageName).then(function() {
                return browser.click(
                    pageList.elements.getDropdownRestoreButton(),
                    'Could not click on the Restore option in the dropdown.'
                );
            });
        },

        //
        syncPageStatus: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            pageList.actions.clickSyncPageStatusButton();
            confirmationModal.actions.confirmConfirmationModal();
            browser.waitUntilNoModal();
        },

        clickSyncPageStatusButton: function() {
            browser.waitForPresence(
                pageList.elements.getDropdownSyncPageStatusButton(),
                'Expected Sync Page Status option to be available in the dropdown.'
            );
            browser.click(
                pageList.elements.getDropdownSyncPageStatusButton(),
                'Could not click on the Sync Page Status option in the dropdown.'
            );
            browser.waitForPresence(
                pageList.elements.getConfirmationDialog(),
                'Expected the presence of confirmation modal window.'
            );
        },
        // Restrictions Viewer
        openRestrictionsViewer: function(pageName) {
            return browser.click(pageList.elements.getRestrictionsForPage(pageName));
        }
    };

    pageList.assertions = {
        assertRestrictionIconForHomePageIsDisabled: function() {
            expect(
                pageList.elements.getRestrictionsIconForHomePage().getAttribute('data-ng-src')
            ).toContain('icon_restriction_small_grey.png');
        },
        assertPageListIsDisplayed: function() {
            expect(pageList.elements.getAddNewPageButton().isPresent()).toBe(true);
        },
        assertOnPageSyncIconStatusByPageIndex: function(pageIndex, expectedStatus) {
            browser.waitForPresence(
                element(by.css('.se-paged-list__table-body .se-paged-list-item'))
            );
            var actualStatus = null;
            browser.waitUntil(function() {
                return element(
                    by.css(
                        '.se-paged-list__table-body > tr:nth-child(' +
                            pageIndex +
                            ') page-list-sync-icon span'
                    )
                )
                    .getAttribute('data-sync-status')
                    .then(function(_actualStatus) {
                        actualStatus = _actualStatus;
                        return actualStatus.indexOf(expectedStatus) > -1;
                    });
            }, 'Expected sync status to be ' + expectedStatus + ' but got ' + actualStatus);
        },
        assertHasSynchronizableItems: function() {
            browser.waitUntil(function() {
                return pageList.elements
                    .getSynchronizableItemsForPage()
                    .count()
                    .then(function(count) {
                        return count > 0;
                    });
            }, 'Expected at least one synchronizable item for the page.');
        },
        assertHasSyncOptionAvailableOnDropdown: function() {
            browser.waitForPresence(
                pageList.elements.getDropdownSyncButton(),
                'Expected sync option to be available in the dropdown.'
            );
            expect(pageList.elements.getDropdownSyncButton().isPresent()).toBe(
                true,
                'Expected the presence of sync option in the dropdown menu'
            );
        },
        searchAndAssertCount: function(query, displayedResults, totalResults) {
            pageList.elements.getSearchInput().clear();
            pageList.elements.getSearchInput().sendKeys(query);
            browser.waitUntilNoModal().then(function() {
                browser.waitForPresence(pageList.elements.getTotalPageCountByValue(totalResults));
                browser.waitUntil(function() {
                    return pageList.elements
                        .getTotalPageCount()
                        .isPresent()
                        .then(function(isPresent) {
                            if (isPresent) {
                                return pageList.elements
                                    .getTotalPageCount()
                                    .getText()
                                    .then(function(text) {
                                        return text.indexOf(totalResults) !== -1;
                                    });
                            }
                            return false;
                        });
                });
                pageList.assertions.totalPageCount(totalResults);
                expect(pageList.elements.getDisplayedPageCount()).toBe(displayedResults);
            });
        },
        assertTotalTrashedpagesCountInButtonText: function(totalCount) {
            return browser.waitUntil(function() {
                return pageList.elements
                    .getPageListTrashLink()
                    .getText()
                    .then(function(buttonText) {
                        return buttonText.indexOf(totalCount);
                    });
            }, 'unexpected count of trashed pages');
        },
        trashPagesCountEquals: function(expectedCount) {
            expect(pageList.elements.getTrashViewLink().getText()).toBe(
                'TRASH (' + expectedCount + ' PAGES)'
            );
        },
        pageRowIsRenderedByPageName: function(pageName) {
            expect(pageList.elements.getRowByPageName(pageName)).toBeDisplayed();
        },
        pageRowIsNotRenderedByPageName: function(pageName) {
            expect(pageList.utils.getPageRowQuery(pageName)).toBeAbsent();
        },
        assertTrashFailure: function() {
            browser.waitToBeDisplayed(pageList.elements.getFailureAlert());
        },

        firstRowColumnContainText: function(column, value) {
            browser.waitForSelectorToContainText(
                pageList.selectors.getFirstRowForKeySelector(column),
                value
            );
        },

        lastRowColumnContainText: function(column, value) {
            browser.waitForSelectorToContainText(
                pageList.selectors.getLastRowForKeySelector(column),
                value
            );
        },

        totalPageCount: function(count) {
            browser.waitForPresence(
                element(by.css('.se-paged-list__page-count-wrapper')),
                'cannot find page list count item'
            );
            browser.waitForSelectorToContainText(
                pageList.selectors.getTotalPageCountSelector(),
                count
            );
        },

        restrictionTooltipToContain: function(count) {
            browser.waitForSelectorToContainText(
                pageList.selectors.getRestrictionsTooltipSelector(),
                count + ' restrictions'
            );
        },
        permanentlyDeleteButtonCanNotBeClicked: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            browser.waitForPresence(
                pageList.elements
                    .getDropdownPermanentlyDeleteButton()
                    .element(by.css('a.se-dropdown-item__disabled'))
            );
        },
        permanentlyDeleteButtonCanBeClicked: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            browser.waitForAbsence(
                pageList.elements
                    .getDropdownPermanentlyDeleteButton()
                    .element(by.css('a.se-dropdown-item__disabled'))
            );
        },
        assertOptionNotAvailableOnDropdown: function(buttonName) {
            browser.waitForAbsence(pageList.elements.getDropdownButtonByName(buttonName));
        },
        pageStatusComponentIsDisplayedInPageRow: function(pageName) {
            var pageRow = pageList.elements.getRowByPageName(pageName);
            pageStatus.assertions.pageStatusComponentIsDisplayed(pageRow);
        },
        pageStatusComponentIsNotDisplayedInPageRow: function(pageName) {
            var pageRow = pageList.elements.getRowByPageName(pageName);
            pageStatus.assertions.pageStatusComponentIsNotDisplayed(pageRow);
        },
        assertSyncStatusButtonNotAvailable: function(pageName) {
            pageList.actions.openPageDropdownByPageName(pageName);
            browser.waitForAbsence(pageList.elements.getDropdownSyncButton());
        },
        // Restrictions Viewer
        assertRestrictionsCountInViewer: function(pageName, expectedCount) {
            pageList.actions.openRestrictionsViewer(pageName);
            pageList.elements
                .getRestrictionsInViewer()
                .count()
                .then(function(count) {
                    expect(count).toBe(expectedCount);
                });
        }
    };

    pageList.utils = {
        getPageRowQuery: function(pageName) {
            return by.cssContainingText('.se-paged-list-item', pageName);
        }
    };

    return pageList;
})();
