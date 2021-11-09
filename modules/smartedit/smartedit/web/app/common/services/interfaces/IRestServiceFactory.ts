/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IRestService } from '@smart/utils';

export abstract class IRestServiceFactory {
    get<T>(uri: string, identifier?: string): IRestService<T> {
        'proxyFunction';

        return null;
    }

    setDomain?(domain: string): void {
        'proxyFunction';
    }

    setBasePath?(basePath: string): void {
        'proxyFunction';
    }

    setGlobalBasePath?(globalDomain: string): void {
        'proxyFunction';
    }
}
