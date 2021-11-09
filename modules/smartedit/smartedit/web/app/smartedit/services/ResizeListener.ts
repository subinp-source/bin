/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import ResizeObserver from 'resize-observer-polyfill';

import { IResizeListener, SeDowngradeService } from 'smarteditcommons';

interface IResizeListenerRegistryValue {
    listener: () => void;
}

@SeDowngradeService(IResizeListener)
export class ResizeListener {
    private _resizeObserver: ResizeObserver;
    private resizeListenersRegistry: Map<HTMLElement, IResizeListenerRegistryValue> = new Map<
        HTMLElement,
        IResizeListenerRegistryValue
    >();

    constructor() {
        this._resizeObserver = new ResizeObserver((entries) => {
            for (const entry of entries) {
                const element = entry.target as HTMLElement;
                const registryElement = this.resizeListenersRegistry.get(element);
                registryElement.listener();
            }
        });
    }

    /**
     * registers a resize listener of a given node
     */
    public register(element: HTMLElement, listener: () => void): void {
        if (!this.resizeListenersRegistry.has(element)) {
            this.resizeListenersRegistry.set(element, { listener });
            this._resizeObserver.observe(element);
        }
    }

    /**
     * unregisters listeners on all nodes and cleans up
     */
    public dispose(): void {
        this.resizeListenersRegistry.forEach((entry, element) => {
            this.unregister(element);
        });
    }

    /**
     * unregisters the resize listener of a given node
     */
    public unregister(element: HTMLElement): void {
        if (this.resizeListenersRegistry.has(element)) {
            this._resizeObserver.unobserve(element);
            this.resizeListenersRegistry.delete(element);
        }
    }

    /*
     * for test purposes
     */
    public _listenerCount(): number {
        return this.resizeListenersRegistry.size;
    }
}
