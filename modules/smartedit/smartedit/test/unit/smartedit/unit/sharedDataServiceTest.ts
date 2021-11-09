/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SharedDataService } from 'smartedit/services';
import { annotationService, GatewayProxied } from 'smarteditcommons';

describe('test sharedDataService', function() {
    let sharedDataService: SharedDataService;

    beforeEach(() => {
        sharedDataService = new SharedDataService();
    });

    it('set function is left empty to enable proxying', () => {
        expect(sharedDataService.set).toBeEmptyFunction();
    });

    it('get function is left empty to enable proxying', () => {
        expect(sharedDataService.get).toBeEmptyFunction();
    });

    it('checks GatewayProxied', () => {
        expect(annotationService.getClassAnnotation(SharedDataService, GatewayProxied)).toEqual([]);
    });
});
