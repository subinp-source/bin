/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('seFileMimeTypeService', function() {
    var seFileMimeTypeService;
    var seFileReader, settingsService, $q;

    beforeEach(
        angular.mock.module('seFileMimeTypeServiceModule', function($provide) {
            seFileReader = jasmine.createSpyObj('seFileReader', ['read']);
            $provide.value('seFileReader', seFileReader);

            settingsService = jasmine.createSpyObj('settingsService', ['getStringList']);
            $provide.value('settingsService', settingsService);
        })
    );

    beforeEach(inject(function(_seFileMimeTypeService_, _$q_) {
        seFileMimeTypeService = _seFileMimeTypeService_;
        $q = _$q_;
    }));

    beforeEach(function() {
        settingsService.getStringList.and.returnValue($q.when(['89504E47']));
    });

    describe('isFileMimeTypeValid', function() {
        it('will return a resolved promise when the file mime type is valid', function() {
            var MOCK_FILE = [0x89, 0x50, 0x4e, 0x47];
            seFileReader.read.and.callFake(function(file, config) {
                config.onLoadEnd({
                    target: {
                        result: file
                    }
                });
            });

            var promise = seFileMimeTypeService.isFileMimeTypeValid(MOCK_FILE);
            expect(promise).toBeResolved();
        });

        it('will return a rejected promise when the file mime type is invalid', function() {
            var MOCK_FILE = [0x84, 0x83, 0x35, 0x53];
            seFileReader.read.and.callFake(function(file, config) {
                config.onLoadEnd({
                    target: {
                        result: file
                    }
                });
            });
            var promise = seFileMimeTypeService.isFileMimeTypeValid(MOCK_FILE);

            expect(promise).toBeRejected();
        });

        it('will return a rejected promise when the file fails to load', function() {
            var MOCK_FILE = [0x89, 0x50, 0x4e, 0x47];
            seFileReader.read.and.callFake(function(file, config) {
                config.onError();
            });
            var promise = seFileMimeTypeService.isFileMimeTypeValid(MOCK_FILE);

            expect(promise).toBeRejected();
        });
    });
});
