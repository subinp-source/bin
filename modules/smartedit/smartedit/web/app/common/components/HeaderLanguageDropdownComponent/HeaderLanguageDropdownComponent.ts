/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';
import { LanguageDropdown } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import { LanguageService } from '../../services/language/LanguageService';
import { CrossFrameEventService } from '../../services/crossFrame/CrossFrameEventService';

import './HeaderLanguageDropdownComponent.scss';

@SeDowngradeComponent()
@Component({
    selector: 'header-language-dropdown',
    template: `
        <ul role="menu" class="fd-menu__list se-language-selector">
            <li *ngIf="selectedLanguage">
                <a
                    class="yToolbarActions__dropdown-element fd-menu__item se-language-selector__element--selected"
                >
                    {{ selectedLanguage.name }}
                </a>
            </li>
            <ng-container *ngFor="let language of items">
                <li *ngIf="selectedLanguage.isoCode !== language.value.isoCode">
                    <a
                        class="yToolbarActions__dropdown-element fd-menu__item se-language-selector__element"
                        (click)="onSelectedLanguage(language)"
                    >
                        {{ language.value.name }}
                    </a>
                </li>
            </ng-container>
        </ul>
    `
})
export class HeaderLanguageDropdownComponent extends LanguageDropdown {
    constructor(
        public languageService: LanguageService,
        public crossFrameEventService: CrossFrameEventService
    ) {
        super(languageService, crossFrameEventService);

        this.languageSortStrategy = 'none' as any;
    }
}
