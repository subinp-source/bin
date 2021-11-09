/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    diBridgeUtils,
    moduleUtils,
    AbstractDecorator,
    CrossFrameEventService,
    COMPONENT_CLASS,
    IDecoratorService,
    ID_ATTRIBUTE,
    IExperience,
    IFeatureService,
    ILegacyDecoratorToCustomElementConverter,
    IPositionRegistry,
    IResizeListener,
    ISmartEditContractChangeListener,
    JQueryUtilsService,
    SeCustomComponent,
    SeDecorator,
    SeEntryModule,
    TYPE_ATTRIBUTE,
    UUID_ATTRIBUTE,
    YJQUERY_TOKEN
} from 'smarteditcommons';

const CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS = {
    root: null as HTMLElement,

    rootMargin: '1000px',

    threshold: 0
};

class GlobalService {
    public pageChangeMessage: string;
    public currentPageId: string;
}

@SeDecorator()
@Component({
    selector: 'deco-1',
    template:
        "<div style='background: rgba(0, 0, 255, .4); position:relative'><span>deco1 {{smarteditComponentId}} / {{componentAttributes.smarteditComponentId}}</span><ng-content></ng-content></div>"
})
export class Deco1DecoratorComponent extends AbstractDecorator {}

@SeDecorator()
@Component({
    selector: 'deco-2',
    template:
        "<div style='background: rgba(255, 0, 0, .4); position:relative'><span>deco2 {{smarteditComponentId}} / {{componentAttributes.smarteditComponentId}}</span><ng-content></ng-content></div>"
})
export class Deco2DecoratorComponent extends AbstractDecorator {}

@SeDecorator()
@Component({
    selector: 'deco-3',
    template:
        "<div style='background: rgba(0, 255, 0, .4); position:relative'><span>deco3 {{smarteditComponentId}} / {{componentAttributes.smarteditComponentId}}</span><ng-content></ng-content></div>"
})
export class Deco3DecoratorComponent extends AbstractDecorator {}

@SeDecorator()
@Component({
    selector: 'deco-4',
    template:
        "<div style='background: rgba(0, 0, 255, .4); position:relative'><span>deco4 {{smarteditComponentId}} / {{componentAttributes.smarteditComponentId}}</span><ng-content></ng-content></div>"
})
export class Deco4DecoratorComponent extends AbstractDecorator {}

@SeCustomComponent()
@Component({
    template: '<span>{{getPageChangeMessage()}}</span>'
})
export class PageChangeTestComponent {
    constructor(private global: GlobalService) {}

    getPageChangeMessage() {
        return this.global.pageChangeMessage;
    }
}

@SeCustomComponent()
@Component({
    selector: 'e-debugger',
    template: `
        <button class="btn btn-info btn-sm toggleDebug" (click)="toggleDebug()">
            <span *ngIf="!showDebug">open</span><span *ngIf="showDebug">close</span> debug
        </button>
        <div *ngIf="showDebug">
            <pre>Total SmartEdit components in page: <div id="total-store-front-components"><strong>{{getTotalSmartEditComponents()}}</strong></div></pre>
            <pre>Total visible SmartEdit components: <div id="total-visible-store-front-components"><strong>{{getTotalVisibleSmartEditComponents()}}</strong></div><span *ngIf="false">{{getVisibleSmartEditComponents() | json}}</span></pre>
            <pre>Total decorators: <div id="total-decorators"><strong>{{getTotalDecorators()}}</strong></div></pre>
            <pre>Total sakExecutor stored elements:<div id="total-sak-executor-elements"><strong>{{getTotalSakExecutorElements()}}</strong></div><span *ngIf="false">{{getSakExecutorElements() | json}}</span></pre>
            <pre>Total resize eventlisteners in DOM:<div id="total-resize-listeners"><strong>{{getTotalResizeEventListeners()}}</strong></div></pre>
            <pre>Total position eventlisteners in DOM:<div id="total-reposition-listeners"><strong>{{getTotalPositionEventListeners()}}</strong></div></pre>
            <pre>Total components in queue:<div id="total-components-queue"><strong>{{getTotalComponentsQueue()}}</strong></div></pre>
            <pre><span id="healthStatus">{{assertAllGood() ? "OK" : "ERROR (possible leak)"}}</span></pre>
        </div>
    `
})
export class SakExecutorDebugComponent {
    private visibleElements: Element[] = [];
    private inPageElements: Element[] = [];
    private intersectionObserver: IntersectionObserver;
    private showDebug = true;
    private smartEditAttributeNames: string[];

    constructor(
        private legacyDecoratorToCustomElementConverter: ILegacyDecoratorToCustomElementConverter,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private resizeListener: IResizeListener,
        private positionRegistry: IPositionRegistry,
        private smartEditContractChangeListener: ISmartEditContractChangeListener,
        private jQueryUtilsService: JQueryUtilsService
    ) {
        this.smartEditAttributeNames = [TYPE_ATTRIBUTE, ID_ATTRIBUTE, UUID_ATTRIBUTE];
    }

    _isInExtendedViewPort(target: Element, isIntersecting: boolean) {
        return this.smartEditContractChangeListener.isExtendedViewEnabled()
            ? this.jQueryUtilsService.isInExtendedViewPort(target as HTMLElement)
            : isIntersecting;
    }

    toggleDebug() {
        this.showDebug = !this.showDebug;
    }

