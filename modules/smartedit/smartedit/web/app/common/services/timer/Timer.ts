/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name timerModule
 *
 * @description
 * A module that provides a Timer object that can invoke a callback after a certain period of time.
 * * {@link timerModule.service:Timer Timer}
 */

import { NgZone } from '@angular/core';

/**
 * @ngdoc service
 * @name timerModule.service:Timer
 *
 * @description
 * A `Timer` must be instanciated calling **`timerService.createTimer()`**.
 * This `Timer` service uses native setInterval function and adds additional functions to it.
 *
 * @param {Function} callback The function that will be invoked upon timeout.
 * @param {Number} [duration=1000] The number of milliseconds to wait before the callback is invoked.
 */

export class Timer {
    // Keeps the interval reference. This will only be non-null when the
    // timer is actively counting down to callback invocation.

    private _timer: number = null;

    constructor(
        private zone: NgZone,
        private _callback: () => void,
        private _duration: number = 1000
    ) {}

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#isActive
     * @methodOf timerModule.service:Timer
     *
     * @description
     * This method can be used to know whether or not the timer is currently active (counting down).
     *
     * @returns {Boolean} Returns true if the timer is active (counting down).
     */
    isActive(): boolean {
        return !!this._timer;
    }

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#restart
     * @methodOf timerModule.service:Timer
     *
     * @description
     * Stops the timer, and then starts it again. If a new duration is given, the timer's duration will be set to that new value.
     *
     * @param {Number} [duration=The previously set duration] The new number of milliseconds to wait before the callback is invoked.
     */
    restart(duration?: number): void {
        this._duration = duration || this._duration;
        this.stop();
        this.start();
    }

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#start
     * @methodOf timerModule.service:Timer
     *
     * @description
     * Start the timer, which will invoke the callback upon timeout.
     *
     * @param {Number} [duration=The previously set duration] The new number of milliseconds to wait before the callback is invoked.
     */
    start(duration?: number): void {
        this._duration = duration || this._duration;
        this.zone.runOutsideAngular(() => {
            this._timer = (setInterval(() => {
                try {
                    if (this._callback) {
                        this._callback();
                    } else {
                        this.stop();
                    }
                } catch (e) {
                    this.stop();
                }
            }, this._duration) as unknown) as number;
        });
    }

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#stop
     * @methodOf timerModule.service:Timer
     *
     * @description
     * Stop the current timer, if it is running, which will prevent the callback from being invoked.
     */
    stop(): void {
        clearInterval(this._timer);
        this._timer = null;
    }

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#resetDuration
     * @methodOf timerModule.service:Timer
     *
     * @description
     * Sets the duration to a new value.
     *
     * @param {Number} [duration=The previously set duration] The new number of milliseconds to wait before the callback is invoked.
     */
    resetDuration(duration: number): void {
        this._duration = duration || this._duration;
    }

    /**
     * @ngdoc method
     * @name timerModule.service:Timer#teardown
     * @methodOf timerModule.service:Timer
     *
     * @description
     * Clean up the internal object references
     */
    teardown(): void {
        this.stop();
        this._callback = null;
        this._duration = null;
        this._timer = null;
    }
}
