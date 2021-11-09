/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name yLoDashModule
 * @description
 * This module manages the use of the lodash library in SmartEdit. It makes sure the library is introduced
 * in the Angular lifecycle and makes it easy to mock for unit tests.
 */
angular
    .module('yLoDashModule', [])
    /**
     * @ngdoc object
     * @name yLoDashModule.lodash
     * @description
     *
     * Makes the underscore library available to SmartEdit.
     *
     * Note: original _ namespace is removed from window in order not to clash with other libraries especially in the storefront AND to enforce proper dependency injection.
     */
    /* forbiddenNameSpaces window._.:false */
    .factory('lodash', function() {
        if (!window.smarteditLodash) {
            if (window._ && window._.noConflict) {
                window.smarteditLodash = window._.noConflict();
            } else {
                throw 'could not find lodash library under window._ namespace';
            }
        }
        return window.smarteditLodash;
    });
