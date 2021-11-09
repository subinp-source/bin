/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';

/* @internal */
export interface AggregatedNode {
    node: HTMLElement;
    parent: HTMLElement;
}

/* @internal */
export interface TargetedNode {
    node: HTMLElement;
    oldAttributes: TypedMap<string>;
}

/* @internal */
export interface ComponentObject {
    isIntersecting: boolean;
    component: HTMLElement;
    parent: HTMLElement;
}

/* @internal */
export interface RemoveComponentObject extends ComponentObject {
    oldAttributes?: TypedMap<string>; // we need old attributes in case of all other attributes were removed from the component.
}

/* @internal */
export interface ComponentEntry extends ComponentObject {
    processed: string;
    oldProcessedValue: string;
}

export abstract class ISmartEditContractChangeListener {
    _newMutationObserver(callback: MutationCallback): MutationObserver {
        return null;
    }

    _newIntersectionObserver(callback: IntersectionObserverCallback): IntersectionObserver {
        return null;
    }

    _addToComponentQueue(entry: IntersectionObserverEntry): void {
        return null;
    }

    _componentsQueueLength(): number {
        return null;
    }

    isExtendedViewEnabled(): boolean {
        return null;
    }

    setEconomyMode(_mode: boolean): void {
        return null;
    }

    initListener(): void {
        return null;
    }

    _processQueue(): void {
        return null;
    }

    isIntersecting(obj: ComponentEntry): boolean {
        return null;
    }

    _rawProcessQueue(): void {
        return null;
    }

    _addComponents(componentsObj: ComponentEntry[]): void {
        return null;
    }

    _removeComponents(componentsObj: RemoveComponentObject[], forceRemoval: boolean = false): void {
        return null;
    }

    _registerSizeAndPositionListeners(component: HTMLElement): void {
        return null;
    }

    _unregisterSizeAndPositionListeners(component: HTMLElement): void {
        return null;
    }

    stopListener(): void {
        return null;
    }

    _stopExpendableListeners(): void {
        return null;
    }

    _startExpendableListeners(): void {
        return null;
    }

    onComponentsAdded(callback: (components: HTMLElement[], isEconomyMode: boolean) => void): void {
        return null;
    }

    onComponentsRemoved(
        callback: (
            components: { component: HTMLElement; parent: HTMLElement }[],
            isEconomyMode: boolean
        ) => void
    ): void {
        return null;
    }

    onComponentChanged(
        callback: (component: HTMLElement, oldAttributes: TypedMap<string>) => void
    ): void {
        return null;
    }

    onComponentResized(callback: (component: HTMLElement) => void): void {
        return null;
    }

    onComponentRepositioned(callback: (component: HTMLElement) => void): void {
        return null;
    }

    onPageChanged(callback: (pageUUID: string) => void): void {
        return null;
    }
}
