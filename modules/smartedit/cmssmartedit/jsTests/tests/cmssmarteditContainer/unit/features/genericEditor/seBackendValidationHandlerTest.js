/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('seBackendValidationHandler', function() {
    var seBackendValidationHandler;
    var RESPONSE = {
        error: {
            errors: [
                {
                    type: 'ModelError'
                },
                {
                    type: 'ValidationError',
                    message: 'no subject provided'
                },
                {
                    type: 'ValidationError',
                    subject: 'someSubject',
                    message: 'some message'
                }
            ]
        }
    };

    beforeEach(angular.mock.module('seBackendValidationHandlerModule'));

    beforeEach(inject(function(_seBackendValidationHandler_) {
        seBackendValidationHandler = _seBackendValidationHandler_;
    }));

    describe('handleResponse', function() {
        it('should transform a response into a list of validation errors filtering on type ValidationError', function() {
            expect(seBackendValidationHandler.handleResponse(RESPONSE)).toEqual([
                {
                    subject: 'someSubject',
                    message: 'some message'
                }
            ]);
        });

        it('should append the validation errors to an errors context list if one is provided', function() {
            var errorsContext = [];
            seBackendValidationHandler.handleResponse(RESPONSE, errorsContext);
            expect(errorsContext).toEqual([
                {
                    subject: 'someSubject',
                    message: 'some message'
                }
            ]);
        });
    });
});
