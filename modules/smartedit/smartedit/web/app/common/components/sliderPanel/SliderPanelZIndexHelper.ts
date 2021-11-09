/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { flatten } from 'lodash';

export class SliderPanelZIndexHelper {
    static BLACKLISTED_NODE_NAMES: Set<string> = new Set(['SCRIPT', 'LINK', 'BASE']);

    public getHighestZIndex(node: JQuery<HTMLElement>): number {
        // retrieve a highest value from array of zIndex integers

        return Math.max(
            ...this.getChildrenNodesFromTreeOrLeaf(node[0])
                .filter((elem) => this.filterBlacklistedNodes(elem))
                .map((elem: HTMLElement) => this.mapToZIndexIntegers(elem))
        );
    }

    private filterBlacklistedNodes(elem: HTMLElement): boolean {
        // exclude blacklisted nodenames

        return !SliderPanelZIndexHelper.BLACKLISTED_NODE_NAMES.has(elem.nodeName);
    }

    private mapToZIndexIntegers(elem: HTMLElement): number {
        // Retrieve zIndex integer value from node, fallback with 0 value in case of NaN

        return parseInt(angular.element(elem).css('z-index'), 10) || 0;
    }

    private getChildrenNodesFromTreeOrLeaf(node: HTMLElement): HTMLElement[] {
        // Return recurring flat array of node and it's children

        return [
            node,
            ...flatten(
                Array.from(node.children).map((child: HTMLElement) =>
                    this.getChildrenNodesFromTreeOrLeaf(child)
                )
            )
        ];
    }
}
