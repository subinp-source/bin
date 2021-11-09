/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { GatewayProxied, IRestOptions, Page, Pageable, Payload } from 'smarteditcommons';
/*
 * internal service to proxy calls from inner RESTService to the outer restServiceFactory and the 'real' IRestService
 */

/** @internal */
@GatewayProxied()
@Injectable()
export class DelegateRestService {
    delegateForSingleInstance<T>(
        methodName: string,
        params: string | Payload,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<T> {
        'proxyFunction';
        return null;
    }

    delegateForArray<T>(
        methodName: string,
        params: string | Payload,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<T[]> {
        'proxyFunction';
        return null;
    }

    delegateForPage<T>(
        pageable: Pageable,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<Page<T>> {
        'proxyFunction';
        return null;
    }
}
