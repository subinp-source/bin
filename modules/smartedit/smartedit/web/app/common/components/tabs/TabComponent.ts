/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    InjectionToken,
    Injector,
    Input,
    OnChanges,
    SimpleChanges
} from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { isEqual } from 'lodash';

import { SeDowngradeComponent } from '../../di';
import { Tab } from './TabsComponent';

export const TAB_DATA = new InjectionToken('tab-data');

/* forbiddenNameSpaces useValue:false */

/**
 * @ngdoc component
 * @name SeTabs.component:TabComponent
 * @scope
 * @element se-tab
 *
 * @description
 * The component  responsible for wrapping the content of a tab within a
 * {@link SeTabs.component:TabsComponent TabsComponent} component.
 *
 * @param {Object} tab Object defining tab contents.
 * @param {Object} model Custom data. Neither the se-tabs component or the se-tab component
 * can modify this value. The tabs' contents determine how to parse and use this object.
 *
 */

/**
 * @ngdoc interface
 * @name SeTabs.interfaces:TabData
 *
 * @description
 * A data to be injected by {@link SeTabs.component:TabComponent TabComponent} to child component
 */

export interface TabData<T = {}> {
    model: T;
    tabId: string;
    tab: Tab;
}

@SeDowngradeComponent()
@Component({
    selector: 'se-tab',
    templateUrl: './TabComponent.html'
})
export class TabComponent<T> implements OnChanges {
    @Input() tab: Tab;
    @Input() model: T;

    public scopeStream: BehaviorSubject<{
        model: T;
        tabId: string;
        tab: Tab;
    }> = new BehaviorSubject(null);
    public tabInjector: Injector;

    constructor(private injector: Injector) {}

    ngOnChanges(changes: SimpleChanges) {
        const modelChanged: boolean =
            changes.model && !isEqual(changes.model.previousValue, changes.model.currentValue);
        const tabChanged: boolean =
            changes.tab && !isEqual(changes.tab.previousValue, changes.tab.currentValue);

        if (tabChanged || modelChanged) {
            this.scopeStream.next({ model: this.model, tabId: this.tab.id, tab: this.tab });
        }
    }

    ngOnInit() {
        if (!this.isLegacyTab) {
            this.tabInjector = Injector.create({
                providers: [
                    {
                        provide: TAB_DATA,
                        useValue: { model: this.model, tabId: this.tab.id, tab: this.tab }
                    }
                ],
                parent: this.injector
            });
        }
    }

    public get isLegacyTab(): boolean {
        return !!this.tab.templateUrl;
    }
}
