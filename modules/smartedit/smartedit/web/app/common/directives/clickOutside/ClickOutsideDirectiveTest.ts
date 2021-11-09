/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ClickOutsideDirective } from './ClickOutsideDirective';
import { ElementRef } from '@angular/core';
import { IIframeClickDetectionService } from '../../services/interfaces';

describe('Click Outside Directive', () => {
    let directive: ClickOutsideDirective;
    let nativeElement: HTMLElement;
    let nativeElementChild: HTMLElement;
    let host: ElementRef;
    let iframeClickDetectionService: jasmine.SpyObj<IIframeClickDetectionService>;

    beforeEach(() => {
        nativeElement = document.createElement('div');
        nativeElementChild = document.createElement('span');

        nativeElement.appendChild(nativeElementChild);
        host = { nativeElement };

        iframeClickDetectionService = jasmine.createSpyObj('iframeClickDetectionService', [
            'registerCallback',
            'removeCallback'
        ]);

        directive = new ClickOutsideDirective(host, iframeClickDetectionService);
    });

    it('Document click will return undefined when target is a host element', () => {
        const emit = spyOn(directive.clickOutside, 'emit');

        expect(directive.onDocumentClick(nativeElement)).toBeUndefined();
        expect(emit).not.toHaveBeenCalled();
    });

    it('Document click will return undefined when target is a child of the host', () => {
        const emit = spyOn(directive.clickOutside, 'emit');

        expect(directive.onDocumentClick(nativeElementChild)).toBeUndefined();
        expect(emit).not.toHaveBeenCalled();
    });

    it('Document click will trigger event to be emitted if target is not a host or a child of the host', () => {
        const emit = spyOn(directive.clickOutside, 'emit');

        directive.onDocumentClick(document.createElement('p'));
        expect(emit).toHaveBeenCalled();
    });

    it('registers event emitting callback on init', () => {
        directive.ngOnInit();

        expect(iframeClickDetectionService.registerCallback).toHaveBeenCalled();
    });

    it('removes event emitting callback on destroy', () => {
        directive.ngOnDestroy();

        expect(iframeClickDetectionService.removeCallback).toHaveBeenCalled();
    });
});
