/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import { IPositionRegistry, SeDowngradeService, YJQuery, YJQUERY_TOKEN } from 'smarteditcommons';

interface IPositionRegistryKey {
    element: HTMLElement;
    position: string;
}

/**
 * Service aimed at determining the list of registered DOM elements that have been repositioned, regardless of how, since it was last queried
 */
@SeDowngradeService(IPositionRegistry)
export class PositionRegistry implements IPositionRegistry {
    private positionRegistry: IPositionRegistryKey[] = [];

    constructor(@Inject(YJQUERY_TOKEN) private yjQuery: YJQuery) {}

    /**
     * registers a given node in the repositioning registry
     */
    public register(element: HTMLElement): void {
        this.unregister(element);
        this.positionRegistry.push({
            element,
            position: this.calculatePositionHash(element)
        });
    }

    /**
     * unregisters a given node from the repositioning registry
     */
    public unregister(element: HTMLElement): void {
        const entryToBeRemoved = this.positionRegistry.find((entry) => entry.element === element);
        const index = this.positionRegistry.indexOf(entryToBeRemoved);
        if (index > -1) {
            this.positionRegistry.splice(index, 1);
        }
    }

    /**
     * Method returning the list of nodes having been repositioned since last query
     */
    public getRepositionedComponents(): HTMLElement[] {
        return this.positionRegistry
            .filter((entry) => {
                // to ignore elements that would keep showing here because of things like display table-inline
                return this.yjQuery(entry.element).height() !== 0;
            })
            .filter((entry) => {
                const element = entry.element;
                const newPosition = this.calculatePositionHash(element);
                if (newPosition !== entry.position) {
                    entry.position = newPosition;
                    return true;
                } else {
                    return false;
                }
            })
            .map((entry) => entry.element);
    }

    /**
     * unregisters all nodes and cleans up
     */
    public dispose(): void {
        this.positionRegistry = [];
    }

    /**
     * for e2e test purposes
     */
    public _listenerCount(): number {
        return this.positionRegistry.length;
    }

    private floor(val: any): number {
        return Math.floor(val * 100) / 100;
    }

    private calculatePositionHash(element: HTMLElement): string {
        const rootPosition = this.yjQuery('body')[0].getBoundingClientRect();
        const position = element.getBoundingClientRect();
        return (
            this.floor(position.top - rootPosition.top) +
            '_' +
            this.floor(position.left - rootPosition.left)
        );
    }
}
