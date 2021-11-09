/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Directive,
    ElementRef,
    EventEmitter,
    HostListener,
    OnDestroy,
    OnInit,
    Output
} from '@angular/core';

import { IIframeClickDetectionService } from '../../services/interfaces/IIframeClickDetectionService';
import { stringUtils } from '../../utils/StringUtils';

@Directive({
    selector: '[seClickOutside]'
})
export class ClickOutsideDirective implements OnInit, OnDestroy {
    @Output() clickOutside: EventEmitter<void> = new EventEmitter<void>();

    private readonly id: string = `clickOutsideIframeClick${stringUtils.generateIdentifier()}`;

    constructor(
        private host: ElementRef,
        private iframeClickDetectionService: IIframeClickDetectionService
    ) {}

    @HostListener('document:click', ['$event.target']) onDocumentClick(target: HTMLElement) {
        if (target === this.host.nativeElement || this.host.nativeElement.contains(target)) {
            return;
        }

        this.clickOutside.emit();
    }

    ngOnInit() {
        this.iframeClickDetectionService.registerCallback(this.id, () => this.onOutsideClick());
    }

    ngOnDestroy() {
        this.iframeClickDetectionService.removeCallback(this.id);
    }

    private onOutsideClick(): void {
        this.clickOutside.emit();
    }
}
