/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page List', function() {
    var FIRST_CATALOG_NAME = 'Apparel UK Content Catalog - Staged';

    var landingPage = e2e.pageObjects.landingPage;
    var pageList = e2e.pageObjects.PageList;
    var sitesLink = e2e.componentObjects.sitesLink;

    beforeEach(function() {
        browser.bootstrap(__dirname);
    });

    beforeEach(function(done) {
        landingPage.actions
            .navigateToFirstStagedCatalogPageList()
            .then(function() {
                return browser.waitForContainerToBeReady();
            })
            .then(function() {
                return browser.waitUntilNoModal();
            })
            .then(function() {
                done();
            });
    });

    it('GIVEN I am on the page list of the first catalog WHEN the page is fully loaded THEN I expect to see a paginated list of 10 pages max, sorted by name ascending', function() {
        expect(pageList.elements.getDisplayedPageCount()).toBe(
            10,
            'Expected the number of page displayed to be 10'
        );
        pageList.assertions.firstRowColumnContainText('name', 'Homepage');

        pageList.actions.navigateToIndex(2);
        pageList.assertions.lastRowColumnContainText('name', 'Third Page');
        expect(pageList.elements.getPaginationCount()).toBe(2, 'Expected pagination count to be 2');
    });

    it('GIVEN I am on the page list of the first catalog WHEN I search for a page THEN I expect the list to show the pages that match the query for name or uid', function() {
        pageList.assertions.searchAndAssertCount('homepage', 1, 1);
        expect(pageList.elements.getPaginationCount()).toBe(1, 'Expected pagination count to be 1');

        // Perform a search on a page UID
        pageList.assertions.searchAndAssertCount('variationCategoryPage', 1, 1);
    });

    it('GIVEN I am on the page list of the first catalog WHEN I search for a page and clear the filter THEN I expect the list all pages again and the page count increase', function() {
        pageList.assertions.searchAndAssertCount('homepage', 1, 1);
        pageList.assertions.totalPageCount(1);
        pageList.elements.clearSearchFilter().then(function() {
            pageList.assertions.totalPageCount(15);
        });
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', function() {
        pageList.actions.clickOnColumnHeader('name');
        pageList.assertions.firstRowColumnContainText('name', 'Third Page');
        pageList.actions.navigateToIndex(2);
        pageList.assertions.lastRowColumnContainText('name', 'Homepage');
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the UID column header THEN I expect the list to be re-sorted by this key in the descending order', function() {
        pageList.actions.clickOnColumnHeader('uid');
        pageList.assertions.firstRowColumnContainText('uid', 'variationProductPage');
        pageList.actions.navigateToIndex(2);
        pageList.assertions.lastRowColumnContainText('uid', 'auid1');
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the page type column header THEN I expect the list to be re-sorted by this key in the descending order', function() {
        pageList.actions.clickOnColumnHeader('itemtype');
        pageList.assertions.firstRowColumnContainText('itemtype', 'ProductPage');
        pageList.actions.navigateToIndex(2);
        pageList.assertions.lastRowColumnContainText('itemtype', 'CategoryPage');
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the name column header THEN I expect the list to not reorder as it is not sortable', function() {
        pageList.actions.clickOnColumnHeader('masterTemplateId');
        pageList.assertions.firstRowColumnContainText('masterTemplateId', 'AccountPageTemplate');
        pageList.actions.navigateToIndex(2);
        pageList.assertions.lastRowColumnContainText('masterTemplateId', 'ProductPageTemplate');
    });

    it('GIVEN I am on the page list of the first catalog WHEN the page is fully loaded THEN I expect to see the catalog name and catalog version', function() {
        expect(pageList.elements.getCatalogName().getText()).toBe(FIRST_CATALOG_NAME);
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on a linkable page name THEN I expect to be redirected to this page', function() {
        var EXPECTED_IFRAME_SRC = '/jsTests/e2e/storefront.html?cmsTicketId=dasdfasdfasdfa';
        var EXPECTED_BROWSER_URL = '/storefront';

        pageList.elements.getLinkForKeyAndRow('name', 1, 'a').click();
        browser.waitForWholeAppToBeReady();
        browser.switchToParent();

        var iframe = element(by.css('#js_iFrameWrapper iframe', 'iFrame not found'));
        expect(iframe.getAttribute('src')).toContain(EXPECTED_IFRAME_SRC);
        expect(browser.getCurrentUrl()).toContain(EXPECTED_BROWSER_URL);
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on Sites link THEN I expect to go to sites page', function() {
        sitesLink.actions.openSitesPage();
        sitesLink.assertions.waitForUrlToMatch();
    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the number of restrictions for a page with restrictions THEN I expect to see a modal with list of restrictions', function() {
        pageList.assertions.assertRestrictionsCountInViewer('Homepage', 2);
    });
});

