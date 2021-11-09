/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeFactory } from 'smarteditcommons/di/types';
import { SeModule } from 'smarteditcommons/di/SeModule';
import {
    booleanUtils,
    urlUtils,
    BooleanUtils,
    CloneableUtils,
    CryptographicUtils,
    FunctionsUtils,
    LogService,
    PromiseUtils,
    UrlUtils,
    URIBuilder
} from '@smart/utils';
import { DiscardablePromiseUtils } from './DiscardablePromiseUtils';
import { windowUtils } from './WindowUtils';
import { nodeUtils, NodeUtils } from './NodeUtils';
import { ModuleUtils } from './ModuleUtils';
import { objectUtils } from './ObjectUtils';
import { stringUtils, StringUtils } from './StringUtils';
import { apiUtils } from './ApiUtils';
import { JQueryUtilsService } from '../services';
import { dateUtils } from './DateUtils';
import { Errors } from './errors';
import { EXTENDED_VIEW_PORT_MARGIN } from './smarteditconstants';
import { scriptUtils } from './ScriptUtils';

/**
 * @ngdoc service
 * @name functionsModule
 *
 * @description
 * provides a list of useful functions that can be used as part of the SmartEdit framework.
 */
@SeModule({
    providers: [
        BooleanUtils,
        CloneableUtils,
        UrlUtils,
        CryptographicUtils,
        FunctionsUtils,
        LogService,
        ModuleUtils,
        NodeUtils,
        PromiseUtils,
        StringUtils,
        DiscardablePromiseUtils,
        {
            provide: 'ParseError',
            useFactory: () => Errors.ParseError
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.getAbsoluteURL
         * @deprecated since 2005 use {@link functionsModule.service:UrlUtils#getAbsoluteURL UrlUtils#getAbsoluteURL}
         */
        {
            provide: 'getAbsoluteURL',
            useFactory: () => urlUtils.getAbsoluteURL
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.getOrigin
         * @deprecated since 1905 use {@link functionsModule.service:UrlUtils#getOrigin UrlUtils#getOrigin}
         */
        {
            provide: 'getOrigin',
            useFactory: () => urlUtils.getOrigin
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.isBlank
         *
         * @description
         * @deprecated since 1905, use {@link functionsModule.service:StringUtils#isBlank StringUtils.isBlank}
         */
        {
            provide: 'isBlank',
            useFactory: () => stringUtils.isBlank
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.extend
         *
         * @description
         * <b>extend</b> provides a convenience to either default a new child or "extend" an existing child with the prototype of the parent
         *
         * @param {Class} ParentClass which has a prototype you wish to extend.
         * @param {Class} ChildClass will have its prototype set.
         *
         * @returns {Class} ChildClass which has been extended
         */
        {
            provide: 'extend',
            useFactory: () => {
                return (ParentClass: SeFactory, ChildClass?: SeFactory) => {
                    if (!ChildClass) {
                        ChildClass = function() {
                            return;
                        };
                    }
                    ChildClass.prototype = Object.create(ParentClass.prototype);
                    return ChildClass;
                };
            }
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.customTimeout
         *
         * @description
         * <b>customTimeout</b> will call the javascrit's native setTimeout method to execute a given function after a specified period of time.
         * This method is better than using $timeout since it is difficult to assert on $timeout during end-to-end testing.
         *
         * @param {Function} func function that needs to be executed after the specified duration.
         * @param {Number} duration time in milliseconds.
         */
        {
            provide: 'customTimeout',
            useFactory: () => {
                return (func: SeFactory, duration: number) => {
                    setTimeout(function() {
                        func();
                    }, duration);
                };
            }
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.copy
         *
         * @deprecated since 1905, use {@link functionsModule.service:ObjectUtils#copy ObjectUtils#copy}
         */
        {
            provide: 'copy',
            useFactory: () => objectUtils.copy
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.merge
         * @description
         * Implementation of {@link functionsModule.service:ObjectUtils ObjectUtils#merge}
         */
        {
            provide: 'merge',
            useFactory: () => objectUtils.merge
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.getQueryString
         *
         * @description
         * @deprecated since 1905, use {@link functionsModule.service:UrlUtils UrlUtils#getQueryString}
         */
        {
            provide: 'getQueryString',
            useFactory: () => urlUtils.getQueryString
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.getURI
         *
         * @description
         * @deprecated since 1905, use {@link functionsModule.service:UrlUtils#getURI UrlUtils#getURI}
         */
        {
            provide: 'getURI',
            useFactory: () => urlUtils.getURI
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.parseQuery
         *
         * @description
         * @deprecated since 1905, use {@link functionsModule.service:UrlUtils#parseQuery UrlUtils#parseQuery}
         */
        {
            provide: 'parseQuery',
            useFactory: () => urlUtils.parseQuery
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.trim
         *
         * @description
         * Implementation of {@link functionsModule.service:StringUtils StringUtils#trim}
         */
        {
            provide: 'trim',
            useFactory: () => stringUtils.trim
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.convertToArray
         *
         * @description
         * Implementation of {@link functionsModule.service:ObjectUtils ObjectUtils#convertToArray}
         */
        {
            provide: 'convertToArray',
            useFactory: () => objectUtils.convertToArray
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.injectJS
         *
         * @description
         * <b>injectJS</b> will inject script tags into html for a given set of sources.
         *
         */
        {
            provide: 'injectJS',
            useFactory: () => scriptUtils.injectJS()
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.uniqueArray
         *
         * @description
         * <b>uniqueArray</b> will return the first Array argument supplemented with new entries from the second Array argument.
         *
         * @param {Array} array1 any JavaScript array.
         * @param {Array} array2 any JavaScript array.
         */
        {
            provide: 'uniqueArray',
            useFactory: () => objectUtils.uniqueArray
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.regExpFactory
         *
         * @description
         * <b>regExpFactory</b> will convert a given pattern into a regular expression.
         * This method will prepend and append a string with ^ and $ respectively replaces
         * and wildcards (*) by proper regex wildcards.
         *
         * @param {String} pattern any string that needs to be converted to a regular expression.
         *
         * @returns {RegExp} a regular expression generated from the given string.
         *
         * @deprecated since 1811, use {@link functionsModule.service:StringUtils#regExpFactory StringUtils#regExpFactory}
         *
         */
        {
            provide: 'regExpFactory',
            useFactory: () => stringUtils.regExpFactory
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.generateIdentifier
         *
         * @description
         * <b>generateIdentifier</b> will generate a unique string based on system time and a random generator.
         * @deprecated since 2005, use {@link functionsModule.service:StringUtils#generateIdentifier StringUtils#generateIdentifier}
         * @returns {String} a unique identifier.
         *
         */
        {
            provide: 'generateIdentifier',
            useFactory: () => stringUtils.generateIdentifier
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.escapeHtml
         *
         * @description
         * <b>escapeHtml</b> will escape &, <, >, " and ' characters .
         *
         * @param {String} a string that needs to be escaped.
         *
         * @returns {String} the escaped string.
         *
         */
        {
            provide: 'escapeHtml',
            useFactory: () => stringUtils.escapeHtml
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.sanitize
         *
         * @description
         * <b>escapes any harmful scripting from a string, leaves innocuous HTML untouched/b>
         * @deprecated since 1905, use {@link functionsModule.service:StringUtils#sanitize StringUtils#sanitize}
         * @param {String} a string that needs to be sanitized.
         *
         * @returns {String} the sanitized string.
         *
         */
        {
            provide: 'sanitize',
            useFactory: () => stringUtils.sanitize
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.sanitizeHTML
         * @deprecated since 1905, use {@link functionsModule.service:StringUtils#sanitizeHTML StringUtils#sanitizeHTML}
         *
         * @description
         *
         * <b>sanitizeHTML</b> will remove breaks and space .
         *
         * @param {String} a string that needs to be escaped.
         *
         * @returns {String} the sanitized HTML.
         *
         */
        {
            provide: 'sanitizeHTML',
            useFactory: () => stringUtils.sanitizeHTML
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.toPromise
         *
         * @description
         * <b>toPromise</> transforms a function into a function that is guaranteed to return a Promise that resolves to the
         * original return value of the function, rejects with the rejected return value and rejects with an exception object when the invocation fails
         */
        {
            provide: 'toPromise',
            useFactory: ($q: angular.IQService, $log: angular.ILogService) => {
                return (method: any, context: any) => {
                    return function() {
                        try {
                            return $q.when(method.apply(context, arguments));
                        } catch (e) {
                            $log.error(
                                'execution of a method that was turned into a promise failed'
                            );
                            $log.error(e);
                            return $q.reject(e);
                        }
                    };
                };
            },
            deps: ['$q', '$log']
        },
        /**
         * Checks if `value` is a function.
         *
         * @static
         * @category Objects
         * @param {*} value The value to check.
         * @returns {boolean} Returns `true` if the `value` is a function, else `false`.
         */
        {
            provide: 'isFunction',
            useFactory: () => objectUtils.isFunction
        },
        /**
         * @static
         *
         * @description
         * checks if the value is the ECMAScript language type of Object
         */
        {
            provide: 'isObject',
            useFactory: () => objectUtils.isObject
        },
        {
            provide: 'debounce',
            useFactory: (isFunction: any, isObject: any) => {
                // tslint:disable-next-line
                class TypeError {}

                return function(func: any, wait: any, options: any) {
                    let args: any;
                    let maxTimeoutId: any;
                    let result: any;
                    let stamp: any;
                    let thisArg: any;
                    let timeoutId: any;
                    let trailingCall: any;
                    let leading: any;
                    let lastCalled = 0;
                    let maxWait: any = false;
                    let trailing = true;
                    let isCalled: any;

                    if (!isFunction(func)) {
                        throw new TypeError();
                    }
                    wait = Math.max(0, wait) || 0;
                    if (options === true) {
                        leading = true;
                        trailing = false;
                    } else if (isObject(options)) {
                        leading = options.leading;
                        maxWait = 'maxWait' in options && (Math.max(wait, options.maxWait) || 0);
                        trailing = 'trailing' in options ? options.trailing : trailing;
                    }
                    const delayed = function() {
                        const remaining = wait - (Date.now() - stamp);
                        if (remaining <= 0) {
                            if (maxTimeoutId) {
                                clearTimeout(maxTimeoutId);
                            }
                            isCalled = trailingCall;
                            maxTimeoutId = timeoutId = trailingCall = undefined;
                            if (isCalled) {
                                lastCalled = Date.now();
                                result = func.apply(thisArg, args);
                                if (!timeoutId && !maxTimeoutId) {
                                    args = thisArg = null;
                                }
                            }
                        } else {
                            timeoutId = setTimeout(delayed, remaining);
                        }
                    };

                    const maxDelayed = function() {
                        if (timeoutId) {
                            clearTimeout(timeoutId);
                        }
                        maxTimeoutId = timeoutId = trailingCall = undefined;
                        if (trailing || maxWait !== wait) {
                            lastCalled = Date.now();
                            result = func.apply(thisArg, args);
                            if (!timeoutId && !maxTimeoutId) {
                                args = thisArg = null;
                            }
                        }
                    };

                    return function() {
                        args = arguments;
                        stamp = Date.now();
                        thisArg = this;
                        trailingCall = trailing && (timeoutId || !leading);
                        let leadingCall;

                        if (maxWait === false) {
                            leadingCall = leading && !timeoutId;
                        } else {
                            if (!maxTimeoutId && !leading) {
                                lastCalled = stamp;
                            }
                            const remaining = maxWait - (stamp - lastCalled);
                            isCalled = remaining <= 0;

                            if (isCalled) {
                                if (maxTimeoutId) {
                                    maxTimeoutId = clearTimeout(maxTimeoutId);
                                }
                                lastCalled = stamp;
                                result = func.apply(thisArg, args);
                            } else if (!maxTimeoutId) {
                                maxTimeoutId = setTimeout(maxDelayed, remaining);
                            }
                        }
                        if (isCalled && timeoutId) {
                            timeoutId = clearTimeout(timeoutId);
                        } else if (!timeoutId && wait !== maxWait) {
                            timeoutId = setTimeout(delayed, wait);
                        }
                        if (leadingCall) {
                            isCalled = true;
                            result = func.apply(thisArg, args);
                        }
                        if (isCalled && !timeoutId && !maxTimeoutId) {
                            args = thisArg = null;
                        }
                        return result;
                    };
                };
            },
            deps: ['isFunction', 'isObject']
        },
        {
            provide: 'throttle',
            useFactory: (debounce: any, isFunction: any, isObject: any) => {
                return function(func: any, wait: any, options: any) {
                    let leading = true;
                    let trailing = true;

                    if (!isFunction(func)) {
                        throw new TypeError();
                    }
                    if (options === false) {
                        leading = false;
                    } else if (isObject(options)) {
                        leading = 'leading' in options ? options.leading : leading;
                        trailing = 'trailing' in options ? options.trailing : trailing;
                    }
                    options = {};
                    options.leading = leading;
                    options.maxWait = wait;
                    options.trailing = trailing;

                    return debounce(func, wait, options);
                };
            },
            deps: ['debounce', 'isFunction', 'isObject']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.parseHTML
         *
         * @description
         * parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.
         *
         * @param {String} stringHTML, the string representation of the HTML to parse
         */
        {
            provide: 'parseHTML',
            useFactory: (yjQuery: any) => {
                return function(stringHTML: any) {
                    return yjQuery.parseHTML(stringHTML);
                };
            },
            deps: ['yjQuery']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.unsafeParseHTML
         * @deprecated since 2005
         *
         * @description
         * Deprecated, use {@link smarteditCommonsModule.service:JQueryUtilsService JQueryUtilsService.unsafeParseHTML}
         * parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.
         * Note - as this preserves the JavaScript present it must only be used on HTML strings originating
         * from a known safe location. Failure to do so may result in an XSS vulnerability.
         *
         * @param {String} stringHTML, the string representation of the HTML to parse
         */
        {
            provide: 'unsafeParseHTML',
            useFactory: (jQueryUtilsService: JQueryUtilsService) => {
                return jQueryUtilsService.unsafeParseHTML;
            },
            deps: ['jQueryUtilsService']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.extractFromElement
         * @deprecated since 2005
         * @description
         * Deprecated, use {@link smarteditCommonsModule.service:JQueryUtilsService JQueryUtilsService.extractFromElement}
         * parses a string HTML into a queriable DOM object
         *
         * @param {Object} parent, the DOM element from which we want to extract matching selectors
         * @param {String} extractionSelector, the yjQuery selector identifying the elements to be extracted
         */
        {
            provide: 'extractFromElement',
            useFactory: (jQueryUtilsService: JQueryUtilsService) => {
                return jQueryUtilsService.extractFromElement;
            },
            deps: ['jQueryUtilsService']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.closeOpenModalsOnBrowserBack
         *
         * @description
         * close any open modal window when a user clicks browser back button
         *
         * @param {Object} modalStack, the $modalStack service of angular-ui.
         */
        {
            provide: 'closeOpenModalsOnBrowserBack',
            useFactory: ($uibModalStack: any) => {
                return function() {
                    if ($uibModalStack.getTop()) {
                        $uibModalStack.dismissAll();
                    }
                };
            },
            deps: ['$uibModalStack']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:URIBuilder
         * @deprecated since 1905, use {@link functionsModule.service:UrlUtils#URIBuilder UrlUtils#URIBuilder}
         *
         * @description
         * builder or URIs, build() method must be invoked to actually retrieve a URI
         * @deprecated since 1905, use {@link functionsModule.service:UrlUtils#URIBuilder UrlUtils#URIBuilder}
         *
         * @param {Object} modalStack, the $modalStack service of angular-ui.
         */
        {
            provide: 'URIBuilder',
            useFactory: () => URIBuilder
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:getDataFromResponse
         *
         * @description
         * Implementation of {@link functionsModule.service:ApiUtils ApiUtils#getDataFromResponse}
         */
        {
            provide: 'getDataFromResponse',
            useFactory: () => apiUtils.getDataFromResponse
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:getKeyHoldingDataFromResponse
         *
         * @description
         * Implementation of {@link functionsModule.service:ApiUtils ApiUtils#getKeyHoldingDataFromResponse}
         */
        {
            provide: 'getKeyHoldingDataFromResponse',
            useFactory: () => apiUtils.getKeyHoldingDataFromResponse
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:resetObject
         * @deprecated since 2005, use {@link functionsModule.service:ObjectUtils#resetObject ObjectUtils#resetObject}
         */
        {
            provide: 'resetObject',
            useFactory: () => objectUtils.resetObject
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:isObjectEmptyDeep
         * @deprecated since 2005, use {@link functionsModule.service:ObjectUtils#isObjectEmptyDeep ObjectUtils#isObjectEmptyDeep}
         */
        {
            provide: 'isObjectEmptyDeep',
            useFactory: () => objectUtils.isObjectEmptyDeep
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:areAllTruthy
         * @deprecated since 1905, use {@link functionsModule.service:BooleanUtils#areAllTruthy BooleanUtils#areAllTruthy}
         *
         * @description
         * Iterate on the given array of Functions, return true if each function returns true
         * @deprecated since 1905, use {@link functionsModule.service:BooleanUtils#areAllTruthy BooleanUtils#areAllTruthy}
         *
         * @param {Array} arguments the functions
         *
         * @return {Boolean} true if every function returns true
         */
        {
            provide: 'areAllTruthy',
            useFactory: () => booleanUtils.areAllTruthy
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:isAnyTruthy
         * @deprecated since 1905, use {@link functionsModule.service:BooleanUtils#isAnyTruthy BooleanUtils#isAnyTruthy}
         *
         * @description
         * Iterate on the given array of Functions, return true if at least one function returns true
         * @deprecated since 1905, use {@link functionsModule.service:BooleanUtils#isAnyTruthy BooleanUtils#isAnyTruthy}
         *
         * @param {Array} arguments the functions
         *
         * @return {Boolean} true if at least one function returns true
         */
        {
            provide: 'isAnyTruthy',
            useFactory: () => booleanUtils.isAnyTruthy
        },
        /**
         * @ngdoc service
         * @name functionsModule.service:formatDateAsUtc
         *
         * @description
         * @deprecated since 2005, use {@link functionsModule.service:DateUtils}
         * Formats provided dateTime as utc.
         *
         * @param {Object|String} dateTime DateTime to format in utc.
         *
         * @return {String} formatted string.
         */
        {
            provide: 'formatDateAsUtc',
            useFactory: () => dateUtils.formatDateAsUtc
        },

        /**
         * @ngdoc service
         * @name functionsModule.service.getEncodedString
         * @deprecated since 2005, use  {@link functionsModule.service:StringUtils#encode StringUtils#encode}
         *
         * @description
         * <b>getEncodedString</b> will creates a base-64 encoded ASCII string
         * from the object or string  passed as input
         *
         * @returns {string} a base-64 encoded ASCII string.
         *
         */
        {
            provide: 'getEncodedString',
            useFactory: () => stringUtils.encode
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.encode
         *
         * @description
         * will return a encoded value for any JSON object passed as argument
         * @param {object} JSON object to be encoded
         *
         * @deprecated since 1905, use {@link functionsModule.service:StringUtils#encode StringUtils#encode}
         */
        {
            provide: 'encode',
            useFactory: () => stringUtils.encode
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.compareHTMLElementsPosition
         *
         * @description
         * Implementation of {@link functionsModule.service:NodeUtils NodeUtils#compareHTMLElementsPosition}
         */
        {
            provide: 'compareHTMLElementsPosition',
            useFactory: () => nodeUtils.compareHTMLElementsPosition
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.isIframe
         * @deprecated since 1905 use {@link functionsModule.service:WindowUtils#isIframe WindowUtils#isIframe}
         * @description
         * <b>isIframe</b> will check if the current document is in an iFrame.
         * @deprecated since 1905 use {@link functionsModule.service:WindowUtils#isIframe WindowUtils#isIframe}
         * @returns {boolean} true if the current document is in an iFrame.
         */
        {
            provide: 'isIframe',
            useFactory: () => windowUtils.isIframe
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.isPointOverElement
         *
         * @description
         * Implementation of {@link functionsModule.service:NodeUtils NodeUtils#isPointOverElement}
         */
        {
            provide: 'isPointOverElement',
            useFactory: () => nodeUtils.isPointOverElement
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.areIntersecting
         * @description
         * Implementation of {@link functionsModule.service:NodeUtils NodeUtils#areIntersecting}
         */
        {
            provide: 'areIntersecting',
            useFactory: () => nodeUtils.areIntersecting
        },

        {
            provide: 'EXTENDED_VIEW_PORT_MARGIN',
            useValue: EXTENDED_VIEW_PORT_MARGIN
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.isInExtendedViewPort
         *
         * @description
         * determines whether a DOM element is partially or totally intersecting with the "extended" viewPort
         * the "extended" viewPort is the real viewPort that extends up and down by a margin, in pixels, given by the overridable constant EXTENDED_VIEW_PORT_MARGIN
         * @param {HtmlElement} element a DOM element
         * @returns {boolean} true if the given element is in the extended view port
         */
        {
            provide: 'isInExtendedViewPort',
            useFactory: (jQueryUtilsService: JQueryUtilsService) => {
                return jQueryUtilsService.isInExtendedViewPort;
            },
            deps: ['jQueryUtilsService']
        },
        {
            provide: 'deepIterateOverObjectWith',
            useFactory: () => objectUtils.deepIterateOverObjectWith
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.deepObjectPropertyDiff
         * @deprecated since 2005, use {@link functionsModule.service:ObjectUtils#deepObjectPropertyDiff ObjectUtils#deepObjectPropertyDiff}
         */
        {
            provide: 'deepObjectPropertyDiff',
            useFactory: () => objectUtils.deepObjectPropertyDiff
        },

        {
            provide: 'isInDOM',
            useFactory: ($document: any, yjQuery: any) => {
                return function(component: any) {
                    return yjQuery.contains($document[0], component);
                };
            },
            deps: ['$document', 'yjQuery']
        },
        /**
         * @ngdoc service
         * @name functionsModule.service.readObjectStructureFactory
         * @description
         * Implementation of {@link functionsModule.service:ObjectUtils ObjectUtils#readObjectStructureFactory}
         */
        {
            provide: 'readObjectStructureFactory',
            useValue: () => objectUtils.readObjectStructure
        }
    ]
})
export class FunctionsModule {}
