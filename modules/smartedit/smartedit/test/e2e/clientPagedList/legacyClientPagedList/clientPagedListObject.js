/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

var PageList = function(PERSPECTIVE_SERVICE_RESULT) {
    PERSPECTIVE_SERVICE_RESULT = PERSPECTIVE_SERVICE_RESULT || false;
    this.pageURI =
        'test/e2e/clientPagedList/legacyClientPagedList/index.html?perspectiveServiceResult=' +
        PERSPECTIVE_SERVICE_RESULT;
    browser.get(this.pageURI);
};

PageList.prototype = {
    totalPageCount: function() {
        return element(by.css('.se-page-list__page-count span'));
    },
    displayedPageCount: function() {
        return element.all(by.css('.se-paged-list-table tbody tr')).count();
    },
    paginationCount: function() {
        return element.all(by.css('.pagination-container  > ul > li')).count();
    },
    searchInput: function() {
        return element(by.css('.page-list-search > input'));
    },
    columnHeaderForKey: function(key) {
        return element(
            by.css('.se-paged-list-table thead tr:first-child .se-paged-list__header-' + key)
        );
    },
    firstRowForKey: function(key) {
        return element(
            by.css('.se-paged-list-table tbody tr:first-child .se-paged-list-item-' + key)
        );
    },
    lastRowForKey: function(key) {
        return element(
            by.css('.se-paged-list-table tbody tr:last-child .se-paged-list-item-' + key)
        );
    },
    elemForKeyAndRow: function(key, row, selector) {
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
    catalogName: function() {
        return element(by.css('.se-page-list__catalog-name'));
    },
    openMoreMenuFirstElement: function() {
        return element
            .all(by.css('.se-dropdown-more-menu'))
            .first()
            .click();
    },
    getDropdownMenuItems: function(openedDropdownMenu) {
        return openedDropdownMenu.all(by.css('.se-dropdown-menu__list se-dropdown-menu-item a'));
    },
    clickOnFirstDropdownItemOfFirstElement: function() {
        var openedDropdownMenu = this.openMoreMenuFirstElement();
        browser.click(this.getDropdownMenuItems(openedDropdownMenu).first());
    }
};

module.exports = PageList;
