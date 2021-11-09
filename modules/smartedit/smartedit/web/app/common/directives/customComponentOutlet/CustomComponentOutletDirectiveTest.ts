/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ElementRef, Renderer2, SimpleChange } from '@angular/core';

import { createSimulateNgOnChanges } from '../../../../../smartedit-build/test/unit/angularHelper';
import { CustomComponentOutletDirective } from './CustomComponentOutletDirective';

describe('CustomComponentOutletDirective', () => {
    let elementRef: jasmine.SpyObj<ElementRef>;
    let renderer: jasmine.SpyObj<Renderer2>;
    let customComponentOutletDirective: CustomComponentOutletDirective;
    type Input = Partial<Pick<typeof customComponentOutletDirective, 'componentName'>>;

    let simulateNgOnChanges: ReturnType<typeof createSimulateNgOnChanges>;
    beforeEach(() => {
        elementRef = jasmine.createSpyObj('elementRef', ['nativeElement']);
        renderer = jasmine.createSpyObj('renderer2', [
            'createElement',
            'appendChild',
            'removeChild'
        ]);
        renderer.createElement.and.callFake((selector: string) => {
            return document.createElement(selector);
        });
        spyOn(window.__smartedit__, 'getComponentDecoratorPayload').and.callFake(
            (componentName: string) => ({ selector: componentName })
        );

        customComponentOutletDirective = new CustomComponentOutletDirective(elementRef, renderer);
        simulateNgOnChanges = createSimulateNgOnChanges<Input>(customComponentOutletDirective);
    });

    describe('initial', () => {
        it('GIVEN no componentName WHEN initialize THEN it should not update the view', () => {
            simulateNgOnChanges({});

            expect(renderer.createElement).not.toHaveBeenCalled();
        });

        it('GIVEN componentName WHEN initialize THEN it should update the view', () => {
            simulateNgOnChanges({
                componentName: new SimpleChange(undefined, 'MyComponent', true)
            });

            expect(renderer.createElement).toHaveBeenCalled();
        });
    });

    it('GIVEN the view was created WHEN componentName changes THEN it should remove that view and create the new one', () => {
        simulateNgOnChanges({ componentName: new SimpleChange(undefined, 'MyComponent', true) });

        simulateNgOnChanges({
            componentName: new SimpleChange('MyComponent', 'MyComponentNew', false)
        });

        expect(renderer.removeChild.calls.argsFor(0)[1].tagName).toBe('MYCOMPONENT');
        expect(renderer.appendChild.calls.argsFor(1)[1].tagName).toBe('MYCOMPONENTNEW');
    });

    it('GIVEN the view was created WHEN componentName changes to empty value THEN it should remove that view', () => {
        simulateNgOnChanges({ componentName: new SimpleChange(undefined, 'MyComponent', true) });

        simulateNgOnChanges({ componentName: new SimpleChange('MyComponent', undefined, false) });

        expect(renderer.removeChild.calls.argsFor(0)[1].tagName).toBe('MYCOMPONENT');
    });
});
