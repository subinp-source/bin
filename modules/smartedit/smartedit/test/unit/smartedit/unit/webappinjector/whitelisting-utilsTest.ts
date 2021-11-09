/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import WhitelistingUtils from '../../../../../web/app/webappinjector/src/whitelisting-utils';
import {
    allowedConfigs,
    allowedOriginURLs,
    disallowedConfigs,
    disallowedOriginURLs
} from './webapp-injector-mock-data';

describe('Whitelisting-Utils from webapp-injector', () => {
    const allowedConfigRegexps = WhitelistingUtils.convertWhitelistingToRegexp(allowedConfigs);

    it('should disallow invalid white-listing config', () => {
        disallowedConfigs.forEach((invalidConfg) => {
            if (WhitelistingUtils.whitelistingConfigRegex.test(invalidConfg)) {
                fail(`Failed to block disallowed config [${invalidConfg}] for whitelisting`);
            }
        });
    });

    it('should allow valid configs', () => {
        allowedConfigs.forEach((validConfg) => {
            if (!WhitelistingUtils.whitelistingConfigRegex.test(validConfg)) {
                fail(`Blocked a valid config [${validConfg}] for whitelisting`);
            }
        });
    });

    it('should test for allowed urls origins base on the allowed configuration.', () => {
        allowedOriginURLs.forEach((allowedUrl) => {
            if (!WhitelistingUtils.isAllowed(allowedUrl, window, allowedConfigRegexps)) {
                fail(`Blocked a valid url [${allowedUrl}] from whitelisting`);
            }
        });
    });

    it('should test for blocked urls origins base on the allowed configuration.', () => {
        disallowedOriginURLs.forEach((disallowedUrl) => {
            if (WhitelistingUtils.isAllowed(disallowedUrl, window, allowedConfigRegexps)) {
                fail(`Failed to block disallowed url [${disallowedUrl}] from whitelisting`);
            }
        });
    });

    it('getWhitelistFromScriptElement should include same origin by default', () => {
        const scriptElement = document.createElement('script');
        expect(WhitelistingUtils.getWhitelistFromScriptElement(scriptElement, window)).toEqual([
            `localhost:${location.port}`
        ]);
    });

    it('getWhitelistFromScriptElement should include all allow origin attribute values', () => {
        const whitelistingRegexes = [`localhost:${location.port}`].concat(allowedConfigs);
        const scriptElement = document.createElement('script');
        scriptElement.setAttribute(
            WhitelistingUtils.allowOriginAttributeName,
            allowedConfigs.join(',')
        );
        expect(WhitelistingUtils.getWhitelistFromScriptElement(scriptElement, window)).toEqual(
            whitelistingRegexes
        );
    });

    it('getWhitelistFromScriptElement should include all allow origin query param values', () => {
        const whitelistingRegexes = [`localhost:${location.port}`].concat(allowedConfigs);
        const scriptElement = document.createElement('script');
        scriptElement.src = `webApplicationInjector.js?${
            WhitelistingUtils.allowOriginQueryParamName
        }=${allowedConfigs.join(',')}`;
        expect(WhitelistingUtils.getWhitelistFromScriptElement(scriptElement, window)).toEqual(
            whitelistingRegexes
        );
    });

    it('getWhitelistFromScriptElement should include both attribute AND query param allow origins', () => {
        const whitelistingRegexes = [`localhost:${location.port}`].concat(allowedConfigs);
        const roughlyHalf = Math.ceil(allowedConfigs.length / 2);
        const firstHalfOfWhitelistingRegexes = allowedConfigs.slice(0, roughlyHalf);
        const secondHalfOfWhitelistingRegexes = allowedConfigs.slice(roughlyHalf);
        const scriptElement = document.createElement('script');
        scriptElement.setAttribute(
            WhitelistingUtils.allowOriginAttributeName,
            firstHalfOfWhitelistingRegexes.join(',')
        );
        scriptElement.src = `webApplicationInjector.js?${
            WhitelistingUtils.allowOriginQueryParamName
        }=${secondHalfOfWhitelistingRegexes.join(',')}`;
        expect(WhitelistingUtils.getWhitelistFromScriptElement(scriptElement, window)).toEqual(
            whitelistingRegexes
        );
    });

    describe('getSanitizedHostFromLocation()', () => {
        function getMockLocationObject(protocol: string, hostname: string, port: string): Location {
            return ({
                protocol,
                hostname,
                port
            } as unknown) as Location;
        }

        it('should return default http port 80', () => {
            const mockLocation = getMockLocationObject('http:', 'xyz', '');
            expect(WhitelistingUtils.getSanitizedHostFromLocation(mockLocation)).toBe('xyz:80');
        });

        it('should return default https port 443', () => {
            const mockLocation = getMockLocationObject('https:', 'xyz', '');
            expect(WhitelistingUtils.getSanitizedHostFromLocation(mockLocation)).toBe('xyz:443');
        });

        it('should return specific http port', () => {
            const mockLocation = getMockLocationObject('http:', 'xyz', '123');
            expect(WhitelistingUtils.getSanitizedHostFromLocation(mockLocation)).toBe('xyz:123');
        });

        it('should return specific https port', () => {
            const mockLocation = getMockLocationObject('http:', 'xyz', '456');
            expect(WhitelistingUtils.getSanitizedHostFromLocation(mockLocation)).toBe('xyz:456');
        });
    });
});
