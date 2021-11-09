/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';

import { SeDowngradeComponent } from '../../../../di';
import { GenericEditorWidgetData } from '../../../genericEditor/types';
import { GENERIC_EDITOR_WIDGET_DATA } from '../../components/GenericEditorFieldComponent';

@SeDowngradeComponent()
@Component({
    selector: 'se-email',
    templateUrl: './EmailComponent.html'
})
export class EmailComponent {
    constructor(@Inject(GENERIC_EDITOR_WIDGET_DATA) public widget: GenericEditorWidgetData<any>) {}
}
