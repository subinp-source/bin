/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {
    rarelyChangingContent,
    Cached,
    GatewayProxied,
    I18N_RESOURCE_URI,
    ITranslationsFetchService,
    PromiseUtils,
    RestServiceFactory,
    SeDowngradeService,
    TranslationMap
} from 'smarteditcommons';

interface WrappedTransationMap {
    value: TranslationMap;
}

@SeDowngradeService(ITranslationsFetchService)
@GatewayProxied()
/*
 * this service, provider in SeTranslationModule, needs be accessible to root
 * so that when downgrading it to legacy usage it will be found in DI
 */
@Injectable({ providedIn: 'root' })
export class TranslationsFetchService extends ITranslationsFetchService {
    private ready = false;

    constructor(private httpClient: HttpClient, private promiseUtils: PromiseUtils) {
        super();
    }

    @Cached({ actions: [rarelyChangingContent] })
    get(lang: string): Promise<TranslationMap> {
        return this.httpClient
            .get<TranslationMap | WrappedTransationMap>(
                `${RestServiceFactory.getGlobalBasePath()}${I18N_RESOURCE_URI}/${lang}`,
                {
                    responseType: 'json'
                }
            )
            .pipe(
                map((result) =>
                    (result as WrappedTransationMap).value
                        ? (result as WrappedTransationMap).value
                        : (result as TranslationMap)
                )
            )
            .toPromise()
            .then((result) => {
                this.ready = true;
                return result;
            });
    }

    isReady(): Promise<boolean> {
        return Promise.resolve(this.ready);
    }

    waitToBeReady(): Promise<void> {
        return this.promiseUtils.resolveToCallbackWhenCondition(
            () => this.ready,
            () => {
                //
            }
        );
    }
}
