/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import {
    annotationService,
    GatewayProxied,
    IRestService,
    Page,
    Pageable,
    Payload,
    RestServiceFactory
} from 'smarteditcommons';
import { DelegateRestService } from 'smarteditcontainer/services';

describe('test DelegateRestService ', () => {
    class DTO {}
    const payload = {} as Payload;
    const pageable = {} as Pageable;

    let delegateRestService: DelegateRestService;

    const uri: string = 'theuri';
    const identifier: string = 'theidentifier';
    const methodname: string = 'methodname';
    const metadataActivated = true;

    const restServiceFactory: jasmine.SpyObj<RestServiceFactory> = jasmine.createSpyObj<
        RestServiceFactory
    >('restServiceFactory', ['get']);
    const restService: jasmine.SpyObj<IRestService<DTO>> = jasmine.createSpyObj<IRestService<DTO>>(
        'iRestService',
        ['activateMetadata', 'getMethodForSingleInstance', 'getMethodForArray', 'page']
    );

    const voidMethod: jasmine.Spy = jasmine.createSpy('voidMethod');
    const singleInstanceMethod: jasmine.Spy = jasmine.createSpy('singleInstanceMethod');
    const arrayMethod: jasmine.Spy = jasmine.createSpy('arrayMethod');

    const singleInstancePromise = Promise.resolve<DTO>('singInstancePromise');
    const arrayPromise = Promise.resolve<DTO[]>('arrayPromise' as any);
    const pagePromise = Promise.resolve<Page<DTO>>('pagePromise' as any);
    const voidPromise = Promise.resolve<any>('voidpromise');

    beforeEach(() => {
        voidMethod.and.returnValue(voidPromise);
        singleInstanceMethod.and.returnValue(singleInstancePromise);
        arrayMethod.and.returnValue(arrayPromise);

        restService.getMethodForSingleInstance.and.returnValue(singleInstanceMethod);
        restService.getMethodForArray.and.returnValue(arrayMethod);
        restService.page.and.returnValue(pagePromise);

        restServiceFactory.get.and.returnValue(restService);

        delegateRestService = new DelegateRestService(restServiceFactory);
    });

    function assertOnCallToRestServiceFactory() {
        expect(restServiceFactory.get).toHaveBeenCalledWith(uri, identifier);
    }

    it('checks GatewayProxied', () => {
        expect(annotationService.getClassAnnotation(DelegateRestService, GatewayProxied)).toEqual(
            []
        );
    });

    it('delegateForSingleInstance delegates to getMethodForSingleInstance of the appropriate RestService', () => {
        expect(
            delegateRestService.delegateForSingleInstance(
                methodname,
                payload,
                uri,
                identifier,
                metadataActivated
            )
        ).toBe(singleInstancePromise);
        assertOnCallToRestServiceFactory();
    });

    it('delegateForArray delegates to getMethodForVoid of the appropriate RestService', () => {
        expect(
            delegateRestService.delegateForArray(
                methodname,
                payload,
                uri,
                identifier,
                metadataActivated
            )
        ).toBe(arrayPromise);
        assertOnCallToRestServiceFactory();
    });

    it('delegateForPage delegates to page of the appropriate RestService', () => {
        expect(
            delegateRestService.delegateForPage(pageable, uri, identifier, metadataActivated)
        ).toBe(pagePromise);
        assertOnCallToRestServiceFactory();
    });
});
