/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslationModule } from '@smart/utils';

import { SliderPanelComponent } from './SliderPanelComponent';
import { SliderPanelServiceFactory } from './SliderPanelServiceFactory';
import { CompileHtmlModule } from '../../directives';

@NgModule({
    imports: [CommonModule, TranslationModule.forChild(), CompileHtmlModule],
    declarations: [SliderPanelComponent],
    entryComponents: [SliderPanelComponent],
    providers: [SliderPanelServiceFactory],
    exports: [SliderPanelComponent]
})
export class SliderPanelModule {}
