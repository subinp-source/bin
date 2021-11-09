/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DelegateRestService } from './DelegateRestServiceInner';
import { IRestOptions, IRestService, Page, Pageable, Payload } from 'smarteditcommons';

/** @internal */
export class RestService<T> implements IRestService<T> {
    private metadataActivated: boolean = false;

    constructor(
        private delegateRestService: DelegateRestService,
        private uri: string,
        private identifier: string
    ) {}

    getById<S extends T = T>(id: string, options?: IRestOptions): Promise<S> {
        return this.delegateRestService.delegateForSingleInstance(
            'getById',
            id,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }
    get<S extends T = T>(searchParams: Payload, options?: IRestOptions): Promise<S> {
        return this.delegateRestService.delegateForSingleInstance(
            'get',
            searchParams,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }
    update<S extends T = T>(payload: Payload, options?: IRestOptions): Promise<S> {
        return this.delegateRestService.delegateForSingleInstance(
            'update',
            payload,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }

    patch<S extends T = T>(payload: Payload, options?: IRestOptions): Promise<S> {
        return this.delegateRestService.delegateForSingleInstance(
            'patch',
            payload,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }

    save<S extends T = T>(payload: Payload, options?: IRestOptions): Promise<S> {
        return this.delegateRestService.delegateForSingleInstance(
            'save',
            payload,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }
    query<S extends T = T>(searchParams: Payload, options?: IRestOptions): Promise<S[]> {
        return this.delegateRestService.delegateForArray(
            'query',
            searchParams,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }
    page<S extends T = T>(pageable: Pageable, options?: IRestOptions): Promise<Page<S>> {
        return this.delegateRestService.delegateForPage<S>(
            pageable,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }
    remove(payload: Payload, options?: IRestOptions): Promise<void> {
        return this.delegateRestService.delegateForSingleInstance(
            'remove',
            payload,
            this.uri,
            this.identifier,
            this.metadataActivated,
            options
        );
    }

    activateMetadata(): void {
        // will activate response headers appending
        this.metadataActivated = true;
    }
}
