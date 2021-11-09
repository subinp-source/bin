/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { Timer } from './Timer';
import { noop } from 'lodash';
import { NgZone } from '@angular/core';

describe('timerModule', () => {
    const callback: () => void = () => noop;
    const duration: number = 3000;
    let zone: jasmine.SpyObj<NgZone>;

    beforeEach(() => {
        zone = jasmine.createSpyObj('zone', ['runOutsideAngular']);
        zone.runOutsideAngular.and.callFake((cb: () => void) => cb());
    });

    it('initializes with default values when only callback is passed', () => {
        const timer = new Timer(zone, callback);

        expect((timer as any)._callback).toBe(callback);
        expect((timer as any)._duration).toBe(1000);
        expect((timer as any)._timer).toBe(null);
    });

    it('initializes with the right values when all parameters are passed', () => {
        const timer = new Timer(zone, callback, duration);

        expect((timer as any)._callback).toBe(callback);
        expect((timer as any)._duration).toBe(duration);
        expect((timer as any)._timer).toBe(null);
    });

    it('isActive returns false when timer is not started', () => {
        const timer = new Timer(zone, callback, duration);

        expect(timer.isActive()).toBe(false);
    });

    it('isActive returns true when timer is started', () => {
        const timer = new Timer(zone, callback, duration);

        timer.start();
        expect(timer.isActive()).toBe(true);
    });

    it('start with a new duration will set the new duration', () => {
        const newDuration = 2000;
        const timer = new Timer(zone, callback, duration);

        expect((timer as any)._duration).toBe(duration);

        timer.start(newDuration);

        expect((timer as any)._duration).toBe(newDuration);
    });

    it('stop will stop the timer if the timer is already running', () => {
        const timer = new Timer(zone, callback, duration);

        timer.start();
        timer.stop();

        expect((timer as any)._timer).toEqual(null);
    });

    it('restart will stop and start the timer if the timer is already running', () => {
        const timer = new Timer(zone, callback, duration);

        spyOn(timer as any, '_callback').and.returnValue({});

        const stop = spyOn(timer, 'stop').and.callThrough();
        const start = spyOn(timer, 'start').and.callThrough();

        timer.start();

        timer.restart();

        expect(stop).toHaveBeenCalled();
        expect(start).toHaveBeenCalled();
        expect(start.calls.count()).toEqual(2);
    });

    it('teardown will stop the timer and reset all properties to null', () => {
        const timer = new Timer(zone, callback, duration);

        spyOn(timer as any, '_callback').and.returnValue({});
        spyOn(timer, 'stop').and.callThrough();

        timer.start();
        timer.teardown();

        expect(timer.stop).toHaveBeenCalled();
        expect((timer as any)._callback).toBe(null);
        expect((timer as any)._duration).toBe(null);
        expect((timer as any)._timer).toBe(null);
    });
});
