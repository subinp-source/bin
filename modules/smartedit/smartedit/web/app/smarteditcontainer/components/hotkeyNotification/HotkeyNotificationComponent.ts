/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

/**
 * This component renders the hotkey notification template which includes the required key(s),
 * a title and optional message.
 *
 * @internal
 */
@Component({
    selector: 'se-hotkey-notification',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './HotkeyNotificationComponentTemplate.html'
})
export class HotkeyNotificationComponent {
    @Input() hotkeyNames: string[];
    @Input() title: string;
    @Input() message: string;
}
