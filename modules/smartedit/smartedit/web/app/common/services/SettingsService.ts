/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { Cached, IRestService, ISettingsService, RestServiceFactory, TypedMap } from '@smart/utils';
import { rarelyChangingContent } from './cache';
import { SeDowngradeService } from '../di';
import { SETTINGS_URI } from '../utils';

/*
 * Meant to be a non-protected API
 */
@SeDowngradeService(ISettingsService)
@Injectable()
export class SettingsService {
    private restService: IRestService<TypedMap<string | boolean | string[]>>;

    constructor(restServicefactory: RestServiceFactory) {
        this.restService = restServicefactory.get<TypedMap<string | boolean | string[]>>(
            SETTINGS_URI
        );
    }

    @Cached({ actions: [rarelyChangingContent] })
    load(): Promise<TypedMap<string | boolean | string[]>> {
        return this.restService.get();
    }

    get(key: string): Promise<string> {
        return this.load().then((map) => map[key] as string);
    }

    getBoolean(key: string): Promise<boolean> {
        return this.load().then((map) => map[key] === true || map[key] === 'true');
    }

    getStringList(key: string): Promise<string[]> {
        return this.load().then((map) => map[key] as string[]);
    }
}
