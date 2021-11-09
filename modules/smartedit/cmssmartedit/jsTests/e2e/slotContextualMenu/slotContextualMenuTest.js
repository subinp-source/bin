/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('slotContextualMenu', function() {
    var perspective = e2e.componentObjects.modeSelector;
    var storefront = e2e.componentObjects.storefront;
    var modalDialog = e2e.componentObjects.yModalDialog;
    var editorModal = e2e.componentObjects.editorModal;
    var slotContextualMenu = e2e.componentObjects.slotContextualMenu;
    var typePermissions = e2e.componentObjects.typePermissions;
    var TOP_HEADER_SLOT_ID = storefront.constants.TOP_HEADER_SLOT_ID;
    var BOTTOM_HEADER_SLOT_ID = storefront.constants.BOTTOM_HEADER_SLOT_ID;
    var backendClient = e2e.mockBackendClient;

    beforeEach(function() {
        browser.bootstrap(__dirname);
    });

    beforeEach(function(done) {
        browser.waitForWholeAppToBeReady().then(function() {
            done();
        });
    });

    describe('slotVisibilityButton', function() {
        beforeEach(function(done) {
            perspective.selectAdvancedPerspective().then(function() {
                done();
            });
        });

        it('WHEN the user hovers over the slot with hidden components THEN the visibility button is displayed with the number of hidden components.', function() {
            storefront.actions.moveToComponent(TOP_HEADER_SLOT_ID);
            var button = slotContextualMenu.elements.visibilityButtonBySlotId(TOP_HEADER_SLOT_ID);
            expect(button.isPresent()).toBe(true);
            expect(button.isDisplayed()).toBe(true);
            expect(button.getText()).toBe('2');
        });

        it('WHEN the user clicks on the visibility button THEN the hidden component list is visible.', function() {
            storefront.actions.moveToComponent(TOP_HEADER_SLOT_ID);
            browser.click(slotContextualMenu.elements.visibilityButtonBySlotId(TOP_HEADER_SLOT_ID));
            var list = slotContextualMenu.elements.visibilityDropdownBySlotId(TOP_HEADER_SLOT_ID);
            var hiddenElementsList = slotContextualMenu.elements.visibilityListBySlotId(
                TOP_HEADER_SLOT_ID
            );

            expect(list.isPresent()).toBe(true);
            expect(list.isDisplayed()).toBe(true);
            expect(hiddenElementsList.get(0).getText()).toMatch(/Hidden Component 1/i);
            expect(hiddenElementsList.get(0).getText()).toMatch(/ABSTRACTCMSCOMPONENT/i);
            expect(hiddenElementsList.get(1).getText()).toMatch(/Hidden Component 2/i);
            expect(hiddenElementsList.get(1).getText()).toMatch(/ABSTRACTCMSCOMPONENT/i);
        });

        it('WHEN the user clicks on the clone button of hidden components, then appropriate data should be visible', function() {
            // GIVEN
            slotContextualMenu.actions.openHiddenComponentsList(TOP_HEADER_SLOT_ID);

            // WHEN
            slotContextualMenu.actions.openFirstHiddenComponentMenu(TOP_HEADER_SLOT_ID);
            slotContextualMenu.actions.clickHiddenComponentMenuItemByLabel('Clone');

            // THEN
            expect(editorModal.elements.getAttributeValueByName('name')).toContain(
                'Clone of Hidden Component 1'
            );

            editorModal.actions.modalDialogClickCancel();
            modalDialog.actions.modalDialogClickOk();
            editorModal.assertions.assertModalIsNotPresent();

            // GIVEN
            slotContextualMenu.actions.openHiddenComponentsList(TOP_HEADER_SLOT_ID);

            // WHEN
            slotContextualMenu.actions.openHiddenComponentMenu(
                TOP_HEADER_SLOT_ID,
                slotContextualMenu.constants.HIDDEN_COMPONENT_2
            );
            slotContextualMenu.actions.clickHiddenComponentMenuItemByLabel('Clone');

            // THEN
            expect(editorModal.elements.getAttributeValueByName('name')).toContain(
                'Clone of Hidden Component 2'
            );
        });

        it('GIVEN hidden non-shared component WHEN hidden components list is shown THEN it does not have a shared component indicator', function() {
            // GIVEN
            slotContextualMenu.actions.openHiddenComponentsList(TOP_HEADER_SLOT_ID);

            // WHEN/THEN
            slotContextualMenu.assertions.assertHiddenComponentIsNotShared(
                TOP_HEADER_SLOT_ID,
                slotContextualMenu.constants.HIDDEN_COMPONENT_1
            );
        });

        it('GIVEN hidden shared component WHEN hidden components list is shown THEN it has a shared component indicator', function() {
            // GIVEN
            slotContextualMenu.actions.openHiddenComponentsList(TOP_HEADER_SLOT_ID);

            // WHEN/THEN
            slotContextualMenu.assertions.assertHiddenComponentIsShared(
                TOP_HEADER_SLOT_ID,
                slotContextualMenu.constants.HIDDEN_COMPONENT_2
            );
        });
    });

    describe('slotSharedButton', function() {
        beforeAll(function() {
            backendClient.replaceFixture(
                [
                    '/cmswebservices/v1/sites/apparel-uk/catalogs/apparel-ukContentCatalog/versions/Staged/pagescontentslots'
                ],
                {
                    pageContentSlotList: [
                        {
                            pageId: 'homepage',
                            position: 'topHeader',
                            slotId: 'topHeaderSlot',
                            slotShared: true,
                            slotStatus: 'TEMPLATE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'bottomHeader',
                            slotId: 'bottomHeaderSlot',
                            slotShared: false,
                            slotStatus: 'OVERRIDE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'footer',
                            slotId: 'footerSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'other',
                            slotId: 'otherSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        }
                    ]
                }
            );
        });

        beforeEach(function() {
            perspective.select(perspective.ADVANCED_CMS_PERSPECTIVE);
        });

        it('WHEN the user hovers over the shared slot THEN the shared slot button is visible', function() {
            // GIVEN
            storefront.actions.moveToComponent(storefront.constants.TOP_HEADER_SLOT_ID);

            // WHEN
            var sharedButton = slotContextualMenu.elements.sharedSlotButtonBySlotId(
                TOP_HEADER_SLOT_ID
            );

            // THEN
            expect(sharedButton.isPresent()).toBe(
                true,
                'Expected shared slot button to be displayed'
            );
        });

        it('WHEN the user hovers over a non-shared slot THEN the slot shared button should not be visible', function() {
            slotContextualMenu.assertions.assertThatSlotShareButtonIsNotPresent(
                BOTTOM_HEADER_SLOT_ID
            );
        });

        it('WHEN the user hovers on the shared slot button THEN the button should open a dropdown menu that contains a message and clone links', function() {
            storefront.actions.moveToComponent(TOP_HEADER_SLOT_ID);
            slotContextualMenu.actions.openSharedSlotButtonByButtonId(TOP_HEADER_SLOT_ID);
            expect(slotContextualMenu.elements.sharedSlotButtonMessage()).toContain(
                'This slot is shared, any changes you make will affect other pages using the same slot.'
            );

            var cloneLinks = slotContextualMenu.elements.sharedSlotMessageButtonCloneLinksBySlotId(
                TOP_HEADER_SLOT_ID
            );
            expect(cloneLinks.isPresent()).toBe(
                true,
                'Expected convert with cloning components button to be present'
            );
        });
    });

    describe('slotSharedButton', function() {
        beforeAll(function() {
            backendClient.replaceFixture(
                [
                    '/cmswebservices/v1/sites/apparel-uk/catalogs/apparel-ukContentCatalog/versions/Staged/pagescontentslots'
                ],
                {
                    pageContentSlotList: [
                        {
                            pageId: 'homepage',
                            position: 'topHeader',
                            slotId: 'topHeaderSlot',
                            slotShared: true,
                            slotStatus: 'TEMPLATE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'bottomHeader',
                            slotId: 'bottomHeaderSlot',
                            slotShared: false,
                            slotStatus: 'OVERRIDE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'footer',
                            slotId: 'footerSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'other',
                            slotId: 'otherSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        }
                    ]
                }
            );
        });

        beforeEach(function() {
            typePermissions.setTypePermission('contentSlotTypePermissions', {
                read: true,
                change: true,
                create: false,
                remove: true
            });
            perspective.select(perspective.ADVANCED_CMS_PERSPECTIVE);
        });

        it('GIVEN user has create permissions on the slot WHEN the user opens the shared slot icon THEN the slot override links should not be visible', function() {
            storefront.actions.moveToComponent(TOP_HEADER_SLOT_ID);
            slotContextualMenu.actions.openSharedSlotButtonByButtonId(TOP_HEADER_SLOT_ID);
            slotContextualMenu.assertions.assertThatSlotSharedButtonOverrideLinksToBeAbsent(
                TOP_HEADER_SLOT_ID
            );
        });
    });

    describe('slotUnsharedButton', function() {
        beforeAll(function() {
            backendClient.replaceFixture(
                [
                    '/cmswebservices/v1/sites/apparel-uk/catalogs/apparel-ukContentCatalog/versions/Staged/pagescontentslots'
                ],
                {
                    pageContentSlotList: [
                        {
                            pageId: 'homepage',
                            position: 'topHeader',
                            slotId: 'topHeaderSlot',
                            slotShared: true,
                            slotStatus: 'TEMPLATE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'bottomHeader',
                            slotId: 'bottomHeaderSlot',
                            slotShared: false,
                            slotStatus: 'OVERRIDE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'footer',
                            slotId: 'footerSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        },
                        {
                            pageId: 'homepage',
                            position: 'other',
                            slotId: 'otherSlot',
                            slotShared: false,
                            slotStatus: 'PAGE'
                        }
                    ]
                }
            );
        });

        beforeEach(function() {
            perspective.select(perspective.ADVANCED_CMS_PERSPECTIVE);
        });

        it('WHEN the user hovers over a shared slot THEN the slot unshared button should not be visible', function() {
            slotContextualMenu.assertions.assertThatSlotUnsharedButtonIsNotPresent(
                TOP_HEADER_SLOT_ID
            );
        });

        it('WHEN the user hovers over an unshared slot THEN the unshared button should be visible', function() {
            slotContextualMenu.assertions.assertThatSlotUnsharedButtonIsPresent(
                BOTTOM_HEADER_SLOT_ID
            );
        });
    });

    afterAll(function() {
        backendClient.removeAllFixtures();
    });
});
