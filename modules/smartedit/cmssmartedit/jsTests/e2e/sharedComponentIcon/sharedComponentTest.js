/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Shared Component', function() {
    var contextualMenu = e2e.componentObjects.componentContextualMenu;
    var storefront = e2e.componentObjects.storefront;
    var perspective = e2e.componentObjects.modeSelector;

    var NON_SHARED_COMPONENT_ID = 'component1';
    var SHARED_COMPONENT_ID = 'component3';

    beforeEach(function(done) {
        browser.bootstrap(__dirname);
        browser.waitForWholeAppToBeReady().then(function() {
            done();
        });
    });

    describe(' basic edit mode - ', function() {
        beforeEach(function(done) {
            perspective.selectBasicPerspective().then(function() {
                done();
            });
        });

        it('GIVEN component is not shared WHEN hovered THEN it must not have a shared component contextual button', function() {
            // GIVEN
            storefront.actions.moveToComponent(NON_SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.assertions.assertSharedComponentButtonIsNotPresent(
                NON_SHARED_COMPONENT_ID
            );
        });

        it('GIVEN component is shared WHEN hovered THEN it must have a shared component icon', function() {
            // GIVEN
            storefront.actions.moveToComponent(SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.assertions.assertSharedComponentButtonIsPresent(SHARED_COMPONENT_ID);
        });

        it('GIVEN shared component WHEN shared component button is clicked THEN it must display the message content', function() {
            // GIVEN
            storefront.actions.moveToComponent(SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.actions.clickSharedComponentButton(SHARED_COMPONENT_ID);

            // THEN
            contextualMenu.assertions.assertSharedComponentToShowDetails(SHARED_COMPONENT_ID);
        });
    });

    describe(' advanced edit mode - ', function() {
        beforeEach(function(done) {
            perspective.selectAdvancedPerspective().then(function() {
                done();
            });
        });

        it('GIVEN component is not shared WHEN hovered THEN it must not have a shared component contextual button', function() {
            // GIVEN
            storefront.actions.moveToComponent(NON_SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.assertions.assertSharedComponentButtonIsNotPresent(
                NON_SHARED_COMPONENT_ID
            );
        });

        it('GIVEN component is shared WHEN hovered THEN it must have a shared component icon', function() {
            // GIVEN
            storefront.actions.moveToComponent(SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.assertions.assertSharedComponentButtonIsPresent(SHARED_COMPONENT_ID);
        });

        it('GIVEN shared component WHEN shared component button is clicked THEN it must display the message content', function() {
            // GIVEN
            storefront.actions.moveToComponent(SHARED_COMPONENT_ID);

            // WHEN/THEN
            contextualMenu.actions.clickSharedComponentButton(SHARED_COMPONENT_ID);

            // THEN
            contextualMenu.assertions.assertSharedComponentToShowDetails(SHARED_COMPONENT_ID);
        });
    });
});
