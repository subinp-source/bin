/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { StringUtils as ParentStringUtils } from '@smart/utils';

/**
 * @ngdoc service
 * @name functionsModule.service:StringUtils
 *
 * @description
 * A collection of utility methods for windows.
 */
export class StringUtils extends ParentStringUtils {
    /**
     * @ngdoc method
     * @name functionsModule.service:StringUtils#sanitizeHTML
     * @methodOf functionsModule.service:StringUtils
     *
     * @description
     * <b>sanitizeHTML</b> will remove breaks and space .
     *
     * @param {String} a string that needs to be escaped.
     *
     * @returns {String} the sanitized HTML.
     *
     */

    sanitizeHTML(text: string): string {
        if (stringUtils.isBlank(text)) {
            return text;
        }

        return text
            .replace(/(\r\n|\n|\r)/gm, '')
            .replace(/>\s+</g, '><')
            .replace(/<\/br\>/g, '');
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:StringUtils#generateIdentifier
     * @methodOf functionsModule.service:StringUtils
     *
     * @description
     * <b>generateIdentifier</b> will generate a unique string based on system time and a random generator.
     *
     * @returns {String} a unique identifier.
     *
     */
    generateIdentifier(): string {
        let d = new Date().getTime();
        if (window.performance && typeof window.performance.now === 'function') {
            d += window.performance.now(); // use high-precision timer if available
        }
        const uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            const r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16);
        });
        return uuid;
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:StringUtils#getEncodedString
     * @methodOf functionsModule.service:StringUtils
     *
     * @description
     * <b>getEncodedString</b> will creates a base-64 encoded ASCII string
     * from the object or string  passed as input
     *
     * @returns {string} a base-64 encoded ASCII string.
     *
     */

    getEncodedString(input: any): string {
        return this.encode(input);
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:StringUtils#trim
     * @methodOf functionsModule.service:StringUtils
     *
     * @description
     * @deprecated since 2005 use {@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/Trim API}
     * <b>trim</b> will remove spaces at the beginning and end of a given string.
     *
     * @param {String} inputString any input string.
     *
     * @returns {String} the newly modified string without spaces at the beginning and the end
     */
    trim(aString: string): string {
        return aString.trim();
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:StringUtils#escapeHtml
     * @methodOf functionsModule.service:StringUtils
     *
     * @description
     * <b>escapeHtml</b> will escape &, <, >, " and ' characters .
     *
     * @param {String | Number} a string that needs to be escaped.
     *
     * @returns {String} the escaped string.
     *
     */
    escapeHtml(str: string | number): string | number {
        if (typeof str === 'string') {
            return str
                .replace(/&/g, '&amp;')
                .replace(/>/g, '&gt;')
                .replace(/</g, '&lt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&apos;');
        }
        return str;
    }

    resourceLocationToRegex(str: string): RegExp {
        return new RegExp(str.replace(/\/:[^\/]*/g, '/.*'));
    }

    /**
     * @ngdoc service
     * @name @smartutils.services:StringUtils#sanitize
     * @methodOf @smartutils.services:StringUtils
     *
     * @description
     * overwrite the logic of 'sanitize' method in smart-utils
     * @returns {String} the sanitized string.
     * sanitize is deprecated since 1808, ECP-4765. The method will be removed in 2105.
     */
    sanitize = (str: string): string => str;

}

export const stringUtils = new StringUtils();
