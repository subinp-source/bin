/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PositionRegistry } from 'smartedit/services/PositionRegistry';
import { YJQuery } from 'smarteditcommons';

describe('positionRegistry', () => {
    let positionRegistry: PositionRegistry;
    let yjQuery: jasmine.Spy;
    let body: jasmine.SpyObj<HTMLBodyElement>;
    let element1: jasmine.SpyObj<HTMLElement>;
    let element2: jasmine.SpyObj<HTMLElement>;
    let element3: jasmine.SpyObj<HTMLElement>;
    let bodyBoundingRect: Partial<ClientRect>;
    let element1BoundingRect: Partial<ClientRect>;
    let element2BoundingRect: Partial<ClientRect>;
    let element3BoundingRect: Partial<ClientRect>;

    beforeEach(() => {
        bodyBoundingRect = {
            top: 5,
            left: 7
        };
        element1BoundingRect = {
            top: 1,
            left: 2
        };
        element2BoundingRect = {
            top: 2,
            left: 3
        };
        element3BoundingRect = {
            top: 3,
            left: 4
        };

        body = jasmine.createSpyObj('body', ['getBoundingClientRect']);
        body.getBoundingClientRect.and.returnValue(bodyBoundingRect);
        element1 = jasmine.createSpyObj('element1', ['getBoundingClientRect', 'height']);
        element1.getBoundingClientRect.and.returnValue(element1BoundingRect);
        element2 = jasmine.createSpyObj('element2', ['getBoundingClientRect', 'height']);
        element2.getBoundingClientRect.and.returnValue(element2BoundingRect);
        element3 = jasmine.createSpyObj('element3', ['getBoundingClientRect', 'height']);
        element3.getBoundingClientRect.and.returnValue(element3BoundingRect);

        yjQuery = jasmine.createSpy('yjQuery');
        (yjQuery as any).fn = {
            extend() {
                return;
            }
        };
        yjQuery.and.callFake((selector: jasmine.SpyObj<HTMLElement> | 'body') => {
            if (selector === 'body') {
                return [body];
            } else {
                return selector;
            }
        });
    });

    beforeEach(() => {
        positionRegistry = new PositionRegistry((yjQuery as unknown) as YJQuery);

        positionRegistry.register(element1);
        positionRegistry.register(element2);
        positionRegistry.register(element3);
    });

    afterEach(() => {
        positionRegistry.dispose();
    });

    it('when element positions change they are detected', () => {
        element1BoundingRect.left = 2.01;
        element3BoundingRect.top = 10;

        const repositionedComponents = positionRegistry.getRepositionedComponents();

        expect(repositionedComponents.length).toEqualData(2);
        expect(repositionedComponents[0]).toBe(element1);
        expect(repositionedComponents[1]).toBe(element3);
    });

    it('reregistering an element is innocuous', () => {
        positionRegistry.register(element1);

        element1BoundingRect.left = 2.01;
        element3BoundingRect.top = 10;

        const repositionedComponents = positionRegistry.getRepositionedComponents();

        expect(repositionedComponents.length).toEqualData(2);
        expect(repositionedComponents[0]).toBe(element3);
        expect(repositionedComponents[1]).toBe(element1);
    });

    it('positions change less than a 100th pixel are not detected', () => {
        element1BoundingRect.left = 2.001;

        expect(positionRegistry.getRepositionedComponents().length).toEqualData(0);
    });

    it('positions change are only detected once', () => {
        element1BoundingRect.left = 2.01;
        element3BoundingRect.top = 10;

        positionRegistry.getRepositionedComponents();
        const repositionedComponents = positionRegistry.getRepositionedComponents();

        expect(repositionedComponents.length).toEqualData(0);
    });

    it('dispose will empty registry', () => {
        element1BoundingRect.left = 2.01;
        element2BoundingRect.left = 10;
        element3BoundingRect.top = 10;

        positionRegistry.dispose();

        expect(positionRegistry.getRepositionedComponents().length).toEqualData(0);
    });
});
