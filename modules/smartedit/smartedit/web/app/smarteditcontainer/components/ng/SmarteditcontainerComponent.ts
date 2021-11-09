/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TranslateService } from '@ngx-translate/core';
import { Component, ElementRef, Injector } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import {
    registerCustomComponents,
    AngularJSBootstrapIndicatorService,
    SeModuleConstructor,
    SMARTEDITCONTAINER_COMPONENT_NAME,
    SMARTEDITLOADER_COMPONENT_NAME
} from 'smarteditcommons';
import { Smarteditcontainer } from 'smarteditcontainer/legacySmarteditcontainer';

const legacyAppName = 'legacyApp';
@Component({
    selector: SMARTEDITCONTAINER_COMPONENT_NAME,
    template: `
        <router-outlet></router-outlet>
        <div id="${legacyAppName}">
            <se-announcement-board></se-announcement-board>
            <se-notification-panel></se-notification-panel>
            <div ng-view></div>
        </div>
    `
})
export class SmarteditcontainerComponent {
    constructor(
        private translateService: TranslateService,
        injector: Injector,
        public upgrade: UpgradeModule,
        private elementRef: ElementRef,
        private bootstrapIndicator: AngularJSBootstrapIndicatorService
    ) {
        this.setApplicationTitle();
        registerCustomComponents(injector);
    }

    ngOnInit() {
        /*
         * for e2e purposes:
         * in e2e, we sometimes add some test code in the parent frame to be added to the runtime
         * since we only bootstrap within smarteditcontainer-component node,
         * this code will be ignored unless added into the component before legacy AnguylarJS bootstrapping
         */
        Array.prototype.slice
            .call(document.body.childNodes)
            .filter(
                (childNode: ChildNode) =>
                    !this.isAppComponent(childNode) && !this.isSmarteditLoader(childNode)
            )
            .forEach((childNode: ChildNode) => {
                this.legacyAppNode.appendChild(childNode);
            });
    }

    ngAfterViewInit() {
        this.upgrade.bootstrap(
            this.legacyAppNode,
            [(Smarteditcontainer as SeModuleConstructor).moduleName],
            { strictDi: false }
        );

        this.bootstrapIndicator.setSmarteditContainerReady();
    }

    private setApplicationTitle() {
        this.translateService.get('se.application.name').subscribe((pageTitle: string) => {
            document.title = pageTitle;
        });
    }

    private get legacyAppNode() {
        return this.elementRef.nativeElement.querySelector(`#${legacyAppName}`);
    }

    private isAppComponent(childNode: ChildNode) {
        return (
            childNode.nodeType === Node.ELEMENT_NODE &&
            (childNode as HTMLElement).tagName === SMARTEDITCONTAINER_COMPONENT_NAME.toUpperCase()
        );
    }

    private isSmarteditLoader(childNode: ChildNode) {
        return (
            childNode.nodeType === Node.ELEMENT_NODE &&
            ((childNode as HTMLElement).id === 'smarteditloader' ||
                (childNode as HTMLElement).tagName === SMARTEDITLOADER_COMPONENT_NAME.toUpperCase())
        );
    }
}
