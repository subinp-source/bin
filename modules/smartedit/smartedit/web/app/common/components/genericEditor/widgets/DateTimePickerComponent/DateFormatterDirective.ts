/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { Directive, ElementRef, Input, Renderer2 } from '@angular/core';
import { NgModel } from '@angular/forms';
import { DatePipe } from '@angular/common';

import { DATE_CONSTANTS } from 'smarteditcommons/utils';

/**
 * The date formatter is for displaying the date in the desired format.
 * You can pass the desired format in the attributes of this directive and it will be shown.
 * It is  used with the <input> tag as we cant use date filter with it.
 * for eg- <input type='text' dateFormatter formatType="short">
 * format-type can be short, medium etc.
 * If the format-type is not given in the directive template, by default it uses the short type
 */
@Directive({
    selector: '[ngModel][dateFormatter]',
    providers: [NgModel, DatePipe]
})
export class DateFormatterDirective {
    @Input() formatType?: string;

    constructor(
        private ngModel: NgModel,
        private element: ElementRef,
        private renderer: Renderer2,
        private datePipe: DatePipe
    ) {}

    ngOnInit() {
        this.ngModel.valueChanges.subscribe((value: string) => {
            const patchedValue = this.datePipe.transform(
                value,
                this.formatType || DATE_CONSTANTS.ANGULAR_FORMAT
            );

            if (patchedValue !== value) {
                this.renderer.setProperty(
                    this.element.nativeElement as HTMLInputElement,
                    'value',
                    patchedValue
                );
            }
        });
    }
}
