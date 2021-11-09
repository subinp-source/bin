/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';

import { SeDowngradeService, YJQUERY_TOKEN } from 'smarteditcommons';
import { ComponentHandlerService } from './ComponentHandlerService';

/**
 * Internal service
 *
 * Service that resizes slots and components in the Inner Frame when the overlay is enabled or disabled.
 */
@SeDowngradeService()
export class ResizeComponentService {
    constructor(
        private componentHandlerService: ComponentHandlerService,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic
    ) {}

    /**
     * This methods appends CSS classes to inner frame slots and components. Passing a boolean true to showResizing
     * enables the resizing, and false vice versa.
     */
    public resizeComponents(showResizing: boolean): void {
        const slots = this.yjQuery(this.componentHandlerService.getAllSlotsSelector());
        const components = this.yjQuery(this.componentHandlerService.getAllComponentsSelector());

        if (showResizing) {
            slots.addClass('ySEEmptySlot');
            components.addClass('se-storefront-component');
        } else {
            slots.removeClass('ySEEmptySlot');
            components.removeClass('se-storefront-component');
        }
    }
}
