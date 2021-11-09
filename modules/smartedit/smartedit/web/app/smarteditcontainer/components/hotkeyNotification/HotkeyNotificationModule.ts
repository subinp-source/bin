/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TranslationModule } from 'smarteditcommons';

import { HotkeyNotificationComponent } from './HotkeyNotificationComponent';
import { PerspectiveSelectorHotkeyNotificationComponent } from './perspectiveSelectorHotkey/PerspectiveSelectorHotkeyNotificationComponent';

/** @internal */
@NgModule({
    imports: [CommonModule, TranslationModule.forChild()],
    declarations: [HotkeyNotificationComponent, PerspectiveSelectorHotkeyNotificationComponent],
    entryComponents: [HotkeyNotificationComponent, PerspectiveSelectorHotkeyNotificationComponent]
})
export class HotkeyNotificationModule {}
