/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { annotationService, GatewayProxied } from 'smarteditcommons';
import { IframeClickDetectionService } from 'smarteditcontainer/services';

describe('outer IframeClickDetectionService', () => {
    let iframeClickDetectionService: IframeClickDetectionService;
    beforeEach(() => {
        iframeClickDetectionService = new IframeClickDetectionService();
    });

    describe('initialization', () => {
        it('checks GatewayProxied', () => {
            expect(
                annotationService.getClassAnnotation(IframeClickDetectionService, GatewayProxied)
            ).toEqual(['onIframeClick']);
        });
    });

    it('GIVEN callback WHEN I call registerCallback THEN it will return removeCallback function', () => {
        // GIVEN
        const mockCallback = function() {
            return;
        };

        // WHEN
        const removeCallback = iframeClickDetectionService.registerCallback('cbId', mockCallback);

        // THEN
        expect(removeCallback).toEqual(jasmine.any(Function));
    });

    describe('removeCallback', () => {
        it('GIVEN callback has been registered WHEN I call removeCallback THEN it will remove the callback', () => {
            // GIVEN
            const mockCallback = function() {
                return;
            };
            iframeClickDetectionService.registerCallback('cbId', mockCallback);

            // WHEN
            const actual = iframeClickDetectionService.removeCallback('cbId');

            // THEN
            expect(actual).toBeTruthy();
        });

        it('WHEN I call removeCallback for not registered callback THEN it will return false', () => {
            // WHEN
            const actual = iframeClickDetectionService.removeCallback('notexistingcallback');

            // THEN
            expect(actual).toBeFalsy();
        });

        it('GIVEN callback has been called and then removed WHEN I call onIframeClick THEN it should not be called', () => {
            // GIVEN
            const mockCallback = jasmine.createSpy('mockCallback');
            iframeClickDetectionService.registerCallback('cbId1', mockCallback);
            iframeClickDetectionService.onIframeClick();
            iframeClickDetectionService.removeCallback('cbId1');

            // WHEN
            iframeClickDetectionService.onIframeClick();

            // THEN
            expect(mockCallback).toHaveBeenCalledTimes(1);
        });
    });

    it('GIVEN callbacks has been registered WHEN I call onIframeClick THEN it will call these callbacks', () => {
        // GIVEN
        const mockCallback1 = jasmine.createSpy('mockCallback1');
        const mockCallback2 = jasmine.createSpy('mockCallback2');
        iframeClickDetectionService.registerCallback('cbId1', mockCallback1);
        iframeClickDetectionService.registerCallback('cbId2', mockCallback2);

        // WHEN
        iframeClickDetectionService.onIframeClick();

        // THEN
        expect(mockCallback1).toHaveBeenCalledTimes(1);
        expect(mockCallback2).toHaveBeenCalledTimes(1);

        // WHEN
        iframeClickDetectionService.onIframeClick();

        // THEN
        expect(mockCallback1).toHaveBeenCalledTimes(2);
        expect(mockCallback2).toHaveBeenCalledTimes(2);
    });
});
