/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslationModule } from '@smart/utils';
import { FormsModule } from '@angular/forms';

import { SelectModule } from '../../../components/select/SelectModule';
import { ShortStringComponent } from './ShortString/ShortStringComponent';
import { LongStringComponent } from './LongStringComponent/LongStringComponent';
import { RichTextFieldModule } from './RichTextField';
import { BooleanModule } from './BooleanComponent';
import { DateTimePickerModule } from './DateTimePickerComponent';
import { EnumComponent, EnumItemPrinterComponent } from './EnumComponent/EnumComponent';
import {
    DropdownComponent,
    DropdownItemPrinterComponent
} from './DropdownComponent/DropdownComponent';
import { FloatModule } from './FloatComponent';
import { NumberModule } from './NumberComponent';
import { EmailModule } from './EmailComponent';

@NgModule({
    imports: [
        CommonModule,
        DateTimePickerModule,
        BooleanModule,
        TranslationModule.forChild(),
        FormsModule,
        RichTextFieldModule,
        SelectModule,
        FloatModule,
        NumberModule,
        EmailModule
    ],
    declarations: [
        ShortStringComponent,
        LongStringComponent,
        EnumComponent,
        EnumItemPrinterComponent,
        DropdownComponent,
        DropdownItemPrinterComponent
    ],
    entryComponents: [
        ShortStringComponent,
        LongStringComponent,
        EnumComponent,
        EnumItemPrinterComponent,
        DropdownComponent,
        DropdownItemPrinterComponent
    ]
})
export class GenericEditorWidgetModule {}
