/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeInjectable } from '../../di';
import { YPopupOverlayDirective } from './yPopupOverlayDirective';

/**
 * @ngdoc service
 * @name yPopupOverlayModule.service:yPopupOverlayUtilsDOMCalculations
 *
 * @description
 * Contains some {@link yPopupOverlayModule.directive:yPopupOverlay yPopupOverlay} helper functions for
 * calculating positions and sizes on the DOM
 */

@SeInjectable()
export class YPopupOverlayUtilsClickOrderService {
    private controllerRegistry: YPopupOverlayDirective[] = [];

    constructor(private $document: angular.IDocumentService, private $log: angular.ILogService) {}

    public register(instance: YPopupOverlayDirective): void {
        const index = this.getIndex(instance);

        if (index === -1) {
            if (this.controllerRegistry.length === 0) {
                angular
                    .element(this.$document[0].body)
                    .on('click', (event: Event) => this.clickHandler(event));
            }

            this.controllerRegistry = [instance, ...this.controllerRegistry];
        } else {
            this.$log.warn(
                'yPopupOverlayUtilsClickOrderService.onHide() - instance already registered'
            );
        }
    }

    public unregister(instance: YPopupOverlayDirective): void {
        const index = this.getIndex(instance);

        if (index !== -1) {
            this.controllerRegistry.splice(index, 1);
        }

        if (this.controllerRegistry.length === 0) {
            angular
                .element(this.$document[0].body)
                .off('click', (event: Event) => this.clickHandler(event));
        }
    }

    private clickHandler($event: Event): void {
        if (this.controllerRegistry.length > 0) {
            this.controllerRegistry[0].onBodyElementClicked($event);
        }
    }

    private getIndex(instance: YPopupOverlayDirective): number {
        return this.controllerRegistry.findIndex(
            (entry: YPopupOverlayDirective) => entry.uuid === instance.uuid
        );
    }
}
