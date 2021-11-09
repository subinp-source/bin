/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';

import { SeDowngradeComponent } from '../../../../di';
import { GenericEditorWidgetData } from '../../../genericEditor/types';
import { GENERIC_EDITOR_WIDGET_DATA } from '../../components/GenericEditorFieldComponent';
import { DEFAULT_GENERIC_EDITOR_FLOAT_PRECISION } from '../../services';

@SeDowngradeComponent()
@Component({
    selector: 'se-float',
    templateUrl: './FloatComponent.html'
})
export class FloatComponent {
    public precision: string = DEFAULT_GENERIC_EDITOR_FLOAT_PRECISION;

    constructor(@Inject(GENERIC_EDITOR_WIDGET_DATA) public widget: GenericEditorWidgetData<any>) {}
}
