/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page Status - \n', function() {
    var workflow = e2e.componentObjects.workflow;
    var landingPage = e2e.pageObjects.landingPage;
    var sitesLink = e2e.componentObjects.sitesLink;
    var pageList = e2e.pageObjects.PageList;
    var pageStatus = e2e.componentObjects.pageStatus;
    var perspective = e2e.componentObjects.modeSelector;
    var popover = e2e.componentObjects.popover;
    var syncMenu = e2e.componentObjects.syncMenu;
    var syncPanel = e2e.componentObjects.synchronizationPanel;

    describe('Online Page - ', function() {
        beforeEach(function(done) {
            navigateToOnlinePage(done);
        });

        it('', function() {
            // Leaving empty for some suites fail at first case.
        });

        it('GIVEN page basic mode WHEN page is loaded THEN the page status should not be displayed', function() {
            // WHEN
            perspective.selectBasicPerspective();

            // THEN
            pageStatus.assertions.pageStatusComponentIsNotDisplayed();
        });

        it('GIVEN advanced mode WHEN page is loaded THEN the page status should not be displayed', function() {
            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageStatusComponentIsNotDisplayed();
        });
    });

    describe('Staged Page - ', function() {
        it('GIVEN page basic mode WHEN page is loaded THEN the page status should be displayed', function() {
            // GIVEN
            navigateToPage();

            // WHEN
            perspective.selectBasicPerspective();

            // THEN
            pageStatus.assertions.pageStatusComponentIsDisplayed();
        });

        it('GIVEN advanced mode WHEN page is loaded THEN the page status should be displayed', function() {
            // GIVEN
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageStatusComponentIsDisplayed();
        });

        it('GIVEN page is in draft WHEN page is loaded THEN the page status will be shown as draft', function() {
            // GIVEN
            pageStatus.utils.setMockPageDisplayStatus(pageStatus.constants.DRAFT_DISPLAY_STATUS);
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageIsInDraftStatus();
        });

        it('GIVEN page is in progress WHEN page is loaded THEN the page status will be as in progress', function() {
            // GIVEN
            pageStatus.utils.setMockPageDisplayStatus(
                pageStatus.constants.IN_PROGRESS_DISPLAY_STATUS
            );
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageIsInProgressStatus();
        });

        it('GIVEN page is ready to be synced WHEN page is loaded THEN the page status will be as ready to sync', function() {
            // GIVEN
            pageStatus.utils.setMockPageDisplayStatus(
                pageStatus.constants.READY_TO_SYNC_DISPLAY_STATUS
            );
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageIsInReadyToSyncStatus();
        });

        it('GIVEN page is synched WHEN page is loaded THEN the page status will be as synched', function() {
            // GIVEN
            pageStatus.utils.setMockPageDisplayStatus(pageStatus.constants.SYNCHED_DISPLAY_STATUS);
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.pageIsSyncedStatus();
        });

        it('GIVEN user is not part of current workflow WHEN page is loaded THEN the page status is shown with lock', function() {
            // GIVEN
            workflow.utils.setWorkflowAvailableToCurrentUser(false);
            navigateToPage();

            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageStatus.assertions.workflowIsUnavailableToCurrentUser();
        });

        it('GIVEN page has been synched before WHEN page is hovered THEN a popover is shown with the last sync date', function() {
            // GIVEN
            workflow.utils.setWorkflowAvailableToCurrentUser(true);
            navigateToPage();
            perspective.selectAdvancedPerspective();

            // WHEN
            pageStatus.actions.hoverPageStatus();

            // THEN
            popover.assertions.isDisplayedWithProvidedText('Last Published 11/10/16 1:10 PM');
        });

        it('GIVEN page display status changes WHEN page is synchronized THEN the status is refreshed appropriately', function() {
            // GIVEN
            pageStatus.utils.setMockPageDisplayStatus(
                pageStatus.constants.READY_TO_SYNC_DISPLAY_STATUS
            );
            navigateToPage();
            perspective.selectAdvancedPerspective();

            // WHEN
            pageStatus.utils.setMockPageDisplayStatus(pageStatus.constants.SYNCHED_DISPLAY_STATUS);
            syncPage();

            // THEN
            pageStatus.assertions.pageIsSyncedStatus();
        });
    });

    describe('Page list - ', function() {
        it('WHEN navigating to the page list of an online catalog THEN the page status is not displayed', function() {
            // WHEN
            navigateToOnlinePageList();

            // THEN
            pageList.assertions.pageStatusComponentIsNotDisplayedInPageRow('homepage');
        });

        it('WHEN navigating to a staged page list of an online catalog THEN the page status is displayed', function() {
            // WHEN
            navigateToStagedPageList();

            // THEN
            pageList.assertions.pageStatusComponentIsDisplayedInPageRow('homepage');
        });
    });

    function navigateToPage(done) {
        return browser.bootstrap(__dirname).then(function() {
            return browser.waitForWholeAppToBeReady().then(function() {
                if (done) {
                    done();
                }
            });
        });
    }

    function navigateToOnlinePage(done) {
        return navigateToPage().then(function() {
            return browser.waitUntilNoModal().then(function() {
                sitesLink.actions.openSitesPage();
                landingPage.actions.navigateToCatalogHomepage(
                    landingPage.constants.APPAREL_UK_CATALOG,
                    landingPage.constants.ACTIVE_CATALOG_VERSION
                );
                browser.waitForWholeAppToBeReady();
                done();
            });
        });
    }

    function navigateToOnlinePageList() {
        return navigateToPage().then(function() {
            sitesLink.actions.openSitesPage();
            landingPage.actions.navigateToCatalogPageList(
                landingPage.constants.APPAREL_UK_CATALOG,
                landingPage.constants.ACTIVE_CATALOG_VERSION
            );
        });
    }

    function navigateToStagedPageList() {
        return navigateToPage().then(function() {
            sitesLink.actions.openSitesPage();
            landingPage.actions.navigateToCatalogPageList(
                landingPage.constants.APPAREL_UK_CATALOG,
                landingPage.constants.STAGED_CATALOG_VERSION
            );
        });
    }

    function syncPage() {
        syncMenu.actions.click();
        syncPanel.actions.checkSyncCheckbox('All');
        syncPanel.clickSync();
        browser.waitUntilNoModal();
    }
});
