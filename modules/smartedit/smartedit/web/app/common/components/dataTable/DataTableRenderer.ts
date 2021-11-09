/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useValue:false */
// tslint:disable:max-classes-per-file

import {
    ChangeDetectionStrategy,
    Component,
    Injector,
    Input,
    OnChanges,
    OnInit,
    SimpleChanges
} from '@angular/core';
import { TypedMap } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import { DynamicPagedListColumnKey } from '../dynamicPagedList';
import { DataTableConfig, DATA_TABLE_COMPONENT_DATA } from './DataTableComponent';
import { CompileHtmlNgController } from '../../directives';

@SeDowngradeComponent()
@Component({
    selector: 'se-data-table-renderer',
    changeDetection: ChangeDetectionStrategy.OnPush,
    template: `
        <div
            *ngIf="column.renderer"
            [seCompileHtml]="column.renderer(item, column)"
            [compileHtmlNgController]="legacyController"
            [scope]="{ item: item, column: column, $index: index }"
        ></div>
        <div *ngIf="!column.renderer && column.component">
            <ng-container
                *ngComponentOutlet="column.component; injector: rendererInjector"
            ></ng-container>
        </div>
        <span *ngIf="!column.renderer && !column.component">{{ item[column.property] }}</span>
    `
})
export class DataTableRendererComponent implements OnInit, OnChanges {
    @Input() column: DynamicPagedListColumnKey;
    @Input() item: TypedMap<any>;
    @Input() config: DataTableConfig;
    @Input() index: number;

    public rendererInjector: Injector;
    public legacyController: CompileHtmlNgController;

    constructor(private injector: Injector) {}

    ngOnInit() {
        this.setInjector();
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.column || changes.item) {
            this.setInjector();
        }

        if (changes.config) {
            this.setLegacyController();
        }
    }

    private setLegacyController(): void {
        const { config } = this;

        this.legacyController = {
            alias: '$ctrl',
            value: class {
                config: DataTableConfig = config;
            }
        };
    }

    private setInjector(): void {
        const { column, item } = this;

        this.rendererInjector = Injector.create({
            providers: [
                {
                    provide: DATA_TABLE_COMPONENT_DATA,
                    useValue: { column, item }
                }
            ],
            parent: this.injector
        });
    }
}
