/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('seMediaUploadField', function() {
    var element, scope, ctrl, $q;

    beforeEach(angular.mock.module('cmssmarteditContainerTemplates'));

    beforeEach(angular.mock.module('seMediaUploadFieldModule'));

    beforeEach(inject(function($rootScope, $compile, _$q_) {
        $q = _$q_;

        scope = $rootScope.$new();
        window.smarteditJQuery.extend(scope, {
            field: 'someField',
            model: {
                someField: 'someValue'
            },
            error: false
        });

        element = $compile(
            '<se-media-upload-field ' +
                'data-error="error" ' +
                'data-field="field" ' +
                'data-model="model">' +
                '</se-media-upload-field>'
        )(scope);
        scope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    describe('initialization', function() {
        it('should have an input pre-populated with the model value and no icons visible', function() {
            expect(element.find('input').val()).toBe('someValue');
            expect(element.find('input').hasClass('se-mu--fileinfo--field__error')).toBe(false);
            expect(element.find('img.se-mu--fileinfo--field--icon__error')).not.toExist();
            expect(element.find('.se-mu--fileinfo--field--icon')).not.toExist();
        });
    });

    describe('on error', function() {
        beforeEach(function() {
            ctrl.error = true;
            scope.$digest();
        });

        it('should add the error class to the input', function() {
            expect(element.find('input').hasClass('is-invalid')).toBe(true);
        });
    });
});
