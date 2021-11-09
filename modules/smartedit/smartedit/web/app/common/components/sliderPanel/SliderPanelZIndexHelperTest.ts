/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { SliderPanelZIndexHelper } from './SliderPanelZIndexHelper';

describe('SliderPanelZIndexHelper', () => {
    let helper: SliderPanelZIndexHelper;
    const nodeTree0 = `
        <div style="z-index: 10">
            <div style="z-index: 5"></div>
            <div style="z-index: 10">
                <div style="z-index: 15">
                    <div style="z-index: 20"></div>
                    <div style="z-index: 30"></div>
                </div>
            </div>
        </div>
    `;

    const nodeTree1 = '<div></div>';

    const nodeTree2 = `
        <div>
            <div></div>
            <div>
                <div></div>
            </div>
        </div>
    `;

    const nodeTree3 = `
        <div>
            <div style="z-index: 20"></div>
            <script style="z-index: 70"></script>
            <base style="z-index: 40" />
            <link style="z-index: 50" />
        </div>
    `;

    beforeEach(() => {
        helper = new SliderPanelZIndexHelper();
    });

    it('Returns the highest zIndex value within the node tree', () => {
        const expected = 30;
        const result = helper.getHighestZIndex(angular.element(nodeTree0));

        expect(result).toBe(expected);
    });

    it('Returns 0 when the node tree is empty', () => {
        const expected = 0;
        const result = helper.getHighestZIndex(angular.element(nodeTree1));

        expect(result).toBe(expected);
    });

    it('Returns 0 when the node tree elements have no specified zIndex', () => {
        const expected = 0;
        const result = helper.getHighestZIndex(angular.element(nodeTree2));

        expect(result).toBe(expected);
    });

    it('Ignores the blacklisted node names', () => {
        const expected = 20;
        const result = helper.getHighestZIndex(angular.element(nodeTree3));

        expect(result).toBe(expected);
    });
});
