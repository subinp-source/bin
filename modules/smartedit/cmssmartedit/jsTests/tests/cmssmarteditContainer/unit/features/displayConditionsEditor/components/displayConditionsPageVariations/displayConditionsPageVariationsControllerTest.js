/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('displayConditionsPageVariationsController', function() {
    var controller;

    beforeEach(function() {
        var harness = AngularUnitTestHelper.prepareModule(
            'displayConditionsPageVariationsControllerModule'
        ).controller('displayConditionsPageVariationsController');
        controller = harness.controller;
    });

    describe('init', function() {
        it('should initialize the i18n keys on the scope', function() {
            expect(controller.variationPagesTitleI18nKey).toBe(
                'se.cms.display.conditions.variation.pages.title'
            );
            expect(controller.noVariationsI18nKey).toBe('se.cms.display.conditions.no.variations');
            expect(controller.variationsDescriptionI18nKey).toBe(
                'se.cms.display.conditions.variations.description'
            );
        });

        it('should initialize the number of items per page, a set of keys, and a set of renderers on the scope', function() {
            expect(controller.itemsPerPage).toBeDefined();
            expect(controller.keys).toBeDefined();
            expect(controller.renderers.creationDate).toBeDefined();
        });
    });
});
