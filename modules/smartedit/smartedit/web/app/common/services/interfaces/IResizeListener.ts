/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export abstract class IResizeListener {
    unregister(element: HTMLElement): void {
        'proxyFunction';
    }
    fix(element: HTMLElement): void {
        'proxyFunction';
    }
    register(element: HTMLElement, listener: (element: HTMLElement) => void): void {
        'proxyFunction';
    }
    init(): void {
        'proxyFunction';
    }
    dispose(): void {
        'proxyFunction';
    }
}
