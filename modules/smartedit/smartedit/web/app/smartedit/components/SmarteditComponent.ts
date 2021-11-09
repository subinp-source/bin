/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { UpgradeModule } from '@angular/upgrade/static';
import { Component, ElementRef, Injector } from '@angular/core';
import {
    nodeUtils,
    registerCustomComponents,
    AngularJSBootstrapIndicatorService,
    SeModuleConstructor,
    SMARTEDIT_COMPONENT_NAME
} from 'smarteditcommons';
import { LegacySmartedit } from 'smartedit/legacySmartedit';

@Component({
    selector: SMARTEDIT_COMPONENT_NAME,
    template: ``
})
export class SmarteditComponent {
    constructor(
        private elementRef: ElementRef,
        public upgrade: UpgradeModule,
        injector: Injector,
        private angularJSBootstrapIndicatorService: AngularJSBootstrapIndicatorService
    ) {
        registerCustomComponents(injector);
    }

    ngAfterViewInit() {
        if (!nodeUtils.hasLegacyAngularJSBootsrap()) {
            const e2ePlaceHolderTagName = 'e2e-placeholder';
            if (document.querySelector(e2ePlaceHolderTagName)) {
                document.querySelector(e2ePlaceHolderTagName).childNodes.forEach((childNode) => {
                    if (childNode.nodeType === Node.ELEMENT_NODE) {
                        this.elementRef.nativeElement.appendChild(childNode);
                    }
                });
            }

            this.upgrade.bootstrap(
                this.elementRef.nativeElement,
                [(LegacySmartedit as SeModuleConstructor).moduleName],
                { strictDi: false }
            );

            this.angularJSBootstrapIndicatorService.setSmarteditReady();
        }
    }
}
