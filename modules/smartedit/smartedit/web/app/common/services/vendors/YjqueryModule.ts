/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { diBridgeUtils } from '../../di';
import { moduleUtils } from '../../utils';

export interface YJQuery extends JQueryStatic {
    /**
     * Return the CSS path of the wrapped JQuery element.
     */
    getCssPath: () => string;
}

export const YJQUERY_TOKEN: string = 'yjQuery';

/**
 * Return a jQuery wrapping factory while preserving potentially pre-existing jQuery in storefront and SmartEditContainer
 */
export function yjQueryServiceFactory(): YJQuery {
    /* forbiddenNameSpaces (window as any):false */
    const namespace = 'smarteditJQuery';
    if (!window[namespace]) {
        if ((window as any).$ && (window as any).$.noConflict) {
            window[namespace] = (window as any).$.noConflict(true);
        } else {
            window[namespace] = (window as any).$;
        }
    }
    return window[namespace];
}

/**
 * @ngdoc overview
 * @name YjqueryModule
 * @description
 * This module manages the use of the jQuery library.
 * It enables to work with a "noConflict" version of jQuery in a storefront that may contain another version
 */
@NgModule({
    providers: [
        {
            provide: YJQUERY_TOKEN,
            useFactory: yjQueryServiceFactory
        },
        moduleUtils.initialize(
            (yjQuery: JQueryStatic) => {
                yjQuery.fn.extend({
                    getCssPath() {
                        let path: string;
                        let node = this;
                        while (node.length) {
                            const realNode = node[0];
                            const name = realNode.className;
                            if (realNode.tagName === 'BODY') {
                                break;
                            }
                            node = node.parent();
                            path = name + (path ? '>' + path : '');
                        }
                        return path;
                    }
                });
                diBridgeUtils.downgradeService(YJQUERY_TOKEN, null, YJQUERY_TOKEN);
            },
            [YJQUERY_TOKEN]
        )
    ]
})
export class YjqueryModule {}
