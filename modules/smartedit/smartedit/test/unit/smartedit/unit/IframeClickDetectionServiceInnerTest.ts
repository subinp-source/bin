/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { annotationService, GatewayProxied } from 'smarteditcommons';
import { IframeClickDetectionService } from 'smartedit/services';

describe('inner IframeClickDetectionService', () => {
    describe('initialization', () => {
        it('checks GatewayProxied', () => {
            expect(
                annotationService.getClassAnnotation(IframeClickDetectionService, GatewayProxied)
            ).toEqual(['onIframeClick']);
        });
    });

    it('WHEN document has been clicked THEN it should call onIframeClick', () => {
        const _document = document.implementation.createHTMLDocument();

        let mousedownCallback: () => void;
        const addEventListenerSpy = spyOn(_document, 'addEventListener');
        addEventListenerSpy.and.callFake(
            (type: string, handler: () => void) => (mousedownCallback = handler)
        );

        const iframeClickDetectionService = new IframeClickDetectionService(_document);
        const onIframeClickSpy = spyOn(iframeClickDetectionService, 'onIframeClick');

        // WHEN
        mousedownCallback();

        // THEN
        expect(onIframeClickSpy).toHaveBeenCalledTimes(1);
    });
});
