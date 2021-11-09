/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { FormsModule, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UpgradeModule } from '@angular/upgrade/static';

import {
    IAlertConfig,
    IAlertConfigLegacy,
    IAlertService,
    IAlertServiceType,
    SeDowngradeComponent,
    SeEntryModule
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div
            class="container"
            style="position: fixed; z-index: 1000; top: 150px; left: 0; width: 100vw; height: 100vh; background-color: white"
        >
            <h2>Show a system alert</h2>
            <div class="row">
                <div class="col-xs-6">
                    <form [formGroup]="form">
                        <div class="form-group">
                            <h5>Message</h5>
                            <textarea
                                id="test-alert-message"
                                class="form-control"
                                type="text"
                                name="alert-message"
                                formControlName="message"
                            ></textarea>
                        </div>
                        <div class="form-group">
                            <h5>Message Placeholder (evaluated to object)</h5>
                            <input
                                id="test-alert-message-placeholder"
                                class="form-control"
                                type="text"
                                name="message-placeholders"
                                formControlName="messagePlaceholders"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Type</h5>
                            <select
                                id="test-alert-type"
                                class="form-control"
                                name="mySelect"
                                formControlName="type"
                            >
                                <option *ngFor="let type of types" [value]="type">{{
                                    type
                                }}</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <h5>Closeable</h5>
                            <input
                                id="test-alert-closeable"
                                class="form-control"
                                type="checkbox"
                                name="closeable"
                                formControlName="closeable"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Timeout</h5>
                            <input
                                id="test-alert-timeout"
                                class="form-control"
                                type="number"
                                step="1"
                                name="timeout"
                                formControlName="timeout"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Template</h5>
                            <input
                                id="test-alert-template"
                                class="form-control"
                                type="text"
                                name="template"
                                formControlName="template"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Template Url</h5>
                            <input
                                id="test-alert-templateUrl"
                                class="form-control"
                                type="text"
                                name="templateUrl"
                                formControlName="templateUrl"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Controller (evaluated to function)</h5>
                            <input
                                id="test-alert-controller"
                                class="form-control"
                                type="text"
                                name="controller"
                                formControlName="controller"
                            />
                        </div>
                        <div class="form-group">
                            <h5>Create multiple (not for automation)</h5>
                            <input
                                class="form-control"
                                type="number"
                                step="1"
                                name="count"
                                formControlName="count"
                            />
                        </div>
                    </form>
                </div>
            </div>
            <button
                id="test-alert-add-button-angular"
                class="btn btn-primary"
                (click)="addAngularAlert()"
            >
                Show Angular Alert
            </button>
            <button
                id="test-alert-add-button-angularjs"
                class="btn btn-primary"
                (click)="addLegacyAlert()"
            >
                Show Legacy Alert
            </button>
            <button id="test-alert-reset-button" class="btn btn-secondary" (click)="reset()">
                Reset
            </button>
        </div>
    `
})
export class AppRootComponent {
    public form: FormGroup = new FormGroup({
        message: new FormControl(undefined),
        type: new FormControl(IAlertServiceType.INFO),
        messagePlaceholders: new FormControl(),
        closeable: new FormControl(true),
        count: new FormControl(1),
        timeout: new FormControl(),
        template: new FormControl(),
        templateUrl: new FormControl(),
        controller: new FormControl()
    });

    public types: IAlertServiceType[] = [
        IAlertServiceType.INFO,
        IAlertServiceType.WARNING,
        IAlertServiceType.DANGER,
        IAlertServiceType.SUCCESS
    ];

    constructor(private alertService: IAlertService, private upgrade: UpgradeModule) {}

    ngOnInit() {
        this.upgrade.$injector
            .get('$templateCache')
            .put(
                'AlertTestComponentTemplate.html',
                '<div class="message">The number is {{ "12345.12345" | number: 2 }}.</div>'
            );

        this.form.valueChanges.subscribe(() => {
            this.composeAngularConfig();
        });
    }

    public addAngularAlert(): void {
        this.showAlert(this.composeAngularConfig());
    }

    public addLegacyAlert(): void {
        this.showAlert(this.composeLegacyConfig());
    }
    public reset(): void {
        this.form.reset();
    }

    private composeLegacyConfig(): IAlertConfigLegacy {
        const placeholders = this.form.get('messagePlaceholders').value;
        const controller = this.form.get('controller').value;

        const config: IAlertConfigLegacy = {
            message: this.form.get('message').value || undefined,
            type: this.form.get('type').value,
            closeable: this.form.get('closeable').value,
            timeout: this.form.get('timeout').value,
            messagePlaceholders: placeholders ? JSON.parse(placeholders) : null,
            template: this.form.get('template').value,
            templateUrl: this.form.get('templateUrl').value
        };

        if (controller) {
            config.controller = new Function(`return ${controller}`)();
        }
        return config;
    }
    private composeAngularConfig(): IAlertConfig {
        const placeholders = this.form.get('messagePlaceholders').value;

        return {
            message: this.form.get('message').value,
            type: this.form.get('type').value,
            dismissible: this.form.get('closeable').value,
            duration: this.form.get('timeout').value,
            messagePlaceholders: placeholders ? JSON.parse(placeholders) : null
        };
    }

    private get count(): number {
        return this.form.get('count').value || 1;
    }

    private showAlert(config: IAlertConfig | IAlertConfigLegacy): void {
        for (let i = 0; i < this.count; i++) {
            this.alertService.showAlert(config);
        }
    }
}

// tslint:disable-next-line:max-classes-per-file
@SeEntryModule('OuterappModule')
@NgModule({
    imports: [CommonModule, FormsModule, ReactiveFormsModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class OuterappModule {}
