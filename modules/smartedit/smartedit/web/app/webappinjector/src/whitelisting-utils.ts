/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import WebUtils from './web-utils';

export default abstract class WhitelistingUtils {
    static readonly whitelistingConfigRegex = new RegExp(
        /^(([-*a-zA-Z0-9]+[.])*([-a-zA-Z0-9]+[.]))?[-a-zA-Z0-9]+(:[0-9]{1,5})$/
    );

    static readonly allowOriginAttributeName = 'data-smartedit-allow-origin';
    static readonly allowOriginQueryParamName = 'allow-origin';

    static readonly whitelistingErrorMsg = `
		Allowed whitelist characters are a-z, A-Z, 0-9, -, period, or *
		The wildcard * can be used to represent a prefixed domain, Good example: *.domain.com:80
		but not a suffix or port, Bad examples: subdomain.*.com subdomain.domain.com:*.
		Every whitelisting must contain a specific port.
	`;

    /**
     * Convert a whitelisting provided to webApplicationInjector into a real regexp
     */
    static convertWhitelistingToRegexp(whitelistings: string[]): RegExp[] {
        whitelistings = whitelistings || [];
        return whitelistings.map((origins) => {
            const trimmed = origins.trim();
            if (WhitelistingUtils.whitelistingConfigRegex.test(trimmed)) {
                const regexpKey = ['^', '$'].join(
                    trimmed.replace(/\./g, '\\.').replace(/\*/g, '[-a-zA-Z0-9]*')
                );
                return new RegExp(regexpKey);
            }
            throw new Error(WhitelistingUtils.whitelistingErrorMsg);
        });
    }

    /**
     * Extract the whitelisting of smartedit location(s) from the
     * webApplicationInjector.js script element
     * @returns string[] white listed regexs
     */
    static getWhitelistFromScriptElement(
        scriptElement: HTMLScriptElement,
        global: Window
    ): string[] {
        // A) default value (same origin always supported)
        let whiteListing = [WhitelistingUtils.getSanitizedHostFromLocation(global.location)];

        // B) append any whitelisting provided by attribute
        const attributeList =
            scriptElement.getAttribute(WhitelistingUtils.allowOriginAttributeName) || '';
        if (attributeList) {
            whiteListing = whiteListing.concat(attributeList.split(','));
        }

        // C) append any whitelisting provided by query param
        let queryList = '';
        const url = global.document.createElement('a');
        url.href = scriptElement.src;
        const encodedAllowOrigin = WebUtils.extractQueryParameter(
            url.search,
            WhitelistingUtils.allowOriginQueryParamName
        );
        if (encodedAllowOrigin) {
            queryList = decodeURI(encodedAllowOrigin);
            if (queryList) {
                queryList.split(',').forEach((s) => whiteListing.push(s));
            }
        }

        return whiteListing;
    }

    static isAllowed(originUrl: string, global: Window, whiteListedOriginRegexes: RegExp[]) {
        if (!/^(https?:)\/\/([-.a-zA-Z0-9]+(:[0-9]{1,5})?)$/.test(originUrl)) {
            return false;
        }

        const originLocation = global.document.createElement('a');
        originLocation.href = originUrl;

        if (global.location.protocol === 'https:' && originLocation.protocol !== 'https:') {
            return false;
        }

        return whiteListedOriginRegexes.some((regex: RegExp) => {
            regex.lastIndex = 0;
            return regex.test(WhitelistingUtils.getSanitizedHostFromLocation(originLocation));
        });
    }

    // on many browsers, location.port and location.host will not return a port if using default ports 80/443
    static getSanitizedHostFromLocation(location: HTMLHyperlinkElementUtils | Location) {
        // TODO - how will this react to file://? do we care?
        const port =
            location.port || (location.protocol.replace(/:/g, '') === 'https' ? '443' : '80');
        return `${location.hostname}:${port}`;
    }
}
