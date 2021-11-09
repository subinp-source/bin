/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input } from '@angular/core';
import { Placement } from 'popper.js';

import { SeDowngradeComponent } from '../../di';

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:HelpComponent
 * @element se-help
 *
 * @description
 * This component will generate a help button that will show a customizable popover on top of it when hovering.
 * It relies on the {@link TooltipComponent TooltipComponent}.
 *
 * @param {String} template the HTML body to be used in the popover body, it will automatically be trusted by the directive. Optional but exactly one of either template or templateUrl must be defined.
 * @param {String} templateUrl the location of the HTML template to be used in the popover body. Optional but exactly one of either template or templateUrl must be defined.
 * @param {String} title the title to be used in the popover title section. Optional.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-help',
    templateUrl: './HelpComponent.html'
})
export class HelpComponent {
    @Input() title: string;
    @Input() template: string;
    @Input() templateUrl: string;

    public trigger: string[] = ['mouseover'];
    public placement: Placement = 'auto';
}
