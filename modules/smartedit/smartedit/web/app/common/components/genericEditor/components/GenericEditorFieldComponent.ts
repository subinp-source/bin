/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    forwardRef,
    Component,
    ElementRef,
    Inject,
    InjectionToken,
    Injector,
    Input,
    OnDestroy,
    ViewChild
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Payload } from '@smart/utils';

import { GenericEditorComponent } from '../GenericEditorComponent';
import { GenericEditorField, GenericEditorWidgetData, IGenericEditor } from '../types';
import { SeDowngradeComponent } from '../../../di';
import { INTERNAL_PROP_NAME } from '../models';

/* forbiddenNameSpaces useValue:false */
/* @internal  */
export interface GenericEditorFieldComponentScope<T = {}, D = {}>
    extends GenericEditorWidgetData<D> {
    editor?: IGenericEditor;
    id: string;
    editorStackId: string;
    // should never have been used but now some widgets use it...
    $ctrl?: GenericEditorFieldComponent<T>;
}

export const GENERIC_EDITOR_WIDGET_DATA = new InjectionToken('GENERIC_EDITOR_WIDGET_DATA');

@SeDowngradeComponent()
@Component({
    selector: 'se-generic-editor-field',
    providers: [
        {
            provide: NG_VALUE_ACCESSOR,
            useExisting: forwardRef(() => GenericEditorFieldComponent),
            multi: true
        }
    ],
    templateUrl: './GenericEditorFieldComponent.html'
})
export class GenericEditorFieldComponent<T> implements ControlValueAccessor, OnDestroy {
    @Input() field: GenericEditorField;
    @Input() model: Payload;
    @Input() qualifier: string;
    @Input() id: string;
    @ViewChild('widget', { read: ElementRef, static: false }) geWidget: ElementRef;

    public widgetInjector: Injector;
    private _onChange: (value: Payload) => void;
    private _onTouched: () => void;
    private _unWatch: () => void;

    constructor(
        private elementRef: ElementRef,
        private injector: Injector,
        @Inject(forwardRef(() => GenericEditorComponent)) public ge: GenericEditorComponent
    ) {}

    writeValue(value: Payload): void {
        this.model[this.qualifier] = value;
    }

    registerOnChange(fn: (value: Payload) => void): void {
        this._onChange = fn;
    }

    registerOnTouched(fn: () => void): void {
        this._onTouched = fn;
    }

    ngOnInit() {
        this.createInjector();

        this._unWatch = (this.model[INTERNAL_PROP_NAME] as any).watch(
            this.qualifier,
            (value: any) => {
                if (this._onChange && this._onTouched) {
                    this._onTouched();
                    this._onChange(value);
                }
            }
        );
    }

    ngOnDestroy() {
        this._unWatch();
    }

    ngAfterViewInit() {
        if (!this.field.template) {
            return;
        }

        const element = document.createElement('se-template-ge-widget') as Partial<
            GenericEditorFieldComponentScope
        >;

        this.elementRef.nativeElement.editor = this.ge.editor;
        this.elementRef.nativeElement.model = this.model;
        this.elementRef.nativeElement.field = this.field;
        this.elementRef.nativeElement.qualifier = this.qualifier;
        this.elementRef.nativeElement.id = this.id;
        this.elementRef.nativeElement.editorStackId = this.ge.editor.editorStackId;
        this.elementRef.nativeElement.isFieldDisabled = () => this.isFieldDisabled();
        this.elementRef.nativeElement.$ctrl = this;
        this.geWidget.nativeElement.appendChild(element);
    }
    /**
     * @internal
     *
     * This method is used to check if the field is disabled. If the field is not localized,
     * then it's the same as field.enabled. However, if the field is localized, then this
     * method will return a different result for each language. For example, this allows to
     * have 'en' disabled but 'fr' disabled, depending on language permissions.
     *
     */
    isFieldDisabled(): boolean {
        let isEnabled = this.field.editable;

        if (this.field.localized) {
            isEnabled = this.field.editable && this.field.isLanguageEnabledMap[this.qualifier];
        }

        return !isEnabled;
    }

    private createInjector(): void {
        this.widgetInjector = Injector.create({
            parent: this.injector,
            providers: [
                {
                    provide: GENERIC_EDITOR_WIDGET_DATA,
                    useValue: {
                        field: this.field,
                        model: this.model,
                        editor: this.ge.editor,
                        qualifier: this.qualifier,
                        isFieldDisabled: () => this.isFieldDisabled()
                    }
                }
            ]
        });
    }
}
