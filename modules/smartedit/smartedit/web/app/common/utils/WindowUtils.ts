/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import * as lodash from 'lodash';
import { NgModule, NgZone } from '@angular/core';
import { SeDowngradeService } from 'smarteditcommons/di/SeDowngradeService';
import { SeConstructor } from 'smarteditcommons/di/types';
import { YJQuery } from 'smarteditcommons/services';
import { TypedMap } from '../dtos';
import {
    annotationService,
    functionsUtils,
    urlUtils,
    WindowUtils as ParentWindowUtils
} from '@smart/utils';

declare global {
    /* @internal */
    interface InternalSmartedit {
        // modules loaded by extensions of smartedit or smarteditcontainer
        modules: TypedMap<NgModule>;
        // modules statically loaded for smarteditloader and smarteditcontainer
        pushedModules: NgModule[];

        /*
         * place where our custom instrumentation will save arguments of all decorators in the followign format:
         * key: `{constructorName-decoratorName:definition`
         * value: the definition object
         */
        smarteditDecoratorPayloads: TypedMap<any>;
        addDecoratorPayload: (className: string, decoratorName: string, payload: any) => void;
        getDecoratorPayload: (decorator: string, constructor: SeConstructor) => any;
        getComponentDecoratorPayload: (
            className: string
        ) => {
            selector: string;
            template: string;
        };

        downgradedService: TypedMap<SeConstructor>;
    }
    interface Window {
        Zone: any;
        angular: angular.IAngularStatic;
        CKEDITOR: any;
        smarteditLodash: lodash.LoDashStatic;
        smarteditJQuery: YJQuery;
        __karma__: any;
        /* @internal */
        __smartedit__: InternalSmartedit;

        pushModules(...modules: any[]): void;
    }
}

/* forbiddenNameSpaces window._:false */
window.__smartedit__ = window.__smartedit__ || ({} as InternalSmartedit);
function getDecoratorKey(className: string, decoratorName: string) {
    return `${className}-${decoratorName}:definition`;
}

lodash.merge(window.__smartedit__, {
    modules: {},
    pushedModules: [],
    smarteditDecoratorPayloads: {},

    addDecoratorPayload(decorator: string, className: string, payload: any) {
        const key = getDecoratorKey(decorator, className);
        if (window.__smartedit__.smarteditDecoratorPayloads[key]) {
            throw new Error('Duplicate class/decorator pair');
        }
        window.__smartedit__.smarteditDecoratorPayloads[key] = payload;
    },

    getDecoratorPayload(decorator: string, constructor: SeConstructor): any {
        const className = functionsUtils.getConstructorName(
            annotationService.getOriginalConstructor(constructor)
        );
        return window.__smartedit__.smarteditDecoratorPayloads[
            getDecoratorKey(decorator, className)
        ];
    },

    getComponentDecoratorPayload(className: string) {
        return window.__smartedit__.smarteditDecoratorPayloads[
            getDecoratorKey('Component', className)
        ];
    },

    downgradedService: {}
});

/**
 * @ngdoc service
 * @name functionsModule.service:pushModules
 *
 * @description
 * Add here a spread of modules containing invocations of HttpBackendService to mocks some calls to the backend
 */
window.pushModules = (...modules: NgModule[]): void => {
    window.__smartedit__.pushedModules = window.__smartedit__.pushedModules.concat(modules);
};

/**
 * @ngdoc service
 * @name functionsModule.service:WindowUtils
 *
 * @description
 * A collection of utility methods for windows.
 */
@SeDowngradeService()
export class WindowUtils extends ParentWindowUtils {
    static SMARTEDIT_IFRAME_ID = 'ySmartEditFrame';

    private trustedIframeDomain: string;

    constructor(ngZone?: NgZone) {
        super(ngZone);
    }

    /**
     * @ngdoc method
     * @name functionsModule.service:WindowUtils#getGatewayTargetFrame
     * @methodOf functionsModule.service:WindowUtils
     *
     * @description
     * Given the current frame, retrieves the target frame for gateway purposes
     *
     * @returns {Window} The content window or null if it does not exists.
     */
    getGatewayTargetFrame = (): Window => {
        if (this.isIframe()) {
            return this.getWindow().parent;
        } else if (this.getWindow().document.getElementById(WindowUtils.SMARTEDIT_IFRAME_ID)) {
            return (this.getWindow().document.getElementById(
                WindowUtils.SMARTEDIT_IFRAME_ID
            ) as HTMLIFrameElement).contentWindow;
        }
        return null;
    };

    getSmarteditIframe(): HTMLElement {
        return document.querySelector('iframe#' + WindowUtils.SMARTEDIT_IFRAME_ID);
    }

    getIframe(): HTMLElement {
        return document.querySelector('iframe');
    }

    setTrustedIframeDomain(trustedIframeSource: string): void {
        this.trustedIframeDomain = urlUtils.getOrigin(trustedIframeSource);
    }

    getTrustedIframeDomain(): string {
        return this.trustedIframeDomain;
    }

    isCrossOrigin(location: string): boolean {
        return urlUtils.getOrigin() !== urlUtils.getOrigin(location);
    }
}

export const windowUtils = new WindowUtils();
