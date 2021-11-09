/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { UpgradeModule } from '@angular/upgrade/static';
import { Component, ElementRef, Injector } from '@angular/core';
import { SeModuleConstructor, SMARTEDITLOADER_COMPONENT_NAME } from 'smarteditcommons';
import { Smarteditloader } from './legacySmarteditloader';

const legacyLoaderTagName = 'legacy-loader';

@Component({
    selector: SMARTEDITLOADER_COMPONENT_NAME,
    template: `<${legacyLoaderTagName}></${legacyLoaderTagName}>`
})
export class SmarteditloaderComponent {
    constructor(public elementRef: ElementRef, public upgrade: UpgradeModule, injector: Injector) {}

    ngAfterViewInit() {
        this.upgrade.bootstrap(
            this.elementRef.nativeElement.querySelector(legacyLoaderTagName),
            [(Smarteditloader as SeModuleConstructor).moduleName],
            { strictDi: false }
        );
    }
}