    $onInit() {
        this.showDebug = true;

        setInterval(() => {
            if (this.intersectionObserver) {
                this.intersectionObserver.disconnect();
            }

            this.intersectionObserver = new IntersectionObserver((entries) => {
                this.cleanVisibleElements();
                entries.filter((entry) => {
                    const index = this.visibleElements.indexOf(entry.target);
                    if (this._isInExtendedViewPort(entry.target, entry.isIntersecting)) {
                        if (index === -1) {
                            this.visibleElements.push(entry.target);
                        }
                    } else {
                        if (index !== -1) {
                            this.visibleElements.splice(index, 1);
                        }
                    }
                });

                if (this.smartEditContractChangeListener.isExtendedViewEnabled()) {
                    this.visibleElements
                        .filter((element) => {
                            return !this.jQueryUtilsService.isInExtendedViewPort(
                                element as HTMLElement
                            );
                        })
                        .forEach((element, index) => {
                            this.visibleElements.splice(index, 1);
                        });
                }
            }, CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS);

            this.inPageElements = Array.prototype.slice.apply(this.yjQuery('.' + COMPONENT_CLASS));
            this.inPageElements.forEach((element) => {
                this.intersectionObserver.unobserve(element);
                this.intersectionObserver.observe(element);
            });
        }, 500);
    }

    /**
     * Removes all elements from the visibleElements that dont have smartedit attributes.
     * This can happen when a smartedit component was converted to a simple element by removing smartedit attributes.
     */
    cleanVisibleElements(): void {
        const index = this.visibleElements.findIndex((element) => {
            return this.smartEditAttributeNames.every((attributeName) => {
                return !element.hasAttribute(attributeName);
            });
        });
        this.visibleElements.splice(index, 1);
    }

    getTotalDecorators(): number {
        return this.yjQuery('.se-decorator-wrap').length;
    }

    getTotalSmartEditComponents(): number {
        return this.yjQuery('.' + COMPONENT_CLASS).length;
    }

    getTotalVisibleSmartEditComponents() {
        return this.visibleElements.length;
    }

    getVisibleSmartEditComponents() {
        return this.visibleElements
            .map((element) => {
                return this.yjQuery(element).attr(ID_ATTRIBUTE);
            })
            .sort();
    }

    getTotalSakExecutorElements() {
        return (
            AbstractDecorator.getScopes().length +
            this.legacyDecoratorToCustomElementConverter.getScopes().length
        );
    }

    getSakExecutorElements() {
        const arr: string[] = [];
        arr.concat(AbstractDecorator.getScopes());
        arr.concat(this.legacyDecoratorToCustomElementConverter.getScopes());
        return arr.sort();
    }

    getTotalResizeEventListeners() {
        return (this.resizeListener as any)._listenerCount();
    }

    getTotalPositionEventListeners() {
        return (this.positionRegistry as any)._listenerCount();
    }

    getTotalComponentsQueue() {
        return this.smartEditContractChangeListener._componentsQueueLength();
    }

    assertAllGood() {
        const totalVisibleElements = this.getTotalVisibleSmartEditComponents();

        return (
            this.getTotalDecorators() >= this.getTotalSakExecutorElements() &&
            totalVisibleElements <= this.getTotalPositionEventListeners() &&
            totalVisibleElements <= this.getTotalResizeEventListeners() &&
            this.getTotalPositionEventListeners() === this.getTotalResizeEventListeners() &&
            this.getTotalComponentsQueue() <= this.getTotalSmartEditComponents()
        );
    }
}

@SeEntryModule('InnerSECCLDecoratorsModule')
@NgModule({
    imports: [CommonModule],
    declarations: [
        Deco1DecoratorComponent,
        Deco2DecoratorComponent,
        Deco3DecoratorComponent,
        Deco4DecoratorComponent,
        SakExecutorDebugComponent,
        PageChangeTestComponent
    ],
    entryComponents: [
        Deco1DecoratorComponent,
        Deco2DecoratorComponent,
        Deco3DecoratorComponent,
        Deco4DecoratorComponent,
        SakExecutorDebugComponent,
        PageChangeTestComponent
    ],
    exports: [],
    providers: [
        GlobalService,
        diBridgeUtils.upgradeProvider('$rootScope'),
        moduleUtils.bootstrap(
            (
                global: GlobalService,
                decoratorService: IDecoratorService,
                crossFrameEventService: CrossFrameEventService,
                featureService: IFeatureService
            ) => {
                crossFrameEventService.subscribe(
                    'PAGE_CHANGE',
                    (eventId: string, experience: IExperience) => {
                        global.pageChangeMessage = 'paged changed to ' + experience.pageId;
                        global.currentPageId = experience.pageId;
                        return Promise.resolve();
                    }
                );

                decoratorService.addMappings({
                    componentType1: ['deco1', 'deco2', 'deco3', 'deco4'],
                    componentType2: ['deco2'],
                    ContentSlot: ['deco3']
                });

                decoratorService.enable('deco1');
                decoratorService.enable('deco2');
                decoratorService.enable('deco3');

                featureService.addDecorator({
                    key: 'deco4',
                    nameI18nKey: 'deco4',
                    displayCondition() {
                        return Promise.resolve(global.currentPageId === 'demo_storefront_page_id');
                    }
                });
            },
            [GlobalService, IDecoratorService, CrossFrameEventService, IFeatureService]
        )
    ]
})
export class InnerSECCLDecoratorsModule {}
