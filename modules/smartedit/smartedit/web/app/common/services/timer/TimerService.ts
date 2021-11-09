/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from '../../di';
import { Timer } from './Timer';
import { Injectable, NgZone } from '@angular/core';

@SeDowngradeService()
@Injectable()
export class TimerService {
    constructor(private ngZone: NgZone) {}

    createTimer(callback: () => void, duration: number): Timer {
        return new Timer(this.ngZone, callback, duration);
    }
}
