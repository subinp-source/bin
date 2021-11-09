/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('seMediaAdvancedProperties', function() {
    var parentScope, scope, element, ctrl;
    var seMediaAdvancedPropertiesConstants;

    beforeEach(angular.mock.module('cmssmarteditContainerTemplates'));

    beforeEach(
        angular.mock.module('pascalprecht.translate', function($translateProvider) {
            $translateProvider.translations('en', {
                'se.media.advanced.information.description': 'Description',
                'se.media.advanced.information.code': 'Code',
                'se.media.advanced.information.alt.text': 'Alt Text',
                'se.media.information': 'Information'
            });
            $translateProvider.preferredLanguage('en');
        })
    );

    beforeEach(angular.mock.module('seMediaAdvancedPropertiesModule'));

    beforeEach(inject(function($compile, $rootScope, _seMediaAdvancedPropertiesConstants_) {
        seMediaAdvancedPropertiesConstants = _seMediaAdvancedPropertiesConstants_;

        parentScope = $rootScope.$new();
        window.smarteditJQuery.extend(parentScope, {
            code: 'someCode',
            description: 'someDescription',
            altText: 'someAltText'
        });

        element = $compile(
            '<se-media-advanced-properties ' +
                'data-code="code" ' +
                'data-description="description" ' +
                'data-alt-text="altText">' +
                '</se-media-advanced-properties>'
        )(parentScope);
        parentScope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    describe('controller', function() {
        it('should be initialized', function() {
            expect(ctrl.i18nKeys).toBe(seMediaAdvancedPropertiesConstants.I18N_KEYS);
        });
    });

    describe('template', function() {
        it('should have a link with translated text', function() {
            expect(
                window
                    .smarteditJQuery(
                        element.find('.se-media__advanced-info .se-media-format__label')
                    )
                    .text()
            ).toContain('Information');
        });
    });
});
