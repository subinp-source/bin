/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    AngularJSLazyDependenciesService,
    IConfirmationModalService,
    IModalService,
    SeDowngradeComponent,
    SeEntryModule,
    SliderPanelModule
} from 'smarteditcommons';
import { IFundamentalModalConfig } from '@smart/utils/services/modal';
import { FormsModule } from '@angular/forms';

// tslint:disable:max-classes-per-file
/* forbiddenNameSpaces angular.module:false */

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <table id="angular" class="smartedit-testing-overlay">
            <tbody>
                <tr>
                    <td style="width:35%"></td>
                    <td style="width:20%"></td>
                    <td style="width:45%"></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button class="btn btn-default" (click)="sliderPanelShow1()">body</button>

                        <se-slider-panel
                            [(sliderPanelShow)]="sliderPanelShow1"
                            [(sliderPanelHide)]="sliderPanelHide1"
                            [sliderPanelConfiguration]="sliderPanelConfiguration1"
                        >
                            <ng-template>
                                <p *ngFor="let p of paragraphs">
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed
                                    interdum mi ut pellentesque scelerisque. Phasellus fermentum est
                                    sed luctus maximus. Sed porttitor lobortis molestie. Nullam vel
                                    imperdiet enim. Maecenas maximus, arcu non commodo rutrum, dolor
                                    augue posuere tellus, a iaculis libero mi ut arcu. Integer
                                    maximus pretium urna, ac hendrerit dui sollicitudin a. Morbi
                                    eget justo id augue feugiat placerat in vitae sem.
                                </p>
                            </ng-template>
                        </se-slider-panel>
                    </td>
                    <td>
                        <div id="withinDiv">
                            <button class="btn btn-default" (click)="sliderPanelShow5()">
                                Image in target 1
                            </button>
                            <button class="btn btn-default" (click)="sliderPanelShow6()">
                                Image in target 2
                            </button>

                            <se-slider-panel
                                [(sliderPanelShow)]="sliderPanelShow5"
                                [(sliderPanelHide)]="sliderPanelHide5"
                                [sliderPanelConfiguration]="sliderPanelConfiguration5"
                            >
                                <button
                                    type="button"
                                    class="se-modal-close"
                                    (click)="sliderPanelHide5()"
                                >
                                    <span class="hyicon hyicon-close"></span>
                                </button>
                                <div id="image-1"></div>
                            </se-slider-panel>

                            <se-slider-panel
                                [(sliderPanelShow)]="sliderPanelShow6"
                                [(sliderPanelHide)]="sliderPanelHide6"
                                [sliderPanelConfiguration]="sliderPanelConfiguration6"
                            >
                                <button
                                    type="button"
                                    class="se-modal-close"
                                    (click)="sliderPanelHide6()"
                                >
                                    <span class="hyicon hyicon-close"></span>
                                </button>
                                <div id="image-2"></div>
                            </se-slider-panel>

                            <div id="target1"><p>target1</p></div>

                            <div id="target2"><p>target2</p></div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <button id="button_openModal" class="btn btn-default" (click)="openModal()">
                            Open Modal
                        </button>
                    </td>
                    <td colspan="2">
                        <button class="btn btn-default" (click)="sliderPanelShow4()">
                            Show primary panel
                        </button>

                        <se-slider-panel
                            [(sliderPanelShow)]="sliderPanelShow4"
                            [(sliderPanelHide)]="sliderPanelHide4"
                            [sliderPanelConfiguration]="sliderPanelConfiguration4"
                        >
                            <p>This is the content of the primary slider panel</p>
                            <br />
                            <button class="btn btn-default" (click)="sliderPanelShow7()">
                                Show secondary panel
                            </button>
                        </se-slider-panel>

                        <se-slider-panel
                            [(sliderPanelShow)]="sliderPanelShow7"
                            [(sliderPanelHide)]="sliderPanelHide7"
                            [sliderPanelConfiguration]="sliderPanelConfiguration7"
                        >
                            <p>This is the content of the secondary slider panel</p>
                        </se-slider-panel>
                    </td>
                </tr>
                <tr>
                    <td>
                        <button
                            id="button_showSliderPanelTemplate"
                            class="btn btn-default"
                            (click)="sliderPanelShow8()"
                        >
                            showSliderPanelTemplate
                        </button>
                        <se-slider-panel
                            [(sliderPanelShow)]="sliderPanelShow8"
                            [(sliderPanelHide)]="sliderPanelHide8"
                            [sliderPanelConfiguration]="sliderPanelConfiguration8"
                        >
                        </se-slider-panel>
                    </td>
                    <td colspan="2">
                        <button
                            id="button_showSliderPanelTemplateUrl"
                            class="btn btn-default"
                            (click)="sliderPanelShow9()"
                        >
                            showSLiderPanelTemplateUrl
                        </button>
                        <se-slider-panel
                            [(sliderPanelShow)]="sliderPanelShow9"
                            [(sliderPanelHide)]="sliderPanelHide9"
                            [sliderPanelConfiguration]="sliderPanelConfiguration9"
                        >
                        </se-slider-panel>
                    </td>
                </tr>
            </tbody>
        </table>
    `
})
export class AppRootComponent implements OnInit {
    paragraphs = Array(4).fill(0);
    sliderPanelConfiguration1 = {
        modal: {
            showDismissButton: true,
            title: 'Header',
            cancel: {
                onClick: () => {
                    this.sliderPanelHide1();
                },
                label: 'Cancel'
            },
            save: {
                onClick: () => {
                    this.sliderPanelHide1();
                },
                label: 'Save'
            }
        }
    };

    sliderPanelConfiguration3 = {
        modal: {
            showDismissButton: true,
            title: 'Header',
            cancel: {
                onClick: () => {
                    this.sliderPanelHide3();
                },
                label: 'Cancel'
            },
            save: {
                onClick: () => {
                    this.sliderPanelHide3();
                },
                label: 'Save'
            }
        },
        slideFrom: 'top'
    };

    sliderPanelConfiguration4 = {
        modal: {
            showDismissButton: true,
            title: 'Primary panel'
        }
    };

    sliderPanelConfiguration5 = {
        cssSelector: '#target1'
    };

    sliderPanelConfiguration6 = {
        cssSelector: '#target2'
    };

    sliderPanelConfiguration7 = {
        modal: {
            showDismissButton: true,
            cancel: {
                onClick: () => {
                    this.sliderPanelHide7();
                },
                label: 'se.cms.component.confirmation.modal.cancel'
            },
            save: {
                onClick: () => {
                    this.sliderPanelHide7();
                },
                label: 'se.cms.component.confirmation.modal.save'
            },
            title: 'Awesome, a secondary panel!'
        }
    };

    sliderPanelConfiguration8 = {
        template: '<div class="message">{{message}}</div>',
        scope: {
            message: '#AngularJS template#'
        }
    };

    sliderPanelConfiguration9 = {
        templateUrl: 'SliderPanelContentTemplateUrlTest.html',
        scope: {
            message: '#AngularJS templateUrl#'
        }
    };

    public sliderPanelHide1: () => void;
    public sliderPanelHide2: () => void;
    public sliderPanelHide3: () => void;
    public sliderPanelHide4: () => void;
    public sliderPanelHide5: () => void;
    public sliderPanelHide6: () => void;
    public sliderPanelHide7: () => void;
    public sliderPanelHide8: () => void;
    public sliderPanelHide9: () => void;

    public sliderPanelShow1: () => void;
    public sliderPanelShow2: () => void;
    public sliderPanelShow3: () => void;
    public sliderPanelShow4: () => void;
    public sliderPanelShow5: () => void;
    public sliderPanelShow6: () => void;
    public sliderPanelShow7: () => void;
    public sliderPanelShow8: () => void;
    public sliderPanelShow9: () => void;

    constructor(
        private modalService: IModalService,
        private angularJSLazyDependenciesService: AngularJSLazyDependenciesService
    ) {
        this.sliderPanelHide1 = this.sliderPanelHide2 = this.sliderPanelHide3 = this.sliderPanelHide4 = this.sliderPanelHide5 = this.sliderPanelHide6 = this.sliderPanelHide7 = this.sliderPanelHide8 = this.sliderPanelHide9 = function() {
            //
        };
        this.sliderPanelShow1 = this.sliderPanelShow2 = this.sliderPanelShow3 = this.sliderPanelShow4 = this.sliderPanelShow5 = this.sliderPanelShow6 = this.sliderPanelShow7 = this.sliderPanelShow8 = this.sliderPanelShow9 = function() {
            //
        };
    }

    ngOnInit() {
        this.angularJSLazyDependenciesService
            .$templateCache()
            .put(
                'SliderPanelContentTemplateUrlTest.html',
                '<div class="message">{{ message }}</div>'
            );
    }

    openModal() {
        this.modalService.open({
            component: SliderPanelModalComponent,
            templateConfig: {
                title: 'Modal Title',
                isDismissButtonVisible: true
            },
            config: { focusTrapped: false }
        } as IFundamentalModalConfig<any>);
    }
}

@Component({
    selector: 'slider-panel-modal',
    template: `
        <div>
            <div>
                <button
                    id="button_showSliderPanel"
                    class="btn btn-default"
                    type="button"
                    (click)="sliderPanelShowModal()"
                >
                    Show Slider Panel
                </button>
            </div>

            <se-slider-panel
                [(sliderPanelShow)]="sliderPanelShowModal"
                [(sliderPanelHide)]="sliderPanelHideModal"
                [sliderPanelConfiguration]="sliderPanelConfigurationModal"
            >
                <form>
                    <p>
                        <span class="y-toggle y-toggle-lg">
                            <input
                                type="checkbox"
                                name="isDirtySwitch"
                                id="isDirtySwitch"
                                [(ngModel)]="isDirtyStatus"
                            />
                            <label
                                for="isDirtySwitch"
                                data-activelabel="On"
                                data-inactivelabel="Off"
                                >Switch isDirty</label
                            >
                        </span>
                    </p>
                    <p>
                        <span class="y-toggle y-toggle-lg">
                            <button
                                class="btn btn-default"
                                (click)="sliderPanelShowModalSecondary()"
                            >
                                Show secondary panel
                            </button>
                        </span>
                    </p>
                </form>

                <p *ngFor="let p of paragraphs">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed interdum mi ut
                    pellentesque scelerisque. Phasellus fermentum est sed luctus maximus. Sed
                    porttitor lobortis molestie. Nullam vel imperdiet enim. Maecenas maximus, arcu
                    non commodo rutrum, dolor augue posuere tellus, a iaculis libero mi ut arcu.
                    Integer maximus pretium urna, ac hendrerit dui sollicitudin a. Morbi eget justo
                    id augue feugiat placerat in vitae sem.
                </p>

                <img src="gliphy.gif" />
            </se-slider-panel>

            <se-slider-panel
                [(sliderPanelShow)]="sliderPanelShowModalSecondary"
                [(sliderPanelHide)]="sliderPanelHideModalSecondary"
                [sliderPanelConfiguration]="sliderPanelConfigurationModalSecondary"
            >
                <img src="gliphy.gif" />
            </se-slider-panel>
        </div>
    `
})
export class SliderPanelModalComponent {
    isDirtyStatus = false;
    sliderPanelShowModal: () => void;
    sliderPanelHideModal: () => void;
    sliderPanelShowModalSecondary: () => void;
    sliderPanelHideModalSecondary: () => void;
    paragraphs = Array(10).fill(0);
    sliderPanelConfigurationModal = {
        modal: {
            showDismissButton: true,
            cancel: {
                onClick: () => {
                    this.cancelSliderPanel();
                },
                label: 'Cancel'
            },
            save: {
                onClick: () => {
                    this.saveSliderPanel();
                },
                label: 'Save',
                isDisabledFn: () => {
                    return this.isSaveDisabled();
                }
            },
            dismiss: {
                onClick: () => {
                    this.dismissSliderPanel();
                }
            },
            title: 'Slider Panel Title'
        },
        cssSelector: '#y-modal-dialog'
    };

    sliderPanelConfigurationModalSecondary = {
        modal: {
            showDismissButton: true,
            title: 'Secondary Slider Panel'
        },
        cssSelector: '#y-modal-dialog'
    };

    constructor(private confirmationModalService: IConfirmationModalService) {}
    saveSliderPanel() {
        this.sliderPanelHideModal();
    }

    cancelSliderPanel() {
        this.sliderPanelHideModal();
    }

    dismissSliderPanel() {
        const message = {
            title: 'Dismiss',
            description: 'Do you want to dismiss ?'
        };

        (this.confirmationModalService.confirm(message) as Promise<any>).then(() => {
            this.sliderPanelHideModal();
        });
    }

    isSaveDisabled() {
        return !this.isDirtyStatus;
    }
}
@SeEntryModule('sliderPanelApp')
@NgModule({
    imports: [SliderPanelModule, CommonModule, FormsModule],
    declarations: [AppRootComponent, SliderPanelModalComponent],
    entryComponents: [AppRootComponent, SliderPanelModalComponent]
})
export class SliderPanelAppNg {}
