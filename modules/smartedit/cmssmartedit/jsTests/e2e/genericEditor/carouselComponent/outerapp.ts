/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';

import { SeEntryModule, SeRouteService } from 'smarteditcommons';
import { CommonModule } from '@angular/common';
import { Routes } from '@angular/router';
import { UpgradeModule } from '@angular/upgrade/static';
import { BackendMocksUtils } from '../../util/outerBackendMocksUtils';

@Component({
    selector: 'carousel-app-root',
    styles: [
        `
            .y-add-btn {
                height: inherit !important;
            }
        `
    ],
    template: `
        <h1>Carousel Component Test</h1>

        <div>
            <button id="openEditorBtn" value="Open Editor" (click)="openEditor()">Editor</button>
        </div>
    `
})
export class CarouselComponentAppRootComponent {
    carouselComponent: any = null;
    componentConfiguration: { componentType: string; componentId: string } = {
        componentType: 'ProductCarouselComponent',
        componentId: null
    };

    constructor(private upgrade: UpgradeModule) {}

    saveOrUpdateComponent(method: any, url: any, data: any, headers: any) {
        const payload = JSON.parse(data);
        payload.uid = 'carouselComponent_1';

        this.carouselComponent = payload;
        this.carouselComponent.onlyOneRestrictionMustApply = false;
        this.carouselComponent.restrictions = [];

        return [201];
    }

    // Information about the component in the generic editor.

    openEditor() {
        const componentId = this.carouselComponent ? this.carouselComponent.uid : null;
        const componentAttributes = {
            smarteditComponentType: this.componentConfiguration.componentType,
            smarteditComponentUuid: componentId,
            catalogVersionUuid: 'somecatalogId/someCatalogVersion'
        };

        this.editorModalService.open(componentAttributes);
    }

    ngOnInit() {
        this.backendMocksUtils
            .getBackendMock('componentPUTMock')
            .respond(this.saveOrUpdateComponent.bind(this));
        this.backendMocksUtils
            .getBackendMock('componentPOSTMock')
            .respond(this.saveOrUpdateComponent.bind(this));

        const componentGETMock = this.backendMocksUtils.getBackendMock('componentGETMock');

        componentGETMock.respond((method, url, data, headers) => {
            const uid = /items\/(.*)/.exec(url)[1];

            if (uid !== this.carouselComponent.uid) {
                return [400];
            } else {
                return [200, this.carouselComponent];
            }
        });
    }

    private get backendMocksUtils(): BackendMocksUtils {
        return this.upgrade.$injector.get('backendMocksUtils');
    }

    private get editorModalService(): any {
        return this.upgrade.$injector.get('editorModalService');
    }
}

const routes: Routes = [
    {
        path: 'ng/carousel',
        component: CarouselComponentAppRootComponent
    }
];

// tslint:disable-next-line:max-classes-per-file
@SeEntryModule('CarouselComponentModule')
@NgModule({
    imports: [
        CommonModule,
        SeRouteService.provideNgRoute(routes, { useHash: true, initialNavigation: true })
    ],
    declarations: [CarouselComponentAppRootComponent],
    entryComponents: [CarouselComponentAppRootComponent]
})
export class CarouselComponentModule {}

window.pushModules(CarouselComponentModule);
