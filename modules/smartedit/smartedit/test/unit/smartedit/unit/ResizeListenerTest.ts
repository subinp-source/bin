/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { ResizeListener } from 'smartedit/services';

describe('resizeListener', () => {
    let resizeListener: ResizeListener;
    const element1: HTMLElement = document.createElement('div');
    const element2: HTMLElement = document.createElement('div');
    const element3: HTMLElement = document.createElement('div');
    let listener1: jasmine.SpyObj<() => void>;
    let listener2: jasmine.SpyObj<() => void>;
    let listener3: jasmine.SpyObj<() => void>;

    beforeEach(() => {
        listener1 = jasmine.createSpy('listener1');
        listener2 = jasmine.createSpy('listener2');
        listener3 = jasmine.createSpy('listener3');
    });

    beforeEach(() => {
        resizeListener = new ResizeListener();
    });

    afterEach(() => {
        resizeListener.dispose();
    });

    it('will register element', () => {
        resizeListener.register(element1, listener1);
        resizeListener.register(element2, listener2);
        resizeListener.register(element3, listener3);
        expect(resizeListener._listenerCount()).toBe(3);
    });

    it('will unregister element', () => {
        const unregisterSpy = spyOn(resizeListener, 'unregister');
        resizeListener.unregister(element1);

        expect(unregisterSpy).toHaveBeenCalledWith(element1);
        expect(resizeListener._listenerCount()).toBe(0);
    });

    it('dispose will empty registry', () => {
        resizeListener.register(element1, listener1);
        resizeListener.register(element2, listener2);
        resizeListener.register(element3, listener3);
        const unregisterSpy = spyOn(resizeListener, 'unregister');
        resizeListener.dispose();

        expect(unregisterSpy.calls.count()).toEqual(3);
        expect(unregisterSpy).toHaveBeenCalledWith(element1);
        expect(unregisterSpy).toHaveBeenCalledWith(element2);
        expect(unregisterSpy).toHaveBeenCalledWith(element3);
    });
});
