/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component } from '@angular/core';

import { SeCustomComponent } from 'smarteditcommons';

/** @internal */
@SeCustomComponent()
@Component({
    selector: 'se-perspective-selector-hotkey-notification',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './PerspectiveSelectorHotkeyNotificationComponentTemplate.html'
})
export class PerspectiveSelectorHotkeyNotificationComponent {}
