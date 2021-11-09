/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { IRestOptions, Page, Pageable, Payload } from 'smarteditcommons';
import { DelegateRestService, RestService } from 'smartedit/services';

describe('test RestService ', () => {
    class DTO {}
    const payload = {} as Payload;
    const options = { headers: { key: 'value' } } as IRestOptions;
    const pageable: Pageable = {} as Pageable;

    let restService: RestService<DTO>;
    let delegateRestServiceMock: jasmine.SpyObj<DelegateRestService>;
    const uri: string = 'theuri';
    const identifier = 'theidentifier';

    const singleInstancePromise = Promise.resolve<DTO>('singInstancePromise');
    const arrayPromise = Promise.resolve<DTO[]>(['arrayPromise']);
    const pagePromise = Promise.resolve<Page<DTO>>(('pagePromise' as any) as Page<DTO>);
    const voidPromise = Promise.resolve();

    beforeEach(() => {
        delegateRestServiceMock = jasmine.createSpyObj<DelegateRestService>('delegateRestService', [
            'delegateForSingleInstance',
            'delegateForArray',
            'delegateForPage'
        ]);
        delegateRestServiceMock.delegateForSingleInstance.and.callFake((methodName: string) => {
            if (methodName === 'remove') {
                return voidPromise;
            }
            return singleInstancePromise;
        });
        delegateRestServiceMock.delegateForArray.and.returnValue(arrayPromise);
        delegateRestServiceMock.delegateForPage.and.returnValue(pagePromise);
        restService = new RestService<DTO>(delegateRestServiceMock, uri, identifier);
    });

    it('getById delegates to delegateForSingleInstance', () => {
        expect(restService.getById('myid', options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'getById',
            'myid',
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.getById('myid', options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'getById',
            'myid',
            uri,
            identifier,
            true,
            options
        );
    });

    it('get delegates to delegateForSingleInstance', () => {
        expect(restService.get(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'get',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.get(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'get',
            payload,
            uri,
            identifier,
            true,
            options
        );
    });

    it('update delegates to delegateForSingleInstance', () => {
        expect(restService.update(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'update',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.update(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'update',
            payload,
            uri,
            identifier,
            true,
            options
        );
    });

    it('patch delegates to delegateForSingleInstance', () => {
        expect(restService.patch(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'patch',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.patch(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'patch',
            payload,
            uri,
            identifier,
            true,
            options
        );
    });

    it('save delegates to delegateForSingleInstance', () => {
        expect(restService.save(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'save',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.save(payload, options)).toBe(singleInstancePromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'save',
            payload,
            uri,
            identifier,
            true,
            options
        );
    });

    it('query delegates to delegateForSingleInstance', () => {
        expect(restService.query(payload, options)).toBe(arrayPromise);
        expect(delegateRestServiceMock.delegateForArray).toHaveBeenCalledWith(
            'query',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
    });

    it('page delegates to delegateForPage', () => {
        expect(restService.page(pageable, options)).toBe(pagePromise);
        expect(delegateRestServiceMock.delegateForPage).toHaveBeenCalledWith(
            pageable,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.page(pageable, options)).toBe(pagePromise);
        expect(delegateRestServiceMock.delegateForPage).toHaveBeenCalledWith(
            pageable,
            uri,
            identifier,
            true,
            options
        );
    });

    it('remove delegates to delegateForSingleInstance', () => {
        expect(restService.remove(payload, options)).toBe(voidPromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'remove',
            payload,
            uri,
            identifier,
            false,
            options
        );
        restService.activateMetadata();
        expect(restService.remove(payload, options)).toBe(voidPromise);
        expect(delegateRestServiceMock.delegateForSingleInstance).toHaveBeenCalledWith(
            'remove',
            payload,
            uri,
            identifier,
            true,
            options
        );
    });
});
