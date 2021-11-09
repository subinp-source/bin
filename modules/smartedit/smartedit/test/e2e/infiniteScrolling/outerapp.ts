/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file

import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import {
    InfiniteScrollingModule,
    SeDowngradeComponent,
    SeEntryModule,
    SmarteditBootstrapGateway
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <ng-container *ngIf="isReady">
                <div id="default-container">
                    <input id="mask" name="mask" placeholder="Enter mask" [(ngModel)]="mask" />
                    <button id="scrollToBottom" (click)="scrollToBottom()">Scroll to bottom</button>

                    <se-infinite-scrolling
                        #seinifnitescrolling
                        dropDownContainerClass="container-class"
                        dropDownClass="holder-class"
                        [pageSize]="pageSize"
                        [mask]="mask"
                        [fetchPage]="fetchPage"
                        [context]="context"
                    >
                        <div *ngFor="let item of context.items" class="item" [attr.id]="item.name">
                            {{ item.id }} : {{ item.name }}
                        </div>
                    </se-infinite-scrolling>
                </div>

                <immediate-check></immediate-check>
            </ng-container>
        </div>
    `
})
export class AppRootComponent {
    public pageSize: number = 10;
    public mask: string = '';
    public context: { items: any[] } = { items: [] };
    public fetchPage: (mask: string, pageSize: number, currentPage: number) => Promise<any>;

    public isReady = false;

    constructor(http: HttpClient, smarteditBootstrapGateway: SmarteditBootstrapGateway) {
        this.fetchPage = getFetchPage(http);

        // Workaround:
        // For some reason in e2e, styles loaded in header are not applied when InifniteScrolling Component calls ngAfterViewInit.
        // Those styles are required for calculating heights for Immediate Check.
        // We wait for smartedit to make sure that styles are applied at this point.
        smarteditBootstrapGateway.getInstance().subscribe('smartEditReady', () => {
            this.isReady = true;
        });
    }

    public scrollToBottom(): void {
        const container = document.querySelector('.se-infinite-scrolling__container ');

        container.scrollTop = container.scrollHeight;
    }
}

@Component({
    selector: 'immediate-check',
    template: `
        <div id="immediate-check-container">
            <se-infinite-scrolling
                dropDownContainerClass="container-class"
                dropDownClass="holder-class"
                [pageSize]="pageSize"
                [mask]="mask"
                [fetchPage]="fetchPage"
                [context]="context"
            >
                <ng-container *ngFor="let item of context.items; let i = index">
                    <div *ngIf="i > showItemsAboveIndex" class="item" [attr.id]="item.name">
                        {{ item.id }} : {{ item.name }}
                    </div>
                </ng-container>
            </se-infinite-scrolling>
        </div>
    `
})
export class ImmediateCheckComponent {
    public pageSize: number = 10;
    public mask: string = '';
    public context: { items: any[] } = { items: [] };
    public fetchPage: (mask: string, pageSize: number, currentPage: number) => Promise<any>;

    public showItemsAboveIndex = 7;

    constructor(http: HttpClient) {
        this.fetchPage = getFetchPage(http);
    }
}

function getFetchPage(http: HttpClient) {
    return (mask: string, pageSize: number, currentPage: number) => {
        return http
            .post('/loadItems', {
                pageSize,
                currentPage,
                mask
            })
            .toPromise();
    };
}

@SeEntryModule('OuterappModule')
@NgModule({
    imports: [CommonModule, InfiniteScrollingModule, FormsModule, HttpClientModule],
    declarations: [AppRootComponent, ImmediateCheckComponent],
    entryComponents: [AppRootComponent, ImmediateCheckComponent]
})
export class OuterappModule {}
