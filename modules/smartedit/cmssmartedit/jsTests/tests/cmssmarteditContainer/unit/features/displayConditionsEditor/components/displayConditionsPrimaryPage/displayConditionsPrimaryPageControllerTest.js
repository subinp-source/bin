/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('displayConditionsPrimaryPageController', function() {
    var controller;

    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule(
            'displayConditionsPrimaryPageControllerModule'
        )
            .mock('displayConditionsFacade', 'getPrimaryPagesForPageType')
            .mock('pageService', 'getPageById')
            .controller('displayConditionsPrimaryPageController');
        controller = harness.controller;
    });

    describe('init', function() {
        it('should initialize the i18n keys on the scope', function() {
            expect(controller.associatedPrimaryPageLabelI18nKey).toBe(
                'se.cms.display.conditions.primary.page.label'
            );
        });
    });
});