describe('Synchronization modal in page list', function() {
    var pageList = e2e.pageObjects.PageList;
    var landingPage = e2e.pageObjects.landingPage;
    var workflow = e2e.componentObjects.workflow;

    beforeEach(function() {
        workflow.utils.setNumberOfWorkflowsToReturn(0);
        browser.bootstrap(__dirname);
    });

    beforeEach(function() {
        landingPage.actions.navigateToFirstStagedCatalogPageList();
        pageList.actions.openPageDropdownByPageName('Homepage');
        browser.waitForContainerToBeReady();
    });

    it('GIVEN I am on the page list of the first staged catalog WHEN the page is fully loaded THEN I expect the sync page modal to be openable from the dropdown.', function() {
        pageList.actions.openSyncModalFromActiveDropdown();
        expect(pageList.elements.getModalSyncPanel().isPresent()).toBe(
            true,
            'Expected the presence of a synchronization panel inside a modal.'
        );
    });

    it('GIVEN I am on the page list of the first staged catalog WHEN sync page modal is opened THEN I expect a list of synchronizable items and a sync button to display', function() {
        pageList.actions.openSyncModalFromActiveDropdown();
        pageList.assertions.assertHasSynchronizableItems();

        // is it okay to declare is variable
        var syncButton = pageList.elements.getModalSyncPanelSyncButton();
        expect(syncButton.isPresent()).toBe(
            true,
            'Expected the presence of a sync button in the synchronization modal.'
        );
    });
});

describe('Options in dropdown', function() {
    var pageList = e2e.pageObjects.PageList;
    var landingPage = e2e.pageObjects.landingPage;
    var syncPanel = e2e.componentObjects.synchronizationPanel;
    var workflow = e2e.componentObjects.workflow;

    describe('Page is not in a workflow', function() {
        beforeEach(function() {
            browser.bootstrap(__dirname);
        });

        beforeEach(function() {
            landingPage.actions.navigateToFirstStagedCatalogPageList();
            syncPanel.setupTest();
            browser.waitForContainerToBeReady();
            browser.waitUntilNoModal();
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu THEN I expect to see sync option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            browser.waitForVisibility(
                pageList.elements.getDropdownSyncButton(),
                'Expected sync option to be available in the dropdown.'
            );
            expect(pageList.elements.getDropdownSyncButton().isPresent()).toBe(
                true,
                'Expected the presence of sync option in the dropdown menu'
            );
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu THEN I expect to see edit option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            expect(pageList.elements.getDropdownEditButton().isPresent()).toBe(
                true,
                'Expected the presence of edit option in the dropdown menu'
            );
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu THEN I expect to move to trash option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            expect(pageList.elements.getDropdownMoveToTrashButton().isPresent()).toBe(
                true,
                'Expected the presence of move to trash option in the dropdown menu'
            );
        });
    });

    describe('Page is in a workflow', function() {
        beforeEach(function() {
            workflow.utils.setNumberOfWorkflowsToReturn(1);
            workflow.utils.setWorkflowAvailableToCurrentUser(false);
            browser.bootstrap(__dirname);
        });

        beforeEach(function() {
            landingPage.actions.navigateToFirstStagedCatalogPageList();
            syncPanel.setupTest();
            browser.waitForContainerToBeReady();
            browser.waitUntilNoModal();
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu for a page that is in a workflow AND current user is not a participant THEN I expect not to see sync option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            expect(browser.isAbsent(pageList.elements.getDropdownSyncButton())).toBe(
                true,
                'Expected the absence of sync option in the dropdown menu'
            );
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu for a page that is in a workflow AND current user is not a participant THEN I expect not to see edit option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            expect(browser.isAbsent(pageList.elements.getDropdownEditButton())).toBe(
                true,
                'Expected the absence of edit option in the dropdown menu'
            );
        });

        it('GIVEN I am on the page list of the first staged catalog WHEN I open the drop down menu for a page that is in a workflow AND current user is not a participant THEN I expect not to move to trash option on dropdown', function() {
            pageList.actions.openPageDropdownByPageName('homepage');
            expect(browser.isAbsent(pageList.elements.getDropdownMoveToTrashButton())).toBe(
                true,
                'Expected the absence of move to trash option in the dropdown menu'
            );
        });
    });
});

