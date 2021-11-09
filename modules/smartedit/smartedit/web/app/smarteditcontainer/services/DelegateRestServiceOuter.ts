/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import {
    GatewayProxied,
    IRestOptions,
    Page,
    Pageable,
    Payload,
    RestServiceFactory
} from 'smarteditcommons';
/*
 * internal service to proxy calls from inner RESTService to the outer restServiceFactory and the 'real' IRestService
 */

/** @internal */
@GatewayProxied()
@Injectable()
export class DelegateRestService {
    constructor(private restServiceFactory: RestServiceFactory) {}

    delegateForSingleInstance<T>(
        methodName: string,
        params: string | Payload,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<T> {
        const restService = this.restServiceFactory.get<T>(uri, identifier);
        if (metadataActivated) {
            restService.activateMetadata();
        }
        return restService.getMethodForSingleInstance(methodName)(params, options);
    }

    delegateForArray<T>(
        methodName: string,
        params: string | Payload,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<T[]> {
        const restService = this.restServiceFactory.get<T>(uri, identifier);
        if (metadataActivated) {
            restService.activateMetadata();
        }
        return restService.getMethodForArray(methodName)(params, options);
    }

    delegateForPage<T>(
        pageable: Pageable,
        uri: string,
        identifier: string,
        metadataActivated: boolean,
        options?: IRestOptions
    ): Promise<Page<T>> {
        const restService = this.restServiceFactory.get<T>(uri, identifier);
        if (metadataActivated) {
            restService.activateMetadata();
        }
        return restService.page(pageable, options);
    }
}
