/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import {
    GatewayProxied,
    ITranslationsFetchService,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';

/* @internal */
@SeDowngradeService(ITranslationsFetchService)
@GatewayProxied()
@Injectable()
export class TranslationsFetchService extends ITranslationsFetchService {
    get(lang: string): Promise<TypedMap<string>> {
        'proxyFunction';
        return null;
    }
    isReady(): Promise<boolean> {
        'proxyFunction';
        return null;
    }
    waitToBeReady(): Promise<void> {
        'proxyFunction';
        return null;
    }
}
