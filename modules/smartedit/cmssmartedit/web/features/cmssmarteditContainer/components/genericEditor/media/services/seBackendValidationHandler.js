/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name seBackendValidationHandlerModule
 * @description
 * This module provides the seBackendValidationHandler service, which handles standard OCC validation errors received
 * from the backend.
 */
angular
    .module('seBackendValidationHandlerModule', [])

    /**
     * @ngdoc service
     * @name seBackendValidationHandlerModule.seBackendValidationHandler
     * @description
     * The seBackendValidationHandler service handles validation errors received from the backend.
     */
    .factory('seBackendValidationHandler', function() {
        /**
         * @ngdoc method
         * @name seBackendValidationHandlerModule.seBackendValidationHandler.handleResponse
         * @methodOf seBackendValidationHandlerModule.seBackendValidationHandler
         * @description
         * Extracts validation errors from the provided response and appends them to a specified contextual errors list.
         *
         * The expected error response from the backend matches the contract of the following response example:
         *
         * <pre>
         * var response = {
         *     data: {
         *         errors: [{
         *             type: 'ValidationError',
         *             subject: 'mySubject',
         *             message: 'Some validation exception occurred'
         *         }, {
         *             type: 'SomeOtherError',
         *             subject: 'mySubject'
         *             message: 'Some other exception occurred'
         *         }]
         *     }
         * }
         * </pre>
         *
         * Example of use:
         * <pre>
         * var errorsContext = [];
         * seBackendValidationHandler.handleResponse(response, errorsContext);
         * </pre>
         *
         * The resulting errorsContext would be as follows:
         * <pre>
         * [{
         *     subject: 'mySubject',
         *     message: 'Some validation exception occurred'
         * }]
         * </pre>
         *
         * @param {Object} response A response consisting of a list of errors; for details of the expected format, see the
         * example above.
         * @param {Array} errorsContext An array that all validation errors are appended to. It is an output parameter.
         * @returns {Array} The error context list originally provided, or a new list, appended with the validation errors
         */
        var handleResponse = function(response, errorsContext) {
            errorsContext = errorsContext || [];
            if (response && response.error && response.error.errors) {
                response.error.errors
                    .filter(function(error) {
                        return error.type === 'ValidationError';
                    })
                    .forEach(function(validationError) {
                        var subject = validationError.subject;
                        if (subject) {
                            errorsContext.push({
                                subject: subject,
                                message: validationError.message
                            });
                        }
                    });
            }
            return errorsContext;
        };

        return {
            handleResponse: handleResponse
        };
    });
