/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page sync toolbar menu test - ', function() {
    var syncMenu = e2e.componentObjects.syncMenu;
    var synchronizationPanel = e2e.componentObjects.synchronizationPanel;
    var sfBuilder = e2e.se.componentObjects.sfBuilder;
    var backendClient = e2e.mockBackendClient;

    beforeEach(function() {
        browser.bootstrap(__dirname);
    });

    describe('Page sync toolbar menu with sync permissions AND approved page', function() {
        beforeEach(function(done) {
            setPageAsDraft(false);
            syncMenu.actions.prepareApp(done, {
                canSynchronize: true,
                targetCatalogVersion: 'Online'
            });
        });

        it('GIVEN advanced edit mode is on, WHEN we click on sync toolbar entry THEN a sync panel shows along with a sync button', function() {
            //find and click on sync toolbar entry
            syncMenu.actions.click();
            //assess that sync panel shows
            syncMenu.assertions.syncPanelIsPresent();
            //assess that it is related to pages
            syncMenu.assertions.syncPanelHeaderContains(
                'Synchronize page information, associations and content slots, except shared content slots'
            );
            //assess the sync icon is truly present
            syncMenu.assertions.syncCautionIconIsDisplayed();
            //check the page's checkbox to select all items to sync
            synchronizationPanel.checkItem('All Slots and Page Information');
            //assess that a sync button is clickable
            synchronizationPanel.assertions.assertSyncButtonIsEnabled();
        });

        it('GIVEN the page that has not been sync with target catalog version WHEN we click on sync toolbar entry THEN a sync panel shows without item list and with a enabled sync button', function() {
            // GIVEN
            sfBuilder.actions.changePageIdAndCatalogVersion(
                'secondpage',
                'apparel-ukContentCatalog/Staged'
            );

            // WHEN
            syncMenu.actions.click();

            // THEN
            syncMenu.assertions.syncPanelIsPresent();
            syncMenu.assertions.syncPanelMessageContains(
                'Sync it so that it is available in the Apparel UK Content Catalog - Online'
            );
            syncMenu.assertions.syncCautionIconIsDisplayed();
            synchronizationPanel.assertions.assertItemListIsHidden();
            synchronizationPanel.assertions.assertSyncButtonIsEnabled();
        });

        it('GIVEN the page that has not been sync with target catalog version WHEN we click on sync toolbar entry THEN a sync panel shows without item list and with a enabled sync button', function() {
            // GIVEN
            sfBuilder.actions.changePageIdAndCatalogVersion(
                'otherpage',
                'apparel-ukContentCatalog/Staged'
            );

            // WHEN
            syncMenu.actions.click();

            // THEN
            syncMenu.assertions.syncPanelIsPresent();
            syncMenu.assertions.syncPanelMessageContains(
                'se.cms.synchronization.page.unavailable.items.description'
            );
            syncMenu.assertions.syncCautionIconIsDisplayed();
            synchronizationPanel.assertions.assertItemListIsHidden();
            synchronizationPanel.assertions.assertSyncButtonIsDisabled();
        });

        it('GIVEN the page that has not been sync with the target catalog version AND a sync panel is without item list AND with a enabled sync button WHEN we click on sync button THEN item list is shown and sync button is disabled', function() {
            // GIVEN
            sfBuilder.actions.changePageIdAndCatalogVersion(
                'secondpage',
                'apparel-ukContentCatalog/Staged'
            );
            syncMenu.actions.click();
            syncMenu.assertions.syncPanelIsPresent();
            // WHEN
            synchronizationPanel.clickSync();
            // THEN
            syncMenu.assertions.syncCautionIconIsHidden();
            synchronizationPanel.assertions.assertItemListIsVisible();
            synchronizationPanel.assertions.assertSyncButtonIsDisabled();
        });

        it('GIVEN advanced edit mode is on an old homepage WHEN we click on sync toolbar entry THEN then a tooltip should be present next the synchronization title AND the first item should be disabled.', function() {
            // given an old homepage
            sfBuilder.actions.changePageIdAndCatalogVersion(
                'thirdpage',
                'apparel-ukContentCatalog/Staged'
            );
            //find and click on sync toolbar entry
            syncMenu.actions.click();
            //assess that sync panel shows
            syncMenu.assertions.syncPanelIsPresent();
            //assert homepage sync message is present
            syncMenu.assertions.syncPanelHeaderContains(
                'To enable sync for all slots and page information, sync new homepage.'
            );
            //assert tooltip is there in the header
            syncMenu.assertions.syncPanelTitleTooltipToBePresent(true);
            //assert first item is disabled.
            synchronizationPanel.assertions.assertSyncCheckboxDisabledForSlot(
                'All Slots and Page Information'
            );
        });

        it('GIVEN the page in content catalog which has sync permissions THEN sync toolbar menu button should be displayed', function() {
            //THEN
            syncMenu.assertions.syncToolbarButtonToBePresent();
        });

        it('WHEN sync toolbar entry is opened THEN the panel is not read-only AND a warning message is not displayed', function() {
            // WHEN
            syncMenu.actions.click();

            // THEN
            syncMenu.assertions.syncPanelIsPresent();
            synchronizationPanel.assertions.assertSyncNotAllowedMessageIsAbsent();
        });
    });

    describe('Page sync toolbar menu with sync permissions AND unapproved page', function() {
        beforeEach(function(done) {
            setPageAsDraft(true);
            syncMenu.actions.prepareApp(done, {
                canSynchronize: true,
                targetCatalogVersion: 'Online'
            });
        });

        it('GIVEN page has been synched before AND is out of sync WHEN sync toolbar entry is opened THEN the panel is read-only AND a warning message is displayed', function() {
            // WHEN
            syncMenu.actions.click();

            // THEN
            syncMenu.assertions.syncPanelIsPresent();
            synchronizationPanel.assertions.assertSyncNotAllowedMessageIsPresent();
            synchronizationPanel.assertions.assertSyncButtonIsDisabled();
        });

        it('GIVEN page has not been synched before WHEN sync toolbar entry is opened THEN the pagel is read-only AND a warning message is displayed', function() {
            // GIVEN
            sfBuilder.actions.changePageIdAndCatalogVersion(
                'secondpage',
                'apparel-ukContentCatalog/Staged'
            );

            // WHEN
            syncMenu.actions.click();

            // THEN
            syncMenu.assertions.syncPanelIsPresent();
            syncMenu.assertions.syncPanelMessageContains(
                'Sync it so that it is available in the Apparel UK Content Catalog - Online'
            );
            synchronizationPanel.assertions.assertSyncNotAllowedMessageIsPresent();
            synchronizationPanel.assertions.assertItemListIsHidden();
            synchronizationPanel.assertions.assertSyncButtonIsDisabled();
        });

        it('GIVEN the page in content catalog which has sync permissions THEN sync toolbar menu button should be displayed', function() {
            //THEN
            syncMenu.assertions.syncToolbarButtonToBePresent();
        });
    });

    describe('Page sync toolbar menu without sync permissions', function() {
        beforeAll(function() {
            backendClient.modifyFixture(
                ['permissionswebservices\\/v1\\/permissions\\/principals\\/cmsmanager\\/catalogs'],
                {
                    'permissionsList.0.syncPermissions': [{}]
                }
            );
        });

        beforeEach(function(done) {
            setPageAsDraft(false);
            syncMenu.actions.prepareApp(done, {});
        });

        it('GIVEN the page in content catalog which has no sync permissions THEN sync toolbar menu button should not be displayed', function() {
            //THEN
            syncMenu.assertions.syncToolbarButtonToBeAbsent();
        });

        afterAll(function() {
            backendClient.removeAllFixtures();
        });
    });

    function setPageAsDraft(approve) {
        browser.executeScript(
            'window.sessionStorage.setItem("setPageAsDraft", arguments[0])',
            approve
        );
    }
});