describe('Soft deletion of a page\n', function() {
    var confirmationModal = e2e.componentObjects.confirmationModal;
    var deletePageItem = e2e.componentObjects.deletePageItem;
    var landingPage = e2e.pageObjects.landingPage;
    var pageList = e2e.pageObjects.PageList;
    var popover = e2e.componentObjects.popover;
    var syncPanel = e2e.componentObjects.synchronizationPanel;
    var alerts = e2e.componentObjects.alerts;
    var workflow = e2e.componentObjects.workflow;

    var DELETABLE_PAGE_NAME = 'My Little Variation Category Page';
    var NON_DELETABLE_PAGE_NAME = 'Homepage';
    var VALIDATION_ERROR_PAGE_NAME = 'My Little Primary Category Page';

    beforeEach(function() {
        workflow.utils.setNumberOfWorkflowsToReturn(0);
        browser.bootstrap(__dirname);
        landingPage.actions.navigateToFirstStagedCatalogPageList();
        syncPanel.setupTest();
        browser.waitForContainerToBeReady();
    });

    it(
        'GIVEN I am on a page list view\n' +
            'WHEN I open the drop down menu of a non-deletable page\n' +
            'THEN I expect a "Move to trash" option to be displayed inactive\n' +
            'AND I expect a popover to get rendered on hover with a meaningful message.',
        function() {
            pageList.actions.openPageDropdownByPageName(NON_DELETABLE_PAGE_NAME);
            deletePageItem.assertions.deletePageItemIsInactive();
            deletePageItem.actions.hoverDeletePageItemAnchor();
            popover.assertions.isDisplayedWithProvidedText(
                'This is the homepage. It cannot be moved to trash.'
            );
        }
    );

    it(
        'GIVEN I am on a page list view\n' +
            'WHEN I open the drop down menu of a deletable page\n' +
            'THEN I expect a "Move to trash" option to be displayed active\n' +
            'AND I expect no popover anchor to be found for the "move to trash" option.',
        function() {
            pageList.actions.openPageDropdownByPageName(DELETABLE_PAGE_NAME);
            deletePageItem.assertions.deletePageItemIsActive();
            deletePageItem.assertions.deletePageItemPopoverAnchorIsNotPresent();
        }
    );

    it(
        'GIVEN I am on a page list view\n' +
            'WHEN I click on the "move to trash" option of a deletable page\n' +
            'THEN I expect this page not be displayed in the updated page list\n' +
            'AND I expect the count of trashed page to be automatically updated\n' +
            'AND I expect this page to be displayed in the trash page view.',
        function() {
            pageList.assertions.trashPagesCountEquals(3);

            pageList.actions.openPageDropdownByPageName(DELETABLE_PAGE_NAME);
            deletePageItem.actions.clickOnDeletePageAnchor();
            confirmationModal.actions.confirmConfirmationModal();
            alerts.actions.flush();
            pageList.actions.bringTrashViewLinkIntoView();
            pageList.assertions.pageRowIsNotRenderedByPageName(DELETABLE_PAGE_NAME);
            pageList.assertions.trashPagesCountEquals(4);

            pageList.actions.clickOnTrashViewLink();
            pageList.assertions.pageRowIsRenderedByPageName(DELETABLE_PAGE_NAME);
        }
    );

    it(
        'GIVEN I am on a page list view\n' +
            'WHEN I open the drop down menu of a page that has some validation error and delete it\n' +
            'THEN I expect to see a validation error\n',
        function() {
            pageList.assertions.trashPagesCountEquals(3);

            pageList.actions.openPageDropdownByPageName(VALIDATION_ERROR_PAGE_NAME);
            deletePageItem.actions.clickOnDeletePageAnchor();
            confirmationModal.actions.confirmConfirmationModal();
            pageList.assertions.assertTrashFailure();

            alerts.actions.flush();
            pageList.actions.bringTrashViewLinkIntoView();
            pageList.assertions.trashPagesCountEquals(3);
        }
    );
});
