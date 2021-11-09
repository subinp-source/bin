/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    EventEmitter,
    Input,
    OnChanges,
    Output,
    SimpleChanges,
    Type
} from '@angular/core';
import { ISelectItem } from '@smart/utils';
import { BehaviorSubject, Observable } from 'rxjs';
import { distinctUntilChanged, map } from 'rxjs/operators';
import { isEqual } from 'lodash';

import { SeDowngradeComponent } from '../../di';
import { TabsSelectAdapter } from './TabsSelectAdapter';

export interface Tab {
    id: string;
    hasErrors: boolean;
    active?: boolean;
    message?: string;
    title: string;
    templateUrl?: string;
    component?: Type<any>;
}

/**
 * @ngdoc overview
 * @name SeTabs
 * @description
 *
 * The Tabs provide the components required to display a group of se-tab components within a se-tabs component. The
 * {@link SeTabs.component:TabsComponent TabsComponent} is of particular interest to SmartEdit developers
 * because this directive is responsible for displaying and organizing tabs.
 *
 */

/**
 * @ngdoc component
 * @name SeTabs.component:TabsComponent
 * @scope
 * @element se-tabs
 *
 * @description
 * The component responsible for displaying and organizing se-tab components within a se-tabs component. A specified number of tabs will
 * display a tab header. If there are more tabs than the maximum number defined, the remaining tabs will be grouped
 * in a drop-down menu with the header "More". When a user clicks on a tab header or an item from the drop-down
 * menu, the content of the tabs changes to the body of the selected tab.
 *
 * Note: The body of each tab is wrapped within a {@link SeTabs.component:TabComponent TabComponent} component.
 *
 * @param {Object} model Custom data to be passed to each tab. Neither the se-tabs component or the
 * se-tab component can modify this value. The tabs' contents determine how to parse and use this object.
 * @param {Object[]} tabsList A list that contains the configuration for each of the tabs to be displayed in the se-tabs.
 * @param {string} tabsList.id The ID used to track the tab within the tabs.
 * @param {String} tabsList.title The tab header.
 * @param {String} tabsList.templateUrl Path to the HTML fragment to be displayed as the tab content.
 *  Deprecated to maintain AngularJS functionality, use component instead.
 * @param {Object} tabsList.component Angular component to be dynamically injected, alternative to templateUrl
 * @param {boolean} tabsList.hasErrors Flag that indicates whether a visual error is to be displayed in the tab or not.
 * @param {Number} numTabsDisplayed The number of tabs for which tab headers will be displayed. The remaining tab
 * headers will be grouped within the 'MORE' drop-down menu.
 * @param {EventEmitter} onTabSelected Event trigger when the tab has been selected
 *
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-tabs',
    templateUrl: './TabsComponent.html'
})
export class TabsComponent<T> implements OnChanges {
    @Input() model: T;
    @Input() tabsList: Tab[] = [];
    @Input() numTabsDisplayed: number;

    @Output() onTabSelected: EventEmitter<string> = new EventEmitter<string>();

    public selectedTab: Tab;
    public selectItems: ISelectItem<Tab>[];
    public dropdownTabs: ISelectItem<Tab>[];

    private tabChangedStream: BehaviorSubject<void> = new BehaviorSubject(null);

    public get isInitialized(): boolean {
        return !!this.tabsList && this.tabsList.length > 1 && !!this.selectedTab;
    }

    isActiveInMoreTab(): boolean {
        return (
            this.tabsList.findIndex((tab) => tab.id === this.selectedTab.id) >=
            this.numTabsDisplayed - 1
        );
    }

    ngOnChanges(changes: SimpleChanges) {
        const hasTabsInitialized: boolean =
            (changes.tabsList &&
                changes.tabsList.currentValue &&
                !changes.tabsList.previousValue) ||
            (changes.tabsList && changes.tabsList.firstChange && !!changes.tabsList.currentValue);

        const hasTabsChanged: boolean =
            changes.tabsList && !!changes.tabsList.previousValue && !!changes.tabsList.currentValue;

        if (hasTabsInitialized) {
            this.selectedTab = this.tabsList.find((tab: Tab) => tab.active) || this.tabsList[0];
            this.tabsList = this.tabsList.map((tab) => ({
                ...tab,
                active: false,
                hasErrors: false
            }));
            this.selectedTab.active = true;
            this.tabChangedStream.next();
        }

        if (hasTabsChanged) {
            this.tabChangedStream.next();
        }

        this.getDropdownTabs().subscribe((tabs) => {
            this.dropdownTabs = tabs;
        });
    }

    selectTab(tabToSelect: Tab): void {
        if (tabToSelect && tabToSelect.id !== this.selectedTab.id) {
            if (!this.selectedTab.active) {
                // If a tab is made active 'manually' from outside this controller, there can be a mismatch.
                // This method finds the tab that is actually selected.
                this.findSelectedTab();
            }
            this.selectedTab.active = false;
            this.selectedTab = tabToSelect;
            this.selectedTab.active = true;

            this.onTabSelected.emit(this.selectedTab.id);
        }
    }

    dropDownHasErrors(): boolean {
        const tabsInDropDown = this.tabsList.slice(this.numTabsDisplayed - 1);

        return tabsInDropDown.some((tab: Tab) => {
            return tab.hasErrors;
        });
    }

    findSelectedTab(): void {
        const selectedTab = this.tabsList.find((tab: Tab) => {
            return tab.active;
        });

        if (selectedTab) {
            this.selectedTab = selectedTab;
        }
    }

    getDropdownTabs(): Observable<ISelectItem<Tab>[]> {
        return this.tabChangedStream.pipe(
            map(() =>
                (this.tabsList || [])
                    .slice(this.numTabsDisplayed - 1)
                    .map(TabsSelectAdapter.transform)
            ),
            distinctUntilChanged((a: ISelectItem<Tab>[], b: ISelectItem<Tab>[]) => isEqual(a, b))
        );
    }

    getVisibleTabs(): Observable<Tab[]> {
        return this.tabChangedStream.pipe(
            map(() => (this.tabsList || []).slice(0, this.numTabsDisplayed - 1)),
            distinctUntilChanged((a: Tab[], b: Tab[]) => isEqual(a, b))
        );
    }

    trackTabById(index: number): number {
        return index;
    }
}
