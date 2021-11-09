/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';

import { nodeUtils } from 'smarteditcommons';

describe('Node Utils Test', () => {
    it('compareHTMLElementsPosition using sourceIndex should return a function to compare DOM elements according to their position in the DOM', () => {
        // GIVEN
        const element1 = {
            name: 'element1',
            sourceIndex: 0
        };
        const element2 = {
            name: 'element2',
            sourceIndex: 1
        };
        const element3 = {
            name: 'element3',
            sourceIndex: 2
        };
        const mockElementsUnsorted = [element2, element1, element3];

        // WHEN
        const sortedElements = mockElementsUnsorted.sort(nodeUtils.compareHTMLElementsPosition());

        // THEN
        expect(sortedElements).toEqualData([element1, element2, element3]);
    });

    it('compareHTMLElementsPosition using compareDocumentPosition should return a function to compare DOM elements according to their position in the DOM', () => {
        // GIVEN
        const element1 = {
            name: 'element1',
            compareDocumentPosition: () => 0
        };
        const element2 = {
            name: 'element2',
            compareDocumentPosition: (el: any) => (el.name === 'element1' ? 2 : 0)
        };
        const element3 = {
            name: 'element3',
            compareDocumentPosition: () => -1
        };
        const mockElementsUnsorted = [element2, element1, element3];

        // WHEN
        const sortedElements = mockElementsUnsorted.sort(nodeUtils.compareHTMLElementsPosition());

        // THEN
        expect(sortedElements).toEqualData([element1, element2, element3]);
    });

    it('compareHTMLElementsPosition using compareDocumentPosition and a key should return a function to compare DOM elements according to their position in the DOM', () => {
        // GIVEN
        const element1 = {
            name: 'element1',
            compareDocumentPosition: () => 0
        };
        const element2 = {
            name: 'element2',
            compareDocumentPosition: (el: any) => (el.name === 'element1' ? 2 : 0)
        };
        const element3 = {
            name: 'element3',
            compareDocumentPosition: () => -1
        };
        const list = [
            {
                element: element2
            },
            {
                element: element1
            },
            {
                element: element3
            }
        ];

        // WHEN
        list.sort(nodeUtils.compareHTMLElementsPosition('element'));

        // THEN
        expect(list).toEqualData([
            {
                element: element1
            },
            {
                element: element2
            },
            {
                element: element3
            }
        ]);
    });

    it('isPointOverElement will return true if given point is over given element', () => {
        spyOn(Element.prototype, 'getBoundingClientRect').and.returnValue({
            width: 50,
            height: 50,
            top: 50,
            left: 50,
            bottom: 100,
            right: 100
        });

        const element = document.createElement('span');

        expect(nodeUtils.isPointOverElement({ x: 50, y: 50 }, element)).toBe(true);
        expect(nodeUtils.isPointOverElement({ x: 49, y: 49 }, element)).toBe(false);
        expect(nodeUtils.isPointOverElement({ x: 101, y: 49 }, element)).toBe(false);
    });

    it('areIntersecting will return true if elements are intersecting', () => {
        const elementsPair1 = {
            clientRect1: {
                width: 50,
                height: 50,
                top: 0,
                left: 0,
                bottom: 50,
                right: 50
            } as ClientRect,
            clientRect2: {
                width: 50,
                height: 50,
                top: 0,
                left: 0,
                bottom: 50,
                right: 50
            } as ClientRect
        };

        const elementsPair2 = {
            clientRect1: {
                width: 50,
                height: 50,
                top: 0,
                left: 0,
                bottom: 50,
                right: 50
            } as ClientRect,
            clientRect2: {
                width: 50,
                height: 50,
                top: 0,
                left: 50,
                bottom: 50,
                right: 100
            } as ClientRect
        };

        const areIntersecting1 = nodeUtils.areIntersecting(
            elementsPair1.clientRect1,
            elementsPair1.clientRect2
        );
        expect(areIntersecting1).toBe(true);

        const areIntersecting2 = nodeUtils.areIntersecting(
            elementsPair2.clientRect1,
            elementsPair2.clientRect2
        );
        expect(areIntersecting2).toBe(true);
    });

    it('areIntersecting will return false if elements are not intersecting', () => {
        const clientRect1: ClientRect = {
            width: 50,
            height: 50,
            top: 0,
            left: 0,
            bottom: 50,
            right: 50
        };
        const clientRect2: ClientRect = {
            width: 50,
            height: 50,
            top: 0,
            left: 51,
            bottom: 50,
            right: 101
        };

        const areIntersecting = nodeUtils.areIntersecting(clientRect1, clientRect2);
        expect(areIntersecting).toBe(false);
    });
});
