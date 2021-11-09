/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as $script from 'scriptjs';

export default abstract class WebUtils {
    static readonly webappScriptId = 'smartedit-injector';
    static readonly webappScriptName = 'webApplicationInjector';

    /**
     * Get script element of webApplicationInjector.js from storefront page
     */
    static getWebappScriptElementFromDocument(document: Document): HTMLScriptElement {
        if (document.currentScript) {
            if (!(document.currentScript instanceof HTMLScriptElement)) {
                throw new Error(
                    'getWebappScriptElementFromDocument() found non htlm script element'
                );
            }
            return document.currentScript;
        }

        const scriptElement = document.querySelector<HTMLScriptElement>(
            `script#${WebUtils.webappScriptId}`
        );
        if (scriptElement) {
            return scriptElement;
        }

        // This will be the case for IE (if no smartedit-injector attr used)
        const nodeListElements: NodeListOf<HTMLScriptElement> = document.querySelectorAll(
            `script[src*=${WebUtils.webappScriptName}]`
        );
        if (nodeListElements.length !== 1) {
            throw new Error(
                `SmartEdit unable to load - invalid ${WebUtils.webappScriptName} script tag`
            );
        }
        return nodeListElements.item(0);
    }

    static extractQueryParameter(url: string, queryParameterName: string): string {
        const queryParameters = {} as any;
        url.replace(
            /([?&])([^&=]+)=([^&]*)?/g,
            ($0: any, $1: any, parameterKey: any, parameterValue: any) => {
                queryParameters[parameterKey] = parameterValue;
                return '';
            }
        );
        return queryParameters[queryParameterName];
    }

    static injectJS(srcs: string[], index = 0) {
        if (srcs.length && index < srcs.length) {
            WebUtils.getScriptJs()(srcs[index], function() {
                WebUtils.injectJS(srcs, index + 1);
            });
        }
    }

    static injectCSS(head: HTMLHeadElement, cssPaths: string[], index = 0) {
        if (!cssPaths || cssPaths.length === 0) {
            return;
        }
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = cssPaths[index];
        head.appendChild(link);
        if (index + 1 < cssPaths.length) {
            WebUtils.injectCSS(head, cssPaths, index + 1);
        }
    }

    static getScriptJs() {
        return $script;
    }
}
