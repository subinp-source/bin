/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';
import { SeDowngradeComponent } from '../../di';

@SeDowngradeComponent()
@Component({
    selector: 'se-collapsible-container-header',
    template: `
        <ng-content></ng-content>
    `
})
export class CollapsibleContainerHeaderComponent {}
