/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import {
    GatewayProxied,
    IIframeClickDetectionService,
    SeDowngradeService,
    TypedMap
} from 'smarteditcommons';

@SeDowngradeService(IIframeClickDetectionService)
@GatewayProxied('onIframeClick')
@Injectable()
export class IframeClickDetectionService extends IIframeClickDetectionService {
    private callbacks: TypedMap<() => void> = {};

    constructor() {
        super();
    }

    public registerCallback(id: string, callback: () => void): () => void {
        this.callbacks[id] = callback;
        return this.removeCallback.bind(this, id);
    }

    public removeCallback(id: string): boolean {
        if (this.callbacks[id]) {
            delete this.callbacks[id];
            return true;
        }
        return false;
    }

    /**
     * Triggers all callbacks currently registered to the service. This function is registered as a listener through
     * the GatewayProxy
     */
    public onIframeClick(): void {
        for (const ref in this.callbacks) {
            if (this.callbacks.hasOwnProperty(ref)) {
                this.callbacks[ref]();
            }
        }
    }
}
