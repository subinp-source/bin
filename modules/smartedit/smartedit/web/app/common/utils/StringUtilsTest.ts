/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { stringUtils } from 'smarteditcommons';

describe('String Utils Test', () => {
    it('trim removes space at the beginning and end of a given string', () => {
        const inputString = '  testStringWithSpaces ';

        expect(stringUtils.trim(inputString)).toBe('testStringWithSpaces');
    });

    it('escapeHtml will escape dangerous characters from a given string', () => {
        const _string = stringUtils.escapeHtml('hello<button>&\'"');
        expect(_string).toBe('hello&lt;button&gt;&amp;&apos;&quot;');
    });

    it('escapeHtml will handle numeric values correctly', () => {
        const _string = stringUtils.escapeHtml(123456);
        expect(_string).toBe(123456);
    });

    it('generateIdentifier will generate a unique identifier each time it is called', () => {
        const uniqueKey1 = stringUtils.generateIdentifier();
        const uniqueKey2 = stringUtils.generateIdentifier();

        expect(uniqueKey1).not.toBe(uniqueKey2);
    });
});
