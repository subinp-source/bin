/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
/* forbiddenNameSpaces angular.module:false */
import { Component, NgModule } from '@angular/core';
import { FormsModule, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { FormModule } from '@fundamental-ngx/core';
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import {
    CollapsibleContainerConfig,
    COLLAPSIBLE_DEFAULT_CONFIGURATION,
    SeDowngradeComponent,
    SeEntryModule,
    SharedComponentsModule
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <form id="control-panel" style="width: 200px; padding: 20px" [formGroup]="form">
                <h4>Icon Alignment</h4>

                <div fd-form-set>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="iconAlignment-1">
                            <input
                                fd-form-control
                                formControlName="iconAlignment"
                                value="left"
                                type="radio"
                                id="iconAlignment-1"
                            />Left
                        </label>
                    </div>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="iconAlignment-2">
                            <input
                                fd-form-control
                                formControlName="iconAlignment"
                                value="right"
                                type="radio"
                                id="iconAlignment-2"
                            />Right
                        </label>
                    </div>
                </div>

                <hr />

                <h4>Icon Display</h4>

                <div fd-form-set>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="iconVisible-1">
                            <input
                                fd-form-control
                                formControlName="iconVisible"
                                [value]="true"
                                type="radio"
                                id="iconVisible-1"
                            />True
                        </label>
                    </div>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="iconVisible-2">
                            <input
                                fd-form-control
                                formControlName="iconVisible"
                                [value]="false"
                                type="radio"
                                id="iconVisible-2"
                            />False
                        </label>
                    </div>
                </div>
                <hr />

                <h4>Title provided</h4>

                <div fd-form-set>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="title-text">
                            <input
                                fd-form-control
                                formControlName="title"
                                value="text"
                                type="radio"
                                id="title-text"
                            />Text
                        </label>
                    </div>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="title-html">
                            <input
                                fd-form-control
                                formControlName="title"
                                value="html"
                                type="radio"
                                id="title-html"
                            />Html
                        </label>
                    </div>
                    <div fd-form-item [isCheck]="true">
                        <label fd-form-label for="title-none">
                            <input
                                fd-form-control
                                formControlName="title"
                                value="none"
                                type="radio"
                                id="title-none"
                            />None
                        </label>
                    </div>
                </div>

                <hr />

                <button id="button-reset" (click)="reset()">Reset</button>
            </form>

            <div id="tested-element" style="width: 500px">
                <pre>{{ configuration | json }}</pre>

                <se-collapsible-container [configuration]="configuration">
                    <se-collapsible-container-header>
                        <div [ngSwitch]="form.get('title').value">
                            <div *ngSwitchCase="'text'"><div id="header-text">Some Text</div></div>

                            <div *ngSwitchCase="'html'">
                                <div id="header-html">
                                    <span style="background-color: green">
                                        Header with custom html
                                    </span>
                                </div>
                            </div>
                        </div>
                    </se-collapsible-container-header>
                    <se-collapsible-container-content>
                        <div id="content" style="height: 500px; background-color: red">
                            Collapsible Content
                        </div>
                    </se-collapsible-container-content>
                </se-collapsible-container>
            </div>
        </div>
    `
})
export class AppRootComponent {
    public form: FormGroup = new FormGroup({
        iconAlignment: new FormControl('right'),
        iconVisible: new FormControl(true),
        title: new FormControl('text')
    });
    public configuration: CollapsibleContainerConfig = COLLAPSIBLE_DEFAULT_CONFIGURATION;

    ngOnInit() {
        this.getConfiguration().subscribe(
            (configuration: CollapsibleContainerConfig) => (this.configuration = configuration)
        );
    }

    public getConfiguration(): Observable<CollapsibleContainerConfig> {
        return this.form.valueChanges.pipe(
            map(() => ({
                expandedByDefault: false,
                iconVisible: this.form.get('iconVisible').value,
                iconAlignment: this.form.get('iconAlignment').value
            }))
        );
    }

    public reset(): void {
        this.form.reset({ ...COLLAPSIBLE_DEFAULT_CONFIGURATION, title: 'text' });
    }
}

@SeEntryModule('collapsibleApp')
@NgModule({
    imports: [SharedComponentsModule, CommonModule, FormsModule, FormModule, ReactiveFormsModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class CollapsibleAppNg {}
