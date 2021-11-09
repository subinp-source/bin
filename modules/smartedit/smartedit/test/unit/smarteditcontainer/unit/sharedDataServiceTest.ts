/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SharedDataService } from 'smarteditcontainer/services';
import { annotationService, GatewayProxied } from 'smarteditcommons';

describe('test sharedDataService', function() {
    let sharedDataService: SharedDataService;

    beforeEach(() => {
        sharedDataService = new SharedDataService();
    });

    it('shared data service should validate get and set method', function() {
        sharedDataService.set('catalogVersion', '1.4');
        (expect(sharedDataService.get('catalogVersion')) as any).toBeResolvedWithData('1.4');
    });

    it('shared data service should override the value for a given key', function() {
        sharedDataService.set('catalogVersion', '1.4');
        sharedDataService.set('catalogVersion', '1.6');
        (expect(sharedDataService.get('catalogVersion')) as any).toBeResolvedWithData('1.6');
    });

    it('shared data service should check the object saved for a given key', function() {
        const obj = {
            catalog: 'apparel-ukContentCatalog',
            catalogVersion: '1.4'
        };

        sharedDataService.set('obj', obj);
        (expect(sharedDataService.get('obj')) as any).toBeResolvedWithData(obj);
    });

    it('shared data service should set the value to null for a given key', function() {
        sharedDataService.set('catalogVersion', '1.4');
        sharedDataService.set('catalogVersion', null);
        (expect(sharedDataService.get('catalogVersion')) as any).toBeResolvedWithData(null);
    });

    it('checks GatewayProxied', function() {
        expect(annotationService.getClassAnnotation(SharedDataService, GatewayProxied)).toEqual([]);
    });

    it('shared data service should remove the entry for the given key', function(done) {
        sharedDataService.set('catalogVersion', '1.4');

        sharedDataService.containsKey('catalogVersion').then((result) => {
            expect(result).toBe(true);

            sharedDataService.remove('catalogVersion');

            sharedDataService.containsKey('catalogVersion').then((result2) => {
                expect(result2).toBe(false);
                done();
            });
        });
    });

    it('shared data service should return false if the given key does not exist', function() {
        sharedDataService.containsKey('unknown').then((result) => {
            expect(result).toBe(false);
        });
    });
});
