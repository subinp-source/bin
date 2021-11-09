/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export abstract class IPositionRegistry {
    register(element: HTMLElement): void {
        'proxyFunction';
    }
    unregister(element: HTMLElement): void {
        'proxyFunction';
    }
    getRepositionedComponents(): HTMLElement[] {
        'proxyFunction';
        return null;
    }
    dispose(): void {
        'proxyFunction';
    }
}
