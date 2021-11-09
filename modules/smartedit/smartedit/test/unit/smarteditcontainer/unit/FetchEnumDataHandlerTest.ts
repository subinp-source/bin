/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FetchEnumDataHandler, IRestService, RestServiceFactory } from 'smarteditcommons';

describe('fetchEnumDataHandler', () => {
    let fetchEnumDataHandler: FetchEnumDataHandler;
    let enumRestService: jasmine.SpyObj<IRestService<any>>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;

    const field = {
        cmsStructureEnumType: 'de.mypackage.Orientation'
    } as any;

    const data = [
        {
            code: 'code1',
            label: 'Vertical'
        },
        {
            code: 'code2',
            label: 'Horizontal'
        }
    ];

    beforeEach(() => {
        enumRestService = jasmine.createSpyObj<IRestService<any>>('enumRestService', ['get']);
        enumRestService.get.and.returnValue(
            Promise.resolve({
                enums: data
            })
        );

        restServiceFactory = jasmine.createSpyObj<RestServiceFactory>('restServiceFactory', [
            'get'
        ]);
        restServiceFactory.get.and.returnValue(enumRestService);

        fetchEnumDataHandler = new FetchEnumDataHandler(restServiceFactory);

        FetchEnumDataHandler.resetForTests();
    });

    it('GIVEN enum REST call succeeds WHEN I findByMask with no mask, promise resolves to the full list', (done) => {
        // WHEN
        const promise = fetchEnumDataHandler.findByMask(field);

        // THEN
        promise.then((result: any) => {
            expect(result).toEqual(data);
            done();
        });
    });

    it('GIVEN enum REST call succeeds WHEN I findByMask with a mask, promise resolves to the relevant filtered list', (done) => {
        // WHEN
        const promise = fetchEnumDataHandler.findByMask(field, 'zo');

        // THEN

        promise.then((result: any) => {
            expect(result).toEqual([
                {
                    code: 'code2',
                    label: 'Horizontal'
                }
            ]);
            done();
        });
    });

    it('GIVEN a first search, second uses cache', (done) => {
        // WHEN
        fetchEnumDataHandler
            .findByMask(field, 'zo')
            .then(() => fetchEnumDataHandler.findByMask(field, 'zon'))
            .then(() => {
                expect(enumRestService.get.calls.count()).toBe(1);
                done();
            });
    });
});
