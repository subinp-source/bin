/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MessageComponent } from './MessageComponent';
import { EventMessageComponent } from './EventMessage/EventMessageComponent';
import { TranslateModule } from '@ngx-translate/core';

/**
 * This module provides the se-message component, which is responsible for rendering contextual
 * feedback messages for the user actions.
 */
@NgModule({
    imports: [CommonModule, TranslateModule.forChild()],
    declarations: [MessageComponent, EventMessageComponent],
    entryComponents: [MessageComponent, EventMessageComponent],
    exports: [MessageComponent, EventMessageComponent]
})
export class MessageModule {}
