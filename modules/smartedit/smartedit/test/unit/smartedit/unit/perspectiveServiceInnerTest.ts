/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PerspectiveService } from 'smartedit/services';

import { annotationService, GatewayProxied } from 'smarteditcommons';

describe('inner perspectiveService', () => {
    let perspectiveService: PerspectiveService;
    beforeEach(() => {
        perspectiveService = new PerspectiveService();
    });

    it('checks GatewayProxied', () => {
        expect(annotationService.getClassAnnotation(PerspectiveService, GatewayProxied)).toEqual(
            []
        );
    });

    it('register is left unimplemented', () => {
        expect(perspectiveService.register).toBeEmptyFunction();
    });

    it('isEmptyPerspectiveActive is left unimplemented', () => {
        expect(perspectiveService.isEmptyPerspectiveActive).toBeEmptyFunction();
    });
});
